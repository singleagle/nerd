package com.enjoy.nerd.remoterequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.google.gson.Gson;

import android.content.Context;





import com.enjoy.nerd.remoterequest.HotFocusListReq.FocusGroup;

public class HotFocusListReq extends GetRequest<FocusGroup> {
	public HotFocusListReq(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/hotspot";
	}

	@Override
	protected FocusGroup parse(Gson gson, String response)
			throws JSONException {
		FocusGroup group  = gson.fromJson(response, FocusGroup.class);
		return group;
		
	}
	
	static final public class FocusGroup{
		private FeedTopic[] topics;
		private FeedTag[] tags;
		
		
		public List<FeedTopic> getTopics() {
			return Arrays.asList(topics);
		}
		
		public List<FeedTag> getTags() {
			return Arrays.asList(tags);
		}
	}

}
