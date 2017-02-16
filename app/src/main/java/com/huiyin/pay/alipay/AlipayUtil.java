package com.huiyin.pay.alipay;

import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.huiyin.api.URLs;

/***
 * 支付宝支付工具类
 * 
 * @author Administrator
 * 
 */
public class AlipayUtil {
	public static final String TAG = "alipay-sdk";

	private final int RQF_PAY = 1;

	private final int RQF_LOGIN = 2;

	private Context context;

	public AlipayUtil(Context context) {
		this.context = context;
	}

	/***
	 * 封装提交支付宝订单信息
	 * 
	 * @param order_no
	 * @param order_name
	 * @param order_desc
	 * @param order_fee
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getNewOrderInfo(String order_no, String order_name,
			String order_desc, String order_fee) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(order_no);
		sb.append("\"&subject=\"");
		sb.append(order_name);
		sb.append("\"&body=\"");
		sb.append(order_desc);
		sb.append("\"&total_fee=\"");
		sb.append(order_fee);
		sb.append("\"&notify_url=\""); // 暗调接口
		// 网址需要做URL编码
		sb.append(URLEncoder.encode(URLs.ALIPAY_URL));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://app.lehumall.com/hy/alipayAfterBlack.do"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"10m");
		sb.append("\"");
		return new String(sb);
	}

	/***
	 * 签名类型
	 * 
	 * @return
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/***
	 * 支付宝支付
	 * 
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public void alipayOrder(String order_no, String order_name,
			String order_desc, String order_fee) {
		try {
			String info = getNewOrderInfo(order_no, order_name, order_desc,
					order_fee);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i(TAG, "info = " + info);
			final String orderInfo = info;

			new Thread() {
				public void run() {
					AliPay alipay = new AliPay((Activity) context, mHandler);
					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);
					String result = alipay.pay(orderInfo);
					Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			System.out.println("信息为----" + msg.obj + "----");
			Result result = new Result((String) msg.obj);
			result.parseResult();

			switch (msg.what) {
			case RQF_PAY:
				if ("9000".equals(result.resultStatusCode)) {
					// 支付成功，调用接口
					if (callBack != null) {
						callBack.payResultCallBack(0, result.resultStatusCode);
					}
				} else {
					if (callBack != null) {
						callBack.payResultCallBack(1, result.resultStatusCode);
					}
				}
				break;
			case RQF_LOGIN: {
				if ("9000".equals(result.resultStatusCode)) {

				} else {
					Toast.makeText(context, result.resultStatus,
							Toast.LENGTH_LONG).show();
				}
			}
				break;
			default:
				break;
			}
		};
	};

	public interface PayCallBack {
		void payResultCallBack(int type, final String resultStatus);
	}

	private PayCallBack callBack;

	public void setPayResultCallBack(PayCallBack callBack) {
		this.callBack = callBack;
	}
}
