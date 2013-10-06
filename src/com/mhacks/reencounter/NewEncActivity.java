package com.mhacks.reencounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Activity which displays users you have been near a few times.
 */
public class NewEncActivity extends ListActivity {
	String user;
	String password;
	ArrayList<String> otherUsers = new ArrayList<String>();
	ArrayList<Integer> times = new ArrayList<Integer>();

	final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
	final String queryUrl = webUrl + "queryNew.php";
	final int maxNum = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_enc);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		Bundle b = getIntent().getExtras();
		user = b.getString("username");
		password = b.getString("password");
		
	    HttpClient defaultClient = new DefaultHttpClient();
	    // Setup the get request
	    String usr = "?user="	  + user
	    		   + "&password=" + password
	    		   + "&num="      + maxNum;
	    Log.w("output", queryUrl + usr);
	    HttpPost httpPostRequest = new HttpPost(queryUrl + usr);
		try {
			// Execute the request in the client
		    HttpResponse response = defaultClient.execute(httpPostRequest);
		    String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    JSONObject obj = new JSONObject(jsonResult);
		    JSONArray array = obj.getJSONArray("posts");
			String[] list = new String[array.length()];
			for(int i = 0; i < array.length();i++){
				otherUsers.add(array.getJSONObject(i).getString("Other_user"));
				times.add(Integer.parseInt(array.getJSONObject(i).getString("Times")));
				list[i] = otherUsers.get(i) + " " + times.get(i);
				Log.w("output",list[i]);
			}
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(NewEncActivity.this, ProfileActivity.class);
				intent.putExtra("username", user);
				intent.putExtra("password", password);
				intent.putExtra("infoUser", otherUsers.get(position));
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
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
