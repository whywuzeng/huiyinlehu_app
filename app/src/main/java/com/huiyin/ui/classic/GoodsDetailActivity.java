package com.huiyin.ui.classic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BrowseItem;
import com.huiyin.bean.GoodsDetailBeen;
import com.huiyin.db.ScanRecordDao;
import com.huiyin.ui.IndexViewPager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.YtShare;
import com.huiyin.wight.rongcloud.RongCloud;

public class GoodsDetailActivity extends BaseGoodsDetailActivity implements
		OnClickListener {

	private static final String TAG = "GoodsDetail2Activity";

	/** 商品详情的key */
	public static final String BUNDLE_KEY_GOODS_ID = "goods_detail_id";
	
	/** 预约key */
	public static final String BUNDLE_KEY_subscribe_ID = "subscribeId";
	/** 商品的id */
	public String goodsId;

	public GoodsDetailBeen gdbbean;

	// 标签卡
	private TextView tab_baseinfo, tab_image_text, tab_parameter,
			tab_packaging, tab_service;

	// 界面
	private ArrayList<Fragment> mFragmentList;
	private FragmentManager mFragmentManager;
	protected IndexViewPager mViewPager;

	private BasicInformationFragment basicInformationFragment;// 基本信息
	private GraphicInformationFragment graphicInformationFragment;// 图文信息
	private SpecificationsFragment specificationsFragment;// 参数规格
	private PackingListFragment packingListFragment;// 包装信息
	private SaleServiceFragment saleServiceFragment;// 售后服务

	public static int showMenuType = 0;// 0加入购物车过来,1结算过来的
	// purchase 立即结算2，加入购物车1，组合购买3

	private ScanRecordDao scanRecordDao;

	private Context mContext;
	
	private int subscribeId;//预约ID
	
	private int listPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_goods_detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.actionbar_common_back_btn2);

		setSlidingActionBarEnabled(false);// 标题栏不移动 //左右两侧slidingmenu的fragment是否显示标题栏

		goodsId = getIntent().getStringExtra(BUNDLE_KEY_GOODS_ID);

		subscribeId = getIntent().getIntExtra(BUNDLE_KEY_subscribe_ID, 0);
		
		listPosition = getIntent().getIntExtra("position", -1);
		
		mContext = this;
		
		initView();

		initData();

		goodsDetailsHttp();

	}

	private void initView() {
		// 标题
		TextView titleText = (TextView) findViewById(R.id.ab_title);
		titleText.setText("商品详情");

		// 返回按钮
		TextView btnBack = (TextView) findViewById(R.id.ab_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		// 右边按钮
		ImageView ab_right_btn1 = (ImageView) findViewById(R.id.ab_right_btn1);
		ImageView ab_right_btn2 = (ImageView) findViewById(R.id.ab_right_btn2);
		ab_right_btn1.setImageResource(R.drawable.ab_ic_share);
		//设置隐藏
		ab_right_btn1.setVisibility(View.GONE);
		
		ab_right_btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				YtShare.createInstance(GoodsDetailActivity.this).share();
			}
		});
		ab_right_btn2.setVisibility(View.GONE);
		ab_right_btn2.setImageResource(R.drawable.ab_ic_customer);
		ab_right_btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				RongCloud.getInstance(GoodsDetailActivity.this)
//						.startCustomerServiceChat();
			}
		});

		tab_baseinfo = (TextView) findViewById(R.id.tab_baseinfo);
		tab_image_text = (TextView) findViewById(R.id.tab_image_text);
		tab_parameter = (TextView) findViewById(R.id.tab_parameter);
		tab_packaging = (TextView) findViewById(R.id.tab_packaging);
		tab_service = (TextView) findViewById(R.id.tab_service);

		tab_baseinfo.setOnClickListener(this);
		tab_image_text.setOnClickListener(this);
		tab_parameter.setOnClickListener(this);
		tab_packaging.setOnClickListener(this);
		tab_service.setOnClickListener(this);

		mFragmentManager = getSupportFragmentManager();
		mFragmentList = new ArrayList<Fragment>();

		basicInformationFragment = new BasicInformationFragment();
		graphicInformationFragment = new GraphicInformationFragment();
		specificationsFragment = new SpecificationsFragment();
		packingListFragment = new PackingListFragment();
		saleServiceFragment = new SaleServiceFragment();

		mFragmentList.add(basicInformationFragment);
		mFragmentList.add(graphicInformationFragment);
		mFragmentList.add(specificationsFragment);
		mFragmentList.add(packingListFragment);
		mFragmentList.add(saleServiceFragment);

		mViewPager = (IndexViewPager) findViewById(R.id.mViewPager);
		mViewPager.setAdapter(new FragmentViewPagerAdapter(mFragmentManager,
				mFragmentList));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				setTabIndex(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mViewPager.setCurrentItem(0);
		setTabIndex(0);
	}

	private void initData() {

		scanRecordDao = new ScanRecordDao();

	}

	/**
	 * 获取商品数据
	 * 
	 * **/
	public void goodsDetailsHttp() {
		RequstClient.goodsDetailsHttpNew(AppContext.getInstance().getUserId(), goodsId,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						boolean isSuccess = true;
						try {
							JSONObject obj = new JSONObject(content);
							String result = obj.getString("type");
							if ("1".equals(result)) {
								// 请求成功
								gdbbean = new Gson().fromJson(content,GoodsDetailBeen.class);
								LogUtil.i(TAG, "GoodsDetail:" + content);
								savaScanRecord();
								isSuccess = true;
							} else {
								isSuccess = false;
								Toast.makeText(getApplicationContext(),
										obj.getString("msg"), Toast.LENGTH_LONG)
										.show();
							}
						} catch (Exception e) {
							isSuccess = false;
						}

						if (isSuccess) {
							setView();

							menuFragment.setData(gdbbean);
//							groupFragment.setData(gdbbean);
						}
					}
				});
