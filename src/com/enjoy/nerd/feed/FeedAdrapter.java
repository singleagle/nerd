package com.enjoy.nerd.feed;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.IFeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedAdrapter extends BaseAdapter {
	private Context mContext;
	private ImageLoader mImageLoader;
	private ArrayList<IFeed> mFeedList = new ArrayList<IFeed>();
	
	
	public FeedAdrapter(Context context){
		mContext = context;
		NerdApp app = (NerdApp)context.getApplicationContext();
	    mImageLoader = app.getImageLoader();
	}
	
	public void addFeedList(List<? extends IFeed> feedList, boolean clear){
		if(clear){
			mFeedList.clear();
		}
		mFeedList.addAll(feedList);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mFeedList.size();
	}

	@Override
	public Object getItem(int position) {
		
		if(position < 0 || position + 1 > mFeedList.size()){
			return null;
		}
		
		return mFeedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.feedprofile_item, parent, false);
			holder = new ViewHolder();
			
			holder.imgView = (NetworkImageView) convertView.findViewById(R.id.photo);
			holder.imgView.setDefaultImageResId(R.drawable.default_scenic_img);
			holder.imgView.setErrorImageResId(R.drawable.default_scenic_img);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.goodCounterView = (TextView)convertView.findViewById(R.id.like_num);
			holder.distanceView = (TextView)convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		bindView(holder, mFeedList.get(position));
		return convertView;
	}
	
	private void bindView(ViewHolder holder, IFeed feed){
		if(feed.getTitle() != null){
			holder.titleView.setText(feed.getTitle());
		}
		
		if(feed.getImageUrl() != null){
			holder.imgView.setImageUrl(feed.getImageUrl(), mImageLoader);
		}

		holder.goodCounterView.setText(String.valueOf(feed.getLikeNum()));
		holder.distanceView.setText(mContext.getString(R.string.distance_meters, feed.getFarawayMeters()));
	}

	private static class ViewHolder{
		NetworkImageView imgView;
		TextView  titleView;
		TextView  goodCounterView;
		TextView  distanceView;
	}
}
