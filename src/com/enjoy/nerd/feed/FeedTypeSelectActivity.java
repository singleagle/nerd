package com.enjoy.nerd.feed;

import java.util.ArrayList;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.CreateDistractionActivity;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.FeedSubject;
import com.enjoy.nerd.remoterequest.FeedTypeListReq;
import com.enjoy.nerd.remoterequest.FeedTypeListReq.FeedTagGroup;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class FeedTypeSelectActivity extends BaseAcitivity implements OnChildClickListener,
												SuccessResponseListner<ArrayList<FeedTagGroup>>{
	private ExpandableListView mFeedTyeListView;
	private FeedTypeListAdapter mAdapter;
	
	@Override
    public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.activity_feedtype_list);
		mFeedTyeListView = (ExpandableListView) findViewById(R.id.feedtype_list);
		mFeedTyeListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent,
					View v, int groupPosition, long id) {
				return true;
			}
		});
		
		mFeedTyeListView.setOnChildClickListener(this);
		FeedTypeListReq request = new FeedTypeListReq(this);
		request.registerListener(1, this, this);
		request.submit(false);
		
	}
	
	private void expandAllGroup(){
		for(int i = 0; i < mAdapter.getGroupCount(); i++){
			mFeedTyeListView.expandGroup(i);
		}
		mFeedTyeListView.setSelectionFromTop(0, 0);
	}

	
	public void onSucess(int requestId, ArrayList<FeedTagGroup> response) {
		mAdapter = new FeedTypeListAdapter(this, response);
		mFeedTyeListView.setAdapter(mAdapter);
		expandAllGroup();
		
	}


	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		FeedTag tag = (FeedTag)mAdapter.getChild(groupPosition, childPosition);
		Intent intent = null;
		
		switch(tag.getSubject()){
		case DISTRACTION:
   			intent = new Intent(this, CreateDistractionActivity.class);
   			intent.putExtra(CreateDistractionActivity.FEED_TAG, tag);
			startActivity(intent);
			break;
		
		case SCENIC:
   			intent = new Intent(this, CreateScenicActivity.class);
   			intent.putExtra(CreateScenicActivity.FEED_TAG, tag);
			startActivity(intent);
			break;
			
		default:
			break;
		}
		return true;
	}
}
