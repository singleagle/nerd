package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;


public  class AddScenicReq extends PostRequest<String>{
	private String title;
	private long creatUserId;
	private String description;
	private Location location;
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
		if(location != null){
			return location.getAddress();
		}
		return null;
	}

	public AddScenicReq setLocation(String address, double longitude, double latitude) {
		Location location = new Location();
		location.setAddress(address).setLatLng(longitude, latitude);
		return setLocation(location);
	}
	
	
	public AddScenicReq setLocation(Location location){
		this.location = location;
		return this;
	}

	
	public AddScenicReq setImgUrlList(List<String> urlList){
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
			params.put("imgurllist", strBuilder.toString());
		}
		params.put("tagidlist", tagIdList.get(0));
		params.put("description", description);
		if(location != null){
			params.put("address", location.getAddress());
			double[] cord = location.getLocation();
			params.put("location", String.format("%f,  %f",cord[0],cord[1]));
		}
		
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
