package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.custom.vg.list.CustomAdapter;
import com.huiyin.R;
import com.huiyin.bean.GoodsDetailBeen.Values1;

public class GoodsDetailSpecValue1Adapter extends CustomAdapter {

	private ArrayList<Values1> specValueList;
	
	private String spec_value_id;

	public LayoutInflater layoutInflater;

	public int selectPosition = 0;

	public GoodsDetailSpecValue1Adapter(Context context,
			ArrayList<Values1> specValueList) {
		this.specValueList = specValueList;

		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return specValueList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return specValueList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (null == contentView) {
			viewHolder = new ViewHolder();
			contentView = layoutInflater.inflate(R.layout.item_spec, null);
			viewHolder.name = (TextView) contentView.findViewById(R.id.tv_spec);
			contentView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) contentView.getTag();
		}
		
		Values1 bean = specValueList.get(position);
		viewHolder.name.setText(bean.VALUE_NAME);

		if (selectPosition == position) {
			viewHolder.name.setSelected(true);
		}else {
			viewHolder.name.setSelected(false);
		}
		return contentView;
	}

	private class ViewHolder {
		TextView name;
	}
}
