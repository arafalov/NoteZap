package com.outerthoughts.notezap;

import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BatchedSendService extends Service{

	public static final String ACTION_ADD_ZAP = "add_zap";
	public static final String ACTION_CHECK_NETWORK = "check_network";
	public static final String PARAM_IN_ZAP_TEXT = "param_in_zap";

	private LinkedList<String> queue = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// do nothing - we don't support binding
		return null;
	}

	@Override
	public void onCreate() {
		queue = new LinkedList<String>();
		Log.i("NoteZap-Batch", "Created queue: " + queue.toString());
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//NOTE: We are still in the main thread here.
		String action = intent.getAction();
		if (action.equals(ACTION_ADD_ZAP))
		{
			String zap = intent.getStringExtra(PARAM_IN_ZAP_TEXT);
			queue.add(zap);
		}
		Log.i("NoteZap-Batch", "StartCommand: " + action + " with thread: " + Thread.currentThread().getId() + " and queue: " + queue.toString());
		return START_STICKY; //TODO may need to change to deal with being killed
	}
	

}
