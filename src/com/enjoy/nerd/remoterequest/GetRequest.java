package com.enjoy.nerd.remoterequest;

import android.content.Context;


public abstract class GetRequest<T> extends RemoteRequest<T>{

	public GetRequest(Context context) {
		super(context);
	}
	
	final protected String methond(){
		return "get";
	}

}
