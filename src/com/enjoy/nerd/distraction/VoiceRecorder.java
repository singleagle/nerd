package com.enjoy.nerd.distraction;

import java.io.File;
import java.io.IOException;

import com.enjoy.nerd.utils.FileUtil;
import com.enjoy.nerd.utils.LogWrapper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OutputFormat;

public class VoiceRecorder {
	private static final String TAG = "VoiceRecorder";
	
	private static final String SAMPLE_PREFIX = "voice_";

	public static final int NO_ERROR = 0;
	public static final int SDCARD_ACCESS_ERROR = 1;
	public static final int INTERNAL_ERROR = 2;
	public static final int IN_CALL_RECORD_ERROR = 3;


	MediaRecorder mRecorder;
	long mSampleStart = 0;       // time at which latest record or play operation started
	int mSampleLength = 0;      // length of current sample
	File mSampleFile = null;
	Context mContext;

	public VoiceRecorder(Context context){
		mContext = context;
	}

	public int startRecording(){
		stopRecording();
		mSampleLength = 0;
		if (mSampleFile == null) {
			File sampleDir = new File(FileUtil.getAppExtDir() + "/temp");

			try {
				if(!sampleDir.exists()){
					sampleDir.mkdirs();
				}
				mSampleFile = File.createTempFile(SAMPLE_PREFIX, ".amr", sampleDir);
			} catch (IOException e) {
				return SDCARD_ACCESS_ERROR;
			}
		}

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(OutputFormat.AMR_NB);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(mSampleFile.getAbsolutePath());

		// Handle IOException
		try {
			mRecorder.prepare();
		} catch(IOException exception) {
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
			return INTERNAL_ERROR;
		}
		// Handle RuntimeException if the recording couldn't start
		try {
			mRecorder.start();
		} catch (RuntimeException exception) {
			AudioManager audioMngr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
			boolean isInCall = ((audioMngr.getMode() == AudioManager.MODE_IN_CALL) ||
					(audioMngr.getMode() == AudioManager.MODE_IN_COMMUNICATION));

			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
			if (isInCall) {
				return IN_CALL_RECORD_ERROR;
			} else {
				return INTERNAL_ERROR;
			}
		}
		mSampleStart = System.currentTimeMillis();
		return NO_ERROR;
	}

	public String stopRecording() {
		if (mRecorder == null)
			return null;

		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		mSampleLength = (int)( (System.currentTimeMillis() - mSampleStart)/1000 );
		LogWrapper.i(TAG, "recoder file path:" + mSampleFile.getPath() + "file len:" + mSampleFile.length());
		return mSampleFile.getPath();
	}

	public int sampleLength() {
		return mSampleLength;
	}
}
