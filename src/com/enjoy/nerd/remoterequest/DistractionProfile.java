package com.enjoy.nerd.remoterequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DistractionProfile {
	private String id;
	private String title;
	private long createTime;
	private long startTime;
	private long createUserId;
	private String description;
	private Location originLoc;
	private Location dstLoc;
	private int farawayMeters;
	private int requestMemberCount;
	private int partnerCount;
	private int goodCount;
	private ArrayList<String> tagNameList;
	
	private String imageId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getScheme(){
		if(tagNameList != null && tagNameList.size() != 0){
			return tagNameList.get(0);
		}
		return null;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginAddress(){
		if(originLoc == null){
			return null;
		}
		
		return originLoc.address;
	}
	
	public String getDestAddress(){
		if(dstLoc == null){
			return null;
		}
		
		return dstLoc.address;
	}
	
	
	public int getFarawayMeters() {
		return farawayMeters;
	}

	public void setFarawayMeters(int farawayMeters) {
		this.farawayMeters = farawayMeters;
	}

	public int getRequestMemberCount() {
		return requestMemberCount;
	}

	public void setRequestMemberCount(int requestMemberCount) {
		this.requestMemberCount = requestMemberCount;
	}

	public int getPartnerCount() {
		return partnerCount;
	}

	public void setPartnerCount(int partnerCount) {
		this.partnerCount = partnerCount;
	}
	
	public int getGoodCount(){
		return goodCount;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	static class Location{
		String address;
		int confidence;
		double[] location;
		
		
		/*
		static public class LocationSerializer implements JsonDeserializer<Location>, JsonSerializer<Location>{

			@Override
			public Location deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jobj = json.getAsJsonObject();
				Location location = new Location();
				
				location.address = jobj.getAsString();
				location.confidence = jobj.get("confidence").getAsInt();
				location.location[0] = jobj.get("location").getAsJsonArray().get(0).getAsDouble();
				location.location[1] = jobj.get("location").getAsJsonArray().get(0).getAsDouble();
				return location;
			}
			
			@Override
			public JsonElement serialize(Location src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonObject jobj = new JsonObject();
				jobj.addProperty("address", src.address);
				jobj.addProperty("confidence", src.confidence);
				JsonArray jArray = new JsonArray();
				jArray.add(new JsonPrimitive(src.location[0]));
				jArray.add(new JsonPrimitive(src.location[0]));
				jobj.add("location", jArray);
				return jobj;
			}	
			
		}*/
	}
	
	static public class PageDAProfile{
		private int startIndex;
		private int totalCount;
		private ArrayList<DistractionProfile> list;
		
		
		public int getStartIndex() {
			return startIndex;
		}
		
		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}
		
		public int getTotalCount() {
			return totalCount;
		}
		
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
		
		public List<DistractionProfile> getDAProfileList() {
			return list;
		}
		
		public void setDAProfileList(ArrayList<DistractionProfile> DAProfileList) {
			this.list = DAProfileList;
		}
	}
	
}
