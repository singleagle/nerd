package com.enjoy.nerd.remoterequest;


public class Account  {
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;

	long uin;
	String name;
	String headerImgUrl;
	String phoneNO;
	String password;
	String accessToken;
	
	int sexType; 
	
	public Account() {
		
	}

	public String getPhoneNO() {
		return phoneNO;
	}


	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}

	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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
		return headerImgUrl;
	}

	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
	}
	
}
