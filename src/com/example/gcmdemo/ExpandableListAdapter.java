package com.example.gcmdemo;

import java.util.ArrayList;

import com.source.Message;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;



public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Message> messages;
	
	public ExpandableListAdapter(Context context, ArrayList<Message> messages)
	{
		this.context = context;
		this.messages = messages;
	}
	
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return messages.get(arg0).getMessage();
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String details = (String) getChild(groupPosition, 0);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_detail, null);	
		}
		
		TextView message = (TextView)convertView.findViewById(R.id.message);
		message.setText(details);
	
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return messages.get(arg0).getTitle();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String title = messages.get(groupPosition).getTitle();
		String time = messages.get(groupPosition).getTime();
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_item, null);
		}
		TextView titleView = (TextView)convertView.findViewById(R.id.title);
		TextView timeView = (TextView)convertView.findViewById(R.id.time);
		titleView.setTypeface(null,Typeface.BOLD);
		titleView.setText(title);
		timeView.setText(time);
		timeView.setTypeface(null, Typeface.ITALIC);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
