package com.huiyin.ui.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.shoppingcar.CommitOrderActivity;
import com.huiyin.ui.user.MyOrderDetailActivity.OrderDetailBean.GoodItem;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyOrderDetailActivity extends BaseActivity {

	private final static String TAG = "MyOrderDetailActivity";
	TextView order_pay_now, order_check_logistics, order_comment;
	private TextView left_rb, middle_title_tv;
	String order_id;
	String pushFlag;
	OrderDetailBean mOrderDetailBean;
	LayoutInflater inflater;

	TextView od_ordernum, od_order_statu, od_total_price, od_good_price,
			od_logistic_fare, od_jifen, od_coupon, od_discount;
	LinearLayout od_goodslist_layout;

	TextView od_usename, od_telephone, od_addr, od_payway, od_peison,
			od_invoice, od_peison_time, kefu_phone, buy_message;

	String[] status = { "", "未付款", "订单审核", "待发货", "待收货", "交易完成", "取消订单" };
	// 订单状态：未付款1，订单审核2，待发货3，待收货4，交易完成5， 取消订单6
	public static final int UNPAY = 1, AUDIT = 2, WAIT_SEND = 3,
			WAIT_RECEIVE = 4, FINISH = 5, CANCEL = 6;
	// 确认收货
	private int request_receive = 0x0021;
	// 确认收货的商品id
	private String commodityId;
	// 免运费
	TextView fjt_mianyunfei;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail_layout);

		initView();
		initData();
		// initFreeFreight();
	}

	/**
	 * 免运费
	 */
	private void initFreeFreight() {

		RequstClient.FreeShipping(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "FreeShipping:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					fjt_mianyunfei.setText(String.format(
							getString(R.string.free_freight),
							obj.getString("freight")));
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

	private void initView() {

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("订单详情");
		fjt_mianyunfei = (TextView) findViewById(R.id.fjt_mianyunfei);
		// fjt_mianyunfei.setText(String.format(getString(R.string.free_freight),
		// "0"));

		left_rb = (TextView) findViewById(R.id.left_ib);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (pushFlag.equals("1")) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
				} else {
					finish();
				}
			}
		});

		order_pay_now = (TextView) findViewById(R.id.order_pay_now);
		order_pay_now.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent();
				i.setClass(MyOrderDetailActivity.this,
						CommitOrderActivity.class);
				i.putExtra(
						"serialNum",
						mOrderDetailBean.orderDetail.orderDetailList.get(0).ORDER_ID);
				i.putExtra("number", mOrderDetailBean.orderDetail.ORDER_CODE);
				i.putExtra("totalPrice",
						mOrderDetailBean.orderDetail.TOTAL_PRICE);
				i.putExtra("perpaidCard",
						mOrderDetailBean.orderDetail.PERPAID_CARD);
				startActivity(i);
				finish();
			}
		});

		order_check_logistics = (TextView) findViewById(R.id.order_check_logistics);
		order_check_logistics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(MyOrderDetailActivity.this,
						CheckLogisticActivity.class);
				i.putExtra("order_id", order_id);
				startActivity(i);
			}
		});

		order_comment = (TextView) findViewById(R.id.order_comment);
		order_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent();
				i.setClass(MyOrderDetailActivity.this,
						OrderCommentActivity.class);
				i.putExtra("goodlist",
						mOrderDetailBean.orderDetail.orderDetailList);
				int flag = 0;
				// 评价
				if (mOrderDetailBean.orderDetail.STATUS == FINISH
						&& !mOrderDetailBean.orderDetail.ORDER_COMMENTS_STATUS
								.equals("1")) {
					flag = 0;
				} else {
					flag = 1;
				}
				i.putExtra("flag", flag);
				startActivity(i);
				finish();
			}
		});

		order_id = getIntent().getStringExtra("order_id");
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0"
				: getIntent().getStringExtra("pushFlag");

		od_ordernum = (TextView) findViewById(R.id.od_ordernum);
		od_order_statu = (TextView) findViewById(R.id.od_order_statu);
		od_total_price = (TextView) findViewById(R.id.od_total_price);
		od_good_price = (TextView) findViewById(R.id.od_good_price);
		od_logistic_fare = (TextView) findViewById(R.id.od_logistic_fare);
		od_jifen = (TextView) findViewById(R.id.od_jifen);
		od_coupon = (TextView) findViewById(R.id.od_coupon);
		od_discount = (TextView) findViewById(R.id.od_discount);

		od_goodslist_layout = (LinearLayout) findViewById(R.id.od_goodslist_layout);

		od_usename = (TextView) findViewById(R.id.od_usename);
		od_telephone = (TextView) findViewById(R.id.od_telephone);
		od_addr = (TextView) findViewById(R.id.od_addr);
		od_payway = (TextView) findViewById(R.id.od_payway);
		od_peison = (TextView) findViewById(R.id.od_peison);
		od_invoice = (TextView) findViewById(R.id.od_invoice);
		od_peison_time = (TextView) findViewById(R.id.od_peison_time);
		buy_message = (TextView) findViewById(R.id.buy_message);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		kefu_phone = (TextView) findViewById(R.id.order_detail_kefu_phone);
		kefu_phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String telPhone = PreferenceUtil.getInstance(mContext)
						.getHotLine();
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(telPhone);
				telPhone = m.replaceAll("").trim();
				Log.i("解析结果", "====" + telPhone);

				if (StringUtils.isBlank(telPhone))
					return;
				Intent d_intent = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:" + telPhone));
				d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(d_intent);
			}
		});
	}

	private void refreshUI() {

		if (mOrderDetailBean == null) {
			return;
		}
		od_ordernum.setText(mOrderDetailBean.orderDetail.ORDER_CODE);
		try {
			if (mOrderDetailBean.orderDetail.STATUS == 30) {
				
				od_order_statu.setText("订单作废");
			} else {
				
				od_order_statu.setText(status[mOrderDetailBean.orderDetail.STATUS]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mOrderDetailBean.orderDetail.STATUS == UNPAY) {
			order_pay_now.setVisibility(View.VISIBLE);
		} else {
			order_pay_now.setVisibility(View.GONE);
		}
		if (mOrderDetailBean.orderDetail.STATUS == WAIT_RECEIVE
				|| mOrderDetailBean.orderDetail.STATUS == FINISH) {

			order_check_logistics.setVisibility(View.VISIBLE);
		} else {
			order_check_logistics.setVisibility(View.GONE);
		}

		od_total_price.setText(MathUtil
				.priceForAppWithSign(mOrderDetailBean.orderDetail.TOTAL_PRICE));

		od_logistic_fare
				.setText(MathUtil
						.priceForAppWithSign(mOrderDetailBean.orderDetail.LOGISTICS_FARE));

		od_jifen.setText(MathUtil
				.priceForAppWithSign(mOrderDetailBean.orderDetail.POINTS_DEDUCTION));
		if (mOrderDetailBean.orderDetail.COUPON == null)
			mOrderDetailBean.orderDetail.COUPON = "0.0";
		od_coupon.setText(MathUtil
				.priceForAppWithSign(mOrderDetailBean.orderDetail.COUPON));

		float disount = MathUtil
				.stringToFloat(mOrderDetailBean.orderDetail.BENEFIT_INFORMATION);
		if (disount <= 0.0f || disount >= 10.0f) {

			od_discount.setText("");
		} else {

			od_discount.setText(disount + "折");
		}
		// 商品总价
		if (mOrderDetailBean.orderDetail.TOTAL_PRICE == null
				|| mOrderDetailBean.orderDetail.TOTAL_PRICE.equals("")) {
			mOrderDetailBean.orderDetail.TOTAL_PRICE = "0.0";
		}
		if (mOrderDetailBean.orderDetail.LOGISTICS_FARE == null
				|| mOrderDetailBean.orderDetail.LOGISTICS_FARE.equals("")) {
			mOrderDetailBean.orderDetail.LOGISTICS_FARE = "0.0";
		}
		if (mOrderDetailBean.orderDetail.POINTS_DEDUCTION == null
				|| mOrderDetailBean.orderDetail.POINTS_DEDUCTION.equals("")) {
			mOrderDetailBean.orderDetail.POINTS_DEDUCTION = "0.0";
		}
		float good_total_price = 0;

		for (GoodItem item : mOrderDetailBean.orderDetail.orderDetailList) {
//			good_total_price += MathUtil.stringToFloat(item.COMMODITY_PRICE)
//					* MathUtil.stringToFloat(item.BUY_QTY);
			
			if(!item.PURCHASE_PRICE.equals("")){
				good_total_price += MathUtil.stringToFloat(item.PURCHASE_PRICE)
						* MathUtil.stringToFloat(item.BUY_QTY);
			}else{
				good_total_price += MathUtil.stringToFloat(item.COMMODITY_PRICE)
						* MathUtil.stringToFloat(item.BUY_QTY);
			}
		}
		od_good_price.setText(good_total_price < 0 ? 0 + "" : MathUtil
				.priceForAppWithSign(good_total_price));

		od_usename.setText(mOrderDetailBean.orderDetail.CONSIGNEE_NAME);
		od_telephone.setText(mOrderDetailBean.orderDetail.CONSIGNEE_PHONE);
		od_addr.setText(mOrderDetailBean.orderDetail.CONSIGNEE_ADDRESS);

		od_payway.setText(mOrderDetailBean.orderDetail.PAYMENTMETHOD);
		od_peison.setText(mOrderDetailBean.orderDetail.SHIPPING_METHOD);
		od_invoice.setText(mOrderDetailBean.orderDetail.INVOICES_TYPE);
		if (mOrderDetailBean.orderDetail.INVOICES_ID != 0) {
			od_invoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							InvoiceDetailActivity.class);
					intent.putExtra("InvoiceId",
							mOrderDetailBean.orderDetail.INVOICES_ID);
					startActivity(intent);
				}
			});
		}

		buy_message.setText(mOrderDetailBean.orderDetail.BUY_MESSAGE);

		od_peison_time.setText(mOrderDetailBean.orderDetail.RECEIPT_TIME);

		for (int i = 0; i < mOrderDetailBean.orderDetail.orderDetailList.size(); i++) {

			final GoodItem g_item = mOrderDetailBean.orderDetail.orderDetailList
					.get(i);
			View view = inflater.inflate(R.layout.order_goods_item, null);
			View view_line_1 = (View) view.findViewById(R.id.view_line_1);
			View view_line_2 = (View) view.findViewById(R.id.view_line_2);
			if (i == 0) {
				view_line_1.setVisibility(View.GONE);
			} else {
				view_line_1.setVisibility(View.VISIBLE);
			}
			if (i == mOrderDetailBean.orderDetail.orderDetailList.size() - 1) {
				view_line_2.setVisibility(View.GONE);
			} else {
				view_line_2.setVisibility(View.GONE);
			}
			ImageView orderlist_img = (ImageView) view
					.findViewById(R.id.orderlist_img);
			TextView orderlist_good_title = (TextView) view
					.findViewById(R.id.orderlist_good_title);
			TextView orderlist_good_color = (TextView) view
					.findViewById(R.id.orderlist_good_color);
			TextView orderlist_good_price = (TextView) view
					.findViewById(R.id.orderlist_good_price);
			TextView orderlist_good_num = (TextView) view
					.findViewById(R.id.orderlist_good_num);
			// 预约
			TextView to_yuyue_tv = (TextView) view
					.findViewById(R.id.to_yuyue_tv);
			// 申请
			TextView to_sq_tv = (TextView) view.findViewById(R.id.to_sq_tv);
			// 评论
			TextView to_comment_tv = (TextView) view
					.findViewById(R.id.to_comment_tv);
			// 确认收货
			TextView to_receive_tv = (TextView) view
					.findViewById(R.id.to_receive_tv);

			orderlist_good_title.setText(g_item.COMMODITY_NAME);
			orderlist_good_color.setText(g_item.SPECVALUE);
			if(g_item.PURCHASE_PRICE!=null && !g_item.PURCHASE_PRICE.equals("")){       //如果折扣价格不为空就显示折扣价格
			    orderlist_good_price.setText(MathUtil.priceForAppWithSign(g_item.PURCHASE_PRICE));
			}else{
				orderlist_good_price.setText(MathUtil.priceForAppWithSign(g_item.COMMODITY_PRICE));
			}
//			orderlist_good_price.setText(MathUtil
//					.priceForAppWithOutSign(g_item.COMMODITY_PRICE));
			orderlist_good_num.setText("数量：" + g_item.BUY_QTY);
			ImageLoader.getInstance()
					.displayImage(URLs.IMAGE_URL + g_item.COMMODITY_IMAGE_PATH,
							orderlist_img);
			// 预约
			if (mOrderDetailBean.orderDetail.STATUS > WAIT_SEND
					&& mOrderDetailBean.orderDetail.STATUS < CANCEL
					&& (g_item.BESPEAK_STATUS == null || g_item.BESPEAK_STATUS
							.equals("1"))) {
				to_yuyue_tv.setVisibility(View.VISIBLE);
			} else {
				to_yuyue_tv.setVisibility(View.GONE);
			}
			// 售后申请
			if (mOrderDetailBean.orderDetail.orderDetailList.get(i).RECEIVING_STATUS
					.equals("2")
					&& (g_item.AFTERMARKET_STATUS == null || g_item.AFTERMARKET_STATUS
							.equals("1"))
					&& isTimeOn(mOrderDetailBean.orderDetail.curDate,
							g_item.RECEIVING_TIME,
							mOrderDetailBean.orderDetail.dayReplace)) {
				to_sq_tv.setVisibility(View.VISIBLE);// 售后申请
			} else {
				to_sq_tv.setVisibility(View.GONE);
			}
			// to_comment_tv.setVisibility(View.VISIBLE);
			// // 评价
			// if
			// (mOrderDetailBean.orderDetail.orderDetailList.get(i).RECEIVING_STATUS.equals("2")
			// &&
			// mOrderDetailBean.orderDetail.orderDetailList.get(i).COMMENTS_STATUS.equals("0")
			// ) {
			// to_comment_tv.setVisibility(View.VISIBLE);
			// to_comment_tv.setText("评价");
			// } else {
			// to_comment_tv.setVisibility(View.GONE);
			// to_comment_tv.setText("已评价");
			// }
			// 确认收货
			if (mOrderDetailBean.orderDetail.STATUS == WAIT_RECEIVE
					&& g_item.RECEIVING_STATUS.equals("1")) {
				to_receive_tv.setVisibility(View.VISIBLE);
			} else {
				to_receive_tv.setVisibility(View.GONE);
			}
			// 跳转商品详情订单
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(MyOrderDetailActivity.this,
							GoodsDetailActivity.class);
					intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
							g_item.COMMODITY_ID);
					startActivity(intent);
				}
			});

			to_receive_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					commodityId = g_item.COMMODITY_ID;
					Intent i = new Intent(MyOrderDetailActivity.this,
							CommonConfrimCancelDialog.class);
					i.putExtra(CommonConfrimCancelDialog.TASK,
							CommonConfrimCancelDialog.TASK_SURE_RECEIVE);
					startActivityForResult(i, request_receive);
				}
			});

			to_yuyue_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					Intent i = new Intent();
					i.setClass(MyOrderDetailActivity.this,
							YuYueShenQingActivity.class);
					i.putExtra("orderCode",
							mOrderDetailBean.orderDetail.ORDER_CODE);
					startActivity(i);
					finish();
				}
			});

			to_sq_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					appVerifyChangeAndReturn(g_item.ORDER_ID,
							g_item.COMMODITY_ID);
				}
			});

			// to_comment_tv.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			//
			// Intent i = new Intent();
			// i.setClass(MyOrderDetailActivity.this,
			// OrderCommentActivity.class);
			// i.putExtra("goodlist",
			// mOrderDetailBean.orderDetail.orderDetailList);
			// int flag = 0;
			// // 评价
			// if (mOrderDetailBean.orderDetail.STATUS == FINISH
			// &&
			// !mOrderDetailBean.orderDetail.ORDER_COMMENTS_STATUS.equals("1"))
			// {
			// flag = 0;
			// } else {
			// flag = 1;
			// }
			// i.putExtra("flag", flag);
			// startActivity(i);
			// finish();
			// }
			// });
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);// StringUtils.DpToPx(mContext,
																			// 100));
			od_goodslist_layout.addView(view, params);

		}

		// 评价
		if (mOrderDetailBean.orderDetail.STATUS == FINISH
		// && !mOrderDetailBean.orderDetail.ORDER_COMMENTS_STATUS
		// .equals("1")
						) {
			order_comment.setVisibility(View.VISIBLE);
		} else {
			order_comment.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == request_receive && resultCode == RESULT_OK) {
			postSureReceive(commodityId);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 确认收货
	 */

	private void postSureReceive(String commodityId) {

		RequstClient.makeSureOrder(order_id, commodityId,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {

						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "makeSureOrCancel:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							MyOrderActivity.setFlush(true);
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
	 * 订单详情
	 */
	private void initData() {

		if (order_id == null) {
			return;
		}
		RequstClient.getOrderDetail(order_id, new CustomResponseHandler(this) {

			@Override
			public void onFinish() {
				super.onFinish();
				UIHelper.cloesLoadDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "getOrderDetail:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (obj.has("LOGISTICS_FARE_FLAG")) {
						fjt_mianyunfei.setText(obj
								.getString("LOGISTICS_FARE_FLAG"));
					}

					mOrderDetailBean = new Gson().fromJson(content,
							OrderDetailBean.class);
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

	/**
	 * 验证是否能申请售后
	 */
	private void appVerifyChangeAndReturn(final String orderid,
			final String commodityId) {
		RequstClient.appVerifyChangeAndReturn(orderid, commodityId,
				new CustomResponseHandler(this) {

					@Override
					public void onFinish() {
						super.onFinish();
						UIHelper.cloesLoadDialog();
					}

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
								return;
							}
							Intent i = new Intent();
							i.setClass(MyOrderDetailActivity.this,
									ApplyAfterSaleActivity.class);
							i.putExtra("orderId", orderid);
							i.putExtra("goodId", commodityId);
							i.putExtra("totalPrice",
									mOrderDetailBean.orderDetail.TOTAL_PRICE);
							i.putExtra("flag", obj.getString("flag"));
							if (!obj.isNull("consignee_name"))
								i.putExtra("consignee_name",
										obj.getString("consignee_name"));
							if (!obj.isNull("consignee_phone"))
								i.putExtra("consignee_phone",
										obj.getString("consignee_phone"));
							if (!obj.isNull("consignee_address"))
								i.putExtra("consignee_address",
										obj.getString("consignee_address"));
							if (!obj.isNull("returnRate"))
								i.putExtra("returnRate",
										obj.getString("returnRate"));
							startActivity(i);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
	}

	public class OrderDetailBean implements Serializable {
		private static final long serialVersionUID = 1L;
		public OrderDetailInfo orderDetail = new OrderDetailInfo();

		public class OrderDetailInfo implements Serializable {
			private static final long serialVersionUID = 1L;
			// 订单状态：1未付款 2 已付款 3 待发货 4待收获 5 交易完成 6、取消
			public String INVOICES_TYPE;// 发票0普通1增值
			public int INVOICES_ID = 0;
			public String LOGISTICS_FARE; // 物流费用
			public String ORDER_CODE; // 订单编号
			public String POINTS_DEDUCTION; // 积分
			public String BENEFIT_INFORMATION;// 折扣
			public int STATUS;// 订单状态
			public String RECEIPT_TIME;// 配送时间1休息日2:工作日3:均可

			public String PAYMENTMETHOD;// 0支付宝，1财付通，2银行卡
			public String CONSIGNEE_PHONE;// 手机号
			public String CONSIGNEE_ADDRESS;// 详细地址
			public String TOTAL_PRICE;// 总价
			public String CONSIGNEE_NAME;// 用户名称
			public String COUPON;// 乐虎劵数量
			public String SHIPPING_METHOD;// 配送方式
			public String ORDER_COMMENTS_STATUS;// 订单评论状态0 未评论 1 已评论

			public ArrayList<GoodItem> orderDetailList = new ArrayList<GoodItem>();

			public String PERPAID_CARD;// 预付卡需要付的钱

			public String curDate;// 服务器时间
			public int dayReplace;// 换货期限
			public int dayReturn;// 退货期限
			public String BUY_MESSAGE;// 备注信息
		}

		public class GoodItem implements Serializable {

			private static final long serialVersionUID = 1L;

			public String COMMODITY_ID;// 商品id
			public String COMMODITY_NAME;// 商品名称
			public String BUY_QTY;// 数量
			public String COMMODITY_PRICE;// 商品价格
			public String COMMODITY_IMAGE_PATH;// 商品图标
			public String SPECVALUE; // 颜色、尺寸
			public String COMMENTS_STATUS;// 状态0没有评价，1已经评价过了
			public String ORDER_ID;
			public String BESPEAK_STATUS;// 1没有预约，2已经预约过了
			public String RECEIVING_STATUS;// 1未收到 2收到
			public String AFTERMARKET_STATUS;// 1没有 2 已经售后申请了
			public String PURCHASE_PRICE;// 商品折后价格

			public String RECEIVING_TIME = null;// 收货时间
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 验证时间
	 * 
	 * @param curDate
	 * @param comTime
	 * @param days
	 * @return
	 */
	public boolean isTimeOn(String curDate, String comTime, int days) {
		if (StringUtils.isBlank(curDate) || StringUtils.isBlank(comTime))
			return false;
		Date com = StringUtils.toDate(comTime);
		Date nowTime = StringUtils.toDate(curDate);

		long comSecond = com.getTime();
		long nowSecond = nowTime.getTime();

		if (comSecond <= 0 || nowSecond <= 0) {
			return false;
		} else {
			long tempSec = nowSecond - comSecond;
			if (tempSec <= 0) {
				return false;
			} else {
				long aDay = 3600000 * 24;
				int comday = (int) (tempSec / aDay);

				return comday < days ? true : false;
			}
		}
	}
}
