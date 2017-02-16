package com.huiyin.ui.housekeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.adapter.HouseKeeperListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.HouseKeeper;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;
import com.huiyin.wight.rongcloud.RongCloud;

public class HouseKeeperListActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "HouseKeeperListActivity";

	private XListView mListView;

	private HouseKeeperListAdapter mAdapter;
	private List<HouseKeeper> listDatas;

	private int page = 1;
	private TextView ab_back, ab_title, ab_right_btn1;
	private ImageView ab_right_btn2;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_housekeeper_list);

		initView();
		getHouseKeeper(true);
	}

	/***
	 * 初始化
	 */
	private void initView() {
		listDatas = new ArrayList<HouseKeeper>();

		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);

		ab_right_btn1 = (TextView) findViewById(R.id.ab_right_btn1);
		ab_right_btn1.setText("咨询");
		ab_right_btn1.setOnClickListener(this);

		ab_right_btn2 = (ImageView) findViewById(R.id.ab_right_btn2);
		ab_right_btn2.setOnClickListener(this);
		ab_right_btn2.setImageResource(R.drawable.ab_ic_customer);

		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("智慧管家");

		mListView = (XListView) findViewById(R.id.mListView);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		// 隐藏滚动条
		mListView.setVerticalScrollBarEnabled(true);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getHouseKeeper(false);
			}

			@Override
			public void onLoadMore() {
				page = mAdapter.getCount() / pageSize + 1;
				if (mAdapter.getCount() % pageSize > 0)
					page++;
				getHouseKeeper(false);
			}
		});
	}

	/***
	 * 网络读取数据
	 */
	private void getHouseKeeper(final boolean initB) {

		RequstClient.houseKeeper(page + "", new CustomResponseHandler(this) {
			@Override
			public void onStart() {
				if (initB)
					super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mListView.stopLoadMore();
				mListView.stopRefresh();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(HouseKeeperListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
						return;
					} else {
						JSONArray arrays = obj.getJSONArray("wisdomList");
						List<HouseKeeper> listTemps = new ArrayList<HouseKeeper>();
						for (int i = 0; i < arrays.length(); i++) {
							JSONObject object = arrays.getJSONObject(i);
							HouseKeeper bean = new HouseKeeper();
							bean.setName(object.getString("TITLE"));
							bean.setAbstracting(object.getString("ABSTRACTING"));
							bean.setContent(object.getString("INTRODUCTION"));
							bean.setRowId(object.getInt("ID"));
							bean.setImageUrl(object.getString("IMG"));
							listTemps.add(bean);
						}

						if (page == 1) {
							listDatas.clear();
							listDatas.addAll(listTemps);
							mAdapter = new HouseKeeperListAdapter(getApplicationContext(), listDatas);
							mListView.setAdapter(mAdapter);
							if (mAdapter.getCount() < pageSize) {
								mListView.hideFooter();
							} 
						} else {
							if (listTemps.size() > 0) {
								listDatas.addAll(listTemps);
							} else {
								Toast.makeText(HouseKeeperListActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
								mListView.setPullLoadEnable(false);
								mListView.hideFooter();

							}
							mAdapter.notifyDataSetChanged();
						}
					}
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ab_right_btn1:
			// 点击后进入电话咨询页面
			String telPhone = PreferenceUtil.getInstance(mContext).getHotLine();
			String regEx = "[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(telPhone);
			telPhone = m.replaceAll("").trim();
			if (StringUtils.isBlank(telPhone))
				return;
			Intent d_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
			d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(d_intent);
			break;
		case R.id.ab_right_btn2:
			RongCloud.getInstance(HouseKeeperListActivity.this).startCustomerServiceChat();
			break;
		case R.id.ab_back:
			finish();
			break;
		default:
			break;
		}
	}

}
