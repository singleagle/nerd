package com.enjoy.nerd.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.DistractionProfile;
import com.enjoy.nerd.remoterequest.DistractionProfile.PageDAProfile;
import com.enjoy.nerd.remoterequest.NearbyDistractionReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NearByFragment extends Fragment implements OnRefreshListener<ListView>, OnItemClickListener,
					SuccessResponseListner<PageDAProfile>, FailResponseListner{
	
	static private final int REQ_ID_PROFILELIST = 1;
	
    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private DAProfileAdrapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.nearby, container, false);
    	mPullListView = (PullToRefreshListView)view.findViewById(R.id.pull_list);
    	mPullListView.setOnRefreshListener(this);
    	mPullListView.setPullLoadEnabled(true);
    	mListView = mPullListView.getRefreshableView();
    	mListView.setOnItemClickListener(this);
    	mAdapter = new DAProfileAdrapter(getActivity());
    	mListView.setAdapter(mAdapter);
    	getActivity().getActionBar().setTitle(R.string.nearby);
    	requestDAProfileList(0);
    	return view;
    }

    private void requestDAProfileList(int startIndex){
    	NearbyDistractionReq request = new NearbyDistractionReq(getActivity());
    	request.registerListener(REQ_ID_PROFILELIST, this, this);
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
	public void onSucess(int requestId, PageDAProfile response) {
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        List<DistractionProfile> profileList = response.getDAProfileList();
        if(profileList != null){
        	 boolean hasMoreData = response.getStartIndex() + profileList.size() < response.getTotalCount();
        	 mPullListView.setHasMoreData(hasMoreData);
        	 boolean clear = response.getStartIndex() == 0;
        	 mAdapter.addProfileList(profileList, clear);
        }
        setLastUpdateTime();
	}
    
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestDAProfileList(0);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        requestDAProfileList(mAdapter.getCount());
	}
	
	
    private void setLastUpdateTime() {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));;
        mPullListView.setLastUpdatedLabel(text);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DistractionProfile profile = (DistractionProfile)parent.getItemAtPosition(position);
	}


}
