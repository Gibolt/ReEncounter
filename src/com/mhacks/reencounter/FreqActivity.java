package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.HtmlUtilities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class FreqActivity extends ListActivity {
    int minNum = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq);

        Bundle b = getIntent().getExtras();
        String user = b.getString("user");
        String pass = b.getString("pass");

        String usr = "?user="    + HtmlUtilities.enc(user)
                   + "&password="+ HtmlUtilities.enc(pass)
                   + "&num="     + minNum;
        String query = getString(R.string.endpoint) + "queryFrequent.php" + usr;
        try {
            JSONObject obj = HtmlUtilities.requestResponse(query);
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
