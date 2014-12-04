package com.enjoy.nerd.remoterequest.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.http.RequestParams;
import com.enjoy.nerd.remoterequest.Account;
import com.google.gson.Gson;

public class IMMessageSendReq  extends XMPPRequest<String>{
	private String textContent; 
	private String recipientUid;
	private String chatTag;
	private int    msgType;
	private long  createTime;
	
	public IMMessageSendReq(Context context) {
		super(context);
		msgType = IMMessage.TYPE_TEXT;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	

	public String getRecipientUid() {
		return recipientUid;
	}

	public void setRecipientUid(String recipientUid) {
		this.recipientUid = recipientUid;
	}

	public String getChatTag() {
		return chatTag;
	}

	/**
	 * 
	 * @param chatTag : 用于标记此次聊天的id
	 */
	public void setChatTag(String chatTag) {
		this.chatTag = chatTag;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Object handleSendAction(Connection connection) throws XMPPException {
		ChatManager manager = connection.getChatManager();
		String jid = recipientUid + "@" + connection.getServiceName() + "/Smack";
		Chat chat = manager.getThreadChat(chatTag);
		if(chat == null){
			chat = manager.createChat(jid, null);
		}
		 
		Message message = new Message();
		message.setProperty(IMMessage.PROP_TIME, createTime);
		message.setBody(textContent);
		chat.sendMessage(message);

		return chat.getThreadID();
	}


	@Override
	protected String parse(Object response) {
		return (String)response;
	}


	
}
