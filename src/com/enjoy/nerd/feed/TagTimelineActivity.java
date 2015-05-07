package com.enjoy.nerd.feed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.DistractionDetailActivity;
import com.enjoy.nerd.feed.ScenicDetailActivity;
import com.enjoy.nerd.remoterequest.FeedProfile;
import com.enjoy.nerd.remoterequest.TaggedFeedReq;
import com.enjoy.nerd.remoterequest.FeedProfile.PageFeed;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TagTimelineActivity extends BaseAcitivity implements OnRefreshListener<ListView>, OnItemClickListener,
					SuccessResponseListner<PageFeed>, FailResponseListner{
	
	static public final String FEED_TAG_NAME = "FEED_TAG_NAME";
	static private final int REQ_ID_TIMELINE = 1;
	private final  SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private FeedAdrapter mAdapter;
    private String mTag;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	mTag = getIntent().getStringExtra(FEED_TAG_NAME);
    	setContentView(R.layout.timeline_feed_list);
    	mPullListView = (PullToRefreshListView)findViewById(R.id.pull_list);
    	mPullListView.setOnRefreshListener(this);
    	mPullListView.setPullLoadEnabled(true);
    	mListView = mPullListView.getRefreshableView();
    	mListView.setOnItemClickListener(this);
    	mAdapter = new FeedAdrapter(this);
    	mListView.setAdapter(mAdapter);
    	getActionBar().setTitle(mTag);
    	requestTagTimeline(0, false);
    }

    private void requestTagTimeline(int startIndex, boolean ignoreCache){
    	TaggedFeedReq request = new TaggedFeedReq(this);
    	request.registerListener(REQ_ID_TIMELINE, this, this);
    	request.setPager(startIndex, 6);
    	request.setTag(mTag);
    	request.submit(ignoreCache);
    }
    
    
	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
		
	}

	@Override
	public void onSucess(int requestId, PageFeed response) {
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        List<FeedProfile> feedList = response.getFeedList();
        if(feedList != null){
        	 boolean hasMoreData = response.getStartIndex() + feedList.size() < response.getTotalCount();
        	 mPullListView.setHasMoreData(hasMoreData);
        	 boolean clear = response.getStartIndex() == 0;
        	 mAdapter.addFeedList(feedList, clear);
        }
        setLastUpdateTime();
	}
    
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestTagTimeline(0, true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestTagTimeline(mAdapter.getCount(), false);
	}
	
	
    private void setLastUpdateTime() {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));;
        mPullListView.setLastUpdatedLabel(text);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FeedProfile feed = (FeedProfile)parent.getItemAtPosition(position);
		Intent 	intent = new Intent(this, ScenicDetailActivity.class);
		intent.putExtra(ScenicDetailActivity.KEY_FEED_ID, feed.getId());
		startActivity(intent);
	}

}
