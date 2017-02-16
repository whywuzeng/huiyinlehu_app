package com.huiyin.adapter;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.ui.home.Logistics.LocationCarActivity;
import com.huiyin.ui.home.Logistics.LogisticLineView;
import com.huiyin.ui.home.Logistics.LogisticListBean.orderLogisticsItem;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LogisticsQueryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<orderLogisticsItem> list;

	public LogisticsQueryAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<orderLogisticsItem>();
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
					R.layout.logistics_query_lv_item, parent, false);
		}
		TextView info = ViewHolder.get(convertView, R.id.logistics_item_info);
		ImageView image = ViewHolder
				.get(convertView, R.id.logistics_item_image);
		TextView name = ViewHolder.get(convertView, R.id.logistics_item_name);
		TextView count = ViewHolder.get(convertView,
				R.id.logistics_item_specvalue);
		TextView price = ViewHolder.get(convertView, R.id.logistics_item_price);
		TextView orderTime = ViewHolder.get(convertView,
				R.id.logistics_item_creat_date);
		Button car = ViewHolder.get(convertView, R.id.logistics_item_car);

		LogisticLineView mLogisticLineView = ViewHolder.get(convertView,
				R.id.mLogisticLineView);

		orderLogisticsItem temp = list.get(position);
		String theMsg = mContext.getResources().getString(
				R.string.logisticsinfo);
		theMsg = String.format(theMsg, temp.ORDER_CODE, typeInfo(temp.STATUS),
				temp.BUY_QTY, MathUtil.priceForAppWithSign(temp.TOTAL_PRICE));
		info.setText(Html.fromHtml(theMsg));
		if (!StringUtils.isBlank(temp.COMMODITY_IMAGE_PATH))
			ImageManager.LoadWithServer(temp.COMMODITY_IMAGE_PATH, image);
		name.setText(temp.COMMODITY_NAME);
		if (StringUtils.isBlank(temp.SPECVALUE)) {
			count.setText("数量：" + temp.BUY_QTY + "件");
		} else {
			count.setText(temp.SPECVALUE + "数量：" + temp.BUY_QTY + "件");
		}
		String priceT = "价格：<font color=\"red\">"
				+ MathUtil.priceForAppWithSign(temp.PURCHASE_PRICE) + "</font>";
		price.setText(Html.fromHtml(priceT));
		orderTime.setText("下单时间：" + temp.CREATE_DATE);

		if (temp.orderLogisticsList != null
				&& temp.orderLogisticsList.getTYPE() != -1) {
			mLogisticLineView.setVisibility(View.VISIBLE);
			mLogisticLineView.setData(temp.orderLogisticsList);
			if (temp.orderLogisticsList.getTYPE() == 1) {
				car.setVisibility(View.VISIBLE);
				car.setOnClickListener(new CarButton(temp.orderLogisticsList
						.getID()));
			} else {
				car.setVisibility(View.INVISIBLE);
			}

		} else {
			car.setVisibility(View.INVISIBLE);
			mLogisticLineView.setVisibility(View.GONE);
			// orderTime.setText("下单时间：" + temp.CREATE_DATE + " 暂无物流信息");
		}

		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<orderLogisticsItem> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}

	public int getId(int position) {
		return list.get(position).ORDER_ID;
	}

	private String typeInfo(int type) {
		String temp = "";
		switch (type) {
		case 1:
			temp = "未付款";
			break;
		case 2:
			temp = "订单待审核";
			break;
		case 3:
			temp = "待发货";
			break;
		case 4:
			temp = "待收货";
			break;
		case 5:
			temp = "交易完成";
			break;
		case 6:
			temp = "取消订单";
			break;
		}
		return temp;
	}

	private class CarButton implements OnClickListener {

		private int Id;

		public CarButton(int Id) {
			this.Id = Id;
		}

		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			i.setClass(mContext, LocationCarActivity.class);
			i.putExtra("Id", Id);
			mContext.startActivity(i);
		}

	}
}
