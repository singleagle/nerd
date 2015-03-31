package com.enjoy.nerd;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.enjoy.nerd.remoterequest.xmpp.XMPPClient;
import com.enjoy.nerd.utils.BitmapCache;

public class NerdApp extends Application{
	private XMPPClient mXMPPClient;
	ImageLoader mImageLoader;

	
	@Override
	public void onCreate() {
		super.onCreate();
		mXMPPClient = new XMPPClient(this);

	}
	

	public XMPPClient getXMPPClient() {
		return mXMPPClient;
	}
	
	
	public ImageLoader getImageLoader(){
		if(mImageLoader == null){
			BitmapCache cache = new BitmapCache();
		    RequestQueue queue = Volley.newRequestQueue(this);    
		    mImageLoader = new ImageLoader(queue, cache);
		}
		return mImageLoader;

	}
	
	
	@Override
	public void onTerminate() {
		
		mXMPPClient.release();
		super.onTerminate();
	}

	
}
