package com.enjoy.nerd.feed;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.enjoy.nerd.BaseAcitivity;
import com.enjoy.nerd.R;
import com.enjoy.nerd.utils.LogWrapper;

public class LocationPickerActivity extends BaseAcitivity implements OnMapClickListener, OnGetPoiSearchResultListener{
	static final private String TAG = "LocationPickerActivity";
	
	private Button  mSearchBtn;
	private ListView mPoiListView;
	private ArrayAdapter<String> sugAdapter = null;
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private PoiSearch mPoiSearch;
	private SuggestionSearch mSuggestionSearch;
	private MyLocationData mCurrLocation;
	
	
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SDKInitializer.initialize(getApplicationContext());
    	setContentView(R.layout.activity_location_picker);
    	mSearchBtn = (Button)findViewById(R.id.search);
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		
		mPoiListView = (ListView)findViewById(R.id.poi_list);
		sugAdapter = new ArrayAdapter<String>(this, R.layout.poiname_item,  R.id.poi_name);
		mPoiListView.setAdapter(sugAdapter);
    	
    	mMapView = (MapView) findViewById(R.id.map);
    	mMapView.showZoomControls(false);
    	mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapClickListener(this);
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.animateMapStatus(u);
		initLocation();
	}
	
	private void initLocation(){
		mBaiduMap.setMyLocationEnabled(true);
		BitmapDescriptor  locationMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_location);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, locationMarker));
		// 定位初始化
		mLocClient = new LocationClient(this);
		mMapView.setVisibility(ViewGroup.VISIBLE);
		mLocClient.registerLocationListener(new BDLocationListener(){

			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null){
					return;
				}
				LogWrapper.d(TAG, String.format("receive user location is:(%f, %f)", location.getLatitude(),location.getLongitude()));
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				mMapView.setVisibility(ViewGroup.VISIBLE);
				
				LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				
				mLocClient.registerLocationListener(null);
				mLocClient.stop(); //不再定位
			}
			
		});
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
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
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		
		super.onDestroy();
	}




	public void onMapClick(LatLng location) {
		if(mLocClient.isStarted()){
			mLocClient.stop();
		}
		MyLocationData locData = new MyLocationData.Builder()
						.latitude(location.latitude)
						.longitude(location.longitude).build();
		mBaiduMap.setMyLocationData(locData); 
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(location);
		mBaiduMap.animateMapStatus(u);
		PoiNearbySearchOption option = new PoiNearbySearchOption().location(location).radius(5000).pageCapacity(8).keyword("银行");
		mPoiSearch.searchNearby(option);
	}


	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			return;
		}
		sugAdapter.clear();
		for(PoiInfo poiInfo : result.getAllPoi()){
			if(poiInfo.address != null){
				sugAdapter.add(poiInfo.name);
			}
		}
		sugAdapter.notifyDataSetChanged();
		
	}
	
}
