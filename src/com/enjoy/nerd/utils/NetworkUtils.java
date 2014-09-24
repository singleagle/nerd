package com.enjoy.nerd.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**         
 */
public class NetworkUtils {

	/**
	 * 
	 * 判断网络是否有效
	 * 
	 * @param context
	 * 		    上下文对象
	 * 
	 * @return boolean
	 *         -- TRUE  有效
	 *         -- FALSE 无效
	 *         
	 */
	public static boolean isNetworkAvailable(Context context){
		if(context != null){
			 ConnectivityManager manager = (ConnectivityManager) context  
	         .getApplicationContext().getSystemService(  
	                Context.CONNECTIVITY_SERVICE);  
			  if (manager == null) {  
			      return false;  
			  }  
			  NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
			  if (networkinfo == null || !networkinfo.isAvailable()) {  
			      return false;  
			  }  
			  
			  if(networkinfo.getState() ==  NetworkInfo.State.CONNECTED){
				  return true; 
			  }else{
				  return false;
			  }
		}
		return false;
	}
	
	
	/**
	 * wifi网络条件下
	 * @param context
	 * @return
	 */
	public static boolean isWifiNetwork(Context context){
		 final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);   
		 NetworkInfo[] info = connMgr.getAllNetworkInfo(); 
	     if (info != null) { 
		     for (int i = 0; i < info.length; i++) { 
		    	 	 if( info[i].isConnected()==false)continue;
			    	 if (info[i].getTypeName().equals("WIFI") || info[i].getTypeName().equals("USBNET")) { 
			    		return true;
				     }
		     	}
	     }
	     return false;
	}
	/**
	 * 非wifi网络条件下
	 * @param context
	 * @return
	 */
	public static boolean isNotWifiNetwork(Activity context){
		 final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);   
		 NetworkInfo[] info = connMgr.getAllNetworkInfo(); 
	     if (info != null) { 
		     for (int i = 0; i < info.length; i++) { 
		    	 	 if( info[i].isConnected()==false)continue;
			    	 if (info[i].getTypeName().equals("WIFI") || info[i].getTypeName().equals("USBNET")) { 
			    		return false;
				     }
		     	}
	     }else{
	    	 return false;
	     }
	     return true;
	}
	
	
	public static boolean isNetworkTypeWIFI(int networkType) {
		switch (networkType) {
		case ConnectivityManager.TYPE_WIFI:
		case ConnectivityManager.TYPE_WIMAX:
			return true;
		default:
			return false;
		}
	}

	public static boolean isNetworkTypeMobile(int networkType) {
		switch (networkType) {
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_MOBILE_MMS:
		case ConnectivityManager.TYPE_MOBILE_SUPL:
		case ConnectivityManager.TYPE_MOBILE_DUN:
		case ConnectivityManager.TYPE_MOBILE_HIPRI:
			return true;
		default:
			return false;
		}
	}
}
