package com.enjoy.nerd.remoterequest;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.enjoy.nerd.remoterequest.BatchPostReqest.PostRequestPipe;
import com.google.gson.Gson;

//用来处理FileUploadReq处理完后将服务器返回的url设入下一个PostRequest
public class FileUploadReqPipe  implements PostRequestPipe{

	@Override
	public void fillPipe(Object content, PostRequest<?> postReq) {
		if(content instanceof String && postReq instanceof FileUploadReq ){
			
		}
		
	}

	
	
}
