package com.mhacks.reencounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.widget.ListView;

/**
 * Activity which displays a conversation between you and another user.
 */
public class MessagingActivity extends ListActivity {
	String user;
	String password;
	
	final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
	final String queryUrl = webUrl + "queryMessages.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);
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
	    		   + "&otherUser="+ "Jerry";
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
				String sender = array.getJSONObject(i).getString("Sender");
//				String recipient = array.getJSONObject(i).getString("Recipient");
				String time = array.getJSONObject(i).getString("Time");
				String message = array.getJSONObject(i).getString("Message");
				list[i] = "From: " + sender + " At: " + time + "\n" + message;
				Log.w("output",list[i]);
			}		
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messaging, menu);
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
