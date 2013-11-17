package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONException;

import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity which displays a conversation between you and another user.
 */
public final class MessagingCore {
    final static String webUrl    = HtmlUtilities.endpoint;
    final static String queryUrl  = webUrl + "queryMessages.php";
    final static String submitUrl = webUrl + "sendMessage.php";

    protected static Intent messagingIntent(Context c, String user, String pass, String otherUser) {
        Intent intent = new Intent(c, MessagingActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("pass", pass);
        intent.putExtra("otherUser", "Jerry");
        return intent;
    }

    protected static boolean sendMessage(String message, Bundle b) {
        if (message.length() == 0) {
            return false;
        }
        message = message.trim();
        String request = submitUrl(message, b);
        AndroidUtilities.log("Send Message Request", request);
        return submitMessage(request);
    }

    protected static String[] queryMessages(Bundle b) {
        String request = queryUrl(b);
        AndroidUtilities.log("Query Messages Request", request);
        JSONArray array = queryMessages(request);
        return parseMessageXml(array);
    }

    private static String submitUrl(String message, Bundle b) {
        String user      = b.getString("user");
        String pass      = b.getString("pass");
        String otherUser = b.getString("otherUser");
        String var = "?user="      + HtmlUtilities.enc(user)
                   + "&password="  + HtmlUtilities.enc(pass)
                   + "&otherUser=" + HtmlUtilities.enc(otherUser)
                   + "&message="   + HtmlUtilities.enc(message);
        return submitUrl + var;
    }

    private static String queryUrl(Bundle b) {
        String user      = b.getString("user");
        String pass      = b.getString("pass");
        String otherUser = b.getString("otherUser");
        String var = "?user="     + HtmlUtilities.enc(user)
                   + "&password=" + HtmlUtilities.enc(pass)
                   + "&otherUser="+ HtmlUtilities.enc(otherUser);
        return queryUrl + var;
    }

    private static boolean submitMessage(String request) {
        HtmlUtilities.executeResponseless(request);
        return true;
    }

    private static JSONArray queryMessages(String request) {
        return HtmlUtilities.queryArray(request, "posts");
    }

    private static String[] parseMessageXml(JSONArray array) {
        String[] list = new String[array.length()];
        try {
            for(int i = 0; i < array.length();i++){
                String sender  = array.getJSONObject(i).getString("Sender");
                String time    = array.getJSONObject(i).getString("Time");
                String message = array.getJSONObject(i).getString("Message");
                list[i] = "From: " + sender + " At: " + time + "\n" + message;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
