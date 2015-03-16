package com.enjoy.nerd.remoterequest;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;


public class ImageUploadReq  extends FileUploadReq{
	private File image;
	
	public ImageUploadReq(Context context) {
		super(context);
		setContentType("image/png");

	}

	@Deprecated
	public void setBitmap(Bitmap bitmap) {
		
	}


	@Override
	protected String buidRequestUrl() {
		return VENUS_BASE_URL + "/image";
	}

	@Override
	protected String parse(Gson gson, String response)
			throws JSONException {
		JSONObject respObj = new JSONObject(response);
		
		return respObj.getString("url");
	}


	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		
	}
	
}
