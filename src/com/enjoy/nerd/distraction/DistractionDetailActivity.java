package com.enjoy.nerd.distraction;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DistractionDetailActivity extends BaseAcitivity implements View.OnClickListener {
	public static final String KEY_DA_ID ="da_id";
	public static final String KEY_DA_CREATOR_UID ="da_creator_uid";
	
	private Button mCreateChatView;
	private String mDAId;
	private long mUId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDAId = getIntent().getStringExtra(KEY_DA_ID);
		mUId = getIntent().getLongExtra(KEY_DA_CREATOR_UID, 0);
		
		setContentView(R.layout.activity_distraction_detail);
		
		mCreateChatView = (Button)findViewById(R.id.create_chat);
		mCreateChatView.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.create_chat:
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.KEY_RECIPIENT_ID, String.valueOf(mUId));
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}


}
