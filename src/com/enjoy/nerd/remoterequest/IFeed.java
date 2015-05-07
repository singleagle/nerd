package com.enjoy.nerd.remoterequest;

import java.text.SimpleDateFormat;
import java.util.Locale;

public  interface IFeed{
	static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
	
	String getId();
	String getTitle();
	String getImageUrl() ;
	String getContenturl();
	FeedSubject getFeedSubject();
	int getLikeNum();
	long getFarawayMeters();
}
