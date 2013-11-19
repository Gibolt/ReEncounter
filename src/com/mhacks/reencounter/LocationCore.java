package com.mhacks.reencounter;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;

public class LocationCore {
    final static String webUrl    = HtmlUtilities.endpoint;
    final static String submitUrl = webUrl + "commit.php";
    final static int INTERVAL = Settings.locationUpdateInterval;
    static AlarmManager alarm = null;
    static PendingIntent intent;

    protected static void startTimedLocationUpdate(Context c, Intent intent, AlarmManager alarm) {
        Calendar cal = Calendar.getInstance();
        LocationCore.alarm = alarm;
        LocationCore.intent = PendingIntent.getService(c, 0, intent, 0);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), LocationCore.INTERVAL, LocationCore.intent); 
    }
    
    protected static boolean endTimedLocationUpdate() {
    	if (alarm != null) {    		
    		alarm.cancel(intent);
    		alarm = null;
    		return true;
    	}
    	return false;
    }

    protected static Intent locationIntent(Context c, String user, String pass) {
        Intent intent = new Intent(c, LocationUpdateService.class);
        intent.putExtra("user", user);
        intent.putExtra("pass", pass);
        return intent;
    }

    protected static boolean sendLocation(Bundle b) {
        if (!validateLocation(b)) {
            return false;
        }
        String request = submitUrl(b);
        AndroidUtilities.log("Send Location Request", request);
        return submitLocation(request);
    }

    private static String submitUrl(Bundle b) {
        String user = b.getString("user");
        String pass = b.getString("pass");
        String time = b.getString("time");
        double lat  = b.getDouble("lat");
        double lon  = b.getDouble("lon");
        String var = "?user="      + HtmlUtilities.enc(user)
                   + "&password="  + HtmlUtilities.enc(pass)
                   + "&timestamp=" + HtmlUtilities.enc(time)
                   + "&lat="       + lat
                   + "&lon="       + lon;
        return submitUrl + var;
    }

    private static boolean validateLocation(Bundle b) {
        double lat = b.getDouble("lat");
        double lon = b.getDouble("lon");
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            return false;
        }
        return HiddenLocationList.isHidden(lat, lon);
    }

    private static boolean submitLocation(String request) {
        HtmlUtilities.executeResponseless(request);
        return true;
    }
}
