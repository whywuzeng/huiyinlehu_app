package com.huiyin;

import io.rong.imkit.RongIM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.huiyin.api.URLs;
import com.huiyin.bean.LoginInfo;
import com.huiyin.bean.ShopItem;
import com.huiyin.db.DBHelper;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class AppContext extends FrontiaApplication {

	private static AppContext instance;
	private UserInfo mUserInfo;//账户信息
	private String userId; // 用户id
	public static boolean isFirstCopy = true;
	public static String MAIN_TASK;
	public static String FIRST_PAGE = "0";
	public static String CLASSIC = "1";
	public static String SHOPCAR = "2";
	public static String HOUSEKEEPER = "3";
	public static String LEHU = "4";
	public static boolean hasNewScanRecord = true;
	public static String orderId;// 用户的订单号
	public static String number;// 用户的订单编号
	public static String price;// 用户的订单金额
	public static String resultType;// 支付结果类型
	public static int payType;// 支付方式，1.支付宝 2.银联 3.微信4.服务卡
	
	// 用于今日头条显示页号的索引,没办法只能往这加
	public int newsTodayPage = 0;

	// 百度地图相关
	private String tempcoor = "bd09ll";
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	public LocationClient mLocationClient;
	public MyLocationListenner myListener;
	private LocationCallBack locationCallBack;
	// 未登录情况下添加到购物车的商品列表数据
	public static ArrayList<ShopItem> shopCarLists;

	// 百度云推送相关
	public String userIdBai;
	public String channelIdBai;

	// 便民服务卡号
	public static String cardNumber;
	public static String token;

	/** 经度 */
	public double lng = 0.0;
	/** 纬度 */
	public double lat = 0.0;
	public String cityName;

	private static DBHelper dbHelper;
	private static SQLiteDatabase db;

	public String nearbyId;

	public String getNearbyId() {
		nearbyId = getResources().getString(R.string.config_number);
		return nearbyId;
	}

	public static AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// Thread.setDefaultUncaughtExceptionHandler(AppException.getInstance());
		dbHelper = DBHelper.getInstance(this);
		db = dbHelper.getWritableDatabase();

		initData();
		initImageLoader(getApplicationContext());
		initBaiduApi();
		initRongCloud();

		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(this, R.layout.bpush_media_list_item,
				R.id.bpush_media_list_img, R.id.bpush_media_list_title, R.id.bpush_media_list_from_text);
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(R.drawable.ic_launcher);
		cBuilder.setLayoutDrawable(2);
		PushManager.setNotificationBuilder(this, 2, cBuilder);
		
		SharedPreferences mPreferences = getSharedPreferences("bind", Context.MODE_PRIVATE);
		channelIdBai = mPreferences.getString("channelIdBai", "");
		userIdBai = mPreferences.getString("userIdBai", "");
	}

	public static SQLiteDatabase getDB() {
		return db;
	}

	public static void openDB() {
		if (!db.isOpen()) {
			db = dbHelper.getWritableDatabase();
		}
	}

	public static void closeDB() {
		dbHelper.close();
	}

	private void initData() {
		MAIN_TASK = "";
		mUserInfo = null;
//		userId = null; // 用户id
		hasNewScanRecord = true;
	}

	/**
	 * 初始化图片加载器
	 * 
	 * @param context
	 */
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "huiyinlehu/cache/ImageCache");
		// DisplayImageOptions mOptions = new
		// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
		// .showStubImage(R.drawable.image_default_rectangle).showImageForEmptyUri(R.drawable.image_default_rectangle)
		// .showImageOnFail(R.drawable.image_default_rectangle).build();
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(getApplicationContext())
		// .denyCacheImageMultipleSizesInMemory()
		// .threadPoolSize(5)
		// // 线程池内加载的数量
		// .threadPriority(Thread.NORM_PRIORITY -
		// 2).denyCacheImageMultipleSizesInMemory()
		// .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 *
		// 1024 * 1024)
		// .discCacheSize(50 * 1024 *
		// 1024).denyCacheImageMultipleSizesInMemory()
		// .discCacheFileNameGenerator(new
		// Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
		// .discCacheFileCount(100).defaultDisplayImageOptions(mOptions).discCache(new
		// UnlimitedDiscCache(cacheDir))
		// .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 *
		// 1000)).build();
		// ImageLoader.getInstance().init(config);
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.image_default_rectangle)
				.showImageForEmptyUri(R.drawable.image_default_rectangle).showImageOnFail(R.drawable.image_default_rectangle)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(options)
				.discCache(new UnlimitedDiscCache(cacheDir)).discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCache(new UnlimitedDiscCache(cacheDir)).build();
		ImageLoader.getInstance().init(config);
	}

	public static void saveLoginInfo(Context mContext, LoginInfo mLoginInfo) {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

		Editor mEditor = mPreferences.edit();
		mEditor.putString("userName", mLoginInfo.userName);
		mEditor.putString("psw", mLoginInfo.psw);
		mEditor.putBoolean("isChecked", mLoginInfo.isChecked);

		mEditor.commit();

	}

	public static LoginInfo getLoginInfo(Context mContext) {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.userName = mPreferences.getString("userName", "");
		loginInfo.psw = mPreferences.getString("psw", "");
		loginInfo.isChecked = mPreferences.getBoolean("isChecked", false);

		return loginInfo;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	public void saveCacheAppindexFirstData(String data) {
		String cacheName = "cache_appindexfirst_data";
		saveObject(data, cacheName);
	}

	public String getCacheAppindexFirstData() {
		String cacheName = "cache_appindexfirst_data";
		return (String) readObject(cacheName);
	}

	public void saveCacheAppIndexPolyData(String data) {
		String cacheName = "cache_appindexpoly_data";
		saveObject(data, cacheName);
	}

	public String getCacheAppIndexPolyFirstData() {
		String cacheName = "cache_appindexpoly_data";
		return (String) readObject(cacheName);
	}

	public void saveCacheClassic(String data) {
		String cacheName = "cache_appclassic_data";
		saveObject(data, cacheName);
	}

	public String getCacheClassic() {
		String cacheName = "cache_appclassic_data";
		return (String) readObject(cacheName);
	}

	/**
	 * 初始化地图相关
	 */
	private void initBaiduApi() {
		SDKInitializer.initialize(getApplicationContext());
		mLocationClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(myListener);
		// StartLoacation();
	}

	/**
	 * 定位初始化
	 */
	public void initLocationClient() {
		LocationClientOption option = new LocationClientOption();
		// option.setCoorType("bd09ll"); // 设置坐标类型
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setLocationMode(tempMode);// 设置定位模式
		// 设置发起定位请求的间隔时间
		option.setScanSpan(2000);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			lat = location.getLatitude();
			lng = location.getLongitude();
			cityName = location.getCity();

			StringBuffer sb = new StringBuffer(256);
			sb.append("latitude : " + lat);
			sb.append("\nlontitude : " + lng);
			sb.append("\ncity : " + cityName);
			Log.i("BaiduLocationApi", sb.toString());

			if (locationCallBack != null)
				locationCallBack.getPoistion(location);

			if (lng != 0.0 && lat != 0.0) {
				mLocationClient.stop();
			}
		}
	}

	public interface LocationCallBack {
		public void getPoistion(BDLocation location);
	}

	public void setLocationCallBack(LocationCallBack locationCallBack) {
		this.locationCallBack = locationCallBack;
	}

	public void initRongCloud() {
		// 初始化。
		RongIM.init(this, URLs.rongIoKey, R.drawable.ic_launcher);
	}

	private void StartLoacation() {
		initLocationClient();
		if (mLocationClient.isStarted()) {
			return;
		}
		mLocationClient.start();
	}
	
	/**
	 * 获取用户信息
	 * 
	 * 先判断是否登陆成功，没有则返回null
	 * @return
	 */
	public UserInfo getUserInfo() {
		
		if(!UserinfoPreferenceUtil.isLoginSuccess(this)) {
			this.mUserInfo = null;
			return this.mUserInfo;
		}
		
		if(this.mUserInfo!=null) {
			return this.mUserInfo;
		} else {
			this.mUserInfo = UserinfoPreferenceUtil.getUserInfo(this);
			return this.mUserInfo;
		}
	}

	public String getUserId() {
		if(StringUtils.isBlank(this.userId)) {
			if(getUserInfo()!=null) {
				this.userId = getUserInfo().userId;
			} else {
				this.userId = null;
			}
		}
		return userId;
	}

	public void setLoginfalse () {
		this.mUserInfo = null;
		this.userId = null;
	}
}