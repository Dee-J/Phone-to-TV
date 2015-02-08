package com.example.pt3;

import org.json.JSONException;
import org.json.JSONObject;






import org.teleal.cling.support.igd.callback.GetStatusInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class TestBroadCast extends BroadcastReceiver{
	private ConvergenceUtil mConvergenceUtil= ConvergenceUtil.getInstance();
	private ConnectThread mConnectThread;
	private ReceiveThread mReceiveThread;
	static Object Rock = new Object();
	Thread t=null;
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		final Intent fintent = intent;
		t= new Thread(
				new Runnable(){


					public void run(){
						
							startConnectThread();
							startReceiveThread();
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String message = fintent.getStringExtra("message");
						Log.d("BroadCastmessage: ",message);
						JSONObject jobj = null ;
						try {
							jobj = new JSONObject(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mConvergenceUtil.sendMessage(jobj);
					}});
		t.start();
	}

	/**
	 * TV와의 연결을 시도하기 위한 thread
	 */
	private class ConnectThread extends Thread {
		boolean m_runFlag = true;

		public void setRunFlag(boolean bFlag) {
			m_runFlag = bFlag;
		}

		@Override
		public void run() {
			while (m_runFlag) {
				synchronized (Rock) {
					mConvergenceUtil.connect();
					stopConnectThread();
				}
			}
			super.run();
		}
	}

	private void startConnectThread() {
		stopConnectThread();
		mConnectThread = new ConnectThread();

		if (null != mConnectThread) {
			mConnectThread.start();
		}
	}

	private void stopConnectThread() {
		if (null != mConnectThread) {
			Thread tmpThread = mConnectThread;
			mConnectThread.setRunFlag(false);
			mConnectThread = null;
			tmpThread.interrupt();
		}
	}

	/**
	 * TV로 부터의 메시지를 수신하기 위한 thread
	 */
	private class ReceiveThread extends Thread {
		boolean m_runFlag = true;

		public void setRunFlag(boolean bFlag) {
			m_runFlag = bFlag;
		}

		public void run() {
			while (m_runFlag) {
				mConvergenceUtil.receiveMessage();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void startReceiveThread() {
		stopReceiveThread();
		mReceiveThread = new ReceiveThread();
		mReceiveThread.start();
	}

	private void stopReceiveThread() {
		if (null != mReceiveThread) {
			Thread tmpThread = mReceiveThread;
			mReceiveThread.setRunFlag(false);
			mReceiveThread = null;
			tmpThread.interrupt();
		}
	}

}
