package com.enjoy.nerd.feed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.enjoy.nerd.AccountManager;
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
import android.graphics.Matrix;
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
	
	static private final float PHOTO_WIDTH = 300.0f;
	static private final float PHOTO_HEIGHT = 300.0f;
	
	private TextView mLocationView;
	private Location mLocation;
	private GridView mTagGridView;
	private EditText mTitleView;
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
    	mTitleView = (EditText)findViewById(R.id.title);
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
            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            Matrix matrix = new Matrix();

            float scaleX = PHOTO_WIDTH / bm.getWidth();
            float scaleY = PHOTO_HEIGHT / bm.getHeight();
            float scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);
            Bitmap thumb = Bitmap.createBitmap(bm, 0, 0,
                                               bm.getWidth(),
                                               bm.getHeight(), matrix,
                                               true);
            ImageView photoView = (ImageView)findViewById(R.id.photo);
            photoView.setImageBitmap(thumb);
            photoView.setVisibility(View.VISIBLE);
            
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = MediaStore.Images.Media.query(resolver, originalUri, proj);
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

    private boolean checkInput(){
    	boolean validate = true;
    	
    	if(mLocation == null){
    		return false;
    	}
    	if(mTitleView.getText() == null || mTitleView.getText().length() == 0){
    		return  false;
    	}
    	
    	return validate;
    }
    
    private void sendAddScenicReq(){
    	if(!checkInput()){
    		 Toast.makeText(this, R.string.invalidate_input, Toast.LENGTH_LONG).show();
    		 return;
    	}
    	
    	ImageUploadReq uploadReq = null;
    	if(mImagLocalPath != null){
    		uploadReq  = new ImageUploadReq(this);
    	    uploadReq.setFile(new File(mImagLocalPath));
    	}
    	
    	AddScenicReq request = new AddScenicReq(this);
    	request.setTitle(mTitleView.getEditableText().toString());
    	request.setLocation(mLocation);
    	request.setDescription(mDescriptionView.getEditableText().toString());
    	request.setCreatUserId(AccountManager.getInstance(this).getLoginUIN());
    	
    	if(uploadReq == null){
        	request.registerListener(ADD_SCENI_ID, this, this);
        	request.submit(false);
    	}else{
    		BatchPostReqest batchReq = new BatchPostReqest(this);
    		batchReq.setRequestGroup(uploadReq, request, new PostRequestPipe(){

				public void fillPipe(Object content, PostRequest<?> postReq) {
					AddScenicReq addReq = (AddScenicReq) postReq;
					ArrayList<String> imgList =new ArrayList<String>();
					imgList.add((String) content);
					addReq.setImgUrlList(imgList);
					
				}
    			
    		});
    		batchReq.registerListener(ADD_SCENI_ID, this, this);
    		batchReq.submit(false);
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
    			finish();
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
		Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
	}
    
    
}
