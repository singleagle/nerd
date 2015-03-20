package com.enjoy.nerd.feed;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.enjoy.nerd.remoterequest.Location;
import com.enjoy.nerd.utils.LogWrapper;

public class LocationPickerActivity extends BaseAcitivity implements OnMapClickListener, 
				OnGetPoiSearchResultListener, OnItemClickListener{
	static final private String TAG = "LocationPickerActivity";
	
	
	static final public  String POI_LOCATION = "location";
	static final public String POI_NAME = "name";
	
	private Button  mSearchBtn;
	private ListView mPoiListView;
	private ArrayAdapter<String> mCanidateAdapter = null;
	private List<PoiInfo> mCanidatePoiInfo;
	
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
		mCanidateAdapter = new ArrayAdapter<String>(this, R.layout.poiname_item,  R.id.poi_name);
		mPoiListView.setAdapter(mCanidateAdapter);
		mPoiListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mPoiListView.setOnItemClickListener(this);
    	
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
		mLocClient = new LocationClient(getApplicationContext());
		
		mLocClient.registerLocationListener(new BDLocationListener(){

			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null){
					return;
				}
				LogWrapper.d(TAG, String.format("receive user location is:(%f, %f)", location.getLatitude(),location.getLongitude()));
				
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				relocate(ll);
				searchNearyByPoi(ll);
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
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		
		super.onDestroy();
	}

	private void searchNearyByPoi(LatLng location){
		if(location != null){
			PoiNearbySearchOption option = new PoiNearbySearchOption().location(location).radius(5000).pageCapacity(8).keyword("景点");
			mPoiSearch.searchNearby(option);
		}
	}
	
	private void relocate(LatLng location){
		MyLocationData locData = new MyLocationData.Builder()
									.latitude(location.latitude)
									.longitude(location.longitude).build();
		
		mBaiduMap.setMyLocationData(locData);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(location);
		mBaiduMap.animateMapStatus(u);
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
		mCanidateAdapter.clear();
		mCanidatePoiInfo = result.getAllPoi();
		for(PoiInfo poiInfo : mCanidatePoiInfo){
			if(poiInfo.address != null){
				mCanidateAdapter.add(poiInfo.name);
			}
		}
		mCanidateAdapter.notifyDataSetChanged();
		
	}

	private int mLastCheckedPoiPos = 0;
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(mCanidatePoiInfo == null || mCanidatePoiInfo.get(position) == null){
			return ;
		}
		PoiInfo info = mCanidatePoiInfo.get(position);
		if(mLastCheckedPoiPos == position){
			Location location = new Location().setAddress(info.address).setLatLng(info.location.longitude, info.location.latitude);
			Intent data = new Intent();
			data.putExtra(POI_LOCATION, location);
			data.putExtra(POI_NAME, info.name);
			setResult(RESULT_OK, data);
			finish();
		}else{
			mLastCheckedPoiPos = position;
			relocate(new LatLng(info.location.latitude, info.location.longitude));
			//searchNearyByPoi(info.location);
		}
		
	}
	
}
