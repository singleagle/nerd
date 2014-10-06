package com.enjoy.nerd.distraction;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class VoicePlayer implements OnCompletionListener {
	
	public static final int NO_ERROR = 0;
	public static final int INTERNAL_ERROR = 2;
	
	protected Context mContext;
	
	MediaPlayer mPlayer;
	boolean isStart = false;
	String filePath;
	private OnCompletionListener mCompletionListener;
	
	public VoicePlayer(Context context){
		mContext = context;
	}
	
	public int startPlay(String filePath, OnCompletionListener completionListener ){
		mPlayer = new MediaPlayer();
		
		try {
			mPlayer.setDataSource(filePath);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(this);
			mCompletionListener = completionListener;
			this.filePath = filePath;
			isStart = true;
		} catch (IOException e) {
			mPlayer.release();
			mPlayer = null;
			isStart = false;
			return INTERNAL_ERROR;
		} catch (RuntimeException e) {
			mPlayer.release();
			mPlayer = null;
			isStart = false;
			return INTERNAL_ERROR;
		}
		return NO_ERROR;
	}

	public int stoplay(){
		if(mPlayer != null){
			try {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
				isStart = false;
			} catch (IllegalStateException e) {
				mPlayer.release();
				mPlayer = null;
				isStart = false;
			}
		}
		return NO_ERROR;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		isStart = false;
		try {
			if(mCompletionListener != null){
				mCompletionListener.onCompletion(mp);
			}
			mp.release();
			mPlayer = null;
		} catch (Exception e) {
			
		}
	}
	
	public boolean isPlaying(){
		return isStart;
	}

	public String getFilePath() {
		return filePath;
	}
	
}
