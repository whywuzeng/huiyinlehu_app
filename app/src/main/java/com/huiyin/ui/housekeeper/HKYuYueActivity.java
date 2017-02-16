package com.huiyin.ui.housekeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.constants.Config;
import com.huiyin.ui.housekeeper.HKYuYueActivity.LXBean.Item;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UploadHelper;
import com.huiyin.utils.UploadHelper.UploadCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HKYuYueActivity extends BaseActivity implements OnClickListener {
	private int length = 0;
	private static final String TAG = "HKYuYueActivity";
	protected static final int RESULT_LOAD_IMAGE = 1;
	protected static final int CAMERA = 2;
	private TextView left_ib, middle_title_tv, gast_go, right_ib, newTv, details_content, inputNumber;
	private EditText make_pointment_people, make_pointment_phone, make_pointment_address;
	private ImageView make_pointment_fapiao, make_pointment_mingpai;
	private String wisdom_id, name, phone, address, invoice_img, nameplate_img, remarks;
	private SelectPicPopupWindow menuWindow;
	private Spinner mySpinner;
	private LinearLayout add_iamges;
	private EditText yuyue_beizhu;
	private boolean isSelecd = false;
	// private Drawable drawable;
	private String ABSTRACTING;
	private int shuxingid;
	private List<Dict> dicts;
	private ImageView tv_bingxiang_fapiao, tv_kongtiao_fapiao, tv_tianshi_fapiao, tv_tiannao_fapiao, tv_fengshan_fapiao,
			iv_ic_fapiao, iv_ic_mingpai;
	private ImageView tv_bingxiang_mingpai, tv_kongtiao_mingpai, tv_tianshi_mingpai, tv_tiannao_mingpai, tv_fengshan_mingpai,
			imageView_iv_wenhao;
	private Bitmap curImagePic;
	private String imagePath;
	private Uri uri;
	private Drawable drawable;
	ArrayList<Bitmap> lists = new ArrayList<Bitmap>();
	private RelativeLayout type_yuyue;
	private Context mContent;

	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hk_yuyue_layout);
		mContent = this;
		ABSTRACTING = getIntent().getStringExtra("ABSTRACTING");
		initView();
		initData();
		leiXingHttp();// 请求预约类型接口
	}

	private void initView() {

		invoice_img = "";
		nameplate_img = "";

		details_content = (TextView) findViewById(R.id.details_content);
		inputNumber = (TextView) findViewById(R.id.inputNumber);
		details_content.setText("         " + ABSTRACTING);
		left_ib = (TextView) findViewById(R.id.left_ib);
		right_ib = (TextView) findViewById(R.id.right_ib);
		right_ib.setText("咨询");
		right_ib.setBackgroundResource(R.color.index_red);
		right_ib.setOnClickListener(this);
		left_ib.setOnClickListener(this);
		gast_go = (TextView) findViewById(R.id.mp_fast_go);
		yuyue_beizhu = (EditText) findViewById(R.id.yuyue_beizhu);
		imageView_iv_wenhao = (ImageView) findViewById(R.id.imageView_iv_wenhao);
		imageView_iv_wenhao.setOnClickListener(this);
		gast_go.setOnClickListener(this);
		// add_iamges = (LinearLayout)findViewById(R.id.add_iamges);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		make_pointment_people = (EditText) findViewById(R.id.make_pointment_people);
		make_pointment_phone = (EditText) findViewById(R.id.make_pointment_phone);
		make_pointment_address = (EditText) findViewById(R.id.make_pointment_address);
		make_pointment_fapiao = (ImageView) findViewById(R.id.make_pointment_fapiao);
		type_yuyue = (RelativeLayout) findViewById(R.id.type_yuyue);
		type_yuyue.setOnClickListener(this);
		make_pointment_fapiao.setOnClickListener(this);
		make_pointment_mingpai = (ImageView) findViewById(R.id.make_pointment_mingpai);
		make_pointment_mingpai.setOnClickListener(this);
		yuyue_beizhu.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String content = yuyue_beizhu.getText().toString();
				length = content.length();
				inputNumber.setText(content.length() + "/" + "300字");
				if (length >= 300) {
					Toast.makeText(mContent, "您输入的内容已经到达最大上限，不可再输入！", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});

	}

	// 上传信息到服务器
	private void makePoinment() {

		RequstClient.houseKeepMakePointment(AppContext.getInstance().getUserId(), wisdom_id, name, phone, address, nameplate_img, invoice_img,
				remarks, new CustomResponseHandler(this) {

					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "HkDetailActivity:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(HKYuYueActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
								return;
							} else {
								Toast.makeText(mContext, "预约已成功提交审核！", 1).show();
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

	private void initData() {
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
		tv_kongtiao_fapiao = (ImageView) findViewById(R.id.tv_kongtiao_fapiao);
		tv_kongtiao_fapiao.setOnClickListener(this);
		tv_tianshi_fapiao = (ImageView) findViewById(R.id.tv_tianshi_fapiao);
		tv_tianshi_fapiao.setOnClickListener(this);
		tv_tiannao_fapiao = (ImageView) findViewById(R.id.tv_tiannao_fapiao);
		tv_tiannao_fapiao.setOnClickListener(this);
		tv_fengshan_fapiao = (ImageView) findViewById(R.id.tv_fengshan_fapiao);
		tv_fengshan_fapiao.setOnClickListener(this);
		tv_bingxiang_mingpai = (ImageView) findViewById(R.id.tv_bingxiang_mingpai);
		tv_bingxiang_mingpai.setOnClickListener(this);
		tv_kongtiao_mingpai = (ImageView) findViewById(R.id.tv_kongtiao_mingpai);
		tv_kongtiao_mingpai.setOnClickListener(this);
		tv_tianshi_mingpai = (ImageView) findViewById(R.id.tv_tianshi_mingpai);
		tv_tianshi_mingpai.setOnClickListener(this);
		tv_tiannao_mingpai = (ImageView) findViewById(R.id.tv_tiannao_mingpai);
		tv_tiannao_mingpai.setOnClickListener(this);
		tv_fengshan_mingpai = (ImageView) findViewById(R.id.tv_fengshan_mingpai);
		tv_fengshan_mingpai.setOnClickListener(this);
		tv_bingxiang_fapiao = (ImageView) findViewById(R.id.tv_bingxiang_fapiao);
		tv_bingxiang_fapiao.setOnClickListener(this);
	}

	boolean isSelected = true;
	int flag;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.type_yuyue:
			mySpinner.performClick();
			break;
		case R.id.imageView_iv_wenhao:
			NameplatePopup menuWindow = new NameplatePopup(HKYuYueActivity.this); // 显示窗口
			menuWindow.showAtLocation(HKYuYueActivity.this.findViewById(R.id.main), Gravity.TOP | Gravity.END, 0, 0);
			break;
		case R.id.tv_kongtiao_fapiao:
			Pop();
			flag = 1;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_fengshan_mingpai:
			Pop();
			flag = 2;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_tiannao_mingpai:
			Pop();
			flag = 3;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_tianshi_mingpai:

			Pop();
			flag = 4;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_kongtiao_mingpai:
			Pop();
			flag = 5;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_bingxiang_mingpai:
			Pop();
			flag = 6;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_fengshan_fapiao:
			Pop();
			flag = 7;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_tiannao_fapiao:
			Pop();
			flag = 8;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_tianshi_fapiao:
			Pop();
			flag = 9;
			isSelected = false;
			drawable = null;
			break;
		case R.id.tv_bingxiang_fapiao:
			Pop();
			flag = 10;
			isSelected = false;
			drawable = null;
			break;
		case R.id.left_ib:
			finish();
			break;
		case R.id.mp_fast_go: // 点击提交

			// 跳转到管家列表界面
			wisdom_id = String.valueOf(shuxingid);
			name = make_pointment_people.getText().toString().trim();
			phone = make_pointment_phone.getText().toString().trim();
			address = make_pointment_address.getText().toString().trim();
			remarks = yuyue_beizhu.getText().toString().trim();

			if (name.equals("")) {
				Toast.makeText(this, "请输入您的真实姓名", Toast.LENGTH_LONG).show();
				return;
			}

			if (phone.equals("")) {
				Toast.makeText(this, "请输入您的手机号码！", Toast.LENGTH_LONG).show();
				return;
			}

			if (!phone.matches("1[3|5|7|8|][0-9]{9}")) {
				Toast.makeText(this, "手机号码格式不正确！", Toast.LENGTH_LONG).show();
				return;
			}

			if (address.equals("")) {
				Toast.makeText(this, "地址不能为空！", Toast.LENGTH_LONG).show();
				return;
			}

			if (invoice_img == "" && nameplate_img == "") {
				Toast.makeText(this, "你至少需要上传一张照片", Toast.LENGTH_SHORT).show();
				return;
			} else if (invoice_img.endsWith(",")) {
				invoice_img = invoice_img.substring(0, invoice_img.length() - 1);
			} else if (nameplate_img.endsWith(",")) {
				nameplate_img = nameplate_img.substring(0, nameplate_img.length() - 1);
			}
			if (length == 0) {
				Toast.makeText(mContent, "备注内容不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}

			makePoinment();

			break;

		case R.id.make_pointment_fapiao:

			LinearLayout dongtai_add = (LinearLayout) findViewById(R.id.dongtai_add);

			iv_ic_fapiao = new ImageView(this);
			flag = 11;
			Pop();
			iv_ic_fapiao.setPadding(0, 0, 30, 0);
			dongtai_add.addView(iv_ic_fapiao);

			isSelected = false;
			drawable = null;
			break;

		case R.id.make_pointment_mingpai:
			LinearLayout dongtai_add2 = (LinearLayout) findViewById(R.id.dongtai_add2);

			iv_ic_mingpai = new ImageView(this);
			flag = 12;
			Pop();
			iv_ic_mingpai.setPadding(0, 0, 30, 0);
			dongtai_add2.addView(iv_ic_mingpai);

			isSelected = false;
			drawable = null;

			break;
		case R.id.right_ib:
			String telPhone = PreferenceUtil.getInstance(mContext).getHotLine();
			String regEx = "[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(telPhone);
			telPhone = m.replaceAll("").trim();
			Log.i("解析结果", "====" + telPhone);

			if (StringUtils.isBlank(telPhone))
				return;
			Intent d_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
			d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(d_intent);
			break;
		default:
			break;
		}
	}

	private void Pop() {
		menuWindow = new SelectPicPopupWindow(HKYuYueActivity.this, itemsOnClick); // 显示窗口
		menuWindow.showAtLocation(HKYuYueActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		private String imagePath;

		@SuppressLint("NewApi")
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo: // 拍照

				Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(camera, CAMERA);

				break;
			case R.id.btn_pick_photo:
				Intent intent = new Intent

				// 调用android的图库

				(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, 1);

				break;
			default:
				break;
			}

		}

	};

	private android.graphics.Bitmap bitmap_ca;
	private String picturePath;

	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {

		case RESULT_OK:

			if (requestCode == CAMERA && null != data) {
				String sdState = Environment.getExternalStorageState();
				if (!sdState.equals(Environment.MEDIA_MOUNTED)) {

					return;
				}

				new DateFormat();
				String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
				Bundle bundle = data.getExtras();

				bitmap_ca = (Bitmap) bundle.get("data");
				FileOutputStream fout = null;
				File file = new File("/sdcard/pintu/");
				file.mkdirs();
				String filename = file.getPath() + name;
				try {
					fout = new FileOutputStream(filename);
					bitmap_ca.compress(Bitmap.CompressFormat.JPEG, 100, fout);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						fout.flush();
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (bitmap_ca != null) {
					try {
						curImagePic = bitmap_ca;
						uploadGoodsImage();
						drawable = new BitmapDrawable(bitmap_ca);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}

		if (requestCode == 1) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				uri = Uri.parse(picturePath);
				cursor.close();

				if (uri != null) {
					try {
						Drawable d = Drawable.createFromPath(picturePath);
						BitmapDrawable bd = (BitmapDrawable) d;

						curImagePic = bd.getBitmap();
						uploadGoodsImage();
						drawable = d;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				break;

			}

		}

	}

	/***
	 * 上传图片
	 * 
	 * @param data
	 */

	private void uploadGoodsImage() {
		final String imageName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
		final File savepath = new File(Config.APP_SAVE_PATH + "CacheImage/");

		new Thread(new Runnable() {
			@Override
			public void run() {
				UploadHelper.getInstance().doUpload(savepath.getAbsolutePath(), imageName, curImagePic, "0",
						new UploadCallBack() {
							@Override
							public void onStart(String filePath) {
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										UIHelper.showLoadDialog(HKYuYueActivity.this,
												HKYuYueActivity.this.getString(R.string.tijiao));
									}
								});

							}

							@Override
							public void onFinish(String content) {
								LogUtil.i(TAG, "uploadGoodsImage:" + content);
								try {
									final JSONObject obj = new JSONObject(content);
									String returnId = obj.getString("returnId");
									returnId = TextUtils.isEmpty(returnId) ? "0" : returnId;
									if (Integer.parseInt(returnId) > 0) {
										final String imagePath1 = obj.getString("saveUrl");

										// Toast.makeText(mContext,imagePath ,
										// 1).show();
										if (null != curImagePic) {
											mHandler.post(new Runnable() {
												@Override
												public void run() {
													Toast.makeText(getBaseContext(), "上传成功", Toast.LENGTH_LONG).show();
													UIHelper.cloesLoadDialog();
													if (flag == 11 || flag == 1 || flag == 7 || flag == 8 || flag == 9
															|| flag == 10) {
														invoice_img += imagePath1 + ",";
													} else if (flag == 12 || flag == 2 || flag == 3 || flag == 4 || flag == 5
															|| flag == 6) {
														nameplate_img += imagePath1 + ",";

													}

													switch (flag) {
													case 1:
														LayoutParams Params = (LayoutParams) tv_kongtiao_fapiao.getLayoutParams();
														Params.height = 60;
														Params.width = 60;

														tv_kongtiao_fapiao.setLayoutParams(Params);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_kongtiao_fapiao, options);
														break;
													case 2:
														LayoutParams Params2 = (LayoutParams) tv_fengshan_mingpai
																.getLayoutParams();
														Params2.height = 60;
														Params2.width = 60;
														tv_fengshan_mingpai.setLayoutParams(Params2);
														// tv_fengshan_mingpai
														// .setImageDrawable(drawable);

														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_fengshan_mingpai, options);

														break;
													case 3:
														LayoutParams Params3 = (LayoutParams) tv_tiannao_mingpai
																.getLayoutParams();
														Params3.height = 60;
														Params3.width = 60;
														tv_tiannao_mingpai.setLayoutParams(Params3);
														// tv_tiannao_mingpai
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_tiannao_mingpai, options);

														break;
													case 4:
														LayoutParams Params4 = (LayoutParams) tv_tianshi_mingpai
																.getLayoutParams();
														Params4.height = 60;
														Params4.width = 60;
														tv_tianshi_mingpai.setLayoutParams(Params4);
														// tv_tianshi_mingpai
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_tianshi_mingpai, options);

														break;
													case 5:
														LayoutParams Params5 = (LayoutParams) tv_kongtiao_mingpai
																.getLayoutParams();
														Params5.height = 60;
														Params5.width = 60;

														tv_kongtiao_mingpai.setLayoutParams(Params5);
														// tv_kongtiao_mingpai
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_kongtiao_mingpai, options);

														break;
													case 6:

														LayoutParams Params6 = (LayoutParams) tv_bingxiang_mingpai
																.getLayoutParams();
														Params6.height = 60;
														Params6.width = 60;
														tv_bingxiang_mingpai.setLayoutParams(Params6);
														// tv_bingxiang_mingpai
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_bingxiang_mingpai, options);

														break;
													case 7:

														LayoutParams Params7 = (LayoutParams) tv_fengshan_fapiao
																.getLayoutParams();
														Params7.height = 60;
														Params7.width = 60;
														tv_fengshan_fapiao.setLayoutParams(Params7);
														// tv_fengshan_fapiao
														// .setBackgroundResource(0);
														tv_fengshan_fapiao.setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_fengshan_fapiao, options);

														break;
													case 8:
														LayoutParams Params8 = (LayoutParams) tv_tiannao_fapiao.getLayoutParams();
														Params8.height = 60;
														Params8.width = 60;
														tv_tiannao_fapiao.setLayoutParams(Params8);
														// tv_tiannao_fapiao
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_tiannao_fapiao, options);

														break;
													case 9:

														LayoutParams Params9 = (LayoutParams) tv_tianshi_fapiao.getLayoutParams();
														Params9.height = 60;
														Params9.width = 60;
														tv_tianshi_fapiao.setLayoutParams(Params9);
														//
														// tv_tianshi_fapiao
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_tianshi_fapiao, options);

														break;
													case 10:
														LayoutParams Params10 = (LayoutParams) tv_bingxiang_fapiao
																.getLayoutParams();
														Params10.height = 60;
														Params10.width = 60;
														tv_bingxiang_fapiao.setLayoutParams(Params10);
														// tv_bingxiang_fapiao
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																tv_bingxiang_fapiao, options);

														break;
													case 11:

														LayoutParams Params11 = (LayoutParams) iv_ic_fapiao.getLayoutParams();
														Params11.height = 90;
														Params11.width = 90;
														iv_ic_fapiao.setLayoutParams(Params11);

														// iv_ic_fapiao
														// .setImageDrawable(drawable);
														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																iv_ic_fapiao, options);

														break;

													case 12:
														LayoutParams Params12 = (LayoutParams) iv_ic_mingpai.getLayoutParams();
														Params12.height = 90;
														Params12.width = 90;
														iv_ic_mingpai.setLayoutParams(Params12);

														// iv_ic_mingpai
														// .setImageDrawable(drawable);

														ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath1,
																iv_ic_mingpai, options);

														break;
													default:
														break;
													}
												}
											});

										}

									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onError(final String errorMsg) {
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
							}
						});
			}

		}).start();

	}

	private Handler mHandler = new Handler();

	/**
	 * 
	 * 预约类型接口
	 * 
	 */

	public void leiXingHttp() {

		RequstClient.getType(new CustomResponseHandler(this) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {

					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 成功
						String errorMsg = obj.getString("msg");
						Toast.makeText(HKYuYueActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					mLXBean = new Gson().fromJson(content, LXBean.class);
					dicts = new ArrayList<Dict>();
					for (Item item : mLXBean.widomJson) {
						dicts.add(new Dict(Integer.parseInt(item.ID), item.TITLE));
					}
					spinni();
				} catch (Exception e) {
				}
			}
		});
	}

	public void spinni() {
		mySpinner = (Spinner) findViewById(R.id.mySpinner);
		ArrayAdapter<Dict> accountTypesAdapter = new ArrayAdapter<Dict>(this, android.R.layout.simple_spinner_item, dicts);
		accountTypesAdapter.setDropDownViewResource(R.layout.drop_down_item);
		mySpinner.setAdapter(accountTypesAdapter);
		String id_s = getIntent().getStringExtra("id");
		for (int i = 0; i < dicts.size(); i++) {

			if (dicts.get(i).getId() == Integer.parseInt(id_s)) {
				String tilte = dicts.get(i).getText();
				middle_title_tv.setText(tilte + "预约");
				mySpinner.setSelection(i);
			}
		}
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final int arg = position;
				shuxingid = dicts.get(arg).getId();

				TextView tv = (TextView) arg1;
				tv.setTextColor(getResources().getColor(R.color.black)); // 设置颜色

				tv.setTextSize(15.0f);

				tt = mySpinner.getItemAtPosition(position).toString();
				middle_title_tv.setText(tt + "预约");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	String tt;

	public class LXBean {
		public ArrayList<Item> widomJson = new ArrayList<Item>();

		public class Item {
			public String ID;
			public String TITLE;
		}
	}

	public LXBean mLXBean;
}
