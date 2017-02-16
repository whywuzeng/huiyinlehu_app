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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;

public class AfterSaleDetailActivity extends BaseActivity {

	public final static String TAG = "AfterSaleDetailActivity";
	TextView left_ib, middle_title_tv;
	// 退换货id、退换货状态
	String returnId;
	int back_statu;
	// 商品id,商品名称
	String commodityId, goodName;
	SaleDetailBean mSaleDetailBean;

	// 布局1、布局2、布局3、布局4
	LinearLayout layout_1, layout_2, layout_3, layout4_invisible_title, layout4_invisible;
	// 布局4
	RelativeLayout layout_4;
	TextView ly4_top_title_statu;
	// 布局1:时间、地址、填写物流、上门取货
	TextView ly1_time_tv, ly1_addr_tv, ly1_write_logisitic_tv, ly1_lehu_logistic_tv;
	// 布局2：快递、物流单号,物流单号布局
	TextView ly2_logistic, ly2_logistic_code, layout2_statu_tip;
	LinearLayout ly2_logistic_code_layout, merchant_tips_layout;
	// 布局3：快递、物流单号、查看物流进度
	TextView ly3_logistic, ly3_logistic_code, ly3_check_logistic;
	// 公共部分：账号、用户名、银行、原因
	TextView sale_account_tv, sale_username_tv, sale_bank_tv, sale_reason_tv;
	// 公共部分：商品名称、商品状态、类型、退款金额
	TextView good_name_tv, good_statu_tv, sale_type_tv, sale_total_price_tv;

	// 退货状态：取消退款7、商家审核8、商家审核通过9、商家审核失败10、
	// 买家发货11、商家收货12、商家退款13、退货成功14

	public static final int BACK_CANCEL = 7, BACK_AUDIT = 8, AUDIT_SUCCESS = 9, AUDIT_UNSUCESS = 10, SEND_BACK = 11,
			BACK_RECEIVE = 12, BACK_MONEY = 13, BACK_FINISH = 14;

	// 换货状态：维权审核15,买家请退货16,维权拒绝17,买家已发货18,
	// 商品检测19,商家发货20,取消换货21,买家收货22,维权完成23

	public static final int CHANGE_AUDIT = 15, CHANGE_BACKOFF = 16, CHANGE_REFUSE = 17, CHANGE_SENDED = 18, CHANGE_DETECT = 19,
			CHANGE_REBACK = 20, CHANGE_CANCEL = 21, CHANGE_ACCEPT = 22, CHANGE_FINISH = 23;

	String[] order_status = { "", "未付款", "订单审核", "待发货", "待收货", "交易完成", "取消订单", "取消退款", "商家审核", "商家审核通过", "商家审核失败", "买家发货中",
			"商家已收货", "商家已退款", "退货成功", "换货申请", "商家审核中", "审核拒绝", "买家发货", "商品检测", "商家发货", "取消换货", "买家收货", "维权完成", "预约申请", "审核中",
			"审核成功", "审核失败", "取消" };

	private static String BACK_TYPE = "1", CHANGE_TYPE = "2";
	private static String CURRENT_TYPE;

