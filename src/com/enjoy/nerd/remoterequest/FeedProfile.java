package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.List;

public  class FeedProfile implements IFeed{
	private String _id;
	private String title;
	private String imgurl;
	private String contenturl;
	private String subject;
	private int like_num;
	private long faraway_meters;
	
	@Override
	public String getId() {
		return _id;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public String getImageUrl() {
		return imgurl;
	}
	
	@Override
	public String getContenturl() {
		return contenturl;
	}
	
	public int getLikeNum() {
		return like_num;
	}

	public long getFarawayMeters() {
		return faraway_meters;
	}
	
	public FeedSubject getFeedSubject() {
		return FeedSubject.translateFrom(subject);
	}
	
	static public class PageFeed{
		private int from;
		private int total;
		private ArrayList<FeedProfile> list;
		
		
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
		
		public List<FeedProfile> getFeedList() {
			return list;
		}
		
		public void setFeedList(ArrayList<FeedProfile> feedlist) {
			this.list = feedlist;
		}
	}
	
}
