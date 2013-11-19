package com.mhacks.reencounter;

public class Settings {
    private final static int minute = 1000 * 60;
    protected static int locationUpdateInterval = 5 * minute;

    private static boolean remainLoggedIn = false;
    private static boolean allowMessaging = false;
    private static boolean enableGps      = false;
    private static boolean enableWifi     = true;
    
    private static int minNew  = 2;
    private static int maxNew  = 3;
    private static int minRare = 4;
    private static int maxRare = 7;
    private static int minFreq = 8;
    private static int maxFreq = Integer.MAX_VALUE;

    public static int getMaxNew() {
        return maxNew;
    }
    public static void setMaxNew(int maxNew) {
        Settings.maxNew = maxNew;
    }
    public static int getMinRare() {
        return minRare;
    }
    public static void setMinRare(int minRare) {
        Settings.minRare = minRare;
    }
    public static int getMaxRare() {
        return maxRare;
    }
    public static void setMaxRare(int maxRare) {
        Settings.maxRare = maxRare;
    }
    public static int getMinFreq() {
        return minFreq;
    }
    public static void setMinFreq(int minFreq) {
        Settings.minFreq = minFreq;
    }
    public static boolean isRemainLoggedIn() {
        return remainLoggedIn;
    }
    public static void setRemainLoggedIn(boolean remainLoggedIn) {
        Settings.remainLoggedIn = remainLoggedIn;
    }
    public static boolean isAllowMessaging() {
        return allowMessaging;
    }
    public static void setAllowMessaging(boolean allowMessaging) {
        Settings.allowMessaging = allowMessaging;
    }
    public static boolean isEnableGps() {
        return enableGps;
    }
    public static void setEnableGps(boolean enableGps) {
        Settings.enableGps = enableGps;
    }
    public static boolean isEnableWifi() {
        return enableWifi;
    }
    public static void setEnableWifi(boolean enableWifi) {
        Settings.enableWifi = enableWifi;
    }
}
