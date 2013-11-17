package com.mhacks.reencounter.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringUtilities {
    public static boolean isEmail(String str) {
        return (str != null && str.contains("@") && str.length() >= 5 && str.indexOf("@") <= str.length()-4);
    }

    public static boolean isPhone(String str) {
        return (str != null && str.length() >= 1);
    }

    public static String currentDate() {
    	int interval = 5;
    	return currentDate(interval);
    }
    
    public static String currentDate(int interval) {
    	Date date = new Date();
    	date.setMinutes(Math.round(date.getMinutes()/interval)*interval);
    	date.setSeconds(0);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    	return sdf.format(date);
    }
}
