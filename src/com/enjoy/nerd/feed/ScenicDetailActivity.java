package com.enjoy.nerd.feed;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.CreateDistractionActivity;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.ScenicDetail;
import com.enjoy.nerd.remoterequest.ScenicDetailReq;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScenicDetailActivity extends BaseAcitivity implements View.OnClickListener, SuccessResponseListner<ScenicDetail> {
	public static final String KEY_FEED_ID ="feed_id";
	public static final String KEY_CREATOR_UID ="creator_uid";
	
	private Button mDABtn, mShareBtn, mLikeBtn;
	private LinearLayout mTagContainer;
	private String mFeedId;
	private ImageLoader mImageLoader;
	private ScenicDetail mScenic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFeedId = getIntent().getStringExtra(KEY_FEED_ID);
		setContentView(R.layout.activity_scenic_detail);
		
		mDABtn = (Button)findViewById(R.id.create_distraction);
		mDABtn.setOnClickListener(this);
		mShareBtn = (Button)findViewById(R.id.share);
		mShareBtn.setOnClickListener(this);
		mLikeBtn = (Button)findViewById(R.id.like);
		mLikeBtn.setOnClickListener(this);
		mTagContainer = (LinearLayout)findViewById(R.id.tag_container);
		mImageLoader = ((NerdApp)getApplication()).getImageLoader();
		requestScenicDetail();
		
	}
	
	private void requestScenicDetail(){
		ScenicDetailReq request = new ScenicDetailReq(this, mFeedId);
		request.registerListener(100, this, this);
		request.submit(false);
	}
	
	private void fillTag(List<String> tagNameList){
		if(tagNameList.size() == 0){
			mTagContainer.setVisibility(ViewGroup.GONE);
			return;
		}
		mTagContainer.setVisibility(ViewGroup.VISIBLE);
		mTagContainer.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for(String name : tagNameList){
			TextView tagView = (TextView) inflater.inflate(R.layout.tag_item, mTagContainer, false);
			tagView.setText(name);
			tagView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ScenicDetailActivity.this, FocusTimelineActivity.class);
					intent.putExtra(FocusTimelineActivity.FEED_FOCUS_NAME, ((TextView)v).getText());
					startActivity(intent);
				}
			});
			mTagContainer.addView(tagView);
		}
	}
	
	
	private void bindScenicDetail(final ScenicDetail scenic){
		NetworkImageView mainImage =(NetworkImageView)findViewById(R.id.main_image);
		mainImage.setDefaultImageResId(R.drawable.default_scenic_img);
		mainImage.setErrorImageResId(R.drawable.default_scenic_img);
		mainImage.setImageUrl(scenic.getImageUrl(), mImageLoader);
		((TextView)findViewById(R.id.title)).setText(scenic.getTitle());
		String distance = getString(R.string.distance_meters, scenic.getFarawayMeters());
		((TextView)findViewById(R.id.distance)).setText(distance);
		((TextView)findViewById(R.id.view_num)).setText(String.valueOf(scenic.getLikeNum()));
		findViewById(R.id.location).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ScenicDetailActivity.this, LocationViewActivity.class);
				intent.putExtra(LocationViewActivity.POI_LOCATION, scenic.getLocation());
				startActivity(intent);
			}
		});
		fillTag(scenic.getTagNameList());
		
		((TextView)findViewById(R.id.description)).setText(scenic.getDescription());
		if(scenic.getOthersImgurl() != null){
			ViewGroup imgContainer = (ViewGroup)findViewById(R.id.img_container);
			imgContainer.removeAllViews();
			imgContainer.setVisibility(ViewGroup.VISIBLE);
			
			for(String imgurl : scenic.getOthersImgurl()){
				NetworkImageView image = (NetworkImageView) LayoutInflater.from(this).inflate(R.layout.scenic_img_item, imgContainer, false);
				image.setDefaultImageResId(R.drawable.default_scenic_img);
				image.setErrorImageResId(R.drawable.default_scenic_img);
				image.setImageUrl(imgurl, mImageLoader);
				imgContainer.addView(image);
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
		case R.id.share:

			break;
		case R.id.like:
			
			break;
			
		case R.id.create_distraction:
			Intent intent = new Intent(this, CreateDistractionActivity.class);
			intent.putExtra(CreateDistractionActivity.POI_LOCATION, mScenic.getLocation());
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void onSucess(int requestId, ScenicDetail response) {
		mScenic = response;
		bindScenicDetail(response);
		
	}


}
