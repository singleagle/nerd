package com.enjoy.nerd.remoterequest;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;


public  class AddScenicReq extends PostRequest<String>{
	
	static public final int PAYTYPE_ME = 0;
	static public final int PAYTYPE_AA = 1;
	static public final int PAYTYPE_YOU = 2;
	
	private String title;
	private long creatUserId;
	private String description;
	private String address;
	private int payType;
	private long startTime;
	private ArrayList<String> tagIdList = new ArrayList<String>();
	private ArrayList<String> imgUrlList = new ArrayList<String>();

	
	public AddScenicReq(Context context) {
		super(context);
	}

	public String getTitle() {
		return title;
	}



	public AddScenicReq setTitle(String title) {
		this.title = title;
		return this;
	}


	public AddScenicReq setType(String type) {
		tagIdList.add(0, type);
		return this;
	}

	public AddScenicReq setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getCreatUserId() {
		return creatUserId;
	}



	public AddScenicReq setCreatUserId(long creatUserId) {
		this.creatUserId = creatUserId;
		return this;
	}



	public String getDescription() {
		return description;
	}



	public AddScenicReq setDescription(String description) {
		this.description = description;
		return this;
	}



	public String getAddress() {
		return address;
	}

	public AddScenicReq setAddress(String address) {
		this.address = address;
		return this;
	}

	/**
	 * @param payType: PAYTYPE_XXX
	 * @return
	 */
	public AddScenicReq setPayType(int payType) {
		this.payType = payType;
		return this;
	}

	
	public AddScenicReq setImgUrlList(AbstractList<String> urlList){
		imgUrlList.clear();
		imgUrlList.addAll(urlList);
		return this;	
	}


	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		if(title != null){
			params.put("title", title);
		}

		params.put("createuser", Long.toString(creatUserId));
		if(!imgUrlList.isEmpty()){
			params.put("imgurllist", imgUrlList.get(0));
		}
		params.put("tagidlist", tagIdList.get(0));
		params.put("description", description);
		params.put("address", address);
		params.put("paytype", Integer.toString(payType));
		params.put("starttime", Long.toString(startTime));
	}

	@Override
	protected String buidRequestUrl() {

		return VENUS_BASE_URL + "/sec/scenic";
	}

	@Override
	protected String parse(Gson gson, String response)throws JSONException {
		return new JSONObject(response).getString("_id");
	}


}
