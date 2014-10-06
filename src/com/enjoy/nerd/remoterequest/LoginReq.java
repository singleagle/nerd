package com.enjoy.nerd.remoterequest;

import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.enjoy.nerd.http.RequestParams;
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
	protected void onFillRequestParams(RequestParams params) {
		params.put("uid", uid);
		Charset charset = Charset.forName("UTF-8");
		byte[]  input = charset.encode(password).array();
		params.put("password", Base64.encodeToString(input, Base64.DEFAULT));
	}

	@Override
	protected String buidRequestUrl() {
		return VENUS_BASE_URL + "/login";
	}

	@Override
	protected Account parse(Gson gson, JSONObject response)
			throws JSONException {
		return gson.fromJson(response.toString(), Account.class);
	}
	
}
