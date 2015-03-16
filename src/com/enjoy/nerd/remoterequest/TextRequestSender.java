package com.enjoy.nerd.remoterequest;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import com.enjoy.nerd.http.AsyncHttpClient;
import com.enjoy.nerd.http.RequestHandle;
import com.enjoy.nerd.http.RequestParams;
import com.enjoy.nerd.http.TextHttpResponseHandler;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.utils.LogWrapper;

import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;


public class TextRequestSender extends  TextHttpResponseHandler {
	private static final String TAG = "TextRequestSender";
	
	 private static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();
	 private Handler mRespondHandler;
	 HttpRequest<?> mRemoteRequest;
	 RequestHandle mRequestHandle;
	 String mRequestUrl;
	 HashMap<String, String> mParams;
	 
	 TextRequestSender(Context context){
		 mRespondHandler = new Handler();
		 mParams = new HashMap<String, String>();
		 TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 params.put("deviceId", tManager.getDeviceId());
	 }
	 
	 public int submitRequest(HttpRequest<?> request){
		 mRemoteRequest = request;
		 if(mRequestHandle != null){
			 mRequestHandle.cancel(true);
		 }
		 mRequestUrl = request.buidRequestUrl();
		 request.fillRequestParams(mParams);
		 RequestParams params = new RequestParams(mParams);
		 
		 if(request.methond().equalsIgnoreCase("get")){
			 mRequestHandle = mAsyncHttpClient.get(mRequestUrl, params,  this);
		 }else if(request.methond().equalsIgnoreCase("post")){
			 mRequestHandle = mAsyncHttpClient.post(mRequestUrl, params,  this);
		 }else if(request.methond().equalsIgnoreCase("put")){
			 mRequestHandle = mAsyncHttpClient.put(mRequestUrl, params,  this);
		 }else if(request.methond().equalsIgnoreCase("delete")){
			 mRequestHandle = mAsyncHttpClient.delete(null, mRequestUrl, null, params,  this);
		 }else{
			 LogWrapper.e(TAG, "the request is invalidate!!!");
		 }
		 
		 return 0;
	 }

	 private int translateToErrorCode(Throwable throwable) {
		int errCode = FailResponseListner.ERR_UNKNOWN;
		if (throwable != null) {
			if (throwable instanceof ConnectTimeoutException) {
				errCode = FailResponseListner.ERR_CONNECT_TIMEOUT;
			} else if (throwable instanceof SocketTimeoutException) {
				errCode = FailResponseListner.ERR_REQUEST_TIMEOUT;
			} else if (throwable instanceof UnknownHostException) {
				errCode = FailResponseListner.ERR_NO_NETWORK;
			} else if (throwable instanceof FileNotFoundException) {
				errCode = FailResponseListner.ERR_INVALIDATE_REQUEST;
			} else if (throwable instanceof java.net.ConnectException) {
				if (throwable.toString().contains("Network is unreachable")) {
					errCode = FailResponseListner.ERR_NO_NETWORK;
				} else {
					errCode = FailResponseListner.ERR_CONNECT_TIMEOUT;
				}
			} else if (throwable instanceof HttpResponseException) {
				errCode = FailResponseListner.ERR_INVALIDATE_REQUEST;
			} else if (throwable instanceof CircularRedirectException) {
				errCode = FailResponseListner.ERR_INVALIDATE_REQUEST;
			} else if (throwable instanceof ClientProtocolException) {
				errCode = FailResponseListner.ERR_INVALIDATE_REQUEST;
			}else if(throwable instanceof NoHttpResponseException){
				errCode = FailResponseListner.ERR_INVALIDATE_REQUEST;
			}
		}
		return errCode;
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		final int code = translateToErrorCode(throwable);
		final String errorDescription;
		if(throwable != null){
			errorDescription = throwable.toString();
		}else{
			errorDescription = null;
		}
		mRequestHandle = null;
		mRespondHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mRemoteRequest.dispatchFailureResult(mRequestUrl, false, code, 0, errorDescription);
			}
		});
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers,
			final String responseString) {
		mRequestHandle = null;
		mRespondHandler.post(new Runnable() {
			
			@Override
			public void run() {
				if(responseString == null){
					mRemoteRequest.dispatchFailureResult(mRequestUrl, false, FailResponseListner.ERR_INVALIDATE_REQUEST, 0, null);
				}else{
					mRemoteRequest.dispatchSuccessResult(mRequestUrl, false, responseString);
				}
			}
		});
	}
}
