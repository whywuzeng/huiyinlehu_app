package com.huiyin.ui.classic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.ClassList3Adapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.CategoryBean;
import com.huiyin.bean.ClassListLevel3Bean;
import com.huiyin.bean.ClassListLevel3Bean.Hotcommodity;
import com.huiyin.ui.home.SiteSearchActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.wight.MyListView;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.Mode;
import com.huiyin.wight.pulltorefresh.PullToRefreshScrollView;
import com.zxing.scan.activity.ZxingCodeActivity;

@SuppressLint("ValidFragment")
public class CategoryListFragment extends Fragment implements OnClickListener {
	private static final String TAG = "ClassList3";
	public static final String INTENT_CATEGORY_BEAN = "category";
	private TextView ab_back;
	private TextView ab_title;
	private ImageView ab_right_btn1, ab_right_btn2;
	private MyListView class_list_level3_lv;
	private LinearLayout class_list_level3_load;
	private LinearLayout class_list_level3_forviewpager_load;
	ClassListLevel3Bean bean3;
	private ClassList3Adapter adapter3;
	private CategoryBean categoryBean;
	private ImageView jianbian_right_iv;
	private ImageView jianbian_left_iv;
	private View category_line;
	private PullToRefreshScrollView sc;
	private View v;
	private List<Hotcommodity> list = null;
	// 单利模式创建一个fragment实例
	private static CategoryListFragment categoryListFragment = null;

	private CategoryListFragment() {
	}

