package com.example.pt3;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {

	private static String vibrate = "vibrate";
	private static String silent = "silent";
	private static String denyCallsmsSend = "denyCallsmsSend";
	private static String TAG="GCMInternetService";
	public GCMIntentService() {
		super(Constants.PROJECT_ID);
		Log.v(TAG,"created");

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
	

	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
	
		String title = null;
		try {
			title = java.net.URLDecoder.decode(arg1.getExtras().getString("opcode"),"utf-8");
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, arg0.toString());
		Log.i(TAG, title);
		AudioManager aManager = (AudioManager) arg0.getSystemService(Context.AUDIO_SERVICE);
		
		if(title.equals(vibrate)){
			 
	         aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	         	Log.d(TAG,"vibrate");
				Toast.makeText(arg0, "vibrate", Toast.LENGTH_SHORT).show();
		}

		if(title.equals(silent)){
			aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			Log.d(TAG,"silent");
			
			Toast.makeText(arg0, "silent", Toast.LENGTH_SHORT).show();
			
		}
		
		if(title.equals(denyCallsmsSend)){
			Log.d(TAG,"deny");
			Toast.makeText(arg0, "deny", Toast.LENGTH_SHORT).show();
			
		}
	}

	@Override
	protected void onRegistered(Context arg0, String regId) {
		// TODO Auto-generated method stub
		/**
		 */

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		/**
		 */

	}

}
