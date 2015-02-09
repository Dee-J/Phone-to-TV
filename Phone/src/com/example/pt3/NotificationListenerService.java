package com.example.pt3;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

public class NotificationListenerService extends android.service.notification.NotificationListenerService
{



	@Override
	public void onCreate()
	{
		Log.d("NotiListener","created");
		super.onCreate();   }


	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	//등록되었을때 하는 함수.
	@Override
	public void onNotificationPosted(StatusBarNotification sbn)
	{
		ArrayList<String> str= getText(sbn);
		Log.d("get text ",str.toString());

		JSONObject jobj = new JSONObject();
		final PackageManager pm = getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		try {
			ai = pm.getApplicationInfo(sbn.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			ai = null;
		}
		final String applicationName = (String) (ai != null ? pm
				.getApplicationLabel(ai) : "(unknown)");
		Log.d("App Name", applicationName);
		try {
			jobj.put("opcode", applicationName);
			jobj.put("title", str.get(0));
			jobj.put("mesg", str.get(1));
			jobj.put("nickname", "박범찬");
			jobj.put("color", "#FF08AB");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Intent i = new  Intent("com.example.pt3.TestBroadCastReceiver");

		i.putExtra("message", jobj.toString());
		sendBroadcast(i);
	}



	private ArrayList<String> getText(StatusBarNotification event) {

		ArrayList<String> result = new ArrayList<String>();
		try {
			Notification notification = event.getNotification();
			RemoteViews views = notification.contentView;
			Class<?> secretClass = views.getClass();

			Field outerFields[] = secretClass.getDeclaredFields();
			for (int i = 0; i < outerFields.length; i++) {
				if (!outerFields[i].getName().equals("mActions"))
					continue;

				outerFields[i].setAccessible(true);

				@SuppressWarnings("unchecked")
				ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
						.get(views);
				for (Object action : actions) {
					Field innerFields[] = action.getClass().getDeclaredFields();

					Object value = null;
					Integer type = null;
					for (Field field : innerFields) {
						field.setAccessible(true);
						if (field.getName().equals("value"))
							value = field.get(action);
						if (field.getName().equals("type"))
							type = field.getInt(action);
					}
					int cnt=1;
					if (type != null && type == 10)
					{
						if(cnt>=1)result.add( value.toString());
						cnt++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn)
	{
		Log.i("aa","removed");
	}


	////


}
