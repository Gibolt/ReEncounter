package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mhacks.reencounter.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;

public class FreqActivity extends ListActivity {
	int minNum = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle b = getIntent().getExtras();
        String username = b.getString("username");
        String password = b.getString("password");

        String usr = "?user="    + HtmlUtilities.enc(username)
                   + "&password="+ HtmlUtilities.enc(password)
                   + "&num="     + minNum;
        String query = getString(R.string.endpoint) + "queryFrequent.php" + usr;
        try {
            JSONObject obj = HtmlUtilities.run(query);
            JSONArray array = obj.getJSONArray("posts");
            String[] list = new String[array.length()];
            for(int i = 0; i < array.length();i++){
                list[i] = array.getJSONObject(i).getString("Other_user") + " " + array.getJSONObject(i).getString("Times");
                Log.w("output",list[i]);
            }
            setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
