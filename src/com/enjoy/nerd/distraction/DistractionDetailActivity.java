package com.enjoy.nerd.distraction;

import com.enjoy.nerd.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DistractionDetailActivity extends Activity {
	static public final String KEY_DA_ID = "da_id";
	
	TextView mTitleView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String DAId = getIntent().getStringExtra(KEY_DA_ID);
		
		setContentView(R.layout.activity_distraction_detail);
		mTitleView = (TextView)findViewById(R.id.title);
		mTitleView.setText(DAId);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