//		RequstClient.goodsDetailsHttp(AppContext.userId, goodsId,
//				new CustomResponseHandler(this) {
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							String content) {
//						super.onSuccess(statusCode, headers, content);
//						boolean isSuccess = true;
//						try {
//							JSONObject obj = new JSONObject(content);
//							String result = obj.getString("type");
//							if ("1".equals(result)) {
//								// 请求成功
//								gdbbean = new Gson().fromJson(content,GoodsDetailBeen.class);
//								LogUtil.i(TAG, "GoodsDetail:" + content);
//								savaScanRecord();
//								isSuccess = true;
//							} else {
//								isSuccess = false;
//								Toast.makeText(getApplicationContext(),
//										obj.getString("msg"), Toast.LENGTH_LONG)
//										.show();
//							}
//						} catch (Exception e) {
//							isSuccess = false;
//						}
//
//						if (isSuccess) {
//							setView();
//
//							menuFragment.setData(gdbbean);
//							groupFragment.setData(gdbbean);
//						}
//					}
//				});
	}

	private void setView() {

		String url_imgeAndText = gdbbean.commodity.commodity.DETAILS_INTRODUCTION;// 图文详情
		String url_guige = gdbbean.commodity.commodity.SPECIFICATIONS;// 规格参数
		String url_baozhuang = gdbbean.commodity.commodity.ONLINE_DEMO;// 包装清单
		String url_shouhou = gdbbean.commodity.commodity.INSTALLATION_DIAGRAM;// 售后服务

		if (basicInformationFragment != null && url_imgeAndText != null) {
			basicInformationFragment.setData(gdbbean,subscribeId);
		}

		if (graphicInformationFragment != null && url_imgeAndText != null) {
			graphicInformationFragment.loadWebData(url_imgeAndText);
		}

		if (specificationsFragment != null && url_guige != null) {
			specificationsFragment.loadWebData(url_guige);
		}

		if (packingListFragment != null && url_baozhuang != null) {
			packingListFragment.loadWebData(url_baozhuang);
		}

		if (saleServiceFragment != null && url_shouhou != null) {
			saleServiceFragment.loadWebData(url_shouhou);
		}

	}

	@Override
	public void onClick(View view) {
		if (view == tab_baseinfo) {
			mViewPager.setCurrentItem(0);
		} else if (view == tab_image_text) {
			mViewPager.setCurrentItem(1);
		} else if (view == tab_parameter) {
			mViewPager.setCurrentItem(2);
		} else if (view == tab_packaging) {
			mViewPager.setCurrentItem(3);
		} else if (view == tab_service) {
			mViewPager.setCurrentItem(4);
		}
	}

	private void setTabIndex(int index) {
		switch (index) {
		case 0:
			tab_baseinfo.setSelected(true);
			tab_image_text.setSelected(false);
			tab_parameter.setSelected(false);
			tab_packaging.setSelected(false);
			tab_service.setSelected(false);
			break;
		case 1:
			tab_baseinfo.setSelected(false);
			tab_image_text.setSelected(true);
			tab_parameter.setSelected(false);
			tab_packaging.setSelected(false);
			tab_service.setSelected(false);
			break;
		case 2:
			tab_baseinfo.setSelected(false);
			tab_image_text.setSelected(false);
			tab_parameter.setSelected(true);
			tab_packaging.setSelected(false);
			tab_service.setSelected(false);
			break;
		case 3:
			tab_baseinfo.setSelected(false);
			tab_image_text.setSelected(false);
			tab_parameter.setSelected(false);
			tab_packaging.setSelected(true);
			tab_service.setSelected(false);
			break;
		case 4:
			tab_baseinfo.setSelected(false);
			tab_image_text.setSelected(false);
			tab_parameter.setSelected(false);
			tab_packaging.setSelected(false);
			tab_service.setSelected(true);
			break;
		}
	}

	private void savaScanRecord() {
		BrowseItem record = new BrowseItem();
		record.COMMODITY_ID = gdbbean.commodity.commodity.COMMODITY_ID;
		record.PRICE = gdbbean.commodity.commodity.PRICE;
		record.COMMODITY_NAME = gdbbean.commodity.commodity.COMMODITY_NAME;
		String imageUrl = gdbbean.commodity.commodity.COMMODITY_IMAGE_LIST;
		String url[] = null;
		if (imageUrl != null) {
			url = imageUrl.split(",");
		}
		if (url.length > 0) {
			record.COMMODITY_IMAGE_PATH = url[0];
		} else {
			record.COMMODITY_IMAGE_PATH = "";
		}
		List<BrowseItem> lists = scanRecordDao.fetcheAll();

		if (lists.size() >= 30) {
			BrowseItem db = scanRecordDao.fetcheById(record.COMMODITY_ID);
			if (db != null) {
				scanRecordDao.delete(db);
			} else {
				scanRecordDao.delete(lists.get(lists.size() - 1));
			}
		} else {
			BrowseItem db = scanRecordDao.fetcheById(record.COMMODITY_ID);
			if (db != null) {
				scanRecordDao.delete(db);
			}
		}
		scanRecordDao.add(record);
	}

	@Override
	public void finish() {
		if(listPosition!=-1&&basicInformationFragment!=null) {
			Intent i = new Intent();
			i.putExtra("positon", listPosition);
			i.putExtra("isCollect", basicInformationFragment.getCollection_checkbox());
			setResult(RESULT_OK, i);
		
		}
		super.finish();
	}

	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		YtShare.createInstance(this).destroy();
//	}

}
