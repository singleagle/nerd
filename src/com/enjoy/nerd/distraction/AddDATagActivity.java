package com.enjoy.nerd.distraction;


import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.AddDATagReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class AddDATagActivity extends BaseAcitivity implements OnClickListener,
										SuccessResponseListner<String> {
	
	private static final int REQ_ID_ADDTAG = 1;
	
	private Button mSubmitBtn;
	private EditText mTagNameView;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_datype);
        mTagNameView = (EditText)findViewById(R.id.type_name);
        mSubmitBtn = (Button)findViewById(R.id.submit_button);
        mSubmitBtn.setOnClickListener(this);
    }

    private void requestAddDATag(String tagName){
    	AddDATagReq request = new AddDATagReq(this);
    	request.setTagName(tagName);
    	request.setCreateUIN(AccountManager.getInstance(this).getLoginUIN());
    	request.registerListener(REQ_ID_ADDTAG, this, this);
    	request.submit(false);
    }
    
	@Override
	public void onClick(View v) {
		String tagName = mTagNameView.getText().toString();
		if(!AccountManager.getInstance(this).isLogin()){
			Toast.makeText(this, R.string.login_tips, Toast.LENGTH_LONG).show();
		}else if(TextUtils.isEmpty(tagName)){
			//TODO: toast
		}else{
			requestAddDATag(tagName);
		}
	}

	@Override
	public void onSucess(int requestId, String response) {
		setResult(RESULT_OK);
		finish();
	}
	


}
