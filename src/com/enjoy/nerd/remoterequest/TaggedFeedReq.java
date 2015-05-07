package com.enjoy.nerd.remoterequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.remoterequest.FeedProfile.PageFeed;
import com.google.gson.Gson;

import android.content.Context;


public class TaggedFeedReq extends GetRequest<PageFeed>{
	private int maxNumPerPage = 5;
	private int offset;
	private String tag;
	
	public TaggedFeedReq(Context context) {
		super(context);
	}

	public void setTag(String tag){
		this.tag = tag;
	}

	public void setPager(int offset, int maxNumPerPage) {
		this.offset = offset;
		this.maxNumPerPage = maxNumPerPage;
	}

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		params.put("fromIndex", String.valueOf(offset));
		params.put("maxItemPerPage", String.valueOf(maxNumPerPage));
	}

	@Override
	protected String buidRequestUrl() {
		String encodeTag = null;
		try {
			encodeTag = URLEncoder.encode(tag, "utf-8");
		} catch (UnsupportedEncodingException e) {
			encodeTag = tag;
		}
		return  VENUS_BASE_URL + "/scenic/timeline/tag/" + encodeTag;
	}

	@Override
	protected PageFeed parse(Gson gson, String response) throws JSONException {
		return gson.fromJson(response, PageFeed.class);
	}
	
}
