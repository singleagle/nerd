package com.enjoy.nerd.feed;

import android.os.Bundle;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.Location;
import com.enjoy.nerd.utils.LogWrapper;

public class LocationViewActivity extends BaseAcitivity implements OnGetBusLineSearchResultListener{
	static final private String TAG = "LocationViewActivity";
	
	static final public  String POI_LOCATION = "location";
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private PoiSearch mPoiSearch;
	private BusLineSearch mBusLineSearch;
	private Location mTargetLocation;
	private ImageView mDestImgView;
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SDKInitializer.initialize(getApplicationContext());
    	setContentView(R.layout.activity_location_viewer);
    	mDestImgView = new ImageView(this);
    	mDestImgView.setImageResource(R.drawable.ic_location);
    	mMapView = (MapView) findViewById(R.id.map);
    	mMapView.showZoomControls(false);
    	mBaiduMap = mMapView.getMap();

		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(u);
    	mTargetLocation = getIntent().getParcelableExtra(POI_LOCATION);
		if(mTargetLocation != null){
			setTargetLocation(new LatLng( mTargetLocation.getLatitude(), mTargetLocation.getLongitude()));
		}
		mBusLineSearch = BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		initMyLocation();
	}
	
	private void initMyLocation(){
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(new BDLocationListener(){

			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null){
					if(mLocClient != null){
						mLocClient.unRegisterLocationListener(this);
					}
					return;
				}
				LogWrapper.d(TAG, String.format("receive user location is:(%f, %f)", location.getLatitude(),location.getLongitude()));
				mLocClient.unRegisterLocationListener(this);
				mLocClient.stop(); //不再定位
			}
			
		});
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if(mLocClient.isStarted()){
			mLocClient.stop();
		}
		
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		
		super.onDestroy();
	}

	
	private void setTargetLocation(LatLng location){
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(location);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.showInfoWindow(new InfoWindow(mDestImgView, location, 0));
	}


	public void onGetBusLineResult(BusLineResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			LogWrapper.e(TAG, "no busline result");
			return;
		}
		mBaiduMap.clear();
		BusLineOverlay overlay = new BusLineOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
		
	}
}
