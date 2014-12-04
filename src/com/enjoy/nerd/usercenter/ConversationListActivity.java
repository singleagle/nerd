package com.enjoy.nerd.usercenter;


import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMConversation;
import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.distraction.ChatActivity;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshListView;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ConversationListActivity extends Activity implements OnRefreshListener<ListView>, OnItemClickListener {
    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private ConversationAdapter mConversationAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);
    	mPullListView = (PullToRefreshListView)findViewById(R.id.pull_list);
    	mPullListView.setOnRefreshListener(this);
    	mPullListView.setPullLoadEnabled(true);
    	mListView = mPullListView.getRefreshableView();
    	mListView.setOnItemClickListener(this);

    	Cursor cursor = getLatestConversation(-1);
    	mConversationAdapter = new ConversationAdapter(this, cursor);
    	mListView.setAdapter(mConversationAdapter);
		
	}
	
	private Cursor getLatestConversation(int num){
    	Cursor cursor = getContentResolver().query(IMConversation.Columns.CONTENT_URI, 
    			IMConversation.Columns.IMCONVERSATION_QUERY_COLUMNS, 
				null, null,
				IMConversation.Columns.DEFAULT_SORT_ORDER);
    	return cursor;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Cursor cursor = (Cursor)mConversationAdapter.getItem(position);
		int cid = cursor.getInt(IMConversation.Columns.IMCONVERSATION_ID_INDEX);
		
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra(ChatActivity.KEY_CONVERSATION_ID, cid);
		startActivity(intent);
		
	}



	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}



	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}




}
