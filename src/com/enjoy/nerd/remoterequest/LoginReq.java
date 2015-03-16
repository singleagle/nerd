package com.enjoy.nerd.remoterequest;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;


public class LoginReq  extends PostRequest<Account>{
	String uid; 
	String password;
	
	public LoginReq(Context context) {
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
	protected void onFillRequestParams(HashMap<String, String> params) {
		params.put("uid", uid);
		params.put("password", Encryptor.encode(password));
	}

	@Override
	protected String buidRequestUrl() {
		return VENUS_BASE_URL + "/login";
	}

	@Override
	protected Account parse(Gson gson, String response)
			throws JSONException {
		return gson.fromJson(response, Account.class);
	}
	
}
