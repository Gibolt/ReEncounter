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

/**
 * Activity which displays a list of your contacts.
 */
public class ContactsActivity extends ListActivity {
	String user;
	String pass;
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
		user = b.getString("user");
		pass = b.getString("pass");

	    String var= "?user="	+ HtmlUtilities.enc(user)
                  + "&password="+ HtmlUtilities.enc(pass);
	    String query = queryUrl + var;
	    Log.w("output", query);
		try {
		    JSONArray array = HtmlUtilities.requestResponse(query).getJSONArray("posts");
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
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			Intent intent = new Intent(ContactsActivity.this, ProfileActivity.class);
			intent.putExtra("user", user);
			intent.putExtra("pass", pass);
			intent.putExtra("infoUser", contacts.get(pos));
			startActivity(intent);
		}
	};
}
