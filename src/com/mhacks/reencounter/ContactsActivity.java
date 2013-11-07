package com.mhacks.reencounter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.mhacks.reencounter.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Activity which displays a list of your contacts.
 */
public class ContactsActivity extends ListActivity {
	String user;
	String password;
	ArrayList<String> contacts = new ArrayList<String>();

	final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
	final String queryUrl = webUrl + "queryContacts.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		Bundle b = getIntent().getExtras();
		user = b.getString("username");
		password = b.getString("password");

	    String con= "?user="	+ HtmlUtilities.enc(user)
                  + "&password="+ HtmlUtilities.enc(password);
	    String query = queryUrl + con;
	    Log.w("output", query);
		try {
		    JSONArray array = HtmlUtilities.run(query).getJSONArray("posts");
			String[] list = new String[array.length()];
			for(int i = 0; i < array.length();i++){
				contacts.add(array.getJSONObject(i).getString("Contact"));
				list[i] = contacts.get(i);
			}
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListView().setOnItemClickListener(viewContactHandler);
	}

	AdapterView.OnItemClickListener viewContactHandler = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int pos, long id) {
			Intent intent = new Intent(ContactsActivity.this, ProfileActivity.class);
			intent.putExtra("username", user);
			intent.putExtra("password", password);
			intent.putExtra("infoUser", contacts.get(pos));
			startActivity(intent);
		}
	};
}
