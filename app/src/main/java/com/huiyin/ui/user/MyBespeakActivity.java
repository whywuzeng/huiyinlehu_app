package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class MyBespeakActivity extends BaseActivity {

	public final static String TAG = "MyBespeakActivity";

	TextView left_rb, right_rb, middle_title_tv;
	private XListView mListView;
	private MyBespeakAdapter myBespeakAdapter;
	private MyBespeakBean myBespeakBean;

	boolean load_flag;

	/** 当前页 */
	private int mPageindex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybespeak_list_layout);

		initView();
		getMyBespeakList(1);
	}

	private void initView() {

		load_flag = false;

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("我的预约");

		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		left_rb.setVisibility(View.VISIBLE);
		right_rb.setVisibility(View.GONE);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
		mListView = (XListView) findViewById(R.id.xlistview);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				load_flag = false;
				mListView.setPullLoadEnable(true);
				getMyBespeakList(1);
			}

			@Override
			public void onLoadMore() {

				getMyBespeakList(2);
			}
		});
		myBespeakAdapter = new MyBespeakAdapter();
		mListView.setAdapter(myBespeakAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						myBespeakAdapter.getItem(arg2 - 1).COMMODITY_ID);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_subscribe_ID,
						myBespeakAdapter.getItem(arg2 - 1).BESPEAK_ID);
				mContext.startActivity(intent);
			}
		});
	}

	private void getMyBespeakList(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		RequstClient.bespeakRecordList(mPageindex, new CustomResponseHandler(
				this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					mListView.stopLoadMore();
					mListView.stopRefresh();
					JSONObject obj = new JSONObject(content);
					if (obj.getInt("type") != 1) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						if (mPageindex == 1) {
							myBespeakAdapter.deleteItem();
						}
						myBespeakBean = new Gson().fromJson(content,
								MyBespeakBean.class);
						if (myBespeakBean != null
								&& myBespeakBean.content != null
								&& myBespeakBean.content.size() == 10) {
							mListView.showFooter();
						} else {
							mListView.hideFooter();
							load_flag = true;
							mListView.setPullLoadEnable(false);
						}
						if (myBespeakBean != null
								&& myBespeakBean.content != null
								&& myBespeakBean.content.size() > 0)
							myBespeakAdapter.addItem(myBespeakBean.content);
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});
	}

	class MyBespeakAdapter extends BaseAdapter {

		LayoutInflater inflater;
		ArrayList<MyBespeakBean.MyBespeakItem> list;

		public MyBespeakAdapter() {
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = new ArrayList<MyBespeakActivity.MyBespeakBean.MyBespeakItem>();
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public MyBespeakBean.MyBespeakItem getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		public void deleteItem() {
			list.clear();
			notifyDataSetChanged();
		}

		public void addItem(ArrayList<MyBespeakBean.MyBespeakItem> temp) {
			if (temp == null) {
				return;
			}
			if (temp instanceof ArrayList) {
				list.addAll(temp);
				notifyDataSetChanged();
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final MyBespeakBean.MyBespeakItem item = list.get(position);
			viewHolder holder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.mybespeak_list_item,
						null);
				holder = new viewHolder();
				holder.mybespeak_image = (ImageView) convertView
						.findViewById(R.id.mybespeak_image);
				holder.mybespeak_title = (TextView) convertView
						.findViewById(R.id.mybespeak_title);
				holder.mybespeak_price = (TextView) convertView
						.findViewById(R.id.mybespeak_price);
				holder.mybespeak_time = (TextView) convertView
						.findViewById(R.id.mybespeak_time);
				holder.mybespeak_btn = (TextView) convertView
						.findViewById(R.id.mybespeak_btn);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}

			ImageManager.LoadWithServer(item.NEW_PRODUCT_PICTURE,
					holder.mybespeak_image);
			holder.mybespeak_title.setText(item.COMMODITY_NAME);
			holder.mybespeak_price.setText(MathUtil
					.priceForAppWithSign(item.BESPEAK_PRICE));
			holder.mybespeak_time.setText("预约时间：" + item.ADD_DATE);
			if (item.ADDED_STATUS == 1) {
				holder.mybespeak_btn.setText("立即购买");
			} else {
				holder.mybespeak_btn.setText("已预约");
			}

			return convertView;
		}

		public class viewHolder {
			ImageView mybespeak_image;
			TextView mybespeak_title;
			TextView mybespeak_price;
			TextView mybespeak_time;
			TextView mybespeak_btn;
		}

	}

	public class MyBespeakBean {

		public ArrayList<MyBespeakItem> content = new ArrayList<MyBespeakItem>();

		public class MyBespeakItem {

			public String NEW_PRODUCT_PICTURE;// 商品图片
			public String COMMODITY_NAME;// 商品名称
			public String BESPEAK_PRICE;// 预约价格
			public String ADD_DATE;// 预约时间
			public int ADDED_STATUS;// 上架状态
			public String COMMODITY_ID;// 商品id
			public int BESPEAK_ID;// 预约ID
		}
	}
}
