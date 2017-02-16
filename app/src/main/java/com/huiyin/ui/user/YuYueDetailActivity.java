package com.huiyin.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;

public class YuYueDetailActivity extends BaseActivity {

	private final static String TAG = "YuYueDetailActivity";
	TextView left_ib, middle_title_tv, cancel_btn_tv;
	String orderId = "";
	String pushFlag = "";
	YuYueInfoBean mYuYueInfoBean;
	TextView yy_type, yy_time, yy_part, yy_username, yy_phone, yy_addr,
			yy_remark, yy_audit_remark;
	// 预约状态
	TextView yuyue_statu_tv;

	TextView yuyue_parts_tv;

	private int request_code = 123;

	// 预约状态：预约申请、预约审核中、审核成功、审核失败、取消
	public static final int YY_APPLY = 24, YY_AUDIT = 25, YY_FAIL = 26,
			YY_SUCCESS = 27, YY_CANCEL = 28;

	String[] order_status = { "", "未付款", "订单审核", "待发货", "待收货", "交易完成", "取消订单",
			"取消退款", "商家审核", "商家审核通过", "商家审核失败", "买家发货中", "商家检测中", "商家退款中",
			"退货成功", "维权审核", "买家请退货", "维权拒绝", "买家已发货", "商品检测", "商家待发货", "取消换货",
			"买家收货", "维权完成", "预约申请", "预约审核中", "预约成功", "审核失败", "已取消预约" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue_detail);

		initView();
		initData();

	}

	private void initView() {

		orderId = getIntent().getStringExtra("orderId");

		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0"
				: getIntent().getStringExtra("pushFlag");
		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
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

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("预约详情");

		yuyue_statu_tv = (TextView) findViewById(R.id.yuyue_statu_tv);

		yuyue_parts_tv = (TextView) findViewById(R.id.yuyue_parts_tv);
		yy_type = (TextView) findViewById(R.id.yy_type);
		yy_time = (TextView) findViewById(R.id.yy_time);
		yy_part = (TextView) findViewById(R.id.yy_part);
		yy_username = (TextView) findViewById(R.id.yy_username);
		yy_phone = (TextView) findViewById(R.id.yy_phone);
		yy_addr = (TextView) findViewById(R.id.yy_addr);
		yy_remark = (TextView) findViewById(R.id.yy_remark);
		yy_audit_remark = (TextView) findViewById(R.id.yy_audit_remark);

		cancel_btn_tv = (TextView) findViewById(R.id.cancel_btn_tv);
		cancel_btn_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(YuYueDetailActivity.this,
						CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK,
						CommonConfrimCancelDialog.TASK_CANCEL_YUYUE);
				startActivityForResult(i, request_code);
			}
		});

	}

	private void refleshUI() {
		yuyue_statu_tv
				.setText(order_status[mYuYueInfoBean.bespeakOrderDetail.STATUS]);
		yy_type.setText(mYuYueInfoBean.bespeakOrderDetail.TYPE);
		yy_time.setText(mYuYueInfoBean.bespeakOrderDetail.STARTTIME + "----"
				+ mYuYueInfoBean.bespeakOrderDetail.ENDTIME);
		String parts = mYuYueInfoBean.bespeakOrderDetail.parts;
		if (parts != null && !parts.equals("")) {

			yy_part.setText(Html.fromHtml(parts));
			yy_part.setVisibility(View.VISIBLE);
			yuyue_parts_tv.setVisibility(View.VISIBLE);
		} else {

			yy_part.setVisibility(View.GONE);
			yuyue_parts_tv.setVisibility(View.GONE);
		}
		yy_username.setText(mYuYueInfoBean.bespeakOrderDetail.USERNAME);
		yy_phone.setText(mYuYueInfoBean.bespeakOrderDetail.PHONE);
		yy_addr.setText(mYuYueInfoBean.bespeakOrderDetail.ADDRESS);
		yy_remark.setText(mYuYueInfoBean.bespeakOrderDetail.REMARKS);

		if (mYuYueInfoBean.bespeakOrderDetail.STATUS == YY_AUDIT) {
			cancel_btn_tv.setVisibility(View.VISIBLE);
		} else {
			cancel_btn_tv.setVisibility(View.GONE);
		}

	}

	private void initData() {

		RequstClient.postYuYueInit(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postLogisticInfo:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					mYuYueInfoBean = new Gson().fromJson(content,
							YuYueInfoBean.class);

					refleshUI();
					String audit_remark = "";
					JSONObject bespeakOrderDetail = (JSONObject) obj
							.get("bespeakOrderDetail");
					if (bespeakOrderDetail.has("AUDIT_REMARKS")) {

						audit_remark = bespeakOrderDetail
								.getString("AUDIT_REMARKS");
						if (audit_remark == null) {
							audit_remark = "";
						}
					}
					yy_audit_remark.setText(audit_remark);

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

	private void cancelOrder() {

		RequstClient.postYuYueCancel(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postLogisticInfo:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == request_code && resultCode == RESULT_OK) {
			cancelOrder();
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	public class YuYueInfoBean {

		YuYueInfoItem bespeakOrderDetail = new YuYueInfoItem();

		public class YuYueInfoItem {

			String PHONE;
			String PARTS_ID;
			String REMARKS;
			String ADDRESS;
			int STATUS;
			String parts; // 商品支架部分
			String USERNAME;
			String STARTTIME;
			String ENDTIME;
			String TYPE;// 安装类型
			String NUM;

		}

	}

}
