package com.example.pt3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/* Page3Activity.java */
public class ActivateActivity extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_activate, container, false);
		return layout;
	}
}
