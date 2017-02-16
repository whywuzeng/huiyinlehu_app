package com.huiyin.ui.flash;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class FlashActivity extends BaseActivity implements IXListViewListener {

	// Content View Elements

	private TextView ab_back;
	private TextView ab_title;
	private TextView textView_top;
	private XListView mXlistview;

	// End Of Content View Elements
	/** 当前页 */
	private int mPageindex;
	private int type = 1;
	private FlashListAdapter mAdapter;
	private FlashListBean data;
	private String maintitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_layout);
		Intent i = getIntent();
		maintitle = i.getStringExtra("maintitle");
		findView();
		setListener();
		InitData();
	}

	private void findView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText(maintitle);
		textView_top = (TextView) findViewById(R.id.textView_top);
		mXlistview = (XListView) findViewById(R.id.xlistview);
		mXlistview.setFooterDividersEnabled(false);
		mXlistview.setPullLoadEnable(false);
		mXlistview.setPullRefreshEnable(true);
		mXlistview.setXListViewListener(this);
		mAdapter = new FlashListAdapter(mContext);
		mXlistview.setAdapter(mAdapter);
	}

	private void setListener() {
		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mXlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int comId = mAdapter.getID(position);
				if (comId != -1) {
					Intent intent = new Intent(mContext, FlashPrefectureActivity.class);
					intent.putExtra("id", comId);
					startActivity(intent);
				} else {
					type = 2;
					loadPageData(1);
				}
			}
		});
	}

	private void InitData() {
		loadPageData(1);
	}

	private void loadPageData(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = FlashListBean.explainJson(content, mContext);
				if (data.type > 0 && data.getRegionList() != null && data.getRegionList().size() > 0) {
					textView_top.setText(data.getNowTitle());
					if (mPageindex == 1) {
						mAdapter.deleteItem();
					}
					if (data.getRegionList().size() >= 10) {
						mAdapter.addItem(data.getRegionList(), null, data.getCurDate());
						mXlistview.setPullLoadEnable(true);
					} else {
						mAdapter.addItem(data.getRegionList(), data.getNextTitle(), data.getCurDate());
						mXlistview.setPullLoadEnable(false);
					}
				} else {
					mXlistview.setPullLoadEnable(false);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				endUpData();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				endUpData();
			}

		};
		RequstClient.flashSaleActive(mPageindex, type, handler);
	}

	private void endUpData() {
		mXlistview.stopLoadMore(1);
		mXlistview.stopRefresh();
		mXlistview.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
	}

	@Override
	public void onRefresh() {
		loadPageData(1);
	}

	@Override
	public void onLoadMore() {
		loadPageData(2);
	}
}
