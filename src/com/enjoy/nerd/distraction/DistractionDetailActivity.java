package com.enjoy.nerd.distraction;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.R;
import com.enjoy.nerd.feed.LocationViewActivity;
import com.enjoy.nerd.feed.ScenicDetailActivity;
import com.enjoy.nerd.remoterequest.DistractionDetail;
import com.enjoy.nerd.remoterequest.DistractionDetailReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.ScenicDetailReq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DistractionDetailActivity extends BaseAcitivity implements View.OnClickListener,
							SuccessResponseListner<DistractionDetail> {
	public static final String KEY_FEED_ID ="FEED_ID";
	
	private final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yy-MM-dd");
	
	private Button mCreateChatView;
	private String mFeedId;
	private DistractionDetail mDistraction;
	private ImageLoader mImageLoader ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFeedId = getIntent().getStringExtra(KEY_FEED_ID);
		
		setContentView(R.layout.activity_distraction_detail);
		mCreateChatView = (Button)findViewById(R.id.create_chat);
		mCreateChatView.setOnClickListener(this);
		mImageLoader = ((NerdApp)getApplication()).getImageLoader();
		requestDistractionDetail();
	}
	
	private void requestDistractionDetail(){
		DistractionDetailReq request = new DistractionDetailReq(this, mFeedId);
		request.registerListener(100, this, this);
		request.submit(false);
	}

	private void bindDistractionDetail(final DistractionDetail distraction){
		NetworkImageView image =(NetworkImageView)findViewById(R.id.image);
		image.setDefaultImageResId(R.drawable.default_da_img);
		image.setErrorImageResId(R.drawable.default_da_img);
		image.setImageUrl(distraction.getImageUrl(), mImageLoader);
		((TextView)findViewById(R.id.title)).setText(distraction.getTitle());
		String distance = getString(R.string.distance_meters, distraction.getFarawayMeters());
		((TextView)findViewById(R.id.distance)).setText(distance);
		((TextView)findViewById(R.id.partner_count)).setText(String.valueOf(distraction.getProfile().getPartnerCount()));
		((TextView)findViewById(R.id.description)).setText(distraction.getDescription());
		((TextView)findViewById(R.id.dest)).setText(distraction.getProfile().getDestAddress());
		String timeStr = DATAFORMAT.format(new Date(distraction.getProfile().getStartTime()));
		((TextView)findViewById(R.id.starttime)).setText(timeStr);
		findViewById(R.id.dest_container).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DistractionDetailActivity.this, LocationViewActivity.class);
				intent.putExtra(LocationViewActivity.POI_LOCATION, distraction.getProfile().getDestLocation());
				startActivity(intent);
			}
		});
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
			long recipientId = mDistraction.getTribeId();
			if(recipientId == 0){
				recipientId = mDistraction.getProfile().getCreateUserInfo().getUin();
			}
			intent.putExtra(ChatActivity.KEY_RECIPIENT_ID, String.valueOf(recipientId));
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}

	public void onSucess(int requestId, DistractionDetail response) {
		mDistraction = response;
		bindDistractionDetail(response);
		
	}


}
