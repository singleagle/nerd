package com.enjoy.nerd.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.DistractionDetailActivity;
import com.enjoy.nerd.feed.FocusTimelineActivity;
import com.enjoy.nerd.feed.ScenicDetailActivity;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.FeedTopic;
import com.enjoy.nerd.remoterequest.HotFocusListReq;
import com.enjoy.nerd.remoterequest.HotFocusListReq.FocusGroup;
import com.enjoy.nerd.remoterequest.IFocusable;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;

public class HotspotFragment extends Fragment implements OnChildClickListener,
					SuccessResponseListner<FocusGroup>, FailResponseListner{
	
	static private final int REQ_ID_PROFILELIST = 1;
	
    private ExpandableListView mFocusListView;
    private FocusListAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.focus_list, container, false);
		mFocusListView = (ExpandableListView) view.findViewById(R.id.focus_list);
		mFocusListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent,
					View v, int groupPosition, long id) {
				return true;
			}
		});
		
		mFocusListView.setOnChildClickListener(this);
		requestHotFocusList(0);
    	return view;
    }
    
	private void expandAllGroup(){
		for(int i = 0; i < mAdapter.getGroupCount(); i++){
			mFocusListView.expandGroup(i);
		}
		mFocusListView.setSelectionFromTop(0, 0);
	}

    private void requestHotFocusList(int startIndex){
    	HotFocusListReq request = new HotFocusListReq(getActivity());
    	request.registerListener(REQ_ID_PROFILELIST, this, this);
    	request.submit();
    }
    
    
	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {

		
	}

	@Override
	public void onSucess(int requestId, FocusGroup response) {
		mAdapter = new FocusListAdapter(getActivity(), response);
		mFocusListView.setAdapter(mAdapter);
		expandAllGroup();
	}
 
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		IFocusable focus = (IFocusable)mAdapter.getChild(groupPosition, childPosition);
		Intent intent = new Intent(getActivity(), FocusTimelineActivity.class);
		intent.putExtra(FocusTimelineActivity.FEED_FOCUS_NAME, focus.getName());
		if(focus instanceof FeedTopic){
			intent.putExtra(FocusTimelineActivity.FEED_FOCUS_TYPE, "topic");
		}
		startActivity(intent);
		
		return true;
	}


}
