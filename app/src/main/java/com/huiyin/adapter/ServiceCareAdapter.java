package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.huiyin.R;
import com.huiyin.bean.ServiceCardListBean.OperationData;
import com.huiyin.ui.nearshop.StoreListItem;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

public class ServiceCareAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<OperationData> list;

	public ServiceCareAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<OperationData>();
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.service_card_list_item, parent, false);
		}

		TextView num = ViewHolder.get(convertView, R.id.operation_num);
		TextView txnid = ViewHolder.get(convertView, R.id.operation_txnid);
		TextView time = ViewHolder.get(convertView, R.id.operation_time);
		TextView figure = ViewHolder.get(convertView, R.id.operation_figure);

		OperationData temp = list.get(position);

		txnid.setText(temp.getNAME() + "：" + temp.getTXNID());
		time.setText("时间：" + temp.getADDDATE());
		figure.setText(temp.getTYPE() + "  "
				+ MathUtil.priceForAppWithSign(temp.getBALANCE()));
		switch (temp.getSTAUTS()) {
		case 0:
			num.setVisibility(View.GONE);
			break;
		case 1:
			num.setVisibility(View.VISIBLE);
			num.setText(temp.getNAME() + "订单：" + temp.getORDERNUM());
			break;
		}
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<OperationData> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}
}
