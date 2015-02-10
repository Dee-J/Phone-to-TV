package com.example.pt3;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/* Page2Activity.java */
public class NotificatonTestActivity extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_notificationtest, container, false);

		layout.findViewById(R.id.testnotibutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotificationManager mNotificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder mBuilder =
					    new NotificationCompat.Builder(getActivity())
					    .setSmallIcon(R.drawable.notification_icon)
					    .setContentTitle("My notification")
					    .setContentText("Hello World")
					    .setTicker("PT 테스트알림");
				Notification notification = mBuilder.build();
				notification.flags|= Notification.FLAG_AUTO_CANCEL;
				
				mNotificationManager.notify((int) (Math.random()/100), mBuilder.build());
			}
		});
		return layout;
	}
}
