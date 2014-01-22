package com.outerthoughts.notezap;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	private static final String SHOW_POPUP_DIALOG_ACTION = "com.outerthoughts.notezap.showaddzap";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		//assume only one instance of widget (what about Home AND LockScreen?)
		for (int widgetId : appWidgetIds) {
	  		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget_view);
	  		Intent intent = new Intent(context,WidgetProvider.class);
	  		intent.setAction(SHOW_POPUP_DIALOG_ACTION);
	  		
	  		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	  		views.setOnClickPendingIntent(R.id.addzap, pendingIntent);
	  		appWidgetManager.updateAppWidget(widgetId, views);
		}
		
//		views.removeAllViews(R.id.layout_cats);
//		
//		RemoteViews btn = new RemoteViews(context.getPackageName(), R.layout.button);
//		btn.setTextViewText(R.id.remote_button, "Success");
//		views.addView(R.id.layout_cats, btn);
//
//		RemoteViews btn2 = new RemoteViews(context.getPackageName(), R.layout.button);
//		btn2.setTextViewText(R.id.remote_button, "Success 2");
//		views.addView(R.id.layout_cats, btn2);
		//as a list for categories and another list (or buttons) for actions. 

//  		appWidgetManager.updateAppWidget(appWidgetIds[0], views);
  		

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SHOW_POPUP_DIALOG_ACTION))
		{
			Log.i("NoteZap", "Clicked");
			Intent popupIntent = new Intent(context, EnterZapActivity.class);
			popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(popupIntent);
		}
		super.onReceive(context, intent);
	}

	
	
}
