package com.enjoy.nerd.remoterequest;

import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.enjoy.nerd.http.RequestParams;
import com.google.gson.Gson;


public class RegisterReq  extends PostRequest<Account>{
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;
	
	String name;
	String headerImgUrl;
	String phoneNO;
	String password;
	int sexType; 
	
	public RegisterReq(Context context) {
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
	protected void onFillRequestParams(RequestParams params) {
		params.put("phoneno", phoneNO);
		params.put("password", Encryptor.encode(password));
		if(name != null){
			params.put("nickname", name);
		}
		
		if(headerImgUrl != null){
			params.put("headerImgUrl", headerImgUrl);
		}
		
	}

	@Override
	protected String buidRequestUrl() {
		return VENUS_BASE_URL + "/register";
	}

	@Override
	protected Account parse(Gson gson, String response)
			throws JSONException {
		return gson.fromJson(response, Account.class);
	}
	
}
