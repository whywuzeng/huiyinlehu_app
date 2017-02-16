package com.huiyin.ui.classic;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.GoodsDetailGroupAdapter;
import com.huiyin.adapter.GoodsDetailGroupAdapter.OnSelectListener;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.GoodsDetailBeen;
import com.huiyin.bean.ShopItem;
import com.huiyin.bean.GoodsDetailBeen.CombineProduct;
import com.huiyin.bean.GoodsDetailBeen.GoodDetials;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsDetailGroupFragment extends Fragment {
	private View rootView;
	private Context mContext;

	// Content View Elements

	private TextView count;
	private TextView price_before;
	private TextView price_after;
	private TextView price_min;
	private TextView sure_shop_tianjia;
	private ListView mListView;

	// End Of Content View Elements
	private GoodsDetailBeen gdbbean;
	private GoodDetials goodsDetail;// 商品信息
	private ArrayList<CombineProduct> groupCommodityList;
	private int countGoods;
	private float sumPrice;

	private GoodsDetailGroupAdapter mAdapter;
	private OnSelectListener mOnSelectListener;

	private String userId, commodityId, goods_codes, num, type, purchase,
			commodityIds;

	public void setData(GoodsDetailBeen bean) {
		gdbbean = bean;
		groupCommodityList = bean.commodity.groupCommodityList;

		if (gdbbean.commodity.goodsList != null
				&& gdbbean.commodity.goodsList.size() > 0) {
			goodsDetail = gdbbean.commodity.goodsList.get(0);
		} else {
			goodsDetail = new GoodsDetailBeen().new GoodDetials();
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(
					R.layout.activity_goods_detail_groupmenu, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		// 初始化控件
		findView();

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mContext = getActivity();
		setListener();
		setView();
	}

	private void findView() {
		count = (TextView) rootView.findViewById(R.id.group_menu_count);
		price_before = (TextView) rootView
				.findViewById(R.id.group_menu_price_before);
		price_after = (TextView) rootView
				.findViewById(R.id.group_menu_price_after);
		price_min = (TextView) rootView.findViewById(R.id.group_menu_price_min);
		sure_shop_tianjia = (TextView) rootView
				.findViewById(R.id.sure_shop_tianjia);
		mListView = (ListView) rootView.findViewById(R.id.group_menu_listView);
	}

	private void setView() {
		countGoods = 1;
		sumPrice = Float.valueOf(gdbbean.commodity.commodity.PRICE);
		SetTheTextView();

		mAdapter = new GoodsDetailGroupAdapter(mContext);
		mAdapter.setMonSelectListener(mOnSelectListener);
		mAdapter.addItem(gdbbean.commodity.groupCommodityList);
		mListView.setAdapter(mAdapter);
	}

	private void setListener() {
		mOnSelectListener = new OnSelectListener() {

			@Override
			public void OnSelectChange(int positon, boolean isSelect) {
				if (isSelect) {
					countGoods++;
					sumPrice += groupCommodityList.get(positon).PRICE;
				} else {
					countGoods--;
					sumPrice -= groupCommodityList.get(positon).PRICE;
				}
				SetTheTextView();
			}
		};
		sure_shop_tianjia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addShoppingCar();
			}
		});
	}

	private void SetTheTextView() {
		String theMsg = mContext.getResources().getString(R.string.buy_sum);
		theMsg = String.format(theMsg, countGoods);
		count.setText(Html.fromHtml(theMsg));

		theMsg = mContext.getResources().getString(R.string.price_before);
		theMsg = String.format(theMsg,
				MathUtil.priceForAppWithOutSign(sumPrice));
		price_before.setText(Html.fromHtml(theMsg));

		float discount = Float.valueOf(gdbbean.commodity.DISCOUNT);
		theMsg = mContext.getResources().getString(R.string.price_after);
		theMsg = String.format(theMsg,
				MathUtil.priceForAppWithOutSign(sumPrice * discount / 100));
		price_after.setText(Html.fromHtml(theMsg));

		theMsg = mContext.getResources().getString(R.string.price_min);
		theMsg = String.format(
				theMsg,
				MathUtil.priceForAppWithOutSign(sumPrice * (100 - discount)
						/ 100));
		price_min.setText(Html.fromHtml(theMsg));

	}

	/**
	 * 加入购物车
	 * 
	 * */
	public void addShoppingCar() {
		userId = AppContext.getInstance().getUserId();
		// 参数规格为空
		if (userId == null || userId.equals("")) {
			// 未登录
			checkOutWithoutLogin();
		} else {
			commodityId = gdbbean.commodity.commodity.COMMODITY_ID;// 商品id
			goods_codes = goodsDetail.CODE;// 货品id
			num = "1"; // "12";//购买数量
			type = "1";// 购物车类型（1，app购买，2tv购买）
			commodityIds = "";// 组合商品id（多个用“，”隔开）
			purchase = "3";

			boolean isSelect = false;
			int[] checklist = mAdapter.getChecklist();

			for (int i = 0; i < checklist.length; i++) {
				if (checklist[i] == 1) {
					CombineProduct temp = groupCommodityList.get(i);
					commodityIds += temp.ID;
					commodityIds += ",";
					isSelect = true;
				}
			}
			if (!isSelect) {
				Toast.makeText(mContext, "您没有选择商品", Toast.LENGTH_SHORT).show();
				return;
			}
			commodityIds = commodityIds.substring(0, commodityIds.length() - 1);
			MyCustomResponseHandler handler = new MyCustomResponseHandler(
					mContext, true) {

				@Override
				public void onRefreshData(String content) {
					super.onRefreshData(content);
					Gson gson = new Gson();
					BaseBean data = gson.fromJson(content, BaseBean.class);
					if (data.type > 0) {
						((GoodsDetailActivity) getActivity()).toggle();
						Toast.makeText(mContext, "成功加入购物车", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext, "加入购物车失败", Toast.LENGTH_SHORT)
								.show();
					}
				}

			};
			RequstClient.goodsDetailsShoppingCar(userId, commodityId,
					goods_codes, num, type, purchase, commodityIds, null,
					handler);
		}
	}

	/**
	 * 未登录状况的结算、购物车
	 * 
	 * */
	private void checkOutWithoutLogin() {

		boolean isSelect = false;
		int[] checklist = mAdapter.getChecklist();
		// 加入购物车
		if (AppContext.shopCarLists == null) {
			AppContext.shopCarLists = new ArrayList<ShopItem>();
		}
		for (int i = 0; i < checklist.length; i++) {
			if (checklist[i] == 1) {
				ShopItem item = new ShopItem();
				CombineProduct temp = groupCommodityList.get(i);
				item.ID = MathUtil.stringToInt(temp.ID);
				item.FID = MathUtil.stringToInt(goodsDetail.COMMODITY_ID);
				item.COMMDOITY_ID = temp.ID;
				item.GOODS_CODE = temp.CODE;
				item.COMMODITY_NAME = temp.COMMODITY_NAME;
				item.IMG_PATH = temp.COMMODITY_IMAGE_PATH;
				item.COMMDOTIY_QTY = 1;
				item.COMMODITY_PRICE = temp.PRICE;
				item.COMMODITY_OLD_PRICE = temp.PRICE;
				item.GOODS_STOCKS = 1;
				item.IS_ADDED = 1;
				item.isGroup = true;

				AppContext.shopCarLists.add(item);
				isSelect = true;
			}
		}
		if (!isSelect) {
			Toast.makeText(mContext, "您没有选择商品", Toast.LENGTH_SHORT).show();
			return;
		}
		ShopItem bean = new ShopItem();
		bean.ID = MathUtil.stringToInt(goodsDetail.COMMODITY_ID);// 购物车id
		bean.FID = -1;// 购物车父类id
		bean.GOODS_CODE = goodsDetail.CODE;// 货品号
		bean.GOODS_STOCKS = MathUtil.stringToInt(goodsDetail.GOODS_STOCKS);// 货品库存
		bean.COMMDOTIY_QTY = 1;// 购买数量

		bean.COMMODITY_OLD_PRICE = MathUtil
				.stringToFloat(gdbbean.commodity.commodity.PRICE);// 打折(促销)商品原价
		bean.COMMDOITY_ID = goodsDetail.COMMODITY_ID;// 商品ID
		bean.IMG_PATH = gdbbean.commodity.commodity.COMMODITY_IMAGE_LIST;// 商品介绍主图

		bean.COMMODITY_PRICE = MathUtil.stringToFloat(goodsDetail.GOODS_PRICE);// 购买商品单价
		bean.COMMODITY_NAME = gdbbean.commodity.commodity.COMMODITY_NAME;// 商品名称
		bean.SPECVALUE = null;// 属性

		// 是否下架
		bean.IS_ADDED = MathUtil
				.stringToInt(gdbbean.commodity.commodity.IS_ADDED);
		bean.isGroup = true;
		bean.disCount = Float.valueOf(gdbbean.commodity.DISCOUNT);
		AppContext.shopCarLists.add(bean);
		// 加入购物车
		((GoodsDetailActivity) getActivity()).toggle();
		Toast.makeText(getActivity(), "成功加入购物车", Toast.LENGTH_SHORT).show();
	}
}
