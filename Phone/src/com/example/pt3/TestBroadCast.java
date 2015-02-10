package com.example.pt3;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class TestBroadCast extends BroadcastReceiver{
	private ConvergenceUtil mConvergenceUtil= ConvergenceUtil.getInstance();

	static Object Rock =null;
	Thread t=null;
	@Override

	public void onReceive(final Context context, Intent intent) {

		
		final Intent fintent = intent;
		t= new Thread(
				new Runnable(){


					public void run(){
						
						SharedPreferences pref = context.getSharedPreferences("PT", Context.MODE_PRIVATE);

						if(getWifiName(context).equals(pref.getString("wifi", "null")))
							mConvergenceUtil.setIpAddress(pref.getString("appURL", "nullURL"));
						else return;	

						if(Rock==null){
							Rock=new Object();
							mConvergenceUtil.connect();
							if(200!=mConvergenceUtil.connect())
							{
								Rock =null;
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}


						String message = fintent.getStringExtra("message");
						JSONObject jobj = null ;
						try {
							jobj = new JSONObject(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Rock=null;
						}

						mConvergenceUtil.sendMessage(jobj);
					}

					private String getWifiName(Context context) {
						// TODO Auto-generated method stub
						WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
						if (manager.isWifiEnabled()) {
							WifiInfo wifiInfo = manager.getConnectionInfo();
							if (wifiInfo != null) {
								DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
								if (state == DetailedState.CONNECTED || state == DetailedState.OBTAINING_IPADDR) {
									return wifiInfo.getSSID();
								}
							}		
						}
						return null;
					}
				}
				);
		t.start();
	}


}
