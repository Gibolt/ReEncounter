package com.mhacks.reencounter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Activity which displays a list of encounters between you and another user.
 */
public class EncounterActivity extends ListActivity {
	String user;
	String password;
	String otherUser;
	ArrayList<String> lats = new ArrayList<String>();
	ArrayList<String> lons = new ArrayList<String>();
	ArrayList<String> times = new ArrayList<String>();

	final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
//	final String webUrl = getString(R.string.endpoint);
	final String queryUrl = webUrl + "maps.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encounter);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		Bundle b = getIntent().getExtras();
		user = b.getString("username");
		password = b.getString("password");
		otherUser = b.getString("otherUser");

	    String map = "?user1="	 + HtmlUtilities.enc(user)
	    		   + "&password="+ HtmlUtilities.enc(password)
	    		   + "&user2="   + HtmlUtilities.enc(otherUser);
	    String query = queryUrl + map;
	    Log.w("output", query);
		try {
		    JSONArray array = HtmlUtilities.run(query).getJSONArray("posts");
			String[] list = new String[array.length()];
			for(int i = 0; i < array.length();i++){
				lats .add(array.getJSONObject(i).getString("lat"));
				lons .add(array.getJSONObject(i).getString("lon"));
				times.add(array.getJSONObject(i).getString("Time"));
				list[i] = lats.get(i) + ", " + lons.get(i)
						+ "\n" + times.get(i);
				Log.w("output",list[i]);
			}
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListView().setOnItemClickListener(viewEncounterHandler);
	}

	AdapterView.OnItemClickListener viewEncounterHandler = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int pos, long id) {
			String lat = lats.get(pos), lon = lons.get(pos);
			String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(Encounter)";
			Log.w("output",uri);
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(uri));
			startActivity(intent);
		}
	};
}
