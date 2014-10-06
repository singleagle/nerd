package com.enjoy.nerd;

import android.app.Application;

import com.enjoy.nerd.remoterequest.xmpp.XMPPClient;

public class NerdApp extends Application{
	private XMPPClient mXMPPClient;

	
	@Override
	public void onCreate() {
		super.onCreate();
		mXMPPClient = new XMPPClient(this);
	}
	

	public XMPPClient getXMPPClient() {
		return mXMPPClient;
	}
	
	@Override
	public void onTerminate() {
		
		mXMPPClient.release();
		super.onTerminate();
	}

	
}
