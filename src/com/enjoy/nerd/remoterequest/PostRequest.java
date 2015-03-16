package com.enjoy.nerd.remoterequest;

import android.content.Context;


public abstract class PostRequest<T> extends HttpRequest<T>{

	public PostRequest(Context context) {
		super(context);
	}

	final protected String methond(){
		return "post";
	}
}
