package com.example.gcmdemo;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.source.Message;
import com.source.MessageUtility;
import com.source.ServerUtility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


@SuppressLint("NewApi") public class GCMNotificationIntentService extends IntentService {

	private static final String TAG = "GCM";
	private NotificationManager notifManager;
	
	public GCMNotificationIntentService() {
		super("GCMIntentService");
		// TODO Auto-generated constructor stub
	}

	
	private void sendNotification(int id,String title,String msg,String time)
	{
		if(id == -1)
		{
			Log.i(TAG,"There is some error");
			return;
		}
		MessageUtility utility = new MessageUtility(getApplicationContext());
		utility.open();
		Message msgobj = utility.insertMessage(id, title, msg, time);
		utility.close();
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
		intent.putExtra("id",msgobj.getId());
		intent.putExtra("title", msgobj.getTitle());
		intent.putExtra("message", msgobj.getMessage());
		intent.putExtra("time", msgobj.getTime());
		Log.i(TAG,intent.getStringExtra("message")+"---"+intent.getStringExtra("time"));
		PendingIntent msgViewIntent = PendingIntent.getActivity(getApplicationContext(), Activity.RESULT_OK, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notif;
		Bitmap ratbmp = BitmapFactory.decodeResource(getResources(), R.drawable.rat);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
		{
			Notification.Builder builder = new Notification.Builder(getApplicationContext());
			
			//builder.setStyle(new Notification.BigTextStyle().bigText("Cloud Notification"));
			builder.setTicker("New Notification");
			builder.setContentTitle(msgobj.getTitle());
			builder.setContentText(msgobj.getMessage());
			builder.setSmallIcon(R.drawable.rat);
			builder.setLargeIcon(ratbmp);
			builder.setContentIntent(msgViewIntent);
			notif = builder.build();
		}
		else
		{
			NotificationCompat.Builder bu = new NotificationCompat.Builder(getApplicationContext());
			bu.setTicker("New Notification");
			bu.setContentTitle(msgobj.getTitle());
			bu.setContentText(msgobj.getMessage());
			bu.setSmallIcon(R.drawable.rat);
			bu.setSound(alarmSound);
			bu.setLargeIcon(ratbmp);
			bu.setContentIntent(msgViewIntent);
			notif = bu.build();
		}
		
		
		notif.flags = notif.flags | Notification.FLAG_AUTO_CANCEL;
		/*notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_LIGHTS;
		notif.defaults |= Notification.DEFAULT_VIBRATE;*/
		notif.defaults |= Notification.DEFAULT_ALL;
		notifManager.notify(1,notif);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
    	GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
    	String message = gcm.getMessageType(intent);
    	
    	if(!extras.isEmpty())
    	{
    		if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(message))
    		{
    			sendNotification(-1,"Send Error : "+extras.toString(),"","");
    		}
    		if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(message))
    		{
    			sendNotification(-1,"Deleted Message : "+extras.toString(),"","");
    		}
    		if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(message))
    		{
    			String msgText = (String) extras.get(ServerUtility.MESSAGE_TEXT);
    			String time = extras.getString(ServerUtility.MESSAGE_TIME);
    			String title = extras.getString(ServerUtility.TITLE_ID);
    			String id = extras.getString(ServerUtility.MESSAGE_ID);
    			Log.i(TAG,"DATA IS - "+id+" : "+title+" : "+msgText+" : "+time);
    			sendNotification(Integer.parseInt(id),title,msgText,time);
    		}
    		GCMBroadcastReceiver.completeWakefulIntent(intent);
    	}
    	
		
	}

}
