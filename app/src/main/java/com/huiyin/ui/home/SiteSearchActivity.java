package com.huiyin.ui.home;

import java.util.ArrayList;
import org.apache.http.Header;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.custom.vg.list.CustomListView;
import com.custom.vg.list.OnItemClickListener;
import com.custom.vg.list.OnItemLongClickListener;
import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.SearchHistoryAdapter;
import com.huiyin.adapter.SearchListAdapter;
import com.huiyin.adapter.SearchListAdapter.MyOnItemClickListener;
import com.huiyin.adapter.SiteSearchAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.MayLikeCommodityBean;
import com.huiyin.bean.SearchHistroyBean;
import com.huiyin.bean.SearchHistroyListResult;
import com.huiyin.db.SearchRecordDao;
import com.huiyin.ui.classic.CategorySearchActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.twoway.TwoWayAbsListView;
import com.huiyin.wight.twoway.TwoWayAdapterView;
import com.huiyin.wight.twoway.TwoWayGridView;

@SuppressLint("ResourceAsColor")
public class SiteSearchActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "SiteSearchActivity";

	private ImageView site_search_sousuo_iv;
	private EditText site_search_content;
	private ImageView site_search_back;
	private TextView site_search_sousuo_tv;
	private String key_word;
	public MayLikeCommodityBean mayLike;
	//可以水平滚动和垂直滚动的GridView
	private TwoWayGridView gridview;// 大家都喜欢

	private LinearLayout layout_maylike;
	private CustomListView clv_search_stroy;// 搜索历史
	private LinearLayout history_layout;//装在一个水平的linearlayout
	private TextView history_tv_top;

	private PopupWindow popWindow;
	private ListView popList;
	private SearchHistroyListResult resultList;// 三级列表数据
	private LinearLayout layout_kuang;
	private SearchRecordDao searchDao;// 数据操作对象
	private ArrayList<SearchHistroyBean> earchHistroyList;

	private TextView tv_delAll;// 清除历史

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_search);
		searchDao = new SearchRecordDao();
		key_word = getIntent().getStringExtra("content");
		findView();
		initSearch();
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		site_search_back = (ImageView) findViewById(R.id.site_search_back);
		site_search_sousuo_iv = (ImageView) findViewById(R.id.site_search_sousuo_iv);
		clv_search_stroy = (CustomListView) findViewById(R.id.clv_search_stroy);
		history_layout = (LinearLayout) findViewById(R.id.history_layout);
		history_tv_top = (TextView) findViewById(R.id.history_tv_top);
		site_search_sousuo_tv = (TextView) findViewById(R.id.site_search_sousuo_tv);
		tv_delAll = (TextView) findViewById(R.id.tv_delAll);
		layout_maylike = (LinearLayout) findViewById(R.id.layout_maylike);
		site_search_back.setOnClickListener(this);
		site_search_sousuo_tv.setOnClickListener(this);
		site_search_sousuo_iv.setOnClickListener(this);
		site_search_content = (EditText) findViewById(R.id.site_search_content);
		site_search_content.requestFocus();
		layout_kuang = (LinearLayout) findViewById(R.id.layout_kuang);

		gridview = (TwoWayGridView) findViewById(R.id.gridview);
	}

	private void initView() {
		site_search_content.addTextChangedListener(new TextWatcher() {// 给编辑框加监听事件
					@Override
					public void onTextChanged(CharSequence text, int arg1,
							int arg2, int arg3) {

						if (!text.toString().isEmpty()) {// 不为空
							boolean flag = (text.toString()).equals(key_word);
							if (!flag) {// 与选择的关键字不同则请求
								showHistroyList(text.toString());// 请求三级列表商品数据
							}
						} else {
							if (popWindow != null && popWindow.isShowing()) {
								popWindow.dismiss();
							}
						}
					}

					@Override
					public void beforeTextChanged(CharSequence text, int arg1,
							int arg2, int arg3) {

					}

					@Override
					public void afterTextChanged(Editable arg0) {

						// setEditTextCursorLocation(site_search_content);
					}
				});
	}

	/** 创建popWindow */
	private void createPopWindow() {
		if (popWindow == null) {

			LayoutInflater inflact = LayoutInflater
					.from(SiteSearchActivity.this);

			View view = inflact.inflate(R.layout.pop_searchlist, null);
			popList = (ListView) view.findViewById(R.id.luck_pop_listView);
			SearchListAdapter adapter = new SearchListAdapter(mContext,
					resultList);
			popList.setAdapter(adapter);

			int size = 0;
			if (resultList.resultList.size() <= 6) {
				size = resultList.resultList.size();
			} else {
				size = 6;
			}
			int height = (int) (getResources()
					.getDimension(R.dimen.search_item_height)) * size;

			popWindow = new PopupWindow(view, layout_kuang.getWidth(), height);

			// popWindow.setFocusable(false); // 设置PopupWindow可获得焦点
			popWindow.setTouchable(true); // 设置PopupWindow可触摸
			popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸

			// 监听点击事件，点击其他位置，popupwindow小窗口消失
			// view.setOnTouchListener(new OnTouchListener() {
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// if (popWindow != null && popWindow.isShowing()) {
			// popWindow.dismiss();
			// }
			// return true;
			// }
			// });
			//PopUpWindow的背景不能为空。必须在popuWindow.showAsDropDown(v); 左下方
			popWindow.showAsDropDown(layout_kuang, 0, 0);
		}
		SearchListAdapter adapter = new SearchListAdapter(mContext, resultList);
		popList.setAdapter(adapter);
		if (!popWindow.isShowing()) {
			popWindow.showAsDropDown(layout_kuang, 0, 0);
		}

		adapter.setOnMyOnItemClickListener(new MyOnItemClickListener() {

			@Override
			public void onItemClick(String value) {

				key_word = value;
				startSearch();// 进入搜索
				// site_search_content.setText(value);
				//
				// site_search_content.setSelection(value.length());// 将光标移至文字末尾
			}
		});
		// popList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {
		//
		// @Override
		// public void onItemClick(AdapterView<?> view, View arg1, int position,
		// long id) {

		// if (popWindow != null && popWindow.isShowing()) {
		// popWindow.dismiss();
		// }
		// key_word = resultList.resultList.get(position).CATEGORY_NAME;// 搜索关键字
		// site_search_content.setText(resultList.resultList.get(position).CATEGORY_NAME);
		// }
		// });

		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		popList.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
					}
					break;
				}
				return true;
			}
		});

	}

	@Override
	public void onBackPressed() {

		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	// 将EditText的光标定位到字符的最后面
	public void setEditTextCursorLocation(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.site_search_back:
			this.finish();
			break;
		case R.id.tv_delAll:// 清除历史搜索
			searchDao.deleteAll();
			earchHistroyList = searchDao.fetcheAll();
			//判断他new 不new呢
			SearchHistoryAdapter adapter = new SearchHistoryAdapter(mContext,
					earchHistroyList);
			clv_search_stroy.setAdapter(adapter);
			history_layout.setVisibility(View.GONE);
			history_tv_top.setVisibility(View.VISIBLE);
			break;
		case R.id.site_search_sousuo_iv:
		case R.id.site_search_sousuo_tv:
			key_word = site_search_content.getText().toString().trim();
			if (key_word.equals("")) {
				Toast.makeText(this, "搜索内容不能为空!", Toast.LENGTH_LONG).show();
				return;
			} else {
				startSearch();// 进入搜索
			}
			break;
		}
	}

	/**
	 * 搜索初始化
	 */
	public void initSearch() {
		//包装请求好的
		RequstClient.SearchInit(new CustomResponseHandler(this) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);

				try {

					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 数据错误
						String errorMsg = obj.getString("msg");
						Toast.makeText(SiteSearchActivity.this, errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					mayLike = new Gson().fromJson(content,
							MayLikeCommodityBean.class);
					
					if (mayLike != null && mayLike.commodity != null
							&& mayLike.commodity.size() > 0) {
						int size = mayLike.commodity.size();
						final SiteSearchAdapter siteSearchAdapter = new SiteSearchAdapter(
								mContext, mayLike);
						if (size<=6) {
							if (size<=3) {
								LinearLayout.LayoutParams params = new LayoutParams(
										LayoutParams.MATCH_PARENT, DensityUtil  //很多控件的方法中都只提供了设置px的方法，例如setPadding，并没有提供设置dp的方法。这个时候，如果需要设置dp的话，就要将dp转换成px
												.dip2px(mContext, 125));
								gridview.setLayoutParams(params);
							}
							gridview.setNumColumns(3);
							gridview.setScrollDirectionPortrait(TwoWayAbsListView.SCROLL_VERTICAL);
							gridview.setScrollDirectionLandscape(TwoWayAbsListView.SCROLL_HORIZONTAL);
						}
						gridview.setAdapter(siteSearchAdapter);
						gridview.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(
									TwoWayAdapterView<?> parent, View view,
									int position, long id) {

								Intent intent = new Intent(mContext,
										GoodsDetailActivity.class);
								intent.putExtra(
										GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
										siteSearchAdapter.getItem(position).ID);
								mContext.startActivity(intent);
							}
						});

						layout_maylike.setVisibility(View.VISIBLE);
					} else {
						layout_maylike.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startSearch() {
		SearchHistroyBean bean = new SearchHistroyBean();
		bean.setSh_name(key_word);
		int code = 0;
		for (int i = 0; i < earchHistroyList.size(); i++) {// 遍历历史
			String name = earchHistroyList.get(i).getSh_name();
			if (name.equals(key_word)) {// 历史中没有搜索记录
				code = 1;
			}
		}
		if (1 != code) {
			searchDao.add(bean);
		}
		//搜索的历史呀  删除搜索的历史中的数据 删除第10个
		if (earchHistroyList.size() > 9) {
			searchDao.delete(earchHistroyList.get(9));
		}
		//进入搜索的商品的种类，，界面
		Intent intent = new Intent(this, CategorySearchActivity.class);
		intent.putExtra(CategorySearchActivity.BUNDLE_KEY_WORD, key_word);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 搜索下拉框，展示搜索三级列表商品参数
	 */
	public void showHistroyList(String key_word) {
		RequstClient.searchHistroyList(key_word, new CustomResponseHandler(
				this, false) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {

				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "站内搜索 下拉框返回：" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 获取错误信息
						String errorMsg = obj.getString("msg");
						Toast.makeText(SiteSearchActivity.this, errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					} else {// 获取成功
						resultList = new Gson().fromJson(content,
								SearchHistroyListResult.class);
						createPopWindow();
					}
					// COUNTRIES = new String[];
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

	}

	@Override
	protected void onResume() {

		earchHistroyList = searchDao.fetcheAll();
		if (earchHistroyList.size() == 0) {
			history_layout.setVisibility(View.GONE);
			history_tv_top.setVisibility(View.VISIBLE);
		} else {
			history_layout.setVisibility(View.VISIBLE);
			history_tv_top.setVisibility(View.GONE);
		}
		SearchHistoryAdapter adapter = new SearchHistoryAdapter(mContext,
				earchHistroyList);
		clv_search_stroy.setDividerHeight(16);
		clv_search_stroy.setDividerWidth(12);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 10, 20, 10);
		clv_search_stroy.setLayoutParams(params);
		clv_search_stroy.setAdapter(adapter);
		clv_search_stroy.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				SearchHistroyBean bean = earchHistroyList.get(position);
				key_word = bean.getSh_name();
				System.out.println("关键词----" + key_word);
				startSearch();// 直接进入搜索
			}
		});
		clv_search_stroy
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// 不做任何处理
						return true;
					}
				});
		tv_delAll.setOnClickListener(this);
		super.onResume();
	}

}
