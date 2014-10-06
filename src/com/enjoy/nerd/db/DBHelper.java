package com.enjoy.nerd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "my.db";  
    private static final int DATABASE_VERSION = 1;  

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 String sql = "CREATE TABLE IF NOT EXISTS immessages" + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, owner BIGINT, sender TEXT, recipient TEXT, group_name TEXT, "+ 
				"type SMALLINT, content TEXT, send_state SMALLINT, rcv_state SMALLINT," +
				"createtime BIGINT);";
		 db.execSQL(sql);
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
