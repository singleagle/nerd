package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.List;

public  class RecommendFeed implements IFeed{
	private String id;
	private String title;
	private String imgurl;
	private String contenturl;
	private String subject;
	private int likenum;
	private long farawayMeters;
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public String getImgurl() {
		return imgurl;
	}
	
	@Override
	public String getContenturl() {
		return contenturl;
	}
	
	public int getLikeNum() {
		return likenum;
	}

	public long getFarawayMeters() {
		return farawayMeters;
	}
	
	public FeedSubject getFeedSubject() {
		return null;
	}
	
	static public class PageFeed{
		private int startIndex;
		private int totalCount;
		private ArrayList<RecommendFeed> list;
		
		
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
		
		public List<RecommendFeed> getFeedList() {
			return list;
		}
		
		public void setFeedList(ArrayList<RecommendFeed> feedlist) {
			this.list = feedlist;
		}
	}
	
}
