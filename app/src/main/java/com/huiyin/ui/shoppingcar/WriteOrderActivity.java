package com.huiyin.ui.shoppingcar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AddressItem;
import com.huiyin.bean.Lehujuan;
import com.huiyin.bean.OrderBean;
import com.huiyin.bean.OrderItem;
import com.huiyin.bean.ShopItem;
import com.huiyin.bean.UnloginBean;
import com.huiyin.bean.WriteOrderBean;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.dialog.SingleConfirmDialog.DialogClickListener;
import com.huiyin.ui.user.AddressManagementActivity;
import com.huiyin.ui.user.AddressModifyActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;

/**
 * 填写订单
 * 
 * 
 * */
public class WriteOrderActivity extends BaseActivity implements TextWatcher, OnClickListener {

	private final static String TAG = "WriteOrderActivity";

	// 无登录结算提交的商品列表
	public static final String INTENT_KEY_GOODS_LIST = "goods_list";
	// 商品详情跳转（登录状态）的ShoppingId
	public static final String INTENT_KEY_SHOPID = "shop_id";

	public static final String INTENT_KEY_ORDER_BEAN = "order_bean";

	private TextView btnBack, ab_title;

	// 提交订单
	private TextView submit_order;
	// 各个信息部分的点击按钮
	private TextView write_receiver_ed, write_pay_ed, write_invoice_ed, write_hujuan_ed, write_good_detail;
	private EditText shop_jifen_ed;
	// 配送界面返回的信息
	private String payId, sendId, shopId, sendTime;
	private String payWayId = "1";
	private String sendWayId = "1";
	private int nearby_mention;

	private static int request_addr = 0x0010, request_pay = 0x0011, request_invoice = 0x0012, request_lhj = 0x0013,
			request_addr_unlogin = 0x0014, request_invoice_unlogin = 0x0015, request_addr_init = 0x0016;
	// 收货人信息
	private TextView write_receiver_tv, write_phone_tv, write_addr_tv;
	// 支付、配送
	private TextView write_pay_way, write_send_way, write_send_time;
	// 发票信息
	private TextView write_invoice_type_tv, write_invoice_tv;
	private LinearLayout layout_invoice;
	private CheckBox integral_deducate_checkBox;
	private LinearLayout integral_deducate_layout;
	// 积分、虎劵
	private TextView integral_deducate_1, integral_deducate_2, write_hujuan_info;
	// 结算信息
	private TextView good_total_price, logistic_price, integral_price, hujuan_price, discount_value, man_mian_fright;
	// 积分和虎劵信息
	private TextView jf_title_tv, hj_title_tv, jf_minus_tv, hj_minus_tv;
	// 乐虎劵、积分layout
	private LinearLayout shop_dedution_layout, shop_lhj_layout;

	// 应付金额
	private TextView final_pay_price;
	// 发票界面返回的 信息
	private String invoiceId, invoice_way, invoice_title, invoice_content,invoice_title_type;
	private String company_name, identification_number, registered_address, registered_phone, bank, account, collector_name,
			collector_phone, collector_address;

	private String hujuan_value = "0", jifen_value = "0", jifen_num = "0";
	private WriteOrderBean mWriteOrderBean;
	// 乐虎劵id
	private String lhj_id;
	// 运费
	private float freight = 0;
	// 折扣
	private float disount = 0.0f;
	// 积分换算率
	private double integerDiscount = 1;// 默认为1
	// 地址id
	private String addrId;

	private UnloginBean mUnlogBean;

	private List<ShopItem> listGoods;

	private OrderBean mOrderBean;// 订单详情跳转的bean

	private OrderBean shopCarOrderBean;// 购物车跳转过来的

	private float totalPrice;

	private List<Lehujuan> listLehujuans = new ArrayList<Lehujuan>();

