package com.mhacks.reencounter;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.ExternalUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;

public class ProfileCore {
    final static String webUrl   = HtmlUtilities.endpoint;
    final static String queryUrl = webUrl + "queryUserInfo.php";
    final String addContactUrl = webUrl + "addContact.php";
    
    protected static Intent profileIntent(Context c, String user, String pass, String infoUser) {
        Intent intent = new Intent(c, MessagingActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("pass", pass);
        intent.putExtra("infoUser", infoUser);
        return intent;
    }

    protected static JSONArray queryProfile(Bundle b) {
        String request = queryUrl(b);
        AndroidUtilities.log("Query Profile Request", request);
        return queryProfile(request);
    }

    private static String queryUrl(Bundle b) {
        String user     = b.getString("user");
        String pass     = b.getString("pass");
        String infoUser = b.getString("infoUser");
        int min = b.getInt("min");
        int max = b.getInt("max");
        String var = "?user="     + HtmlUtilities.enc(user)
                   + "&password=" + HtmlUtilities.enc(pass)
                   + "&infoUser=" + HtmlUtilities.enc(infoUser);
        return queryUrl + var;
    }

    private static JSONArray queryProfile(String request) {
        return HtmlUtilities.queryArray(request, "posts");
    }

    protected static View.OnClickListener callHandler(final Context c, final String phone) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                ExternalUtilities.callPhone(c, phone);
            }
        };
    }

    protected static View.OnClickListener emailHandler(final Context c, final String email, final String subject) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                ExternalUtilities.sendEmail(c, email, subject);
            }
        };
    }

    protected static View.OnClickListener messageHandler(final Context c, final String user, final String pass, final String infoUser) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                c.startActivity(MessagingCore.messagingIntent(c, user, pass, infoUser));
            }
        };
    }
    
    protected static View.OnClickListener viewEncountersHandler(final Context c, final String user, final String pass, final String otherUser) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(c, EncounterActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("pass", pass);
                intent.putExtra("otherUser", otherUser);
                c.startActivity(intent);
            }
        };
    }
}
