package com.enjoy.nerd.remoterequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DistractionProfile implements IFeed{
	
	private String _id;
	private String title;
	private String create_time;
	private String start_time;
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
		try {
			return DATAFORMAT.parse(create_time).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	public void setCreateTime(long createTime) {
		this.create_time = DATAFORMAT.format(new Date(createTime));
	}

	public long getStartTime() {
		try {
			return DATAFORMAT.parse(start_time).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	public void setStartTime(long startTime) {
		this.start_time = DATAFORMAT.format(new Date(startTime));
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
		private int from;
		private int total;
		private ArrayList<DistractionProfile> list;
		
		
		public int getStartIndex() {
			return from;
		}
		
		public void setStartIndex(int startIndex) {
			this.from = startIndex;
		}
		
		public int getTotalCount() {
			return total;
		}
		
		public void setTotalCount(int totalCount) {
			this.total = totalCount;
		}
		
		public List<DistractionProfile> getDAProfileList() {
			return list;
		}
		
		public void setDAProfileList(ArrayList<DistractionProfile> DAProfileList) {
			this.list = DAProfileList;
		}
	}

	
}
