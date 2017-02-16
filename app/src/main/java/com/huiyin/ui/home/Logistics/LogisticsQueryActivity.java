package com.huiyin.ui.home.Logistics;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.LogisticsQueryAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.user.MyOrderDetailActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class LogisticsQueryActivity extends Activity {
	private Context mContext;

	private TextView left_ib, middle_title_tv;
	private ListView mListView;
	private LogisticsQueryAdapter mAdapter;
	private ViewSwitcher mViewSwitcher;
	private LogisticListBean data;

	private String userId;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_query);
		mContext = this;
		userId = AppContext.getInstance().getUserId();
		findView();
		setListener();
		initData();

	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("物流查询");
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logistics_query_viewSwitcher);
		mListView = (ListView) findViewById(R.id.logistics_query_listView);
		mAdapter = new LogisticsQueryAdapter(mContext);
		mListView.setAdapter(mAdapter);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int orderId = mAdapter.getId(position);
				Intent i = new Intent();
				i.setClass(mContext, MyOrderDetailActivity.class);
				i.putExtra("order_id", String.valueOf(orderId));
				startActivity(i);
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
					mAdapter.addItem(data.orderLogistics);
				} else {
					mViewSwitcher.setDisplayedChild(1);
					Toast.makeText(mContext, "没有订单数据", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
		RequstClient.appQueryLogistics(handler, userId, null);
	}

}
