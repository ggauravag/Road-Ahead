package com.example.gcmdemo;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.source.ServerUtility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String TAG = "GCM";
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private static final String MyPrefs = "MyPrefs";
	private Context context;
	private ProgressDialog progress;
	GoogleCloudMessaging gcm;
	String cred, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = getApplicationContext();
		final Button loginBtn = (Button)findViewById(R.id.loginButton);
		final EditText credText = (EditText)findViewById(R.id.credText);
		final EditText passwordText = (EditText)findViewById(R.id.passwordText);
		
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cred = credText.getText().toString();
				password = passwordText.getText().toString();
				loginGCM();
			}
		});
		
	}
	
	
	
	public void storeRegisterationId(String id)
	{
		final SharedPreferences prefs = getSharedPreferences(MyPrefs, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(REG_ID, id);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}	
	
	private void loginGCM()
	{
		
		final String regID = getStoredId();
		
		if(regID.isEmpty())
		{
			registerInBackGround();
		}
		else
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
			Log.i(TAG,"ID is saved : "+regID);
	
			new AsyncTask<String,String, String>() {
				
				@Override
				protected void onPreExecute() {
					progress = ProgressDialog.show(LoginActivity.this, "Processing", "Attempting to Login...",true,false);
				}
				
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					progress.dismiss();
					Toast.makeText(context, result, Toast.LENGTH_LONG).show();
					Log.i(TAG,"Used Device : "+result);
					if(result.contains("Logged"))
					{
						Intent intent = new Intent(context,NoticeActivity.class);
			    		startActivity(intent);
					}
				}

				
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
				
					String msg  = ServerUtility.login(cred, password, regID);
					
							
							
					Log.i(TAG,"Task Completed with status : "+msg);
					return msg;
				}
			}.execute();
			
		
			
		
		}
	}
	
	public void registerInBackGround()
	{
		
		new AsyncTask<String,String, String>() {
			
			@Override
			protected void onPreExecute() {
				progress = ProgressDialog.show(LoginActivity.this, "Processing", "Attempting to Login...",true,false);
			}
			
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				progress.dismiss();
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
				Log.i(TAG,"New device : "+result);
				if(result.contains("Logged"))
				{
					Intent intent = new Intent(context,NoticeActivity.class);
		    		startActivity(intent);
				}
			}

			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				if(gcm == null)
				{
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				int i = 0;
				String msg = "";
				while(i < 5)
				{
					try {
						String regID = gcm.register(ServerUtility.GOOGLE_ID);
						Log.i(TAG,"Device Registered : "+regID);
						storeRegisterationId(regID);
						msg = ServerUtility.login(cred, password, regID);
						
						break;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg = e.getMessage();
						i++;
					}
				}
				
				Log.i(TAG,"Task Completed with status : "+msg);
				
				return msg;
			}
		}.execute();
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
