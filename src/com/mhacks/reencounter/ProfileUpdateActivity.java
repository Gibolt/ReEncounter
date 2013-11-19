package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;
import com.mhacks.reencounter.util.StringUtilities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a user's profile.
 */
public class ProfileUpdateActivity extends Activity {

	private TextView profileUser;
	private TextView profileEmail1;

    private EditText profileName;
    private EditText profileEmail2;
    private EditText profilePhone1;
    private EditText profilePhone2;
    private EditText profileDescription;

    private CheckBox profileMessage;
    private Button profileUpdate;
    Bundle b;

    String user, pass;
    String name, description, email1, email2, phone1, phone2;
    boolean messaging;

    final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
    final String queryUrl = webUrl + "queryUserInfo.php";
    final String updateUrl = webUrl + "updateUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        b = getIntent().getExtras();

        user = b.getString("user");
        pass = b.getString("pass");

        profileUser   = (TextView) findViewById(R.id.profile_update_username);
        profileEmail1 = (TextView) findViewById(R.id.profile_update_email1);
        profileName        = (EditText) findViewById(R.id.profile_update_profilename);
        profileEmail2      = (EditText) findViewById(R.id.profile_update_email2);
        profilePhone1      = (EditText) findViewById(R.id.profile_update_phone1);
        profilePhone2      = (EditText) findViewById(R.id.profile_update_phone2);
        profileDescription = (EditText) findViewById(R.id.profile_update_desription);
        profileMessage = (CheckBox) findViewById(R.id.profile_update_message);
        profileUpdate = (Button) findViewById(R.id.profile_update_submit);

        b = new Bundle();
        b.putString("user", user);
        b.putString("pass", pass);
        b.putString("infoUser", user);
        sendJson();
    }

    protected void sendJson() {
        Thread t = new Thread() {
            public void run() {
                try {
                	JSONArray array = ProfileCore.queryProfile(b);
                    JSONObject responseJson = array.getJSONObject(0);
                    name        = responseJson.getString("Name");
                    description = responseJson.getString("Description");
                    phone1      = responseJson.getString("Phone");
                    phone2      = responseJson.getString("Phone2");
                    email1      = responseJson.getString("Email");
                    email2      = responseJson.getString("Email2");
                    messaging   = responseJson.getString("Message").equals("1") ? true : false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        profileUser.setText(user);
                        profileName.setText(name);
                        profileDescription.setText(description);
                        profileEmail1.setText(email1);
                        profileEmail2.setText(email2);
                        profilePhone1.setText(phone1);
                        profilePhone2.setText(phone2);
                        profileMessage.setChecked(messaging);
                        profileUpdate.setOnClickListener(profileUpdateHandler);
                    }
                });
            }
        };
        t.start();
    }

    View.OnClickListener profileUpdateHandler = new View.OnClickListener() {
        public void onClick(View v) {
        	String name   = profileName.getEditableText().toString();
        	String email1 = profileEmail1.getText().toString();
        	String email2 = profileEmail2.getEditableText().toString();
        	String phone1 = profilePhone1.getEditableText().toString();
        	String phone2 = profilePhone2.getEditableText().toString();
        	String desc   = profileDescription.getEditableText().toString();
        	boolean message = profileMessage.isChecked();
        	
            String var = "?user="     + HtmlUtilities.enc(user)
                       + "&password=" + HtmlUtilities.enc(pass);
            if (!name.isEmpty()) var   += "&name="  + HtmlUtilities.enc(name);
            if (StringUtilities.isEmail(email1)) var += "&email="  + HtmlUtilities.enc(email1);
        	if (StringUtilities.isEmail(email2)) var += "&email2=" + HtmlUtilities.enc(email2);
        	if (StringUtilities.isPhone(phone1)) var += "&phone=" + HtmlUtilities.enc(phone1);
        	if (StringUtilities.isPhone(phone2)) var += "&phone2=" + HtmlUtilities.enc(phone2);
        	if (!desc.isEmpty()) var += "&description=" + HtmlUtilities.enc(desc);
        	var += "&message=" + (message? 1 : 0);
            AndroidUtilities.log("ProfileUpdateActivity", updateUrl + var);
            HtmlUtilities.executeResponseless(updateUrl + var);
        }
    };
}
