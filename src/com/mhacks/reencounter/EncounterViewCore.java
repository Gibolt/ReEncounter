package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;

public class EncounterViewCore {
    final static String webUrl   = HtmlUtilities.endpoint;
    final static String queryUrl = webUrl + "queryEncounterView.php";

    protected static Intent encounterViewIntent(Context c, String user, String pass, String otherUser, int min, int max) {
        Intent intent = new Intent(c, MessagingActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("pass", pass);
        intent.putExtra("otherUser", otherUser);
        intent.putExtra("min", min);
        intent.putExtra("max", max);
        return intent;
    }

    protected static String[] queryEncounters(Bundle b) {
        String request = queryUrl(b);
        AndroidUtilities.log("Query Encounters Request", request);
        JSONArray array = queryEncounters(request);
        return parseEncounterXml(array);
    }

    private static String queryUrl(Bundle b) {
        String user      = b.getString("user");
        String password  = b.getString("pass");
        String otherUser = b.getString("otherUser");
        int min = b.getInt("min");
        int max = b.getInt("max");
        String var = "?user="     + HtmlUtilities.enc(user)
                   + "&password=" + HtmlUtilities.enc(password)
                   + "&otherUser="+ HtmlUtilities.enc(otherUser)
                   + "&min="      + min
                   + "&max="      + max;
        return queryUrl + var;
    }

    private static JSONArray queryEncounters(String request) {
        return HtmlUtilities.queryArray(request, "posts");
    }

    private static String[] parseEncounterXml(JSONArray array) {
        String[] list = new String[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                String times     = array.getJSONObject(i).getString("Times");
                String otherUser = array.getJSONObject(i).getString("Other_user");
                list[i] = otherUser + " " + times;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
