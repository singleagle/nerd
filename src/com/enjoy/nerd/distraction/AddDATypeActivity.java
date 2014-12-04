package com.enjoy.nerd.distraction;


import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.AddDATypeReq;
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


public class AddDATypeActivity extends Activity implements OnClickListener, FailResponseListner,
										SuccessResponseListner<String> {
	
	private static final int REQ_ID_ADDTYPE = 1;
	
	private Button mSubmitBtn;
	private EditText mTypeNameView;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_datype);
        mTypeNameView = (EditText)findViewById(R.id.type_name);
        mSubmitBtn = (Button)findViewById(R.id.submit_button);
        mSubmitBtn.setOnClickListener(this);
    }

    private void requestAddDAType(String typeName){
    	AddDATypeReq request = new AddDATypeReq(this);
    	request.setMainTypeId(0);
    	request.setSubTypeName(typeName);
    	request.setCreateUIN(AccountManager.getInstance(this).getLoginUIN());
    	request.registerListener(REQ_ID_ADDTYPE, this, this);
    	request.submit();
    }
    
	@Override
	public void onClick(View v) {
		String typeName = mTypeNameView.getText().toString();
		if(!AccountManager.getInstance(this).isLogin()){
			Toast.makeText(this, R.string.login_tips, Toast.LENGTH_LONG).show();
		}else if(TextUtils.isEmpty(typeName)){
			//TODO: toast
		}else{
			requestAddDAType(typeName);
		}
	}
	


	@Override
	public void onFailure(int requestId, int error, int subErr, String errDescription) {
		
	}

	@Override
	public void onSucess(int requestId, String response) {
		setResult(RESULT_OK);
		finish();
	}
	


}
