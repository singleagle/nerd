package com.enjoy.nerd.distraction;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.feed.LocationPickerActivity;
import com.enjoy.nerd.remoterequest.AddDistractionReq;
import com.enjoy.nerd.remoterequest.BatchPostReqest;
import com.enjoy.nerd.remoterequest.BatchPostReqest.PostRequestPipe;
import com.enjoy.nerd.remoterequest.FeedTag;
import com.enjoy.nerd.remoterequest.ImageUploadReq;
import com.enjoy.nerd.remoterequest.Location;
import com.enjoy.nerd.remoterequest.PostRequest;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;
import com.enjoy.nerd.utils.LogWrapper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateDistractionActivity extends BaseAcitivity implements SuccessResponseListner<String>,
								View.OnClickListener, OnDateSetListener{
	static private final String TAG = "CreateDistractionActivity";
	static public final String FEED_TAG = "feedtag";
	static public final String POI_LOCATION = "location";
	
	static private final int MENU_ID_PUBLISH = 0;
	static private final int ADD_DA_ID = 1;
	
	static private final int REQ_CODE_SELECT_DATYPE = 1;
	static private final int REQ_CODE_PICKUP_PHOTO = 2;
	static private final int REQ_CODE_SELECT_LOCATION = 3;
	
	static private final float PHOTO_WIDTH = 300.0f;
	static private final float PHOTO_HEIGHT = 300.0f;
	
	private TextView mTimeView;
	private TextView mDestinationView;
	private View     mDATypeContainer;
	private TextView mDATypeView;
	private EditText mDescriptionView;
	private DatePickerDialog mDatePickerDialog;
	private FeedTag  mSelectedTag;
	private String mImagLocalPath;
	private Location mDestLocation;
	
	private final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yy-MM-dd");
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    	setContentView(R.layout.create_distraction);
    	supportInvalidateOptionsMenu();
    	mDescriptionView = (EditText)findViewById(R.id.description);
    	mDATypeContainer = findViewById(R.id.type_container);
    	mDATypeContainer.setOnClickListener(this);
    	
    	mDATypeView = (TextView)findViewById(R.id.type);
    	mSelectedTag = (FeedTag)getIntent().getParcelableExtra(FEED_TAG);
    	if(mSelectedTag != null){
    		mDATypeView.setText(mSelectedTag.getName());
    	}
    	
    	mDestLocation = (Location)getIntent().getParcelableExtra(POI_LOCATION);
    	mTimeView = (TextView)findViewById(R.id.time);
    	mTimeView.setText(DATAFORMAT.format(Calendar.getInstance().getTime()));
    	mTimeView.setOnClickListener(this);
    	mDestinationView = (TextView)findViewById(R.id.destination);
    	if(mDestLocation == null){
    		mDestinationView.setOnClickListener(this);
    	}else{
    		mDestinationView.setText(mDestLocation.getAddress());
    	}
    	findViewById(R.id.add).setOnClickListener(this);
		getActionBar().setTitle(R.string.create_distraction);
        
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
		case R.id.time:
            final Calendar calendar = Calendar.getInstance();
            if(mDatePickerDialog == null){
                mDatePickerDialog = new DatePickerDialog(
                        this,
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }else{
            	//TODO:
            	//mDatePickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            }
            mDatePickerDialog.show();
			break;
			
		case R.id.type_container:
			Intent intent = new Intent(this, DATagSelectActivity.class);
			startActivityForResult(intent, REQ_CODE_SELECT_DATYPE);
			break;
			
		case R.id.destination:
   			Intent locationIntent = new Intent(this, LocationPickerActivity.class);
			startActivityForResult(locationIntent, REQ_CODE_SELECT_LOCATION);
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
		
		if(REQ_CODE_SELECT_DATYPE == requestCode){
			mSelectedTag = data.getParcelableExtra(DATagSelectActivity.FEEDTAG);
			if(mSelectedTag != null){
				mDATypeView.setText(mSelectedTag.getName());
			}
		}else if(REQ_CODE_PICKUP_PHOTO == requestCode){
			handlePickupPhtoResult(data);
		}else if(REQ_CODE_SELECT_LOCATION == requestCode){
			mDestLocation = data.getParcelableExtra(LocationPickerActivity.POI_LOCATION);
			String poiName = data.getStringExtra(LocationPickerActivity.POI_NAME);
			mDestinationView.setText(poiName);
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar calendar =  Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
		mTimeView.setText(DATAFORMAT.format(calendar.getTime()));
	}
    
	
	
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
    	MenuItem publish = menu.add(0, MENU_ID_PUBLISH, 0, R.string.publish);
    	publish.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	return super.onCreateOptionsMenu(menu);
    }

    private void sendAddDAReq(){
    	ImageUploadReq uploadReq = null;
    	if(mImagLocalPath != null){
    		uploadReq  = new ImageUploadReq(this);
    	    uploadReq.setFile(new File(mImagLocalPath));
    	}
    	
    	AddDistractionReq request = new AddDistractionReq(this);
    	request.setDescription(mDescriptionView.getText().toString());
    	request.setPayType(AddDistractionReq.PAYTYPE_AA);
    	request.setCreatUserId(AccountManager.getInstance(this).getLoginUIN());
    	request.setType(mSelectedTag.getId());
    	
    	if(mDestLocation!= null){
    		request.setLocation(mDestLocation);
    	}
    	
    	long startTime = 0;
		try {
			startTime = DATAFORMAT.parse(mTimeView.getText().toString()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	request.setStartTime(startTime);
    	if(uploadReq == null){
        	request.registerListener(ADD_DA_ID, this, this);
        	request.submit(true);
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
    		batchReq.registerListener(ADD_DA_ID, this, this);
    		batchReq.submit(true);
    	}

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_ID_PUBLISH:
    		if(mSelectedTag == null || mDescriptionView.getText() == null){
    			Toast.makeText(this, R.string.invalidate_input, Toast.LENGTH_LONG).show();
    		}else{
    			sendAddDAReq();
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
        if(mDatePickerDialog != null){
        	mDatePickerDialog.dismiss();
        }
        super.onDestroy();
    }

	@Override
	public void onSucess(int requestId, String response) {
		Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
	}
    
    
}
