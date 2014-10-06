package com.enjoy.nerd.home;

import com.enjoy.nerd.R;
import com.enjoy.nerd.usercenter.AccountManager;
import com.enjoy.nerd.usercenter.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserCenterFragment extends Fragment implements View.OnClickListener {
	private static final int CODE_LOGIN = 1;
	
	private TextView mNickView;
	AccountManager mAccountManager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	getActivity().getActionBar().setTitle(R.string.user_center);
    	mAccountManager = AccountManager.getInstance(getActivity());
    	View view = inflater.inflate(R.layout.user_center, container, false);
    	mNickView = (TextView) view.findViewById(R.id.nick_name);
    	if(mAccountManager.isLogin()){
    		mNickView.setText(mAccountManager.getLoginUserName());
    	}
    	mNickView.setOnClickListener(this);
    	return view;
    }

    
    private void startLoginActivity(){
    	Intent intent = new Intent(getActivity(), LoginActivity.class);
    	startActivityForResult(intent, CODE_LOGIN);
    }
    
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CODE_LOGIN){
			if(mAccountManager.isLogin()){
				mNickView.setText(mAccountManager.getLoginUserName());
			}
			
		}
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.nick_name:
			if(mAccountManager.isLogin()){
				mAccountManager.logout();
				mNickView.setText(null);
				Toast.makeText(getActivity(), R.string.logout_success, Toast.LENGTH_LONG).show();
			}else{
				startLoginActivity();
			}
			break;
			
		default:
			break;
		}
	}

}
