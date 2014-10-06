package com.enjoy.nerd.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.os.AsyncTask;

public class RemoteFileFetcher {
	private static final String TAG = "RemoteFileFetcher";
	
	public static final int FETCHER_TYPE_VOICE = 1;
	public static final int FETCHER_TYPE_IMG = 2;
	public static final int FETCHER_TYPE_TMP = 3;
	
	private static final String VOICE_DIR_SEGMENT = "/voice/";
	private static final String IMG_DIR_SEGMENT = "/image/";
	private static final String TMP_DIR_SEGMENT = "/temp/";
	
	Context mContext;
	FetchListner mListner;
	String mBasePath;
	
	public RemoteFileFetcher(Context context, int fetcherType, FetchListner listner){
		mContext = context;
		mListner = listner;
		
		if(fetcherType == FETCHER_TYPE_VOICE){
			mBasePath = FileUtil.getAppExtDir() + VOICE_DIR_SEGMENT;
		}else if(fetcherType == FETCHER_TYPE_IMG){
			mBasePath = FileUtil.getAppExtDir() + IMG_DIR_SEGMENT;
		}else{
			mBasePath = FileUtil.getAppExtDir() + TMP_DIR_SEGMENT;
		}
	}
	
	public boolean isFetched(String url){
		String path = getLocalPath(url);
		File file = new File(path);
		return file.exists();
	}
	
	private String getLocalPath(String url){
		return mBasePath + FileUtil.mapToLocalFilePathSegment(url);
	}
	
	/**
	 * !!!note:该函数只能在ui线程中调用
	 * @param url
	 * @return 返回文件在本地缓存的文件路径。该文件路径只是指明取到的远程文件在本地缓存文件所在的路径，
	 * 它并不说明文件已经存在，只有在FetchListner.onFetched()回调表明成功获取后，才表示文件已经下载到本地缓存，返回的文件路径才会有文件存在；
	 */
	public String fetch(String url){
		if(url == null){
			notifyFetchedResult(url, null, false);
			return null;
		}
		
		String path = getLocalPath(url);
		File file = new File(path);
		if(file.exists()){
			notifyFetchedResult(url, path, true);
		}else{
			downloadFile(url, path);
		}
		return path;
	}
	
	private void notifyFetchedResult(String remoteUrl, String localPath, boolean success){
		if(mListner != null){
			mListner.onFetched(remoteUrl, localPath, success);
		}
	}
	
	private void downloadFile(String url, String saveLocalPath){
		DownloadTask task = new DownloadTask(this, url);
		task.execute(saveLocalPath);
	}
	
	
	private static class DownloadTask extends AsyncTask<String, Void, String>{
		private String mRemoteUrl;
		private RemoteFileFetcher mFetcher;
		
		DownloadTask(RemoteFileFetcher fetcher, String url){
			mFetcher = fetcher;
			mRemoteUrl = url;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String saveLocalPath = (String)arg0[0];
			boolean saveSucess = false;
			FileOutputStream fos = null;
			long remain = 0;
			
			HttpGet getCMD = new HttpGet(mRemoteUrl);
			HttpClient httpClient = new DefaultHttpClient();
			try {
				File file = FileUtil.creatWritableFile(saveLocalPath);
				fos = new FileOutputStream(file, false);
				HttpResponse response = httpClient.execute(getCMD);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream inputStream = entity.getContent();
						remain = entity.getContentLength();
						int readLen = 0;
						byte[] tmpBuff = new byte[2048];
						int retry = 3;
						while (remain > 0 && retry > 0) {
							readLen = inputStream.read(tmpBuff, 0, tmpBuff.length);
							if(readLen > 0){
								fos.write(tmpBuff, 0, readLen);
								remain -= readLen;
							}else{
								retry --;
								LogWrapper.e(TAG, "read file error, url=" + mRemoteUrl);
							}
						}
						
						inputStream.close();
						fos.close();
						if(remain == 0){
							saveSucess = true;
						}
					}
				}

			}catch(IOException e){
				e.printStackTrace();
				LogWrapper.e(TAG, "download error, remain data: " + remain);
			}finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(saveSucess){
				return saveLocalPath;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result != null){
				mFetcher.notifyFetchedResult(mRemoteUrl, result, true);
			}else{
				mFetcher.notifyFetchedResult(mRemoteUrl, null, false);
			}
		}

	}
	
	
	
	public interface FetchListner{
		void onFetched(String remoteUrl, String localPath, boolean success);
	}
}
