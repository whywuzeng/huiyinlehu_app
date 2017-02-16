package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.HouseKeeper;

public class HouseKeeperGridViewAdapter extends BaseAdapter {

	private Context mContext;

	private List<HouseKeeper> listDatas;
	public LayoutInflater layoutInflater;

	public HouseKeeperGridViewAdapter(Context context,
			List<HouseKeeper> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		HouseKeeper bean = listDatas.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.house_kpper_item,
					null);
			holder.item = (TextView) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item.setText(bean.getName());
		return convertView;
	}

	private class ViewHolder {
		TextView item;
	}

}