package com.example.pt3;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Converter {

	private static String callappname= "com.android.phone";


	public static JSONObject execute(ArrayList<String> str, String applicationName,String pkgname,Context context) {
		SharedPreferences pref = context.getSharedPreferences("PT", Context.MODE_PRIVATE);
		String nick;
		if((nick=pref.getString("nickname", "")).equals(""))nick="�⺻�����";
		
		boolean isprivate =	pref.getBoolean("Private", false);
		
		boolean iscallapp = callappname.equals(pkgname);
		
		String color = pref.getString("color", "#ffffff");
		if(iscallapp){
		Log.d("is call app",callappname+"  "+applicationName);	
		}else{
			Log.d("isn't call app",callappname+"  "+applicationName);	
		}
		
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("opcode", applicationName);
			jobj.put("title", 
					isprivate?
							iscallapp?
									"�͸�"
									:"�˸�"
										:str.get(0)

					);
			jobj.put("mesg", 
					isprivate?
							iscallapp?
									"��ȭ�Խ��ϴ�"
									:"�˸��� �Խ��ϴ�"
										:
											iscallapp?
													"��ȭ�Խ��ϴ�":str.get(1)
					);
			jobj.put("nickname", nick);
			jobj.put("color", color);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return jobj;


	}

}
