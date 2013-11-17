package com.mhacks.reencounter.util;

import android.os.StrictMode;
import android.util.Log;

public class AndroidUtilities {
	public static boolean allowLogging = true;
	public static void setStrictMode() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	}
	public static void log(String tag, String loggingMessage) {
		log(tag, loggingMessage, "i");
	}
	
	/**
	 * Abstracts logging to util class. Can easily turn on/off globally
	 * @param loggingMessage String to output to log
	 * @param loggingType {(v, Verbose), (d, Debug), (i, Info), (w, Warning), (e, Error), (wtf, Wtf)}
	 */
	public static void log(String tag, String loggingMessage, String loggingType) {
		if (allowLogging) {
			if (loggingType.equals("e")) {
				Log.e(tag, loggingMessage);
			}
			else if (loggingType.equals("w")) {
				Log.w(tag, loggingMessage);
			}
			else if (loggingType.equals("i")) {
				Log.i(tag, loggingMessage);
			}
			else if (loggingType.equals("d")) {
				Log.d(tag, loggingMessage);
			}
			else if (loggingType.equals("v")) {
				Log.v(tag, loggingMessage);
			}
			else if (loggingType.equals("wtf")) {
				Log.wtf(tag, loggingMessage);
			}
		}
	}
}
