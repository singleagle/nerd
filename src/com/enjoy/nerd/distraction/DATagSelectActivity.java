package com.enjoy.nerd.distraction;

import java.util.ArrayList;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.DATagReq;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DATagSelectActivity extends BaseAcitivity implements OnClickListener,
										SuccessResponseListner<ArrayList<FeedTag>>, OnItemClickListener {
	
	public static final String FEEDTAG = "tag";
	private static final int REQ_ID_TYPE = 1;
	
	private static final int REQ_CODE_ADDTYPE = 1;
	
	private ListView mListView;
	private ImageView mAddImg;
	private DATagAdapter mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datype_list);
        mListView = (ListView)findViewById(R.id.type_list);
        mAddImg = (ImageView)findViewById(R.id.type_add);
        mAddImg.setOnClickListener(this);
        requestDATagList();
    }

    private void requestDATagList(){
    	DATagReq request = new DATagReq(this);
    	request.registerListener(REQ_ID_TYPE, this, this);
    	request.submit(true);
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_CODE_ADDTYPE && resultCode == RESULT_OK){
			requestDATagList();
		}
	}
    
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, AddDATagActivity.class);
		startActivityForResult(intent, REQ_CODE_ADDTYPE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FeedTag tag = (FeedTag) mAdapter.getItem(position);
		Intent data = new Intent();
		data.putExtra(FEEDTAG, tag);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onSucess(int requestId, ArrayList<FeedTag> response) {
		mAdapter = new DATagAdapter(this, response);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	private static class DATagAdapter extends BaseAdapter{
		private ArrayList<FeedTag> mTagList;
		private Context mContext;
		
		
		public DATagAdapter(Context context,  ArrayList<FeedTag> tagList ){
			mContext = context;
			mTagList = (ArrayList<FeedTag>) tagList.clone();
		}
		
		@Override
		public int getCount() {
			return mTagList.size();
		}

		@Override
		public Object getItem(int position) {
			return mTagList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FeedTag tag = (FeedTag) getItem(position);
			if(tag == null){
				return null;
			}
			
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.datag_item, parent, false);
			}
			TextView tagView = (TextView) convertView.findViewById(R.id.tag_name);
			tagView.setText(tag.getName());
			return convertView;
		}
		
	}

}
