package com.enjoy.nerd.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.DistractionDetailActivity;
import com.enjoy.nerd.feed.ScenicDetailActivity;
import com.enjoy.nerd.remoterequest.IFeed;
import com.enjoy.nerd.remoterequest.RecommendFeed;
import com.enjoy.nerd.remoterequest.RecommendFeed.PageFeed;
import com.enjoy.nerd.remoterequest.RecommendFeedReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RecommandFeedFragment extends Fragment implements OnRefreshListener<ListView>, OnItemClickListener,
					SuccessResponseListner<PageFeed>, FailResponseListner{
	
	static private final int REQ_ID_FEEDLIST = 1;
	
    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private FeedAdrapter mAdapter;
    
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.recommend_feed_list, container, false);
    	mPullListView = (PullToRefreshListView)view.findViewById(R.id.pull_list);
    	mPullListView.setOnRefreshListener(this);
    	mPullListView.setPullLoadEnabled(true);
    	mListView = mPullListView.getRefreshableView();
    	mListView.setOnItemClickListener(this);
    	mAdapter = new FeedAdrapter(getActivity());
    	mListView.setAdapter(mAdapter);
    	getActivity().getActionBar().setTitle(R.string.recommend);
    	requestRecommendList(0);
    	return view;
    }

    private void requestRecommendList(int startIndex){
    	RecommendFeedReq request = new RecommendFeedReq(getActivity());
    	request.registerListener(REQ_ID_FEEDLIST, this, this);
    	request.setPager(startIndex, 6);
    	request.setLocation(113.9, 22.5);
    	request.submit();
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
        List<RecommendFeed> feedList = response.getFeedList();
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
		requestRecommendList(0);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestRecommendList(mAdapter.getCount());
	}
	
	
    private void setLastUpdateTime() {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));;
        mPullListView.setLastUpdatedLabel(text);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RecommendFeed feed = (RecommendFeed)parent.getItemAtPosition(position);
		
		Intent intent;
		switch(feed.getFeedSubject()){
		case SCENIC:
			intent = new Intent(getActivity(), ScenicDetailActivity.class);
			intent.putExtra(ScenicDetailActivity.KEY_FEED_ID, feed.getId());
			startActivity(intent);
			break;
			
		case DISTRACTION:
			intent = new Intent(getActivity(), DistractionDetailActivity.class);
			intent.putExtra(DistractionDetailActivity.KEY_FEED_ID, feed.getId());
			startActivity(intent);
			break;
			
		default:
			break;
			
		
		}
	}


}
