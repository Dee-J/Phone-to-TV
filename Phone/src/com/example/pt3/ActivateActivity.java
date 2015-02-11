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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;

/* Page3Activity.java */
public class ActivateActivity extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_activate, container, false);
		
		Switch activateswitch = (Switch)layout.findViewById(R.id.activateswitch);
		
		activateswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences pref =getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
				Editor activateEditor = pref.edit();
				if(isChecked)
					activateEditor.putBoolean("Activate", true);
				else
					activateEditor.remove("Activate");
				activateEditor.commit();
				
			}
		});
		return layout;
	}
	
	
}
