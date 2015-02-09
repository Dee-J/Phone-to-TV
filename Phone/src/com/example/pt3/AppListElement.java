package com.example.pt3;

import android.graphics.drawable.Drawable;

public class AppListElement {

	private String appname;
	private String apppkgname;
	private Drawable drawable;

	
	
	public AppListElement(String appname, String apppkgname,Drawable drawable){
		this.appname=appname;
		this.apppkgname = apppkgname;
		this.drawable=drawable;
		
	}
	
	public String getAppname() {
		return appname;
	}
	public String getApppkgname() {
		return apppkgname;
	}
	public Drawable getDrawable() {
		return drawable;
	}
}
