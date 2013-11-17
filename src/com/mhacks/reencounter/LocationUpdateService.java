package com.mhacks.reencounter;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.mhacks.reencounter.MyLocation.LocationResult;
import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;
import com.mhacks.reencounter.util.StringUtilities;

public class LocationUpdateService extends IntentService {
    private final String webUrl = HtmlUtilities.endpoint + "commit.php";
    private String user, pass;
    private String lat, lon, time;

    public LocationUpdateService() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AndroidUtilities.log("Starting LocationUpdateService", "LocationUpdateService has successfully been handled");
        user = intent.getStringExtra("user");
        pass = intent.getStringExtra("pass");
        getLocation();
    }

    private void getLocation() {
        LocationResult locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location){
                time = StringUtilities.currentDate();
                sendLocation(location);
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }

    private void sendLocation(final Location location) {
        Thread t = new Thread() {
            public void run() {
                Bundle b = new Bundle();
                b.putString("user", user);
                b.putString("pass", pass);
                b.putString("time", time);
                b.putDouble("lat", location.getLatitude());
                b.putDouble("lon", location.getLongitude());
                AndroidUtilities.log("Threading LocationUpdateService", "LocationUpdateService is preparing to send location data");
                LocationCore.sendLocation(b);
            }
        };
        t.start();
    }
}
