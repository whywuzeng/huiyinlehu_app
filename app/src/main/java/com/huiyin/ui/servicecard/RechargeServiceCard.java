package com.huiyin.ui.servicecard;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.zxing.common.detector.MathUtils;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ServiceCardPrivilegeBean;
import com.huiyin.bean.ServiceCardPrivilegeBean.PrivilegeItem;
import com.huiyin.pay.alipay.AlipayUtil;
import com.huiyin.pay.alipay.AlipayUtil.PayCallBack;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 便民服务卡充值
 * 
 * @author zhangshuaijun
 * 
 */
public class RechargeServiceCard extends BaseActivity {

	TextView left_ib, middle_title_tv;
	private EditText money;
	private String moneytext = "";
	private double balance;

	private LinearLayout privilege_layout;
	private RadioGroup privilege_chose;

	private RelativeLayout zhifubao;
	private RelativeLayout yinlian;

	private String mTitle = "汇银乐虎";// 标题
	private String detail = "品质才是硬道理";// 详细

	private int pid = 0;
	private int payMode = 1; // 支付方式 1为支付宝 2为银联

	private String orderId; // 订单号

	private String TN;

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private static final String MODE = "00";

	private ServiceCardPrivilegeBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge_servicecard_layout);
		initView();
		setListener();
		initData();
	}

	private void initView() {
		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("便民生活服务卡充值");

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		privilege_layout = (LinearLayout) findViewById(R.id.privilege_layout);
		privilege_chose = (RadioGroup) findViewById(R.id.privilege_chose);

		money = (EditText) findViewById(R.id.money);
		zhifubao = (RelativeLayout) findViewById(R.id.zhifubao);
		yinlian = (RelativeLayout) findViewById(R.id.yinlian);
	}

	private void payRequst() {
		String moneyText = money.getText().toString();
		RequstClient.appAddRechargeOrder(AppContext.getInstance().getUserId(), AppContext.getInstance().getUserInfo().cardNum, moneyText,
				AppContext.getInstance().getUserInfo().token, payMode + "", pid + "", new CustomResponseHandler(this, true) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}
							// money.setText("0.01");
							orderId = obj.getString("orderId");
							pay(orderId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});

	}

	private void ylPay(String orderId) {
		RequstClient.payForUP(URLs.TN_URL, orderId, new CustomResponseHandler(this, true) {
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
				UPPayAssistEx.startPayByJAR(RechargeServiceCard.this, PayActivity.class, null, null, TN, MODE);
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
			}
		});
	}

	private void setListener() {
		privilege_chose.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == -1)
					return;
				RadioButton mRadioButton = (RadioButton) group.findViewById(checkedId);
				if (mRadioButton.isChecked()) {
					PrivilegeItem temp = (PrivilegeItem) mRadioButton.getTag();
					pid = temp.id;
					moneytext = MathUtil.priceForAppWithOutSign(temp.money);
					money.setText(MathUtil.priceForAppWithOutSign(temp.money));
					
				}
			}
		});
		zhifubao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (money.getText().toString().equals("")) {
					Toast.makeText(mContext, "请输入要充值的金额", Toast.LENGTH_LONG).show();
					return;
				}
				payMode = 1;
				payRequst();
			}
		});
		yinlian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (money.getText().toString().equals("")) {
					Toast.makeText(mContext, "请输入要充值的金额", Toast.LENGTH_LONG).show();
					return;
				}
				payMode = 2;
				payRequst();
			}
		});

		money.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				Log.d("onTextChanged", s + "+" + start + "+" + before + "+" + count);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				Log.d("beforeTextChanged", s + "+" + start + "+" + count + "+" + after);
			}

			@Override
			public void afterTextChanged(Editable s) {
//				Log.d("afterTextChanged", s.toString());
				if (!moneytext.equals(s.toString())) {
					privilege_chose.clearCheck();
					pid = 0;
					moneytext = s.toString();
				}
				money.setSelection(s.length());
			}
		});
	}

	private void pay(String orderId) {
		switch (payMode) {
		case 1: // 支付宝
			startPay(orderId);
			break;
		case 2: // 银联
			ylPay(orderId);
			break;
		}

	}

	/**
	 * 支付宝开始支付
	 * 
	 * @param orderString
	 * @param money
	 */
	public void startPay(String orderString) {
		AlipayUtil alipay = new AlipayUtil(this);
		alipay.setPayResultCallBack(new PayCallBack() {
			@Override
			public void payResultCallBack(int type, String resultStatus) {
				if (type == 0) {// 支付成功
					// requestBackToWeb(2, orderId, resultStatus);
					AppContext.getInstance().getUserInfo().balance += Float.valueOf(money.getText().toString());
					
					UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
					UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
					goToPayResult(1);
				} else {// 支付失败
					goToPayResult(2);
				}

			}
		});
		while (true) {
			alipay.alipayOrder(orderString, mTitle, detail, String.valueOf(money.getText().toString()));
			return;
		}
	}

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
	}

	/**
	 * 调用服务端回调
	 * 
	 * @param type类型
	 * @param tn
	 *            订单信息
	 * @param resultStatus
	 *            状态码
	 */
	public void requestBackToWeb(int type, String tn, final String resultStatus) {
		RequstClient.toRequestWebResult(type, orderId, tn, new CustomResponseHandler(this, true) {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				// 调用pay方法进行支付
				try {
					JSONObject json = new JSONObject(content);
					String type = json.optString("type");// 返回类型
					String msg = json.optString("msg");// 返回信息
					if ("1".equals(type)) {// 回调返回支付成功
						AppContext.getInstance().getUserInfo().balance += Float.valueOf(money.getText().toString());
						
						UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
						UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
						goToPayResult(1);
					} else {
						goToPayResult(2, msg);
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
	}

	/**
	 * 跳转支付结果页面
	 * 
	 * @param type
	 *            1.支付成功 2.支付失败
	 */
	public void goToPayResult(int type, String msg) {
		Intent i = new Intent();
		i.setClass(RechargeServiceCard.this, RechargeSuccess.class);
		i.putExtra("resultType", type);
		i.putExtra("payMode", payMode);
		i.putExtra("msg", msg);
		startActivity(i);
		finish();
	}

	public void goToPayResult(int type) {
		goToPayResult(type, null);
	}

	private void initData() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				bean = ServiceCardPrivilegeBean.explainJson(content, mContext);
				if (bean.type > 0 && bean.data != null && bean.data.size() > 0) {
					UpdataView(bean.data);
				}
			}

		};
		RequstClient.appCardPromotionsList(handler);
	}

	private void UpdataView(ArrayList<PrivilegeItem> data) {
		privilege_layout.setVisibility(View.VISIBLE);
		for (PrivilegeItem temp : data) {
			RadioButton mRadioButton = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.privilege_radiobutton, null);
			mRadioButton.setText(temp.rechargeName);
			mRadioButton.setTag(temp);
			RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 10, 0, 10);
			mRadioButton.setLayoutParams(lp);
			privilege_chose.addView(mRadioButton);
		}
	}

}
