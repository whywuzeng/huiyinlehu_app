package com.huiyin.ui.nearshop;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.huiyin.AppContext;
import com.huiyin.AppContext.LocationCallBack;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.db.SQLOpearteImpl;
import com.huiyin.ui.nearshop.adapter.NearShopAdapter;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class NearTheShopActivityNew extends Activity implements
		IXListViewListener {
	private Context mContext;
	private RelativeLayout mCommon_title_top;
	private TextView mLeft_ib;
	private TextView mMiddle_title_tv;
	private ImageButton mDitu_imageButton;
	private LinearLayout mNear_shop_buttom;
	private TextView mLocation_textview;
	private Button mRetry_location;
	private XListView mXlistview;
	private NearShopAdapter mAdapter;
	private NearShopListBean data;
	/** 当前页 */
	private int mPageindex;

	private LocationClient mLocationClient;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;
	private int cityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_shop_layout);
		mContext = this;
		findView();
		setListener();
		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;
		if (lng == 0.0 || lat == 0.0) {
			StartLoacation();
		} else {
			SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
			cityId = temp.checkIdByName(cityName);
			temp.CloseDB();
			mLocation_textview.setText("您的位置是：" + cityName);
			initData();
		}
	}

	private void findView() {
		mCommon_title_top = (RelativeLayout) findViewById(R.id.common_title_top);
		mLeft_ib = (TextView) findViewById(R.id.left_ib);
		mMiddle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		mDitu_imageButton = (ImageButton) findViewById(R.id.ditu_imageButton);
		mNear_shop_buttom = (LinearLayout) findViewById(R.id.near_shop_buttom);
		mLocation_textview = (TextView) findViewById(R.id.location_textview);
		mRetry_location = (Button) findViewById(R.id.retry_location);
		mXlistview = (XListView) findViewById(R.id.xlistview);

		mXlistview.setFooterDividersEnabled(false);
		mXlistview.setPullLoadEnable(false);
		mXlistview.setPullRefreshEnable(true);
		mXlistview.setXListViewListener(this);

		mAdapter = new NearShopAdapter(mContext);
		mXlistview.setAdapter(mAdapter);
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
		mXlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UIHelper.showNearTheShopInfoActivity(mContext,
						(StoreListItem) mAdapter.getItem(position - 1));
			}
		});
		mDitu_imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.setNearbyStoreList(mAdapter.getList());
				UIHelper.showNearTheShopMapActivity(mContext, data);
			}
		});
	}

	private void initData() {
		laodPageData(1);
	}

	private void laodPageData(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = NearShopListBean.explainJson(content, mContext);
				if (data.type > 0 && data.nearbyStoreList != null
						&& data.nearbyStoreList.size() > 0) {
					if (mPageindex == 1) {
						mAdapter.deleteItem();
					}
					mAdapter.addItem(data.nearbyStoreList, new LatLng(lat, lng));
					if (data.nearbyStoreList.size() >= 10) {
						mXlistview.setPullLoadEnable(true);
					} else {
						mXlistview.setPullLoadEnable(false);
					}
				} else {
					mXlistview.setPullLoadEnable(false);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				endUpData();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				endUpData();
			}

		};
		RequstClient.appNearbyStore(cityId,cityName.replaceAll("市", ""), mPageindex,
				lng, lat, handler);
		Log.i("", "====" + cityName.replaceAll("市", "") + "|" + mPageindex);
	}

	@SuppressLint("SimpleDateFormat")
	private void endUpData() {
		mXlistview.stopLoadMore(1);
		mXlistview.stopRefresh();
		mXlistview.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
				if (lng != 0.0 && lat != 0.0) {
					mLocationClient.stop();
					SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
					cityId = temp.checkIdByName(cityName);
					temp.CloseDB();
					initData();
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		if (lng != 0.0 && lat != 0.0) {
			initData();
		} else {
			StartLoacation();
		}
	}

	@Override
	public void onLoadMore() {
		laodPageData(2);
	}
}
