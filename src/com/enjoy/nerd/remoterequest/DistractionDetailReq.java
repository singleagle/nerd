package com.enjoy.nerd.remoterequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;

public class DistractionDetailReq extends GetRequest<DistractionDetail> {
	private String mDAId;
	
	public DistractionDetailReq(Context context, String daId) {
		super(context);
		mDAId = daId;
	}
	
	

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/distractions/" + mDAId;
	}

	@Override
	protected DistractionDetail parse(Gson gson, String response)
			throws JSONException {
		DistractionProfile profile = gson.fromJson(response, DistractionProfile.class);
		DistractionDetail distraction =  gson.fromJson(response, DistractionDetail.class);
		distraction.setProfile(profile);
		return distraction;
		
	}
	
	
	
}
