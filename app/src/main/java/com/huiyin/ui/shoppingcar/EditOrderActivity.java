package com.huiyin.ui.shoppingcar;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.Address;
import com.huiyin.bean.AddressItem;
import com.huiyin.bean.Distribution;
import com.huiyin.bean.Integral;
import com.huiyin.bean.Invoice;
import com.huiyin.bean.Invoice.InvoiceType;
import com.huiyin.bean.Order;
import com.huiyin.bean.Payment;
import com.huiyin.ui.user.AddressManagementActivity;
import com.huiyin.ui.user.AddressModifyActivity;
import com.huiyin.utils.MathUtil;

/**
 * 填写订单
 * 
 * @author lixiaobin
 * */
public class EditOrderActivity extends BaseActivity implements TextWatcher, OnClickListener {

	private final static String TAG = "WriteOrderActivity";

	private static int REQUEST_ADDRESS = 0x0010, REQUEST_PAYMENT = 0x0011, REQUEST_INVOICE = 0x0012, REQUEST_LHJ = 0x0013, REQUEST_ADDRESS_UNLOGIN = 0x0014, REQUEST_INVOICE_UNLOGIN = 0x0015;

	// 无登录结算提交的商品列表
	public static final String INTENT_KEY_GOODS_LIST = "goods_list";
	// 商品详情跳转（登录状态）的ShoppingId
	public static final String INTENT_KEY_SHOPID = "shop_id";

	public static final String INTENT_KEY_ORDER_BEAN = "order_bean";

	private TextView btnBack, ab_title;// 标题栏

	// 提交订单
	private TextView submit_order;
	// 各个信息部分的点击按钮
	private TextView write_receiver_ed, write_pay_ed, write_invoice_ed, write_hujuan_ed, write_good_detail;
	// 积分编辑框
	private EditText shop_jifen_ed;
	// 收货人信息
	private TextView write_receiver_tv, write_phone_tv, write_addr_tv;
	// 支付、配送
	private TextView write_pay_way, write_send_way, write_send_time;
	// 发票信息
	private TextView write_invoice_type_tv, write_invoice_tv;
	// 积分、虎劵
	private TextView integral_deducate_1, integral_deducate_2, write_hujuan_info;
	// 结算信息
	private TextView good_total_price, logistic_price, integral_price, hujuan_price, should_pay_price, man_mian_fright;
	// 积分和虎劵信息
	private TextView jf_title_tv, hj_title_tv, jf_minus_tv, hj_minus_tv;
	// 乐虎劵、积分layout
	private LinearLayout shop_dedution_layout, shop_lhj_layout;
	// 应付金额
	private TextView final_pay_price;

	private Order order;

