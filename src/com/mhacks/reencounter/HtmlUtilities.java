package com.mhacks.reencounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

public class HtmlUtilities {
    public static HttpClient defaultClient = new DefaultHttpClient();
    static {
        HttpConnectionParams.setConnectionTimeout(defaultClient.getParams(), 10000);
    }

	public static String enc(String str) {
		try {
			return (str == null) ? "" : URLEncoder.encode(str, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static HttpResponse execute(String str) {
	    try {
	    	return defaultClient.execute(new HttpPost(str));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}

	public static JSONObject toJson(HttpResponse response) {
		try {
			return new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject run(String query) {
		return toJson(execute(query));
	}

	private static StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
          
        try {
        	while ((rLine = rd.readLine()) != null) {
        	answer.append(rLine);
        	}
        }      
        catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }
}
