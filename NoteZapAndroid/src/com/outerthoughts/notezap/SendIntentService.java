package com.outerthoughts.notezap;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SendIntentService extends IntentService {

	public static final String PARAM_IN_ZAP_TEXT = "zap";

	private static final String ZAP_ENDPOINT = "https://zapier.com/hooks/catch/n/11lxg/";
	//private static final String ZAP_ENDPOINT = "https://zapier.com/hooks/catch/n/11lsfasdfxg/"; //wrong endpoint for failure testing - not failing currently
	
//	private LinkedList<String> queue = new LinkedList<String>();

	public SendIntentService() {
		super("SendIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String zap = intent.getStringExtra(PARAM_IN_ZAP_TEXT);
//		queue.push(zap);
		
	      HttpClient myClient = new DefaultHttpClient();
	      HttpGet myConnection = new HttpGet(ZAP_ENDPOINT + "?zap=" + zap);
	         
	      String result = null;
	      JSONObject json = null;
	      try {
	            HttpResponse response = myClient.execute(myConnection);
	            result = EntityUtils.toString(response.getEntity(), "UTF-8");
	             
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	         
	         
	        try{
	            json = new JSONObject(result);
	            Log.i("ZAP result json: ", json.toString(2)); 	             
	        } catch ( JSONException e) {
	            e.printStackTrace();                
	        }
	}

}
