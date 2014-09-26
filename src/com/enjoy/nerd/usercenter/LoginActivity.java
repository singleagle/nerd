package com.enjoy.nerd.usercenter;

import com.enjoy.nerd.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener{
	static public final int CODE_REGISTER = 1;
	
	private View mRegisterView;
	private View mForgetPassword;
	private Button mLoginBtn;
	private EditText mUinView;
	private EditText mPasswordView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUinView = (EditText) findViewById(R.id.login_uin);
		mPasswordView = (EditText) findViewById(R.id.login_password);
		
		mLoginBtn = (Button) findViewById(R.id.login_login_button);
		mLoginBtn.setOnClickListener(this);
		mRegisterView = findViewById(R.id.login_register_view);
		mRegisterView.setOnClickListener(this);
		mForgetPassword = findViewById(R.id.login_forget_password_view);
		mForgetPassword.setOnClickListener(this);

		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CODE_REGISTER){
			long uin = data.getLongExtra(RegisterActivity.KEY_UIN, 0);
			if(uin != 0){
				mUinView.setText(String.valueOf(uin));
			}else{
				mUinView.setText(null);
			}
			
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_login_button:
			break;
			
		case R.id.login_register_view:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, CODE_REGISTER);
			break;
		}
		
	}

}
