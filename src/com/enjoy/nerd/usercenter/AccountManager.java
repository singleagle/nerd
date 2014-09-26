package com.enjoy.nerd.usercenter;

import com.enjoy.nerd.remoterequest.Account;

import android.content.Context;

public class AccountManager {
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
	
	public String getLoginUserName(){
		if(mAccount != null){
			return mAccount.getName();
		}
		return null;
	}
	
	
	public void login(int uin, String encodedPwd){
		
	}
	
	public void logout(){
		mAccount = null;
	}
}
