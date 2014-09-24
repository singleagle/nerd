package com.enjoy.nerd.remoterequest;

import javax.xml.transform.ErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.http.RequestParams;
import com.enjoy.nerd.utils.LogWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;


abstract public class RemoteRequest<T> {
	static private final String TAG = "RemoteRequest";
	
	static public final String VENUS_BASE_URL = "http://192.168.1.5:8080/Venus";
	protected final TextRequestSender mSender;
	protected Context mContext;
	int mId;
	private FailResponseListner mFailResponseListner;
	private SuccessResponseListner<T> mSuccessListner;
	
	public RemoteRequest(Context context){
		mSender = new TextRequestSender(context);
	}
	
	
	public void registerListener(int id, SuccessResponseListner<T> successListner, FailResponseListner failResponseListner ){
		mId = id;
		mSuccessListner = successListner;
		mFailResponseListner = failResponseListner;
	}
	
	
	
	final void fillRequestParams(RequestParams params){
		onFillRequestParams(params);
	}
	
	public void submit(){
		mSender.submitRequest(this);
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
				T data = parse(gson, jsonData.getJSONObject("body"));
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
		LogWrapper.e(TAG, "dispatchFailureResult, parse failed:" + error);
		if(mFailResponseListner != null){
			mFailResponseListner.onFailure(mId, error, serverErr, errorDescription);
		}
	}
	
	abstract protected  void onFillRequestParams(RequestParams params);
	
	abstract protected String buidRequestUrl();
	
	abstract protected String methond();
	
	abstract protected T parse(Gson gson, JSONObject response) throws JSONException;


	
	public interface SuccessResponseListner<T>{
		void onSucess(int requestId, T response);
	}
	
	public interface FailResponseListner{
		static final int ERR_UNKNOWN = -1;
		static final int ERR_NO_NETWORK = -2;
		static final int ERR_CONNECT_TIMEOUT = -3;
		static final int ERR_REQUEST_TIMEOUT = -4;
		static final int ERR_INVALIDATE_REQUEST = -5;
		/**
		 * 接收到服务器的回复，但该回复的内容是表述该请求没有得到正常处理的原因，具体错误类型由subErr来表示，该subErr是由服务器给出的错误类型
		 */
		static final int ERR_ERROR_MESSAGE = -5;
		static final int ERR_INVALID_FORMAT_MESSAGE = -6;
		
		/**
		 * 
		 * @param requestId
		 * @param error: ERR_XXX等值
		 * @param subErr:是由服务器给出的错误类型,只有error为ERR_ERROR_MESSAGE才有效，error为其它值时应为0
		 * @param errDescription
		 */
		void onFailure(int requestId, int error, int subErr, String errDescription);
	}
}
