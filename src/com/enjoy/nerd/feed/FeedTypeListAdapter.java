package com.enjoy.nerd.feed;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.FeedTypeListReq.FeedTagGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class FeedTypeListAdapter extends BaseExpandableListAdapter{
	private ArrayList<FeedTagGroup> mGroupList;
	private Context mContext;
	
	public FeedTypeListAdapter(Context context, ArrayList<FeedTagGroup> tagGroupList){
		super();
		mContext = context;
		mGroupList = tagGroupList;
		
	}
	
	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		FeedTagGroup group = (FeedTagGroup) getGroup(groupPosition);
		if(group == null){
			return 0;
		}
		
		FeedTag[] tagList = group.getTagList();
		if(tagList == null){
			return 0;
		}
		return tagList.length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if(groupPosition >= mGroupList.size()){
			return null;
		}
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		FeedTagGroup group = (FeedTagGroup) getGroup(groupPosition);
		if(group == null){
			return null;
		}
		
		FeedTag[] tagList = group.getTagList();
		if(tagList == null || tagList.length < childPosition){
			return null;
		}
		return tagList[childPosition];
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.feedtype_group, parent, false);
		}
		TextView txView = (TextView)convertView.findViewById(R.id.group_title);
		FeedTagGroup group = (FeedTagGroup) getGroup(groupPosition);
		txView.setText(group.getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		FeedTag tag = (FeedTag) getChild(groupPosition, childPosition);
		if(tag == null){
			return null;
		}
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.feedtag_item, parent, false);
		}
		TextView tagView = (TextView) convertView.findViewById(R.id.tag_name);
		tagView.setText(tag.getName());
		return convertView;
	}


}
