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

public class RareActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rare);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		Bundle b = getIntent().getExtras();
		String user = b.getString("username");
		String password = b.getString("password");

		HttpClient defaultClient = new DefaultHttpClient();
	    String usr = "?user="     + user
	    		   + "&password=" + password
	    		   + "&min_num="  + 4
	    		   + "&max_num="  + 7;
	    HttpPost httpPostRequest = new HttpPost("http://web.engr.illinois.edu/~reese6/MHacks/queryRare.php"+usr);
		try {
		    HttpResponse response = defaultClient.execute(httpPostRequest);
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
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rare, menu);
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
