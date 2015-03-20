package com.enjoy.nerd.feed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.AddDistractionReq;
import com.enjoy.nerd.remoterequest.AddScenicReq;
import com.enjoy.nerd.remoterequest.BatchPostReqest;
import com.enjoy.nerd.remoterequest.BatchPostReqest.PostRequestPipe;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.ImageUploadReq;
import com.enjoy.nerd.remoterequest.Location;
import com.enjoy.nerd.remoterequest.PostRequest;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.utils.LogWrapper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateScenicActivity extends BaseAcitivity implements SuccessResponseListner<String>,
								View.OnClickListener{
	static private final String TAG = "CreateScenicActivity";
	
	static public final String FEED_TAG = "feedtag";
	
	static private final int MENU_ID_PUBLISH = 0;
	
	static private final int ADD_SCENI_ID = 1;
	
	static private final int REQ_CODE_SELECT_LOCATION = 1;
	static private final int REQ_CODE_PICKUP_PHOTO = 2;
	
	private TextView mLocationView;
	private Location mLocation;
	private GridView mTagGridView;
	private EditText mDescriptionView;
	private ArrayList<FeedTag>  mSelectedTagList;
	private String mImagLocalPath;
	
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    	setContentView(R.layout.create_scenic);
    	supportInvalidateOptionsMenu();
    	
    	mLocationView = (TextView)findViewById(R.id.location);
    	mLocationView.setOnClickListener(this);
    	mDescriptionView = (EditText)findViewById(R.id.description);
    	mTagGridView = (GridView)findViewById(R.id.tag_grid);
    	findViewById(R.id.add).setOnClickListener(this);
		getActionBar().setTitle(R.string.create_scenic);
        
    }
	
	private void pickupPhoto(){
		Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQ_CODE_PICKUP_PHOTO);
	}
	


	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.location:
   			Intent intent = new Intent(this, LocationPickerActivity.class);
			startActivityForResult(intent, REQ_CODE_SELECT_LOCATION);
			break;
			
		case R.id.add:
			pickupPhoto();
			break;
		default:
			break;
		}
		
	}

	protected void handlePickupPhtoResult(Intent data){
	    //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
	    ContentResolver resolver = getContentResolver();

        try {
        	Uri originalUri = data.getData();        //获得图片的uri 
            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片
            ImageView photoView = (ImageView)findViewById(R.id.photo);
            photoView.setImageBitmap(bm);
            photoView.setVisibility(View.VISIBLE);
            String[] proj = {MediaStore.Images.Media.DATA};

            //好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            mImagLocalPath = cursor.getString(column_index);
        }catch (IOException e) {
            LogWrapper.e(TAG,e.toString()); 
        }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK){
			LogWrapper.e(TAG, "onActivityResult fail:" + resultCode);
			return;
		}
		
		if(REQ_CODE_SELECT_LOCATION == requestCode){
			mLocation = data.getParcelableExtra(LocationPickerActivity.POI_LOCATION);
			String poiName = data.getStringExtra(LocationPickerActivity.POI_NAME);
			mLocationView.setText(poiName);
		}else if(REQ_CODE_PICKUP_PHOTO == requestCode){
			handlePickupPhtoResult(data);
		}
	}


	
	
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
    	MenuItem publish = menu.add(0, MENU_ID_PUBLISH, 0, R.string.publish);
    	publish.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	return super.onCreateOptionsMenu(menu);
    }

    private void sendAddScenicReq(){
    	ImageUploadReq uploadReq = null;
    	if(mImagLocalPath != null){
    		uploadReq  = new ImageUploadReq(this);
    	    uploadReq.setFile(new File(mImagLocalPath));
    	}
    	
    	AddScenicReq request = new AddScenicReq(this);
    	if(uploadReq == null){
        	request.registerListener(ADD_SCENI_ID, this, this);
        	request.submit();
    	}else{
    		BatchPostReqest batchReq = new BatchPostReqest(this);
    		batchReq.setRequestGroup(uploadReq, request, new PostRequestPipe(){

				public void fillPipe(Object content, PostRequest<?> postReq) {
					AddDistractionReq addReq = (AddDistractionReq) postReq;
					ArrayList<String> imgList =new ArrayList<String>();
					imgList.add((String) content);
					addReq.setImgUrlList(imgList);
					
				}
    			
    		});
    		batchReq.registerListener(ADD_SCENI_ID, this, this);
    		batchReq.submit();
    	}

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_ID_PUBLISH:
    		if( mDescriptionView.getText() == null){
    			Toast.makeText(this, R.string.invalidate_input, Toast.LENGTH_LONG).show();
    		}else{
    			sendAddScenicReq();
    		}
    		
    		break;
    	
    	default:
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    
	@Override
    public void onDestroy() {
        super.onDestroy();
    }

	@Override
	public void onSucess(int requestId, String response) {
		Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
	}
    
    
}
