package com.outerthoughts.notezap;

import android.os.Bundle;
import android.app.Activity;
import android.sax.TextElementListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EnterZapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		setContentView(R.layout.activity_enter_zap);
		Button zapSend = (Button)findViewById(R.id.zapSend);
		zapSend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				EditText text = ((EditText)findViewById(R.id.zapEditText));
				
				Log.i("NoteZap", "Button is clicked with text: " + 
						text.getText().toString());
				text.setText("Updated");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_zap, menu);
		return true;
	}

}