	public static CategoryListFragment getInstance() {
		if (categoryListFragment == null) {
			synchronized (CategoryListFragment.class) {
				if (categoryListFragment == null) {
					categoryListFragment = new CategoryListFragment();
				}
			}
		}
		return categoryListFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.class_list_level3, null);
		initView();
		return v;
	}

	private void initView() {
		category_line = v.findViewById(R.id.category_line);
		jianbian_left_iv = (ImageView) v.findViewById(R.id.jianbian_left_iv);
		jianbian_right_iv = (ImageView) v.findViewById(R.id.jianbian_right_iv);
		categoryBean = (CategoryBean) getArguments().getSerializable(INTENT_CATEGORY_BEAN);
		sc = (PullToRefreshScrollView) v.findViewById(R.id.class_list_level3_sc);
		sc.setMode(Mode.BOTH);

		sc.setEnabled(true);
		sc.setLoadingDrawable(null, Mode.BOTH);
		sc.getLoadingLayoutProxy().setLastUpdatedLabel("");
		sc.getLoadingLayoutProxy().setPullLabel("");
		sc.getLoadingLayoutProxy().setRefreshingLabel("");
		sc.getLoadingLayoutProxy().setReleaseLabel("");

		class_list_level3_lv = (MyListView) v.findViewById(R.id.class_list_level3_lv);

		class_list_level3_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (bean3.category != null && bean3.category.size() != 0) {
					String id4 = bean3.category.get(arg2).ID;
					String categoryName = bean3.category.get(arg2).CATEGORY_NAME;
					Intent intent = new Intent(getActivity(), CategorySearchActivity.class);
					if ("全部".equals(categoryName)) {
						String two_category_id = categoryBean.getCategoryID() + "";
						intent.putExtra(CategorySearchActivity.BUNDLE_TWO_CATEGORY_ID, two_category_id);
						intent.putExtra(CategorySearchActivity.BUNDLE_KEY_CATEGORY_ID, "0");
					} else {
						intent.putExtra(CategorySearchActivity.BUNDLE_KEY_CATEGORY_ID, id4);
					}
					startActivity(intent);
				} else {
					return;
				}
			}
		});

		ab_back = (TextView) v.findViewById(R.id.ab_back);
		ab_title = (TextView) v.findViewById(R.id.ab_title);
		ab_right_btn1 = (ImageView) v.findViewById(R.id.ab_right_btn1);
		ab_right_btn2 = (ImageView) v.findViewById(R.id.ab_right_btn2);
		View line = v.findViewById(R.id.ab_line);
		line.setVisibility(View.VISIBLE);

		class_list_level3_load = (LinearLayout) v.findViewById(R.id.class_list_level3_load);
		ab_back.setOnClickListener(this);
		ab_right_btn1.setOnClickListener(this);
		ab_right_btn2.setOnClickListener(this);

		ab_right_btn2.setImageResource(R.drawable.ab_ic_search2);
		ab_right_btn1.setImageResource(R.drawable.ab_home_barcode);
		ab_right_btn1.setVisibility(View.VISIBLE);
		ab_right_btn2.setVisibility(View.VISIBLE);
		ab_title.setText(categoryBean.getCategoryName());

		// 初始化数据
		initDate();
	}

	private void initDate() {
		// 请求数据并初始化listView即三级目录列表
		if (categoryBean != null) {
			int categoryID = categoryBean.getCategoryID();
			RequstClient.getClassListLevel3(URLs.SORT_LIST_LEVEL3, categoryID + "", new CustomResponseHandler(getActivity()) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					Gson gson = new Gson();
					bean3 = gson.fromJson(content, ClassListLevel3Bean.class);
					if (bean3.type.equals("1")) {
//						Toast.makeText(getActivity(), "数据加载" + bean3.msg, 0).show();
						adapter3 = new ClassList3Adapter(getActivity(), bean3);
						class_list_level3_lv.setAdapter(adapter3);
						adapter3.notifyDataSetChanged();

						if (bean3.hotcommodity != null) {
							// 初始化横条
							initScrollView();
						} else {
							return;
						}

					} else {
						Toast.makeText(getActivity(), "数据异常！", 0).show();
						return;
					}
				}
			});
		} else {
			Toast.makeText(getActivity(), "获取数据异常...", Toast.LENGTH_LONG).show();
			return;
		}
	}

	private void initScrollView() {
		// 初始化滚动条（加载二十张图片的广告）
		// 获取屏幕的宽度
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		int newWidth = screenWidth / 3;
		LayoutParams params = new LayoutParams(newWidth, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 5, 0);
		list = bean3.hotcommodity;
		if (bean3 != null && list != null) {

			if (list.size() > 0) {
				category_line.setVisibility(View.VISIBLE);
			} else {
				category_line.setVisibility(View.GONE);
			}

			if (list.size() > 3) {
				jianbian_left_iv.setVisibility(View.VISIBLE);
				jianbian_right_iv.setVisibility(View.VISIBLE);
			} else {
				jianbian_left_iv.setVisibility(View.GONE);
				jianbian_right_iv.setVisibility(View.GONE);
			}

			// 按价格的升序排列
			Collections.sort(list, new Comparator<Hotcommodity>() {

				@Override
				public int compare(Hotcommodity hot1, Hotcommodity hot2) {
					double price1 = Double.parseDouble(MathUtil.priceForAppWithOutSign(hot1.PRICE));
					double price2 = Double.parseDouble(MathUtil.priceForAppWithOutSign(hot2.PRICE));

					if (price1 == price2) {
						return 0;
					}
					if (price1 > price2) {
						return 1;
					} else {
						return -1;
					}
				}
			});

			// 引入一个布局
			for (int i = 0; i < list.size(); i++) {
				final int count = i;
				View v = LayoutInflater.from(getActivity()).inflate(R.layout.class_list_level3_ll_for_scrollview, null);
				ImageView img = (ImageView) v.findViewById(R.id.class_list_level3_forviewpager_img);
				TextView name = (TextView) v.findViewById(R.id.class_list_level3_forviewpager_name);
				TextView price = (TextView) v.findViewById(R.id.class_list_level3_forviewpager_price);

				if (list.get(i).equals("") || list.get(i) == null) {
					return;
				} else {
					ImageManager.LoadWithServer(list.get(i).COMMODITY_IMAGE_PATH, img);
				}
				if (list.get(i).COMMODITY_NAME != null && !list.get(i).COMMODITY_NAME.equals("")) {
					name.setText(list.get(i).COMMODITY_NAME);
				} else {
					name.setText("");
				}
				if (list.get(i).PRICE != null && !list.get(i).PRICE.equals("")) {
					price.setText(MathUtil.priceForAppWithSign(list.get(i).PRICE));
				} else {
					price.setText(MathUtil.priceForAppWithSign(0));
				}

				class_list_level3_forviewpager_load = (LinearLayout) v.findViewById(R.id.class_list_level3_forviewpager_load);
				class_list_level3_forviewpager_load.setLayoutParams(params);
				class_list_level3_forviewpager_load.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
						if (list.get(count).ID != null && !list.get(count).ID.equals("")) {
							intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, list.get(count).ID);
							Log.i(TAG, list.get(count).ID + "三级分类列表的热销商品的ID");
							startActivity(intent);
						} else {
							return;
						}
					}
				});
				class_list_level3_load.addView(v);
			}
		} else {
			return;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ab_back:
			getFragmentManager().popBackStack();
			break;
		case R.id.ab_right_btn2:
			startActivity(new Intent(getActivity(), SiteSearchActivity.class));
			break;
		case R.id.ab_right_btn1:
			startActivity(new Intent(getActivity(), ZxingCodeActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		isShow = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		isShow = false;
	}

	private boolean isShow;

	public boolean isShow() {
		return isShow;
	}

}
