package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class DATagReq extends GetRequest<ArrayList<FeedTag>> {

	public DATagReq(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/tags/distraction";
	}

	@Override
	protected ArrayList<FeedTag> parse(Gson gson, String response)
			throws JSONException {
		JSONObject respObj = new JSONObject(response);
		int total = respObj.getInt("total");
		JSONArray list = respObj.getJSONArray("list");
		if(list == null){
			return null;
		}
		
		ArrayList<FeedTag>  typeList = new ArrayList<FeedTag>(list.length());
		for(int i = 0; i < list.length(); i ++ ){
			JSONObject typeObj = list.getJSONObject(i);
			FeedTag type = gson.fromJson(typeObj.toString(), FeedTag.class);
			typeList.add(type); 
		}
		
		return typeList;
		
	}
	
	
	
}
