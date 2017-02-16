package com.huiyin.ui.nearshop;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.AppContext.LocationCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NearShopLineActivity extends Activity implements
		OnGetRoutePlanResultListener {
	private Context mContext;
	private TextView mLeft_ib;
	private ImageButton mDitu_imageButton;

	private TextView lineStart, lineEnd;
	private TextView linesAddto;
	private ImageButton goCenter;
	private RadioGroup tripMode;
	private int tripWay = 1;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private LocationClient mLocationClient;
	private RoutePlanSearch mSearch = null;
	private OverlayManager routeOverlay = null;
	private boolean showOverlay = false;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;

	/** 终点经度 */
	private double endLng = 0.0;
	/** 终点纬度 */
	private double endLat = 0.0;
	
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_shop_map_line_layout);
		mContext = this;
		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;

		Intent intent = getIntent();
		endLng = intent.getDoubleExtra("lng", 0.0);
		endLat = intent.getDoubleExtra("lat", 0.0);
		tripWay = intent.getIntExtra("tripWay", 1);
		name = intent.getStringExtra("endPoint");
		
		findView();
		setListener();
		if (lng == 0.0 || lat == 0.0) {
			StartLoacation();
		} else {
			initView();
		}
	}

	private void findView() {
		mLeft_ib = (TextView) findViewById(R.id.left_ib);
		mDitu_imageButton = (ImageButton) findViewById(R.id.ditu_imageButton);

		goCenter = (ImageButton) findViewById(R.id.nearby_map_center);
		tripMode = (RadioGroup) findViewById(R.id.near_shop_map_trip_mode);

		lineStart = (TextView) findViewById(R.id.line_start);
		lineEnd = (TextView) findViewById(R.id.line_end);
		linesAddto = (TextView) findViewById(R.id.lines_add);
		lineEnd.setText("终点："+name);
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
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	private void setListener() {
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
				PlanNode stNode = PlanNode.withLocation(new LatLng(lat, lng));
				PlanNode enNode = PlanNode.withLocation(new LatLng(endLat,
						endLng));
				switch (checkedId) {
				case R.id.by_walk:
					if (tripWay != 1) {
						if (routeOverlay != null || showOverlay) {
							routeOverlay.removeFromMap();
							showOverlay = false;
						}
						mSearch.walkingSearch((new WalkingRoutePlanOption())
								.from(stNode).to(enNode));
					}
					break;
				case R.id.by_bus:
					if (tripWay != 2) {
						if (routeOverlay != null || showOverlay) {
							routeOverlay.removeFromMap();
							showOverlay = false;
						}
						mSearch.transitSearch((new TransitRoutePlanOption())
								.from(stNode).city(cityName).to(enNode));
					}
					break;
				case R.id.by_car:
					if (tripWay != 3) {
						if (routeOverlay != null || showOverlay) {
							routeOverlay.removeFromMap();
							showOverlay = false;
						}
						mSearch.drivingSearch((new DrivingRoutePlanOption())
								.from(stNode).to(enNode));
					}
					break;
				}
			}
		});

	}

	private void initView() {
		PlanNode stNode = PlanNode.withLocation(new LatLng(lat, lng));
		PlanNode enNode = PlanNode.withLocation(new LatLng(endLat, endLng));

		mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
				enNode));
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

				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (lng != 0.0 || lat != 0.0) {
					mLocationClient.stop();
				}
				initView();
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

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			
			List<DrivingStep> temp = result.getRouteLines().get(0).getAllStep();
			String line = "";
			for (int i = 0; i < temp.size(); i++) {
				line += i + 1 + ".";
				line += temp.get(i).getInstructions();
				if (i != temp.size() - 1) {
					line += "<br />";
				}
			}
			linesAddto.setText(Html.fromHtml(line));
			
			showOverlay = true;
			tripWay = 3;
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			
			List<TransitStep> temp = result.getRouteLines().get(0).getAllStep();
			String line = "";
			for (int i = 0; i < temp.size(); i++) {
				line += i + 1 + ".";
				line += temp.get(i).getInstructions();
				if (i != temp.size() - 1) {
					line += "<br />";
				}
			}
			linesAddto.setText(Html.fromHtml(line));
			
			showOverlay = true;
			tripWay = 2;
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			
			List<WalkingStep> temp = result.getRouteLines().get(0).getAllStep();
			String line = "";
			for (int i = 0; i < temp.size(); i++) {
				line += i + 1 + ".";
				line += temp.get(i).getInstructions();
				if (i != temp.size() - 1) {
					line += "<br />";
				}
			}
			linesAddto.setText(Html.fromHtml(line));
			
			showOverlay = true;
			tripWay = 1;
		}
	}
}
