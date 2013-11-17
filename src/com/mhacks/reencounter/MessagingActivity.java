package com.mhacks.reencounter;

import com.mhacks.reencounter.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity which displays a conversation between you and another user.
 */
public class MessagingActivity extends ListActivity {
	String user;
	String password;
	String otherUser;

	private Bundle b;
	private EditText mMessageText;
	private Button mMessageSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);
		b = getIntent().getExtras();

		mMessageText = (EditText) findViewById(R.id.messaging_message);
		mMessageSend = (Button) findViewById(R.id.messaging_send_button);
		mMessageSend.setOnClickListener(mMessageSendHandler);

		String[] list = MessagingCore.queryMessages(b);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.messaging, menu);
		return true;
	}

	View.OnClickListener mMessageSendHandler = new View.OnClickListener() {
	    public void onClick(View v) {
	    	String message = mMessageText.getEditableText().toString().trim();
	    	MessagingCore.sendMessage(message, b);
			runOnUiThread(new Runnable() {
			    public void run() {
			    	mMessageText.setText("");
			    }
			});
	    }
	};
}
