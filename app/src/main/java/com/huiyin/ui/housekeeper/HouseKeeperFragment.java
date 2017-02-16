package com.huiyin.ui.housekeeper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.adapter.HouseKeeperGridViewAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.HouseKeeper;
import com.huiyin.ui.classic.CategoryFragment;
import com.huiyin.utils.NetworkUtils;

public class HouseKeeperFragment extends Fragment implements OnItemClickListener, OnClickListener {

	private List<HouseKeeper> listDatas;
	private GridView mHouseGridView;

	private HouseKeeperGridViewAdapter adapter;
	private LinearLayout class_list_level1_netll;
	private Button class_list_level1_reload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.house_keeper_first_pager_layout, null);

		initData();

		initViews(rootView);

		netTion();

		return rootView;
	}

	private void netTion() {
		if (!NetworkUtils.isNetworkAvailable(getActivity())) {
			mHouseGridView.setVisibility(View.GONE);
			class_list_level1_netll.setVisibility(View.VISIBLE);
			class_list_level1_reload.setOnClickListener(this);
			return;
		} else {
			requestData();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.class_list_level1_reload:
			if (!NetworkUtils.isNetworkAvailable(getActivity())) {
				return;
			} else {
				mHouseGridView.setVisibility(View.VISIBLE);
				class_list_level1_netll.setVisibility(View.GONE);
				requestData();
			}
			break;

		default:
			break;
		}
	}

	private void initData() {
		listDatas = new ArrayList<HouseKeeper>();
	}

	private void initViews(View view) {
		class_list_level1_netll = (LinearLayout) view.findViewById(R.id.class_list_level1_netll);
		class_list_level1_reload = (Button) view.findViewById(R.id.class_list_level1_reload);
		mHouseGridView = (GridView) view.findViewById(R.id.mHouseGridView);
		adapter = new HouseKeeperGridViewAdapter(getActivity(), listDatas);
		mHouseGridView.setAdapter(adapter);
		mHouseGridView.setOnItemClickListener(this);
		class_list_level1_reload.setOnClickListener(this);
	}

	/***
	 * 请求数据
	 * */
	private void requestData() {
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), false) {
			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				mHouseGridView.setVisibility(View.GONE);
				class_list_level1_netll.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onRefreshData(String content) {
				analyticalData(content);// 解析数据
			}
		};
		RequstClient.getTitle(handler);
	}

	/**
	 * 解析数据
	 * 
	 * */
	private void analyticalData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONArray arrays = roots.getJSONArray("wisdom");
				for (int i = 0; i < arrays.length(); i++) {
					HouseKeeper bean = new HouseKeeper();
					JSONObject obj = arrays.getJSONObject(i);

					bean.setName(obj.getString("TITLE"));
					bean.setRowId(obj.getInt("ID"));

					listDatas.add(bean);
					if (i >= 8) {
						HouseKeeper bean1 = new HouseKeeper();
						bean1.setName("更多");
						bean1.setRowId(0);
						listDatas.add(bean1);
						break;
					}
				}

				loadData();
			} else {
				String errorMsg = roots.getString("msg");
				Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void loadData() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		HouseKeeper bean = listDatas.get(position);
		if (bean.getRowId() == 0) {
			// 更多
			Intent intent = new Intent(new Intent(getActivity(), HouseKeeperListActivity.class));
			startActivity(intent);
		} else {
			Intent intent = new Intent(new Intent(getActivity(), HkDetailActivity.class));
			intent.putExtra("id", bean.getRowId() + "");
			intent.putExtra("title", bean.getName() + "");
			startActivity(intent);
		}
	}
}
