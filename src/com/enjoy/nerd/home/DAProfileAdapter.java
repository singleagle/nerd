package com.enjoy.nerd.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.enjoy.nerd.NerdApp;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.DistractionProfile;
import com.enjoy.nerd.remoterequest.SimpleUserInfo;
import com.enjoy.nerd.utils.BitmapCache;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DAProfileAdapter extends BaseAdapter {
	
	private final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("MM-dd HH:mm");
	
	private Context mContext;
	private ImageLoader mImageLoader;
	private ArrayList<DistractionProfile> mProfileList = new ArrayList<DistractionProfile>();
	
	
	public DAProfileAdapter(Context context){
		mContext = context;
		NerdApp app = (NerdApp)context.getApplicationContext();
		mImageLoader = app.getImageLoader();
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
			holder.userAvatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
			holder.userAvatar.setDefaultImageResId(R.drawable.default_user_header);
			holder.userAvatar.setErrorImageResId(R.drawable.default_user_header);
			
			holder.daImgView = (NetworkImageView) convertView.findViewById(R.id.photo);
			holder.daImgView.setDefaultImageResId(R.drawable.default_da_img);
			holder.daImgView.setErrorImageResId(R.drawable.default_da_img);
			
			holder.creatorNameView = (TextView) convertView.findViewById(R.id.user_name);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
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
		}else{
			holder.titleView.setText("无标题");
		}
		
		if(profile.getImageUrl() != null){
			String host = Uri.parse(profile.getImageUrl()).getHost();
			holder.daImgView.setImageUrl(profile.getImageUrl(), mImageLoader);
		}
		
		SimpleUserInfo userInfo =  profile.getCreateUserInfo();
		if(userInfo != null){
			holder.creatorNameView.setText(String.valueOf(userInfo.getName()));
			holder.userAvatar.setImageUrl(userInfo.getHeaderImgUrl(), mImageLoader);
		}
		
		holder.destView.setText(profile.getDestAddress());
		holder.createTimeView.setText(TIMEFORMAT.format(new Date(profile.getCreateTime())));
		holder.startTimeView.setText(TIMEFORMAT.format(new Date(profile.getStartTime())));
		holder.partnerView.setText(String.valueOf(profile.getPartnerCount()));
		holder.goodCounterView.setText(String.valueOf(profile.getLikeNum()));
		holder.distanceView.setText(mContext.getString(R.string.distance_meters, profile.getFarawayMeters()));
	}

	private static class ViewHolder{
		NetworkImageView userAvatar;
		NetworkImageView daImgView;
		TextView  creatorNameView;
		TextView  titleView;
		TextView  destView;
		TextView  createTimeView;
		TextView  startTimeView;
		TextView  partnerView;
		TextView  goodCounterView;
		TextView  distanceView;
	}
}
