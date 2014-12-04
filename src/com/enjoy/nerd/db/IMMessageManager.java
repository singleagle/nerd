package com.enjoy.nerd.db;

import org.jivesoftware.smack.packet.Message;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.db.IMConversation.Columns;
import com.enjoy.nerd.remoterequest.xmpp.IMMessageSendReq;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class IMMessageManager {
	
	
	static public Uri addMessage(ContentResolver resolver, IMMessageSendReq sendRequest,
			IMConversation conv,  long sender){
		if(conv == null){
			return null;
		}
		
		conv.setLastMessage(sendRequest.getTextContent());
		conv.setLastUpdateTime(System.currentTimeMillis());
		ContentValues values = new ContentValues(2);
		values.put(Columns.LAST_MSG, conv.getLastMessage() );
		values.put(Columns.UPDATE_TIME, conv.getLastUpdateTime());
		resolver.update(ContentUris.withAppendedId(IMConversation.Columns.CONTENT_URI, conv.getId()), 
				values, null, null);
		
		IMMessage imMsg = new IMMessage(sendRequest, sender, conv.getId());
		ContentValues contentValues = imMsg.createContentValues();
		Uri contentUri = resolver.insert(IMMessage.Columns.CONTENT_URI, contentValues);
		return contentUri;
	}
	
	static public Uri addMessage(ContentResolver resolver, Message message){
		
		String from = message.getFrom();
		boolean fromRoom = isRoomId(message.getFrom()); 
		String uin = from.split("@")[0];
		Cursor cursor = resolver.query(IMConversation.Columns.CONTENT_URI, IMConversation.Columns.IMCONVERSATION_QUERY_COLUMNS, 
				IMConversation.Columns.WHERE_RECIPIENT, new String []{uin},
				IMConversation.Columns.DEFAULT_SORT_ORDER);
		int cid = 0;
		if(cursor != null && cursor.getCount() != 0){
			cursor.moveToFirst();
			cid = cursor.getInt(Columns.IMCONVERSATION_ID_INDEX);
			int unread = cursor.getInt(Columns.IMCONVERSATION_UNREAD_COUNT_INDEX);
			cursor.close();
			ContentValues values = new ContentValues(3);
			values.put(Columns.LAST_MSG, message.getBody());
			values.put(Columns.UPDATE_TIME, System.currentTimeMillis());
			values.put(Columns.UNREAD_COUNT, unread + 1);
			resolver.update(ContentUris.withAppendedId(IMConversation.Columns.CONTENT_URI, cid), 
					values, null, null);
			
		}else{
			IMConversation conv = new IMConversation(from, fromRoom, from);
			conv.setLastMessage(message.getBody());
			conv.setLastUpdateTime(System.currentTimeMillis());
			conv.setUnreadCount(1);
			ContentValues content = conv.createContentValues();
			Uri uri = resolver.insert(IMConversation.Columns.CONTENT_URI, content);
			cid = (int)ContentUris.parseId(uri);
			conv.setId(cid);
		}
		IMMessage imMsg = new IMMessage(message, cid, fromRoom);
		ContentValues content = imMsg.createContentValues();
		return resolver.insert(IMMessage.Columns.CONTENT_URI, content);
	}
	
	
	
	public static IMConversation getIMConversation(ContentResolver resolver, String recipient, boolean create){
		if(recipient == null){
			return null;
		}
		
		IMConversation conv = null;
		Cursor cursor = resolver.query(IMConversation.Columns.CONTENT_URI, IMConversation.Columns.IMCONVERSATION_QUERY_COLUMNS, 
					IMConversation.Columns.WHERE_RECIPIENT, new String []{recipient},
					IMConversation.Columns.DEFAULT_SORT_ORDER);
		
		if(cursor != null && cursor.getCount() != 0){
			cursor.moveToFirst();
			conv = new IMConversation(cursor);
			cursor.close();
		}else if(create){
			conv = new IMConversation(recipient, IMMessageManager.isRoomId(recipient), recipient);
			conv.setLastUpdateTime(System.currentTimeMillis());
			conv.setLastMessage(null);
			ContentValues content = conv.createContentValues();
			Uri uri = resolver.insert(IMConversation.Columns.CONTENT_URI, content);
			conv.setId((int)ContentUris.parseId(uri));
		}
		return conv;
	}
	
	
	public static IMConversation getIMConversation(ContentResolver resolver, long cid){
		IMConversation conv = null;
		
		Uri uri = ContentUris.withAppendedId(IMConversation.Columns.CONTENT_URI, cid);
		Cursor cursor = resolver.query(uri, IMConversation.Columns.IMCONVERSATION_QUERY_COLUMNS, 
				null,null, IMConversation.Columns.DEFAULT_SORT_ORDER);
		
		if(cursor != null){
			cursor.moveToFirst();
			conv = new IMConversation(cursor);
			cursor.close();
		}
		
		return conv;
	}
	
	public static boolean isRoomId(String jid){
		if(jid.contains("/chatroom")){
			return true;
		}
		return false;
	}
}
