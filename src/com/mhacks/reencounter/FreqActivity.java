package com.mhacks.reencounter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class FreqActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freq);
		Bundle b = getIntent().getExtras();
		String ans = b.getString("id");
		
		Toast toast=Toast.makeText(this, "Index: " + ans, Toast.LENGTH_SHORT);
        toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.freq, menu);
		return true;
	}

}
