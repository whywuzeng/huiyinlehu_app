package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.OrderBean;
import com.huiyin.bean.OrderItem;
import com.huiyin.bean.ShopItem;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteGoodsListAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private OrderBean mOrderBean;

	private Context mContext;

	private ImageLoader imageLoader;

	public WriteGoodsListAdapter(Context context, OrderBean mOrderBean) {
		mContext = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOrderBean = mOrderBean;
		imageLoader = ImageLoader.getInstance();
	}

	public OrderBean getOrderBean() {
		return this.mOrderBean;
	}

	@Override
	public int getCount() {
		return mOrderBean.orderList == null ? 0 : mOrderBean.orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return position == 0 ? null : mOrderBean.orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mOrderBean.orderList.get(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.fragment_write_goods_list_item, null);
		holder.layout_group = (LinearLayout) convertView.findViewById(R.id.layout_group);
		holder.groupTitle = (TextView) convertView.findViewById(R.id.groupTitle);
		holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);

		final OrderItem orderItem = mOrderBean.orderList.get(position);

		if (orderItem.goodList.size() > 1) {
			holder.layout_group.setVisibility(View.VISIBLE);
			holder.groupTitle.setText(String.format(mContext.getString(R.string.buy_together), orderItem.goodList.size() + 1));
		} else {
			holder.layout_group.setVisibility(View.GONE);
		}

		if (orderItem.goodList.size() > 0) {
			initGoodView(orderItem.good_qty, orderItem.shopItem, holder.contentLayout, true);
			for (int i = 0; i < orderItem.goodList.size(); i++) {
				if (i == orderItem.goodList.size() - 1) {
					ShopItem shop_item = orderItem.goodList.get(i);
					initGoodView(orderItem.good_qty, shop_item, holder.contentLayout, false);
				} else {
					ShopItem shop_item = orderItem.goodList.get(i);
					initGoodView(orderItem.good_qty, shop_item, holder.contentLayout, true);
				}
			}
		} else {
			initGoodView(orderItem.good_qty, orderItem.shopItem, holder.contentLayout, false);
		}

		return convertView;

	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		TextView groupTitle;
		LinearLayout layout_group;
		LinearLayout contentLayout;
	}

	/**
	 * 添加Item数据
	 * 
	 * @param shopItem
	 * @param shop_add_listitem
	 * @param isVisible
	 *            true显示下划线
	 */
	private void initGoodView(int count, ShopItem shopItem, LinearLayout shop_add_listitem, boolean isVisible) {
		View view = inflater.inflate(R.layout.activity_write_goods_list_item, null);
		ImageView goodlist_img = (ImageView) view.findViewById(R.id.goodlist_img);
		TextView goodlist_title = (TextView) view.findViewById(R.id.goodlist_title);
		TextView goodlist_price = (TextView) view.findViewById(R.id.goodlist_price);
		TextView goodlist_count = (TextView) view.findViewById(R.id.goodlist_count);
		View list_line = (View) view.findViewById(R.id.list_line);
		if (isVisible) {
			list_line.setVisibility(View.VISIBLE);
		} else {
			list_line.setVisibility(View.GONE);
		}

		String strs[] = null;
		if (shopItem.IMG_PATH != null) {
			strs = shopItem.IMG_PATH.split(",");
		}

		if (strs != null && strs.length > 0) {
			imageLoader.displayImage(URLs.IMAGE_URL + strs[0], goodlist_img);
		}

		goodlist_title.setText(shopItem.COMMODITY_NAME + "");
		goodlist_price.setText(MathUtil.priceForAppWithSign(shopItem.COMMODITY_PRICE));

		goodlist_count.setText("×" + count);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.DpToPx(mContext, 105));
		shop_add_listitem.addView(view, params);
	}
}
