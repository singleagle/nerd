package com.enjoy.nerd.distraction;


import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.distraction.MsgItemAdapter.ContentObserver;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.remoterequest.xmpp.IMMessageSendReq;
import com.enjoy.nerd.usercenter.AccountManager;
import com.enjoy.nerd.view.PullToRefreshBase;
import com.enjoy.nerd.view.PullToRefreshBase.OnRefreshListener;
import com.enjoy.nerd.view.PullToRefreshListView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnRefreshListener<ListView>,
								View.OnClickListener, ContentObserver{
	private static final int REQID_SEND_CHAT = 1;
	
	long mDACreator;
	private ImageView emotionView;
	private ImageView addView;
	private ImageView chatModeSwitcher;	
	private EditText chatTextView;
	private Button sendButton;
	private ListView mChatListView;
	private MsgItemAdapter mMsgItemAdapter;
	private String mChatTag;
	private VoicePlayer mVoicePlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mDACreator = getIntent().getLongExtra(DistractionDetailActivity.KEY_DA_CREATOR_UID, 0);
		mVoicePlayer = new VoicePlayer(this);
		initView();
	}
	
	private Cursor getIMMessageCursor(){
		String owner = AccountManager.getInstance(this).getLastLoginUIN();
		return getContentResolver().query(IMMessage.Columns.CONTENT_URI, IMMessage.Columns.IMMESSAGE_QUERY_COLUMNS, 
				IMMessage.Columns.WHERE_GROUP, new String []{owner, String.valueOf(mDACreator)}, IMMessage.Columns.DEFAULT_SORT_ORDER);
		
	}
	
	private void initView(){
		chatTextView = (EditText) findViewById(R.id.chat_edit_text);
		chatTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String contentStr = chatTextView.getText().toString();
				if(contentStr.length() > 0){
					sendButton.setVisibility(View.VISIBLE);
				}else {
					sendButton.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		sendButton = (Button) findViewById(R.id.chat_send_btn);
		sendButton.setOnClickListener(this);
		emotionView = (ImageView) findViewById(R.id.chat_emotion_btn);
		addView = (ImageView) findViewById(R.id.chat_btn_add);
		PullToRefreshListView pullListView = (PullToRefreshListView)findViewById(R.id.chat_pull_list);
		pullListView.setOnRefreshListener(this);
		pullListView.setPullLoadEnabled(true);
		mChatListView = pullListView.getRefreshableView();
		Cursor cursor = getIMMessageCursor();
		mMsgItemAdapter = new MsgItemAdapter(this, cursor, mVoicePlayer);
		mMsgItemAdapter.registerContentObserver(this);
		mChatListView.setAdapter(mMsgItemAdapter);
		mChatListView.setStackFromBottom(true);
		chatModeSwitcher = (ImageView) findViewById(R.id.chat_switch_btn);

	}

	@Override
	protected void onDestroy() {
		mVoicePlayer.stoplay();
		super.onDestroy();
	}


	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
	}


	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		
	}

	private void sendChat(String content){
		IMMessageSendReq sendReq = new IMMessageSendReq(this);
		sendReq.setUid(String.valueOf(mDACreator));
		sendReq.setTextContent(content);
		sendReq.setChatTag(mChatTag);
		sendReq.setCreateTime(System.currentTimeMillis());
		IMMessage imMsg = new IMMessage(sendReq, AccountManager.getInstance(this).getLoginUIN());
		ContentValues contentValues = imMsg.createContentValues();
		Uri contentUri = getContentResolver().insert(IMMessage.Columns.CONTENT_URI, contentValues);
		ChatMsgSendResultListner listner = new ChatMsgSendResultListner(contentUri);
		sendReq.registerListener(REQID_SEND_CHAT, listner, listner);
		sendReq.submit();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.chat_send_btn:
			sendChat(chatTextView.getText().toString());
			chatTextView.setText(null);
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void notifyContentChanged() {
		mMsgItemAdapter.swapCursor(getIMMessageCursor());
	}
	

	private class ChatMsgSendResultListner implements SuccessResponseListner<String>, FailResponseListner{
		Uri mIMMessageUri;
		
		public ChatMsgSendResultListner(Uri messageUri){
			mIMMessageUri = messageUri;
		}
		
		@Override
		public void onFailure(int requestId, int error, int subErr,
				String errDescription) {
			ContentValues values = new ContentValues(1);
			values.put(IMMessage.Columns.SEND_STATE, IMMessage.SEND_STATE_FAILURE);
			ChatActivity.this.getContentResolver().update(mIMMessageUri, values, null, null);
		}
	
	
		@Override
		public void onSucess(int requestId, String response) {
			ContentValues values = new ContentValues(1);
			values.put(IMMessage.Columns.SEND_STATE, IMMessage.SEND_STATE_SUCCESS);
			ChatActivity.this.getContentResolver().update(mIMMessageUri, values, null, null);
		}
	}


}
