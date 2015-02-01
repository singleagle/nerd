package com.enjoy.nerd.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.DistractionProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DAProfileAdrapter extends BaseAdapter {
	
	private final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("MM-dd HH:mm");
	
	private Context mContext;
	ArrayList<DistractionProfile> mProfileList = new ArrayList<DistractionProfile>();
	
	
	public DAProfileAdrapter(Context context){
		mContext = context;
	}
	
	public void addProfileList(List<DistractionProfile> profileList, boolean clear){
		if(clear){
			mProfileList.clear();
		}
		mProfileList.addAll(profileList);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mProfileList.size();
	}

	@Override
	public Object getItem(int position) {
		
		if(position < 0 || position + 1 > mProfileList.size()){
			return null;
		}
		
		return mProfileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.daprofile_item, parent, false);
			holder = new ViewHolder();
			holder.daImgView = (ImageView) convertView.findViewById(R.id.image);
			holder.creatorNameView = (TextView) convertView.findViewById(R.id.user_name);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.contentView = (TextView)convertView.findViewById(R.id.description);  
			holder.destView = (TextView)convertView.findViewById(R.id.dest);
			holder.createTimeView = (TextView)convertView.findViewById(R.id.createtime);
			holder.startTimeView = (TextView)convertView.findViewById(R.id.starttime);
			holder.partnerView = (TextView)convertView.findViewById(R.id.partner_count);
			holder.goodCounterView = (TextView)convertView.findViewById(R.id.good_count);
			holder.distanceView = (TextView)convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		bindView(holder, mProfileList.get(position));
		return convertView;
	}
	
	private void bindView(ViewHolder holder, DistractionProfile profile){
		if(profile.getTitle() != null){
			holder.titleView.setText(profile.getTitle());
		}else if(profile.getScheme() != null){
			holder.titleView.setText(profile.getScheme());
		}else{
			holder.titleView.setText("无主题");
		}
		
		holder.creatorNameView.setText(String.valueOf(profile.getCreateUserId()));
		holder.contentView.setText(profile.getDescription());
		holder.destView.setText(profile.getDestAddress());
		holder.createTimeView.setText(TIMEFORMAT.format(new Date(profile.getCreateTime())));
		holder.startTimeView.setText(TIMEFORMAT.format(new Date(profile.getStartTime())));
		holder.partnerView.setText(String.valueOf(profile.getPartnerCount()));
		holder.goodCounterView.setText(String.valueOf(profile.getGoodCount()));
		holder.distanceView.setText(mContext.getString(R.string.distance_meters, profile.getFarawayMeters()));
	}

	private static class ViewHolder{
		ImageView daImgView;
		TextView  creatorNameView;
		TextView  titleView;
		TextView  contentView;
		TextView  destView;
		TextView  createTimeView;
		TextView  startTimeView;
		TextView  partnerView;
		TextView  goodCounterView;
		TextView  distanceView;
	}
}
