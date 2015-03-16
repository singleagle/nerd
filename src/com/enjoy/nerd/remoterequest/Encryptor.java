package com.enjoy.nerd.remoterequest;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import android.util.Base64;


public  class Encryptor{
	
	public static String encode(String rawString){
		return rawString;
		/*
		Charset charset = Charset.forName("UTF-8");
		byte[]  input = charset.encode(rawString).array();
		return  Base64.encodeToString(input, Base64.DEFAULT);*/
	}

}
