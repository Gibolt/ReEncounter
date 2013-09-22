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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
//query stuff from php
public class NewEncActivity extends ListActivity {
	String username;
	String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_enc);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		Bundle b = getIntent().getExtras();
		username = b.getString("username");
		password = b.getString("password");
		String id = b.getString("id");
		 // Create a new HTTP Client
	    HttpClient defaultClient = new DefaultHttpClient();
	    // Setup the get request
	    String usr = "?user="+username + "&password="+password+"&num="+3;
	    Log.w("output", "http://web.engr.illinois.edu/~reese6/MHacks/queryNew.php"+usr);
	    HttpPost httpPostRequest = new HttpPost("http://web.engr.illinois.edu/~reese6/MHacks/queryNew.php"+usr);
		try {
			// Execute the request in the client
		    HttpResponse response = defaultClient.execute(httpPostRequest);
		    // Grab the response
		    String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    JSONObject obj = new JSONObject(jsonResult);
		    JSONArray array = obj.getJSONArray("posts");
			String[] list = new String[array.length()];
			for(int i = 0; i < array.length();i++){
				list[i] = array.getJSONObject(i).getString("Other_user") + " " + array.getJSONObject(i).getString("Times");
				Log.w("output",list[i]);
			}
			
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//String[] list = getResources().getStringArray(R.array.list_array);
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		/*Toast toast=Toast.makeText(this, "Index: " + ans, Toast.LENGTH_SHORT);
        toast.show();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_enc, menu);
		return true;
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
}
