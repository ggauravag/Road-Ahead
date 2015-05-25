package com.example.gcmdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

public class MessageActivity extends Activity {

	TextView msgView,timeView,titleView;
	String message,time,title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.activity_title);
		Intent i = getIntent();
		message = i.getStringExtra("message");
		time = i.getStringExtra("time");
		title = i.getStringExtra("title");
		Log.i("GCM","Message is : "+message+"@"+time);
		msgView = (TextView)findViewById(R.id.msgView);
		timeView = (TextView)findViewById(R.id.timeView);
		titleView = (TextView)findViewById(R.id.titleV);
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		msgView.setText(message);
		timeView.setText(time);
		timeView.setText(title);
	}

	
}
