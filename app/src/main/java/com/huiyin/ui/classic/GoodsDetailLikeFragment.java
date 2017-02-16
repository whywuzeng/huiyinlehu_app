package com.huiyin.ui.classic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.huiyin.R;
import com.huiyin.adapter.GoodsDetailLikeGridViewAdapter;
import com.huiyin.bean.GoodsDetailBeen.GDBItem;

public class GoodsDetailLikeFragment extends Fragment implements
		OnItemClickListener {

	private GridView mGridView;

	private List<GDBItem> listDatas;

	private GoodsDetailLikeGridViewAdapter adapter;

	public GoodsDetailLikeFragment() {
	}

	public void setData(List<GDBItem> listDatas) {
		this.listDatas = listDatas;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_detail_like,
				container, false);
		initData();

		initViews(view);
		return view;
	}

	private void initData() {
		if (listDatas == null) {
			listDatas = new ArrayList<GDBItem>();
		}

	}

	public void initViews(View view) {
		mGridView = (GridView) view.findViewById(R.id.likeGridView);

		adapter = new GoodsDetailLikeGridViewAdapter(getActivity(), listDatas);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		GDBItem bean = listDatas.get(position);
		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, bean.ID);
		startActivity(intent);
		getActivity().finish();

	}

}