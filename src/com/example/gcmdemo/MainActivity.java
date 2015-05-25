package com.example.gcmdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ProgressBar;


public class MainActivity extends Activity {
	
	private static final String TAG = "GCM";
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private static final String MyPrefs = "MyPrefs";
	private static Context context;
	private ProgressBar bar;
	Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        context = getApplicationContext();
        this.findViewById(android.R.id.content).setBackgroundColor(Color.rgb(60, 16, 238));
        bar = (ProgressBar)findViewById(R.id.progressBar1);
        
    }

    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	bar.setVisibility(ProgressBar.VISIBLE);
    	final String id = getStoredId();
    	
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
		    	{
		    		Thread.sleep(2000);
		    	}
		    	catch(Exception e)
		    	{
		    		
		    	}
		    	if(id.isEmpty())
		    	{
		    		Intent intent = new Intent(context,RegistrationActivity.class);
		    		startActivity(intent);
		    	}
		    	else
		    	{
		    		Intent intent = new Intent(context,NoticeActivity.class);
		    		startActivity(intent);
		    	}
			}
		}).start();
    	
    	/*try
    	{
    		Thread.sleep(5000);
    	}
    	catch(Exception e)
    	{
    		
    	}
    	if(id.isEmpty())
    	{
    		//Intent intent = new Intent(context,RegistrationActivity.class);
    		//startActivity(intent);
    	}
    	else
    	{
    		//Intent intent = new Intent(context,NoticeActivity.class);
    		//startActivity(intent);
    	}*/
    }

    private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d(TAG,
					"RA : I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}
    
    
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    
    public String getStoredId()
	{
		final SharedPreferences prefs = getSharedPreferences(MyPrefs, MODE_PRIVATE);
		String regID = prefs.getString(REG_ID, "");
		
		if(regID.isEmpty())
		{
			return "";
		}
		
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "RA : App version changed.");
			return "";
		}
		return regID;
	}

    
}
