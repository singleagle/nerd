package com.enjoy.nerd.feed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.DistractionDetailActivity;
import com.enjoy.nerd.feed.ScenicDetailActivity;
import com.enjoy.nerd.remoterequest.FeedProfile;
import com.enjoy.nerd.remoterequest.FeedProfile.PageFeed;
import com.enjoy.nerd.remoterequest.FocusFeedReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.FocusFeedReq.FocusPageFeed;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FocusTimelineActivity extends BaseAcitivity implements OnRefreshListener<ListView>, OnItemClickListener,
					SuccessResponseListner<FocusPageFeed>, FailResponseListner{
	
	static public final String FEED_FOCUS_TYPE = "FEED_FOCUS_TYPE";
	static public final String FEED_FOCUS_NAME = "FEED_FOCUS_NAME";
	static private final int REQ_ID_TIMELINE = 1;
	static private final int MENU_ID_FOLLOW = 1;
	private final  SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private FeedAdrapter mAdapter;
    private String mFocusName;
    private String mFocusType;
    private boolean mIsFollowed;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mFocusName = getIntent().getStringExtra(FEED_FOCUS_NAME);
    	mFocusType = getIntent().getStringExtra(FEED_FOCUS_TYPE);
    	setContentView(R.layout.timeline_feed_list);
    	mPullListView = (PullToRefreshListView)findViewById(R.id.pull_list);
    	mPullListView.setOnRefreshListener(this);
    	mPullListView.setPullLoadEnabled(true);
    	mListView = mPullListView.getRefreshableView();
    	mListView.setOnItemClickListener(this);
    	mAdapter = new FeedAdrapter(this);
    	mListView.setAdapter(mAdapter);
    	getActionBar().setTitle(mFocusName);
    	requestTagTimeline(0, false);
    }

    private void requestTagTimeline(int startIndex, boolean ignoreCache){
    	FocusFeedReq request = new FocusFeedReq(this);
    	request.registerListener(REQ_ID_TIMELINE, this, this);
    	request.setPager(startIndex, 6);
    	request.setFocusName(mFocusName);
    	request.setFocusType(mFocusType);
    	request.submit(ignoreCache);
    }
    
    
	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
		
	}

	@Override
	public void onSucess(int requestId, FocusPageFeed response) {
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mIsFollowed = response.isFollowed();
        if(mIsFollowed){
        	invalidateOptionsMenu();
        }
        PageFeed pageFeed = response.getPageFeed();
        if(pageFeed !=null){
            List<FeedProfile> feedList = pageFeed.getFeedList();
            if(feedList != null){
            	 boolean hasMoreData = pageFeed.getStartIndex() + feedList.size() < pageFeed.getTotalCount();
            	 mPullListView.setHasMoreData(hasMoreData);
            	 boolean clear = pageFeed.getStartIndex() == 0;
            	 mAdapter.addFeedList(feedList, clear);
            }       	
        }
        setLastUpdateTime();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuItem follow = menu.add(0, MENU_ID_FOLLOW, 0, R.string.follow);
    	follow.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	return true;
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.getItem(0);
		if(mIsFollowed){
			item.setTitle(R.string.unfollow);
		}else{
			item.setTitle(R.string.follow);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	
	@Override  
	  public boolean onOptionsItemSelected(MenuItem item){  
		if(item.getItemId() == MENU_ID_FOLLOW){
			mIsFollowed = !mIsFollowed;
			invalidateOptionsMenu();
		}
		return super.onOptionsItemSelected(item);
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
