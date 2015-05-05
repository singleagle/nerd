package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.List;


public class DistractionProfile implements IFeed{
	private String _id;
	private String title;
	private long create_time;
	private long start_time;
	private SimpleUserInfo created_by;
	private String description;
	private Location origin_loc;
	private Location dst_loc;
	private int faraway_meters;
	private int requestMemberCount;
	private int partner_count;
	private int like_num;
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
		return create_time;
	}

	public void setCreateTime(long createTime) {
		this.create_time = createTime;
	}

	public long getStartTime() {
		return start_time;
	}

	public void setStartTime(long startTime) {
		this.start_time = startTime;
	}

	public SimpleUserInfo getCreateUserInfo() {
		return created_by;
	}

	public void setCreateUserInfo(SimpleUserInfo createUserInfo) {
		this.created_by = createUserInfo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginAddress(){
		if(origin_loc == null){
			return null;
		}
		
		return origin_loc.getAddress();
	}
	
	public Location getDestLocation(){
		return dst_loc;
	}
	
	public String getDestAddress(){
		if(dst_loc == null){
			return null;
		}
		
		return dst_loc.getAddress();
	}
	
	
	public long getFarawayMeters() {
		return faraway_meters;
	}

	public void setFarawayMeters(int farawayMeters) {
		this.faraway_meters = farawayMeters;
	}

	public int getRequestMemberCount() {
		return requestMemberCount;
	}

	public void setRequestMemberCount(int requestMemberCount) {
		this.requestMemberCount = requestMemberCount;
	}

	public int getPartnerCount() {
		return partner_count;
	}

	public void setPartnerCount(int partnerCount) {
		this.partner_count = partnerCount;
	}
	
	public int getLikeNum(){
		return like_num;
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
