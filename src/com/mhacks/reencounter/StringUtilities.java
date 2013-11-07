package com.mhacks.reencounter;

public class StringUtilities {
	public static boolean isEmail(String str) {
		return (str != null && str.contains("@") && str.length() >= 5 && str.indexOf("@") <= str.length()-4);
	}

	public static boolean isPhone(String str) {
		return (str != null && str.length() >= 1);
	}
}
