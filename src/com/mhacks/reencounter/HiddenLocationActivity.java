package com.mhacks.reencounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HiddenLocationActivity extends Activity {
	Button addCurrent, removeCurrent;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_location);
        Bundle b = getIntent().getExtras();

        user = b.getString("user");
        pass = b.getString("pass");

        addCurrent    = (Button) findViewById(R.id.hiddenLocationAddCurrent);
        removeCurrent = (Button) findViewById(R.id.hiddenLocationRemoveCurrent);
        
        addCurrent   .setOnClickListener(hiddenLocationAddCurrentHandler);
        removeCurrent.setOnClickListener(hiddenLocationRemoveCurrentHandler);
    }
	
	View.OnClickListener hiddenLocationAddCurrentHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if (MyLocation.location != null) {
            	String toast = "Adding hidden Lat: " + MyLocation.location.getLatitude() + ", Lon: " + MyLocation.location.getLongitude();
            	Toast.makeText(HiddenLocationActivity.this, toast, Toast.LENGTH_SHORT).show();
            	HiddenLocationList.addLocation(MyLocation.location, .1);
            }
        }
    };
    
    View.OnClickListener hiddenLocationRemoveCurrentHandler = new View.OnClickListener() {
    	public void onClick(View v) {
    		if (MyLocation.location != null) {
    			String toast = "Removing hidden Lat: " + MyLocation.location.getLatitude() + ", Lon: " + MyLocation.location.getLongitude();
    			Toast.makeText(HiddenLocationActivity.this, toast, Toast.LENGTH_SHORT).show();
    			HiddenLocationList.removeLocation(MyLocation.location);
    		}
    	}
    };
}
