package com.huiyin.ui.home.prefecture;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ZhuanquBean;
import com.huiyin.bean.ZhuanquGallery;
import com.huiyin.bean.ZhuanquGoodbean;
import com.huiyin.ui.classic.CategorySearchActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.home.prefecture.adapter.ZhuanQuAdapter;
import com.huiyin.utils.Utils;
import com.huiyin.utils.YtShare;
import com.huiyin.wight.MyGridView;
import com.huiyin.wight.MyListView;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.Mode;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.huiyin.wight.pulltorefresh.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZhuanQuActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnRefreshListener2 {

	// 请求的ID
	public static final String INTENT_KEY_ID = "type_id";
	// 标题
	public static final String INTENT_KEY_NAME = "title";
	public static final String INTENT_KEY_BARNER_ID = "barnerId";// 待讨论是否使用？？
	// 跳商品详情还是商品列表的标记
	public static final String INTENT_KEY_FLAG = "flag";
	// 跳转类型(判断从分类聚合还是从促销活动跳转的)
	public static final String INTENT_KEY_TYPE = "type";

	protected static final String TAG = "ZhuanQuActivity";
	private MyListView zhuan_qu_body_xv;
	private ZhuanquBean bean;
	private ZhuanQuAdapter adapter;
	private PullToRefreshScrollView scrollview;
	private int pageIndex;
	private int pageSize = 10;
	private String id;
	private String title;
	private String barnerId;
	private String flag;
	private int type = 0;

	// 广告轮换图
	private MyGridView gv;
	private ZhuanquGallery gallery;
	private View zqView;

	private TextView ab_title, ab_back;
	private ImageView ab_right_btn;

	private ImageView bannerImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuan_qu_body);

		initView();

		initData();
	}

	private void initData() {
		bean = new ZhuanquBean();// 这样做避免报空指针
		type = getIntent().getIntExtra(INTENT_KEY_TYPE, 0);

		pageIndex = 1;
		if (type == 2) {
			// 促销跳转过
			id = getIntent().getStringExtra(INTENT_KEY_ID);
			flag = getIntent().getStringExtra(INTENT_KEY_FLAG);
			requestData(id, type, pageIndex + "", false);
		} else if (type == 1) {
			// 分类聚合过来的
			title = getIntent().getStringExtra(INTENT_KEY_NAME);
			ab_title.setText(title);
			barnerId = getIntent().getStringExtra(INTENT_KEY_BARNER_ID);
			id = getIntent().getStringExtra(INTENT_KEY_ID);
			flag = getIntent().getStringExtra(INTENT_KEY_FLAG);
			requestData(id, type, pageIndex + "", false);
		} else if (type == 3) {
			// 首页banner跳转过来的
			id = getIntent().getStringExtra(INTENT_KEY_ID);
			requestData(id, type, pageIndex + "", false);
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		zqView = findViewById(R.id.zhun_qu_view);

		bannerImage = (ImageView) findViewById(R.id.zhuanqu_barner_img);
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);
		ab_title = (TextView) findViewById(R.id.ab_title);

		ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
		ab_right_btn.setImageResource(R.drawable.ab_ic_share);
		// 隐藏按钮
		ab_right_btn.setVisibility(View.GONE);

		scrollview = (PullToRefreshScrollView) findViewById(R.id.zhuan_qu_body_scrollview);

		zhuan_qu_body_xv = (MyListView) findViewById(R.id.zhuan_qu_body_xv);
		zhuan_qu_body_xv.setOnItemClickListener(this);

		gv = (MyGridView) findViewById(R.id.zhuan_qu_body_gv);

		// 设定上下拉刷新
		scrollview.setMode(Mode.BOTH);
		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrTiem());
		scrollview.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		scrollview.setOnRefreshListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(getApplicationContext(),
				GoodsDetailActivity.class);
		intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, bean
				.getListGoodbeans().get(position).getId()
				+ "");
		startActivity(intent);
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		pageIndex = 1;
		requestData(id, type, pageIndex + "", false);
	}

	// 上拉刷新
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// 加载更多
		if (bean.getListGoodbeans() == null
				|| bean.getListGoodbeans().size() <= 0) {
			SystemClock.sleep(3000);
			scrollview.onRefreshComplete();
			Toast.makeText(mContext, "加载失败", 0).show();
			return;
		} else {
			pageIndex = adapter.getCount() / pageSize + 1;

			if (adapter.getCount() % pageSize > 0) {
				pageIndex++;
			}
			requestData(id, type, pageIndex + "", false);
		}
	}

	// 获取网络数据的方法
	private void requestData(String id, final int type, final String page,
			final boolean bool) {

		RequstClient.getZhuanQu(URLs.GET_ZHUANQU_DATE, id, page, type + "",
				new CustomResponseHandler(this, false) {

					@Override
					public void onStart() {
						if (bool)
							super.onStart();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						scrollview.onRefreshComplete();
					}

					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(mContext, "加载失败", Toast.LENGTH_LONG)
								.show();
						scrollview.onRefreshComplete();
						return;
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						if (pageIndex == 1) {
							bean = parseJson(content, new ZhuanquBean());
							if (bean != null) {
								// 如果是促销活动就设置标题
								if (type == 2 || type == 3) {
									if (bean.getTitle() == null
											|| bean.getTitle().equals("")) {
										ab_title.setText("");
									} else {
										ab_title.setText(bean.getTitle());
									}
								}
								if (bean.getListGalleries() != null) {
									gallery = bean.getListGalleries().get(0);
									// 初始化广告条
									initBarner(bannerImage, gallery, zqView);
								} else {
									return;
								}
								String layout = bean.getLayout();
								adapter = new ZhuanQuAdapter(mContext, layout,
										bean.getListGoodbeans());
								if (layout.equals("1")) {
									zhuan_qu_body_xv
											.setVisibility(View.VISIBLE);
									gv.setVisibility(View.GONE);
									zhuan_qu_body_xv.setAdapter(adapter);
								}
								if (layout.equals("2")) {
									zhuan_qu_body_xv.setVisibility(View.GONE);
									gv.setVisibility(View.VISIBLE);
									gv.setAdapter(adapter);
								}
								if (layout.equals("3")) {
								}
							} else {
								return;
							}
						} else {
							ZhuanquBean tBean = parseJson(content,
									new ZhuanquBean());
							if (tBean.getListGoodbeans() != null) {
								if (tBean.getListGoodbeans().size() > 0) {
									bean.getListGoodbeans().addAll(
											tBean.getListGoodbeans());
									adapter.notifyDataSetChanged();
								} else {
									Toast.makeText(mContext, "已无更多数据！",
											Toast.LENGTH_SHORT).show();
									return;
								}
							} else {
								Toast.makeText(mContext, "已无更多数据！",
										Toast.LENGTH_SHORT).show();
								return;
							}
							scrollview.onRefreshComplete();
						}
					}
				});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ab_back:
			this.finish();
			break;
		case R.id.ab_right_btn:
			// Toast.makeText(this, "分享...", Toast.LENGTH_SHORT).show();
			// YtShare.createInstance(this).share();
			break;
		case R.id.zhuan_qu_top_back_iv:
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 解析json数据的方法
	 * 
	 * @param content
	 */
	public ZhuanquBean parseJson(String content, ZhuanquBean zBean) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONObject json = roots.getJSONObject("list");
				JSONArray goods = json.getJSONArray("pageBean");
				List<ZhuanquGoodbean> listGoodbeans = new ArrayList<ZhuanquGoodbean>();
				for (int i = 0; i < goods.length(); i++) {
					JSONObject obj = goods.getJSONObject(i);
					ZhuanquGoodbean bean = new ZhuanquGoodbean();
					bean.setId(obj.getInt("ID"));
					bean.setImagePath(obj.getString("COMMODITY_IMAGE_PATH"));
					bean.setTitle(obj.getString("COMMODITY_NAME"));
					bean.setPrice(obj.getString("PRICE"));
					bean.setReprice(obj.getString("REFERENCE_PRICE"));
					bean.setSaleSum(obj.getString("SALES_VOLUME"));
					listGoodbeans.add(bean);
				}
				List<ZhuanquGallery> listGalleries = new ArrayList<ZhuanquGallery>();
				JSONArray gallerys = json.getJSONArray("prefecture");
				for (int i = 0; i < gallerys.length(); i++) {
					JSONObject obj = gallerys.getJSONObject(i);
					ZhuanquGallery bean = new ZhuanquGallery();
					bean.setImageUrl(obj.getString("BIG_IMG"));// 图片路径
					bean.setId(obj.getString("BIG_URL"));// 请求id
					bean.setBigFlag(obj.getString("BIG_FLAG"));// 跳转界面的标记
					listGalleries.add(bean);
				}
				zBean.setListGalleries(listGalleries);
				zBean.setListGoodbeans(listGoodbeans);
				String title = json.getString("TITLE");
				String layout = json.getString("LAYOUT");
				zBean.setTitle(title);
				zBean.setLayout(layout);
			} else {
				String errorMsg = roots.getString("msg");
				Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return zBean;
	}

	// 初始化广告条
	private void initBarner(ImageView img, final ZhuanquGallery gallery,
			View view) {
		if (gallery != null) {
			if (gallery.getImageUrl() == null
					|| gallery.getImageUrl().equals("")) {
				return;
			} else {
				ImageLoader.getInstance().displayImage(
						URLs.IMAGE_URL + gallery.getImageUrl(), img);
				view.setVisibility(View.VISIBLE);
				img.setVisibility(View.VISIBLE);
			}
		} else {
			return;
		}
		// 跳转到相应的界面
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (gallery == null || gallery.getBigFlag() == null
						|| gallery.getBigFlag().equals("")) {
					return;
				} else {
					if (gallery.getBigFlag().equals("1")) {
						Intent intent = new Intent(mContext,
								GoodsDetailActivity.class);
						intent.putExtra(
								GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
								gallery.getId() + "");
						mContext.startActivity(intent);
					}
					if (gallery.getBigFlag().equals("2")) {
						Intent intent = new Intent(mContext,
								CategorySearchActivity.class);
						intent.putExtra(
								CategorySearchActivity.BUNDLE_KEY_CATEGORY_ID,
								gallery.getId() + "");
						mContext.startActivity(intent);
					}
					if (gallery.getBigFlag().equals("3")) {

					}
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		YtShare.createInstance(this).destroy();
	}
}
