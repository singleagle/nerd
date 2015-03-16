package com.enjoy.nerd.remoterequest;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.utils.LogWrapper;

import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;


public class VolleyStringRequestSender implements Listener<String>, ErrorListener {
	private static final String TAG = "TextRequestSender";
	
	 private static RequestQueue sRequestQueue;
	 private Handler mRespondHandler;
	 private HttpRequest<?> mRemoteRequest;
	 private Request<?> mVolleyRequest;
	 private static HashMap<String, String> sDefaultUrlParams;
	 
	 VolleyStringRequestSender(Context context){
		 mRespondHandler = new Handler();
		 
		 
		 if(sDefaultUrlParams == null){
			 sDefaultUrlParams = new HashMap<String, String>();
			 TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			 sDefaultUrlParams.put("deviceId", tManager.getDeviceId()); 
		 }

		 if(sRequestQueue == null){
			 sRequestQueue = Volley.newRequestQueue(context);
		 }
	 }
	 
	 
	 static private String getUrlWithQueryString(String url, HashMap<String, String> urlparams) {
        if (urlparams != null) {
            List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

            for (HashMap.Entry<String, String> entry : urlparams.entrySet()) {
                lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            String paramString = URLEncodedUtils.format(lparams, HTTP.UTF_8);
            
            if (!url.contains("?")) {
                url += "?" + paramString;
            } else {
                url += "&" + paramString;
            }
        }

        return url;
	 }
	    
	 private StringRequestWithParam createVolleyRequest(HttpRequest<?> request){
		 int method = Method.GET;
		 String requestUrl = request.buidRequestUrl();
		 HashMap<String, String> params = new HashMap<String, String>();
		 HashMap<String, String> bodyParams = null;
		 request.fillRequestParams(params);
		 params.putAll(sDefaultUrlParams);
		 
		 if(request.methond().equalsIgnoreCase("get")){
			 requestUrl = getUrlWithQueryString(request.buidRequestUrl(), params);
		 }else if(request.methond().equalsIgnoreCase("post")){
			 method = Method.POST;
			 bodyParams = params;
		 }else if(request.methond().equalsIgnoreCase("put")){
			 method = Method.PUT;
			 bodyParams = params;
		 }else if(request.methond().equalsIgnoreCase("delete")){
			 requestUrl = getUrlWithQueryString(request.buidRequestUrl(), params);
			 method = Method.DELETE;
		 }else{
			 LogWrapper.e(TAG, "the request is invalidate!!!");
		 }
		 
		 StringRequestWithParam volleyReq = new StringRequestWithParam(method, requestUrl, this, this);
		 volleyReq.setBodyParams(bodyParams);
		 volleyReq.setShouldCache(true);
		 return volleyReq;
	 }

	 /**
	  * 
	  * @param request
	  * @param file : stream比较难管理生命期,所以使用File做入参
	  * @param contentType
	  * @return
	  */
	 public int submitRequest(HttpRequest<?> request, File file, String contentType){
		 mRemoteRequest = request;
		 String requestUrl = request.buidRequestUrl();
		 FileVolleyRequest volleyReq = new FileVolleyRequest(requestUrl, this, this);
		 try {
			volleyReq.setFileBody(file, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 mVolleyRequest = sRequestQueue.add(volleyReq);
		 return 0;
	 }
	 
	 
	 public int submitRequest(HttpRequest<?> request){
		 mRemoteRequest = request;
		 StringRequest volleyReq = createVolleyRequest(request);
		 mVolleyRequest = sRequestQueue.add(volleyReq);
		 return 0;
	 }

	 private int translateToErrorCode(VolleyError throwable) {
		int errCode = FailResponseListner.ERR_UNKNOWN;
		return errCode;
	}


	public void onErrorResponse(VolleyError error) {
		final int code = translateToErrorCode(error);
		final String errorDescription;
		if(error != null){
			errorDescription = error.toString();
		}else{
			errorDescription = null;
		}
		mRespondHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mRemoteRequest.dispatchFailureResult(mVolleyRequest.getUrl(), false, code, 0, errorDescription);
			}
		});
		
	}

	public void onResponse(final String response) {
		mRespondHandler.post(new Runnable() {
			
			@Override
			public void run() {
				if(response == null){
					mRemoteRequest.dispatchFailureResult(mVolleyRequest.getUrl(), false, 
							FailResponseListner.ERR_INVALIDATE_REQUEST, 0, null);
				}else{
					mRemoteRequest.dispatchSuccessResult(mVolleyRequest.getUrl(), false, response);
				}
			}
		});
		
	}
	
	static private class StringRequestWithParam extends StringRequest{
		HashMap<String, String> mBodyParams;
		
		public StringRequestWithParam(int method, String url,
				Listener<String> listener, ErrorListener errorListener) {
			super(method, url, listener, errorListener);
			mBodyParams = null;
		}
		
		public void setBodyParams( HashMap<String, String> bodyParams){
			mBodyParams = bodyParams;
		}

		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			return mBodyParams;
		}

		@Override
		public String getCacheKey() {
			return getUrlWithQueryString(getUrl(), mBodyParams);
		}
		
	}
	

}
