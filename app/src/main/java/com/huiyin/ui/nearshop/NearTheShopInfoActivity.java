package com.huiyin.ui.nearshop;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class NearTheShopInfoActivity extends Activity {
	private Context mContext;
	private TextView mLeft_ib;
	private TextView shop_name;
	private TextView shop_phone;
	private TextView shop_address;
	private TextView shop_businessTime;
	private TextView shop_place;
	private ImageButton mDitu_imageButton;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;
	private StoreListItem data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_shop_info_layout);
		mContext = this;

		Intent intent = getIntent();
		data = (StoreListItem) intent.getSerializableExtra("StoreListItem");

		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;
		
		findView();
		setListener();
		initView();
	}

	private void findView() {
		mLeft_ib = (TextView) findViewById(R.id.left_ib);
		mDitu_imageButton = (ImageButton) findViewById(R.id.ditu_imageButton);

		shop_name = (TextView) findViewById(R.id.shop_name);
		shop_phone = (TextView) findViewById(R.id.shop_phone);
		shop_address = (TextView) findViewById(R.id.shop_address);
		shop_businessTime = (TextView) findViewById(R.id.shop_businessTime);
		shop_place = (TextView) findViewById(R.id.shop_place);

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
		mDitu_imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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
		shop_name.setText("门店名称：" + data.getNAME());
		shop_phone.setText("联系电话：" + data.getTELEPHONE());
		shop_address.setText("门店地址：" + data.getADDRESS());
		shop_businessTime.setText("营业时间：" + data.getBUSINESS());
		shop_place.setText("店铺规模：" + data.getSCALE());

		LatLng shopPosition = data.getLatLng();
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);

		OverlayOptions option = new MarkerOptions().position(shopPosition)
				.icon(bitmap);
		Marker marker = null;

		// 在地图上添加Marker，并显示
		marker = (Marker) (mBaiduMap.addOverlay(option));
		Bundle bundle = new Bundle();
		bundle.putString("name", data.NAME);
		marker.setExtraInfo(bundle);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(shopPosition);
		mBaiduMap.animateMapStatus(u);
	}
}
