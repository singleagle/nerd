package com.enjoy.nerd.remoterequest;

import javax.xml.transform.ErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.utils.LogWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;


abstract public class RemoteRequest<T> {
	static private final String TAG = "RemoteRequest";
	protected Context mContext;
	protected int mId;
	protected FailResponseListner mFailResponseListner;
	protected SuccessResponseListner<T> mSuccessListner;
	
	public RemoteRequest(Context context){
		
	}
	
	
	public void registerListener(int id, SuccessResponseListner<T> successListner, FailResponseListner failResponseListner ){
		mId = id;
		mSuccessListner = successListner;
		mFailResponseListner = failResponseListner;
	}
	
	public void submit(){
		submit(false);
	}
	
	public void submit(boolean ignoreCache){
		onSubmitRequest(ignoreCache);
	}
	
	
	abstract protected void onSubmitRequest(boolean ignoreCache);

	
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
