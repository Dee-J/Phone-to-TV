package com.example.pt3;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends TabActivity
{
	
   
	Context context = this;
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
	  super.onCreate(savedInstanceState);
      
	
	  setContentView(R.layout.activity_main);
      
      ((Button) findViewById(R.id.btn_setting_notification)).setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
            
         }
      });
      
      ((Button) findViewById(R.id.btn_find_Device)).setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
           
            startActivity(new Intent(context, BrowserActivity.class));
            
         }
      });
      
   }
   
}
