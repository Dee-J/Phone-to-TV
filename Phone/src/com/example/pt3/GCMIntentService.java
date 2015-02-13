package com.example.pt3;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	AudioManager aManager=null;
	private static String vibrate="vibrate";
	private static String silent="silent";
	private static String deny="denyCallsmsSend";
	private static String onsound="sound";
	static String lastcallNum;
	public GCMIntentService() {
		super(Constants.PROJECT_ID);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub



	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(aManager ==null) 
			aManager = (AudioManager) arg0.getSystemService(Context.AUDIO_SERVICE);

		String title = null;
		try {
			title = java.net.URLDecoder.decode(arg1.getExtras().getString("opcode"),"utf-8");
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("vyujong_onMessage", arg0.toString());
		Log.i("vyujong_onMessage", title);

		if(title.equals(vibrate))
			aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		if(title.equals(silent))
			aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		if(title.equals(onsound))
			aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		if(title.equals(deny))
		{
			Log.d("시발 디나이 받음","ㅇㅇ");
			String srvcName = Context.TELEPHONY_SERVICE;
			TelephonyManager	 telephonyManagerx = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			try{
				Class	clazz = Class.forName(telephonyManagerx.getClass().getName());
				Method method = clazz.getDeclaredMethod("getITelephony");
				method.setAccessible(true);
				ITelephony telephonyService = (ITelephony) method.invoke(telephonyManagerx);
				telephonyService.endCall();
			}catch(Exception e){
				Log.e("번호가 잘못되었다",lastcallNum);
				e.printStackTrace();
			}
		}
	}




	@Override
	protected void onRegistered(Context arg0, String regId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