	private String shopId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_order);

		initData();// 初始化数值

		initView();

		if (isLogin()) {
			getData(shopId);
		} else {
			// 获取运费
			getFreightData();
		}
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		order = new Order();

		if (getIntent().hasExtra(INTENT_KEY_GOODS_LIST)) {
			// 未登录立即结算提交订单情况，直接发送过来商品列表

		} else if (getIntent().hasExtra(INTENT_KEY_SHOPID)) {
			// 已登录提交订单情况，提交的是ShopId
			shopId = getIntent().getStringExtra(INTENT_KEY_SHOPID);
		} else if (getIntent().hasExtra(INTENT_KEY_ORDER_BEAN)) {
			// 未登录购物车提交订单情况

		}
	}

	/**
	 * 初始化视图
	 * 
	 * */
	private void initView() {
		btnBack = (TextView) findViewById(R.id.ab_back);
		btnBack.setOnClickListener(this);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("填写订单");

		// 收货信息
		write_receiver_tv = (TextView) findViewById(R.id.write_receiver_tv);
		write_phone_tv = (TextView) findViewById(R.id.write_phone_tv);
		write_addr_tv = (TextView) findViewById(R.id.write_addr_tv);
		write_receiver_ed = (TextView) findViewById(R.id.write_receiver_ed);// 编辑

		// 配送
		write_pay_way = (TextView) findViewById(R.id.write_pay_way);
		write_send_way = (TextView) findViewById(R.id.write_send_way);
		write_send_time = (TextView) findViewById(R.id.write_send_time);
		write_pay_ed = (TextView) findViewById(R.id.write_pay_ed);// 编辑

		// 发票
		write_invoice_type_tv = (TextView) findViewById(R.id.write_invoice_type_tv);
		write_invoice_tv = (TextView) findViewById(R.id.write_invoice_tv);
		write_invoice_ed = (TextView) findViewById(R.id.write_invoice_ed);// 编辑

		write_hujuan_ed = (TextView) findViewById(R.id.write_hujuan_ed);
		write_good_detail = (TextView) findViewById(R.id.write_good_detail);

		shop_jifen_ed = (EditText) findViewById(R.id.shop_jifen_ed);
		shop_jifen_ed.addTextChangedListener(this);

		write_receiver_ed.setOnClickListener(this);
		write_pay_ed.setOnClickListener(this);
		write_invoice_ed.setOnClickListener(this);
		write_hujuan_ed.setOnClickListener(this);
		write_good_detail.setOnClickListener(this);

		// 积分
		integral_deducate_1 = (TextView) findViewById(R.id.integral_deducate_1);
		integral_deducate_2 = (TextView) findViewById(R.id.integral_deducate_2);

		// 积分和乐虎劵布局
		shop_dedution_layout = (LinearLayout) findViewById(R.id.shop_dedution_layout);
		shop_lhj_layout = (LinearLayout) findViewById(R.id.shop_lhj_layout);

		// 虎劵
		write_hujuan_info = (TextView) findViewById(R.id.write_hujuan_info);

		// 结算信息
		man_mian_fright = (TextView) findViewById(R.id.man_mian_freight);
		good_total_price = (TextView) findViewById(R.id.good_total_price);// 商品金额
		logistic_price = (TextView) findViewById(R.id.logistic_price);// 运费
		// 积分结算
		jf_title_tv = (TextView) findViewById(R.id.jf_title_tv);
		jf_minus_tv = (TextView) findViewById(R.id.jf_minus_tv);
		integral_price = (TextView) findViewById(R.id.integral_price);
		// 虎劵结算
		hj_title_tv = (TextView) findViewById(R.id.hj_title_tv);
		hj_minus_tv = (TextView) findViewById(R.id.hj_minus_tv);
		hujuan_price = (TextView) findViewById(R.id.hujuan_price);
		// 应付金额
		should_pay_price = (TextView) findViewById(R.id.discount_value);

		// 总价
		final_pay_price = (TextView) findViewById(R.id.final_pay_price);
		// 提交按钮
		submit_order = (TextView) findViewById(R.id.submit_order);
		submit_order.setOnClickListener(this);
	}

	/***
	 * 判断是否登录
	 * 
	 * */
	private boolean isLogin() {
		if (AppContext.getInstance().getUserId() != null && !AppContext.getInstance().getUserId().equals("")) {
			// 已经登录了
			return true;
		} else {
			// 未登录
			return false;
		}
	}

	/**
	 * 设置收货人信息
	 * 
	 * */
	private void setAddressView() {
		write_receiver_tv.setText(order.getAddress().getName());
		write_phone_tv.setText(order.getAddress().getPhone());
		write_addr_tv.setText(order.getAddress().getAddress());
	}

	/**
	 * 设置支付与配送方式
	 * 
	 * */
	private void setPaymentView() {
		write_pay_way.setText(order.getPayment().getName());// 支付方式
		write_send_way.setText(order.getDistribution().getName());// 配送方式
		write_send_time.setText(order.getDistribution().getStartTime() + " —— " + order.getDistribution().getEndTime());// 配送时间
	}

	/**
	 * 设置发票信息
	 * 
	 * */
	private void setInvoiceView() {
		if (order.getInvoice().getInvoiceType() == InvoiceType.InvoiceTypeTax) {
			write_invoice_type_tv.setText(Invoice.INVOICE_TYPE_TAX);// 发票类型
		} else {
			write_invoice_type_tv.setText(Invoice.INVOICE_TYPE_NORMAL);// 发票类型
		}
		write_invoice_tv.setText(order.getInvoice().getTitle());// 发票抬头
	}

	/**
	 * 设置积分信息
	 * 
	 * */
	private void setIntegralView() {
		if (isLogin()) {
			shop_dedution_layout.setVisibility(View.VISIBLE);
			// TODO
			String amount = MathUtil.keepMost2Decimal(order.getIntegral().getAmount());
			String discount = MathUtil.keepMost2Decimal(order.getIntegral().getAmount() / order.getIntegral().getUnitAmount() * order.getIntegral().getDiscount());
			integral_deducate_1.setText("共" + amount + "个积分可用，抵" + discount + "元");// 积分规则
			integral_deducate_2.setText("个积分，可抵" + discount + "元");// 积分抵扣信息
			shop_jifen_ed.setText(0 + "");// 积分编辑框
		} else {
			// 未登录
			shop_dedution_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置虎劵信息
	 * 
	 * */
	private void setHujuanView() {
		if (isLogin()) {
			shop_lhj_layout.setVisibility(View.VISIBLE);
			// TODO
			order.getLehujuan().getAmount();
			write_hujuan_info.setText("共" + order.getLehujuan().getCount() + "张可使用的虎券，打算使用一张面值" + MathUtil.priceForAppWithSign(order.getLehujuan().getAmount()) + "元的虎劵");// 虎劵抵扣信息
		} else {
			// 未登录
			shop_lhj_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置结算与总额信息
	 * 
	 * */
	private void setSettlementView() {

		if (isLogin()) {
			jf_title_tv.setVisibility(View.VISIBLE);
			jf_minus_tv.setVisibility(View.VISIBLE);
			integral_price.setVisibility(View.VISIBLE);

			hj_title_tv.setVisibility(View.VISIBLE);
			hj_minus_tv.setVisibility(View.VISIBLE);
			hujuan_price.setVisibility(View.VISIBLE);

			// 积分
			integral_price.setText("");
			// 虎劵
			hujuan_price.setText("");
		} else {
			jf_title_tv.setVisibility(View.GONE);
			jf_minus_tv.setVisibility(View.GONE);
			integral_price.setVisibility(View.GONE);

			hj_title_tv.setVisibility(View.GONE);
			hj_minus_tv.setVisibility(View.GONE);
			hujuan_price.setVisibility(View.GONE);
		}

		// 商品总额
		good_total_price.setText(MathUtil.priceForAppWithSign(order.getTotalAmount()));
		// 运费
		logistic_price.setText(MathUtil.priceForAppWithSign(order.getFreight()));

		float pay = order.getTotalAmount() + order.getFreight() - order.getIntegralAmount() - order.getHujuanAmount();
		order.setShouldPay(pay);

		// 应付金额
		should_pay_price.setText(MathUtil.priceForAppWithSign(order.getShouldPay()));

		final_pay_price.setText(MathUtil.priceForAppWithSign(order.getShouldPay()));

	}

	/**
	 * 登录状态，提交订单获取订单数据
	 * */
	private void getData(String shopId) {
		RequstClient.writeOrderInit(AppContext.getInstance().getUserId(), shopId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getString("type").equals("1")) {
						Log.i("", "==================" + content);
						// 多少减免运费
						order.setFreeFreight(MathUtil.stringToFloat(obj.getString("freight")));// 运费减免标准

						// 积分折扣情况
						JSONObject integralObj = obj.getJSONObject("userMap");
						Integral integral = new Integral();
						integral.setAmount(MathUtil.stringToFloat(integralObj.getString("INTEGRAL")));
						integral.setUnitAmount(MathUtil.stringToFloat(integralObj.getString("integerOffset")));
						integral.setDiscount(MathUtil.stringToFloat(integralObj.getString("integerDiscount")));
						order.setIntegral(integral);

						// 商品总额
						order.setTotalAmount(MathUtil.stringToFloat(integralObj.getString("price")));

						// 收货地址
						JSONObject addressObj = obj.getJSONObject("shoppingAddress");
						Address address = new Address();
						address.setName(addressObj.getString("CONSIGNEE_NAME"));
						address.setProvince(addressObj.getString("PROVINCE"));
						address.setCity(addressObj.getString("CITY"));
						address.setAddress(addressObj.getString("ADDRESS"));
						address.setArea(addressObj.getString("AREA"));
						address.setPhone(addressObj.getString("CONSIGNEE_PHONE"));
						order.setAddress(address);

						// 支付方式
						JSONObject paymentObj = obj.getJSONObject("payMethod");
						Payment payment = new Payment();
						payment.setId(MathUtil.stringToInt(paymentObj.getString("PAYMETHOD_ID")));
						payment.setName(paymentObj.getString("PAYMETHOD_NAME"));
						order.setPayment(payment);

						// 运输
						JSONObject distributionObj = obj.getJSONObject("distributionManagementList");
						Distribution distribution = new Distribution();
						distribution.setId(MathUtil.stringToInt(distributionObj.getString("DIS_MANAG_ID")));
						distribution.setName(distributionObj.getString("DIS_MANAG_NAME"));
						distribution.setTime(obj.getString("deliveryTime"));
						order.setDistribution(distribution);

						return;
					} else {
						// 返回失败信息
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
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

	/**
	 * 未登录状态，获取运费规则
	 * */
	private void getFreightData() {
		RequstClient.FreeShipping(new CustomResponseHandler(this) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getString("type").equals("1")) {

						return;
					} else {
						// 返回失败信息
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				Toast.makeText(getBaseContext(), "服务器连接失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 提交订单
	 * */
	private void submitOrder() {

	}

	/**
	 * 登录状态提交
	 * */
	private void submitWithSignIn() {

	}

	/**
	 * 未登录状态提交
	 * */
	private void submitWithOutSignIn() {

	}

	// /***
	// * 未登录提交订单
	// */
	// private void postUnLogInfo() {
	// String json = creatJson(mOrderBean.orderList);
	// mUnlogBean.commodity = json;
	// RequstClient.commitOrderUnLogin(mUnlogBean.consignee_name,
	// mUnlogBean.consignee_phone, mUnlogBean.postal_code,
	// mUnlogBean.consignee_address, mUnlogBean.shipping_method,
	// mUnlogBean.payment_method, mUnlogBean.receipt_time,
	// mUnlogBean.invoicing_method, mUnlogBean.invoice_title,
	// mUnlogBean.invoice_content, mUnlogBean.company_name,
	// mUnlogBean.identification_number, mUnlogBean.registered_address,
	// mUnlogBean.registered_phone, mUnlogBean.bank, mUnlogBean.account,
	// mUnlogBean.collector_name,
	// mUnlogBean.collector_phone, mUnlogBean.collector_address,
	// mUnlogBean.commodity, mUnlogBean.delivery_remark, mUnlogBean.province_id,
	// mUnlogBean.city_id, mUnlogBean.area_id,
	// mUnlogBean.freight, new CustomResponseHandler(this) {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, String content) {
	// // TODO Auto-generated method stub
	// super.onSuccess(statusCode, headers, content);
	// try {
	// JSONObject obj = new JSONObject(content);
	// if (!obj.getString("type").equals("1")) {
	// String errorMsg = obj.getString("msg");
	// Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// Intent i = new Intent();
	// i.setClass(EditOrderActivity.this, CommitOrderActivity.class);
	// i.putExtra("number", obj.getString("number"));
	// i.putExtra("serialNum", obj.getString("orderId"));
	//
	// String result = final_pay_price.getText().toString().trim();
	// if (result.startsWith("￥")) {
	// result = result.substring(1);
	// }
	// i.putExtra("totalPrice", result.trim());
	// startActivity(i);
	// finish();
	//
	// } catch (JsonSyntaxException e) {
	// e.printStackTrace();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }
	//
	// private boolean checkCommitInfo() {
	// if (AppContext.userId == null) {
	// Toast.makeText(EditOrderActivity.this, "请先登录！",
	// Toast.LENGTH_LONG).show();
	// return false;
	// } else if (payId == null || sendId == null) {
	// Toast.makeText(EditOrderActivity.this, "请选择支付方式！",
	// Toast.LENGTH_LONG).show();
	// return false;
	// }
	// return true;
	// }

	// /**
	// * 提交订单
	// */
	// private void postCommitInfo() {
	// RequstClient.postCommitInfo(AppContext.userId, shopId, freight,
	// jifen_value, jifen_num, "", addrId, payId, sendTime, invoiceId, lhj_id,
	// sendId, new CustomResponseHandler(this) {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, String content) {
	// super.onSuccess(statusCode, headers, content);
	// LogUtil.i(TAG, "postCommitInfo:" + content);
	// try {
	// JSONObject obj = new JSONObject(content);
	// if (!obj.getString("type").equals("1")) {
	// String errorMsg = obj.getString("msg");
	// Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
	// return;
	// }
	// Intent i = new Intent();
	// i.setClass(EditOrderActivity.this, CommitOrderActivity.class);
	// i.putExtra("serialNum", obj.getString("serialNum"));
	// i.putExtra("number", obj.getString("number"));
	//
	// String result = final_pay_price.getText().toString().trim();
	// if (result.startsWith("￥")) {
	// result = result.substring(1);
	// }
	// i.putExtra("totalPrice", result.trim());
	//
	// startActivity(i);
	// finish();
	// } catch (JsonSyntaxException e) {
	// e.printStackTrace();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PAYMENT && resultCode == RESULT_OK) {
			String payId = data.getStringExtra("pay");
			String sendId = data.getStringExtra("send");
			String sendTime = data.getStringExtra("sendtime");

			write_pay_way.setText(payId);
			write_send_way.setText(sendId);
			write_send_time.setText(sendTime);
		} else if (requestCode == REQUEST_INVOICE && resultCode == RESULT_OK) {
			String invoiceId = data.getStringExtra("invoice_id");
			String invoice_way = data.getStringExtra("invoice_way");
			String invoice_title = data.getStringExtra("invoice_title");
			if (invoice_way.equals("1")) {
				invoice_way = "增值发票";
			} else {
				invoice_way = "普通发票";
			}
			write_invoice_type_tv.setText(invoice_way);
			write_invoice_tv.setText(invoice_title);
		} else if (requestCode == REQUEST_LHJ && resultCode == RESULT_OK) {
			String lhj_id = data.getStringExtra("lhj_value_id");
			String hujuan_value = data.getStringExtra("lhj_value_price");
		} else if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK) {
			AddressItem addrItem = (AddressItem) data.getSerializableExtra("addr");
		} else if (requestCode == REQUEST_ADDRESS_UNLOGIN && resultCode == RESULT_OK) {
			// mUnlogBean.consignee_name = data.getStringExtra("receiver");
			// mUnlogBean.consignee_phone = data.getStringExtra("phone");
			// mUnlogBean.postal_code = data.getStringExtra("code");
			// mUnlogBean.consignee_address =
			// data.getStringExtra("detail_addr");
			// mUnlogBean.province_id = data.getStringExtra("provinceId");
			// mUnlogBean.city_id = data.getStringExtra("cityId");
			// mUnlogBean.area_id = data.getStringExtra("areaId");

			// write_receiver_tv.setText(mUnlogBean.consignee_name);
			// write_phone_tv.setText(mUnlogBean.consignee_phone);
			// write_addr_tv.setText(mUnlogBean.consignee_address);
		} else if (requestCode == REQUEST_INVOICE_UNLOGIN && resultCode == RESULT_OK) {
			String invoice_way = data.getStringExtra("invoice_way");
			String invoice_title = data.getStringExtra("invoice_title");
			if (invoice_way.equals("1")) {
				invoice_way = "增值发票";
			} else {
				invoice_way = "普通发票";
			}
			write_invoice_type_tv.setText(invoice_way);
			write_invoice_tv.setText(invoice_title);
			// mUnlogBean.invoicing_method = data.getStringExtra("invoice_way");
			// mUnlogBean.invoice_title = data.getStringExtra("invoice_title");
			// mUnlogBean.invoice_content =
			// data.getStringExtra("invoice_content");
			// mUnlogBean.company_name = data.getStringExtra("company_name");
			// mUnlogBean.identification_number =
			// data.getStringExtra("identification_number");
			// mUnlogBean.registered_address =
			// data.getStringExtra("registered_address");
			// mUnlogBean.registered_phone =
			// data.getStringExtra("registered_phone");
			// mUnlogBean.bank = data.getStringExtra("bank");
			// mUnlogBean.account = data.getStringExtra("account");
			// mUnlogBean.collector_name =
			// data.getStringExtra("collector_name");
			// mUnlogBean.collector_phone =
			// data.getStringExtra("collector_phone");
			// mUnlogBean.collector_address =
			// data.getStringExtra("collector_address");
		}
	}

	/**
	 * 信息验证是否填完
	 */
	private boolean checkInfo() {

		return true;
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (arg0.length() < 1) {
			return;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ab_back:
			finish();
			break;
		case R.id.submit_order:
			// if (AppContext.userId == null && checkInfo()) {
			// postUnLogInfo();
			// } else if (checkCommitInfo()) {
			// postCommitInfo();
			// }
			break;
		case R.id.write_receiver_ed:
			if (AppContext.getInstance().getUserId() == null || AppContext.getInstance().getUserId().equals("")) {
				Intent i = new Intent();
				i.setClass(EditOrderActivity.this, AddressModifyActivity.class);
				i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.UNLOGIN);
				startActivityForResult(i, REQUEST_ADDRESS_UNLOGIN);
			} else {
				Intent i = new Intent();
				i.setClass(EditOrderActivity.this, AddressManagementActivity.class);
				i.putExtra(AddressManagementActivity.TAG, AddressManagementActivity.order);
				startActivityForResult(i, REQUEST_ADDRESS);
			}
			break;
		case R.id.write_pay_ed:
			Intent pay_intent = new Intent();
			pay_intent.setClass(EditOrderActivity.this, PayWayActivity.class);
			startActivityForResult(pay_intent, REQUEST_PAYMENT);
			break;
		case R.id.write_invoice_ed:
			if (AppContext.getInstance().getUserId() == null || AppContext.getInstance().getUserId().equals("")) {
				Intent invoice_intent = new Intent();
				invoice_intent.setClass(EditOrderActivity.this, InvoiceInfoActivity.class);
				startActivityForResult(invoice_intent, REQUEST_INVOICE_UNLOGIN);
			} else {
				Intent invoice_intent = new Intent();
				invoice_intent.setClass(EditOrderActivity.this, InvoiceInfoActivity.class);
				startActivityForResult(invoice_intent, REQUEST_INVOICE);
			}
			break;
		case R.id.write_hujuan_ed:
			Intent hu_intent = new Intent();
			hu_intent.setClass(EditOrderActivity.this, HuJuanDeductionActivity.class);
			// hu_intent.putStringArrayListExtra("lhjPriceList", lhjPriceList);
			// hu_intent.putStringArrayListExtra("lhjIdList", lhjIdList);
			// hu_intent.putExtra("total_price", mWriteOrderBean.price);
			startActivityForResult(hu_intent, REQUEST_LHJ);
			break;
		case R.id.write_good_detail:
			// 订单详情
			Intent g_intent = new Intent();
			// g_intent.putExtra(GoodsListActivity.INTENT_KEY_GOODS_LIST,
			// mOrderBean);
			g_intent.setClass(EditOrderActivity.this, GoodsListActivity.class);
			startActivity(g_intent);
			break;
		default:
			break;
		}
	}

}
