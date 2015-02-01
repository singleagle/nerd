package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.remoterequest.DATagReq.DATag;
import com.enjoy.nerd.http.RequestParams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class DATagReq extends GetRequest<ArrayList<DATag>> {

	public DATagReq(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onFillRequestParams(RequestParams params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/datags";
	}

	@Override
	protected ArrayList<DATag> parse(Gson gson, String response)
			throws JSONException {
		JSONObject respObj = new JSONObject(response);
		int total = respObj.getInt("total");
		JSONArray list = respObj.getJSONArray("list");
		if(list == null){
			return null;
		}
		
		ArrayList<DATag>  typeList = new ArrayList<DATag>(list.length());
		for(int i = 0; i < list.length(); i ++ ){
			JSONObject typeObj = list.getJSONObject(i);
			DATag type = gson.fromJson(typeObj.toString(), DATag.class);
			typeList.add(type); 
		}
		
		return typeList;
		
	}
	
	
	public static class DATag implements Parcelable{
		private String id;
		private String parentId;
		private String name;
		
		
		
		public DATag(String typeId, String parentId, String name) {
			this.id = typeId;
			this.parentId = parentId;
			this.name= name;
		}



		public String getId() {
			return id;
		}



		public String getParentId() {
			return parentId;
		}



		public String getName() {
			return name;
		}



		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(id);
			dest.writeString(parentId);
			dest.writeString(name);
		}
		
		private DATag(Parcel in){
			id = in.readString();
			parentId = in.readString();
			name = in.readString();
		}
		
	     public static final Parcelable.Creator<DATag> CREATOR= new Parcelable.Creator<DATag>() {
		     public DATag createFromParcel(Parcel in) {
		         return new DATag(in);
		     }
		
		     public DATag[] newArray(int size) {
		         return new DATag[size];
		     }
		};
 
	}

}
