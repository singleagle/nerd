package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.remoterequest.DATypeReq.DAType;
import com.enjoy.nerd.http.RequestParams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class DATypeReq extends GetRequest<ArrayList<DAType>> {

	public DATypeReq(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onFillRequestParams(RequestParams params) {
		return ;
	}

	@Override
	protected String buidRequestUrl() {
		return  VENUS_BASE_URL + "/sec/datypes";
	}

	@Override
	protected ArrayList<DAType> parse(Gson gson, String response)
			throws JSONException {
		JSONObject respObj = new JSONObject(response);
		int mainTypeCount = respObj.getInt("mainTypeCount");
		JSONArray list = respObj.getJSONArray("list");
		if(list == null){
			return null;
		}
		
		ArrayList<DAType>  typeList = new ArrayList<DAType>(list.length());
		for(int i = 0; i < list.length(); i ++ ){
			JSONObject typeObj = list.getJSONObject(i);
			DAType type = gson.fromJson(typeObj.toString(), DAType.class);
			typeList.add(type); 
		}
		
		return typeList;
		
	}
	
	
	public static class DAType implements Parcelable{
		private int typeId;
		private String mainTypeName;
		private String subTypeName;
		
		
		
		public DAType(int typeId, String mainTypeName, String subTypeName) {
			this.typeId = typeId;
			this.mainTypeName = mainTypeName;
			this.subTypeName = subTypeName;
		}

		public int getTypeId() {
			return typeId;
		}
		
		public String getMainTypeName() {
			return mainTypeName;
		}
		
		public String getSubTypeName() {
			return subTypeName;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(typeId);
			dest.writeString(mainTypeName);
			dest.writeString(subTypeName);
		}
		
		private DAType(Parcel in){
			typeId = in.readInt();
			mainTypeName = in.readString();
			subTypeName = in.readString();
		}
		
	     public static final Parcelable.Creator<DAType> CREATOR= new Parcelable.Creator<DAType>() {
		     public DAType createFromParcel(Parcel in) {
		         return new DAType(in);
		     }
		
		     public DAType[] newArray(int size) {
		         return new DAType[size];
		     }
		};
 
	}

}
