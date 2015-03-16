package com.enjoy.nerd.db;

import com.enjoy.nerd.AccountManager;

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
	public static final Uri CONTENT_URI = Uri.parse("content://com.enjoy.nerd");
	
	public static final String AUTHORITY = "com.enjoy.nerd";
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);  
	private static final int IMMESSAGES = 1;
	private static final int IMMESSAGES_ID = 2;
	private static final int CONVERSATIONS = 3;
	private static final int CONVERSATIONS_ID = 4;
	
	static{
		MATCHER.addURI(AUTHORITY, "immessages", IMMESSAGES);
		MATCHER.addURI(AUTHORITY, "immessages/#", IMMESSAGES_ID);
		MATCHER.addURI(AUTHORITY, "imconversations", CONVERSATIONS);
		MATCHER.addURI(AUTHORITY, "imconversations/#", CONVERSATIONS_ID);
	}
	
	@Override
	public boolean onCreate() {
		long loginUin = AccountManager.getInstance(getContext()).getLoginUIN();
		if(loginUin == 0){
			return false;
		}
		helper = new DBHelper(getContext(), loginUin);
		
		return true;
	}
	
	private void ensureDBHelperCreate(){
		if(helper != null){
			return;
		}
		
		long loginUin = AccountManager.getInstance(getContext()).getLoginUIN();
		if(loginUin == 0){
			throw new IllegalStateException("you must login!!"); 
		}
		helper = new DBHelper(getContext(), loginUin);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		ensureDBHelperCreate();
		
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
		
		case CONVERSATIONS:
			qb.setTables("imconversations");
			break;
			
			
		case CONVERSATIONS_ID:
			qb.setTables("imconversations");
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
        	ret.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
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
			
		case CONVERSATIONS:
			return "vnd.android.cursor.dir/imconversations";
			
		case CONVERSATIONS_ID:
			return "vnd.android.cursor.item/imconversations";
		default:
			throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		ensureDBHelperCreate();
		SQLiteDatabase db = helper.getWritableDatabase();
 
		long rowId = 0;
		switch (MATCHER.match(uri)) {
		case IMMESSAGES:
			rowId = db.insert("immessages", null, values);
			break;
			
		case CONVERSATIONS:
			rowId = db.insert("imconversations", null, values);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown URI " + uri); 
		}
		
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			return noteUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		ensureDBHelperCreate();
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		long rowId = 0;
		switch (MATCHER.match(uri)) {
			
		case IMMESSAGES_ID:
	        String segment = uri.getPathSegments().get(1);
            rowId = Long.parseLong(segment);
            if (TextUtils.isEmpty(where)) {
                where = "_id=" + segment;
            } else {
                where = "_id=" + segment + " AND (" + where + ")";
            }
            count = db.delete("immessages", where, whereArgs);
			break;
			
		case CONVERSATIONS_ID:
	        String idString = uri.getPathSegments().get(1);
            rowId = Long.parseLong(idString);
            if (TextUtils.isEmpty(where)) {
                where = "_id=" + idString;
            } else {
                where = "_id=" + idString + " AND (" + where + ")";
            }
            count = db.delete("imconversations", where, whereArgs);
			break;

		default:
			throw new UnsupportedOperationException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		ensureDBHelperCreate();
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		long rowId = 0;
		String segment;
		switch (MATCHER.match(uri)) {
		case IMMESSAGES_ID:
			segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("immessages", values, "_id=" + rowId, null);
			break;
			
		case CONVERSATIONS_ID:
			segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("imconversations", values, "_id=" + rowId, null);
			break;	

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);  
		}
		
		getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		return count;
	}

}
