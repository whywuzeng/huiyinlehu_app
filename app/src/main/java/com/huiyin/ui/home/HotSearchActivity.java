package com.huiyin.ui.home;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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

public class HotSearchActivity extends BaseActivity {

	public final static String TAG = "HotSearchActivity";

	TextView left_rb, right_rb, middle_title_tv;
	private XListView mListView;
	private HotSearchAdapter hotSearchAdapter;
	private HotSearchBean hotSearchBean;

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
		middle_title_tv.setText("热搜商品");

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
		hotSearchAdapter = new HotSearchAdapter();
		mListView.setAdapter(hotSearchAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						hotSearchAdapter.getItem(arg2 - 1).ID);
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
		RequstClient.hotSearch(mPageindex, new CustomResponseHandler(this) {
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
							hotSearchAdapter.deleteItem();
						}
						hotSearchBean = new Gson().fromJson(content,
								HotSearchBean.class);
						if (hotSearchBean != null
								&& hotSearchBean.commodityList != null
								&& hotSearchBean.commodityList.size() == 10) {
							mListView.showFooter();
						} else {
							mListView.hideFooter();
							load_flag = true;
							mListView.setPullLoadEnable(false);
						}
						if (hotSearchBean != null
								&& hotSearchBean.commodityList != null
								&& hotSearchBean.commodityList.size() > 0)
							hotSearchAdapter
									.addItem(hotSearchBean.commodityList);
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});
	}

	class HotSearchAdapter extends BaseAdapter {

		LayoutInflater inflater;
		ArrayList<HotSearchBean.HotSearchItem> list;

		public HotSearchAdapter() {
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = new ArrayList<HotSearchActivity.HotSearchBean.HotSearchItem>();
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public HotSearchBean.HotSearchItem getItem(int position) {

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

		public void addItem(ArrayList<HotSearchBean.HotSearchItem> temp) {
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
			final HotSearchBean.HotSearchItem item = list.get(position);
			viewHolder holder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.hotsearch_list_item,
						null);
				holder = new viewHolder();
				holder.hotsearch_img = (ImageView) convertView
						.findViewById(R.id.hotsearch_img);
				holder.hotsearch_title = (TextView) convertView
						.findViewById(R.id.hotsearch_title);
				holder.hotsearch_price = (TextView) convertView
						.findViewById(R.id.hotsearch_price);
				holder.hotsearch_reference_price = (TextView) convertView
						.findViewById(R.id.hotsearch_reference_price);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
			ImageManager.LoadWithServer(item.COMMODITY_IMAGE_PATH,
					holder.hotsearch_img);
			holder.hotsearch_title.setText(item.COMMODITY_NAME);
			holder.hotsearch_price.setText(MathUtil
					.priceForAppWithSign(item.PRICE));
			holder.hotsearch_reference_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			holder.hotsearch_reference_price.setText(MathUtil
					.priceForAppWithSign(item.REFERENCE_PRICE));
			return convertView;
		}

		public class viewHolder {
			ImageView hotsearch_img;
			TextView hotsearch_title;
			TextView hotsearch_price;
			TextView hotsearch_reference_price;
		}
	}

	public class HotSearchBean {

		public ArrayList<HotSearchItem> commodityList = new ArrayList<HotSearchItem>();

		public class HotSearchItem {

			public String COMMODITY_IMAGE_PATH;// 商品图片
			public String COMMODITY_NAME;// 商品名称
			public String PRICE;// 商品价格
			public String REFERENCE_PRICE;// 参考价格
			public String ID;// 商品id
		}
	}
}
