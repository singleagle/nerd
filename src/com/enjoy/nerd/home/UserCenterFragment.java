package com.enjoy.nerd.home;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.usercenter.ConversationListActivity;
import com.enjoy.nerd.usercenter.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserCenterFragment extends Fragment implements View.OnClickListener {
	private static final int CODE_LOGIN = 1;
	
	private static final int MENU_ID_CREATE = 1;
	
	private TextView mNickView;
	AccountManager mAccountManager;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

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
    	TextView convView = (TextView)view.findViewById(R.id.my_conversation);
    	convView.setOnClickListener(this);
    	return view;
    }
    
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	MenuItem publish = menu.add(0, MENU_ID_CREATE, 0, R.string.create);
    	publish.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_ID_CREATE:
    		if(AccountManager.getInstance(getActivity()).isLogin()){
    			Intent intent = new Intent(getActivity(), CreateDistractionActivity.class);
    			startActivity(intent);
    		}else{
    			Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
    		}
    		break;
    		
    	default:
    		break;	
    	}
    	
    	return  super.onOptionsItemSelected(item);
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
		
		case R.id.my_conversation:
			Intent intent = new Intent(getActivity(), ConversationListActivity.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}

}
