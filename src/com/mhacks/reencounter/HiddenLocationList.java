package com.mhacks.reencounter;

import java.util.ArrayList;

import android.location.Location;

public class HiddenLocationList {
    private static ArrayList<HiddenLocation> list = new ArrayList<HiddenLocation>();

    protected static void addLocation(double lat, double lon, double radius) {
        HiddenLocation location = new HiddenLocation(lat, lon, radius);
        if (!list.contains(location)) {
            list.add(location);
        }
        else {
            int index = list.indexOf(location);
            list.remove(index);
            list.add(index, location);
        }
    }

    protected static void addLocation(String lat, String lon, double radius) {
        addLocation(Double.parseDouble(lat), Double.parseDouble(lon), radius);
    }

    protected static void addLocation(Location location, double radius) {
        addLocation(location.getLatitude(), location.getLongitude(), radius);
    }

    protected static void removeLocation(double lat, double lon) {
        HiddenLocation location = new HiddenLocation(lat, lon);
        if (list.contains(location)) {
            list.remove(location);
        }
    }

    protected static void removeLocation(String lat, String lon) {
        removeLocation(Double.parseDouble(lat), Double.parseDouble(lon));
    }

    protected static boolean isHidden(double lat, double lon) {
        for (HiddenLocation location: list) {
            if (location.withinRadius(lat, lon)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isHidden(Location location) {
        return isHidden(location.getLatitude(), location.getLongitude());
    }
}

final class HiddenLocation {
    private double lat, lon, rad;
    private char defaultUnit = 'M';

    HiddenLocation(double lat, double lon) {
        new HiddenLocation(lat, lon, 0);
    }

    HiddenLocation(double lat, double lon, double radius) {
        this.lat = lat;
        this.lon = lon;
        this.rad = radius;
    }

    protected boolean withinRadius(double lat, double lon) {
        if (Math.abs(distance(this.lat, this.lon, lat, lon, defaultUnit)) <= this.rad) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        HiddenLocation location = (HiddenLocation) o;
        return (location.lat == this.lat && location.lon == this.lon);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        double miles = rad2deg(dist) * 60 * 1.1515;
        if (unit == 'k' || unit == 'K') {
            return (miles * 1.609344);
        } 
        else if (unit == 'n' || unit == 'N') {
            return (miles * 0.8684);
        }
        else {
            return miles;
        }
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
