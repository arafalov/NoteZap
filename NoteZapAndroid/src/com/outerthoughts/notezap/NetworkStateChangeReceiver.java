package com.outerthoughts.notezap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("NoteZap", "Network state changed. Thread id: " + Thread.currentThread().getId() + ". Network is: " + (NetworkChecker.isNetworkAvailable(context)?"available":"not available"));
		Intent sendZap = new Intent(context, BatchedSendService.class);
		sendZap.setAction(BatchedSendService.ACTION_CHECK_NETWORK);
		context.startService(sendZap);

	}


}
