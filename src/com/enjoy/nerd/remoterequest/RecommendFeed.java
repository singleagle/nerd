package com.enjoy.nerd.remoterequest;

public  class RecommendFeed implements IFeed{
	private String id;
	private String title;
	private String imgurl;
	private String contenturl;
	
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
	

}
