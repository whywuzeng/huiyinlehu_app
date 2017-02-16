package com.huiyin.ui.housekeeper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.ImageViewWithDel;
import com.huiyin.wight.ImageViewWithDel.MyImageViewCallBack;

public class HouseKeeperOrderActivity extends BaseActivity {

	// Content View Elements
	private TextView left_ib;
	private TextView middle_title_tv;
	private TextView right_ib;
	private TextView details_content;
	private TextView main;
	// private LinearLayout linearLayout1;
	private Spinner mySpinner;
	private RelativeLayout type_yuyue;
	// private ImageView imageView1;
	private EditText make_pointment_people;
	private EditText make_pointment_phone;
	private EditText make_pointment_address;
	private ImageViewWithDel imageViewWithDel1;
	private ImageViewWithDel imageViewWithDel2;
	private ImageViewWithDel imageViewWithDel3;
	private ImageViewWithDel imageViewWithDel4;
	private ImageViewWithDel imageViewWithDel5;
	// private HorizontalScrollView horizontalScrollView1;
	private LinearLayout dongtai_add;
	private ImageView make_pointment_fapiao;
	// private ImageView imageView_iv_wenhao;
	private ImageViewWithDel imageViewWithDel6;
	private ImageViewWithDel imageViewWithDel7;
	private ImageViewWithDel imageViewWithDel8;
	private ImageViewWithDel imageViewWithDel9;
	private ImageViewWithDel imageViewWithDel10;
	// private HorizontalScrollView horizontalScrollView2;
	private LinearLayout dongtai_add2;
	private ImageView make_pointment_mingpai;
	private EditText yuyue_beizhu;
	private TextView inputNumber;
	private Button mp_fast_go;

	// End Of Content View Elements
	private String abstracting;
	private String wisdom_id, name, phone, address, invoice_img, nameplate_img, remarks;

	private WidomListBean bean;

