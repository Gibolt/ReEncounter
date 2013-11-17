package com.mhacks.reencounter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
//	final String webUrl = getString(R.string.endpoint);
	int i = 0;
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

	    String usr = "?user="	  + HtmlUtilities.enc(user)
	    		   + "&password=" + HtmlUtilities.enc(password)
	    		   + "&num="      + maxNum;
	    String query = queryUrl + usr;
	    Log.w("output", query);
		try {
			JSONArray array = HtmlUtilities.run(query).getJSONArray("posts");
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
				startActivity(intent);
			}
		});
	}
}
