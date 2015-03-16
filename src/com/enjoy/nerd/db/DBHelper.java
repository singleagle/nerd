package com.enjoy.nerd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "my.db";  
    private static final int DATABASE_VERSION = 1;  

	public DBHelper(Context context, long loginUIN) {
		super(context, String.valueOf(loginUIN)+".db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 
		 db.execSQL("CREATE TABLE IF NOT EXISTS immessages (" + 
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "cid INTEGER, "
					+ "sender TEXT, "
					+ "type SMALLINT, "
					+ "content TEXT, "
					+ "send_state SMALLINT, "
					+ "rcv_state SMALLINT," 
					+ "createtime BIGINT);");
		 
	     db.execSQL("CREATE TABLE imconversations (" 
                  + "_id  INTEGER PRIMARY KEY AUTOINCREMENT," 
                  + "subject TEXT,"
                  + "avatar TEXT,"
                  + "type SMALLINT DEFAULT 0,"
                  + "members TEXT,"
                  + "recipient TEXT,"
                  + "updatetime BIGINT DEFAULT 0," 
                  + "unread_count SMALLINT DEFAULT 0," 
                  + "alert_switcher SMALLINT DEFAULT 0," 
                  + "last_msg TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
