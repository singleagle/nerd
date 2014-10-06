package com.enjoy.nerd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {

	private static final String APP_NAME = "neard";
	
	
	public static SharedPreferences getSharedPreferences(Context context){
		SharedPreferences preferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
		return preferences;
	}
	
	public static void putString(Context context, String key, String value){
		SharedPreferences preferences = getSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(Context context, String key){
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString(key, "");
	}
	
	public static void putInt(Context context, String key, int value){
		SharedPreferences preferences = getSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static int getInt(Context context,String key){
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getInt(key, 0);
	}
	
	public static void putLong(Context context,String key, long value){
		SharedPreferences preferences = getSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static long getLong(Context context, String key){
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getLong(key, 0L);
	}
	
	public static void remove(Context context,String key){
		SharedPreferences preferences = getSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	
}
