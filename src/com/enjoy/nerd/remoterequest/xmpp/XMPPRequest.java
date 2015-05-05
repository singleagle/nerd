package com.enjoy.nerd.remoterequest.xmpp;

import javax.xml.transform.ErrorListener;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.remoterequest.RemoteRequest;
import com.enjoy.nerd.utils.LogWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.os.Handler;


abstract public class XMPPRequest<T> extends RemoteRequest<T> {
	static private final String TAG = "XMPPRequest";
	XMPPClient mClient;
	Handler mUIHandler;
	
	public XMPPRequest(Context context){
		super(context);
		mClient = ((NerdApp)context.getApplicationContext()).getXMPPClient();
		mUIHandler = new Handler();
	}
	
	@Override
	final public void onSubmitRequest(boolean ignoreCache){
		mClient.excuteRequest(this);
	}
	
	final void onSuccessResult(Object xmppResponse){
		T data = parse(xmppResponse);
		if(mSuccessListner != null){
			mSuccessListner.onSucess(mId, data);
		}
	}
	
	
	final void onFailureResult(final int error, final int serverErr, final String errorDescription){
		if(mFailResponseListner != null){
			mFailResponseListner.onFailure(mId, error, serverErr, errorDescription);
		}
	}
	
	final void  dispatchSuccessResult(final Object xmppResponse){
		mUIHandler.post(new Runnable() {

			@Override
			public void run() {
				onSuccessResult(xmppResponse);
			}
			
		});
	}
	
	final void dispatchFailureResult(final int error, final int serverErr, final String errorDescription){
		mUIHandler.post(new Runnable() {
			
			@Override
			public void run() {
				onFailureResult(error, serverErr, errorDescription);
			}
		});
	}
	
	/**
	 * 该函数在另一个线程中调用
	 * @param connection
	 * @return
	 */
	abstract protected  Object handleSendAction(Connection connection)throws XMPPException;
	abstract protected T parse(Object response);
	
}
