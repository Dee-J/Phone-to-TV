package com.example.pt3;

import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author Christian Bauer
 */
public class MainActivity extends Activity {

    private static Logger log = Logger.getLogger(MainActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

     
    }

    public void ButtonClicked(View V){
    	
    	startActivity(new Intent(this,BrowserActivity.class));
    	
    }
}