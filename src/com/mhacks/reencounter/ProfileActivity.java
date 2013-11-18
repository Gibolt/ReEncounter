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
import android.widget.TextView;

/**
 * Activity which displays a user's profile.
 */
public class ProfileActivity extends Activity {

    private TextView profileName;
    private TextView profileUser;
    private TextView profileDescription;
    private Button   profileEmail;
    private Button   profileMessage;
    private Button   profileCall;
    private Button   profileAddContact;
    private Button   profileViewEncounters;
    Bundle b;

    String user, infoUser, pass, otherUser;
    String name, description, email1, email2, phone1, phone2;
    boolean messaging;

    final String webUrl = "http://web.engr.illinois.edu/~reese6/MHacks/";
    final String queryUrl = webUrl + "queryUserInfo.php";
    final String addContactUrl = webUrl + "addContact.php";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        b = getIntent().getExtras();

        user     = b.getString("user");
        pass     = b.getString("pass");
        infoUser = b.getString("infoUser");

        profileName        = (TextView) findViewById(R.id.profileName);
        profileUser        = (TextView) findViewById(R.id.profileUser);
        profileDescription = (TextView) findViewById(R.id.profileDescription);
        profileEmail         = (Button) findViewById(R.id.profileEmail);
        profileMessage       = (Button) findViewById(R.id.profileMessage);
        profileCall          = (Button) findViewById(R.id.profileCall);
        profileAddContact    = (Button) findViewById(R.id.profileAddContact);
        profileViewEncounters= (Button) findViewById(R.id.profileViewEncounters);

        profileEmail  .setVisibility(View.GONE);
        profileMessage.setVisibility(View.GONE);
        profileCall   .setVisibility(View.GONE);
        sendJson();
    }

    protected void sendJson() {
        Thread t = new Thread() {
            public void run() {
                JSONArray array = ProfileCore.queryProfile(b);
                JSONObject responseJson = null;
                try {
                    responseJson = array.getJSONObject(0);
                    otherUser   = responseJson.getString("User");
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
                        profileUser.setText(otherUser);
                        profileName.setText(name);
                        profileDescription.setText(description);
                        profileAddContact.setOnClickListener(profileAddContactHandler);
                        profileViewEncounters.setOnClickListener(ProfileCore.viewEncountersHandler(ProfileActivity.this, user, pass, otherUser));
                        if (StringUtilities.isEmail(email1)) {
                            profileEmail.setVisibility(View.VISIBLE);
                            profileEmail.setOnClickListener(ProfileCore.emailHandler(ProfileActivity.this, email1, "Message from " + user));
                        }
                        if (messaging) {
                            profileMessage.setVisibility(View.VISIBLE);
                            profileMessage.setOnClickListener(ProfileCore.messageHandler(ProfileActivity.this, user, pass, infoUser));
                        }
                        if (StringUtilities.isPhone(phone1)) {
                            profileCall.setVisibility(View.VISIBLE);
                            profileCall.setOnClickListener(ProfileCore.callHandler(ProfileActivity.this, phone1));
                        }
                    }
                });
            }
        };
        t.start();
    }

    View.OnClickListener profileAddContactHandler = new View.OnClickListener() {
        public void onClick(View v) {
            String var = "?user="      + HtmlUtilities.enc(user)
                       + "&password="  + HtmlUtilities.enc(pass)
                       + "&otherUser=" + HtmlUtilities.enc(otherUser);
            AndroidUtilities.log("ProfileActivity", addContactUrl + var);
            HtmlUtilities.executeResponseless(addContactUrl + var);
        }
    };
}
