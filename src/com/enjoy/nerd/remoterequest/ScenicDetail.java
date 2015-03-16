package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;


public  class ScenicDetail{
	private Location location;
	private ArrayList<DistractionProfile> profileList;
	private ArrayList<Comment> commentList;
	private ArrayList<SimpleUserInfo> likeUserList;
	private ArrayList<String> tagNameList;
	
	public ArrayList<DistractionProfile> getDAProfileList() {
		return profileList;
	}
	
	
	public ArrayList<Comment> getCommentList() {
		return commentList;
	}
	
	
	public ArrayList<SimpleUserInfo> getLikeUserList() {
		return likeUserList;
	}

}
