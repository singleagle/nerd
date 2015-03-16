package com.enjoy.nerd.remoterequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;


public class FileUploadReq  extends PostRequest<String>{
	private File file;
	private String contentType;
	
	public FileUploadReq(Context context) {
		super(context);

	}

	public void setFile(File file) {
		this.file = file;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	protected String buidRequestUrl() {
		return VENUS_BASE_URL + "/upload";
	}

	@Override
	protected String parse(Gson gson, String response)
			throws JSONException {
		return null;
	}


	@Override
	public void onSubmitRequest() {
		mSender.submitRequest(this, file, contentType);
	}

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		
	}
	
}
