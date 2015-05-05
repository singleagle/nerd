package com.enjoy.nerd.remoterequest;

import java.util.HashMap;

import javax.xml.transform.ErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.utils.LogWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;


abstract public class HttpRequest<T> extends RemoteRequest<T> {
	static private final String TAG = "RemoteRequest";
	
	//static public final String VENUS_BASE_URL ="http://192.168.1.101/api/v1";
	static public final String VENUS_BASE_URL = "http://www.funnycity.cn/venus/api/v1";
	protected final VolleyStringRequestSender mSender;

	
	public HttpRequest(Context context){
		super(context);
		mSender = new VolleyStringRequestSender(context);
	}
	
	
	final void fillRequestParams(HashMap<String, String> params){
		onFillRequestParams(params);
	}
	
	@Override
	public void onSubmitRequest(boolean ignoreCache){
		mSender.submitRequest(this, ignoreCache);
	}
	
	/**package*/ final boolean dispatchSuccessResult(String reqUrl,  boolean cached, String respondData){
		LogWrapper.i(TAG, "dispatchSuccessResult:" + respondData);
		
		int error = FailResponseListner.ERR_INVALID_FORMAT_MESSAGE;
		int subErr = 0;
		String errorMsg = null;
		try{
			JSONObject jsonData = new JSONObject(respondData);
			subErr = jsonData.getInt("statecode");
			if(subErr == 0){
				GsonBuilder gsonBuilder = new GsonBuilder();  
				//gsonBuilder.excludeFieldsWithModifiers(Modifier.PROTECTED);
				//gsonBuilder.setExclusionStrategies(new FieldExclusionStrategy());
				//gsonBuilder.excludeFieldsWithoutExposeAnnotation();
 
			    Gson gson = gsonBuilder.create();
			    JSONObject body = jsonData.getJSONObject("body");
				T data = parse(gson, body.toString());
				if(data != null){
					error = 0;
					if(mSuccessListner != null){
						LogWrapper.i(TAG, "parse finish,data type:" + data.getClass().getSimpleName());
						mSuccessListner.onSucess(mId, data);
					}
				}
			}else{
				error = FailResponseListner.ERR_ERROR_MESSAGE;
				errorMsg = jsonData.optString("stateDescription");
			}

		} catch (JSONException e) {
			errorMsg = e.toString();
		}
		
		if(error != 0 && mFailResponseListner != null){
			
			dispatchFailureResult(reqUrl, cached, error, subErr, errorMsg);
		}
		
		return error == 0 ;
	}
	
	final void dispatchFailureResult(String reqUrl, final boolean cached, final int error, final int serverErr, final String errorDescription){
		LogWrapper.e(TAG, "dispatchFailureResult,  failed:" + error);
		if(mFailResponseListner != null){
			mFailResponseListner.onFailure(mId, error, serverErr, errorDescription);
		}
	}
	
	abstract protected  void onFillRequestParams(HashMap<String, String> params);
	
	abstract protected String buidRequestUrl();
	
	abstract protected String methond();
	
	abstract protected T parse(Gson gson, String response) throws JSONException;

	
}
