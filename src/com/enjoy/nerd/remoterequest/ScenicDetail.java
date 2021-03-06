package com.enjoy.nerd.remoterequest;

import java.util.Arrays;
import java.util.List;

public  class ScenicDetail implements IFeed{
	private String _id;
	private String title;
	private String description;
	private String imgurl;
	private String contenturl;
	private int likenum;
	private long farawayMeters;
	private Location location;
	private String[] others_imgurl;
	private DistractionProfile[] profile_list;
	private Comment[] comment_list;
	private String[] tag_list;
	
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
	
	public List<String> getOthersImgurl(){
		return Arrays.asList(others_imgurl);
	}
	
	public List<String> getTagNameList(){
		return Arrays.asList(tag_list);
	}
	
	@Override
	public String getContenturl() {
		return contenturl;
	}
	
	public String getDescription(){
		return description;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public int getLikeNum() {
		return likenum;
	}

	public long getFarawayMeters() {
		return farawayMeters;
	}
	
	public FeedSubject getFeedSubject() {
		return FeedSubject.SCENIC;
	}
	
	public List<DistractionProfile> getDAProfileList() {
		return Arrays.asList(profile_list);
	}
	
	
	public List<Comment> getCommentList() {
		return Arrays.asList(comment_list);
	}
	

}
