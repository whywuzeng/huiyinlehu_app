package com.huiyin.ui.classic;

import java.util.ArrayList;
import org.apache.http.Header;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.HscViewAdapter;
import com.huiyin.adapter.SaleRankAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.Utils;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.Mode;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.huiyin.wight.pulltorefresh.PullToRefreshListView;

public class SaleRankActivity extends BaseActivity implements OnClickListener,OnRefreshListener2 {
	private TextView ab_title, ab_back;
	private HlvBean mHlvBean;
	private LvlBean mLBean;
	private static final String TAG = "SaleRankActivity";
	public HorizontialListView lv_h;
	public PullToRefreshListView lv_v;
	public String two_parent_class;
	private int pageSize = 10;
	private int pageIndex = 1;
	private String two_parent_class_name;
	private HscViewAdapter mAdapter;
	private SaleRankAdapter srAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_r);
		initView();
		initDate();
	}

	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("热销榜");
		ab_back.setOnClickListener(this);

		lv_h = (HorizontialListView) findViewById(R.id.lv_h);
		

		// 设置横条中每个条目对应的单击事件
		lv_h.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int mPosition,
					long arg3) {
				mLBean.commodity.clear();//清空数组
				mAdapter.setCurrent(mPosition);
				mAdapter.notifyDataSetChanged();
				two_parent_class = mHlvBean.erjifenlei.get(mPosition).TWO_PARENT_CLASS;
				if (two_parent_class != null && !two_parent_class.equals("")){
					// 请求ListView中的数据（第一页）
					getLlvHTTP(two_parent_class, 1);
				} else {
					return;
				}
			}
		});

		lv_v = (PullToRefreshListView) findViewById(R.id.lv_v);
		lv_v.setEnabled(false);
		// 设定上下拉刷新
//		lv_v.setMode(Mode.BOTH);
		lv_v.setMode(Mode.PULL_FROM_START);
		lv_v.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		lv_v.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		lv_v.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		lv_v.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");
		
		// 下拉刷新数据
		lv_v.setOnRefreshListener(this);
		lv_v.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(mContext, GoodsDetailActivity.class);
				// 传商品id
				i.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						mLBean.commodity.get(position - 1).ID);
				mContext.startActivity(i);
			}
		});
	}

	private void initDate() {
		// TODO Auto-generated method stub
		mHlvBean = new HlvBean();
		mLBean = new LvlBean();
		HLVHttp();
	}

	//请求横条的数据
	public void HLVHttp() {
		RequstClient.getHLVHttp(new CustomResponseHandler(this) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 成功
						String errorMsg = obj.getString("msg");
						Toast.makeText(SaleRankActivity.this, errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						mHlvBean = new Gson().fromJson(content, HlvBean.class);
						if (mHlvBean != null && mHlvBean.erjifenlei != null) {
							// 如果有数据就显示横条
							if (mHlvBean.erjifenlei.size() > 0) {
								lv_h.setVisibility(View.VISIBLE);
							}
							mAdapter = new HscViewAdapter(
									SaleRankActivity.this, mHlvBean);
							lv_h.setAdapter(mAdapter);

							// 初始化横条的第一个条目的数据（第一页的数据）
							SystemClock.sleep(1000);// 缓冲一秒钟
							two_parent_class = mHlvBean.erjifenlei.get(0).TWO_PARENT_CLASS;
							getLlvHTTP(two_parent_class, 1);
						} else {
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ab_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	//请求数据ListView的数据
	public void getLlvHTTP(String id, int page) {
		
		RequstClient.getVlvHttp(id, page + "", new CustomResponseHandler(this) {
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				lv_v.onRefreshComplete();
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				// TODO Auto-generated method stub
				super.onFailure(error, errorMessage);
				Toast.makeText(mContext, "加载失败", Toast.LENGTH_LONG).show();
				lv_v.onRefreshComplete();
				return;
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 成功
						String errorMsg = obj.getString("msg");
						Toast.makeText(SaleRankActivity.this, errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						if (pageIndex == 1) {
							mLBean = new Gson()
									.fromJson(content, LvlBean.class);
							if (mLBean != null && mLBean.commodity != null) {
								srAdapter = new SaleRankAdapter(mContext,
										mLBean.commodity);
								lv_v.setAdapter(srAdapter);
							} else {
								return;
							}
						} else {
							LvlBean lvlBean = new Gson().fromJson(content,
									LvlBean.class);
							if (lvlBean.commodity.size() > 0) {
								mLBean.commodity.addAll(lvlBean.commodity);
								srAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(mContext, "已无更多数据！",
										Toast.LENGTH_SHORT).show();
								return;
							}
						}
						lv_v.onRefreshComplete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		pageIndex = 1;
		getLlvHTTP(two_parent_class, pageIndex);
	}

	// 加载更多
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// 加载更多
		if (mLBean.commodity == null || mLBean.commodity.size() <= 0) {
			lv_v.onRefreshComplete();
			Toast.makeText(mContext, "加载失败", 0).show();
			return;
		} else {
			pageIndex = srAdapter.getCount() / pageSize + 1;

			if (srAdapter.getCount() % pageSize > 0) {
				pageIndex++;
			}
			getLlvHTTP(two_parent_class, pageIndex);
		}
	}
	
	// listview的Bean类
	public class LvlBean {
		public ArrayList<lBeanItem> commodity = new ArrayList<lBeanItem>();
		public class lBeanItem {
			public String PRICE;// 价格
			public String COMMODITY_IMAGE_PATH;// 图片路径
			public String SALES_VOLUME;// 销量
			public String ID;
			public String NUM;
			public String COMMODITY_NAME;// 商品名称
			public String COMMODITY_AD;
		}
	}

	// 横条的bean
	public class HlvBean {
		public ArrayList<HBean> erjifenlei = new ArrayList<HBean>();
		public class HBean {
			public String CATEGORY_NAME;// 名称
			public String TWO_PARENT_CLASS;// 二级分类
		}
	}
}
