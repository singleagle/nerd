package com.enjoy.nerd.remoterequest;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.jivesoftware.smack.sasl.SASLMechanism.Success;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.google.gson.Gson;

//用于将两个PostRequest组合在一起按先后顺序批处理的请求
public class BatchPostReqest  extends PostRequest<Object> implements SuccessResponseListner<Object>, FailResponseListner{
	private static final int FIRST_REQ_ID = 1;
	private static final int SECOND_REQ_ID = 2;
	
	private PostRequest<?> firstReq;
	private PostRequest<?> secondReq;
	private PostRequestPipe pipe;
	private boolean ignoreCache;
	
	
	public BatchPostReqest(Context context) {
		super(context);

	}

	public void setRequestGroup(PostRequest<?> firstReq, PostRequest<?> secondReq, PostRequestPipe pipe){
		this.firstReq = firstReq;
		this.secondReq = secondReq;
		this.pipe = pipe;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerListener(int id,
			SuccessResponseListner successListner,
			FailResponseListner failResponseListner) {
		firstReq.registerListener(FIRST_REQ_ID, (SuccessResponseListner) this, this);
		secondReq.registerListener(SECOND_REQ_ID, (SuccessResponseListner)this, this);
		super.registerListener(id, successListner, failResponseListner);
	}



	@Override
	protected String buidRequestUrl() {
		throw new IllegalArgumentException("BatchPostReqest not pasrse!!");
	}

	@Override
	protected Object parse(Gson gson, String response)
			throws JSONException {
		throw new IllegalArgumentException("BatchPostReqest not pasrse!!");
	}


	@Override
	public void onSubmitRequest(boolean ignoreCache) {
		this.ignoreCache = ignoreCache;
		firstReq.submit(ignoreCache);
	}

	@Override
	protected void onFillRequestParams(HashMap<String, String> params) {
		throw new IllegalArgumentException("BatchPostReqest not onFillRequestParams!!");
		
	}

	
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
		if(mFailResponseListner == null){
			return;
		}
		
		if(requestId == FIRST_REQ_ID){
			mFailResponseListner.onFailure(mId, error, subErr, errDescription);
		}else if(requestId == SECOND_REQ_ID){
			mFailResponseListner.onFailure(mId, error, subErr, errDescription);
		}
		
	}


	public void onSucess(int requestId, Object response) {
		if(requestId == FIRST_REQ_ID){
			pipe.fillPipe(response, secondReq);
			secondReq.submit(ignoreCache);
		}else if(requestId == SECOND_REQ_ID){
			mSuccessListner.onSucess(mId, response);
		}
	}



	public interface PostRequestPipe{
		void fillPipe(Object content, PostRequest<?> postReq);
	}
	
}
