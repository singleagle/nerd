package com.enjoy.nerd.usercenter;

import java.nio.charset.Charset;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.AccountManager.LoginResultListner;
import com.enjoy.nerd.remoterequest.Account;

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

public class LoginActivity extends Activity implements View.OnClickListener, LoginResultListner{
	static public final int CODE_REGISTER = 1;
	
	private View mRegisterView;
	private View mForgetPassword;
	private Button mLoginBtn;
	private EditText mUIDView;
	private EditText mPasswordView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUIDView = (EditText) findViewById(R.id.login_uin);
		mUIDView.setText(AccountManager.getInstance(this).getLastLoginUIN());
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
		if(requestCode == CODE_REGISTER && resultCode == Activity.RESULT_OK){
			long uin = data.getLongExtra(RegisterActivity.KEY_UIN, 0);
			if(uin != 0){
				mUIDView.setText(String.valueOf(uin));
			}else{
				mUIDView.setText(null);
			}
			
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void performLogin(){
		EditText invaidateEdit = null;
		
		if(mUIDView.getText() == null){
			invaidateEdit = mUIDView;
		}else	if(mPasswordView.getText() == null){
			invaidateEdit = mPasswordView;
		}
		
		if(invaidateEdit != null){
			Toast.makeText(this, R.string.invalidate_input, Toast.LENGTH_LONG).show();
			invaidateEdit.requestFocus();
		}else{

			 AccountManager.getInstance(this).login(mUIDView.getText().toString(),
					 mPasswordView.getText().toString() , this);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_login_button:
			performLogin();
			break;
			
		case R.id.login_register_view:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, CODE_REGISTER);
			break;
		}
		
	}

	@Override
	public void onSucess(Account account) {
		Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();
		setResult(1);
		finish();
	}

	@Override
	public void onFailure(int error, String reason) {
		Toast.makeText(this, getString(R.string.login_fail, error), Toast.LENGTH_LONG).show();
	}

}
