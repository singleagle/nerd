package com.enjoy.nerd.remoterequest;

public  interface IFeed{
	
	String getId();
	String getTitle();
	String getImageUrl() ;
	String getContenturl();
	FeedSubject getFeedSubject();
	int getLikeNum();
	long getFarawayMeters();
}
