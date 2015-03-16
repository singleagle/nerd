package com.enjoy.nerd.usercenter;

import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMConversation;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;


public class ConversationAdapter extends CursorAdapter {
	
    public static final int INCOMING_ITEM_TYPE = 0;
    public static final int OUTGOING_ITEM_TYPE = 1;
	
	private Context mContext;
	LayoutInflater mInflater;
	ContentObserver mContentObserver;
	
	public ConversationAdapter(Context context, Cursor c) {
		super(context, c, true);
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		
	}
	
	public void registerContentObserver(ContentObserver observer){
		mContentObserver = observer;
	}
	
	@Override
	protected void onContentChanged() {
		super.onContentChanged();
		if(mContentObserver != null){
			mContentObserver.notifyContentChanged();
		}
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.convasation_item, parent, false);
	}

	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		IMConversation conv = new IMConversation(cursor);
		if(view instanceof ConversationListItemView){
			ConversationListItemView listItemView = (ConversationListItemView) view;
			listItemView.bindIMConversation(conv, null, false);
		}
	}

	public interface ContentObserver{
		public void notifyContentChanged();
	}

	
}
