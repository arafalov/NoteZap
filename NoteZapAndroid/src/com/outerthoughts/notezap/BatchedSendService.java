package com.outerthoughts.notezap;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BatchedSendService extends Service{

	private static final String ZAP_ENDPOINT = "https://zapier.com/hooks/catch/n/11lxg/";

	public static final String ACTION_ADD_ZAP = "add_zap";
	public static final String ACTION_CHECK_NETWORK = "check_network";
	public static final String PARAM_IN_ZAP_TEXT = "param_in_zap";

	private LinkedBlockingDeque<String> queue = null;
	private SenderRunnable senderRunnable;
	private Thread senderThread;

	@Override
	public IBinder onBind(Intent intent) {
		// do nothing - we don't support binding
		return null;
	}

	@Override
	public void onCreate() {
		queue = new LinkedBlockingDeque<String>();
		senderRunnable = new SenderRunnable(queue);
		
		senderThread = new Thread(senderRunnable);
		senderThread.start();
		Log.i("NoteZap-Batch", "Created queue: " + queue.toString());		
	}
	
	@Override
	public void onDestroy() {
//		senderRunnable.markFinished();
		senderThread.interrupt();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//NOTE: We are still in the main thread here.
		String action = intent.getAction();
		if (action.equals(ACTION_ADD_ZAP))
		{
			String zap = intent.getStringExtra(PARAM_IN_ZAP_TEXT);
			queue.add(zap);
		} else if (action.equals(ACTION_CHECK_NETWORK))
		{
			synchronized (senderRunnable) {
				senderRunnable.notify(); //wake it up				
			}
		}
		
		Log.i("NoteZap-Batch", "StartCommand: " + action + " with thread: " + Thread.currentThread().getId() + " and queue: " + queue.toString());
		return START_STICKY; //TODO may need to change to deal with being killed
	}
	

	private class SenderRunnable implements Runnable
	{
		private LinkedBlockingDeque<String> queue;
//		private boolean isFinished = false;

		public SenderRunnable(LinkedBlockingDeque<String> queue)
		{
			this.queue = queue;
		}

//		public void markFinished() {
//			isFinished  = true;
//		}

		@Override
		public void run(){
			String zap;
			try{
				while (true) // until interrupted
				{
					Log.i("NoteZap-Runnable", "Take an item, when available");
					zap = queue.take(); //wait if nothing is available
					Log.i("NoteZap-Runnable", "Got an item");
					if (NetworkChecker.isNetworkAvailable(BatchedSendService.this))
					{
						if (!sendZapNow(zap)) //push stuff back to queue if we had an exception?
						{
							Log.i("NoteZap-Runnable", "Failed to send item: '" + zap + "' Will push item back and wait to retry");
							queue.push(zap);
							synchronized (this) {
								this.wait(60000); //wait for 60 seconds or until network check
							}
							Log.i("NoteZap-Runnable", "Wake up from retry wait");
						}
					}
					else
					{
						queue.push(zap); //put it back in
						Log.i("NoteZap-Runnable", "No network. Wait.");
						synchronized (this) {
							this.wait();							
						}
						Log.i("NoteZap-Runnable", "Wake up from wait for network status change.");
					}
				}
			}
			catch(InterruptedException ex)
			{
				Log.i("NoteZap-Runnable", "Interrupted. Quiting");
			}
		}
		
		private boolean sendZapNow(String zap) {
			Log.i("NoteZap-Runnable", "Sending zap: " + zap);
			HttpClient myClient = new DefaultHttpClient();
			HttpGet myConnection = new HttpGet(ZAP_ENDPOINT + "?zap=" + zap);

			String result = null;
			JSONObject json = null;
			try {
				HttpResponse response = myClient.execute(myConnection);
				result = EntityUtils.toString(response.getEntity(), "UTF-8");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}


			try{
				json = new JSONObject(result);
				Log.i("ZAP result json: ", json.toString(2)); 	             
			} catch ( JSONException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}
}
