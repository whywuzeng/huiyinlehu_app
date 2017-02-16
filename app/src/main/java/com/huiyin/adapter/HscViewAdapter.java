package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.ui.classic.SaleRankActivity.HlvBean;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HscViewAdapter extends BaseAdapter {
	private HlvBean mHlvBean;
	private LayoutInflater inflater;
	private Activity act;
	private int lv_hSelect=0;
	public HscViewAdapter(Activity act,HlvBean mHlvBean) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.mHlvBean = mHlvBean;
		this.inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHlvBean.erjifenlei == null ? 0 : mHlvBean.erjifenlei.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mHlvBean.erjifenlei.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return mHlvBean.erjifenlei.get(arg0).hashCode();
	}

	public void setCurrent(int positon){
		this.lv_hSelect=positon;
	}
	
	public void addItems(HlvBean bean) {
		this.mHlvBean = bean;
		notifyDataSetChanged();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(act)
					.inflate(R.layout.h_listview_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.h_list_item);
			convertView.setTag(holder);
			// activity_scale_item
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title
				.setText(mHlvBean.erjifenlei.get(position).CATEGORY_NAME);
		holder.title.setSelected(position == lv_hSelect ? true : false);
		return convertView;
	}

	private class ViewHolder {
		TextView title;
	}

}
