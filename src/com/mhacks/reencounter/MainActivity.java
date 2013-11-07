package com.mhacks.reencounter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.mhacks.reencounter.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//gets gps position every 5min and updates it to the database
public class MainActivity extends ListActivity {
	Timer timer1;
	JSONObject obj = new JSONObject();
	String user;
	String pw;
	final int INTERVAL = 1000*60*5; // 5 Minutes
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bundle b = getIntent().getExtras();
		user = b.getString("username");
		pw = b.getString("password");
		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, 0, mlocListener);
				
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
					intent.putExtra("username", user);
					intent.putExtra("password", pw);
					startActivity(intent);
				}
				//If Freq is clicked
				else if (position==1){
					Intent intent = new Intent(MainActivity.this, FreqActivity.class);
					intent.putExtra("username", user);
					intent.putExtra("password", pw);
					startActivity(intent);
				}
				//If Rare is clicked
				else if (position==2){
					Intent intent = new Intent(MainActivity.this, RareActivity.class);
					intent.putExtra("username", user);
					intent.putExtra("password", pw);
					startActivity(intent);
				}
				//If Messaging is clicked
				else if (position==3){
					Intent intent = new Intent(MainActivity.this, MessagingActivity.class);
					intent.putExtra("username", user);
					intent.putExtra("password", pw);
					intent.putExtra("otherUser", "Jerry");
					startActivity(intent);
				}
				//If Contacts is clicked
				else if (position==4){
					Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
					intent.putExtra("username", user);
					intent.putExtra("password", pw);
					startActivity(intent);
				}
			}
		});
	}
	protected void sendJson(final JSONObject param) {
        Thread t = new Thread() {
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = param;

                try {
                	String usr = "?user="     + HtmlUtilities.enc(json.getString("user"))
                			   + "&password=" + HtmlUtilities.enc(json.getString("password"))
                			   + "&timestamp="+ HtmlUtilities.enc(json.getString("time"))
                			   + "&lat="      + HtmlUtilities.enc(json.getString("lat"))
                			   + "&lon="      + HtmlUtilities.enc(json.getString("lon"));
                	String query = getString(R.string.endpoint) + "commit.php" + usr;
                	Log.i("output", query);
                    response = HtmlUtilities.execute(query);
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();      
    }
	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener
	{
		boolean gpsEnabled=false;
	    boolean networkEnabled=false;
		@SuppressWarnings("deprecation")
		@Override
		public void onLocationChanged(Location loc)
		{
			Date date = new Date();
			date.setMinutes(Math.round(date.getMinutes()/5)*5);
			date.setSeconds(0);
			/* debug: is it local time? */
			/* date formatter in local timezone */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

			double lat = loc.getLatitude();
			double lon = loc.getLongitude();

			String Text = "My current location is: " + "Latitude = " + lat + "Longitud = " + lon + "Time = "+ sdf.format(date);
			Toast.makeText(getApplicationContext(),Text,Toast.LENGTH_SHORT).show();
			Log.v("Output", Text); //logs the current latitude in 	
			
			try {
				obj.put("user",user);
				obj.put("password", pw);
				obj.put("time", sdf.format(date));
				obj.put("lat", lat);
				obj.put("lon", lon);
				sendJson(obj);
			}catch (JSONException e) {
				e.printStackTrace();
			} 
			Log.v("obj",obj.toString());
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}/* End of Class MyLocationListener */
}/* End of MainActivity */