	private int request_code = 123;
	private String flag;
	private String pushFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.after_sale_detail);

		initView();
		initData();

	}

	private void refreshUI() {
		// 时间、地址
		ly1_time_tv.setText(mSaleDetailBean.commodityReturnDetail.STATUS_DESC);
		ly1_addr_tv.setText(Html.fromHtml(mSaleDetailBean.commodityReturnDetail.STATUS_DESC1));
		// 物流、快递号
		ly2_logistic.setText(mSaleDetailBean.commodityReturnDetail.LOGISTICS_NAME);
		ly2_logistic_code.setText(mSaleDetailBean.commodityReturnDetail.EXPRESS_NO);

		if (!(mSaleDetailBean.commodityReturnDetail.LOGISTICS_NAME == null)
				&& mSaleDetailBean.commodityReturnDetail.LOGISTICS_NAME.equals("上门取货")) {
			ly2_logistic_code_layout.setVisibility(View.GONE);
		} else {
			ly2_logistic_code_layout.setVisibility(View.VISIBLE);
		}

		// 布局三
		ly3_logistic.setText(mSaleDetailBean.commodityReturnDetail.LOGISTICS_NAME);
		ly3_logistic_code.setText(mSaleDetailBean.commodityReturnDetail.EXPRESS_NO);
		// 布局4
		ly4_top_title_statu.setText(mSaleDetailBean.commodityReturnDetail.STATUS_NAME);

		// 公共部分
		sale_account_tv.setText(mSaleDetailBean.commodityReturnDetail.ACCOUNT_NUMBER);
		sale_username_tv.setText(mSaleDetailBean.commodityReturnDetail.ACCOUNT_HOLDER);
		sale_bank_tv.setText(mSaleDetailBean.commodityReturnDetail.BANK);
		sale_reason_tv.setText(mSaleDetailBean.commodityReturnDetail.REASON);
		sale_total_price_tv.setText(MathUtil.priceForAppWithSign(mSaleDetailBean.commodityReturnDetail.RETURN_MONEY));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == request_code && resultCode == RESULT_OK) {
			postLogisticInfo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initData() {

		RequstClient.postAfterInfo(AppContext.getInstance().getUserId(), returnId, commodityId, CURRENT_TYPE, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postAfterInfo:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					mSaleDetailBean = new Gson().fromJson(content, SaleDetailBean.class);
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

	private void initView() {

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

		mSaleDetailBean = new SaleDetailBean();

		// 布局1、2、3
		layout_1 = (LinearLayout) findViewById(R.id.layout_1);
		layout_2 = (LinearLayout) findViewById(R.id.layout_2);
		layout_3 = (LinearLayout) findViewById(R.id.layout_3);
		// 布局4
		layout_4 = (RelativeLayout) findViewById(R.id.layout_4);
		layout4_invisible_title = (LinearLayout) findViewById(R.id.layout4_invisible_title);
		layout4_invisible = (LinearLayout) findViewById(R.id.layout4_invisible);

		ly4_top_title_statu = (TextView) findViewById(R.id.ly4_top_title_statu);

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("售后详情");

		/**
		 * 地址、时间 ；布局1 begin
		 */
		ly1_addr_tv = (TextView) findViewById(R.id.ly1_addr_tv);
		ly1_time_tv = (TextView) findViewById(R.id.ly1_time_tv);

		// 填写物流
		ly1_write_logisitic_tv = (TextView) findViewById(R.id.ly1_write_logisitic_tv);
		ly1_write_logisitic_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(AfterSaleDetailActivity.this, WriteLogisticNumActivity.class);
				i.putExtra("returnId", returnId);
				i.putExtra("commodityId", commodityId);
				i.putExtra("flag", flag);
				startActivity(i);
				finish();
			}
		});

		// 上门取货
		ly1_lehu_logistic_tv = (TextView) findViewById(R.id.ly1_lehu_logistic_tv);
		ly1_lehu_logistic_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(AfterSaleDetailActivity.this, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_SMQH);
				startActivity(i);
			}
		});
		/**
		 * 布局1 end
		 */

		// 布局2
		ly2_logistic = (TextView) findViewById(R.id.ly2_logistic);
		ly2_logistic_code = (TextView) findViewById(R.id.ly2_logistic_code);
		ly2_logistic_code_layout = (LinearLayout) findViewById(R.id.ly2_logistic_code_layout);
		layout2_statu_tip = (TextView) findViewById(R.id.layout2_statu_tip);

		merchant_tips_layout = (LinearLayout) findViewById(R.id.merchant_tips_layout);

		// 布局3
		ly3_logistic = (TextView) findViewById(R.id.ly3_logistic);
		ly3_logistic_code = (TextView) findViewById(R.id.ly3_logistic_code);
		ly3_check_logistic = (TextView) findViewById(R.id.ly3_check_logistic);
		// ly3_check_logistic.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// Intent i = new Intent(AfterSaleDetailActivity.this,
		// CheckLogisticActivity.class);
		// i.putExtra("order_id", returnId);
		// startActivity(i);
		// }
		// });

		// 商品名称、商品状态、售后类型、退款金额
		good_name_tv = (TextView) findViewById(R.id.good_name_tv);
		good_statu_tv = (TextView) findViewById(R.id.good_statu_tv);
		sale_type_tv = (TextView) findViewById(R.id.sale_type_tv);
		sale_total_price_tv = (TextView) findViewById(R.id.sale_total_price_tv);
		// 账号、用户名、银行、原因
		sale_account_tv = (TextView) findViewById(R.id.sale_account_tv);
		sale_username_tv = (TextView) findViewById(R.id.sale_username_tv);
		sale_bank_tv = (TextView) findViewById(R.id.sale_bank_tv);
		sale_reason_tv = (TextView) findViewById(R.id.sale_reason_tv);

		// 获取的数据
		returnId = getIntent().getStringExtra("returnId");
		back_statu = getIntent().getIntExtra("statu", 0);
		commodityId = getIntent().getStringExtra("commodityId");
		goodName = getIntent().getStringExtra("goodName");
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		if (back_statu < 0) {
			back_statu = 0;
		}

		if (back_statu < CHANGE_AUDIT) {
			sale_type_tv.setText("退货");
			CURRENT_TYPE = BACK_TYPE;
			flag = WriteLogisticNumActivity.BACK_FLAG;
			merchant_tips_layout.setVisibility(View.VISIBLE);
		} else if (back_statu > BACK_FINISH) {
			sale_type_tv.setText("换货");
			CURRENT_TYPE = CHANGE_TYPE;
			flag = WriteLogisticNumActivity.CHANGE_FLAG;
			merchant_tips_layout.setVisibility(View.GONE);
		}
		layout2_statu_tip.setText(order_status[back_statu]);
		good_statu_tv.setText(order_status[back_statu]);
		good_name_tv.setText(goodName);

		if (back_statu == AUDIT_SUCCESS || back_statu == CHANGE_BACKOFF) {
			layout_1.setVisibility(View.VISIBLE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.GONE);

			layout_4.setVisibility(View.GONE);
			if (back_statu == AUDIT_SUCCESS) {
				layout4_invisible_title.setVisibility(View.VISIBLE);
				layout4_invisible.setVisibility(View.VISIBLE);
			} else {
				layout4_invisible_title.setVisibility(View.GONE);
				layout4_invisible.setVisibility(View.GONE);
			}

		} else if (back_statu == SEND_BACK || back_statu == BACK_RECEIVE || back_statu == BACK_MONEY) {

			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.VISIBLE);
			layout_3.setVisibility(View.GONE);

			layout_4.setVisibility(View.GONE);
			layout4_invisible_title.setVisibility(View.VISIBLE);
			layout4_invisible.setVisibility(View.VISIBLE);

		} else if (back_statu == CHANGE_SENDED || back_statu == CHANGE_DETECT || back_statu == CHANGE_ACCEPT
				|| back_statu == CHANGE_FINISH) {

			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.VISIBLE);
			layout_3.setVisibility(View.GONE);

			layout_4.setVisibility(View.GONE);
			layout4_invisible_title.setVisibility(View.GONE);
			layout4_invisible.setVisibility(View.GONE);

		} else if (back_statu == BACK_FINISH) {
			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.VISIBLE);

			layout_4.setVisibility(View.GONE);
			layout4_invisible_title.setVisibility(View.VISIBLE);
			layout4_invisible.setVisibility(View.VISIBLE);

		} else {

			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.GONE);

			layout_4.setVisibility(View.VISIBLE);
			layout4_invisible_title.setVisibility(View.GONE);
			layout4_invisible.setVisibility(View.GONE);

		}

	}

	/***
	 * 乐虎上门取货
	 */
	private void postLogisticInfo() {

		RequstClient.writeLogistic("上门取货", "", returnId, commodityId, "", "", new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "writeLogistic:" + content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getBaseContext(), "我们会尽快安排上门取货，请耐心等待！", Toast.LENGTH_SHORT).show();
					MyOrderActivity.setFlush(true);
					MyOrderActivity.setBackList(MyOrderActivity.second);
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

	public class SaleDetailBean {

		SaleDetailInfo commodityReturnDetail = new SaleDetailInfo();

		public class SaleDetailInfo {

			String STATUS_DESC;// 时间
			String STATUS_DESC1;// 地址

			String EXPRESS_NO;// 物流单号
			String LOGISTICS_NAME;// 物流公司

			String REASON;// 退货原因
			String BANK;// 银行
			String ACCOUNT_NUMBER;// 账号
			String ACCOUNT_HOLDER;// 账户
			String RETURN_MONEY;// 退货金额
			String STATUS_NAME;// 表头

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

}
