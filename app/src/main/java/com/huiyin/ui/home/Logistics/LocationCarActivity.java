package com.huiyin.ui.home.Logistics;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.AppContext.LocationCallBack;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.home.Logistics.CarLocationBean.carPosition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class LocationCarActivity extends Activity {
	private Context mContext;
	private TextView mLeft_ib;
	private TextView carLocation;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;

	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;
	private LocationClient mLocationClient;
	private int Id;
	private CarLocationBean data;
	private Handler mHandler;
	private int count  = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_location_car_layout);
		mContext = this;

		Intent intent = getIntent();
		Id = intent.getIntExtra("Id", -1);
		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;

		mHandler = new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				count++;
				if(count==2) {
					LatLng shopPosition = data.value.getLatLng();
					double distance = DistanceUtil.getDistance(new LatLng(lat, lng),
							shopPosition);
					carLocation.setText("车辆当前位置距离您"
							+ String.format("%.2f", distance / 1000) + "km");
				}
				return true;
			}
		});
		findView();
		setListener();
		initView();

		if (lng == 0.0 || lat == 0.0) {
			StartLoacation();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	private void findView() {
		mLeft_ib = (TextView) findViewById(R.id.left_ib);
		carLocation = (TextView) findViewById(R.id.car_location_info);

		mMapView = (MapView) findViewById(R.id.nearby_map_layout_bmapView);
		mBaiduMap = mMapView.getMap();
	}

	private void setListener() {
		mLeft_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initView() {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = CarLocationBean.explainJson(content, mContext);
				if (data.type > 0 && data.value != null) {
					updataView(data.value);
				}
			}

		};
		RequstClient.carLocattion(handler, Id);
	}

	protected void updataView(carPosition value) {
		LatLng shopPosition = value.getLatLng();
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);

		OverlayOptions option = new MarkerOptions().position(shopPosition)
				.icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(shopPosition);
		mBaiduMap.animateMapStatus(u);

		mHandler.sendEmptyMessage(0);
	}

	private void StartLoacation() {
		AppContext.getInstance().initLocationClient();
		if (mLocationClient == null)
			mLocationClient = AppContext.getInstance().mLocationClient;
		if (mLocationClient.isStarted()) {
			return;
		}
		mLocationClient.start();
		AppContext.getInstance().setLocationCallBack(new LocationCallBack() {

			@Override
			public void getPoistion(BDLocation location) {
				Toast.makeText(mContext, "定位成功", Toast.LENGTH_SHORT).show();
				lng = location.getLongitude();
				lat = location.getLatitude();
				cityName = location.getCity();

//				MyLocationData locData = new MyLocationData.Builder()
//						.accuracy(location.getRadius())
//						// 此处设置开发者获取到的方向信息，顺时针0-360
//						.direction(100).latitude(location.getLatitude())
//						.longitude(location.getLongitude()).build();
//				mBaiduMap.setMyLocationData(locData);
				if (lng != 0.0 || lat != 0.0) {
					mLocationClient.stop();
					mHandler.sendEmptyMessage(0);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		super.onResume();

	}

	@Override
	protected void onPause() {
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		super.onPause();

	}
	
}
