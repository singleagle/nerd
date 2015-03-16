package com.enjoy.nerd.distraction;

import java.util.regex.Matcher;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.utils.EmotionUtil;
import com.enjoy.nerd.utils.LogWrapper;
import com.enjoy.nerd.utils.RemoteFileFetcher;
import com.enjoy.nerd.view.CircularImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IMMessageListItemView extends RelativeLayout {
	private IMMessage mIMMessage;
	private RemoteFileFetcher mImageFetcher;
	private RemoteFileFetcher mVoiceFetcher;
	private VoicePlayer mVoicePlayer;
	private TextView   mMsgCreateTimeView;
	private CircularImageView mHeaderImg;
	private FrameLayout mContentContainer;
	private ImageView mPicContentView;
	private ImageView mVoiceContentView;
	private TextView mTextContentView;
	private ProgressBar mProgressBar;
	private ImageView mStateIndicator;
	
	private VoiceViewClickListener mVoiceClickListener = new VoiceViewClickListener(); 
	
	public IMMessageListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mMsgCreateTimeView = (TextView) findViewById(R.id.msgitem_time);
		mHeaderImg = (CircularImageView) findViewById(R.id.msgitem_headimg);
		mContentContainer = (FrameLayout) findViewById(R.id.msgitem_content_container);
		mTextContentView = (TextView) findViewById(R.id.msgitem_textcontent);
		mPicContentView = (ImageView) findViewById(R.id.msgitem_piccontent);
		mVoiceContentView = (ImageView) findViewById(R.id.msgitem_voicecontent);
		mStateIndicator = (ImageView) findViewById(R.id.msgitem_state_indicator);
		mProgressBar = (ProgressBar)findViewById(R.id.msgitem_progressbar); 
	}


	private CharSequence formatMessage(String content){
		if(content == null){
			return " ";
		}
		Matcher matcher = EmotionUtil.pattern.matcher(content);
		SpannableString spannableString = new SpannableString(content);
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			String subStr = content.substring(start, end);
			Bitmap bitmap = EmotionUtil.getBitmapFromAssets(getContext().getAssets(), subStr);
			if(bitmap != null){ 
				ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
				spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}
	
	public void bindIMMessage(IMMessage message, VoicePlayer voicePlayer, Bitmap headerBmp, boolean needShowTime){
		mIMMessage = message;
		mVoicePlayer = voicePlayer;
		if(headerBmp != null){
			mHeaderImg.setImageBitmap(headerBmp);
		}else{
			mHeaderImg.setImageResource(R.drawable.default_user_header);
		}
		
		if(needShowTime){
			mMsgCreateTimeView.setVisibility(View.VISIBLE);
		}else{
			mMsgCreateTimeView.setVisibility(View.GONE);
		}
		
		if(message.getType() == IMMessage.TYPE_TEXT){
			mTextContentView.setVisibility(View.VISIBLE);
			mVoiceContentView.setVisibility(View.GONE);
			mPicContentView.setVisibility(View.GONE);
			mTextContentView.setText(formatMessage(message.getContent()));
		}else if(message.getType() == IMMessage.TYPE_PIC){
			mTextContentView.setVisibility(View.GONE);
			mVoiceContentView.setVisibility(View.GONE);
			mPicContentView.setVisibility(View.VISIBLE);
		}else if(message.getType() == IMMessage.TYPE_TEXT){
			mTextContentView.setVisibility(View.GONE);
			mVoiceContentView.setVisibility(View.VISIBLE);
			mPicContentView.setVisibility(View.GONE);
		}else{
			mTextContentView.setVisibility(View.GONE);
			mVoiceContentView.setVisibility(View.GONE);
			mPicContentView.setVisibility(View.GONE);
		}
		
		if(message.getSendState() == IMMessage.SEND_STATE_FAILURE){
			mStateIndicator.setVisibility(View.VISIBLE);
			mStateIndicator.setImageResource(R.drawable.msg_state_fail);
			mProgressBar.setVisibility(View.GONE);
		}else{
			mStateIndicator.setVisibility(View.GONE);
			if(message.getSendState() == IMMessage.SEND_STATE_SENDING){
				mProgressBar.setVisibility(View.VISIBLE);
			}else{
				mProgressBar.setVisibility(View.GONE);
			}
		}
		
	}
	
	
	private void setVoiceImgState(ImageView voiceImg, boolean playing){
		boolean incoming = mIMMessage.isIncomingMsg(AccountManager.getInstance(getContext()).getLoginUIN());
		if(playing){
			if(incoming){
				voiceImg.setImageResource(R.drawable.right_speaker);
			}else{
				voiceImg.setImageResource(R.drawable.left_speaker);
			}
			AnimationDrawable anim = (AnimationDrawable) voiceImg.getDrawable();
			anim.start();
		}else{
			if(incoming){
				voiceImg.setImageResource(R.drawable.icon_voice_right);
			}else{
				voiceImg.setImageResource(R.drawable.icon_voice_left);
			}
		}
	}
	
	private class VoiceViewClickListener implements OnClickListener, OnCompletionListener {
		private ImageView mPlayingImg;
		
		@Override
		public void onClick(View v) {
			ImageView voiceImg = (ImageView) v;
			if(mPlayingImg != null){
				setVoiceImgState(mPlayingImg, false);
				mVoicePlayer.stoplay();
			}
			
			if(mPlayingImg != voiceImg){
				String path = mVoiceFetcher.fetch(voiceImg.getTag().toString());
				mVoicePlayer.startPlay(path, this);
				setVoiceImgState(voiceImg, true);
				mPlayingImg = voiceImg;
			}else{
				mPlayingImg = null;
			}
			
		}
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			setVoiceImgState(mPlayingImg, false);
			mPlayingImg = null;
		}
	};
	
}
