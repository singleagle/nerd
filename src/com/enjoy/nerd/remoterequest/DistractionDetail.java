package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public  class DistractionDetail implements IFeed{
	private DistractionProfile profile;
	private long tribeId;
	private String[] img_url_list;
	private FeedTag[] tag_list;;
	private Comment[] comment_list;
	private SimpleUserInfo[] partner_list;
	
	public DistractionDetail(){
		
	}
	
	public void setProfile(DistractionProfile profile){
		this.profile = profile;
	}
	
	public String getId() {
		return profile.getId();
	}

	public String getTitle() {
		return profile.getTitle();
	}

	public String getImageUrl() {
		if(img_url_list != null){
			return img_url_list[0];
		}
		return null;
	}
	
	/*package*/void setImgurlList(String[] list){
		img_url_list = list;
	}

	public String getContenturl() {
		return null;
	}

	public FeedSubject getFeedSubject() {
		return FeedSubject.DISTRACTION;
	}

	public int getLikeNum() {
		return profile.getLikeNum();
	}

	public long getFarawayMeters() {
		return profile.getFarawayMeters();
	}
	
	public String getDescription(){
		return profile.getDescription();
	}
	
	public DistractionProfile getProfile() {
		return profile;
	}
	
	public long getTribeId() {
		return tribeId;
	}
	
	
	public List<Comment> getCommentList() {
		return Arrays.asList(comment_list);
	}
	
	
	public List<SimpleUserInfo> getPartnerList() {
		return Arrays.asList(partner_list);
	}

}
