package com.enjoy.nerd.remoterequest.xmpp;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.enjoy.nerd.http.RequestParams;
import com.enjoy.nerd.remoterequest.Account;
import com.enjoy.nerd.remoterequest.Encryptor;
import com.google.gson.Gson;


public class OpenfireLoginReq  extends XMPPRequest<Account>{
	String uid; 
	String password;
	
	public OpenfireLoginReq(Context context) {
		super(context);
	}
	

	public String getUid() {
		return uid;
	}

	/**
	 * 
	 * @param uid : 可以是phoneNO，也可以是uin
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	protected Object handleSendAction(Connection connection) throws XMPPException {
		if(!connection.isConnected()){
			connection.connect();
		}
		connection.login(uid, Encryptor.encode(password));
		VCard vcard = new VCard();
		vcard.load(connection);
		Account account = new Account();
		account.setName(connection.getUser());
		account.setPhoneNO(vcard.getPhoneHome("CELL"));
		try{
			account.setUin(Long.valueOf(uid));
		}catch(NumberFormatException  e){
			e.printStackTrace();
		}
		
		byte[] headerImg = vcard.getAvatar();
		return account;
	}


	@Override
	protected Account parse(Object response) {
		return (Account) response;
	}


	
}
