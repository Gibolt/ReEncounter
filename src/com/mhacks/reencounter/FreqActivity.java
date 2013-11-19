package com.mhacks.reencounter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FreqActivity extends ListActivity {
    int min = 8;
    String user, pass;
    ArrayList<String> otherUsers = new ArrayList<String>();
    ArrayList<Integer> times = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq);

        Bundle b = getIntent().getExtras();
        user = b.getString("user");
        pass = b.getString("pass");

        String usr = "?user="    + HtmlUtilities.enc(user)
                   + "&password="+ HtmlUtilities.enc(pass)
                   + "&min="     + min;
        String query = getString(R.string.endpoint) + "queryFrequent.php" + usr;
        try {
            JSONObject obj = HtmlUtilities.requestResponse(query);
            JSONArray array = obj.getJSONArray("posts");
            String[] list = new String[array.length()];
            for(int i = 0; i < array.length();i++){
                otherUsers.add(array.getJSONObject(i).getString("Other_user"));
                times.add(Integer.parseInt(array.getJSONObject(i).getString("Times")));
                list[i] = array.getJSONObject(i).getString("Other_user") + " " + array.getJSONObject(i).getString("Times");
                Log.w("output",list[i]);
            }
            setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ProfileCore.profileIntent(FreqActivity.this, user, pass, otherUsers.get(position));
                startActivity(intent);
            }
        });
    }
}
