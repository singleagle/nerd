package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.List;


public class DistractionProfile implements IFeed{
	private String _id;
	private String title;
	private long createTime;
	private long startTime;
	private SimpleUserInfo createUserInfo;
	private String description;
	private Location originLoc;
	private Location dstLoc;
	private int farawayMeters;
	private int requestMemberCount;
	private int partnerCount;
	private int likeNum;
	private String imgurl;

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public SimpleUserInfo getCreateUserInfo() {
		return createUserInfo;
	}

	public void setCreateUserInfo(SimpleUserInfo createUserInfo) {
		this.createUserInfo = createUserInfo;
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
		
		return originLoc.getAddress();
	}
	
	public Location getDestLocation(){
		return dstLoc;
	}
	
	public String getDestAddress(){
		if(dstLoc == null){
			return null;
		}
		
		return dstLoc.getAddress();
	}
	
	
	public long getFarawayMeters() {
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
	
	public int getLikeNum(){
		return likeNum;
	}

	public String getImageUrl() {
		return imgurl;
	}
	
	public String getContenturl() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeedSubject getFeedSubject() {
		return FeedSubject.DISTRACTION;
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
