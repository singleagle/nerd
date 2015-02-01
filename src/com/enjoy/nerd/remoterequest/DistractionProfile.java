package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.List;

public class DistractionProfile {
	private String id;
	private String title;
	private long creatTime;
	private long startTime;
	private long creatUserId;
	private String description;
	private String origin; //始发地
	private int farawayMeters;
	private int requestMemberCount;
	private int partnerCount;
	
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

	public long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getCreatUserId() {
		return creatUserId;
	}

	public void setCreatUserId(long creatUserId) {
		this.creatUserId = creatUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
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
