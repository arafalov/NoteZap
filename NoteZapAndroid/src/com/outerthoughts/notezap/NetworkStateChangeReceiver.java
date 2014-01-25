package com.outerthoughts.notezap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("NoteZap", "Network state changed. Thread id: " + Thread.currentThread().getId() + ". Network is: " + (isNetworkAvailable(context)?"available":"not available"));
		Intent sendZap = new Intent(context, BatchedSendService.class);
		sendZap.setAction(BatchedSendService.ACTION_CHECK_NETWORK);
		context.startService(sendZap);

	}

	private boolean isNetworkAvailable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) 
	      context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 

}
