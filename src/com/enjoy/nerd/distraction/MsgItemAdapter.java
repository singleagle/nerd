package com.enjoy.nerd.distraction;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMMessage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;


public class MsgItemAdapter extends CursorAdapter {
	
    public static final int INCOMING_ITEM_TYPE = 0;
    public static final int OUTGOING_ITEM_TYPE = 1;
	
	private Context mContext;
	private VoicePlayer mVoicePlayer;
	LayoutInflater mInflater;
	ContentObserver mContentObserver;
	
	public MsgItemAdapter(Context context, Cursor c, VoicePlayer voicePlayer) {
		super(context, c, true);
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mVoicePlayer = voicePlayer;
		
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
		int resId = isIncomingMsg(cursor) ? R.layout.msgitem_incoming : R.layout.msgitem_outgoing;
		return mInflater.inflate(resId, parent, false);
	}

	private boolean isIncomingMsg(Cursor cursor){
		IMMessage immsg = new IMMessage(cursor);
		return immsg.isIncomingMsg(AccountManager.getInstance(mContext).getLoginUIN());
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		boolean isIncoming = isIncomingMsg((Cursor)getItem(position));
		return isIncoming ? INCOMING_ITEM_TYPE : OUTGOING_ITEM_TYPE;
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		IMMessage immsg = new IMMessage(cursor);
		if(view instanceof IMMessageListItemView){
			IMMessageListItemView listItemView = (IMMessageListItemView) view;
			listItemView.bindIMMessage(immsg, mVoicePlayer, null, false);
		}
	}

	public interface ContentObserver{
		public void notifyContentChanged();
	}

	
}
