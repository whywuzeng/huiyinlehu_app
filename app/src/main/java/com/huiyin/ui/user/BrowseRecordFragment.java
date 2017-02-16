package com.huiyin.ui.user;

import java.util.List;

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
import com.huiyin.adapter.BrowseRescordAdapter;
import com.huiyin.bean.BrowseItem;
import com.huiyin.ui.classic.GoodsDetailActivity;

public class BrowseRecordFragment extends Fragment implements OnItemClickListener {

	private List<BrowseItem> listDatas;
	private GridView mGridView;
	private BrowseRescordAdapter adapter;

	public void setListDatas(List<BrowseItem> listDatas) {
		this.listDatas = listDatas;

		if (mGridView != null) {
			setView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_lehu_browse_record, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.mBrowseRecordGridView);

		if (listDatas != null) {
			setView();
		}
	}

	private void setView() {
		adapter = new BrowseRescordAdapter(getActivity(), listDatas);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		BrowseItem bean = listDatas.get(position);

		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, bean.COMMODITY_ID);
		getActivity().startActivity(intent);

	}
}
