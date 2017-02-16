package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.utils.ImageManager;

public class BannerAdapter extends BaseAdapter {

	private ArrayList<String> list;
	private Context mContext;

	public BannerAdapter(Context mContext) {
//		list = new ArrayList<String>();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
//		return list.size();
		return 1;
	}

	@Override
	public Object getItem(int position) {
//		return list.get(position);
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bannner2, parent, false);
		}
		ImageView bananaView = ViewHolder.get(convertView, R.id.banner_iv);
		ImageManager.Load(list.get(position), bananaView);*/
		View view=LayoutInflater.from(mContext).inflate(R.layout.bannner2, parent, false);
		return view;
	}

}
