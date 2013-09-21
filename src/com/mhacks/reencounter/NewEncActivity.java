package com.mhacks.reencounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
//query stuff from php
public class NewEncActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_enc);
		Bundle b = getIntent().getExtras();
		String ans = b.getString("id");
		
		Toast toast=Toast.makeText(this, "Index: " + ans, Toast.LENGTH_SHORT);
        toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_enc, menu);
		return true;
	}

}
