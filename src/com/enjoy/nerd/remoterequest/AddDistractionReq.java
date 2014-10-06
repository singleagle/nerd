package com.enjoy.nerd.remoterequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.http.RequestParams;
import com.google.gson.Gson;

import android.content.Context;


public  class AddDistractionReq extends PostRequest<String>{
	
	static public final int PAYTYPE_ME = 0;
	static public final int PAYTYPE_AA = 1;
	static public final int PAYTYPE_YOU = 2;
	
	private String title;
	private int type;
	private long creatUserId;
	private String description;
	private String address;
	private int payType;
	private long startTime;

	
	public AddDistractionReq(Context context) {
		super(context);
	}

	public String getTitle() {
		return title;
	}



	public AddDistractionReq setTitle(String title) {
		this.title = title;
		return this;
	}

	


	public AddDistractionReq setType(int type) {
		this.type = type;
		return this;
	}

	public AddDistractionReq setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getCreatUserId() {
		return creatUserId;
	}



	public AddDistractionReq setCreatUserId(long creatUserId) {
		this.creatUserId = creatUserId;
		return this;
	}



	public String getDescription() {
		return description;
	}



	public AddDistractionReq setDescription(String description) {
		this.description = description;
		return this;
	}



	public String getAddress() {
		return address;
	}

	public AddDistractionReq setAddress(String address) {
		this.address = address;
		return this;
	}

	/**
	 * @param payType: PAYTYPE_XXX
	 * @return
	 */
	public AddDistractionReq setPayType(int payType) {
		this.payType = payType;
		return this;
	}



	@Override
	protected void onFillRequestParams(RequestParams params) {
		params.put("title", title);
		params.put("createuser", Long.toString(creatUserId));
		params.put("description", description);
		params.put("address", address);
		params.put("paytype", Integer.toString(payType));
		params.put("starttime", Long.toString(startTime));
	}

	@Override
	protected String buidRequestUrl() {

		return VENUS_BASE_URL + "/sec/distractions";
	}

	@Override
	protected String parse(Gson gson, JSONObject response)throws JSONException {
		return response.getString("_id");
	}


}
