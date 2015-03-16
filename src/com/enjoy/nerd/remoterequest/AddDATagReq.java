package com.enjoy.nerd.remoterequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;


public  class AddDATagReq extends PostRequest<String>{

	private String parentId;
	private String tagName;
	private long createUIN;
	//0:"pr(ivate)", 1:"fr(iend)";2:"pu(blic)";
	private int scope = -1; 
	

	public AddDATagReq(Context context) {
		super(context);
	}


	
	public String getParentId() {
		return parentId;
	}



	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public String getTagName() {
		return tagName;
	}



	public void setTagName(String tagName) {
		this.tagName = tagName;
	}



	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}



	public long getCreateUIN() {
		return createUIN;
	}


	public void setCreateUIN(long createUIN) {
		this.createUIN = createUIN;
	}


	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		params.put("createuin", Long.toString(createUIN));
		params.put("tagname", tagName);
		if(parentId != null){
			params.put("parent", parentId);
		}
		
		if(scope > 0){
			params.put("scope", "pr");
		}
	}

	@Override
	protected String buidRequestUrl() {

		return VENUS_BASE_URL + "/sec/datags";
	}

	@Override
	protected String parse(Gson gson, String response)throws JSONException {
		return new JSONObject(response).getString("_id");
	}

}
