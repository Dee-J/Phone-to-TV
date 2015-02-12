package com.example.pt3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppListActivity extends Fragment  {
	PackageManager pm; 
	ListView listview;

	AppListActivity applistactivity= this;
	ArrayList<AppListElement> appinfolist = new ArrayList<AppListElement>();
	AppListElementsAdpater elemadapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}	

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(listview!=null){
			ViewGroup parent = (ViewGroup)listview.getParent();
			if(parent != null)
				parent.removeView(listview);
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		appinfolist.clear();
		View view=inflater.inflate(R.layout.activity_applist, container, false);
		listview =(ListView)view.findViewById(R.id.applist);
		pm=getActivity().getPackageManager();

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);

		for(ResolveInfo r: pkgAppsList)
		{
			String appname =(String) r.loadLabel(pm);
			Drawable drawableicon=	r.loadIcon(pm);
			String packname =r.activityInfo.packageName;
			appinfolist.add(new AppListElement(appname,
					packname.equals("com.android.contacts")?"com.android.phone":packname
					,drawableicon));
	
			}


		((ViewGroup)listview.getParent()).removeView(listview);
		listview.setAdapter(elemadapter=new AppListElementsAdpater(getActivity(), R.layout.applistelementlayout, appinfolist));
		return listview;


	}

	private class AppListElementsAdpater extends ArrayAdapter<AppListElement>{
		private SharedPreferences pref;
		private ArrayList<AppListElement> elements;

		public AppListElementsAdpater(Context context, int textViewResourceId, ArrayList<AppListElement> elements) {
			super(context, textViewResourceId, elements);
			this.elements = elements;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			pref=getActivity().getSharedPreferences("PT", Context.MODE_PRIVATE);


			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.applistelementlayout, null);
			}
			
			final AppListElement elem = elements.get(position);
			if(elem!=null)
			{
				ImageView imageview =(ImageView) v.findViewById(R.id.appimageview);
				imageview.setImageDrawable(elem.getDrawable());
				imageview.getLayoutParams().width=100;
				imageview.getLayoutParams().height=100;
				TextView txtview = (TextView) v.findViewById(R.id.nicktextview);
				txtview.setText(elem.getAppname());
				final CheckBox ckbox= (CheckBox)v.findViewById(R.id.appcheckbox);

				ckbox.setChecked(pref.getBoolean(elem.getApppkgname(), false));

				ckbox.setOnClickListener(new OnClickListener() {
					boolean clicked =false;
					@Override
					public void onClick(View v) {
						Log.d(elem.getApppkgname(),"clicked");
						Editor editor = pref.edit();
						editor.remove(elem.getApppkgname());
						clicked =!elem.getClicked();
						if(clicked)editor.putBoolean(elem.getApppkgname(), true);
						else editor.remove(elem.getApppkgname());
						editor.commit();
						elem.setClicked(clicked);
						elemadapter.notifyDataSetChanged();
					}

				});

			}
			return v;
		}
	}  


}