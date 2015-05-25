package com.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String TAG = "GCM";
	
	public static final String DATABASE_NAME = "RoadAhead.db";
	public static final String TABLE_NAME = "Message";
	public static final String COLUMN_1 = "_id";
	public static final String COLUMN_2 = "title";
	public static final String COLUMN_3 = "message";
	public static final String COLUMN_4 = "time";
	
	
	public DBHelper(Context con)
	{
		
		super(con,DATABASE_NAME,null,1);
		Log.i("GCM","Database opened/created.");
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(TAG,"Table being created !");
		db.execSQL("create table "+TABLE_NAME+" ("+COLUMN_1+" integer primary key, "+COLUMN_2+" text not null, "+COLUMN_3+" text not null, "+COLUMN_4+" text not null );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i(TAG,"Database being upgraded");
		db.execSQL("Drop table if exists "+TABLE_NAME);
		onCreate(db);
	}
	

}
