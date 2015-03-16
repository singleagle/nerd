package com.enjoy.nerd.remoterequest.xmpp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;

import com.enjoy.nerd.remoterequest.Account;
import com.enjoy.nerd.remoterequest.Encryptor;


public class OpenfireRegisterReq  extends XMPPRequest<Account>{
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;
	
	String name;
	String headerImgUrl;
	String phoneNO;
	String password;
	int sexType; 
	
	public OpenfireRegisterReq(Context context) {
		super(context);
	}
	
	
	public String getPhoneNO() {
		return phoneNO;
	}


	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}
	

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public String getHeaderImgUrl() {
		return headerImgUrl;
	}

	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
	}


	@Override
	protected Object handleSendAction(Connection connection)
			throws XMPPException {
		connection.connect();
		AccountManager accountManager = connection.getAccountManager();
	    Map<String, String> attributes = new HashMap<String, String>();
	    if(headerImgUrl != null){
	    	attributes.put("headerImgUrl", headerImgUrl);
	    }
	    attributes.put("phone", phoneNO);
	    attributes.put("name", name);
	    accountManager.createAccount(phoneNO, Encryptor.encode(password), attributes);
	    Collection<String> accountAttributes = accountManager.getAccountAttributes();
		return accountManager.getAccountAttribute("name");
	}


	@Override
	protected Account parse(Object response) {
		Collection<String> attributes = (Collection<String>) response;
		Account account = new Account();
		account.setName(name);
		account.setPhoneNO(phoneNO);
		account.setUin(Integer.valueOf(phoneNO));
		return account;
	}


}
