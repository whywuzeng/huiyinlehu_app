package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.home.Logistics.LogisticLineView;
import com.huiyin.ui.home.Logistics.LogisticListBean;
import com.huiyin.ui.home.Logistics.LogisticListBean.ReLikeItem;
import com.huiyin.ui.home.Logistics.LogisticListBean.orderLogisticsItem;
import com.huiyin.ui.home.Logistics.ReLikeAdapter;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.MyGridView;

public class CheckLogisticActivity extends BaseActivity {
	private Context mContext;

	private TextView left_ib, middle_title_tv;
	private TextView orderNum, orderStatus;
	private TextView phoneNum;
	private LogisticLineView mLogisticLineView;
	private MyGridView mGridView;
	private ReLikeAdapter mAdapter;

	private LogisticListBean data;
	private String order_id;
	private String userId;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_logistics_layout);
		mContext = this;
		userId = AppContext.getInstance().getUserId();
		Intent i = getIntent();
		order_id = i.getStringExtra("order_id");
		findView();
		setListener();
		initData();

	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("查看物流");
		orderNum = (TextView) findViewById(R.id.logistic_order_num);
		orderStatus = (TextView) findViewById(R.id.logistic_order_status);
		phoneNum = (TextView) findViewById(R.id.logistic_order_phoneNum);
		String info = "配送公司  汇银乐虎 ";
		if (PreferenceUtil.getInstance(mContext).getHotLine() == null) {
			info += "<font color=\"#3592e2\"><u>4001885022</u></font>";
		} else {
			info += "<font color=\"#3592e2\"><u>"
					+ PreferenceUtil.getInstance(mContext).getHotLine()
					+ "</u></font>";
		}
		phoneNum.setText(Html.fromHtml(info));
		mLogisticLineView = (LogisticLineView) findViewById(R.id.mLogisticLineView);
		mGridView = (MyGridView) findViewById(R.id.check_log_GridView);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		phoneNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String telPhone = phoneNum.getText().toString();
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

	private void initData() {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = LogisticListBean.explainJson(content, mContext);
				if (data.type > 0 && data.orderLogistics != null
						&& data.orderLogistics.size() > 0) {
					updataView(data.orderLogistics.get(0));
					updataGridView(data.reList);
				} else {
					Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
		RequstClient.appQueryLogistics(handler, userId, order_id);
	}

	private void updataView(orderLogisticsItem orderLogisticsItem) {
		orderNum.setText(orderLogisticsItem.ORDER_CODE);
		orderStatus.setText(typeInfo(orderLogisticsItem.STATUS));
		if (orderLogisticsItem.orderLogisticsList != null
				&& orderLogisticsItem.orderLogisticsList.getTYPE() != -1) {
			mLogisticLineView.setVisibility(View.VISIBLE);
			mLogisticLineView.setData(orderLogisticsItem.orderLogisticsList);
		} else {
			mLogisticLineView.setVisibility(View.GONE);
		}
	}

	protected void updataGridView(ArrayList<ReLikeItem> reList) {
		if (reList == null || reList.size() == 0)
			return;
		mAdapter = new ReLikeAdapter(mContext, reList);
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemId = mAdapter.getId(position);
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						String.valueOf(itemId));
				startActivity(intent);
			}
		});
	}

	private String typeInfo(int type) {
		String temp = "";
		switch (type) {
		case 1:
			temp = "未付款";
			break;
		case 2:
			temp = "订单待审核";
			break;
		case 3:
			temp = "待发货";
			break;
		case 4:
			temp = "待收货";
			break;
		case 5:
			temp = "交易完成";
			break;
		case 6:
			temp = "取消订单";
			break;
		}
		return temp;
	}
}
