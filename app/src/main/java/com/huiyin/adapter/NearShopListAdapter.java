package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.NearShopBean.ShopMention;
import com.huiyin.bean.StoreListComparator;
import com.huiyin.utils.ViewHolder;

public class NearShopListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ShopMention> list;

	public NearShopListAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<ShopMention>();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_listview_item, parent, false);
		}

		TextView name = ViewHolder.get(convertView, R.id.shopName);
		TextView juli = ViewHolder.get(convertView, R.id.shop_juli);

		ShopMention temp = (ShopMention) getItem(position);

		name.setText(temp.NAME);
		juli.setText(String.format("%.2f", temp.distance / 1000) + "km");
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<ShopMention> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		Collections.sort(list, new StoreListComparator());
		notifyDataSetChanged();
	}

	public int getShopId(int position) {
		return list.get(position).ID;
	}

	public String getShopName(int position) {
		return list.get(position).NAME;
	}
}
