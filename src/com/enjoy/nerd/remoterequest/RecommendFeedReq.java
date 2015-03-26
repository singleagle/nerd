package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.remoterequest.RecommendFeed.PageFeed;
import com.google.gson.Gson;

import android.content.Context;


public class RecommendFeedReq extends GetRequest<PageFeed>{
	private int maxNumPerPage = 5;
	private int offset;
	private int nearbyMiles = 5000;//5km
	private double longitude,  latitude;
	
	public RecommendFeedReq(Context context) {
		super(context);
	}

	public void setLocation(double longitude, double latitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public void setPager(int offset, int maxNumPerPage) {
		this.offset = offset;
		this.maxNumPerPage = maxNumPerPage;
	}

	public int getNearbyMiles() {
		return nearbyMiles;
	}

	public void setNearbyMiles(int nearbyMiles) {
		this.nearbyMiles = nearbyMiles;
	}

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		params.put("location", String.format("%f, %f", longitude, latitude)); 
		params.put("fromIndex", String.valueOf(offset));
		params.put("maxItemPerPage", String.valueOf(maxNumPerPage));
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/recommend";
	}

	@Override
	protected PageFeed parse(Gson gson, String response) throws JSONException {
		return gson.fromJson(response, PageFeed.class);
	}
	
}
