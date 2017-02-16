package com.huiyin.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LoginInfo;
import com.huiyin.constants.Config;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.UploadHelper;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.utils.UploadHelper.UploadCallBack;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AccountManageActivity extends BaseActivity {

	private final static String TAG = "AccountManageActivity";
	TextView account_address, left_ib, middle_title_tv;
	ImageView account_head;

	TextView account_username, account_modify, account_level, account_jifen;
	TextView account_tie, account_regist_time, account_last_login;
	TextView account_exit_tv;

	String change_name, phone;
	private int request_code = 0x00110;
	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;
	private File imageFile;
	private ImageUpload mIUpload;

	private RelativeLayout jifen;

	// Bitmap curImagePic;
	// private String imagePath = "";
	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 0) {
	// UIHelper.showLoadDialog(AccountManageActivity.this,
	// AccountManageActivity.this.getString(R.string.loading));
	// } else {
	// DisplayImageOptions options = new
	// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
	// .showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
	// .showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	// .bitmapConfig(Bitmap.Config.RGB_565).displayer(new
	// RoundedBitmapDisplayer(100)).build();
	// ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath,
	// account_head, options);
	//
	// if (AppContext.mUserInfo != null) {
	// AppContext.mUserInfo.img = imagePath;
	// }
	// UIHelper.cloesLoadDialog();
	// }
	//
	// }
	//
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manage_layout);

		initView();
		initData();

	}

	private void initData() {

		if (AppContext.getInstance().getUserInfo() == null) {
			return;
		}
		UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
		account_username.setText(mUserInfo.userName);
		account_level.setText(mUserInfo.level);
		account_jifen.setText(mUserInfo.integral);
		account_regist_time.setText(mUserInfo.createDate);
		account_last_login.setText(mUserInfo.lastDate);

		if (mUserInfo.img != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
					.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
					.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
			ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + mUserInfo.img, account_head, options);

		} else {
			account_head.setImageDrawable(getResources().getDrawable(R.drawable.default_head));
		}
	}

	private void initView() {

		jifen = (RelativeLayout) findViewById(R.id.myjifen);
		jifen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, MyJiFenActivity.class));
			}
		});

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("账户管理");

		account_address = (TextView) findViewById(R.id.account_address);
		account_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(AccountManageActivity.this, AddressManagementActivity.class);
				i.putExtra(AddressManagementActivity.TAG, AddressManagementActivity.addr);
				startActivity(i);
			}
		});

		account_head = (ImageView) findViewById(R.id.account_head);
		account_username = (TextView) findViewById(R.id.account_username);
		account_modify = (TextView) findViewById(R.id.account_modify);
		account_level = (TextView) findViewById(R.id.account_level);
		account_jifen = (TextView) findViewById(R.id.account_jifen);
		account_tie = (TextView) findViewById(R.id.account_tie);
		account_regist_time = (TextView) findViewById(R.id.account_regist_time);
		account_last_login = (TextView) findViewById(R.id.account_last_login);
		account_exit_tv = (TextView) findViewById(R.id.account_exit_tv);

		account_modify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(AccountManageActivity.this, ModifyUserNameActivity.class);
				startActivityForResult(i, request_code);
			}
		});

		account_exit_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 退出
				Intent i = new Intent(AccountManageActivity.this, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_EXIT_DIALOG);
				startActivityForResult(i, CommonConfrimCancelDialog.EXIT_APP_CODE);

			}
		});

		account_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(AccountManageActivity.this, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
				startActivityForResult(i, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
			}
		});

		account_tie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 绑定手机号
				Intent i = new Intent(AccountManageActivity.this, BindActivity.class);
				startActivity(i);

			}
		});

		account_level.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AccountManageActivity.this, MemberRankActivity.class);
				startActivity(i);
			}
		});
	}

	// private void doBindPhone() {
	//
	// if (AppContext.userId == null) {
	// return;
	// }
	// RequstClient.bindPhone(AppContext.userId, "121154", new
	// CustomResponseHandler(this) {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, String content) {
	// super.onSuccess(statusCode, headers, content);
	// LogUtil.i(TAG, "doBindPhone:" + content);
	// try {
	// JSONObject obj = new JSONObject(content);
	// if (!obj.getString("type").equals("1")) {
	// String errorMsg = obj.getString("msg");
	// Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// Toast.makeText(getBaseContext(), obj.getString("msg"),
	// Toast.LENGTH_SHORT).show();
	//
	// } catch (JsonSyntaxException e) {
	// e.printStackTrace();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// });
	// }

	private void doModify() {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.doModifyUsername(AppContext.getInstance().getUserId(), change_name, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doModify:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					account_username.setText(change_name);
					if (AppContext.getInstance().getUserInfo() != null) {
						AppContext.getInstance().getUserInfo().userName = change_name;
						
						UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
						UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
					}
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
	 * 上传图片
	 * 
	 * @param data
	 */
	// private void uploadGoodsImage(Intent data) {
	//
	// Uri uri;
	// if (data.getData() != null) {
	// uri = data.getData();
	// try {
	// curImagePic =
	// MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// } else {
	// Bundle bundle = data.getExtras();
	// curImagePic = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
	// uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
	// curImagePic, null, null));
	// }
	// final String imageName = new
	// SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
	// final File savepath = new File(Config.APP_SAVE_PATH + "CacheImage/");
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// UploadHelper.getInstance().doUpload(savepath.getAbsolutePath(),
	// imageName, curImagePic, "1",
	// new UploadCallBack() {
	// @Override
	// public void onStart(String filePath) {
	// Message msg = new Message();
	// msg.what = 0;
	// mHandler.sendMessage(msg);
	// }
	//
	// @Override
	// public void onFinish(String content) {
	// LogUtil.i(TAG, "uploadGoodsImage:" + content);
	// try {
	// final JSONObject obj = new JSONObject(content);
	// String returnId = obj.getString("returnId");
	// returnId = TextUtils.isEmpty(returnId) ? "0" : returnId;
	// if (Integer.parseInt(returnId) > 0) {
	// // imagePath = obj.getString("saveUrl");
	// if (null != curImagePic) {
	// Message msg = new Message();
	// msg.what = 1;
	// mHandler.sendMessage(msg);
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onError(final String errorMsg) {
	// }
	// });
	// }
	//
	// }).start();
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CommonConfrimCancelDialog.EXIT_APP_CODE && resultCode == RESULT_OK) {
			// AppManager.getAppManager().AppExit(AccountManageActivity.this);
			// 退出登录
			LoginInfo mLoginInfo = new LoginInfo();
			mLoginInfo.psw = "";
			mLoginInfo.userName = "";
			mLoginInfo.isChecked = false;
//			AppContext.mUserInfo = null;
//			AppContext.userId = null;
			AppContext.saveLoginInfo(getApplicationContext(), mLoginInfo);
			UserinfoPreferenceUtil.setLoginFail(mContext);
			
			// Intent intent = new Intent();
			// intent.setAction("com.login.exit");
			// sendBroadcast(intent);

			// Intent i = new Intent(AccountManageActivity.this,
			// MainActivity.class);
			// setResult(RESULT_OK, i);
			Intent intent = new Intent(AccountManageActivity.this, ExitSuccessActivity.class);
			startActivity(intent);
			finish();

		} else if (requestCode == request_code && resultCode == RESULT_OK) {
			change_name = data.getStringExtra("username");
			doModify();
		} else if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE && resultCode == RESULT_OK) {
			/* 选择图片来源 */
			String uploadType = data.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}

		} else if (resultCode == RESULT_OK && requestCode == CAMER_CODE) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(imageFile), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 100);
			intent.putExtra("outputY", 100);
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("scale", true);
			imageFile = ImageFolder.getTempImageName();
			intent.putExtra("output", Uri.fromFile(imageFile));
			intent.putExtra("outputFormat", "JPEG");// 返回格式
			startActivityForResult(intent, LOCAL_CODE);
		} else if (resultCode == RESULT_OK && requestCode == LOCAL_CODE) {
			Log.i(TAG, "获取图片返回成功");
			// uploadGoodsImage(data);
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(imageFile.getPath());
			mIUpload = new ImageUpload(mContext, temp, mUpImageListener);
			mIUpload.upLoadHeadImage();
		}

	}

	private void callCamerImage() {

		Log.i(TAG, "启动照相机");
		imageFile = ImageFolder.getTempImageName();
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(imageFile);
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		captureIntent.putExtra("return-data", true);
		startActivityForResult(captureIntent, CAMER_CODE);
	}

	private void callLocalImage() {

		Log.i(TAG, "打开图库");
		// Intent localIntent = new Intent(Intent.ACTION_GET_CONTENT);
		// localIntent.addCategory(Intent.CATEGORY_OPENABLE);
		// localIntent.setType("image/*");
		// // 是否调用系统裁剪
		// localIntent.putExtra("crop", "true");
		//
		// /* 图片宽高的像素值 */
		// localIntent.putExtra("outputX", 100);
		// localIntent.putExtra("outputY", 100);
		// localIntent.putExtra("return-data", true);
		// startActivityForResult(localIntent, LOCAL_CODE);
		imageFile = ImageFolder.getTempImageName();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true"); // 开启剪切
		intent.putExtra("aspectX", 1); // 剪切的宽高比为1：1
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100); // 保存图片的宽和高
		intent.putExtra("outputY", 100);
		intent.putExtra("output", Uri.fromFile(imageFile)); // 保存路径
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(intent, LOCAL_CODE);
	}

	private UpLoadImageListener mUpImageListener = new UpLoadImageListener() {

		@Override
		public void UpLoadSuccess(ArrayList<String> netimageurls) {
			if (netimageurls == null || netimageurls.size() == 0)
				return;
			if (AppContext.getInstance().getUserInfo() != null) {
				AppContext.getInstance().getUserInfo().img = netimageurls.get(0);
				
				UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
				UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
			}
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
					.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
					.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
			ImageManager.LoadWithServer(netimageurls.get(0), account_head, options);

		}

		@Override
		public void UpLoadFail() {

		}
	};
}
