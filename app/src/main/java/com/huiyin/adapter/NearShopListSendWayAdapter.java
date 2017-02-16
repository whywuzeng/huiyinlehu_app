package com.huiyin.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ViewHolder;

public class NearShopListSendWayAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> list;

	public NearShopListSendWayAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<String>();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_listview_send_way_item, parent, false);
		}

		TextView name = ViewHolder.get(convertView, R.id.send_way_name);
		String temp = (String) getItem(position);
		name.setText(temp);
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<String> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}

}