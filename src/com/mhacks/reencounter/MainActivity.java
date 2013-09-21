package com.mhacks.reencounter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//gets gps position every 5min and updates it to the database
public class MainActivity extends ListActivity {

	JSONObject obj = new JSONObject();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mlocListener);
		
		//Sets the different categories in the list
		String[] list = getResources().getStringArray(R.array.list_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//If New Encounter is clicked
				if(position==0){
					Intent intent = new Intent(MainActivity.this, NewEncActivity.class);
					intent.putExtra("id", obj.toString());
					startActivity(intent);
				}
				//If Freq is clicked
				else if(position==1){
					Intent intent = new Intent(MainActivity.this, FreqActivity.class);
					intent.putExtra("id", obj.toString());
					startActivity(intent);
				}
				//If Rare is clicked
				else{
					Intent intent = new Intent(MainActivity.this, RareActivity.class);
					intent.putExtra("id", obj.toString());
					startActivity(intent);
				}
			}
		});
	    
	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc)
		{
			double lat = loc.getLatitude();
			double lon = loc.getLongitude();
			String Text = "My current location is: " + "Latitude = " + lat + "Longitud = " + lon;
			Toast.makeText(getApplicationContext(),Text,Toast.LENGTH_SHORT).show();
			Log.v("Output", Text); //logs the current latitude in 
			
			try {
				obj.put("lat", lat);
				obj.put("lon", lon);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v("obj",obj.toString());
		}

		@Override
		public void onProviderDisabled(String provider)
		{
			Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onProviderEnabled(String provider)
		{
			Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
	}/* End of Class MyLocationListener */
}/* End of MainActivity */