package com.example.gcmdemo;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

	private static final String TAG = "GCM";
	
    @Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
    	
    	ComponentName component = new ComponentName(context.getPackageName(), GCMNotificationIntentService.class.getName());
    	startWakefulService(context, intent.setComponent(component));
    	setResultCode(Activity.RESULT_OK);
		Log.i(TAG,"Notif received");
	}

}
