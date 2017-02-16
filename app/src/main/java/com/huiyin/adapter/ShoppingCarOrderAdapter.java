package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.OrderBean;
import com.huiyin.bean.OrderItem;
import com.huiyin.bean.ShopItem;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class ShoppingCarOrderAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private OrderBean mOrderBean;

	private Context mContext;

	private ImageLoader imageLoader;

	private OrderDataChangeListener listener;

	public ShoppingCarOrderAdapter(Context context, OrderBean mOrderBean) {
		mContext = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOrderBean = mOrderBean;
		imageLoader = ImageLoader.getInstance();
	}

	public OrderBean getOrderBean() {
		return this.mOrderBean;
	}

	public void setOnOrderDataChangeListener(OrderDataChangeListener listener) {
		this.listener = listener;
	}

	public interface OrderDataChangeListener {
		void onOrderDataChangeListener();
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
		convertView = inflater.inflate(R.layout.fragment_shopping_car_item, null);
		holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		holder.groupFlag = (TextView) convertView.findViewById(R.id.groupFlag);
		holder.groupTitle = (TextView) convertView.findViewById(R.id.groupTitle);
		holder.count = (EditText) convertView.findViewById(R.id.count);
		holder.btnAdd = (ImageView) convertView.findViewById(R.id.btnAdd);
		holder.btnMinus = (ImageView) convertView.findViewById(R.id.btnMinus);
		holder.totalPrice = (TextView) convertView.findViewById(R.id.totalPrice);
		holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);

		final OrderItem orderItem = mOrderBean.orderList.get(position);

		holder.checkbox.setChecked(orderItem.isCheck);

		if (orderItem.shopItem.GOODS_STOCKS < orderItem.good_qty) {
			orderItem.good_qty = orderItem.shopItem.GOODS_STOCKS;
			holder.count.setText(orderItem.shopItem.GOODS_STOCKS + "");
		} else {
			holder.count.setText(orderItem.good_qty + "");
		}

		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				orderItem.isCheck = isChecked;
				if (listener != null) {
					listener.onOrderDataChangeListener();
				}
			}
		});

		holder.contentLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra("goods_detail_id", orderItem.shopItem.COMMDOITY_ID);
				mContext.startActivity(intent);
			}
		});

		holder.count.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (arg0.length() < 1) {
					return;
				}
				int num = Integer.parseInt(arg0.toString());

				if (num > orderItem.shopItem.GOODS_STOCKS) {
					num = orderItem.shopItem.GOODS_STOCKS;
					holder.count.setText(orderItem.shopItem.GOODS_STOCKS + "");
				} else if (num == 0) {
					num = 1;
					holder.count.setText("1");
				}

				// 修改数量后的值
				orderItem.good_qty = num;

				holder.totalPrice.setText("小计：" + MathUtil.priceForAppWithSign(orderItem.total_price * orderItem.good_qty));

				if (listener != null) {
					listener.onOrderDataChangeListener();// 刷新总额数据
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		holder.btnMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int num = 0;
				if (holder.count.getText().length() < 1) {
					num = 1;
					holder.count.setText("1");
				} else {
					num = Integer.parseInt(holder.count.getText().toString());
					if (num > 1) {
						num--;
						holder.count.setText(num + "");
					}
				}
				// 修改数量
				orderItem.good_qty = num;

				holder.totalPrice.setText("小计：" + MathUtil.priceForAppWithSign(orderItem.total_price * orderItem.good_qty));

				if (listener != null) {
					listener.onOrderDataChangeListener();// 刷新总额数据
				}
			}
		});

		holder.btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int num = 0;
				if (orderItem.shopItem.QUOTA_FLAG != 3) {
					if (holder.count.getText().length() < 1) {
						num = 1;
						holder.count.setText("1");
					} else {

						int limit = orderItem.shopItem.GOODS_STOCKS;
						if (orderItem.shopItem.QUOTA_FLAG == 1) {
							int temp = MathUtil.stringToInt(orderItem.shopItem.QUOTA_QUANTITY);
							if (temp > 0)
								limit = temp < orderItem.shopItem.GOODS_STOCKS ? temp : orderItem.shopItem.GOODS_STOCKS;
						}
						num = Integer.parseInt(holder.count.getText().toString());

						if (num < limit) {
							num++;
							holder.count.setText(num + "");
						} else {
							holder.count.setText(limit + "");
						}
					}
				} else {
					holder.count.setText(num + "");
					Toast.makeText(mContext, "该商品只能购买" + orderItem.shopItem.QUOTA_NUMBER + "次", Toast.LENGTH_SHORT).show();
				}
				// 修改数量后的值
				orderItem.good_qty = num;

				holder.totalPrice.setText("小计：" + MathUtil.priceForAppWithSign(orderItem.total_price * orderItem.good_qty));

				if (listener != null) {
					listener.onOrderDataChangeListener();// 刷新总额数据
				}
			}
		});

		if (orderItem.goodList.size() > 1) {
			holder.groupFlag.setVisibility(View.VISIBLE);
			holder.groupTitle.setVisibility(View.VISIBLE);
			holder.groupTitle.setText(String.format(mContext.getString(R.string.buy_together), orderItem.goodList.size() + 1));
		} else {
			holder.groupFlag.setVisibility(View.GONE);
			holder.groupTitle.setVisibility(View.GONE);
		}

		if (orderItem.goodList.size() > 0) {
			initGoodView(orderItem.shopItem, holder.contentLayout, true);
			for (int i = 0; i < orderItem.goodList.size(); i++) {
				if (i == orderItem.goodList.size() - 1) {
					ShopItem shop_item = orderItem.goodList.get(i);
					initGoodView(shop_item, holder.contentLayout, false);
				} else {
					ShopItem shop_item = orderItem.goodList.get(i);
					initGoodView(shop_item, holder.contentLayout, true);
				}
			}
		} else {
			initGoodView(orderItem.shopItem, holder.contentLayout, false);
		}

		holder.totalPrice.setText("小计：" + MathUtil.priceForAppWithSign(orderItem.total_price * orderItem.good_qty));

		return convertView;

	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		CheckBox checkbox;
		TextView groupFlag;
		TextView groupTitle;

		EditText count;
		ImageView btnAdd;
		ImageView btnMinus;

		TextView totalPrice;

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
	private void initGoodView(ShopItem shopItem, LinearLayout shop_add_listitem, boolean isVisible) {
		View view = inflater.inflate(R.layout.fragment_shopping_car_item_item, null);
		ImageView goodlist_img = (ImageView) view.findViewById(R.id.goodlist_img);
		goodlist_img.setFocusable(false);
		TextView goodlist_title = (TextView) view.findViewById(R.id.goodlist_title);
		TextView goodlist_promotions = (TextView) view.findViewById(R.id.promotions_price);
		TextView goodlist_price = (TextView) view.findViewById(R.id.goodlist_price);
		goodlist_price.setFocusable(false);
		TextView goodlist_exist = (TextView) view.findViewById(R.id.goodlist_exist);
		View list_line = (View) view.findViewById(R.id.list_line);
		if (isVisible) {
			list_line.setVisibility(View.VISIBLE);
		} else {
			list_line.setVisibility(View.GONE);
		}

		String str[] = null;
		if (shopItem.IMG_PATH != null) {
			str = shopItem.IMG_PATH.split(",");
		}

		if (str != null && str.length > 0) {
			imageLoader.displayImage(URLs.IMAGE_URL + str[0], goodlist_img);
		}

		if (shopItem.QUOTA_FLAG != 2) {
			String text = "<font color=\"red\">[限购]</font>" + shopItem.COMMODITY_NAME;
			goodlist_title.setText(Html.fromHtml(text));
		} else {
			goodlist_title.setText(shopItem.COMMODITY_NAME);
		}

		SpannableString sp = null;
		String title = null;
		if (!shopItem.PROMOTIONS_TYPE.equals("3")) {
			sp = new SpannableString(MathUtil.priceForAppWithSign(shopItem.COMMODITY_PRICE));
			sp.setSpan(new StrikethroughSpan(), 0, String.valueOf(shopItem.COMMODITY_PRICE).length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			title = "促销价：<font color=\"red\">" + MathUtil.priceForAppWithSign(shopItem.PROMOTIONS_PRICE) + "</font>";
			goodlist_promotions.setText(Html.fromHtml(title));
			goodlist_price.setText(sp);
			goodlist_price.setTextColor(mContext.getResources().getColor(R.color.grey_text));
		} else {
			goodlist_price.setText(MathUtil.priceForAppWithSign(shopItem.COMMODITY_PRICE));
		}

		if (shopItem.IS_ADDED == 1) {
			if (shopItem.GOODS_STOCKS > 0) {
				goodlist_exist.setText("库存：" + shopItem.GOODS_STOCKS + "件");
			} else {
				goodlist_exist.setText("无货");
			}
		} else {
			goodlist_exist.setText("已下架");
		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.DpToPx(mContext,
				108));
		shop_add_listitem.addView(view, params);
	}

	public void deleteItem() {
		mOrderBean = new OrderBean();
		notifyDataSetChanged();
	}
}
