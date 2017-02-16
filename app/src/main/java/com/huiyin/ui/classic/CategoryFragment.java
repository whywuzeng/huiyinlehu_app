package com.huiyin.ui.classic;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.adapter.CategoryLeftViewAdapter;
import com.huiyin.adapter.CategoryRightListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.CategoryBean;
import com.huiyin.ui.home.SiteSearchActivity;
import com.huiyin.ui.show.view.AnimationSildingLayout;
import com.huiyin.utils.NetworkUtils;
import com.zxing.scan.activity.ZxingCodeActivity;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements OnClickListener {

	private static final String TAG = "ClassicFragment";

	private View v = null;
	private AnimationSildingLayout layout;
	private ListView rightList;
	private ListView leftList;
	private CategoryRightListAdapter rightAdapter;
	private CategoryLeftViewAdapter leftAdapter;
	private int foodpoition;
	private View ab_search;;
	private ImageView class_sort_code_scan;
	private ArrayList<CategoryBean> categories;
	private ArrayList<CategoryBean> categories1;
	private MyOnItemClickListener listener;
	private RelativeLayout class_list_level1_rl;
	private Button class_list_level1_reload;
	private LinearLayout class_list_level1_netll;
	private CategoryListFragment cf = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_classic, null);
		initView();
		return v;
	}

	private void initView() {
		cf = CategoryListFragment.getInstance();
		class_list_level1_rl = (RelativeLayout) v
				.findViewById(R.id.class_list_level1_rl);
		class_list_level1_reload = (Button) v
				.findViewById(R.id.class_list_level1_reload);
		class_list_level1_netll = (LinearLayout) v
				.findViewById(R.id.class_list_level1_netll);

		class_sort_code_scan = (ImageView) v
				.findViewById(R.id.class_sort_code_scan);

		class_sort_code_scan.setOnClickListener(this);
		leftList = (ListView) v.findViewById(R.id.leftCategoryList);
		rightList = (ListView) v.findViewById(R.id.rightCategoryList);
		ab_search = v.findViewById(R.id.ab_search);
		ab_search.setOnClickListener(this);

		// 跳转到三级目录列表
		listener = new MyOnItemClickListener();
		rightList.setOnItemClickListener(listener);

		layout = (AnimationSildingLayout) v.findViewById(R.id.main_slayout);
		layout.initLayout(leftList, rightList);
		layout.setOnSildingFinishListener(new AnimationSildingLayout.OnSildingFinishListener() {
			@Override
			public void onSildingFinish() {

			}
		});
		categories = new ArrayList<CategoryBean>();

		// 初始化数据
		initDate();
	}

	/**
	 * 初始化数据（主要从服务其获取Json数据并解析）
	 */
	private void initDate() {
		// 检测网络
		if (!NetworkUtils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "网络不可用，请先检查网络！", Toast.LENGTH_SHORT)
					.show();
			class_list_level1_rl.setVisibility(View.GONE);
			class_list_level1_netll.setVisibility(View.VISIBLE);
			class_list_level1_reload.setOnClickListener(this);
			return;
		} else {
			getDate(true);
		}
	}

	private void getDate(final boolean bool) {
		// 请求一级二级目录列表
		RequstClient.getClassLists(URLs.SHOUYE_URL, new CustomResponseHandler(
				getActivity()) {
			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				Toast.makeText(getActivity(),
						"实在是很抱歉，服务器可能出现错误。请耐心等待...，稍后再访问。",
						Toast.LENGTH_SHORT).show();
				class_list_level1_rl.setVisibility(View.GONE);
				class_list_level1_netll.setVisibility(View.VISIBLE);
				class_list_level1_reload.setOnClickListener(CategoryFragment.this);
			}

			@Override
			public void onStart() {
				if (bool) {
					super.onStart();
				}
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (statusCode != 200) {
					Toast.makeText(getActivity(),
							"实在是很抱歉，服务器可能出现错误。请耐心等待...，稍后再访问。",
							Toast.LENGTH_SHORT).show();
					class_list_level1_rl.setVisibility(View.GONE);
					class_list_level1_netll.setVisibility(View.VISIBLE);
					return;
				} else {
					changeJsonToCategory(content);
					leftAdapter = new CategoryLeftViewAdapter(getActivity(),
							categories);
					leftList.setAdapter(leftAdapter);
					leftList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							foodpoition = position;
							leftAdapter.setSelectedPosition(position);
							leftAdapter.notifyDataSetInvalidated();
							categories1 = categories.get(position)
									.getCategorys();
							rightAdapter = new CategoryRightListAdapter(
									getActivity(), categories1, foodpoition);
							rightList.setDivider(null);
							rightList.setAdapter(rightAdapter);
							rightList.setOnItemClickListener(listener);
							layout.startSildingInAnimation(position);
						}
					});
				}
			}
		});

	}
	// 右边listView的数据的请求
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Bundle args = new Bundle();
			args.putSerializable(CategoryListFragment.INTENT_CATEGORY_BEAN,
					categories1.get(position));
			cf.setArguments(args);
			//fragment换
			ft.add(R.id.fragment_content, cf);
			//这个fragment 换到堆载里
			ft.addToBackStack(getTag());
			ft.commit();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.class_sort_code_scan:
			startActivity(new Intent(getActivity(), ZxingCodeActivity.class));
			break;
		case R.id.class_list_level1_reload:
			if (!NetworkUtils.isNetworkAvailable(getActivity())) {
				Toast.makeText(getActivity(), "网络没连接吧，亲！请先检查一下网络吧！",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				class_list_level1_rl.setVisibility(View.VISIBLE);
				class_list_level1_netll.setVisibility(View.GONE);
				getDate(true);
			}
			break;
		case R.id.ab_search:
			startActivity(new Intent(getActivity(), SiteSearchActivity.class));
			break;
		default:
			break;
		}
	}

	/**
	 * 将二级分类json数据转换成category数组对象
	 * 
	 * @param json
	 */
	public void changeJsonToCategory(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			if(!jsonObj.getString("type").equals("1")) {
				Toast.makeText(getActivity(),
						"实在是很抱歉，服务器可能出现错误。请耐心等待...，稍后再访问。",
						Toast.LENGTH_SHORT).show();
				class_list_level1_rl.setVisibility(View.GONE);
				class_list_level1_netll.setVisibility(View.VISIBLE);
				return;
			}
			JSONArray jsonArray1 = jsonObj.getJSONArray("oneCategories");
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObj1 = (JSONObject) jsonArray1.get(i);
				CategoryBean category1 = new CategoryBean();
				category1.setCategoryID(jsonObj1.optInt("ID"));
				category1
						.setCategoryName(jsonObj1.optString("ONE_PARENT_NAME"));
				category1
						.setCategoryImgUri(jsonObj1.optString("ONE_ICON_PATH"));
				JSONArray jsonArray2 = jsonObj1.getJSONArray("twoList");
				ArrayList<CategoryBean> categories1 = new ArrayList<CategoryBean>();
				for (int j = 0; j < jsonArray2.length(); j++) {
					JSONObject jsonObj2 = (JSONObject) jsonArray2.get(j);
					CategoryBean category2 = new CategoryBean();
					category2.setCategoryID(jsonObj2.optInt("ID"));
					category2.setCategoryName(jsonObj2
							.optString("CATEGORY_NAME"));
					category2
							.setCategoryImgUri(jsonObj2.optString("ICON_PATH"));
					JSONArray jsonArray3 = jsonObj2.getJSONArray("THREELIST");
					ArrayList<CategoryBean> categories2 = new ArrayList<CategoryBean>();
					for (int k = 0; k < jsonArray3.length(); k++) {
						JSONObject jsonObj3 = (JSONObject) jsonArray3.get(k);
						CategoryBean category3 = new CategoryBean();
						category3.setCategoryID(jsonObj3.optInt("THREEE_ID"));
						category3.setCategoryName(jsonObj3
								.optString("THREEE_NAME"));
						categories2.add(category3);
					}
					category2.setCategorys(categories2);
					categories1.add(category2);
				}
				category1.setCategorys(categories1);
				categories.add(category1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回到类别页面
	 * 
	 * */
	public void backToCategory() {
		if (cf != null && cf.isShow()) {
			getFragmentManager().popBackStack();
		}
	}

	/***
	 * 类别初始化到起始第一类类别
	 * 
	 * */
	public void backToOrigin() {
		if (layout != null) {
			//已经在csdn 封装好的一个类
			layout.scrollInit();
		}
	}

}
