package com.huiyin.ui.nearshop.adapter;

import java.util.ArrayList;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huiyin.R;
import com.huiyin.ui.nearshop.StoreListItem;
import com.huiyin.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NearShopAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<StoreListItem> list;
	private LatLng mLatLng;// 当前位置

	public NearShopAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<StoreListItem>();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.near_shop_lv_item, parent, false);
		}

		TextView name = ViewHolder.get(convertView, R.id.near_shop_lv_name);
		TextView address = ViewHolder.get(convertView, R.id.near_shop_lv_address);
		TextView phone = ViewHolder.get(convertView, R.id.near_shop_lv_phone);
		TextView juli = ViewHolder.get(convertView, R.id.near_shop_lv_juli);

		StoreListItem temp = (StoreListItem) getItem(position);

		name.setText(temp.getNAME());
		address.setText(temp.getADDRESS());
		phone.setText("联系电话：" + temp.getTELEPHONE());
		if (temp.getDistance() != Double.MAX_VALUE) {
			double distance = temp.getDistance();
			juli.setText(String.format("%.2f", distance / 1000) + "km");
		} else {
			juli.setText("未知距离");
		}
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<StoreListItem> temp, LatLng mLatLng) {
		if (temp == null) {
			return;
		}
		this.mLatLng = mLatLng;
		for (int i = 0; i < temp.size(); i++) {
			LatLng storeLatLng = temp.get(i).getLatLng();
			if (mLatLng != null && storeLatLng != null) {
				double distance = DistanceUtil.getDistance(mLatLng, storeLatLng);
				temp.get(i).setDistance(distance);
			} else {
				temp.get(i).setDistance(Double.MAX_VALUE);
			}
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		// Collections.sort(list, new StoreListItemComparator());
		notifyDataSetChanged();

	}

	public ArrayList<StoreListItem> getList() {
		return list;
	}
}
