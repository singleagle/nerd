package com.enjoy.nerd.remoterequest;

import java.util.HashMap;
import org.json.JSONException;
import com.google.gson.Gson;
import android.content.Context;

public class ScenicDetailReq extends GetRequest<ScenicDetail> {
	private String mScenicId;
	
	public ScenicDetailReq(Context context, String scenicId) {
		super(context);
		mScenicId = scenicId;
	}
	
	

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/scenics/" + mScenicId;
	}

	@Override
	protected ScenicDetail parse(Gson gson, String response)
			throws JSONException {
		ScenicDetail scenic = gson.fromJson(response, ScenicDetail.class);
		return scenic;
		
	}
	
	
	
}
