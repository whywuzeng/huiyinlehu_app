package com.huiyin.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.LoginInfo;
import com.huiyin.bean.UpdataInfo;
import com.huiyin.ui.classic.CategoryFragment;
import com.huiyin.ui.home.HomeFragment;
import com.huiyin.ui.housekeeper.HouseKeeperFragment;
import com.huiyin.ui.shoppingcar.ShoppingCarFragment;
import com.huiyin.ui.user.MyLeHuFragment;
import com.huiyin.utils.AppManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.NetworkUtils;
import com.huiyin.utils.SettingPreferenceUtil;
import com.huiyin.utils.SettingPreferenceUtil.SettingItem;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UpdateManager;
import com.huiyin.utils.UpdateVersionTool;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.utils.Utils;
import com.huiyin.wight.BadgeView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/***
 * 
 * @author xieshibin
 * 
 */
public class MainActivity extends FragmentActivity {

	/** 快捷请求CODE */
	public final static int LNKTOOLS_REQUEST = 1;
	/** 快捷工具返回的RESULTCODE */
	public final static int LNKTOOLS_RESULT = 10;

	private HomeFragment mHomeFragment;
	private CategoryFragment mClassicFragment;
	private ShoppingCarFragment mShoppingCarFragment;
	private HouseKeeperFragment mHouseKeeperFragment;
	private MyLeHuFragment mMyLeHuFragment;

	public static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentList;
	private FragmentManager mFragmentManager;
	public ViewPager mainPager;

	public static final int FIRST_PAGE_INDEX = 0, CLASSIC_INDEX = 1, SHOPPING_CAR_INDEX = 2, HOUSE_KEEPER_INDEX = 3,
			MY_LEHU_INDEX = 4;

	private TextView mFirstPageTV, mClassicTV, mShoppingCarTV, mHouseKeeperTV, mMyLeHuTv;
	private ImageView mFirstPageIV, mClassicIV, mShoppingCarIV, mHouseKeeperIV, mMyLeHuIV;
	private View mFirstPageView, mClassicView, mShoppingCarView, mHouseKeeperView, mMyLeHuView;

	private ImageView mFirstPageFlag, mClassicFlag, mShoppingCarFlag, mHouseKeeperFlag, mMyLeHuFlag;

	private SettingItem mSettingItem;

	private BadgeView shoppingBadge, messageBadge;

	// private LoginReceiver mLoginReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		autoLogin();

		new ProgressDialog(this);
		if (!NetworkUtils.isNetworkAvailable(this)) {
			Toast.makeText(this, "网络故障，请先检查网络连接!", Toast.LENGTH_LONG).show();
			return;
		} else {
			// 检测版本
			requestVersion();
		}