	private int photoFlag;
	private int CAMERA_WITH_DATA = 0x11;
	private int CAMERA_PICK_PHOTO = 0x21;
	private MyImageViewCallBack ImageViewDel, fapiaoImageViewDel;
	private File imageFile;
	private ImageUpload mIUpload, fapiaoIUpload;
	private ArrayList<String> hostimageurls, fapiaohostimageurls;
	private ArrayList<String> netimageurls, fapiaonetimageurls;
	private int countImage, fapiaoCountImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.housekeeper_order_layout);
		Intent intent = getIntent();
		abstracting = intent.getStringExtra("ABSTRACTING");
		wisdom_id = intent.getStringExtra("id");
		countImage = 0;
		fapiaoCountImage = 0;
		findView();
		setListener();
		InitData();
	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		right_ib = (TextView) findViewById(R.id.right_ib);
		right_ib.setText("咨询");
		right_ib.setBackgroundResource(R.color.index_red);

		details_content = (TextView) findViewById(R.id.details_content);
		details_content.setText(Html.fromHtml(abstracting));

		main = (TextView) findViewById(R.id.main);
		// linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		mySpinner = (Spinner) findViewById(R.id.mySpinner);
		type_yuyue = (RelativeLayout) findViewById(R.id.type_yuyue);
		// imageView1 = (ImageView) findViewById(R.id.imageView1);
		make_pointment_people = (EditText) findViewById(R.id.make_pointment_people);
		make_pointment_phone = (EditText) findViewById(R.id.make_pointment_phone);
		make_pointment_address = (EditText) findViewById(R.id.make_pointment_address);

		imageViewWithDel1 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel1);
		Drawable dr1 = getResources().getDrawable(R.drawable.ic_bing_ing_l);
		dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
		imageViewWithDel1.setImage(dr1);
		imageViewWithDel2 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel2);
		Drawable dr2 = getResources().getDrawable(R.drawable.ic_kongtiao_l);
		dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
		imageViewWithDel2.setImage(dr2);
		imageViewWithDel3 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel3);
		Drawable dr3 = getResources().getDrawable(R.drawable.ic_dianshi_l);
		dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
		imageViewWithDel3.setImage(dr3);
		imageViewWithDel4 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel4);
		Drawable dr4 = getResources().getDrawable(R.drawable.ic_dinanao_l);
		dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
		imageViewWithDel4.setImage(dr4);
		imageViewWithDel5 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel5);
		Drawable dr5 = getResources().getDrawable(R.drawable.ic_fengshan_);
		dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
		imageViewWithDel5.setImage(dr5);

		// horizontalScrollView1 = (HorizontalScrollView)
		// findViewById(R.id.horizontalScrollView1);
		dongtai_add = (LinearLayout) findViewById(R.id.dongtai_add);
		make_pointment_fapiao = (ImageView) findViewById(R.id.make_pointment_fapiao);
		// imageView_iv_wenhao = (ImageView)
		// findViewById(R.id.imageView_iv_wenhao);

		imageViewWithDel6 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel6);
		imageViewWithDel6.setImage(dr1);
		imageViewWithDel7 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel7);
		imageViewWithDel7.setImage(dr2);
		imageViewWithDel8 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel8);
		imageViewWithDel8.setImage(dr3);
		imageViewWithDel9 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel9);
		imageViewWithDel9.setImage(dr4);
		imageViewWithDel10 = (ImageViewWithDel) findViewById(R.id.imageViewWithDel10);
		imageViewWithDel10.setImage(dr5);

		// horizontalScrollView2 = (HorizontalScrollView)
		// findViewById(R.id.horizontalScrollView2);
		dongtai_add2 = (LinearLayout) findViewById(R.id.dongtai_add2);
		make_pointment_mingpai = (ImageView) findViewById(R.id.make_pointment_mingpai);
		yuyue_beizhu = (EditText) findViewById(R.id.yuyue_beizhu);
		inputNumber = (TextView) findViewById(R.id.inputNumber);
		mp_fast_go = (Button) findViewById(R.id.mp_fast_go);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		right_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
			}
		});

		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				wisdom_id = String.valueOf(bean.getWidomJson().get(position).getID());
				middle_title_tv.setText(bean.getWidomJson().get(position).getTITLE() + "预约");
				TextView tv = (TextView) view;
				tv.setTextColor(getResources().getColor(R.color.black)); // 设置颜色
				tv.setTextSize(15.0f);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		type_yuyue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mySpinner.performClick();
			}
		});

		imageViewWithDel1.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 11;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel1.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_bing_ing_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel1.setImage(dr1);
				if (--countImage < 0) {
					countImage = 0;
				}
			}
		});
		imageViewWithDel2.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 12;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel2.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_kongtiao_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel2.setImage(dr1);
				if (--countImage < 0) {
					countImage = 0;
				}
			}
		});
		imageViewWithDel3.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 13;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel3.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_dianshi_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel3.setImage(dr1);
				if (--countImage < 0) {
					countImage = 0;
				}
			}
		});
		imageViewWithDel4.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 14;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel4.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_dinanao_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel4.setImage(dr1);
				if (--countImage < 0) {
					countImage = 0;
				}
			}
		});
		imageViewWithDel5.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 15;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel5.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_fengshan_);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel5.setImage(dr1);
				if (--countImage < 0) {
					countImage = 0;
				}
			}
		});
		imageViewWithDel6.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 21;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel6.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_bing_ing_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel6.setImage(dr1);
				if (--fapiaoCountImage < 0) {
					fapiaoCountImage = 0;
				}
			}
		});
		imageViewWithDel7.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 22;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel7.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_kongtiao_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel7.setImage(dr1);
				if (--fapiaoCountImage < 0) {
					fapiaoCountImage = 0;
				}
			}
		});
		imageViewWithDel8.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 23;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel8.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_dianshi_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel8.setImage(dr1);
				if (--fapiaoCountImage < 0) {
					fapiaoCountImage = 0;
				}
			}
		});
		imageViewWithDel9.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 24;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel9.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_dinanao_l);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel9.setImage(dr1);
				if (--fapiaoCountImage < 0) {
					fapiaoCountImage = 0;
				}
			}
		});
		imageViewWithDel10.setCallback(new MyImageViewCallBack() {

			@Override
			public void imgClick() {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 25;
				showpopwindow();
			}

			@Override
			public void del(View parent) {
				imageViewWithDel10.setVisibility(View.GONE);
				Drawable dr1 = getResources().getDrawable(R.drawable.ic_fengshan_);
				dr1.setBounds(0, 0, DensityUtil.dip2px(mContext, 50), DensityUtil.dip2px(mContext, 50));
				imageViewWithDel10.setImage(dr1);
				if (--fapiaoCountImage < 0) {
					fapiaoCountImage = 0;
				}
			}
		});
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
		fapiaoImageViewDel = new MyImageViewCallBack() {

			@Override
			public void imgClick() {

			}

			@Override
			public void del(View v) {
				ViewGroup parent = (ViewGroup) v.getParent();
				if (parent != null) {
					parent.removeView(v);
					if (--fapiaoCountImage < 0) {
						fapiaoCountImage = 0;
					}
				}
			}
		};
		make_pointment_fapiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (countImage >= 5) {
					Toast.makeText(mContext, "发票一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 30;
				showpopwindow();
			}
		});

		make_pointment_mingpai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fapiaoCountImage >= 5) {
					Toast.makeText(mContext, "凭证一次仅支持上传5张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				photoFlag = 40;
				showpopwindow();
			}
		});
		yuyue_beizhu.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				inputNumber.setText(s.length() + "/" + "300字");
				if (s.length() >= 300) {
					Toast.makeText(mContext, "您输入的内容已经到达最大上限，不可再输入！", Toast.LENGTH_SHORT).show();
					s.subSequence(0, 299);
					return;
				}
			}
		});
		mp_fast_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = make_pointment_people.getText().toString().trim();
				phone = make_pointment_phone.getText().toString().trim();
				address = make_pointment_address.getText().toString().trim();
				remarks = yuyue_beizhu.getText().toString().trim();

				if (StringUtils.isBlank(name)) {
					Toast.makeText(mContext, "请输入您的真实姓名", Toast.LENGTH_LONG).show();
					return;
				}

				if (StringUtils.isBlank(phone)) {
					Toast.makeText(mContext, "请输入您的手机号码！", Toast.LENGTH_LONG).show();
					return;
				}

				if (!StringUtils.isPhoneNumber(phone)) {
					Toast.makeText(mContext, "手机号码格式不正确！", Toast.LENGTH_LONG).show();
					return;
				}

				if (StringUtils.isBlank(address)) {
					Toast.makeText(mContext, "地址不能为空！", Toast.LENGTH_LONG).show();
					return;
				}

				if (StringUtils.isBlank(remarks)) {
					Toast.makeText(mContext, "备注内容不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}

				hostimageurls = new ArrayList<String>();
				if (imageViewWithDel1.getImagePath() != null) {
					hostimageurls.add(imageViewWithDel1.getImagePath());
				}
				if (imageViewWithDel2.getImagePath() != null) {
					hostimageurls.add(imageViewWithDel2.getImagePath());
				}
				if (imageViewWithDel3.getImagePath() != null) {
					hostimageurls.add(imageViewWithDel3.getImagePath());
				}
				if (imageViewWithDel4.getImagePath() != null) {
					hostimageurls.add(imageViewWithDel4.getImagePath());
				}
				if (imageViewWithDel5.getImagePath() != null) {
					hostimageurls.add(imageViewWithDel5.getImagePath());
				}
				if (dongtai_add.getChildCount() > 0) {
					for (int i = 0; i < dongtai_add.getChildCount(); i++) {
						ImageViewWithDel temp = (ImageViewWithDel) dongtai_add.getChildAt(i);
						hostimageurls.add(temp.getImagePath());
					}
				}

				fapiaohostimageurls = new ArrayList<String>();
				if (imageViewWithDel6.getImagePath() != null) {
					fapiaohostimageurls.add(imageViewWithDel6.getImagePath());
				}
				if (imageViewWithDel7.getImagePath() != null) {
					fapiaohostimageurls.add(imageViewWithDel7.getImagePath());
				}
				if (imageViewWithDel8.getImagePath() != null) {
					fapiaohostimageurls.add(imageViewWithDel8.getImagePath());
				}
				if (imageViewWithDel9.getImagePath() != null) {
					fapiaohostimageurls.add(imageViewWithDel9.getImagePath());
				}
				if (imageViewWithDel10.getImagePath() != null) {
					fapiaohostimageurls.add(imageViewWithDel10.getImagePath());
				}
				if (dongtai_add2.getChildCount() > 0) {
					for (int i = 0; i < dongtai_add2.getChildCount(); i++) {
						ImageViewWithDel temp = (ImageViewWithDel) dongtai_add2.getChildAt(i);
						fapiaohostimageurls.add(temp.getImagePath());
					}
				}
				if (hostimageurls.size() < 1 && fapiaohostimageurls.size() < 1) {
					Toast.makeText(mContext, "请最少上传一张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				UpLoadImage();
			}
		});
	}

	private void InitData() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				bean = WidomListBean.explainJson(content, mContext);
				if (bean.type > 0 && bean.getWidomJson() != null && bean.getWidomJson().size() > 0) {
					InitSpinner(bean.getWidomJson());
				}
			}
		};
		RequstClient.getType(handler);
	}

	protected void InitSpinner(List<WidomItem> widomJson) {
		ArrayAdapter<WidomItem> accountTypesAdapter = new ArrayAdapter<WidomItem>(this, android.R.layout.simple_spinner_item,
				widomJson);
		accountTypesAdapter.setDropDownViewResource(R.layout.drop_down_item);
		mySpinner.setAdapter(accountTypesAdapter);

		for (int i = 0; i < widomJson.size(); i++) {
			if (widomJson.get(i).getID() == MathUtil.stringToInt(wisdom_id)) {
				mySpinner.setSelection(i);
			}
		}
	}

	private SelectPicPopupWindow mPopupWindow = null;

	private void showpopwindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new SelectPicPopupWindow(mContext, new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mPopupWindow.isShowing()) {
						mPopupWindow.dismiss();
					}
					switch (v.getId()) {
					case R.id.btn_take_photo: // 拍照
						callCamerImage();
						break;
					case R.id.btn_pick_photo:
						callLocalImage();
						break;
					}
				}
			});
		}
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAtLocation(main, Gravity.BOTTOM, 0, 0);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		Bitmap bm;
		switch (requestCode) {
		case 0x11:
			bm = BitmapUtils.showimageFull(imageFile.getPath(), 50, 50);
			showimage(bm);
			break;
		case 0x21:
			Uri selectedImage = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			bm = BitmapUtils.showimageFull(picturePath, 50, 50);
			showimage(bm, picturePath);
			// try {
			// bm = MediaStore.Images.Media.getBitmap(resolver, selectedImage);
			// bm = BitmapUtils.zoomBitmap(bm, 50, 50);
			// showimage(bm, picturePath);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			break;
		}
	}

	public void showimage(Bitmap bm) {
		switch (photoFlag) {
		case 11:
			imageViewWithDel1.setImage(bm, imageFile.getPath());
			imageViewWithDel1.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 12:
			imageViewWithDel2.setImage(bm, imageFile.getPath());
			imageViewWithDel2.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 13:
			imageViewWithDel3.setImage(bm, imageFile.getPath());
			imageViewWithDel3.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 14:
			imageViewWithDel4.setImage(bm, imageFile.getPath());
			imageViewWithDel4.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 15:
			imageViewWithDel5.setImage(bm, imageFile.getPath());
			imageViewWithDel5.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 21:
			imageViewWithDel6.setImage(bm, imageFile.getPath());
			imageViewWithDel6.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 22:
			imageViewWithDel7.setImage(bm, imageFile.getPath());
			imageViewWithDel7.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 23:
			imageViewWithDel8.setImage(bm, imageFile.getPath());
			imageViewWithDel8.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 24:
			imageViewWithDel9.setImage(bm, imageFile.getPath());
			imageViewWithDel9.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 25:
			imageViewWithDel10.setImage(bm, imageFile.getPath());
			imageViewWithDel10.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 30:
			ImageViewWithDel temp = new ImageViewWithDel(mContext);
			temp.setImage(bm, imageFile.getPath());
			temp.setVisibility(View.VISIBLE);
			temp.setCallback(ImageViewDel);
			dongtai_add.addView(temp);
			countImage++;
			break;
		case 40:
			ImageViewWithDel temp1 = new ImageViewWithDel(mContext);
			temp1.setImage(bm, imageFile.getPath());
			temp1.setVisibility(View.VISIBLE);
			temp1.setCallback(fapiaoImageViewDel);
			dongtai_add2.addView(temp1);
			fapiaoCountImage++;
			break;
		default:
			break;
		}
	}

	public void showimage(Bitmap bm, String path) {
		switch (photoFlag) {
		case 11:
			imageViewWithDel1.setImage(bm, path);
			imageViewWithDel1.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 12:
			imageViewWithDel2.setImage(bm, path);
			imageViewWithDel2.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 13:
			imageViewWithDel3.setImage(bm, path);
			imageViewWithDel3.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 14:
			imageViewWithDel4.setImage(bm, path);
			imageViewWithDel4.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 15:
			imageViewWithDel5.setImage(bm, path);
			imageViewWithDel5.setVisibility(View.VISIBLE);
			countImage++;
			break;
		case 21:
			imageViewWithDel6.setImage(bm, path);
			imageViewWithDel6.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 22:
			imageViewWithDel7.setImage(bm, path);
			imageViewWithDel7.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 23:
			imageViewWithDel8.setImage(bm, path);
			imageViewWithDel8.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 24:
			imageViewWithDel9.setImage(bm, path);
			imageViewWithDel9.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 25:
			imageViewWithDel10.setImage(bm, path);
			imageViewWithDel10.setVisibility(View.VISIBLE);
			fapiaoCountImage++;
			break;
		case 30:
			ImageViewWithDel temp = new ImageViewWithDel(mContext);
			temp.setImage(bm, path);
			temp.setVisibility(View.VISIBLE);
			temp.setCallback(ImageViewDel);
			dongtai_add.addView(temp);
			countImage++;
			break;
		case 40:
			ImageViewWithDel temp1 = new ImageViewWithDel(mContext);
			temp1.setImage(bm, path);
			temp1.setVisibility(View.VISIBLE);
			temp1.setCallback(fapiaoImageViewDel);
			dongtai_add2.addView(temp1);
			fapiaoCountImage++;
			break;
		default:
			break;
		}
	}

	protected void UpLoadImage() {
		if ((fapiaohostimageurls != null && fapiaohostimageurls.size() > 0)
				&& (hostimageurls != null && hostimageurls.size() > 0)) {
			fapiaoIUpload = new ImageUpload(mContext, fapiaohostimageurls, new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> netimageurls) {
					fapiaonetimageurls = netimageurls;
					makePoinment();
				}

				@Override
				public void UpLoadFail() {

				}
			});
			mIUpload = new ImageUpload(mContext, hostimageurls, new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> imageurls) {
					netimageurls = imageurls;
					if (fapiaoIUpload != null)
						fapiaoIUpload.startLoad();
				}

				@Override
				public void UpLoadFail() {

				}
			});
			mIUpload.startLoad();
			return;
		}
		if ((fapiaohostimageurls == null || fapiaohostimageurls.size() == 0)
				&& (hostimageurls != null && hostimageurls.size() > 0)) {
			mIUpload = new ImageUpload(mContext, hostimageurls, new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> imageurls) {
					netimageurls = imageurls;
					makePoinment();
				}

				@Override
				public void UpLoadFail() {

				}
			});
			mIUpload.startLoad();
			return;
		}
		if ((fapiaohostimageurls != null && fapiaohostimageurls.size() > 0)
				&& (hostimageurls == null || hostimageurls.size() == 0)) {
			fapiaoIUpload = new ImageUpload(mContext, fapiaohostimageurls, new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> netimageurls) {
					fapiaonetimageurls = netimageurls;
					makePoinment();
				}

				@Override
				public void UpLoadFail() {

				}
			});
			fapiaoIUpload.startLoad();
			return;
		}
	}

	private void makePoinment() {
		
		nameplate_img = "";
		if (netimageurls != null && netimageurls.size() > 0) {
			for (int i = 0; i < netimageurls.size(); i++) {
				nameplate_img += netimageurls.get(i);
				nameplate_img += ",";
			}
			nameplate_img = nameplate_img.substring(0, nameplate_img.length() - 1);
		}
		invoice_img = "";
		if (fapiaonetimageurls != null && fapiaonetimageurls.size() > 0) {
			for (int i = 0; i < fapiaonetimageurls.size(); i++) {
				invoice_img += fapiaonetimageurls.get(i);
				invoice_img += ",";
			}
			invoice_img = invoice_img.substring(0, invoice_img.length() - 1);
		}
		//由于个人疏忽，令发票和铭牌的图片集合弄反了，现将其修改过来。
		RequstClient.houseKeepMakePointment(AppContext.getInstance().getUserId(), wisdom_id, name, phone, address,invoice_img, nameplate_img, 
				remarks, new MyCustomResponseHandler(mContext, true) {

					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
								return;
							} else {
								Toast.makeText(mContext, "预约已成功提交审核！", Toast.LENGTH_SHORT).show();
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
