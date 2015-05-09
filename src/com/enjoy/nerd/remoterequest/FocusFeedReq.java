package com.enjoy.nerd.remoterequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;

import com.enjoy.nerd.remoterequest.FeedProfile.PageFeed;
import com.enjoy.nerd.remoterequest.FocusFeedReq.FocusPageFeed;

public class FocusFeedReq extends GetRequest<FocusPageFeed>{
	private int maxNumPerPage = 5;
	private int offset;
	private String focusName;
	private String focusType;
	private FeedSubject subject;
	
	public FocusFeedReq(Context context) {
		super(context);
		subject = FeedSubject.SCENIC;
	}

	public void setFocusName(String focusName){
		this.focusName = focusName;
	}
	
	public void setFeedSubject(FeedSubject subject){
		this.subject = subject;
	}
	
	public void setFocusType(String focusType){
		this.focusType = focusType;
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
		String encodeFocusName = null;
		try {
			encodeFocusName = URLEncoder.encode(focusName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			encodeFocusName = focusName;
		}
		String focusdir;
		if("topic".equalsIgnoreCase(focusType)){
			focusdir="topics";
		}else{
			focusdir = "tags";
		}
		StringBuilder urlBuilder = new StringBuilder(VENUS_BASE_URL);
		urlBuilder.append("/").append(subject.getDescription()).append("/timeline/")
				.append(focusdir).append("/").append(encodeFocusName);
		return urlBuilder.toString();
	}

	@Override
	protected FocusPageFeed parse(Gson gson, String response) throws JSONException {
		return gson.fromJson(response, FocusPageFeed.class);
	}
	
	static public class FocusPageFeed{
		private int followed;
		private String focus_name;
		private PageFeed page_feed;
		
		public boolean isFollowed() {
			return followed == 1;
		}
		
		public String getFocusName() {
			return focus_name;
		}
		
		public PageFeed getPageFeed() {
			return page_feed;
		}
	}
}
