package com.enjoy.nerd.remoterequest;



public  class Comment{
	private SimpleUserInfo creater;
	private long createTime;
	private String content;
	
	
	public SimpleUserInfo getCreater() {
		return creater;
	}
	
	
	public void setCreater(SimpleUserInfo creater) {
		this.creater = creater;
	}
	
	
	public long getCreateTime() {
		return createTime;
	}
	
	
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	public String getContent() {
		return content;
	}
	
	
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
