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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CollectActivity extends BaseActivity {

	public final static String TAG = "CollectActivity";
	private XListView mListView;
	private CollectBean mCollectBean;
	CollectAdapter mCollectAdapter;
	TextView left_rb, right_rb, middle_title_tv;
	private int page = 1;
	private int pageSize = 10;
	private int request_code = 123;
	// 商品id
	String commodityId;
	// 位置
	int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect_list_layout);

		initView();
		getCollectList(true);

	}

	private void initView() {

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("我的收藏");

		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		left_rb.setVisibility(View.VISIBLE);
		right_rb.setVisibility(View.VISIBLE);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mCollectBean = new CollectBean();
		mListView = (XListView) findViewById(R.id.xlistview);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getCollectList(false);
			}

			@Override
			public void onLoadMore() {
				page = mCollectAdapter.getCount() / pageSize + 1;
				if (mCollectAdapter.getCount() % pageSize > 0)
					page++;
				getCollectList(false);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					return;
				}
				pos = arg2;
				commodityId = mCollectBean.collectList.get(arg2 - 1).COMMODITY_ID;
				Intent i = new Intent(CollectActivity.this, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_FOCUS_TYPE);
				startActivityForResult(i, request_code);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (request_code == requestCode && resultCode == RESULT_OK) {
			// 查看商品详情
			Intent i = new Intent(CollectActivity.this, GoodsDetailActivity.class);
			i.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, commodityId);
			i.putExtra("position", pos);
			startActivityForResult(i, 69);
		} else if (request_code == requestCode && resultCode == RESULT_FIRST_USER) {
			// 取消关注
			cancleFoucs();
		} else if (resultCode == RESULT_OK && requestCode == 69) {
			int position = data.getIntExtra("positon", -1);
			int isCollect = data.getIntExtra("isCollect", 0);
			if (isCollect != 1 && position != -1) {
				mCollectBean.collectList.remove(pos - 1);
				mCollectAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/***
	 * 取消关注
	 */
	private void cancleFoucs() {
		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.cancelFoucs(AppContext.getInstance().getUserId(), commodityId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "cancelFoucs:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					mCollectBean.collectList.remove(pos - 1);
					mCollectAdapter.notifyDataSetChanged();

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

	/***
	 * 获取收藏列表
	 * 
	 * @param initB
	 */
	private void getCollectList(final boolean initB) {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.getCollectList(AppContext.getInstance().getUserId(), page + "", new CustomResponseHandler(this) {
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
				LogUtil.i(TAG, "getCollectList:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					if (page == 1) {
						mCollectBean = new Gson().fromJson(content, CollectBean.class);
						mCollectAdapter = new CollectAdapter(mCollectBean);
						mListView.setAdapter(mCollectAdapter);

						if (mCollectAdapter.getCount() < pageSize) {
							mListView.hideFooter();
						} else {
							mListView.showFooter();
						}

					} else {

						CollectBean collectBean = new Gson().fromJson(content, CollectBean.class);
						if (collectBean.collectList != null) {
							if (collectBean.collectList.size() > 0) {
								mCollectBean.collectList.addAll(collectBean.collectList);
								mCollectAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(CollectActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
								mListView.hideFooter();
							}

						}
						mListView.stopLoadMore();
						mListView.stopRefresh();

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

	class CollectAdapter extends BaseAdapter {

		LayoutInflater inflater;
		CollectBean mCollectBean;

		public CollectAdapter(CollectBean mCollectBean) {
			this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mCollectBean = mCollectBean;
		}

		@Override
		public int getCount() {

			return mCollectBean.collectList == null ? 0 : mCollectBean.collectList.size();
		}

		@Override
		public Object getItem(int position) {
			return mCollectBean.collectList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mCollectBean.collectList.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CollectBean.CollectItem item = mCollectBean.collectList.get(position);
			convertView = inflater.inflate(R.layout.collect_list_item, null);

			TextView collect_good_title = (TextView) convertView.findViewById(R.id.collect_good_title);
			TextView collect_people_num = (TextView) convertView.findViewById(R.id.collect_people_num);
			TextView collect_good_price = (TextView) convertView.findViewById(R.id.collect_good_price);
			ImageView collect_listitem_image = (ImageView) convertView.findViewById(R.id.collect_listitem_image);

			ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + item.COMMODITY_IMAGE_PATH, collect_listitem_image);

			collect_good_title.setText(item.COMMODITY_NAME);
			collect_people_num.setText(item.COLLECT_COUNT + "人收藏");
			if (item.MARK != -1) {
				collect_good_price.setText(MathUtil.priceForAppWithSign(item.PROMOTIONS_PRICE));
			} else {
				collect_good_price.setText(MathUtil.priceForAppWithSign(item.PRICE));
			}
			return convertView;
		}

	}

	public class CollectBean {

		public ArrayList<CollectItem> collectList = new ArrayList<CollectItem>();

		public class CollectItem {

			public String COLLECT_COUNT;// 收藏人数
			public String PRICE;// 商品价格
			public String COMMODITY_ID;// 商品id
			public String COMMODITY_IMAGE_PATH;// 收藏图标
			public String COMMODITY_NAME;// 商品名称
			public int MARK;// 是否参加促销活动
			public String PROMOTIONS_PRICE;
		}
	}

}
