package com.source;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MessageUtility 
{
	private SQLiteDatabase database;
	private DBHelper helper;
	private String[] tableColumns = {DBHelper.COLUMN_1, DBHelper.COLUMN_2, DBHelper.COLUMN_3, DBHelper.COLUMN_4};
	public static final String TAG = "GCM";
	
	public MessageUtility(Context context)
	{
		helper = new DBHelper(context);
	}
	
	public void open()
	{
		database = helper.getWritableDatabase();
	}
	
	public void close()
	{
		helper.close();
	}
	
	public boolean deleteAll()
	{
		boolean success = true;
		database.delete(DBHelper.TABLE_NAME, null, null);
		return success;
	}
	
	
	public boolean deleteMessage(Message msg)
	{
		boolean isDeleted = false;
		
		int id = msg.getId();
		int result = database.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_1 + " = " + id, null);
		if(result == 1)
		{
			isDeleted = true;
			Log.i("GCM","Message deleted successfully");
		}
		return isDeleted;
	}
	
	
	public List<Message> getAllMessages()
	{
		List<Message> messages = new ArrayList<Message>();
		Cursor cursor = database.query(DBHelper.TABLE_NAME, tableColumns, null, null, null, null, DBHelper.COLUMN_1 + " desc");
		int i = 0;
		Log.i(TAG,"Getting messages from database");
		while(cursor.moveToNext())
		{
			messages.add(new Message(cursor.getInt(0),cursor.getString(1),cursor.getString(2), cursor.getString(3)));
			Log.i("GCM",cursor.getInt(0)+cursor.getString(1)+cursor.getString(2)+ cursor.getString(3));
		}
		cursor.close();
		return messages;
	}
	
	
	
	public Message insertMessage(int id,String title,String message, String time)
	{
		
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_1, id);
		values.put(DBHelper.COLUMN_2, title);
		values.put(DBHelper.COLUMN_3, message);
		values.put(DBHelper.COLUMN_4, time);
		
		Long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
		
		if(insertId == -1)
			return null;
		Log.i(TAG,"Message Inserted into database.");
		//Message msg = new Message(id,title,message,time);
		
		Cursor cursor = database.query(DBHelper.TABLE_NAME, tableColumns , DBHelper.COLUMN_1 + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		Message msg = getMessage(cursor);
		cursor.close();
		return msg;
	}
	
	public Message getMessage(Cursor cursor)
	{
		Message msg = new Message();
		msg.setId(cursor.getInt(0));
		msg.setTitle(cursor.getString(1));
		msg.setMessage(cursor.getString(2));
		msg.setTime(cursor.getString(3));
		Log.i("GCM","Message inserted successfully");
		return msg;
	}
}
