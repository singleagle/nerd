package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;


public  class AddDistractionReq extends PostRequest<String>{
	
	static public final int PAYTYPE_ME = 0;
	static public final int PAYTYPE_AA = 1;
	static public final int PAYTYPE_YOU = 2;
	
	private String title;
	private long creatUserId;
	private String description;
	Location location;
	private int payType;
	private long startTime;
	private ArrayList<String> tagIdList = new ArrayList<String>();
	private ArrayList<String> imgUrlList = new ArrayList<String>();

	
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


	public AddDistractionReq setType(String type) {
		tagIdList.add(0, type);
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
		if(location != null){
			return location.getAddress();
		}
		return null;
	}
	

	
	public AddDistractionReq setLocation(Location location){
		this.location = location;
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

	
	public AddDistractionReq setImgUrlList(List<String> urlList){
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
			StringBuilder strBuilder = new StringBuilder();
			int size = imgUrlList.size();
			int num = 0;
			for(String url:imgUrlList){
				strBuilder.append(url);
				if (++num < size){
					strBuilder.append(",");
				}
			}
			params.put("img_url_list", strBuilder.toString());
		}
		params.put("tag_list", tagIdList.get(0));
		params.put("description", description);
		if(location != null){
			params.put("address", location.getAddress());
			double[] cord = location.getLocation();
			params.put("location", String.format("%f,%f",cord[0],cord[1]));
		}
		params.put("paytype", Integer.toString(payType));
		params.put("starttime", Long.toString(startTime));
	}

	@Override
	protected String buidRequestUrl() {

		return VENUS_BASE_URL + "/sec/distractions";
	}

	@Override
	protected String parse(Gson gson, String response)throws JSONException {
		return new JSONObject(response).getString("_id");
	}


}
