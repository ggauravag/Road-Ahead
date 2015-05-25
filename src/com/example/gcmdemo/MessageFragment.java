package com.example.gcmdemo;

import java.util.ArrayList;

import com.source.Message;
import com.source.MessageUtility;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ExpandableListView;

public class MessageFragment extends Fragment 
{
	
	private static final String TAG = "GCM";
	private static Context context;
	private ExpandableListView messageView;
	
	
	public MessageFragment(Context context)
	{
		this.context = context;
	}
	
	
	public void loadMessages()
	{
		MessageUtility utility = new MessageUtility(this.context);
		utility.open();
		final ArrayList<Message> messages = (ArrayList<Message>) utility.getAllMessages();
		utility.close();
		Log.i(TAG,"Number of messages : "+messages.size());
		ExpandableListAdapter adapter = new ExpandableListAdapter(context, messages);
		messageView.setAdapter(adapter);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.message_fm, container, false);
		messageView = (ExpandableListView)v.findViewById(R.id.expandableListView1);
		loadMessages();
		return v;
	}
}
