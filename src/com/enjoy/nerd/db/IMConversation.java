package com.enjoy.nerd.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class IMConversation {
	static final private String MEMBER_SPLITER = ";";
	
	private int mId;
	private String mSubject;
	private String mThumbAvatarName;
	private boolean mType; //是否是群聊??
	private String mRecipient; //群聊则是room的uin, 单聊则是对方用户的uin
	private ArrayList<String> mMembers = new ArrayList<String>(); 
	private long mLastUpdateTime;
	private String mLastMessage;
	private int mUnreadCount;
	private boolean mMessageAlert;
	
	public IMConversation(Cursor cursor){
		mId = cursor.getInt(Columns.IMCONVERSATION_ID_INDEX);
		mSubject = cursor.getString(Columns.IMCONVERSATION_SUBJECT_INDEX);
		mThumbAvatarName = cursor.getString(Columns.IMCONVERSATION_AVATAR_INDEX);
		mType = cursor.getShort(Columns.IMCONVERSATION_TYPE_INDEX) == 1;
		mRecipient = cursor.getString(Columns.IMCONVERSATION_RECIPIENT_INDEX);
		String members = cursor.getString(Columns.IMCONVERSATION_MRMNRTS_INDEX);
		if(members != null){
			String[] memArray = members.split(MEMBER_SPLITER);
			for(String mem : memArray){
				mMembers.add(mem);
			}
			
		}
		
		mLastUpdateTime = cursor.getLong(Columns.IMCONVERSATION_LAST_UPDATE_INDEX);
		mUnreadCount = cursor.getShort(Columns.IMCONVERSATION_UNREAD_COUNT_INDEX);
		mMessageAlert = cursor.getShort(Columns.IMCONVERSATION_ALERT_INDEX) == 1;
		mLastMessage = cursor.getString(Columns.IMCONVERSATION_LAST_MSG_INDEX);
	}
	
	
	public IMConversation(String mSubject, boolean mType,
			String mRecipient) {
		this.mId = -1;
		this.mSubject = mSubject;
		this.mType = mType;
		this.mRecipient = mRecipient;
		mUnreadCount = 0;
		mMessageAlert = true;
	}


	public ContentValues createContentValues(){
		ContentValues content = new ContentValues(10);
		content.put(Columns.SUBJECT, mSubject);
		content.put(Columns.AVATAR, mThumbAvatarName);
		content.put(Columns.TYPE, mType);
		content.put(Columns.RECIPIENT, mRecipient);
		StringBuilder sb = new StringBuilder();
		if(mMembers != null){
			for(String member: mMembers){
				sb.append(member).append(MEMBER_SPLITER);
			}
		}
		content.put(Columns.MEMBERS, sb.toString());
		content.put(Columns.UPDATE_TIME, mLastUpdateTime);
		content.put(Columns.LAST_MSG, mLastMessage);
		content.put(Columns.UNREAD_COUNT, mUnreadCount);
		content.put(Columns.ALERT_SWITCHER, mMessageAlert);
		return content;
	}
	
	public int getId() {
		return mId;
	}

	public void setId(int id){
		mId = id;
	}

	public String getSubject() {
		return mSubject;
	}

	public IMConversation addMember(String memberNickName){
		if(!mMembers.contains(memberNickName)){
			mMembers.add(memberNickName);
		}
		return this;
	}

	public ArrayList<String> getMembers() {
		return mMembers;
	}

	
	public String getThumbAvatarName() {
		return mThumbAvatarName;
	}


	public void setThumbAvatarPath(String mThumbAvatarName) {
		this.mThumbAvatarName = mThumbAvatarName;
	}


	public String getLastMessage() {
		return mLastMessage;
	}


	public void setLastMessage(String mLastMessage) {
		this.mLastMessage = mLastMessage;
	}


	public int getUnreadCount() {
		return mUnreadCount;
	}


	public void setUnreadCount(int mUnreadCount) {
		this.mUnreadCount = mUnreadCount;
	}


	public boolean isMessageAlert() {
		return mMessageAlert;
	}


	public void setMessageAlert(boolean mMessageAlert) {
		this.mMessageAlert = mMessageAlert;
	}


	public void setLastUpdateTime(long mLastUpdateTime) {
		this.mLastUpdateTime = mLastUpdateTime;
	}

	public long getLastUpdateTime() {
		return mLastUpdateTime;
	}


	public String getRecipient() {
		return mRecipient;
	}

	
	
	public static class Columns implements BaseColumns {
	    public static final Uri CONTENT_URI = Uri.parse("content://com.enjoy.nerd/imconversations");
	    
	    public static final String SUBJECT = "subject";
	    public static final String AVATAR = "avatar";
	    public static final String TYPE = "type";
	    public static final String RECIPIENT = "recipient";
	    public static final String MEMBERS = "members";
	    public static final String UPDATE_TIME = "updatetime";
	    public static final String LAST_MSG = "last_msg";
	    public static final String UNREAD_COUNT = "unread_count";
	    public static final String ALERT_SWITCHER = "alert_switcher";
	    
        public static final String DEFAULT_SORT_ORDER = UPDATE_TIME + " ASC";
        
        public static final String[] IMCONVERSATION_QUERY_COLUMNS = {
            _ID,SUBJECT, AVATAR, TYPE, RECIPIENT, MEMBERS,UPDATE_TIME,LAST_MSG, UNREAD_COUNT, ALERT_SWITCHER
        };
        
        public static final String  WHERE_RECIPIENT =  RECIPIENT + "=?";
        
	    public static final int IMCONVERSATION_ID_INDEX = 0;
	    public static final int IMCONVERSATION_SUBJECT_INDEX = 1;
	    public static final int IMCONVERSATION_AVATAR_INDEX = 2;
	    public static final int IMCONVERSATION_TYPE_INDEX = 3;
	    public static final int IMCONVERSATION_RECIPIENT_INDEX = 4;
	    public static final int IMCONVERSATION_MRMNRTS_INDEX = 5;
	    public static final int IMCONVERSATION_LAST_UPDATE_INDEX = 6;
	    public static final int IMCONVERSATION_LAST_MSG_INDEX = 7;
	    public static final int IMCONVERSATION_UNREAD_COUNT_INDEX = 8;
	    public static final int IMCONVERSATION_ALERT_INDEX = 9;
	    
	}
}
