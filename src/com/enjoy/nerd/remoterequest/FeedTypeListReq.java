package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;


import com.enjoy.nerd.remoterequest.FeedTypeListReq.FeedTagGroup;

public class FeedTypeListReq extends GetRequest<ArrayList<FeedTagGroup>> {
	
	
	public FeedTypeListReq(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/feedgroup";
	}

	@Override
	protected ArrayList<FeedTagGroup> parse(Gson gson, String response)
			throws JSONException {
		JSONObject respObj = new JSONObject(response);
		int total = respObj.getInt("total");
		JSONArray list = respObj.getJSONArray("list");
		if(list == null){
			return null;
		}
		
		ArrayList<FeedTagGroup>  typeList = new ArrayList<FeedTagGroup>(list.length());
		for(int i = 0; i < list.length(); i ++ ){
			JSONObject typeObj = list.getJSONObject(i);
			FeedTagGroup type = gson.fromJson(typeObj.toString(), FeedTagGroup.class);
			typeList.add(type); 
		}
		
		return typeList;
		
	}
	
	static final public class FeedTagGroup{
		private String name;
		private FeedTag[] value;
		private String moreurl;
		
		
		public String getName() {
			return name;
		}
		
		public FeedTag[] getTagList() {
			return value;
		}
		
		public String getMoreurl() {
			return moreurl;
		}
		
		
	}

}
