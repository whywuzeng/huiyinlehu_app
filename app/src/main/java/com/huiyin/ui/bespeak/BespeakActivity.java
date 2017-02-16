package com.huiyin.ui.bespeak;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.classic.HorizontialListView;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.wight.MyListView;
import com.huiyin.wight.StickyScrollView;
import com.huiyin.wight.StickyScrollView.OnBorderListener;
import com.huiyin.wight.viewflow.CircleFlowIndicator;
import com.huiyin.wight.viewflow.ViewFlow;

public class BespeakActivity extends BaseActivity {

	private final static String TAG = "BespeakActivity";

	private TextView ab_back;
	private Button ab_right;
	// 广告轮换图
	private ViewFlow viewFlow;
	private CircleFlowIndicator indic;
	private BespeakViewflowAdapter bespeakViewflowAdapter;

	public HorizontialListView lv_h;
	private BespeakHorizontialListViewAdapter BespeakHoriListAdapter;

	private StickyScrollView scrollView;
	private MyListView bespeak_listview;
	private BespeakListAdapter bespeakListAdapter;
	private TextView showfoot;

	private BespeakTitleBean bespeakTitleBean;
	private BespeakListBean bespeakListBean;
	private String date;
	private Boolean status;
	private int type = -1;
	/** 当前页 */
	private int mPageindex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bespeak_layout);
		findView();
		setListener();
		InitData();
	}

	private void findView() {

		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_right = (Button) findViewById(R.id.ab_right);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);// 获得viewFlow对象
		indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic); // viewFlow下的indic
		indic.setVisibility(View.GONE);
		lv_h = (HorizontialListView) findViewById(R.id.lv_h);
		scrollView = (StickyScrollView) findViewById(R.id.scrollview);
		bespeak_listview = (MyListView) findViewById(R.id.bespeak_listview);
		bespeakListAdapter = new BespeakListAdapter(mContext);
		bespeak_listview.setAdapter(bespeakListAdapter);
		showfoot = (TextView) findViewById(R.id.showfoot);
	}

	private void setListener() {

		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ab_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, IntroduceActivity.class);
				intent.putExtra("id", -45);
				startActivity(intent);
			}
		});
		bespeak_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						bespeakListAdapter.getItem(arg2).COMMODITY_ID);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_subscribe_ID,
						bespeakListAdapter.getItem(arg2).ID);
				mContext.startActivity(intent);
			}
		});
		scrollView.setOnBorderListener(new OnBorderListener() {

			@Override
			public void onother() {

			}

			@Override
			public void onTop() {

			}

			@Override
			public void onBottom() {
				if (status) {
					getTheList(2);
					status = false;
				}
			}
		});
	}

	private void InitData() {
		RequstClient.bespeakType(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getInt("type") != 1) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						bespeakTitleBean = new Gson().fromJson(content,
								BespeakTitleBean.class);
						RefreshTitle();
						getTheList(1);
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});

	}

	private void RefreshTitle() {

		if (bespeakTitleBean != null) {
			if (bespeakTitleBean.appCode != null) {

				bespeakViewflowAdapter = new BespeakViewflowAdapter(mContext,
						bespeakTitleBean.appCode);
				if (bespeakTitleBean.appCode.size() < 1) {
					indic.setVisibility(View.GONE);
				} else {
					indic.setVisibility(View.VISIBLE);
					viewFlow.setAdapter(bespeakViewflowAdapter);
					viewFlow.setmSideBuffer(bespeakTitleBean.appCode.size());
					viewFlow.setFlowIndicator(indic);
					viewFlow.setTimeSpan(5000);
					viewFlow.setSelection(bespeakTitleBean.appCode.size() * 10);
					if (bespeakTitleBean.appCode.size() <= 1) {
						indic.setVisibility(View.GONE);
					} else {
						viewFlow.startAutoFlowTimer();
					}
				}
			}

			if (bespeakTitleBean.content != null) {
				BespeakHoriListAdapter = new BespeakHorizontialListViewAdapter(
						BespeakActivity.this, bespeakTitleBean.content);
				lv_h.setAdapter(BespeakHoriListAdapter);
				lv_h.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						BespeakHoriListAdapter.setCurrent(arg2);
						BespeakHoriListAdapter.notifyDataSetChanged();
						type = BespeakHoriListAdapter.getItem(arg2).ID;
						getTheList(1);
					}
				});
				lv_h.setVisibility(View.VISIBLE);
			}
		}
	}

	private void getTheList(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		RequstClient.bespeak(type, mPageindex, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getInt("type") != 1) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						bespeakListBean = new Gson().fromJson(content,
								BespeakListBean.class);
						if (obj.has("curDate"))
							date = obj.getString("curDate");
						if (mPageindex == 1) {
							bespeakListAdapter.deleteItem();
							status = true;
							showfoot.setVisibility(View.GONE);
						}
						if (bespeakListBean != null
								&& bespeakListBean.content != null
								&& bespeakListBean.content.size() > 0) {

							bespeakListAdapter.addItem(bespeakListBean.content,
									date);
							if (bespeakListBean.content.size() < 10) {
								status = false;
								showfoot.setVisibility(View.VISIBLE);
							} else {
								status = true;
								showfoot.setVisibility(View.GONE);
							}
						} else {
							showfoot.setVisibility(View.VISIBLE);
						}
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		showfoot.setVisibility(View.GONE);
		getTheList(1);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		bespeakListAdapter.destroyTimer();
	}
}
