package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONException;

import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.os.Bundle;

/**
 * Activity which displays a conversation between you and another user.
 */
public final class MessagingCore {
    final static String webUrl    = HtmlUtilities.endpoint;
    final static String queryUrl  = webUrl + "queryMessages.php";
    final static String submitUrl = webUrl + "sendMessage.php";

    protected static boolean sendMessage(String message, Bundle b) {
        if (message.length() == 0) {
            return false;
        }
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
        String user      = b.getString("username");
        String password  = b.getString("password");
        String otherUser = b.getString("otherUser");
        String var = submitUrl
                   + "?user="      + HtmlUtilities.enc(user)
                   + "&password="  + HtmlUtilities.enc(password)
                   + "&otherUser=" + HtmlUtilities.enc(otherUser)
                   + "&message="   + HtmlUtilities.enc(message);
        return submitUrl + var;
    }

    private static String queryUrl(Bundle b) {
        String user      = b.getString("username");
        String password  = b.getString("password");
        String otherUser = b.getString("otherUser");
        String var = "?user="     + HtmlUtilities.enc(user)
                   + "&password=" + HtmlUtilities.enc(password)
                   + "&otherUser="+ HtmlUtilities.enc(otherUser);
        return queryUrl + var;
    }

    private static boolean submitMessage(String request) {
        HtmlUtilities.execute(request);
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
