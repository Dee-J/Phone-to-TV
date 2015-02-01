package com.example.pt2;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NLService extends NotificationListenerService{
//
//	private String Nick="박범찬";
//	
//	
//	private final String TAG = "ConvergenceUtil";
//
//	private static final String JSON_TYPE 	= "type";
//	private static final String JSON_VALUE 	= "msg";
//	public static final String RECEIVE = "com.example.pt2";
//	public static final ConvergenceUtil conv = new ConvergenceUtil();
	
	public void onCreate(){
		super.onCreate();

		
		Log.i("created", "NLService");
	}
	public void onDestroy(){
		super.onDestroy();
		
	}
	public void  onNotificationPosted(final StatusBarNotification sbn){
//		Log.i("got", sbn.getPackageName());
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final PackageManager pm = getApplicationContext().getPackageManager();
				ApplicationInfo ai;
				try {
					ai = pm.getApplicationInfo( sbn.getPackageName(), 0);
				} catch (final NameNotFoundException e) {
					ai = null;
				}
				final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
				Log.d("App Name", applicationName);
		 
				Bundle bundle =sbn.getNotification().extras;
				Object val = bundle.get("android.text");
				String str=String.format("%s", val);
//				Object val2 = bundle.get("android.title");
//				String str2=String.format("%s", val2);
				JSONObject jobj= new JSONObject();
				try {
					jobj.put("opcode", "recvNoti");
					jobj.put("sender", "박범찬");
					jobj.put("pno", "01041330060");
					jobj.put("mesg", str);
					jobj.put("color", "#000000");
					jobj.put("nickname", "엄마보고싶다");
					} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			Log.d("catch",jobj.toString());
			
				for(String k:bundle.keySet()){
					
//					Object o= bundle.get(k);
//					String str3= String.format("%s: %s", k,o);
//					Log.d("debug", str3);
				}

				
			}
		}
				).start();
	}

	public void onNotificationRemoved(final StatusBarNotification sbn){		

	}

}
