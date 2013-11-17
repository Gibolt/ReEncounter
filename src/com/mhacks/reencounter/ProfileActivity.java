package com.mhacks.reencounter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mhacks.reencounter.R;
import com.mhacks.reencounter.util.AndroidUtilities;
import com.mhacks.reencounter.util.HtmlUtilities;
import com.mhacks.reencounter.util.StringUtilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
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
        Bundle b = getIntent().getExtras();
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
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                String usr = "?user="   + HtmlUtilities.enc(user)
                         + "&password=" + HtmlUtilities.enc(pass)
                         + "&infoUser=" + HtmlUtilities.enc(infoUser);
                String query = queryUrl + usr;
                try {
                    AndroidUtilities.log("ProfileActivity", query);
                    JSONObject obj = HtmlUtilities.requestResponse(query);
                    JSONArray array = obj.getJSONArray("posts");
                    JSONObject responseJson = array.getJSONObject(0);
                    otherUser   = responseJson.getString("User");
                    name        = responseJson.getString("Name");
                    description = responseJson.getString("Description");
                    phone1      = responseJson.getString("Phone");
                    phone2      = responseJson.getString("Phone2");
                    email1      = responseJson.getString("Email");
                    email2      = responseJson.getString("Email2");
                    messaging   = responseJson.getString("Message").equals("1") ? true : false;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            profileUser.setText(otherUser);
                            profileName.setText(name);
                            profileDescription.setText(description);
                            profileAddContact.setOnClickListener(profileAddContactHandler);
                            profileViewEncounters.setOnClickListener(profileViewEncountersHandler);
                            if (StringUtilities.isEmail(email1)) {
                                profileEmail.setVisibility(View.VISIBLE);
                                profileEmail.setOnClickListener(profileEmailHandler);
                            }
                            if (messaging) {
                                profileMessage.setVisibility(View.VISIBLE);
                                profileMessage.setOnClickListener(profileMessageHandler);
                            }
                            if (StringUtilities.isPhone(phone1)) {
                                profileCall.setVisibility(View.VISIBLE);
                                profileCall.setOnClickListener(profileCallHandler);
                            }
                        }
                    });
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        };
        t.start();
    }

    View.OnClickListener profileMessageHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, MessagingActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("pass", pass);
            intent.putExtra("otherUser", infoUser);
            startActivity(intent);
        }
    };
    View.OnClickListener profileEmailHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL,new String[] {email1}); 
            intent.putExtra(Intent.EXTRA_SUBJECT, "Message from " + user);  
            startActivity(Intent.createChooser(intent, "Send email using:")); 
        }
    };
    View.OnClickListener profileCallHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Uri phoneUri = Uri.parse("tel:" + phone1);
            Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
            startActivity(intent); 
        }
    };
    View.OnClickListener profileAddContactHandler = new View.OnClickListener() {
        public void onClick(View v) {
            String var = "?user="      + HtmlUtilities.enc(user)
                       + "&password="  + HtmlUtilities.enc(pass)
                       + "&otherUser=" + HtmlUtilities.enc(otherUser);
            AndroidUtilities.log("ProfileActivity", addContactUrl + var);
            HtmlUtilities.executeResponseless(addContactUrl + var);
        }
    };
    View.OnClickListener profileViewEncountersHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, EncounterActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("pass", pass);
            intent.putExtra("otherUser", infoUser);
            startActivity(intent);
        }
    };
}
