package com.outerthoughts.notezap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EnterZapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.activity_enter_zap);
		Button zapSend = (Button)findViewById(R.id.zapSend);
		zapSend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				EditText textField = ((EditText)findViewById(R.id.zapEditText));
				String textValue = textField.getText().toString();
				
				Log.i("NoteZap", "Button is clicked with text: " + 
						textValue);

				Context context = getApplicationContext();
				Notification.Builder mBuilder =
						new Notification.Builder(context)
							.setNumber(1)
							.setAutoCancel(true)
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("Sending Zap")
							.setContentText(textValue);
				Intent notifyIntent = new Intent(context, PreferencesMainActivity.class);
				notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(pIntent);
				NotificationManager mNotificationManager =
						(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
				Log.i("NoteZap", "Notifiction setup");
				
				Intent msgIntent = new Intent(context, SendIntentService.class);
				msgIntent.putExtra(SendIntentService.PARAM_IN_ZAP_TEXT, textValue);
				startService(msgIntent);
				
//				Toast toast = Toast.makeText(context, "Button pressed: " + text.getText(), Toast.LENGTH_SHORT);
//				toast.show();
				finish();
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.enter_zap, menu);
//		return true;
//	}

}