	// -1 都关闭 ，0 都开启，1：普通发票开启，增值税发票关闭 ，2：普通发票关闭，增值税发票开启
	private int invoice_flag = 0;
	// 备注信息
	private EditText remark;
	private String remarkInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_order);

		initData();// 初始化数值

		initView();

		setView();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		if (getIntent().hasExtra(INTENT_KEY_GOODS_LIST)) {
			mUnlogBean = new UnloginBean();
			listGoods = new ArrayList<ShopItem>();// 商品详情
			if ((ArrayList<ShopItem>) getIntent().getSerializableExtra(INTENT_KEY_GOODS_LIST) != null) {
				listGoods.addAll((ArrayList<ShopItem>) getIntent().getSerializableExtra(INTENT_KEY_GOODS_LIST));
			}
		} else if (getIntent().hasExtra(INTENT_KEY_SHOPID)) {
			shopId = getIntent().getStringExtra(INTENT_KEY_SHOPID);
		} else if (getIntent().hasExtra(INTENT_KEY_ORDER_BEAN)) {
			mUnlogBean = new UnloginBean();
			shopCarOrderBean = (OrderBean) getIntent().getSerializableExtra(INTENT_KEY_ORDER_BEAN);
		} else {
			shopId = getIntent().getStringExtra("shopId");
		}
	}

	private void initView() {
		btnBack = (TextView) findViewById(R.id.ab_back);
		btnBack.setOnClickListener(this);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("填写订单");

		submit_order = (TextView) findViewById(R.id.submit_order);
		submit_order.setOnClickListener(this);

		write_receiver_ed = (TextView) findViewById(R.id.write_receiver_ed);
		write_pay_ed = (TextView) findViewById(R.id.write_pay_ed);
		write_invoice_ed = (TextView) findViewById(R.id.write_invoice_ed);
		write_hujuan_ed = (TextView) findViewById(R.id.write_hujuan_ed);
		write_good_detail = (TextView) findViewById(R.id.write_good_detail);

		shop_jifen_ed = (EditText) findViewById(R.id.shop_jifen_ed);
		shop_jifen_ed.addTextChangedListener(this);

		write_receiver_ed.setOnClickListener(this);
		write_pay_ed.setOnClickListener(this);
		write_invoice_ed.setOnClickListener(this);
		write_hujuan_ed.setOnClickListener(this);
		write_good_detail.setOnClickListener(this);

		mWriteOrderBean = new WriteOrderBean();
		// 配送
		write_pay_way = (TextView) findViewById(R.id.write_pay_way);
		write_send_way = (TextView) findViewById(R.id.write_send_way);
		write_send_time = (TextView) findViewById(R.id.write_send_time);
		// 收货信息
		write_receiver_tv = (TextView) findViewById(R.id.write_receiver_tv);
		write_phone_tv = (TextView) findViewById(R.id.write_phone_tv);
		write_addr_tv = (TextView) findViewById(R.id.write_addr_tv);
		// 发票
		write_invoice_type_tv = (TextView) findViewById(R.id.write_invoice_type_tv);
		// write_invoice_type_tv.setText("普通发票");
		write_invoice_tv = (TextView) findViewById(R.id.write_invoice_tv);
		layout_invoice = (LinearLayout) findViewById(R.id.layout_invoice);
		// 积分
		integral_deducate_1 = (TextView) findViewById(R.id.integral_deducate_1);
		integral_deducate_2 = (TextView) findViewById(R.id.integral_deducate_2);
		jf_title_tv = (TextView) findViewById(R.id.jf_title_tv);
		jf_minus_tv = (TextView) findViewById(R.id.jf_minus_tv);

		// 积分和乐虎劵布局
		shop_dedution_layout = (LinearLayout) findViewById(R.id.shop_dedution_layout);
		shop_lhj_layout = (LinearLayout) findViewById(R.id.shop_lhj_layout);

		integral_deducate_checkBox = (CheckBox) findViewById(R.id.integral_deducate_checkBox);
		integral_deducate_layout = (LinearLayout) findViewById(R.id.integral_deducate_layout);
		integral_deducate_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					integral_deducate_layout.setVisibility(View.VISIBLE);
				} else {
					shop_jifen_ed.setText("0");
					integral_deducate_layout.setVisibility(View.GONE);
				}
			}
		});

		// 虎劵
		hj_title_tv = (TextView) findViewById(R.id.hj_title_tv);
		hj_minus_tv = (TextView) findViewById(R.id.hj_minus_tv);
		write_hujuan_info = (TextView) findViewById(R.id.write_hujuan_info);
		// 备注信息
		remark = (EditText) findViewById(R.id.order_remark_info);
		// 结算信息
		man_mian_fright = (TextView) findViewById(R.id.man_mian_freight);
		good_total_price = (TextView) findViewById(R.id.good_total_price);
		logistic_price = (TextView) findViewById(R.id.logistic_price);
		integral_price = (TextView) findViewById(R.id.integral_price);
		hujuan_price = (TextView) findViewById(R.id.hujuan_price);
		discount_value = (TextView) findViewById(R.id.discount_value);
		// 总价
		final_pay_price = (TextView) findViewById(R.id.final_pay_price);

		if (AppContext.getInstance().getUserId() == null) {
			// 未登录，不能使用积分，乐虎劵抵扣价格
			shop_dedution_layout.setVisibility(View.GONE);// 积分
			shop_lhj_layout.setVisibility(View.GONE);// 乐虎劵
			jf_title_tv.setVisibility(View.GONE);// 结算积分标题
			jf_minus_tv.setVisibility(View.GONE);// 积分减号
			hj_title_tv.setVisibility(View.GONE);// 乐虎劵标题
			hj_minus_tv.setVisibility(View.GONE);// 乐虎劵减号
			integral_price.setVisibility(View.GONE);// 积分值
			hujuan_price.setVisibility(View.GONE);// 乐虎劵值
		} else {
			shop_dedution_layout.setVisibility(View.VISIBLE);
			shop_lhj_layout.setVisibility(View.VISIBLE);
			jf_title_tv.setVisibility(View.VISIBLE);
			jf_minus_tv.setVisibility(View.VISIBLE);
			hj_title_tv.setVisibility(View.VISIBLE);
			hj_minus_tv.setVisibility(View.VISIBLE);
			integral_price.setVisibility(View.VISIBLE);
			hujuan_price.setVisibility(View.VISIBLE);
		}
	}

	private void setView() {
		// 积分
		String deducate = String.format(getString(R.string.integral_deducate_1), 0)
				+ String.format(getString(R.string.integral_deducate_2), 0);
		integral_deducate_2.setText(Html.fromHtml(String.format(getString(R.string.jifen_deducate),
				MathUtil.priceForAppWithOutSign(0))));
		integral_deducate_1.setText(Html.fromHtml(deducate));

		// 虎劵
		String hujuan = String.format(getString(R.string.order_discount_1), "0")
				+ String.format(getString(R.string.order_discount_2), MathUtil.priceForAppWithOutSign(0));
		write_hujuan_info.setText(Html.fromHtml(hujuan));

		if (AppContext.getInstance().getUserId() == null) {
			// 如果无用户登录
			if (listGoods != null) {
				rebuildData(listGoods);
				refreshUnLoginUI();
			} else if (shopCarOrderBean != null) {
				mOrderBean = shopCarOrderBean;
				refreshUnLoginUI();
			}
		} else {
			// 有用户登录
			RequstClient.writeOrderInit(AppContext.getInstance().getUserId(), shopId, new CustomResponseHandler(this) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					try {
						JSONObject obj = new JSONObject(content);
						if (obj.getInt("type") != 1) {
							String errorMsg = obj.getString("msg");
							if (obj.getInt("type") == -1) {
								noGoodsDialog(errorMsg);
							} else {
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
							}
							return;
						}
						mWriteOrderBean = new Gson().fromJson(content, WriteOrderBean.class);
						if (mWriteOrderBean == null) {
							return;
						}
						if (obj.has("invoice")) {

							invoice_flag = obj.getInt("invoice");
							if (invoice_flag == -1) {
								layout_invoice.setVisibility(View.GONE);
							} else {
								layout_invoice.setVisibility(View.VISIBLE);
							}
						}
						rebuildData(mWriteOrderBean.shoppingCar);
						refreshLoginUI();
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

	private void noGoodsDialog(String message) {
		SingleConfirmDialog dialog = new SingleConfirmDialog(this);
		dialog.setCustomTitle("失败");
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setConfirm("确定");
		dialog.setClickListener(new DialogClickListener() {
			@Override
			public void onConfirmClickListener() {
				finish();
			}
		});
		dialog.show();
	}

	/**
	 * 未登录状态更新界面
	 */
	private void refreshUnLoginUI() {
		// //结算信息
		float totalPrice = 0;

		for (OrderItem item : mOrderBean.orderList) {
			totalPrice += item.total_price * item.good_qty;
		}

		good_total_price.setText(MathUtil.priceForAppWithSign(totalPrice));// 商品总额

		final_pay_price.setText(MathUtil.priceForAppWithSign(totalPrice));// 最终总额

		this.totalPrice = totalPrice;

		RequstClient.appOrderDiscount(totalPrice, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						return;
					}

					disount = MathUtil.stringToFloat(obj.getString("discount"));
					float price = 0.0f;
					if (disount <= 0.0f || disount >= 10.0f) {
						discount_value.setText("");
						price = WriteOrderActivity.this.totalPrice;
					} else {
						discount_value.setText(disount + "折");
						price = WriteOrderActivity.this.totalPrice * disount * 0.1f;
					}
					final_pay_price.setText(MathUtil.priceForAppWithSign(price));// 最终总额
					WriteOrderActivity.this.totalPrice = price;
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Intent i = new Intent();
		i.setClass(WriteOrderActivity.this, AddressModifyActivity.class);
		i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.UNLOGIN);
		startActivityForResult(i, request_addr_unlogin);
	}

	/**
	 * 登录状态更新界面
	 */
	private void refreshLoginUI() {

		// 配送
		payId = mWriteOrderBean.payMethod.PAYMETHOD_NAME;
		sendId = mWriteOrderBean.distributionManagementList.DIS_MANAG_NAME;
		sendTime = mWriteOrderBean.deliveryTime;

		write_pay_way.setText(payId);
		write_send_way.setText(sendId);
		write_send_time.setText(sendTime);

		// 发票
		// write_invoice_type_tv.setText(text);
		// write_invoice_tv.setText(text);

		// //积分
		String deducate;
		if (mWriteOrderBean.userMap != null) {
			integerDiscount = MathUtil.stringToFloat(mWriteOrderBean.userMap.integerDiscount);
			if (mWriteOrderBean.userMap.INTEGRAL.equals("")) {
				// 积分抵扣规则为空，直接设置为 0积分抵扣 0.00 元
				deducate = String.format(getString(R.string.integral_deducate_1), 0 + "")
						+ String.format(getString(R.string.integral_deducate_2), MathUtil.priceForAppWithOutSign(0));
			} else {
				// 积分抵扣规则
				String value = MathUtil.priceForAppWithOutSign(Double.parseDouble(mWriteOrderBean.userMap.INTEGRAL)
						* integerDiscount / Double.parseDouble(mWriteOrderBean.userMap.integerOffset));
				deducate = String.format(getString(R.string.integral_deducate_1), mWriteOrderBean.userMap.INTEGRAL)
						+ String.format(getString(R.string.integral_deducate_2), value);
			}
		} else {
			// 共0积分，可抵扣 0.00 元
			deducate = String.format(getString(R.string.integral_deducate_1), 0 + "")
					+ String.format(getString(R.string.integral_deducate_2), MathUtil.priceForAppWithOutSign(0));
		}
		integral_deducate_1.setText(Html.fromHtml(deducate));

		// //虎劵
		String hujuan;
		if (mWriteOrderBean.discountList == null || mWriteOrderBean.discountList.size() < 1) {
			hujuan = String.format(getString(R.string.order_discount_1), "0")
					+ String.format(getString(R.string.order_discount_2), MathUtil.priceForAppWithOutSign(0));

		} else {
			// hujuan_value = mWriteOrderBean.discountList.get(0).PRICE;
			// lhj_id = mWriteOrderBean.discountList.get(0).ID;
			hujuan = String.format(getString(R.string.order_discount_1), mWriteOrderBean.discountList.size() + "")
					+ String.format(getString(R.string.order_discount_2), MathUtil.priceForAppWithOutSign(0));
		}
		lhj_id = "";
		write_hujuan_info.setText(Html.fromHtml(hujuan));

		// 运费
		man_mian_fright.setText(mWriteOrderBean.promotions_name);
		logistic_price.setText(MathUtil.priceForAppWithSign(mWriteOrderBean.freight));
		freight = mWriteOrderBean.freight;
		// 结算信息

		good_total_price.setText(MathUtil.priceForAppWithSign(mWriteOrderBean.price));
		// shop_logistic_price.setText(text);
		// shop_integral_price.setText(text);

		hujuan_price.setText(MathUtil.priceForAppWithSign(hujuan_value));

		disount = MathUtil.stringToFloat(mWriteOrderBean.discount);
		float pay_price = 0.0f;
		if (disount <= 0.0f || disount >= 10.0f) {
			discount_value.setText("");
			pay_price = mWriteOrderBean.price - MathUtil.stringToFloat(hujuan_value) + freight;
		} else {
			discount_value.setText(mWriteOrderBean.discount + "折");
			pay_price = mWriteOrderBean.price * disount * 0.1f - MathUtil.stringToFloat(hujuan_value) + freight;
		}

		if (pay_price < 0) {
			pay_price = 0;
		}

		final_pay_price.setText(MathUtil.priceForAppWithSign(pay_price));

		this.totalPrice = pay_price;

		for (int i = 0; i < mWriteOrderBean.discountList.size(); i++) {
			Lehujuan bean = new Lehujuan();
			bean.setAmount(MathUtil.stringToFloat(mWriteOrderBean.discountList.get(i).PRICE));
			bean.setDate(mWriteOrderBean.discountList.get(i).ENDTIME);
			bean.setId(MathUtil.stringToInt(mWriteOrderBean.discountList.get(i).ID));
			listLehujuans.add(bean);
		}

		// 收货信息
		addrId = mWriteOrderBean.shoppingAddress.ID;
		if (addrId != null && !addrId.equals("")) {

			write_receiver_tv.setText(mWriteOrderBean.shoppingAddress.CONSIGNEE_NAME);
			write_phone_tv.setText(mWriteOrderBean.shoppingAddress.CONSIGNEE_PHONE);
			String addr = mWriteOrderBean.shoppingAddress.PROVINCE + mWriteOrderBean.shoppingAddress.CITY
					+ mWriteOrderBean.shoppingAddress.AREA + mWriteOrderBean.shoppingAddress.ADDRESS;
			write_addr_tv.setText(addr);

			invoice_title = mWriteOrderBean.shoppingAddress.CONSIGNEE_NAME;
			write_invoice_tv.setText(mWriteOrderBean.shoppingAddress.CONSIGNEE_NAME);
		} else {
			Intent i = new Intent();
			i.setClass(WriteOrderActivity.this, AddressModifyActivity.class);
			i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.INIT);
			startActivityForResult(i, request_addr_init);
		}
	}

	/**
	 * 重新组装数据，转成订单详情的数据类型
	 */
	private void rebuildData(List<ShopItem> lists) {
		mOrderBean = new OrderBean();
		for (int i = 0; i < lists.size(); i++) {

			if (lists.get(i).FID < 0) {
				OrderItem order_item = new OrderItem();
				ShopItem shopItem = lists.get(i);
				ArrayList<ShopItem> goodList = new ArrayList<ShopItem>();
				float totalPrice = shopItem.COMMODITY_PRICE;
				for (int j = 0; j < lists.size(); j++) {
					if (shopItem.ID == lists.get(j).FID) {
						goodList.add(lists.get(j));
						totalPrice += lists.get(j).COMMODITY_PRICE;
					}
				}
				order_item.goodList = goodList;
				order_item.total_price = totalPrice;
				order_item.shopItem = shopItem;
				order_item.good_qty = order_item.shopItem.COMMDOTIY_QTY;
				mOrderBean.orderList.add(order_item);
			}
		}
	}

	/***
	 * 未登录提交订单
	 */
	private void postUnLogInfo() {
		String json = creatJson(mOrderBean.orderList);
		mUnlogBean.commodity = json;
		RequstClient.commitOrderUnLogin(mUnlogBean.consignee_name, mUnlogBean.consignee_phone, mUnlogBean.postal_code,
				mUnlogBean.consignee_address, sendWayId, payWayId, mUnlogBean.receipt_time, mUnlogBean.invoicing_method,
				mUnlogBean.invoice_title, mUnlogBean.invoice_content, mUnlogBean.company_name, mUnlogBean.identification_number,
				mUnlogBean.registered_address, mUnlogBean.registered_phone, mUnlogBean.bank, mUnlogBean.account,
				mUnlogBean.collector_name, mUnlogBean.collector_phone, mUnlogBean.collector_address, mUnlogBean.commodity,
				mUnlogBean.delivery_remark, mUnlogBean.province_id, mUnlogBean.city_id, mUnlogBean.area_id, mUnlogBean.freight,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {

						super.onSuccess(statusCode, headers, content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								if (obj.getString("type").equals("-2")) {
									noGoodsDialog(errorMsg);
								} else {
									Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								}
								return;
							}

							Intent i = new Intent();
							if (totalPrice <= 0) {
								i.setClass(WriteOrderActivity.this, PaymentSuccessActivity.class);
								i.putExtra(PaymentSuccessActivity.INTENT_KEY_ORDER_NUM, obj.getString("number"));
								i.putExtra(PaymentSuccessActivity.INTENT_KEY_ORDER_ID, obj.getString("orderId"));
								startActivity(i);
							} else {
								i.setClass(WriteOrderActivity.this, CommitOrderActivity.class);
								i.putExtra("number", obj.getString("number"));
								i.putExtra("serialNum", obj.getString("orderId"));
								i.putExtra("totalPrice", MathUtil.keepMost2Decimal(obj.getDouble("price")));
								startActivity(i);
							}
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

	private boolean checkCommitInfo() {
		if (AppContext.getInstance().getUserId() == null) {
			Toast.makeText(WriteOrderActivity.this, "请先登录！", Toast.LENGTH_LONG).show();
			return false;
		} else if (payId == null || sendId == null) {
			Toast.makeText(WriteOrderActivity.this, "请选择支付方式！", Toast.LENGTH_LONG).show();
			return false;
		}
		remarkInfo = remark.getText().toString();
		return true;
	}

	/**
	 * 提交订单
	 */
	private void postCommitInfo() {
		RequstClient.postCommitInfo(AppContext.getInstance().getUserId(), shopId, freight + "", jifen_value, jifen_num, "", addrId, payWayId,
				sendTime, invoiceId, lhj_id, sendWayId, nearby_mention, remarkInfo, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postCommitInfo:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								if (obj.getString("type").equals("-2")) {
									noGoodsDialog(errorMsg);
								} else {
									Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								}
								return;
							}
							Intent i = new Intent();
							if (totalPrice <= 0) {
								i.setClass(WriteOrderActivity.this, PaymentSuccessActivity.class);
								i.putExtra(PaymentSuccessActivity.INTENT_KEY_ORDER_ID, obj.getString("serialNum"));
								i.putExtra(PaymentSuccessActivity.INTENT_KEY_ORDER_NUM, obj.getString("number"));
							} else {
								i.setClass(WriteOrderActivity.this, CommitOrderActivity.class);
								i.putExtra("serialNum", obj.getString("serialNum"));
								i.putExtra("number", obj.getString("number"));
								i.putExtra("totalPrice", MathUtil.keepMost2Decimal(obj.getDouble("price")));
								i.putExtra("perpaidCard", MathUtil.keepMost2Decimal(obj.getDouble("perpaidCard")));
							}
							startActivity(i);
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
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_pay && resultCode == RESULT_OK) {
			payWayId = data.getStringExtra("payWayId");
			sendWayId = data.getStringExtra("sendWayId");
			payId = data.getStringExtra("pay");
			sendId = data.getStringExtra("send");
			sendTime = data.getStringExtra("sendtime");
			nearby_mention = data.getIntExtra("shopId", -1);
			write_pay_way.setText(payId);
			write_send_way.setText(sendId);
			write_send_time.setText(sendTime);

			refreshOnPay();

			if (mUnlogBean == null) {
				return;
			} else {
				// 未登录的没有进行上门自提处理
				mUnlogBean.payment_method = payId;
				mUnlogBean.shipping_method = sendId;
				mUnlogBean.receipt_time = sendTime;
			}
		} else if (requestCode == request_invoice && resultCode == RESULT_OK) {
			invoiceId = data.getStringExtra("invoice_id");
			invoice_way = data.getStringExtra("invoice_way");
			invoice_title = data.getStringExtra("invoice_title");
			invoice_title_type = data.getStringExtra("invoice_title_type");
			invoice_content = data.getStringExtra("invoice_content");
			company_name = data.getStringExtra("company_name");
			identification_number = data.getStringExtra("identification_number");
			registered_address = data.getStringExtra("registered_address");
			registered_phone = data.getStringExtra("registered_phone");
			bank = data.getStringExtra("bank");
			account = data.getStringExtra("account");
			collector_name = data.getStringExtra("collector_name");
			collector_phone = data.getStringExtra("collector_phone");
			collector_address = data.getStringExtra("collector_address");

			if (invoice_way.equals("1")) {
				write_invoice_type_tv.setText("增值发票");
			} else {
				write_invoice_type_tv.setText("普通发票");
			}
			write_invoice_tv.setText(invoice_title);
		} else if (requestCode == request_lhj && resultCode == RESULT_OK) {
			lhj_id = data.getStringExtra("lhj_value_id");
			hujuan_value = data.getStringExtra("lhj_value_price");
			refreshOnLHJ();
		} else if (requestCode == request_addr && resultCode == RESULT_OK) {
			AddressItem addrItem = (AddressItem) data.getSerializableExtra("addr");
			refreshOnAddr(addrItem);
		} else if (requestCode == request_addr_init && resultCode == RESULT_OK) {
			AddressItem addrItem = (AddressItem) data.getSerializableExtra("addr");
			refreshOnAddr(addrItem);
		} else if (requestCode == request_addr_unlogin && resultCode == RESULT_OK) {
			if (mUnlogBean == null) {
				return;
			}

			mUnlogBean.consignee_name = data.getStringExtra("receiver");
			mUnlogBean.consignee_phone = data.getStringExtra("phone");
			mUnlogBean.postal_code = data.getStringExtra("code");
			mUnlogBean.consignee_address = data.getStringExtra("detail_addr");
			mUnlogBean.province_id = data.getStringExtra("provinceId");
			mUnlogBean.city_id = data.getStringExtra("cityId");
			mUnlogBean.area_id = data.getStringExtra("areaId");
			String address = data.getStringExtra("address");
			setUnlogAddrFreight();

			write_receiver_tv.setText(mUnlogBean.consignee_name);
			write_phone_tv.setText(mUnlogBean.consignee_phone);
			write_addr_tv.setText(address);

		} else if (requestCode == request_invoice_unlogin && resultCode == RESULT_OK) {

			invoice_way = data.getStringExtra("invoice_way");
			invoice_title = data.getStringExtra("invoice_title");
			if (invoice_way.equals("1")) {
				invoice_way = "增值发票";
			} else {
				invoice_way = "普通发票";
			}
			write_invoice_type_tv.setText(invoice_way);
			write_invoice_tv.setText(invoice_title);

			mUnlogBean.invoicing_method = data.getStringExtra("invoice_way");
			mUnlogBean.invoice_title = data.getStringExtra("invoice_title");
			mUnlogBean.invoice_content = data.getStringExtra("invoice_content");
			mUnlogBean.company_name = data.getStringExtra("company_name");
			mUnlogBean.identification_number = data.getStringExtra("identification_number");
			mUnlogBean.registered_address = data.getStringExtra("registered_address");
			mUnlogBean.registered_phone = data.getStringExtra("registered_phone");
			mUnlogBean.bank = data.getStringExtra("bank");
			mUnlogBean.account = data.getStringExtra("account");
			mUnlogBean.collector_name = data.getStringExtra("collector_name");
			mUnlogBean.collector_phone = data.getStringExtra("collector_phone");
			mUnlogBean.collector_address = data.getStringExtra("collector_address");
		}
	}

	private void setUnlogAddrFreight() {

		float price = 0.0f;
		for (OrderItem item : mOrderBean.orderList) {
			price += item.total_price * item.good_qty;
		}
		final float pay_price = price;
		RequstClient.appFreight(price, mUnlogBean.city_id, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						return;
					}

					mUnlogBean.freight = obj.getInt("freight") + "";
					freight = obj.getInt("freight");
					logistic_price.setText(MathUtil.priceForAppWithSign(mUnlogBean.freight));
					float price = 0.0f;
					if (disount <= 0.0f || disount >= 10.0f) {
						price = pay_price + freight;
					} else {
						price = pay_price * disount * 0.1f + freight;
					}
					final_pay_price.setText(MathUtil.priceForAppWithSign(price));// 最终总额
					WriteOrderActivity.this.totalPrice = price;
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
	 * 地址返回的信息
	 */
	private void refreshOnAddr(AddressItem addrItem) {

		shop_jifen_ed.setText("0");

		write_receiver_tv.setText(addrItem.CONSIGNEE_NAME);
		write_phone_tv.setText(addrItem.CONSIGNEE_PHONE);

		String addr = addrItem.LEVELADDR + addrItem.ADDRESS;
		write_addr_tv.setText(addr);
		addrId = addrItem.ADDRESSID;

		RequstClient.appFreight(mWriteOrderBean.price, addrItem.CITY_ID, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						return;
					}

					disount = MathUtil.stringToFloat(mWriteOrderBean.discount);
					freight = obj.getInt("freight");
					logistic_price.setText(MathUtil.priceForAppWithSign(freight));
					float pay_price = 0.0f;
					if (disount <= 0.0f || disount >= 10.0f) {
						discount_value.setText("");
						pay_price = mWriteOrderBean.price - MathUtil.stringToFloat(hujuan_value)
								- MathUtil.stringToFloat(jifen_value) + freight;
					} else {
						discount_value.setText(mWriteOrderBean.discount + "折");
						pay_price = mWriteOrderBean.price * disount * 0.1f - MathUtil.stringToFloat(hujuan_value)
								- MathUtil.stringToFloat(jifen_value) + freight;
					}

					if (pay_price < 0) {
						pay_price = 0;
					}
					final_pay_price.setText(MathUtil.priceForAppWithSign(pay_price));
					WriteOrderActivity.this.totalPrice = pay_price;
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

	private void refreshOnPay() {
		shop_jifen_ed.setText("0");

		int i = Integer.valueOf(sendWayId);
		if (i == -1) {
			logistic_price.setText(MathUtil.priceForAppWithSign(0));
			freight = 0;
		} else {
			logistic_price.setText(MathUtil.priceForAppWithSign(mWriteOrderBean.freight));
			freight = mWriteOrderBean.freight;
		}
		disount = MathUtil.stringToFloat(mWriteOrderBean.discount);
		float pay_price = 0.0f;
		if (disount <= 0.0f || disount >= 10.0f) {
			discount_value.setText("");
			pay_price = mWriteOrderBean.price - MathUtil.stringToFloat(hujuan_value) - MathUtil.stringToFloat(jifen_value)
					+ freight;
		} else {
			discount_value.setText(mWriteOrderBean.discount + "折");
			pay_price = mWriteOrderBean.price * disount * 0.1f - MathUtil.stringToFloat(hujuan_value)
					- MathUtil.stringToFloat(jifen_value) + freight;
		}

		if (pay_price < 0) {
			pay_price = 0;
		}
		final_pay_price.setText(MathUtil.priceForAppWithSign(pay_price));
		this.totalPrice = pay_price;
	}

	/**
	 * 乐虎劵界面返回的信息
	 */
	private void refreshOnLHJ() {
		// 结算信息
		hujuan_price.setText(MathUtil.priceForAppWithSign(hujuan_value));

		// 虎劵
		String hujuan = String.format(getString(R.string.order_discount_1), listLehujuans.size())
				+ String.format(getString(R.string.order_discount_2), MathUtil.priceForAppWithOutSign(hujuan_value));
		write_hujuan_info.setText(Html.fromHtml(hujuan));

		disount = MathUtil.stringToFloat(mWriteOrderBean.discount);
		float pay_price = 0.0f;
		if (disount <= 0.0f || disount >= 10.0f) {
			discount_value.setText("");
			pay_price = mWriteOrderBean.price - MathUtil.stringToFloat(hujuan_value) - MathUtil.stringToFloat(jifen_value)
					+ freight;
		} else {
			discount_value.setText(mWriteOrderBean.discount + "折");
			pay_price = mWriteOrderBean.price * disount * 0.1f - MathUtil.stringToFloat(hujuan_value)
					- MathUtil.stringToFloat(jifen_value) + freight;
		}

		if (pay_price < 0) {
			pay_price = 0;
		}
		final_pay_price.setText(MathUtil.priceForAppWithSign(pay_price));
		this.totalPrice = pay_price;
	}

	/**
	 * 未登录时信息验证
	 */
	private boolean checkInfo() {
		if (mUnlogBean == null) {
			return false;
		} else if (mUnlogBean.payment_method == null) {
			Toast.makeText(WriteOrderActivity.this, "请选择支付方式！", Toast.LENGTH_LONG).show();
			return false;
		} else if (mUnlogBean.consignee_name == null) {
			Toast.makeText(WriteOrderActivity.this, "请填写地址信息！", Toast.LENGTH_LONG).show();
			return false;
		}
		// else if(mUnlogBean.invoicing_method==null){
		// Toast.makeText(WriteOrderActivity.this, "请填写发票信息！",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		return true;
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (arg0.length() < 1) {
			// 不支持什么都不写
			shop_jifen_ed.setText(0 + "");
			return;
		} else if (arg0.length() > 1 && MathUtil.stringToInt(arg0.toString()) <= 0) {
			// 不支持太多联系2个0开头
			shop_jifen_ed.setText(0 + "");
			return;
		} else if (arg0.length() > 1 && arg0.toString().startsWith("0")) {
			// 不支持01、02、03etc
			shop_jifen_ed.setText(MathUtil.stringToInt(arg0.toString()) + "");
		}

		float discount = MathUtil.stringToFloat(mWriteOrderBean.userMap.integerDiscount);
		float offset = MathUtil.stringToFloat(mWriteOrderBean.userMap.integerOffset);

		int mostCount = 0;
		if (discount != 0) {
			mostCount = (int) ((mWriteOrderBean.price + freight) * offset / discount);// 最大的分数值
		}

		if (offset != 0 && mostCount * discount / offset < mWriteOrderBean.price + freight) {
			mostCount += 1;
		}

		if (Integer.parseInt(arg0.toString()) <= MathUtil.stringToFloat(mWriteOrderBean.userMap.INTEGRAL)) {
			// 输入未超过自身总分
			if (Integer.parseInt(arg0.toString()) <= mostCount) {
				// 输入的值未超过商品总额
				jifen_num = arg0.toString();

				jifen_value = MathUtil.stringToInt(jifen_num) * discount / offset + "";

				if (offset == 0) {
					jifen_value = "" + 0;
				}

				integral_deducate_2.setText(Html.fromHtml(String.format(getString(R.string.jifen_deducate),
						MathUtil.priceForAppWithOutSign(jifen_value))));

				float price = 0, hujuan = 0, jifen = 0;
				price = mWriteOrderBean.price;
				if (hujuan_value != null && !hujuan_value.equals("")) {
					hujuan = MathUtil.stringToFloat(hujuan_value);
				}
				if (jifen_value != null && !jifen_value.equals("")) {
					jifen = MathUtil.stringToFloat(jifen_value);
				}
				disount = MathUtil.stringToFloat(mWriteOrderBean.discount);
				float pay_price = 0.0f;
				if (disount <= 0.0f || disount >= 10.0f) {
					discount_value.setText("");
					pay_price = price - hujuan - jifen + freight;
				} else {
					discount_value.setText(mWriteOrderBean.discount + "折");
					pay_price = price * disount * 0.1f - hujuan - jifen + freight;
				}

				integral_price.setText(MathUtil.priceForAppWithSign(jifen));

				if (pay_price < 0) {
					final_pay_price.setText(MathUtil.priceForAppWithSign(0));
					this.totalPrice = 0.0f;
				} else {
					final_pay_price.setText(MathUtil.priceForAppWithSign(pay_price));
					this.totalPrice = pay_price;
				}
				shop_jifen_ed.setSelection(shop_jifen_ed.getText().length());
			} else {
				shop_jifen_ed.setText(mostCount + "");
				shop_jifen_ed.setSelection(shop_jifen_ed.getText().length());
			}
		} else {
			// 超过总积分不允许输入。
			if (MathUtil.stringToFloat(mWriteOrderBean.userMap.INTEGRAL) <= mostCount) {
				// 自己的积分小于最大需要的积分,以自己最大的积分算
				shop_jifen_ed.setText(MathUtil.stringToInt(mWriteOrderBean.userMap.INTEGRAL) + "");
				shop_jifen_ed.setSelection(shop_jifen_ed.getText().length());
			} else {
				shop_jifen_ed.setText(mostCount + "");
				shop_jifen_ed.setSelection(shop_jifen_ed.getText().length());
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ab_back:
			finish();
			break;
		case R.id.submit_order:
			if (AppContext.getInstance().getUserId() == null && checkInfo()) {
				postUnLogInfo();
			} else if (checkCommitInfo()) {
				postCommitInfo();
			}
			break;

		case R.id.write_receiver_ed:
			if (AppContext.getInstance().getUserId() == null || AppContext.getInstance().getUserId().equals("")) {
				Intent i = new Intent();
				i.setClass(WriteOrderActivity.this, AddressModifyActivity.class);
				i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.UNLOGIN);
				startActivityForResult(i, request_addr_unlogin);
			} else {
				Intent i = new Intent();
				i.setClass(WriteOrderActivity.this, AddressManagementActivity.class);
				i.putExtra(AddressManagementActivity.TAG, AddressManagementActivity.order);
				startActivityForResult(i, request_addr);
			}
			break;

		case R.id.write_pay_ed:
			Intent pay_intent = new Intent();
			pay_intent.setClass(WriteOrderActivity.this, PayWayActivity.class);
			pay_intent.putExtra("sendWayId", sendWayId);
			pay_intent.putExtra("shopId", nearby_mention);
			pay_intent.putExtra("send", sendId);
			startActivityForResult(pay_intent, request_pay);
			break;

		case R.id.write_invoice_ed:
			if (AppContext.getInstance().getUserId() == null || AppContext.getInstance().getUserId().equals("")) {
				Intent invoice_intent = new Intent();
				invoice_intent.setClass(WriteOrderActivity.this, InvoiceInfoActivity.class);
				startActivityForResult(invoice_intent, request_invoice_unlogin);
			} else {
				Intent invoice_intent = new Intent();
				invoice_intent.setClass(WriteOrderActivity.this, InvoiceInfoActivity.class);
				invoice_intent.putExtra(InvoiceInfoActivity.INTENT_KEY_FLAG, invoice_flag);
				invoice_intent.putExtra("invoice_way", invoice_way);
				invoice_intent.putExtra("invoice_title", invoice_title);
				invoice_intent.putExtra("invoice_content", invoice_content);
				invoice_intent.putExtra("invoice_title_type", invoice_title_type);
				invoice_intent.putExtra("company_name", company_name);
				invoice_intent.putExtra("identification_number", identification_number);
				invoice_intent.putExtra("registered_address", registered_address);
				invoice_intent.putExtra("registered_phone", registered_phone);
				invoice_intent.putExtra("bank", bank);
				invoice_intent.putExtra("account", account);
				invoice_intent.putExtra("collector_name", collector_name);
				invoice_intent.putExtra("collector_phone", collector_phone);
				invoice_intent.putExtra("collector_address", collector_address);
				startActivityForResult(invoice_intent, request_invoice);
			}

			break;
		case R.id.write_hujuan_ed:
			if (listLehujuans != null && listLehujuans.size() > 0) {
				Intent hu_intent = new Intent();
				hu_intent.setClass(WriteOrderActivity.this, HuJuanDeductionActivity.class);
				hu_intent.putParcelableArrayListExtra(HuJuanDeductionActivity.INTENT_KEY_LIST,
						(ArrayList<? extends Parcelable>) listLehujuans);
				startActivityForResult(hu_intent, request_lhj);
			} else {
				Toast.makeText(getApplicationContext(), "您还没有乐虎劵", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.write_good_detail:
			// 订单详情
			Intent g_intent = new Intent();
			g_intent.putExtra(GoodsListActivity.INTENT_KEY_GOODS_LIST, mOrderBean);
			g_intent.setClass(WriteOrderActivity.this, GoodsListActivity.class);
			startActivity(g_intent);

			break;
		default:
			break;
		}
	}

	private String creatJson(ArrayList<OrderItem> orderLists) {
		String json = "[";
		for (OrderItem order : orderLists) {
			int num = order.good_qty;// 数量
			String id1 = order.shopItem.COMMDOITY_ID;
			long fid1 = order.shopItem.FID;
			String code1 = order.shopItem.GOODS_CODE;
			if (json.endsWith("}")) {
				json += ",";
			}
			String bean = "{" + "\"GOODS_CODE\":\"" + code1 + "\",\"ID\":\"" + id1 + "\",\"COMMDOTIY_QTY\":\"" + num
					+ "\",\"FID\":\"" + fid1 + "\"}";
			json += bean;
			if (order.goodList != null) {
				for (ShopItem shopItem : order.goodList) {
					int num2 = shopItem.COMMDOTIY_QTY;// 数量
					String id2 = shopItem.COMMDOITY_ID;
					long fid2 = shopItem.FID;
					String code2 = shopItem.GOODS_CODE;
					if (json.endsWith("}")) {
						json += ",";
					}
					String bean2 = "{" + "\"GOODS_CODE\":\"" + code2 + "\",\"ID\":\"" + id2 + "\",\"COMMDOTIY_QTY\":\"" + num2
							+ "\",\"FID\":\"" + fid2 + "\"}";
					json += bean2;
				}
			}
		}
		json += "]";
		return json;
	}
}
