package com.example.pt3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

/* Page3Activity.java */
public class PrivacyActivity extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_privacy, container, false);
		Switch privacyswitch = (Switch)layout.findViewById(R.id.privacyswitch);
		privacyswitch.setActivated(true);
		privacyswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences pref =getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
				Editor privateEditor = pref.edit();
				if(isChecked)
					privateEditor.putBoolean("Private", true);
				else
					privateEditor.remove("Private");
			
					privateEditor.commit();
				
			}
		});
		return layout;
	}
}
