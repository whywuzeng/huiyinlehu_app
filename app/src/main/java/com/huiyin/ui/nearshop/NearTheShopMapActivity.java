package com.huiyin.ui.nearshop;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.AppContext.LocationCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class NearTheShopMapActivity extends Activity {
	private Context mContext;
	private NearShopListBean data;
	private TextView mLeft_ib;
	private ImageButton mDitu_imageButton;
	private TextView mLocation_textview;
	private Button mRetry_location;

	private ImageButton goCenter;
	private RadioGroup tripMode;
	private int tripWay = 1;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private LocationClient mLocationClient;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_shop_map_layout);
		mContext = this;
		Intent intent = getIntent();
		data = (NearShopListBean) intent
				.getSerializableExtra("NearShopListBean");
		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;

		findView();
		setListener();
		if (lng == 0.0 || lat == 0.0) {
			StartLoacation();
		} else {
			mLocation_textview.setText("您的位置是：" + cityName);
			initView();
		}
	}

	private void findView() {
		mLeft_ib = (TextView) findViewById(R.id.left_ib);
		mDitu_imageButton = (ImageButton) findViewById(R.id.ditu_imageButton);
		mLocation_textview = (TextView) findViewById(R.id.location_textview);
		mRetry_location = (Button) findViewById(R.id.retry_location);

		goCenter = (ImageButton) findViewById(R.id.nearby_map_center);
		tripMode = (RadioGroup) findViewById(R.id.near_shop_map_trip_mode);

		mMapView = (MapView) findViewById(R.id.nearby_map_layout_bmapView);
		mBaiduMap = mMapView.getMap();
		// 打开定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 设置定位参数
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		if (lng != 0.0 && lat != 0.0) {
			// 设置定位数据
			MyLocationData locData = new MyLocationData.Builder()
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(lat).longitude(lng).build();
			mBaiduMap.setMyLocationData(locData);

			LatLng ll = new LatLng(lat, lng);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
		}
	}

	private void setListener() {
		mRetry_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StartLoacation();
			}
		});
		mLeft_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mDitu_imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		goCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lng != 0.0 && lat != 0.0) {
					// 设置定位数据
					MyLocationData locData = new MyLocationData.Builder()
							// 此处设置开发者获取到的方向信息，顺时针0-360
							.direction(100).latitude(lat).longitude(lng)
							.build();
					mBaiduMap.setMyLocationData(locData);

					LatLng ll = new LatLng(lat, lng);
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}
			}
		});
		tripMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.by_walk:
					tripWay = 1;
					break;
				case R.id.by_bus:
					tripWay = 2;
					break;
				case R.id.by_car:
					tripWay = 3;
					break;
				}
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				String name = marker.getExtraInfo().getString("name");
				UIHelper.showNearShopLineActivity(mContext,
						marker.getPosition().latitude,
						marker.getPosition().longitude,name);
				return true;
			}
		});
	}

	private void initView() {
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		ArrayList<StoreListItem> list = data.getNearbyStoreList();
		for (int i = 0; i < list.size(); i++) {
			LatLng temp = list.get(i).getLatLng();
			OverlayOptions option = new MarkerOptions().position(temp).icon(
					bitmap);
			Marker marker = null;

			// 在地图上添加Marker，并显示
			marker = (Marker) (mBaiduMap.addOverlay(option));
			Bundle bundle = new Bundle();
			bundle.putString("name", list.get(i).NAME);
			marker.setExtraInfo(bundle);
		}

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
				mLocation_textview.setText("您的位置是：" + location.getAddrStr());

				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (lng != 0.0 || lat != 0.0) {
					mLocationClient.stop();
				}
			}

		});

		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// mLocationClient.stop();
		// }
		// }, 6000);
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
