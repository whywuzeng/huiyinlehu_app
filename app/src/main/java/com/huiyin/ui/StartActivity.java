package com.huiyin.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.PreferenceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class StartActivity extends BaseActivity {

	private TextView version;
	
	private ImageView welcome;
	private File welcomeimgFlord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		
		StatService.setDebugOn(true);
		

		welcome = (ImageView) findViewById(R.id.welcome);
		welcomeimgFlord = StorageUtils.getOwnCacheDirectory(mContext, "/huiyinlehu/cache/welcome");
		if (welcomeimgFlord.listFiles().length > 0) {
			Bitmap mbitmap = BitmapFactory.decodeFile(welcomeimgFlord
					.listFiles()[0].getPath());
			welcome.setImageBitmap(mbitmap);
		}else{
			Drawable drawable = getResources().getDrawable(R.drawable.welcome);
			welcome.setImageDrawable(drawable);
		}
		
		getImageView();

		sendMobileInfo();

		Log.i("", android.os.Build.MODEL + "==屏幕宽度=======" + DensityUtil.px2dip(this, DensityUtil.getScreenWidth(this)));

		version = (TextView) findViewById(R.id.version);

		version.setText("v " + getVersionName());

		// 渐变展示启动屏
//		AlphaAnimation animation = new AlphaAnimation(0.8f, 1.0f);
//		animation.setDuration(3000);
//		view.startAnimation(animation);
//		animation.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				boolean isFirstStart = PreferenceUtil.getInstance(getApplicationContext()).isFirstStart();
//				if (isFirstStart) {
//					UIHelper.showWelcomeAc(mContext);
//					finish();
//				} else {
//					UIHelper.showMainAc(mContext);
//					finish();
//				}
//				
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//		});
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				boolean isFirstStart = PreferenceUtil.getInstance(getApplicationContext()).isFirstStart();
				if (isFirstStart) {
					UIHelper.showWelcomeAc(mContext);
					finish();
				} else {
					UIHelper.showMainAc(mContext);
					finish();
				}
			}
		}, 3000);
	}
	
	
	private void getImageView() {
		CustomResponseHandler handler = new CustomResponseHandler(
				this, false) {
			@Override
			public void onRefreshData(String content) {
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = new JSONArray(object.getString("kaiji"));
					JSONObject jsonObject = array.getJSONObject(0);
					String[] str = jsonObject.getString("IMG").split("/");
					String FileName = str[str.length - 1];
					if (welcomeimgFlord.listFiles().length == 0
							|| !FileName.equals(welcomeimgFlord.list()[0]))
						downloadImage(jsonObject.getString("IMG"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		RequstClient.appOpenPicture(handler);
	}

	private void sendMobileInfo() {
		if (!PreferenceUtil.getInstance(getApplicationContext()).isSendMobileInfo()) {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			CustomResponseHandler handler = new CustomResponseHandler(this, false) {
				@Override
				public void onRefreshData(String content) {
					try {
						JSONObject roots = new JSONObject(content);
						if (roots.getString("type").equals("1")) {
							PreferenceUtil.getInstance(getApplicationContext()).setSendMobileInfo(true);
						} else {
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			RequstClient.sendMobileInfo(imei, getVersionName(), handler);
		}
	}
	
	
	private void downloadImage(String welcome_img) {
		final String imagename = welcome_img;
		welcome_img = URLs.IMAGE_URL + welcome_img;
		AsyncHttpClient client = new AsyncHttpClient();
		String[] allowedTypes = new String[] { "image/jpeg", "image/png" };
		client.get(welcome_img, new BinaryHttpResponseHandler(allowedTypes) {
			public void onSuccess(byte[] imageData) {
				if (imageData != null) {
					Bitmap mBitmap = BitmapFactory.decodeByteArray(imageData,
							0, imageData.length);// bitmap
					try {
						saveFile(mBitmap, imagename, mContext);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@SuppressWarnings("unused")
			public void onFailure(Throwable e, byte[] imageData) {

			}
		});

	}

	public void saveFile(Bitmap bm, String fileName, Context mContext)
			throws IOException {
		String[] str = fileName.split("/");
		String FileName = str[str.length - 1];
		File file = new File(welcomeimgFlord,FileName);
		File[] list = file.getParentFile().listFiles();
		for (int i = 0; i < list.length; i++)
			list[i].delete();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}


	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
		return version;
	}


	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
}
