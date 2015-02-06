package com.example.pt3;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.StrictMode;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

public class MyListenerService extends NotificationListenerService {

	private ConvergenceUtil mConvergenceUtil;
	private ConnectThread mConnectThread;
	private ReceiveThread mReceiveThread;
	JSONObject obj = new JSONObject();

	Object Rock =new Object();
	public void onCreate() {
		super.onCreate();
		mConvergenceUtil=new ConvergenceUtil();

		SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
		String text=	prefs.getString(getResources().getString(R.string.appURL), "");
		Log.d("Extracted-URL",text);
		prefs.edit().clear();
		mConvergenceUtil.setIpAddress(text);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			obj.put("opcode", "hello");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Thread( new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				startConnectThread();
				startReceiveThread();
				sendHello();
			}

			private void sendHello() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(5000);
					while(true) {mConvergenceUtil.sendMessage(obj);Thread.sleep(5000);}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}).start();
		Log.i("created", "NLService");

	}

	public void onDestroy() {
		new Thread( new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mConvergenceUtil.disconnect();
				stopReceiveThread();
			}
		}).start();

		super.onDestroy();

	}

	public void onNotificationPosted(final StatusBarNotification sbn) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

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
				ArrayList<String> str =  getText(sbn);
				Log.d("Text", str.get(0)+"  "+str.get(1));

				JSONObject jobj = new JSONObject();

				try {
					jobj.put("opcode", "recvNoti");
					jobj.put("title", str.get(0));
					jobj.put("mesg",str.get(1));
					jobj.put("nickname", "박범찬");
					jobj.put("color", "#FF08AB");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("text",str.toString());
				synchronized(Rock){
					mConvergenceUtil.sendMessage(jobj);
				}
			}
		}).start();
	}

	public void onNotificationRemoved(final StatusBarNotification sbn) {

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
	/**
	 * TV와의 연결을 시도하기 위한 thread 
	 */
	private class ConnectThread extends Thread {
		boolean m_runFlag = true;

		public void setRunFlag(boolean bFlag)
		{
			m_runFlag = bFlag;
		}

		@Override
		public void run() {
			while (m_runFlag) {
				synchronized (getApplicationContext()) {					
					mConvergenceUtil.connect();
					stopConnectThread();
				}
			}			
			super.run();			
		}
	}

	private void startConnectThread()
	{
		stopConnectThread();
		mConnectThread	= new  ConnectThread();

		if(null!=mConnectThread)
		{
			mConnectThread.start();
		}
	}

	private void stopConnectThread()
	{
		if(null!=mConnectThread)
		{
			Thread tmpThread = mConnectThread;
			mConnectThread.setRunFlag(false);
			mConnectThread = null;
			tmpThread.interrupt();
		}
	}

	/**
	 * TV로 부터의 메시지를 수신하기 위한 thread
	 */	
	private class ReceiveThread extends Thread
	{	
		boolean m_runFlag = true;

		public void setRunFlag(boolean bFlag)
		{
			m_runFlag = bFlag;
		}

		public void run()
		{
			while(m_runFlag)
			{
				mConvergenceUtil.receiveMessage();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void startReceiveThread()
	{
		stopReceiveThread();
		mReceiveThread = new ReceiveThread();
		mReceiveThread.start();
	}

	private void stopReceiveThread()
	{
		if(null!=mReceiveThread)
		{
			Thread tmpThread = mReceiveThread;
			mReceiveThread.setRunFlag(false);
			mReceiveThread = null;
			tmpThread.interrupt();
		}
	}
}
