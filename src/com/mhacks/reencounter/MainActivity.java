package com.mhacks.reencounter;

import java.util.Timer;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.AndroidUtilities;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//Retrieve GPS position every 5min and updates it to the database
public class MainActivity extends ListActivity {
    Timer timer1;
    String user, pass;
    final int INTERVAL = 1000*60*5; // 5 Minutes
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidUtilities.setStrictMode();

        Bundle b = getIntent().getExtras();
        user = b.getString("user");
        pass = b.getString("pass");

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
                    intent.putExtra("user", user);
                    intent.putExtra("pass", pass);
                    startActivity(intent);
                }
                //If Freq is clicked
                else if (position==1){
                    Intent intent = new Intent(MainActivity.this, FreqActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("pass", pass);
                    startActivity(intent);
                }
                //If Rare is clicked
                else if (position==2){
                    Intent intent = new Intent(MainActivity.this, RareActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("pass", pass);
                    startActivity(intent);
                }
                //If Messaging is clicked
                else if (position==3){
                    Intent intent = MessagingCore.messagingIntent(MainActivity.this, user, pass, "Jerry");
                    startActivity(intent);
                }
                //If Contacts is clicked
                else if (position==4){
                    Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("pass", pass);
                    startActivity(intent);
                }
                //If Get Location is clicked
                else if (position==5){
                    Toast.makeText(MainActivity.this, "Updating Location", Toast.LENGTH_LONG).show();
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = LocationCore.locationIntent(MainActivity.this, user, pass);
                    LocationCore.startTimedLocationUpdate(MainActivity.this, intent, alarm);
                }
                //If End Location is clicked
                else if (position==6){
                    if (LocationCore.endTimedLocationUpdate()) {
                        Toast.makeText(MainActivity.this, "Terminating Updates", Toast.LENGTH_LONG).show();
                    }
                    else {
                    	Toast.makeText(MainActivity.this, "Location Service Off", Toast.LENGTH_LONG).show();
                    }
                }
                //If Logout is clicked
                else if (position == 7){
                }
            }
        });
    }
}