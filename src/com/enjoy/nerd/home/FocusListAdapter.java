package com.enjoy.nerd.home;


import java.util.ArrayList;
import java.util.List;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.FeedTopic;
import com.enjoy.nerd.remoterequest.HotFocusListReq.FocusGroup;
import com.enjoy.nerd.remoterequest.IFocusable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class FocusListAdapter extends BaseExpandableListAdapter{
	static private final int  GROUP_TOPIC_INDEX = 0;
	static private final int GROUP_TAG_INDEX = 1;
	static private final int GROUP_COUNT = 2;
	
	private Context mContext;
	private List<FeedTopic> topicList;
	private List<FeedTag> tagList;
	
	public FocusListAdapter(Context context, FocusGroup focusGroup){
		super();
		mContext = context;
		topicList = focusGroup.getTopics();
		tagList = focusGroup.getTags();
	}
	
	@Override
	public int getGroupCount() {
		return GROUP_COUNT;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition == GROUP_TOPIC_INDEX){
			return topicList.size();
		}else if(groupPosition == GROUP_TAG_INDEX){
			return tagList.size();
		}
		return 0;
		
	}

	@Override
	public Object getGroup(int groupPosition) {
		if(groupPosition == GROUP_TOPIC_INDEX){
			return topicList;
		}else if(groupPosition == GROUP_TAG_INDEX){
			return tagList;
		}
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<?extends IFocusable> group = (List<?extends IFocusable>) getGroup(groupPosition);
		if(group == null){
			return null;
		}

		if(group.size() < childPosition){
			return null;
		}
		return group.get(childPosition);
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
		return true;
	}	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.focus_group, parent, false);
		}
		TextView txView = (TextView)convertView.findViewById(R.id.group_title);
		if( groupPosition == GROUP_TOPIC_INDEX){
			txView.setText(R.string.hot_topic);
		}else{
			txView.setText(R.string.hot_tag);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		IFocusable focus = (IFocusable) getChild(groupPosition, childPosition);
		if(focus == null){
			return null;
		}
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.focus_item, parent, false);
		}
		TextView tagView = (TextView) convertView;
		tagView.setText(focus.getName());
		return convertView;
	}


}
