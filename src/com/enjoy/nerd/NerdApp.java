package com.enjoy.nerd;

import android.app.Application;

import com.enjoy.nerd.remoterequest.xmpp.XMPPClient;
import com.enjoy.nerd.utils.BitmapCache;

public class NerdApp extends Application{
	private XMPPClient mXMPPClient;
	private BitmapCache mBitmapCache;

	
	@Override
	public void onCreate() {
		super.onCreate();
		mXMPPClient = new XMPPClient(this);
		mBitmapCache = new BitmapCache();
	}
	

	public XMPPClient getXMPPClient() {
		return mXMPPClient;
	}
	
	
	public BitmapCache getBitmapCache(){
		return mBitmapCache;
	}
	
	
	@Override
	public void onTerminate() {
		
		mXMPPClient.release();
		super.onTerminate();
	}

	
}
