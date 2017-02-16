package com.huiyin.ui.shoppingcar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.BaseBean;
import com.huiyin.pay.alipay.AlipayUtil;
import com.huiyin.pay.alipay.AlipayUtil.PayCallBack;
import com.huiyin.pay.wxpay.PayUtil;
import com.huiyin.ui.servicecard.RechargeServiceCard;
import com.huiyin.utils.BaseHelper;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wxapi.WXPayEntryActivity;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class CommitOrderActivity extends BaseActivity implements OnClickListener {

	public static final String LOG_TAG = "PayDemo";
	private int mGoodsIdx = 0;
	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private static final String MODE = "00";

	private TextView left_rb, middle_title_tv, right_rb;
	private TextView pay_order_num_tv, pay_order_price_tv;
	private LinearLayout payway_yl;
	private LinearLayout payway_wx;
	private LinearLayout payway_zfb_android;
	private LinearLayout payway_serviceCard;
	private TextView serviceCard_info;

	private String orderId;// 订单号
	private String number;// 订单编号

	private String mTitle = "汇银乐虎";// 标题
	private String detail = "品质才是硬道理";// 详细
	private String mBody = "汇银家电";// 商品

	private float price;// 价格

	private float perpaidCard;// 预付卡优惠价

	private String TN;

	private int payType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_commit_order);

		number = getIntent().getStringExtra("number");// 订单编号
		AppContext.number = number;
		orderId = getIntent().getStringExtra("serialNum");// 订单id
		System.out.println("订单id----" + orderId);
		AppContext.orderId = orderId;
		String price = getIntent().getStringExtra("totalPrice");
		if (null != price && !"".equals(price)) {
			this.price = Float.parseFloat(price);
			AppContext.price = price;
		}
		String priceCard = getIntent().getStringExtra("perpaidCard");
		if (!StringUtils.isBlank(priceCard)) {
			this.perpaidCard = Float.parseFloat(priceCard);
		}
		initView();

		checkPrice();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		number = getIntent().getStringExtra("number");// 订单编号
		AppContext.number = number;
		orderId = getIntent().getStringExtra("serialNum");// 订单id
		System.out.println("订单id----" + orderId);
		AppContext.orderId = orderId;
		String price = getIntent().getStringExtra("totalPrice");
		if (null != price && !"".equals(price)) {
			this.price = Float.parseFloat(price);
			AppContext.price = price;
		}
		String priceCard = getIntent().getStringExtra("perpaidCard");
		if (!StringUtils.isBlank(priceCard)) {
			this.perpaidCard = Float.parseFloat(priceCard);
		}
		initView();
		
		checkPrice();
	}

	@SuppressWarnings("static-access")
	private void initView() {

		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		pay_order_price_tv = (TextView) findViewById(R.id.pay_order_price_tv);
		payway_yl = (LinearLayout) findViewById(R.id.payway_yl);
		payway_wx = (LinearLayout) findViewById(R.id.payway_wx);
		payway_zfb_android = (LinearLayout) findViewById(R.id.payway_zfb_android);
		payway_serviceCard = (LinearLayout) findViewById(R.id.payway_serviceCard);
		serviceCard_info = (TextView) findViewById(R.id.serviceCard_info);

		right_rb.setVisibility(View.GONE);
		payway_yl.setOnClickListener(this);
		payway_wx.setOnClickListener(this);
		payway_zfb_android.setOnClickListener(this);

		if (AppContext.getInstance().getUserInfo() != null && AppContext.getInstance().getUserInfo().bdStatus == 1) {
			payway_serviceCard.setVisibility(View.VISIBLE);
			payway_serviceCard.setOnClickListener(this);
			if (perpaidCard > 0 && perpaidCard < price) {
				serviceCard_info.setText("使用服务卡支付价格为" + MathUtil.priceForAppWithSign(perpaidCard) + "元");
			}
		}

		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("在线支付");

		right_rb = (TextView) findViewById(R.id.right_ib);
		right_rb.setVisibility(View.GONE);

		pay_order_num_tv = (TextView) findViewById(R.id.pay_order_num_tv);
		pay_order_num_tv.setText(number);
		pay_order_price_tv.setText(MathUtil.priceForAppWithSign(price) + "元");

	}

	@Override
	public void onClick(View v) {
		if (0 >= price) {// 如果金额小于等于0,不进入支付
			BaseHelper.showDialog(CommitOrderActivity.this, "提示", "支付金额错误，请确认！", R.drawable.infoicon);
			return;
		}
		switch (v.getId()) {
		case R.id.payway_yl:// 银联支付
			payType = 2;
			AppContext.payType = 2;
			RequstClient.payForUP(URLs.TN_URL, number, new CustomResponseHandler(this, true) {
				@Override
				public void onFinish() {

					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, String content) {

					super.onSuccess(statusCode, content);
					TN = "";
					try {
						JSONObject json = new JSONObject(content);
						json.optInt("type");
						json.optString("msg");
						TN = json.optString("tn");
					} catch (JSONException e) {

						e.printStackTrace();
					}
					System.out.println(TN);
					System.out.println(MODE);
					UPPayAssistEx.startPayByJAR(CommitOrderActivity.this, PayActivity.class, null, null, TN, MODE);
					// UPPayAssistEx.startPay(CommitOrderActivity.this,
					// null, null, TN, MODE);
				}

				@Override
				public void onFailure(String error, String errorMessage) {

					super.onFailure(error, errorMessage);
				}
			});
			break;
		case R.id.payway_zfb_android:// 支付宝客户端支付
			payType = 1;
			AppContext.payType = 1;
			RequstClient.payForAlipay(URLs.ALIPAY_BEFORE, number, new CustomResponseHandler(this, true) {
				@Override
				public void onFinish() {

					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, String content) {

					super.onSuccess(statusCode, content);
					try {
						JSONObject json = new JSONObject(content);
						json.optInt("type");
						json.optString("msg");
						if (price == Float.parseFloat(json.optString("realMoney"))) {
							startPay(number, price);
						}
					} catch (JSONException e) {

						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(String error, String errorMessage) {

					super.onFailure(error, errorMessage);
				}
			});
			break;
		case R.id.payway_wx:// 微信支付
			payType = 3;
			AppContext.payType = 3;
			PayUtil wxapi = new PayUtil(mContext, number, mBody, String.valueOf((int) (price * 100)));
			wxapi.Pay();
			break;
		case R.id.payway_serviceCard:
			payType = 4;
			AppContext.payType = 4;
			showPayDialog();
			break;
		}
	}

	/**
	 * 支付宝开始支付
	 * 
	 * @param orderString
	 * @param money
	 */
	public void startPay(String orderString, float money) {
		AlipayUtil alipay = new AlipayUtil(this);
		alipay.setPayResultCallBack(new PayCallBack() {
			@Override
			public void payResultCallBack(int type, String resultStatus) {

				if (type == 0) {// 支付成功
					requestBackToWeb(2, "", resultStatus);
				} else {// 支付失败
					goToPayResult(2);
				}
			}
		});
		while (true) {
			alipay.alipayOrder(orderString, mTitle, detail, String.valueOf(money));
			return;
		}

	}

	/**
	 * 调用服务端回调
	 * 
	 * @param type
	 *            类型
	 * @param tn
	 *            订单信息
	 * @param resultStatus
	 *            状态码
	 */
	public void requestBackToWeb(int type, String tn, final String resultStatus) {
		RequstClient.toRequestWebResult(type, number, tn, new CustomResponseHandler(this, true) {
			@Override
			public void onFinish() {

				super.onFinish();
				goToPayResult(1);
			}

			@Override
			public void onSuccess(int statusCode, String content) {

				super.onSuccess(statusCode, content);
				// 调用pay方法进行支付
				try {
					JSONObject json = new JSONObject(content);
					String type = json.optString("type");// 返回类型
					String msg = json.optString("msg");// 返回信息
					// if ("1".equals(type)) {// 回调返回支付成功
					goToPayResult(1);
					// } else {
					// }
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String error, String errorMessage) {

				super.onFailure(error, errorMessage);
				goToPayResult(1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}
		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		// mProgress.dismiss();
		if (str.equalsIgnoreCase("success")) {// 支付成功
			msg = "支付成功！";
			requestBackToWeb(1, TN, resultCode + "");
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
			goToPayResult(2);
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
			goToPayResult(2);
		}
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("支付结果通知");
		// builder.setMessage(msg);
		// builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
	}

	/**
	 * 跳转支付结果页面
	 * 
	 * @param type
	 *            1.支付成功 2.支付失败
	 */
	public void goToPayResult(int type) {
		Intent i = new Intent();
		i.setClass(CommitOrderActivity.this, WXPayEntryActivity.class);
		i.putExtra("resultType", type);
		startActivity(i);
		CommitOrderActivity.this.finish();
	}

	private Dialog mDialog;

	private void showPayDialog() {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.service_card_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		TextView tip = (TextView) mView.findViewById(R.id.com_tip_tv);
		tip.setText("确认支付？");
		final EditText pwd = (EditText) mView.findViewById(R.id.com_message_tv);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		yes.setText("确定支付");
		cancle.setText("取消支付");
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				String password = pwd.getText().toString();
				if (StringUtils.isBlank(password)) {
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				PayByCard(password);
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);

		// Window dialogWindow = mDialog.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// lp.height = DensityUtil.dip2px(mContext, 350); // 高度设置为屏幕的0.2
		// lp.width = DensityUtil.dip2px(mContext, 590); // 宽度设置为屏幕的0.8
		// dialogWindow.setAttributes(lp);

		mDialog.show();
	}

	@SuppressWarnings("static-access")
	private void PayByCard(String password) {
		UserInfo mUsrInfo = AppContext.getInstance().getUserInfo();
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					goToPayResult(1);
					finish();
				} else {
					showFailDialog(bean.msg);
					// goToPayResult(2);
				}

			}
		};
		String money;
		if (perpaidCard > 0) {
			money = MathUtil.keep2decimal(perpaidCard);
		} else {
			money = MathUtil.keep2decimal(price);
		}
		RequstClient.appCardRedeem(handler, mUsrInfo.userId, mUsrInfo.cardNum, password, money, number, mUsrInfo.token, orderId);
	}

	private void showFailDialog(String msg) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.service_card_pay_fail_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		TextView pwd = (TextView) mView.findViewById(R.id.com_message_tv);
		pwd.setText(msg);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent fuwu_intent = new Intent();
				fuwu_intent.setClass(mContext, RechargeServiceCard.class);
				mContext.startActivity(fuwu_intent);
				finish();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);

		// Window dialogWindow = mDialog.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// lp.height = DensityUtil.dip2px(mContext, 350); // 高度设置为屏幕的0.2
		// lp.width = DensityUtil.dip2px(mContext, 590); // 宽度设置为屏幕的0.8
		// dialogWindow.setAttributes(lp);

		mDialog.show();
	}

	public void checkPrice() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				OrderPriceBean bean = OrderPriceBean.explainJson(content, mContext);
				if (bean.type > 0) {
					if (bean.perpaidCard > 0)
						perpaidCard = bean.perpaidCard;
					if (bean.totalPrice > 0) {
						AppContext.price = String.valueOf(bean.totalPrice);
						price = bean.totalPrice;
					}
					pay_order_price_tv.setText(MathUtil.priceForAppWithSign(price) + "元");
					if (payway_serviceCard.getVisibility() == View.VISIBLE) {
						if (perpaidCard > 0 && perpaidCard < price) {
							serviceCard_info.setText("使用服务卡支付价格为" + MathUtil.priceForAppWithSign(perpaidCard) + "元");
						}
					}
				}
			}

		};
		RequstClient.orderPrice(handler, orderId);
	}
}
