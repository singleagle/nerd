package com.enjoy.nerd.remoterequest;


public class SimpleUserInfo  {
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;

	long uin;
	String name;
	String avatar_id;
	int sex_type; 
	
	public SimpleUserInfo() {
		
	}

	public void setUin(long uin) {
		this.uin = uin;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getUin() {
		return uin;
	}

	public String getName() {
		return name;
	}

	public String getHeaderImgUrl() {
		return avatar_id;
	}

	public void setHeaderImgUrl(String headerImgUrl) {
		this.avatar_id = headerImgUrl;
	}
	
}
