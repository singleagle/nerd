package com.enjoy.nerd.remoterequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.http.RequestParams;
import com.google.gson.Gson;

import android.content.Context;


public  class AddDATypeReq extends PostRequest<String>{

	private int mainTypeId;
	private String subTypeName;
	private long createUIN;
	

	public AddDATypeReq(Context context) {
		super(context);
	}


	public int getMainTypeId() {
		return mainTypeId;
	}


	public void setMainTypeId(int mainTypeId) {
		this.mainTypeId = mainTypeId;
	}


	public String getSubTypeName() {
		return subTypeName;
	}
	
	public void setSubTypeName(String typeName){
		subTypeName = typeName;
	}
	
	
	public long getCreateUIN() {
		return createUIN;
	}


	public void setCreateUIN(long createUIN) {
		this.createUIN = createUIN;
	}


	@Override
	protected void onFillRequestParams(RequestParams params) {
		params.put("createuin", Long.toString(createUIN));
		params.put("subtypename", subTypeName);
		params.put("maintypeid", Integer.toString(mainTypeId));
	}

	@Override
	protected String buidRequestUrl() {

		return VENUS_BASE_URL + "/sec/datypes";
	}

	@Override
	protected String parse(Gson gson, String response)throws JSONException {
		return new JSONObject(response).getString("typeId");
	}


}
