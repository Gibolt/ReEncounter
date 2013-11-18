package com.mhacks.reencounter.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ExternalUtilities {
	public static void callPhone(Context c, String phone) {
	    Uri phoneUri = Uri.parse("tel:" + phone);
	    Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
	    c.startActivity(intent);
	}

	public static void sendEmail(Context c, String email, String subject) {
		Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email}); 
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);  
        c.startActivity(Intent.createChooser(intent, "Send email using:")); 
	}

	public static void viewMap(Context c, String lat, String lon, String tag) {
        String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + tag + ")";
        AndroidUtilities.log("ExternalUtilities", uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        c.startActivity(intent);
	}
}
