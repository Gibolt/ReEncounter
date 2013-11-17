package com.mhacks.reencounter;

public class Settings {
    private final static int minute = 1000 * 60;

    private static boolean remainLoggedIn = false;
    private static boolean allowMessaging = false;
    private static boolean enableGps      = false;
    private static boolean enableWifi     = true;

    protected static int locationUpdateInterval = 5 * minute;

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