		// 百度云推送
		mSettingItem = SettingPreferenceUtil.getInstance(this).getSettingItem();
		if (mSettingItem.isMessage()) {
			PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(this, "api_key"));
			if (mSettingItem.isMessageTime())
				PushManager.setNoDisturbMode(this, 23, 00, 06, 50);
			else
				PushManager.setNoDisturbMode(this, 00, 01, 23, 59);
		}

		// mLoginReceiver = new LoginReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("com.login.success");
		// filter.addAction("com.login.exit");
		// registerReceiver(mLoginReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(mLoginReceiver);
	}

	private void initBottomView() {
		BottomItemOnClickListener mClickListener = new BottomItemOnClickListener();
		mFirstPageView = findViewById(R.id.first_page_item);
		mFirstPageIV = (ImageView) findViewById(R.id.first_page_iv);
		mFirstPageTV = (TextView) findViewById(R.id.first_page_tv);

		mClassicView = findViewById(R.id.classic_item);
		mClassicIV = (ImageView) findViewById(R.id.classic_iv);
		mClassicTV = (TextView) findViewById(R.id.classic_tv);

		mShoppingCarView = findViewById(R.id.shopping_car_item);
		mShoppingCarIV = (ImageView) findViewById(R.id.shopping_car_iv);
		mShoppingCarTV = (TextView) findViewById(R.id.shopping_car_tv);

		mHouseKeeperView = findViewById(R.id.house_keeper_item);
		mHouseKeeperIV = (ImageView) findViewById(R.id.house_keeper_iv);
		mHouseKeeperTV = (TextView) findViewById(R.id.house_keeper_tv);

		mMyLeHuView = findViewById(R.id.my_lehu_item);
		mMyLeHuIV = (ImageView) findViewById(R.id.my_lehu_iv);
		mMyLeHuTv = (TextView) findViewById(R.id.my_lehu_tv);

		mFirstPageFlag = (ImageView) findViewById(R.id.first_page_item_flag);
		mClassicFlag = (ImageView) findViewById(R.id.classic_item_flag);
		mShoppingCarFlag = (ImageView) findViewById(R.id.shopping_car_item_flag);
		mHouseKeeperFlag = (ImageView) findViewById(R.id.house_keeper_item_flag);
		mMyLeHuFlag = (ImageView) findViewById(R.id.my_lehu_item_flag);

		mFirstPageView.setOnClickListener(mClickListener);
		mClassicView.setOnClickListener(mClickListener);
		mShoppingCarView.setOnClickListener(mClickListener);
		mHouseKeeperView.setOnClickListener(mClickListener);
		mMyLeHuView.setOnClickListener(mClickListener);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (AppContext.getInstance().getUserInfo() == null) {
			setTheShoppcar(0);
			setTheMessage(0);
		} else {
			setTheShoppcar(AppContext.getInstance().getUserInfo().SHOPPING_CAR);
			int count = MathUtil.stringToInt(AppContext.getInstance().getUserInfo().systemMessage);
			setTheMessage(count);
		}

		if (AppContext.MAIN_TASK == null || StringUtils.isEmpty(AppContext.MAIN_TASK)) {
			return;
		}
		if (AppContext.MAIN_TASK.equals(AppContext.FIRST_PAGE)) {
			mainPager.setCurrentItem(FIRST_PAGE_INDEX);
			updataBottomStatu(FIRST_PAGE_INDEX);
		} else if (AppContext.MAIN_TASK.equals(AppContext.CLASSIC)) {
			mainPager.setCurrentItem(CLASSIC_INDEX);
			updataBottomStatu(CLASSIC_INDEX);
		} else if (AppContext.MAIN_TASK.equals(AppContext.SHOPCAR)) {
			mainPager.setCurrentItem(SHOPPING_CAR_INDEX);
			updataBottomStatu(SHOPPING_CAR_INDEX);
		} else if (AppContext.MAIN_TASK.equals(AppContext.HOUSEKEEPER)) {
			mainPager.setCurrentItem(HOUSE_KEEPER_INDEX);
			updataBottomStatu(HOUSE_KEEPER_INDEX);
		} else if (AppContext.MAIN_TASK.equals(AppContext.LEHU)) {
			mainPager.setCurrentItem(MY_LEHU_INDEX);
			updataBottomStatu(MY_LEHU_INDEX);
		}

	}

	private void setFirstPage(boolean isSelect) {
		mFirstPageView.setSelected(isSelect);
		mFirstPageIV.setSelected(isSelect);
		mFirstPageTV.setSelected(isSelect);
		mFirstPageFlag.setSelected(isSelect);
	}

	private void setClassic(boolean isSelect) {
		mClassicView.setSelected(isSelect);
		mClassicIV.setSelected(isSelect);
		mClassicTV.setSelected(isSelect);
		mClassicFlag.setSelected(isSelect);
	}

	private void setShoppingCar(boolean isSelect) {
		mShoppingCarView.setSelected(isSelect);
		mShoppingCarIV.setSelected(isSelect);
		mShoppingCarTV.setSelected(isSelect);
		mShoppingCarFlag.setSelected(isSelect);
	}

	private void setKeeperHouse(boolean isSelect) {
		mHouseKeeperView.setSelected(isSelect);
		mHouseKeeperIV.setSelected(isSelect);
		mHouseKeeperTV.setSelected(isSelect);
		mHouseKeeperFlag.setSelected(isSelect);
	}

	private void setMyLeHu(boolean isSelect) {
		mMyLeHuView.setSelected(isSelect);
		mMyLeHuIV.setSelected(isSelect);
		mMyLeHuTv.setSelected(isSelect);
		mMyLeHuFlag.setSelected(isSelect);
	}

	private void updataBottomStatu(int index) {
		switch (index) {
		case FIRST_PAGE_INDEX: {
			/* 首页 */
			if (!mFirstPageView.isSelected()) {
				setTitle(R.string.first_page);
				setFirstPage(true);
				setClassic(false);
				setShoppingCar(false);
				setKeeperHouse(false);
				setMyLeHu(false);
				mClassicFragment.backToCategory();
			}

		}
			break;
		case CLASSIC_INDEX: {
			/* 分类 */
			if (!mClassicView.isSelected()) {
				setTitle(R.string.classification);
				setFirstPage(false);
				setClassic(true);
				setShoppingCar(false);
				setKeeperHouse(false);
				setMyLeHu(false);
				mClassicFragment.backToCategory();
				mClassicFragment.backToOrigin();
			}
		}
			break;
		case SHOPPING_CAR_INDEX: {
			/* 购物车 */
			if (!mShoppingCarView.isSelected()) {
				setTitle(R.string.shopping_car);
				setFirstPage(false);
				setClassic(false);
				setShoppingCar(true);
				setKeeperHouse(false);
				setMyLeHu(false);
				mClassicFragment.backToCategory();
			}
		}
			break;
		case HOUSE_KEEPER_INDEX: {
			/* 管家 */
			if (!mHouseKeeperView.isSelected()) {
				setTitle(R.string.housekeeper);
				setFirstPage(false);
				setClassic(false);
				setShoppingCar(false);
				setKeeperHouse(true);
				setMyLeHu(false);
				mClassicFragment.backToCategory();
			}
		}
			break;
		case MY_LEHU_INDEX: {
			/* 我的乐虎 */
			if (!mMyLeHuView.isSelected()) {
				setTitle(R.string.my_lehu);
				setFirstPage(false);
				setClassic(false);
				setShoppingCar(false);
				setKeeperHouse(false);
				setMyLeHu(true);
				mClassicFragment.backToCategory();
			}
		}
			break;
		}

	}

	protected class BottomItemOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.first_page_item: {
				AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
				mainPager.setCurrentItem(FIRST_PAGE_INDEX);
				updataBottomStatu(FIRST_PAGE_INDEX);
			}
				break;
			case R.id.classic_item: {
				AppContext.MAIN_TASK = AppContext.CLASSIC;
				mainPager.setCurrentItem(CLASSIC_INDEX);
				updataBottomStatu(CLASSIC_INDEX);
			}
				break;
			case R.id.shopping_car_item: {
				if (AppContext.MAIN_TASK != AppContext.SHOPCAR) {
					AppContext.MAIN_TASK = AppContext.SHOPCAR;
					mainPager.setCurrentItem(SHOPPING_CAR_INDEX);
					updataBottomStatu(SHOPPING_CAR_INDEX);
					// setTheShoppcar(0);
					mShoppingCarFragment.pageSelected(SHOPPING_CAR_INDEX);
				}
			}
				break;
			case R.id.house_keeper_item: {
				AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
				mainPager.setCurrentItem(HOUSE_KEEPER_INDEX);
				updataBottomStatu(HOUSE_KEEPER_INDEX);
			}
				break;
			case R.id.my_lehu_item: {
				if (AppContext.MAIN_TASK != AppContext.LEHU) {
					AppContext.MAIN_TASK = AppContext.LEHU;
					mainPager.setCurrentItem(MY_LEHU_INDEX);
					updataBottomStatu(MY_LEHU_INDEX);
					// setTheMessage(0);
					mMyLeHuFragment.pageSelected(MY_LEHU_INDEX);
				}
			}
				break;
			}
		}

	}

	private void doLogin(String userName, String psw) {

		RequstClient.doLogin(userName, psw, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						UserinfoPreferenceUtil.setLoginFail(MainActivity.this);
						return;
					}

					UserInfo mUserInfo = new Gson().fromJson(new JSONObject(content).getJSONObject("user").toString(),
							UserInfo.class);
//					AppContext.mUserInfo = mUserInfo;
//					AppContext.userId = mUserInfo.userId;
					UserinfoPreferenceUtil.setLoginSuccess(MainActivity.this, mUserInfo);
					setTheShoppcar(mUserInfo.SHOPPING_CAR);
					setTheMessage(Integer.valueOf(mUserInfo.systemMessage));
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	/***
	 * 自动登录
	 */
	private void autoLogin() {
		LoginInfo mLoginInfo = AppContext.getLoginInfo(getApplicationContext());
		if (mLoginInfo.isChecked) {
			doLogin(mLoginInfo.userName, mLoginInfo.psw);
		}
	}

	private void initView() {
		AppManager.getAppManager().addActivity(this);

		initBottomView();

		mFragmentManager = getSupportFragmentManager();
		mFragmentList = new ArrayList<Fragment>();
		mClassicFragment = new CategoryFragment();
		mShoppingCarFragment = new ShoppingCarFragment();
		mHouseKeeperFragment = new HouseKeeperFragment();
		mMyLeHuFragment = new MyLeHuFragment();

		mHomeFragment = new HomeFragment();

		mFragmentList.add(mHomeFragment);
		mFragmentList.add(mClassicFragment);
		mFragmentList.add(mShoppingCarFragment);
		mFragmentList.add(mHouseKeeperFragment);
		mFragmentList.add(mMyLeHuFragment);
		mainPager = (ViewPager) findViewById(R.id.content_pager);
		mainPager.setOffscreenPageLimit(5);
		mainPager.setAdapter(new MainPagerAdapter(mFragmentManager));
		mainPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				updataBottomStatu(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mainPager.setCurrentItem(0);
		updataBottomStatu(FIRST_PAGE_INDEX);

	}

	private long secondTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		long spaceTime = 0;
		long firstTime = 0;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mainPager.getCurrentItem() != 0) {
				goShopping(null);
				return false;
			}
			firstTime = System.currentTimeMillis();
			spaceTime = firstTime - secondTime;
			secondTime = firstTime;
			if (spaceTime > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				AppManager.getAppManager().AppExit(this);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	class MainPagerAdapter extends FragmentStatePagerAdapter {

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragmentList == null ? 0 : mFragmentList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}

	/***
	 * 检测版本
	 * */
	private void requestVersion() {
		CustomResponseHandler handler = new CustomResponseHandler(this, false) {
			@Override
			public void onRefreshData(String content) {
				analyticalVersionData(content);
			}
		};
		RequstClient.getVersion(handler);
	}

	/***
	 * 解析数据
	 * */
	private void analyticalVersionData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				String url = roots.getString("editionUrl");
				String versionName = roots.getString("editionName");
				String versionVersion = roots.getString("editionExplain");

				int versionCode = Integer.valueOf(roots.getString("edition"));
				UpdataInfo updateInfo = new UpdataInfo();
				updateInfo.setUrl(url);
				updateInfo.setVersion(versionName);
				updateInfo.setVersionCode(versionCode);
				updateInfo.setDescription(versionVersion);

				updateApk(updateInfo);

			} else {
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void updateApk(final UpdataInfo updateInfo) {
		if (UpdateVersionTool.getVersionCode(this) >= updateInfo.getVersionCode()) {
			return;
		} else {
			UpdateManager manager = new UpdateManager(this, "huiyin", updateInfo.getUrl());
			manager.checkUpdate(updateInfo.getVersionCode(), updateInfo.getDescription());
		}
	}

	public void goShopping(View v) {
		AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
		mainPager.setCurrentItem(FIRST_PAGE_INDEX);
		updataBottomStatu(FIRST_PAGE_INDEX);
	}

	public void setTheShoppcar(int count) {

		//BadgeView 数字提醒
		if (shoppingBadge == null) {
			shoppingBadge = new BadgeView(this, mShoppingCarView);
		}
		if (count <= 0) {
			shoppingBadge.hide();
		} else if (count <= 99) {
			shoppingBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			shoppingBadge.setTextSize(12);
			shoppingBadge.setText(String.valueOf(count));
			shoppingBadge.show();
		} else if (count > 99) {
			shoppingBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			shoppingBadge.setTextSize(12);
			shoppingBadge.setText("99+");
			shoppingBadge.show();
		}
	}

	public void setTheMessage(int count) {

		if (messageBadge == null) {
			messageBadge = new BadgeView(this, mMyLeHuView);
		}
		if (count <= 0) {
			messageBadge.hide();
		} else if (count <= 99) {
			messageBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			messageBadge.setTextSize(12);
			messageBadge.setText(String.valueOf(count));
			messageBadge.show();
		} else if (count > 99) {
			messageBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			messageBadge.setTextSize(12);
			messageBadge.setText("99+");
			messageBadge.show();
		}
	}

	// 登陆成功以及注销登陆的广播接收器
	public class LoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.login.success")) {
				int shoppcar = intent.getIntExtra("shoppcar", 0);
				setTheShoppcar(shoppcar);
				int message = intent.getIntExtra("message", 0);
				setTheMessage(message);
			}
			if (action.equals("com.login.exit")) {
				setTheShoppcar(0);
				setTheMessage(0);
			}
		}
	}
}
