package com.example.pt3;

import android.graphics.drawable.Drawable;

public class AppListElement {

	private String appname;
	private String apppkgname;
	private Drawable drawable;
	private boolean clicked;
	
	
	public AppListElement(String appname, String apppkgname,Drawable drawable){
		this.appname=appname;
		this.apppkgname = apppkgname;
		this.drawable=drawable;
		clicked=false;
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
	public boolean getClicked(){
		return clicked;
		
	}
	public void setClicked(boolean clicked){
		this.clicked= clicked;
	}
}
