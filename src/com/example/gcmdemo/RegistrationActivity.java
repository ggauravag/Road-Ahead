package com.example.gcmdemo;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.source.ServerUtility;

import android.app.ActionBar;
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
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	GoogleCloudMessaging gcm;
	Context context;
	String regId;
	
	private ProgressDialog progress;
	private static final String TAG = "GCM";
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private static final String MyPrefs = "MyPrefs";
	
	String name,email,mobile,pass,confirmpass;
	
	
	
	
	public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
	
	
	private void registerGCM()
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
					progress = ProgressDialog.show(RegistrationActivity.this, "Processing", "Registering..",true,false);
				}
				
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					progress.dismiss();
					Toast.makeText(context, result, Toast.LENGTH_LONG).show();
					if(result.equals("Registration Successful !"))
					{
						Intent i = new Intent(context, NoticeActivity.class);
						startActivity(i);
					}
				}

				
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
				
					String msg = "";
				
							int state = ServerUtility.registerAccount(regID, name, mobile, email, pass);

							if(state == 2)
							{
								msg = "Account already exists !";
							}
							if(state == 1)
							{
								msg = "Registration Successful !";
							}
							if(state == 0)
							{
								msg = "Unable to register ! Try Again. ";
							}
							
					Log.i(TAG,"Task Completed with status : "+msg);
					return msg;
				}
			}.execute();
			
		}
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
	
	
	
	public void registerInBackGround()
	{
		
		new AsyncTask<String,String, String>() {
			
			@Override
			protected void onPreExecute() {
				progress = ProgressDialog.show(RegistrationActivity.this, "Processing", "Registering..",true,false);
			}
			
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				progress.dismiss();
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
				if(result.equals("Registration Successful !"))
				{
					Intent i = new Intent(context, NoticeActivity.class);
					startActivity(i);
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
						int status = ServerUtility.registerAccount(regID, name, mobile, email, pass);
						if(status == 2)
						{
							msg = "Account already exists !";
						}
						if(status == 1)
						{
							msg = "Registration Successful !";
						}
						if(status == 0)
						{
							msg = "Unable to register ! Try Again. ";
						}
						
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = getApplicationContext();
        
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.activity_title);
        
        Button registerBtn = (Button)findViewById(R.id.registerButton);
      
        final TextView text = (TextView)findViewById(R.id.titleView);
        text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			}
		});
        
        
        final EditText nameET = (EditText)findViewById(R.id.nameText);
        final EditText mobileET = (EditText)findViewById(R.id.mobile);
        final EditText emailET = (EditText)findViewById(R.id.email);
        final EditText passET = (EditText)findViewById(R.id.password);
        final EditText confirmET = (EditText)findViewById(R.id.confirmPassword);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                mobile = mobileET.getText().toString();
                email = emailET.getText().toString();
                pass = passET.getText().toString();
                confirmpass = confirmET.getText().toString();

                if(name.equals("") || mobile.equals("") || email.equals("") || pass.equals("") || confirmpass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Any Field Can't be left Blank",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(!isValidEmail(emailET.getText()))
                    {
                        Toast.makeText(getApplicationContext(),"Email ID is invalid !",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (!pass.equals(confirmpass)) {
                            Toast.makeText(getApplicationContext(), "Passwords should be same !", Toast.LENGTH_SHORT).show();
                        } else {
                        	
                        	registerGCM();
                        	
                        }
                    }
                }


            }
        });

    }
}
