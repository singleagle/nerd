package com.enjoy.nerd.usercenter;

import java.nio.charset.Charset;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.Account;
import com.enjoy.nerd.remoterequest.RegisterReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.xmpp.OpenfireRegisterReq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements View.OnClickListener, 
							SuccessResponseListner<Account>, FailResponseListner{
	static private final int MIN_PASSWORD_LEN = 6;
	static private final int REQUEST_ID_REGISTER = 1;
	static public final String KEY_UIN = "uin";
	
	private Button mRegisterBtn;
	private EditText mPhoneNOView;
	private EditText mNameView;
	private EditText mPasswordView;
	private EditText mPasswordConfirmView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mPhoneNOView = (EditText)findViewById(R.id.reg_phoneNO);
		mNameView = (EditText)findViewById(R.id.reg_user_name);
		mPasswordView = (EditText)findViewById(R.id.reg_password);
		mPasswordConfirmView = (EditText)findViewById(R.id.reg_password_confirm);
		mRegisterBtn = (Button)findViewById(R.id.reg_register_button);
		mRegisterBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void submitRegisterInfo(){
		RegisterReq request = new RegisterReq(this);
		request.setPhoneNO(mPhoneNOView.getText().toString());
		String password = mPasswordView.getText().toString();
		request.setPassword(password);
		
		if(mNameView.getText() != null){
			request.setName(mNameView.getText().toString());
		}
		request.registerListener(REQUEST_ID_REGISTER, this, this);
		request.submit(true);
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.reg_register_button:
			EditText invaidateEdit = null;
			
			if(mPhoneNOView.getText() == null || mPhoneNOView.getText().length() != 11){
				invaidateEdit = mPhoneNOView;
			}else	if(mPasswordView.getText() == null || mPasswordView.getText().length() < MIN_PASSWORD_LEN){
				invaidateEdit = mPasswordView;
			}else if(!mPasswordView.getText().toString().equals(mPasswordConfirmView.getText().toString())){
				invaidateEdit = mPasswordConfirmView;
			}
			
			if(invaidateEdit != null){
				Toast.makeText(this, R.string.invalidate_input, Toast.LENGTH_LONG).show();
				invaidateEdit.requestFocus();
			}else{
				submitRegisterInfo();
			}
			
			break;
			
		default:
			break;
		}
		
	}

	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSucess(int requestId, Account response) {
		Intent data = new Intent();
		data.putExtra(KEY_UIN, response.getUin());
		setResult(0, data);
		finish();
	}


}
