package com.enjoy.nerd.utils;

import android.util.Log;

public class LogWrapper {
	static final private boolean DEBUG = true;
	
	static  public int d(String tag, String msg){
		if(DEBUG){
			return Log.d(tag, msg);
		}else{
			return 0;
		}
		
	}
	
	static public int i(String tag, String msg){
		return Log.i(tag, msg);
	}
	
	static  public int e(String tag, String msg){
		return Log.e(tag, msg);
	}
}
