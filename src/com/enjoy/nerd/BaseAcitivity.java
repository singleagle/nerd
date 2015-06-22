package com.enjoy.nerd;

import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseAcitivity extends FragmentActivity implements FailResponseListner {

	
	
	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
		Toast.makeText(getApplicationContext(), errDescription + ":" + error, Toast.LENGTH_SHORT).show();
	}

}
