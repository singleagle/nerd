package com.enjoy.nerd;

import com.enjoy.nerd.remoterequest.Account;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.xmpp.IMMessageRecieverService;
import com.enjoy.nerd.remoterequest.xmpp.OpenfireLoginReq;
import com.enjoy.nerd.utils.SharePreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;

public class AccountManager {
	
	static private final String KEY_ACCOUNT = "account";
	
	private static AccountManager sSelf;
	private Account mAccount;
	private Context mContext;
	
	private AccountManager(Context context){
		mContext = context;
	}
	
	public static AccountManager getInstance(Context context){
		if(sSelf == null){
			sSelf = new AccountManager(context);
		}
		return sSelf;
	}
	
	public boolean isLogin(){
		return mAccount != null;
	}
	
	public long getLoginUIN(){
		if(mAccount != null){
			return mAccount.getUin();
		}
		return 0;
	}
	
	public String getLoginUserName(){
		if(mAccount != null){
			return mAccount.getName();
		}
		return null;
	}
	
	private Account getSavedAccount(){
		String accountJson = SharePreferenceUtil.getString(mContext, KEY_ACCOUNT);
		if(accountJson != null && accountJson.length() != 0){
			Gson gson = new GsonBuilder().create();
			Account account = gson.fromJson(accountJson, Account.class);
			return account;
		}

		return null;	
	}
	
	private void  saveAccount(Account account){
		Gson gson = new GsonBuilder().create();
		String accountJson = gson.toJson(account);
		SharePreferenceUtil.putString(mContext, KEY_ACCOUNT, accountJson);
	}
	
	public String getLastLoginUIN(){
		Account saved = getSavedAccount();
		if(saved != null){
			return String.valueOf(saved.getUin());
		}
		return null;
	}
	
	public void login(String uid, String password, LoginResultListner listner){
		OpenfireLoginReq request = new OpenfireLoginReq(mContext);
		request.setUid(uid);
		request.setPassword(password);
		LoginResponseListner respListner = new LoginResponseListner(listner);
		request.registerListener(0, respListner, respListner);
		request.submit(true);
	}
	
	public void logout(){
		mAccount = null;
		SharePreferenceUtil.remove(mContext, KEY_ACCOUNT);
		stopIMMessagerReciever();
		NerdApp app = (NerdApp) mContext.getApplicationContext();
		app.getXMPPClient().release();
		
	}
	
	private void startIMMessageReciever(){
		Intent service = new Intent(mContext, IMMessageRecieverService.class);
		mContext.startService(service);
	}
	
	private void stopIMMessagerReciever(){
		Intent service = new Intent(mContext, IMMessageRecieverService.class);
		mContext.stopService(service);
	}
	
	public interface LoginResultListner{
		
		void onSucess(Account account);
		void onFailure(int error, String reason);
	}
	
	private  class LoginResponseListner implements SuccessResponseListner<Account>, FailResponseListner{
		LoginResultListner mListner;

		public LoginResponseListner(LoginResultListner mListner) {
			this.mListner = mListner;
		}

		@Override
		public void onFailure(int requestId, int error, int subErr,
				String errDescription) {
			mAccount = null;
			if(mListner != null){
				mListner.onFailure(error, errDescription);
			}
			
		}

		@Override
		public void onSucess(int requestId, Account response) {
			mAccount = response;
			saveAccount(response);
			startIMMessageReciever();
			if(mListner != null){
				mListner.onSucess(mAccount);
			}
			
		}
	}
}
