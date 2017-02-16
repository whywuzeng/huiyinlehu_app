package com.huiyin.ui.user;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.user.MyOrderDetailActivity.OrderDetailBean.GoodItem;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommodityCommentActivity extends BaseActivity {

	private final static String TAG = "CommodityCommentActivity";

	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;

	TextView middle_title_tv, left_ib;
	GoodItem Commodity;

	ImageView commodity_img;
	TextView commodity_title, commodity_color, commodity_price, commodity_num;
	ImageView goods_star_iv;
	EditText comment_description;
	UploadImageLinearLayout upload_image_layout;
	TextView comment_commit_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.commodity_comment_layout);

		initView();
	}

	private void initView() {

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("评价晒单");

		Commodity = (GoodItem) getIntent().getSerializableExtra("commodity");

		commodity_img = (ImageView) findViewById(R.id.commodity_img);
		commodity_title = (TextView) findViewById(R.id.commodity_title);
		commodity_color = (TextView) findViewById(R.id.commodity_color);
		commodity_price = (TextView) findViewById(R.id.commodity_price);
		goods_star_iv = (ImageView) findViewById(R.id.goods_star_iv);
		comment_description = (EditText) findViewById(R.id.comment_description);
		upload_image_layout = (UploadImageLinearLayout) findViewById(R.id.upload_layout);
		comment_commit_tv = (TextView) findViewById(R.id.comment_commit_tv);

		upload_image_layout.setMaxSize(9);

		ImageLoader.getInstance().displayImage(
				URLs.IMAGE_URL + Commodity.COMMODITY_IMAGE_PATH, commodity_img);
		commodity_title.setText(Commodity.COMMODITY_NAME);
		commodity_color.setText(Commodity.SPECVALUE);
		commodity_price.setText(MathUtil
				.priceForAppWithSign(Commodity.COMMODITY_PRICE));

		goods_star_iv.setTag("5");
		goods_star_iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_DOWN:
					float x = event.getX();

					float widthRate = x / arg0.getWidth();
					if (widthRate > 0 && widthRate < 0.2) {
						goods_star_iv.setBackgroundResource(R.drawable.px_1);
						goods_star_iv.setTag("1");
					} else if (widthRate > 0.2 && widthRate < 0.4) {
						goods_star_iv.setBackgroundResource(R.drawable.px_2);
						goods_star_iv.setTag("2");
					} else if (widthRate > 0.4 && widthRate < 0.6) {
						goods_star_iv.setBackgroundResource(R.drawable.px_3);
						goods_star_iv.setTag("3");
					} else if (widthRate > 0.6 && widthRate < 0.8) {
						goods_star_iv.setBackgroundResource(R.drawable.px_4);
						goods_star_iv.setTag("4");
					} else if (widthRate > 0.8 && widthRate < 1) {
						goods_star_iv.setBackgroundResource(R.drawable.px_5);
						goods_star_iv.setTag("5");
					}
					break;

				}
				return false;

			}

		});

		comment_commit_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String comment = comment_description.getText().toString();
				final String startNum = (String) goods_star_iv.getTag();

				if (upload_image_layout.getUrls().size() > 0) {

					new ImageUpload(mContext, upload_image_layout.getUrls(),
							new UpLoadImageListener() {

								@Override
								public void UpLoadSuccess(
										ArrayList<String> netimageurls) {
									String urls = "";
									for (int i = 0; i < netimageurls.size(); i++) {

										if (i == netimageurls.size() - 1) {

											urls += netimageurls.get(i);
											break;
										}
										urls += netimageurls.get(i) + ",";
									}
									postComment(Commodity, comment, startNum,
											urls);
								}

								@Override
								public void UpLoadFail() {
									return;
								}

							}).startLoad();
				} else {
					String urls = "";
					postComment(Commodity, comment, startNum, urls);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE
				&& resultCode == RESULT_OK) {
			/* 选择图片来源 */
			String uploadType = data
					.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType
					.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}

		} else if ((requestCode == CAMER_CODE || requestCode == LOCAL_CODE)) {

			if (resultCode == RESULT_OK) {
				/* 相机或者图库返回OK */
				Log.i(TAG, "获取图片返回成功");
				dealWithImage(data);

			} else {
				Log.e(TAG, "获取图片失败");
			}

		}

	}

	private void dealWithImage(Intent data) {
		Bitmap curImagePic;
		if (data != null && data.getData() != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			curImagePic = BitmapUtils.showimageFull(picturePath, 100, 100);
			if (null == curImagePic) {
				return;
			}
			upload_image_layout.addPics(curImagePic);
			upload_image_layout.addUrls(picturePath);
		} else {
			File f = new File(capturePath);
			if (!f.exists()) {
				return;
			} else {
				curImagePic = BitmapUtils.showimageFull(capturePath, 100, 100);
				if (null == curImagePic) {
					return;
				}
				upload_image_layout.addPics(curImagePic);
				upload_image_layout.addUrls(capturePath);
			}
		}
	}

	public String capturePath;

	private void callCamerImage() {

		Log.i(TAG, "启动照相机");
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent captureIntent = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			String out_file_path;
			out_file_path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/huiyin";
			File dir = new File(out_file_path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			capturePath = ImageFolder.getTempImageName().getPath();
			captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(capturePath)));
			startActivityForResult(captureIntent, CAMER_CODE);
		}
	}

	private void callLocalImage() {

		Log.i(TAG, "打开图库");
		Intent localIntent = new Intent(Intent.ACTION_PICK);
		localIntent.setType("image/*");
		startActivityForResult(localIntent, LOCAL_CODE);
	}

	/**
	 * 发表商品评论
	 * 
	 */
	private void postComment(GoodItem item, String comment, String score,
			String imageUrls) {

		RequstClient.postCommentInfo(AppContext.getInstance().getUserId(), item.COMMODITY_ID,
				score, comment, item.ORDER_ID, imageUrls,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {

						super.onSuccess(statusCode, headers, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
							} else {
								setResult(RESULT_OK);
								finish();
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
}
