package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.shoppingcar.DuteSelectActivity;
import com.huiyin.ui.user.YuYueShenQingActivity.YuYueBean.PartItem;
import com.huiyin.ui.user.YuYueShenQingActivity.YuYueBean.TypeItem;
import com.huiyin.ui.user.YuYueShenQingActivity.YuYueBean.YuYueItem;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YuYueShenQingActivity extends BaseActivity {

	private final static String TAG = "YuYueShenQingActivity";
	Button yuyue_commit_btn;
	TextView left_ib, middle_title_tv;
	// 预约类型
	LinearLayout shenqin_anzhuan_tv, shenqin_weihu_tv;
	Drawable img_drawable;
	YuYueBean mYuYueBean;
	// 添加商品、配件、预约类型布局
	LinearLayout yuyue_add_good_ly, peijian_add_ly, yuyue_type_add_ly;
	// 预约类型、配件布局
	LinearLayout sq_type_layout, sq_peijian_layout;

	private LayoutInflater inflater;
	public ArrayList<PartItem> mPartsList = new ArrayList<PartItem>();
	// 电话、联系人、详细地址、备注
	String phone, contact, addr_detail, remarks;
	// 开始时间、结束时间
	String startTime, endTime;
	// 电话、联系人、详细地址
	EditText sh_phone, sh_contact, sh_addr_detail;
	// 备注
	EditText sh_remark;
	// 关联订单号
	EditText sq_order_num;
	private Toast mToast;
	// 搜索订单号、用户id、配件ids,预约类型,商品id,订单id,商品数量
	String orderCode, userId, partIds, type_id, commodity_id, orderId,
			commodity_num;
	// 输入的字数提醒
	TextView sh_remark_tip;
	// 搜索
	ImageView sq_icon_search;
	// 预约时间tv
	TextView yuyue_time;
	// 提交备注信息的字数的长度
	private int length = 0;
	private String order_num;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shenqingyuyue_layout);

		initView();
		initData();

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
		middle_title_tv.setText("预约申请");

		userId = AppContext.getInstance().getUserId();

		sq_order_num = (EditText) findViewById(R.id.sq_order_num);
		// 获取订单编号
		if (getIntent().hasExtra("orderCode")) {
			orderCode = getIntent().getStringExtra("orderCode");
			sq_order_num.setText(orderCode);
		}

		yuyue_time = (TextView) findViewById(R.id.yuyue_time);
		yuyue_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(YuYueShenQingActivity.this,
						DuteSelectActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
						DuteSelectActivity.REQUESET_CODE);
			}
		});

		yuyue_commit_btn = (Button) findViewById(R.id.yuyue_commit_btn);
		yuyue_commit_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkInfo()) {
					postCommitInfo();
				}
			}
		});

		sh_remark_tip = (TextView) findViewById(R.id.sh_remark_tip);
		sh_phone = (EditText) findViewById(R.id.sh_phone);
		sh_contact = (EditText) findViewById(R.id.sh_contact);
		sh_addr_detail = (EditText) findViewById(R.id.sh_addr_detail);
		sh_remark = (EditText) findViewById(R.id.sh_remark);
		sh_remark.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				content = sh_remark.getText().toString();
				sh_remark_tip.setText(content.length() + "/300字");
				length = content.length();
				// 当字数超过300时设置文本为不可编辑状态
				if (length >= 300) {
					showToast(R.string.yuyue_content_over_length);
					sh_remark.setFreezesText(true);
					return;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		sq_icon_search = (ImageView) findViewById(R.id.sq_icon_search);
		sq_icon_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				orderCode = sq_order_num.getText().toString();
				if (orderCode.equals("")) {
					Toast.makeText(YuYueShenQingActivity.this, "订单编号不能为空！",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!orderCode.matches("[L][H]\\d{19}$")) {
					showToast(R.string.yuyue_order_num_format);
					return;
				} else {
					initData();
				}
			}
		});

		img_drawable = getResources().getDrawable(R.drawable.icon_choice);
		img_drawable.setBounds(0, 0, img_drawable.getMinimumWidth(),
				img_drawable.getMinimumHeight());

		yuyue_add_good_ly = (LinearLayout) findViewById(R.id.yuyue_add_good_ly);
		peijian_add_ly = (LinearLayout) findViewById(R.id.peijian_add_ly);
		yuyue_type_add_ly = (LinearLayout) findViewById(R.id.yuyue_type_add_ly);

		sq_type_layout = (LinearLayout) findViewById(R.id.sq_type_layout);
		sq_peijian_layout = (LinearLayout) findViewById(R.id.sq_peijian_layout);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DuteSelectActivity.REQUESET_CODE
				&& resultCode == RESULT_OK) {
			if (data != null) {
				String time = data.getStringExtra("duteTime");
				yuyue_time.setText(time);
				if (time.contains("~")) {
					String[] times = time.split("~");
					startTime = times[0];
					endTime = times[1];
				}
			}
		}
	}

	// 各个文本内容的校验
	private boolean checkInfo() {

		phone = sh_phone.getText().toString();
		contact = sh_contact.getText().toString();
		addr_detail = sh_addr_detail.getText().toString();
		remarks = sh_remark.getText().toString();
		order_num = sq_order_num.getText().toString();
		content = sh_remark.getText().toString();
		// 订单号
		if (order_num.equals("")) {
			showToast(R.string.yuyue_order_num_regx);
			return false;
		}
		if (!order_num.matches("[L][H]\\d{19}$")) {
			showToast(R.string.yuyue_order_num_format);
			return false;
		}
		// 预约时间
		String time = yuyue_time.getText().toString().trim();
		if (time.equals("")) {
			showToast(R.string.yuyeu_tiem);
			return false;
		}
		// 联系人电话
		if (TextUtils.isEmpty(phone)) {
			showToast(R.string.phone_is_null);
			return false;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			showToast(R.string.yuyue_phone_number_regx);
			return false;
		}
		// 联系人
		else if (TextUtils.isEmpty(contact)) {
			showToast(R.string.contact_is_null);
			return false;

			/*
			 * 1、一个正则表达式，只含有汉字、数字、字母、下划线不能以下划线开头和结尾 2、长度从2-15之间
			 * ^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{2,15}$
			 */
		} else if (!contact
				.matches("[-\\w\u4e00-\u9fa5]{1,20}")) {
			Toast.makeText(mContext, "用户名不符合命名规则，请重新输入！", Toast.LENGTH_SHORT)
					.show();
			sh_contact.setText("");
			return false;
		}
		// 联系地址
		else if (TextUtils.isEmpty(addr_detail)) {
			showToast(R.string.addr_is_null);
			return false;
		}
		// 备注信息
		else if (content.equals("")) {
			Toast.makeText(mContext, "备注信息不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		// 订单id和商品id
		else if (orderId == null || commodity_id == null) {
			showToast(R.string.commodity_is_null);
			return false;
		}
		return true;
	}

	private void showToast(int resId) {

		if (mToast == null) {
			mToast = Toast
					.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	/**
	 * 提交预约信息
	 */

	private void postCommitInfo() {

		ArrayList<PartItem> partsList = new ArrayList<PartItem>();
		partIds = "";
		if (mPartsList != null) {
			for (int i = 0; i < mPartsList.size(); i++) {
				PartItem item = mPartsList.get(i);
				if (item.isCheck) {
					partsList.add(item);
				}
			}
			for (int j = 0; j < partsList.size(); j++) {
				PartItem item = partsList.get(j);
				if (j == partsList.size() - 1) {
					partIds += item.ID;
				} else {
					partIds += item.ID + ",";
				}
			}
		}

//		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {
//
//			@Override
//			public void onRefreshData(String content) {
//				super.onRefreshData(content);
//				LogUtil.i(TAG, "postCommitYuYue:" + content);
//				try {
//					JSONObject obj = new JSONObject(content);
//					if (!obj.getString("type").equals("1")) {
//						String errorMsg = obj.getString("msg");
//						Toast.makeText(getBaseContext(), errorMsg,
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
//
//					Toast.makeText(getBaseContext(),
//							"您的预约已成功提交，我们会尽快审核", Toast.LENGTH_SHORT)
//							.show();
//					MyOrderActivity.setFlush(true);
//					MyOrderActivity.setBackList(MyOrderActivity.third);
//					finish();
//
//				} catch (JsonSyntaxException e) {
//					e.printStackTrace();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		};
//		RequstClient.appAddBespeak(orderId, type_id, phone, contact,
//				addr_detail, remarks, userId, startTime, endTime, partIds,handler);
		RequstClient.postCommitYuYue(orderId, type_id, phone, contact,
				addr_detail, remarks, userId, startTime, endTime, partIds,
				commodity_id, commodity_num, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postCommitYuYue:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(getBaseContext(),
									"您的预约已成功提交，我们会尽快审核", Toast.LENGTH_SHORT)
									.show();
							MyOrderActivity.setFlush(true);
							MyOrderActivity.setBackList(MyOrderActivity.third);
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

	/**
	 * 再次请求前情况界面控件
	 */
	private void clearLayout() {
		yuyue_add_good_ly.setVisibility(View.VISIBLE);
		sq_type_layout.setVisibility(View.VISIBLE);
		sq_peijian_layout.setVisibility(View.VISIBLE);

		yuyue_add_good_ly.removeAllViews();
		peijian_add_ly.removeAllViews();
		yuyue_type_add_ly.removeAllViews();
	}

	/**
	 * 数据初始化
	 */
	private void initData() {

		if (orderCode == null) {
			return;
		}

		RequstClient.postYuYueInfo(orderCode, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postYuYueInfo:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					mYuYueBean = new Gson().fromJson(content, YuYueBean.class);
					clearLayout();
					refreshUI();

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
	 * 刷新预约类型
	 */
	private void refreshTypeUI() {

		for (int i = 0; i < mYuYueBean.reservationTypeList.size(); i++) {

			final TypeItem item = mYuYueBean.reservationTypeList.get(i);
			View type_view = inflater.inflate(R.layout.yuyue_type_item, null);
			final TextView apply_install_tv = (TextView) type_view
					.findViewById(R.id.apply_install_tv);
			apply_install_tv.setText(item.RESERVATIONTYP_NAME);
			if (i == 0) {
				apply_install_tv.setCompoundDrawables(null, null, img_drawable,
						null);
				type_id = item.RESERVATIONTYP_ID;
			}
			apply_install_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					for (int j = 0; j < yuyue_type_add_ly.getChildCount(); j++) {
						View view = yuyue_type_add_ly.getChildAt(j);
						TextView install_tv = (TextView) view
								.findViewById(R.id.apply_install_tv);
						install_tv.setCompoundDrawables(null, null, null, null);
					}
					apply_install_tv.setCompoundDrawables(null, null,
							img_drawable, null);
					type_id = item.RESERVATIONTYP_ID;
				}
			});

			yuyue_type_add_ly.addView(type_view);
		}

	}

	/***
	 * 刷新商品布局
	 */
	private void refreshGoodsUI() {

		if (mYuYueBean.bespeakList == null || mYuYueBean.bespeakList.size() < 1) {
			yuyue_add_good_ly.setVisibility(View.GONE);
			sq_peijian_layout.setVisibility(View.GONE);
			return;
		}

		for (int i = 0; i < mYuYueBean.bespeakList.size(); i++) {

			final YuYueItem item = mYuYueBean.bespeakList.get(i);
			View good_item = inflater.inflate(R.layout.yuyue_goods_item, null);
			ImageView orderlist_img = (ImageView) good_item
					.findViewById(R.id.orderlist_img);
			TextView orderlist_good_title = (TextView) good_item
					.findViewById(R.id.orderlist_good_title);
			TextView orderlist_good_color = (TextView) good_item
					.findViewById(R.id.orderlist_good_color);
			TextView orderlist_good_price = (TextView) good_item
					.findViewById(R.id.orderlist_good_price);
			TextView orderlist_good_num = (TextView) good_item
					.findViewById(R.id.orderlist_good_num);
			final ImageView img_choice = (ImageView) good_item
					.findViewById(R.id.img_choice);

			orderlist_good_title.setText(item.COMMODITY_NAME);
			orderlist_good_color.setText(item.SPECVALUE);
			if (!(item.price == null))
				orderlist_good_price.setText(MathUtil
						.priceForAppWithSign(item.price));
			orderlist_good_num.setText("数量：" + item.num);
			ImageLoader.getInstance().displayImage(
					URLs.IMAGE_URL + item.COMMODITY_IMAGE_PATH, orderlist_img);
			if (i == 0) {
				commodity_id = item.COMMODITY_ID;
				img_choice.setVisibility(View.VISIBLE);
				mPartsList = item.partsList;
				refreshPartsUI();
				orderId = item.ORDER_ID;
				commodity_num = item.num;
			}
			good_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					for (int j = 0; j < yuyue_add_good_ly.getChildCount(); j++) {
						View view = yuyue_add_good_ly.getChildAt(j);
						ImageView img_choice = (ImageView) view
								.findViewById(R.id.img_choice);
						img_choice.setVisibility(View.INVISIBLE);
					}
					commodity_id = item.COMMODITY_ID;
					img_choice.setVisibility(View.VISIBLE);
					mPartsList = item.partsList;
					commodity_num = item.num;
					refreshPartsUI();
				}
			});
			yuyue_add_good_ly.addView(good_item);

		}

	}

	/***
	 * 刷新配件布局
	 */
	private void refreshPartsUI() {
		if (mPartsList == null || mPartsList.size() < 1) {
			sq_peijian_layout.setVisibility(View.GONE);
			return;
		}

		for (int i = 0; i < mPartsList.size(); i++) {

			final PartItem item = mPartsList.get(i);
			View good_part_item = inflater.inflate(R.layout.good_part_item,
					null);
			CheckBox part_good_check = (CheckBox) good_part_item
					.findViewById(R.id.part_good_check);
			TextView part_good_tv = (TextView) good_part_item
					.findViewById(R.id.part_good_tv);
			part_good_tv.setText(item.NAME);
			part_good_check
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							item.isCheck = arg1;
						}
					});
			peijian_add_ly.addView(good_part_item);
		}
	}

	/**
	 * 
	 * 刷新联系信息
	 */
	private void refreshConsigneeUI() {
		if (mYuYueBean.consignee != null
				&& mYuYueBean.consignee.CONSIGNEE_NAME != null) {
			sh_contact.setText(mYuYueBean.consignee.CONSIGNEE_NAME);
		} else {
			sh_contact.setText("");
		}
		if (mYuYueBean.consignee != null
				&& mYuYueBean.consignee.CONSIGNEE_PHONE != null) {
			sh_phone.setText(mYuYueBean.consignee.CONSIGNEE_PHONE);
		} else {
			sh_phone.setText("");
		}
		if (mYuYueBean.consignee != null
				&& mYuYueBean.consignee.CONSIGNEE_ADDRESS != null) {
			sh_addr_detail.setText(mYuYueBean.consignee.CONSIGNEE_ADDRESS);
		} else {
			sh_addr_detail.setText("");
		}
	}

	/***
	 * 刷新界面
	 */
	private void refreshUI() {

		refreshTypeUI();
		refreshGoodsUI();
		refreshConsigneeUI();

	}

	public class YuYueBean {

		public ArrayList<TypeItem> reservationTypeList = new ArrayList<TypeItem>();
		public ArrayList<YuYueItem> bespeakList = new ArrayList<YuYueItem>();
		public CONSIGNEE consignee;

		public class CONSIGNEE {
			public String CONSIGNEE_PHONE; // 手机号码
			public String CONSIGNEE_ADDRESS; // 详细地址
			public String CONSIGNEE_NAME; // 联系人
		}

		public class YuYueItem {

			public String ORDER_ID; // 订单id
			public String COMMODITY_ID;// 商品id
			public String COMMODITY_IMAGE_PATH;// 商品图片
			public String COMMODITY_NAME;// 商品名称
			public String SPECVALUE; // 规格
			public String price; // 价格
			public String num; // 数量
			// 配件集合
			public ArrayList<PartItem> partsList = new ArrayList<PartItem>();

		}

		public class PartItem {

			public String NAME;// 配件名称
			public String ID;// 配件id
			public boolean isCheck;

		}

		public class TypeItem {
			public String RESERVATIONTYP_ID;// 预约类型id
			public String RESERVATIONTYP_NAME;// 预约名称
		}

	}

}
