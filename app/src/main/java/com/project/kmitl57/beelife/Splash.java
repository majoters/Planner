package com.project.kmitl57.beelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.supakorn.new_beelife_login.Login;

public class Splash extends Activity {
	Handler handler;
	Runnable runnable;
	long delay_time;
	long time = 3000L;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		handler = new Handler();

		runnable = new Runnable() { 
	         public void run() { 
	        	 Intent intent = new Intent(Splash.this, Login.class);
					startActivity(intent);
					finish();
	         } 
	    };
	}
	
	public void onResume() {
		super.onResume();
		delay_time = time;
	    handler.postDelayed(runnable, delay_time);
	    time = System.currentTimeMillis();

	}
	
	public void onStop() {
		super.onStop();
		handler.removeCallbacks(runnable);
	    time = delay_time - (System.currentTimeMillis() - time);

	}
}
