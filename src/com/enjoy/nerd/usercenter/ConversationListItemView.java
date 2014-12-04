package com.enjoy.nerd.usercenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.db.IMConversation;
import com.enjoy.nerd.db.IMMessage;
import com.enjoy.nerd.utils.EmotionUtil;
import com.enjoy.nerd.utils.FileUtil;
import com.enjoy.nerd.utils.LogWrapper;
import com.enjoy.nerd.utils.RemoteFileFetcher;
import com.enjoy.nerd.view.CircularImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
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

public class ConversationListItemView extends RelativeLayout {
	private IMConversation mIMConv;
	private TextView   mSubjectView;
	private TextView   mTimeView;
	private ImageView mHeaderImg;
	private TextView mLastMsgView;
	private ImageView mAlertIndicator;

	
	public ConversationListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHeaderImg = (ImageView) findViewById(R.id.conv_headimg);
		mSubjectView = (TextView) findViewById(R.id.conv_subject);
		mTimeView = (TextView) findViewById(R.id.conv_time);
		mLastMsgView = (TextView) findViewById(R.id.conv_content);
		mAlertIndicator = (ImageView) findViewById(R.id.conv_alert);
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
	
	public void bindIMConversation(IMConversation conv, Bitmap headerBmp, boolean forbidAlert){
		mIMConv = conv;
		
		mHeaderImg.setImageURI(Uri.parse(FileUtil.getFriendAvatarDir() + conv.getThumbAvatarName()));
		mSubjectView.setText(conv.getSubject());
		Date updateTime = new Date(conv.getLastUpdateTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		mTimeView.setText(dateFormat.format(updateTime));
		
		mLastMsgView.setText(formatMessage(conv.getLastMessage()));
		if(forbidAlert){
			mAlertIndicator.setVisibility(View.VISIBLE);;
		}else{
			mAlertIndicator.setVisibility(View.GONE);
		}
	}
	
	

}
