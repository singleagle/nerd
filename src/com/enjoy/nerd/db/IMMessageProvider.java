package com.enjoy.nerd.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class IMMessageProvider extends ContentProvider {

	private DBHelper helper;
	public static final String AUTHORITY = "com.enjoy.nerd";
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);  
	private static final int IMMESSAGES = 1;
	private static final int IMMESSAGES_ID = 2;
	
	static{
		MATCHER.addURI(AUTHORITY, "immessages", IMMESSAGES);
		MATCHER.addURI(AUTHORITY, "immessages/#", IMMESSAGES_ID);
	}
	
	@Override
	public boolean onCreate() {
		helper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch (MATCHER.match(uri)) {
		case IMMESSAGES:
			qb.setTables("immessages");
			break;
		
		case IMMESSAGES_ID:
			qb.setTables("immessages");
			qb.appendWhere("_id=");
			qb.appendWhere(uri.getPathSegments().get(1));
			break;
			
		default:  
            throw new IllegalArgumentException("unknow uri" + uri.toString()); 

		}
		
		SQLiteDatabase db = helper.getReadableDatabase();
        Cursor ret = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        if(ret != null){
        	ret.setNotificationUri(getContext().getContentResolver(), uri);
        }
		
		return ret;
	}

	@Override
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case IMMESSAGES:
			return "vnd.android.cursor.dir/immessages";
			
		case IMMESSAGES_ID:
			return "vnd.android.cursor.item/immessages";	
		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (MATCHER.match(uri) != IMMESSAGES) {  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
		
		long rowId = db.insert("immessages", null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(uri, null);
			return noteUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		long rowId = 0;
		switch (MATCHER.match(uri)) {
		case IMMESSAGES:
			count = db.delete("immessages", where, whereArgs);
			break;
			
		case IMMESSAGES_ID:
	        String segment = uri.getPathSegments().get(1);
            rowId = Long.parseLong(segment);
            if (TextUtils.isEmpty(where)) {
                where = "_id=" + segment;
            } else {
                where = "_id=" + segment + " AND (" + where + ")";
            }
            count = db.delete("alarms", where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		long rowId = 0;
		switch (MATCHER.match(uri)) {
		case IMMESSAGES_ID:
			String segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("immessages", values, "_id=" + rowId, null);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);  
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
