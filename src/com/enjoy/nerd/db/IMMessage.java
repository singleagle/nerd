package com.enjoy.nerd.db;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DelayInfo;

import com.enjoy.nerd.remoterequest.xmpp.IMMessageSendReq;
import com.enjoy.nerd.usercenter.AccountManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class IMMessage {
	public static final String PROP_TIME = "creattime";
	public static final String PROP_TYPE = "type";
	
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_PIC =  1;
	public static final int TYPE_VOICE = 2;
	public static final int TYPE_VIDEO = 4;
	public static final int TYPE_NUM = 5;
	
	public static final int SEND_STATE_SENDING = 0;
	public static final int SEND_STATE_SUCCESS = 1;
	public static final int SEND_STATE_FAILURE = 2;
	
	public static final int RCV_STATE_RECIEVING = 0;
	public static final int RCV_STATE_UNREAD = 1;
	public static final int RCV_STATE_READ = 2;
	
	private int mId;
	private long mOwner;
	private String mSender;
	private String mRecipient;
	private String mGroup;
	private int mType;
	private String mContent;
	private int mSendState;
	private int  mRecieveState;
	private long mCreateTime;
	
	
	public IMMessage(Cursor cursor){
		mId = cursor.getInt(Columns.IMMESSAGE_ID_INDEX);
		mOwner = cursor.getLong(Columns.IMMESSAGE_OWNER_INDEX);
		mSender = cursor.getString(Columns.IMMESSAGE_SENDER_INDEX);
		mRecipient = cursor.getString(Columns.IMMESSAGE_RECIPIENT_INDEX);
		mGroup = cursor.getString(Columns.IMMESSAGE_GROUP_INDEX);
		mType = cursor.getShort(Columns.IMMESSAGE_TYPE_INDEX);
		mContent = cursor.getString(Columns.IMMESSAGE_CONTENT_INDEX);
		mSendState = cursor.getShort(Columns.IMMESSAGE_SEND_STATE_INDEX);
		mRecieveState = cursor.getShort(Columns.IMMESSAGE_RCV_STATE_INDEX);
		mCreateTime = cursor.getLong(Columns.IMMESSAGE_CREATETIME_INDEX);
	}
	
	public IMMessage(Message recieveMessage, long owner, boolean fromGroup){
		mId = -1;
		mOwner =  owner;
		if(fromGroup){
			mSender = recieveMessage.getFrom().split("/")[1];
			mGroup =  recieveMessage.getFrom().split("@")[0];;
		}else{
			mSender = recieveMessage.getFrom().split("@")[0];
			mGroup =  mSender;
		}
		
		mRecipient = String.valueOf(owner);
		String typeValue = (String)recieveMessage.getProperty(PROP_TYPE);
		mType = TYPE_TEXT;
		if(typeValue != null){
			try{
				mType = Integer.valueOf(typeValue);
			}catch(NumberFormatException e ){
				e.printStackTrace();
			}
			
			if(mType < 0 || mType > TYPE_NUM ){
				mType = TYPE_TEXT;
			}
		}
		mContent =  recieveMessage.getBody();
		mSendState = SEND_STATE_SUCCESS;
		mRecieveState = RCV_STATE_RECIEVING;
		
         DelayInfo timestamp = (DelayInfo) recieveMessage.getExtension("delay", "urn:xmpp:delay");  
         if (timestamp == null){  
             timestamp = (DelayInfo) recieveMessage.getExtension("x",  "jabber:x:delay");  
         }
         if (timestamp != null){ 
             mCreateTime = timestamp.getStamp().getTime(); 
         }else if(recieveMessage.getProperty(PROP_TIME) != null){
        	 mCreateTime = Long.valueOf((String)recieveMessage.getProperty(PROP_TIME));
         }else{  
        	 mCreateTime = System.currentTimeMillis(); 
         }
	}
	
	public IMMessage(IMMessageSendReq sendRequest, long owner){
		mId = -1;
		mOwner = owner;
		mSender = String.valueOf(owner);
		mRecipient = sendRequest.getUid();
		mGroup = mRecipient;
		mType = sendRequest.getMsgType();
		mContent = sendRequest.getTextContent();
		mSendState = SEND_STATE_SENDING;
		mRecieveState = RCV_STATE_READ;
		mCreateTime = sendRequest.getCreateTime();
	}
	
	public ContentValues createContentValues(){
		ContentValues content = new ContentValues();
		
		content.put(IMMessage.Columns.OWNER, mOwner);
		content.put(IMMessage.Columns.SENDER, mSender);
		content.put(IMMessage.Columns.RECIPIENT, mRecipient);
		content.put(IMMessage.Columns.GROUP, mGroup);
		content.put(IMMessage.Columns.TYPE, mType);
		content.put(IMMessage.Columns.CONTENT, mContent);
		content.put(IMMessage.Columns.SEND_STATE, mSendState);
		content.put(IMMessage.Columns.RCV_STATE, mRecieveState);
		content.put(IMMessage.Columns.CREATETIME, mCreateTime);
		
		return content;
	}
	
	public boolean isIncomingMsg(){
		String ownerStr = String.valueOf(mOwner);
		return ownerStr.equals(mSender);
	}
	
	public int getId() {
		return mId;
	}

	public long getOwner() {
		return mOwner;
	}

	public String getSender() {
		return mSender;
	}

	public String getRecipient() {
		return mRecipient;
	}

	public String getGroup() {
		return mGroup;
	}

	public int getType() {
		return mType;
	}

	public String getContent() {
		return mContent;
	}

	public int getSendState() {
		return mSendState;
	}

	public int getRecieveState() {
		return mRecieveState;
	}

	public long getCreateTime() {
		return mCreateTime;
	}



	public static class Columns implements BaseColumns {
	    public static final Uri CONTENT_URI = Uri.parse("content://com.enjoy.nerd/immessages");
	    
	    
	    public static final String OWNER = "owner";
	    public static final String SENDER = "sender";
	    public static final String RECIPIENT = "recipient";
	    public static final String GROUP = "group_name";
	    public static final String TYPE = "type";
	    public static final String CONTENT = "content";	  
	    public static final String SEND_STATE = "send_state";
	    public static final String RCV_STATE = "rcv_state";
	    public static final String CREATETIME = "createtime";
	    
        public static final String DEFAULT_SORT_ORDER = CREATETIME + " ASC";
        
        public static final String[] IMMESSAGE_QUERY_COLUMNS = {
            _ID, OWNER, SENDER, RECIPIENT, GROUP, TYPE, CONTENT, SEND_STATE, RCV_STATE, CREATETIME
        };
        
        public static final String  WHERE_GROUP = OWNER + "=? and " + GROUP + "=?";
	    public static final int IMMESSAGE_ID_INDEX = 0;
	    public static final int IMMESSAGE_OWNER_INDEX = 1;
	    public static final int IMMESSAGE_SENDER_INDEX = 2;
	    public static final int IMMESSAGE_RECIPIENT_INDEX = 3;
	    public static final int IMMESSAGE_GROUP_INDEX = 4;
	    public static final int IMMESSAGE_TYPE_INDEX = 5;
	    public static final int IMMESSAGE_CONTENT_INDEX = 6;
	    public static final int IMMESSAGE_SEND_STATE_INDEX = 7;
	    public static final int IMMESSAGE_RCV_STATE_INDEX = 8;
	    public static final int IMMESSAGE_CREATETIME_INDEX = 9;
	}
}
