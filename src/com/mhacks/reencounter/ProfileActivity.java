package com.mhacks.reencounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

/**
 * Activity which displays a user's profile.
 */
public class ProfileActivity extends Activity {

	// UI references.
	private TextView mProfileName;
	private TextView mProfileUser;
	private TextView mProfileDescription;
	private ListView mProfilePhoneList;
	private ListView mProfileEmailList;

	String user;
	String infoUser;
	String password;
	
	final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
	final String queryUrl = webUrl + "queryUserInfo.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_profile);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		Bundle b = getIntent().getExtras();
		user = b.getString("username");
		password = b.getString("password");
		infoUser = b.getString("id");
		
		mProfileName        = (TextView) findViewById(R.id.profileName);
		mProfileName        = (TextView) findViewById(R.id.profileUser);
		mProfileDescription = (TextView) findViewById(R.id.profileDescription);
		mProfilePhoneList   = (ListView) findViewById(R.id.profilePhoneList);
		mProfileEmailList   = (ListView) findViewById(R.id.profileEmailList);
		
		sendJson();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	protected void sendJson() {
        Thread t = new Thread() {
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                String usr = "?user=" + enc(user)
      	    		   + "&password=" + enc(password)
      	    		   + "&infoUser=" + enc(infoUser);
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                HttpPost post = new HttpPost(queryUrl + usr);
                try {
                	HttpResponse response = client.execute(post);
            	    Log.w("output", queryUrl + usr);
        		    String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
        		    JSONObject obj = new JSONObject(jsonResult);
        		    JSONArray array = obj.getJSONArray("posts");
        			String[] list = new String[array.length()];
        			for(int i = 0; i < array.length();i++){
        				JSONObject responseJson = array.getJSONObject(i);
        				mProfileName.setText(responseJson.getString("Name"));
        				mProfileUser.setText(responseJson.getString("User"));
        				mProfileDescription.setText(responseJson.getString("Description"));
        				//mProfilePhoneList.
        				list[i] = responseJson.getString("Phone1");
        				Log.w("output",list[i]);
        			}
        			
        			//setListAdapter(new ArrayAdapter<String>(this, R.id.profilePhoneList, list));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        };
        t.start();      
    }
	private StringBuilder inputStreamToString(InputStream is) {
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
	private String enc(String str) {
		try {
			return URLEncoder.encode(str, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
