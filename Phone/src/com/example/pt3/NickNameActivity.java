package com.example.pt3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.pt3.ColorPickerView.OnColorChangedListener;


/* Page1Activity.java */
public class NickNameActivity extends Fragment  {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		SharedPreferences pref = getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
		final Editor editor = pref.edit();
		if(pref.getString("nickname", null)==null){
			editor.putString("nickname", "기본사용자");
			editor.commit();
		}

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		 
		
		SharedPreferences pref = getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
		final Editor editor = pref.edit();
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_nickname, container, false);
	
		addColorpicker(layout);
		
		final EditText textdialog = (EditText) layout.findViewById(R.id.nickedittext);
		textdialog.setHint(pref.getString("nickname", "기본사옹자"));
		textdialog.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Log.i("textset",s.toString());
				editor.putString("nickname", s.toString());
				
				editor.commit();
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			

			}});
		
		return layout;
	}

	private void addColorpicker(RelativeLayout layout) {
		// TODO Auto-generated method stub
		ColorPickerView cpv= new ColorPickerView(getActivity());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT	, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.BELOW, R.id.colorpickerText);
		params.setMargins(10, 69, 10, 10);
		cpv.setLayoutParams(params);
		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			
			@Override
			public void onColorChanged(int color) {
				// TODO Auto-generated method stub
				String hexColor = String.format("#%06X", (0xFFFFFF & color));
				SharedPreferences pref= getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
				pref.edit().putString("color", hexColor).commit();
				
			}
		});
		layout.addView(cpv);
	}

	
}
