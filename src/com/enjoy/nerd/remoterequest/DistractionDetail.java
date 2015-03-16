package com.enjoy.nerd.remoterequest;

import java.util.ArrayList;


public  class DistractionDetail{
	private DistractionProfile profile;
	private long chatRoomId;
	private ArrayList<Comment> commentList;
	private ArrayList<SimpleUserInfo> likeUserList;
	
	
	public DistractionProfile getProfile() {
		return profile;
	}
	
	public long getChatRoomId() {
		return chatRoomId;
	}
	
	
	public ArrayList<Comment> getCommentList() {
		return commentList;
	}
	
	
	public ArrayList<SimpleUserInfo> getLikeUserList() {
		return likeUserList;
	}
	
	

}
