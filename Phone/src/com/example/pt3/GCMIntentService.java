package com.example.pt3;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	AudioManager aManager=null;
	private static String vibrate="vibrate";
	private static String silent="silent";
	private static String deny="denyCallsmsSend";
	private static String onsound="sound";
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
			Log.i("title",deny);
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
