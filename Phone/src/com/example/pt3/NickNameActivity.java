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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pt3.ColorPickerView.OnColorChangedListener;


/* Page1Activity.java */
public class NickNameActivity extends Fragment  {

	View colorview;
	int selectedcolor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onAttach(Activity activity){
		super.onAttach(activity);
		SharedPreferences pref = getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
		final Editor editor = pref.edit();
		if(pref.getString("nickname", null)==null){
			editor.putString("nickname", "default");
			editor.commit();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final SharedPreferences pref = getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
		final Editor editor = pref.edit();
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_nickname, container, false);
		addColorpicker(layout);

		final EditText textdialog = (EditText) layout.findViewById(R.id.nickedittext);
		textdialog.setHint(pref.getString("nickname", "default"));

		layout.findViewById(R.id.colorView).setBackgroundColor(Integer.parseInt(pref.getString("color", "#ffffff").substring(1),16)|0xFF000000);
		layout.findViewById(R.id.regibutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putString("nickname",textdialog.getText().toString()).commit();
				String hexColor = String.format("#%06X", (0xFFFFFF & selectedcolor));

				pref.edit().putString("color", hexColor).commit();
				Toast.makeText(getActivity(), "닉네임과 색상이 적용됩니다.", Toast.LENGTH_LONG).show();

			}
		});;


		return layout;
	}

	private void addColorpicker( RelativeLayout layout) {
		// TODO Auto-generated method stub
		ColorPickerView cpv= new ColorPickerView(getActivity());
		final SharedPreferences pref= getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT	, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.BELOW, R.id.colorpickerText);
		params.setMargins(30, 69, 30, 10);
		cpv.setLayoutParams(params);
		cpv.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void onColorChanged(int color) {
				// TODO Auto-generated method stub
				selectedcolor =color;

				View v = getActivity().findViewById(R.id.colorView);
				v.setBackgroundColor(color|0xFF000000);
				v.invalidate();
			}
		});


		layout.addView(cpv);
	}



}
