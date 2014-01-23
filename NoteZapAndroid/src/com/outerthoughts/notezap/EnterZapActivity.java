package com.outerthoughts.notezap;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.sax.TextElementListener;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
				EditText text = ((EditText)findViewById(R.id.zapEditText));
				
				Log.i("NoteZap", "Button is clicked with text: " + 
						text.getText().toString());

				Context context = getApplicationContext();
				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(context)
							.setContentInfo("1")
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("Sending Zap")
							.setContentText(text.getText().toString());
				Intent notifyIntent = new Intent(context, PreferencesMainActivity.class);
				notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(pIntent);
				NotificationManager mNotificationManager =
						(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
				Log.i("NoteZap", "Notifiction setup");
				
				
				Toast toast = Toast.makeText(context, "Button pressed: " + text.getText(), Toast.LENGTH_SHORT);
				toast.show();
				finish();
//				text.setText("Updated");
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
