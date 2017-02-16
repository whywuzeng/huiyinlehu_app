package com.huiyin.ui.user;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.ImageViewWithDel;
import com.huiyin.wight.ImageViewWithDel.MyImageViewCallBack;

public class WriteLogisticNumActivity extends BaseActivity {

	private final static String TAG = "WriteLogisticNumActivity";
	private Button write_logistic_commit_btn;
	private TextView left_ib, middle_title_tv;
	private LinearLayout dongtai_add;
	private ImageView make_pointment;

	EditText logistic_num, logistic_company;
	// 订单
	String logisticsName, logisticsNo, returnId, commodityId, picUrls = "";
	String flag;
	public static String CHANGE_FLAG = "2", BACK_FLAG = "1";
	private Toast mToast;
	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;

	Bitmap curImagePic;

	String imagePath = "";

	private int CAMERA_WITH_DATA = 0x11;
	private int CAMERA_PICK_PHOTO = 0x21;
	private MyImageViewCallBack ImageViewDel;
	private File imageFile;
	private ImageUpload mIUpload;
	private ArrayList<String> hostimageurls;
	private ArrayList<String> netimageurls;
	private int countImage;

	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 0) {
	// upload_layout.addPics(curImagePic);
	// picUrls += imagePath + ",";
	// UIHelper.cloesLoadDialog();
	// } else if (msg.what == 1) {
	// UIHelper.showLoadDialog(WriteLogisticNumActivity.this,
	// WriteLogisticNumActivity.this.getString(R.string.loading));
	// } else if (msg.what == 2) {
	// UIHelper.cloesLoadDialog();
	// }
	//
	// }
	//
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_logistics_number);

		initView();

	}

	private void initView() {

		returnId = getIntent().getStringExtra("returnId");
		commodityId = getIntent().getStringExtra("commodityId");
		flag = getIntent().getStringExtra("flag");

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("填写物流信息");

		write_logistic_commit_btn = (Button) findViewById(R.id.write_logistic_commit_btn);
		write_logistic_commit_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkLogisticInfo()) {

					hostimageurls = new ArrayList<String>();
					if (dongtai_add.getChildCount() > 0) {
						for (int i = 0; i < dongtai_add.getChildCount(); i++) {
							ImageViewWithDel temp = (ImageViewWithDel) dongtai_add.getChildAt(i);
							hostimageurls.add(temp.getImagePath());
						}
						UpLoadImage();
					} else {
						postLogisticInfo();
					}

				}
			}
		});

		logistic_num = (EditText) findViewById(R.id.logistic_num);
		logistic_company = (EditText) findViewById(R.id.logistic_company);

		dongtai_add = (LinearLayout) findViewById(R.id.dongtai_add);
		make_pointment = (ImageView) findViewById(R.id.make_pointment);

		make_pointment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (countImage >= 5) {
					Toast.makeText(mContext, "一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent i = new Intent(mContext, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
				startActivityForResult(i, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
			}
		});

		countImage = 0;
		ImageViewDel = new MyImageViewCallBack() {

			@Override
			public void imgClick() {

			}

			@Override
			public void del(View v) {
				ViewGroup parent = (ViewGroup) v.getParent();
				if (parent != null) {
					parent.removeView(v);
					if (--countImage < 0) {
						countImage = 0;
					}
				}
			}
		};
	}

	/**
	 * 检验物流信息
	 * 
	 * @return
	 */
	private boolean checkLogisticInfo() {

		logisticsName = logistic_company.getText().toString();
		logisticsNo = logistic_num.getText().toString();

		if (TextUtils.isEmpty(logisticsName)) {
			showToast(R.string.logistic_company);
			return false;
		} else if (TextUtils.isEmpty(logisticsNo)) {
			showToast(R.string.logistic_num);
			return false;
		}
		return true;
	}

	private void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	private void postLogisticInfo() {
		picUrls = "";
		if (netimageurls != null && netimageurls.size() > 0) {
			for (int i = 0; i < netimageurls.size(); i++) {
				picUrls += netimageurls.get(i);
				picUrls += ",";
			}
			picUrls = picUrls.substring(0, picUrls.length() - 1);
		}
		RequstClient.writeLogistic(logisticsName, logisticsNo, returnId, commodityId, picUrls, flag, new CustomResponseHandler(
				this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postLogisticInfo:" + content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					MyOrderActivity.setFlush(true);
					MyOrderActivity.setBackList(MyOrderActivity.second);
					finish();

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

	protected void UpLoadImage() {
		if (hostimageurls != null && hostimageurls.size() > 0) {
			mIUpload = new ImageUpload(mContext, hostimageurls, new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> imageurls) {
					netimageurls = imageurls;
					postLogisticInfo();
				}

				@Override
				public void UpLoadFail() {

				}
			});
			mIUpload.startLoad();
			return;
		}
	}

	// /***
	// * 上传图片
	// *
	// * @param data
	// */
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
	// if (null == curImagePic) {
	// return;
	// }
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	//
	// UploadHelper.getInstance().doUpload(savepath.getAbsolutePath(),
	// imageName, curImagePic, "0",
	// new UploadCallBack() {
	// @Override
	// public void onStart(String filePath) {
	// Message msg = new Message();
	// msg.what = 1;
	// mHandler.sendMessage(msg);
	// }
	//
	// @Override
	// public void onFinish(String content) {
	// LogUtil.i(TAG, "uploadGoodsImage:" + content);
	// try {
	// JSONObject obj = new JSONObject(content);
	// String returnId = obj.getString("returnId");
	// returnId = TextUtils.isEmpty(returnId) ? "0" : returnId;
	// if (Integer.parseInt(returnId) > 0) {
	//
	// imagePath = obj.getString("saveUrl");
	// if (null != curImagePic) {
	// Message msg = new Message();
	// msg.what = 0;
	// mHandler.sendMessage(msg);
	//
	// }
	//
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onError(final String errorMsg) {
	// Message msg = new Message();
	// msg.what = 2;
	// mHandler.sendMessage(msg);
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
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE) {
			/* 选择图片来源 */
			String uploadType = data.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}

		} else {
			Bitmap bm;
			switch (requestCode) {
			case 0x11:
				bm = BitmapUtils.showimageFull(imageFile.getPath(), 50, 50);
				showimage(bm);
				break;
			case 0x21:
				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				bm = BitmapUtils.showimageFull(picturePath, 50, 50);
				showimage(bm, picturePath);
				break;
			}
		}
	}

	private void callCamerImage() {
		imageFile = ImageFolder.getTempImageName();
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(imageFile);
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		captureIntent.putExtra("return-data", true);
		startActivityForResult(captureIntent, CAMERA_WITH_DATA);
	}

	private void callLocalImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, CAMERA_PICK_PHOTO);
	}

	public void showimage(Bitmap bm) {
		ImageViewWithDel temp = new ImageViewWithDel(mContext);
		temp.setImage(bm, imageFile.getPath());
		temp.setVisibility(View.VISIBLE);
		temp.setCallback(ImageViewDel);
		LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 52),
				DensityUtil.dip2px(mContext, 52));
		temp.setLayoutParams(viewParams);
		dongtai_add.addView(temp);
		countImage++;
	}

	public void showimage(Bitmap bm, String path) {
		ImageViewWithDel temp = new ImageViewWithDel(mContext);
		temp.setImage(bm, path);
		temp.setVisibility(View.VISIBLE);
		temp.setCallback(ImageViewDel);
		LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 52),
				DensityUtil.dip2px(mContext, 52));
		temp.setLayoutParams(viewParams);
		dongtai_add.addView(temp);
		countImage++;
	}
}
