package com.example.pt3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

public class FlashWidgetActivity extends AppWidgetProvider {

	private static final String ACTION_FLASH_ON ="com.flash.widget.ON";
	private static final String ACTION_FLASH_OFF ="com.flash.widget.OFF";
	private ComponentName flashWidget;
	private RemoteViews views = null;

	boolean flashControl =true;
	SharedPreferences pref;
	private Intent intent;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// TODO Auto-generated method stub
		Log.e("Widget State","onUpdate");
		pref = context.getSharedPreferences("PT", Context.MODE_PRIVATE);
		flashWidget = new ComponentName(context, FlashWidgetActivity.class);
		views = new RemoteViews(context.getPackageName(), R.layout.main);

		if(flashControl==false){

			views.setImageViewResource(R.id.flash_btn, R.drawable.off);
			intent = new Intent(ACTION_FLASH_ON);

		}else if(flashControl == true){

			views.setImageViewResource(R.id.flash_btn, R.drawable.on);
			intent = new Intent(ACTION_FLASH_OFF);
		}
		// Flash Intent
		PendingIntent onPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.flash_btn, onPendingIntent);

		appWidgetManager.updateAppWidget(flashWidget, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("Widget State","onReceive");

		String action = intent.getAction();

		if(action.equals(ACTION_FLASH_ON)){
			Log.e("Flash state", intent.getAction());
			try{
				flashControl = true;

				AppWidgetManager manager = AppWidgetManager.getInstance(context);
				this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, FlashWidgetActivity.class)));

			}catch (Exception e) {
				// TODO: handle exception
				Log.e("Flash state", "Flash ON Exception");
			}
		}else if(action.equals(ACTION_FLASH_OFF)){
			Log.e("Flash state", intent.getAction());   
			try{

				flashControl = false;

				AppWidgetManager manager = AppWidgetManager.getInstance(context);
				this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, FlashWidgetActivity.class)));

			}catch (Exception e) {
				// TODO: handle exception
				Log.e("Flash state", "Flash OFF Exception");
			}
		}else{
			super.onReceive(context, intent);
		}

		pref.edit().putBoolean("Activate", flashControl).commit();

	}
}