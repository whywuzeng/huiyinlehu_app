package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.custom.vg.list.CustomAdapter;
import com.huiyin.R;
import com.huiyin.bean.GoodsDetailBeen.Values1;
import com.huiyin.bean.Attribute;
import com.huiyin.bean.SearchHistroyBean;

public class SelecteAttributeAdapter extends CustomAdapter {

	private String spec_value_id;
	private ArrayList<Attribute> attributes;
	public LayoutInflater layoutInflater;

	public int selectPosition = 0;

	public SelecteAttributeAdapter(Context context,ArrayList<Attribute> attributes) {
		this.attributes=attributes;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return attributes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return attributes.get(position);
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
			contentView = layoutInflater.inflate(R.layout.item_selecte_attribute, null);
			viewHolder.name = (TextView) contentView.findViewById(R.id.tv_attribute_name);
			viewHolder.layout_del = (LinearLayout) contentView.findViewById(R.id.layout_del);
			contentView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) contentView.getTag();
		}
		Attribute bean = attributes.get(position);
		viewHolder.name.setText(bean.value.name);
		viewHolder.layout_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 删除该属性
				if (null!= listener ) {
					listener.onItemDelete(position);
				}
			}
		});
		return contentView;
	}

	static class ViewHolder {
		TextView name;// 名称
		LinearLayout layout_del;// 删除图标
	}

	private OnItemDeleteListner listener;

	public void setOnItemDeleteListner(OnItemDeleteListner listener) {
		this.listener = listener;
	}

	public interface OnItemDeleteListner {
		void onItemDelete(int position);
	}
}
