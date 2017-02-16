package com.huiyin.ui.classic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.vg.list.CustomListView;
import com.custom.vg.list.OnItemClickListener;
import com.custom.vg.list.OnItemLongClickListener;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.GoodsDetailSpecValue1Adapter;
import com.huiyin.adapter.GoodsDetailSpecValue2Adapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.GoodsDetailBeen;
import com.huiyin.bean.GoodsDetailBeen.GoodDetials;
import com.huiyin.bean.GoodsDetailBeen.SpecialValue1;
import com.huiyin.bean.GoodsDetailBeen.SpecialValues2;
import com.huiyin.bean.GoodsDetailBeen.Value2;
import com.huiyin.bean.GoodsDetailBeen.Values1;
import com.huiyin.bean.ShopItem;
import com.huiyin.ui.shoppingcar.WriteOrderActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsDetailRightFragment extends Fragment implements OnClickListener {

	private final String TAG = "GoodsDetailRightFragment";

	private ImageView imageView_jiahao, imageView_jianhao;
	public TextView sure_shop_tianjia, tv_yuhujiange_show_show, tv_shuxiang_zhi_1, tv_shuxiang_zhi_2, tv_shuxiang_zhi_value_1,
			tv_shuxiang_zhi_value_2, tv_name_shangping, tv_zxs, tv_kucun, xiangou_info;
	public EditText et_num;
	private String userId, commodityId, goods_codes, num, type, purchase, commodityIds;

	private View view;
	private GoodsDetailBeen gdbbean;

	private LinearLayout layout_type_1;// 显示规格参数布局1
	private TextView type_name_1;// 参数1
	private CustomListView type_list_1;// 参数1列表
	private LinearLayout layout_type_2;// 显示规格参数布局2
	private TextView type_name_2;// 参数2
	private CustomListView type_list_2;// 参数2列表

	private ImageView goodsImg;// 商品图片
	private SpecialValue1 specList;// 规格参对象
	private String spec_valid1 = "";// 规格参数id1
	private String spec_valid2 = "";// 规格参数id2
	private String specIds = "";// 规格ids

	private GoodDetials goodsDetail;// 商品信息
	private String specValue1;// 规格参数

	public void setData(GoodsDetailBeen bean) {
		gdbbean = bean;

		setView();

		setListener();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.activity_goods_detail_rightmenu, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		// 初始化控件
		findView();

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void findView() {
		tv_name_shangping = (TextView) view.findViewById(R.id.tv_name_shangping);// 商品名称
		xiangou_info = (TextView) view.findViewById(R.id.xiangou_info);
		imageView_jiahao = (ImageView) view.findViewById(R.id.imageView_jiahao);// 加号
		imageView_jianhao = (ImageView) view.findViewById(R.id.imageView_jianhao);// 减号
		et_num = (EditText) view.findViewById(R.id.et_num);// 商品数量
		sure_shop_tianjia = (TextView) view.findViewById(R.id.sure_shop_tianjia);// 加入购物车
		tv_zxs = (TextView) view.findViewById(R.id.tv_zxs);// 总销售
		tv_yuhujiange_show_show = (TextView) view.findViewById(R.id.tv_yuhujiange_show_show);// 商品价格
		tv_kucun = (TextView) view.findViewById(R.id.tv_kucun);// 商品库存
		goodsImg = (ImageView) view.findViewById(R.id.imageView_chanpin_ic);// 商品图片

		layout_type_1 = (LinearLayout) view.findViewById(R.id.layout_type_1);// 显示规格参数布局1
		type_name_1 = (TextView) view.findViewById(R.id.type_name_1);// 参数1
		type_list_1 = (CustomListView) view.findViewById(R.id.type_list_1);// 参数1列表
		layout_type_2 = (LinearLayout) view.findViewById(R.id.layout_type_2);// 显示规格参数布局2
		type_name_2 = (TextView) view.findViewById(R.id.type_name_2);// 参数2
		type_list_2 = (CustomListView) view.findViewById(R.id.type_list_2);// 参数2列表
	}

	public void setView() {
		tv_name_shangping.setText(gdbbean.commodity.commodity.COMMODITY_NAME);
		tv_zxs.setText("已售出 " + gdbbean.commodity.commodity.SALES_VOLUME + " 件");// 销量

		if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("1")) {
			tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PROMOTIONS_PRICE));
		} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("2")) {
			tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PROMOTIONS_PRICE));
		} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("3")) {
			tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PRICE));
		}
		if (gdbbean.commodity.commodity.QUOTA_FLAG != 2) {
			if (gdbbean.commodity.commodity.QUOTA_NUMBER > 0 && gdbbean.commodity.commodity.QUOTA_QUANTITY > 0) {
				xiangou_info.setText("数量(限购" + gdbbean.commodity.commodity.QUOTA_NUMBER + "次，每次"
						+ gdbbean.commodity.commodity.QUOTA_QUANTITY + "件):");
			}
			if (gdbbean.commodity.commodity.QUOTA_NUMBER > 0 && gdbbean.commodity.commodity.QUOTA_QUANTITY <= 0) {
				xiangou_info.setText("数量(限购" + gdbbean.commodity.commodity.QUOTA_NUMBER + "次):");
			}
			if (gdbbean.commodity.commodity.QUOTA_NUMBER <= 0 && gdbbean.commodity.commodity.QUOTA_QUANTITY > 0) {
				xiangou_info.setText("数量(单次最多购买" + gdbbean.commodity.commodity.QUOTA_QUANTITY + "件):");
			}
		}
		// 根据条件查询库存
		/**
		 * 拼接规格参数id
		 */
		if (1 == gdbbean.commodity.goodsList.size() && "-1".equals(gdbbean.commodity.goodsList.get(0).SPEC_VALUE_IDS)) {
			// 没有规格参数的情况
			// 没有参数规格的情况直接获取商品本身的库存
		} else {
			specList = gdbbean.commodity.specList;// 规格参数对象
			layout_type_1.setVisibility(View.VISIBLE);// 设置第一个参数布局可见
			type_name_1.setText(specList.NAME);
			specValue1 = specList.NAME;// 规格1名称

			if (specList.specValueList != null && specList.specValueList.size() > 0) {
				spec_valid1 = specList.specValueList.get(0).VALUE_ID;// 规格1 id
				GoodsDetailSpecValue1Adapter adapter1 = new GoodsDetailSpecValue1Adapter(getActivity(), specList.specValueList);
				type_list_1.setDividerHeight(16);
				type_list_1.setDividerWidth(12);
				type_list_1.setAdapter(adapter1);
				type_list_1.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						Values1 bean = gdbbean.commodity.specList.specValueList.get(position);
						spec_valid1 = bean.VALUE_ID;
						specValue1 += bean.VALUE_NAME;

						GoodsDetailSpecValue2Adapter adapter2 = new GoodsDetailSpecValue2Adapter(getActivity(),
								bean.twoSpecMap.twoSpecMap);
						type_list_2.setAdapter(adapter2);
						refrushUI();// 刷新界面
					}
				});

				type_list_1.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// 不做任何处理
						return true;
					}
				});

				SpecialValues2 twoSpecMap = specList.specValueList.get(0).twoSpecMap;
				if (null != twoSpecMap) {// 有第二级规格参数
					layout_type_2.setVisibility(View.VISIBLE);// 设置第二个参数布局可见
					type_name_2.setText(twoSpecMap.NAME);
					spec_valid2 = twoSpecMap.twoSpecMap.get(0).VALUE_ID;
					GoodsDetailSpecValue2Adapter adapter2 = new GoodsDetailSpecValue2Adapter(getActivity(), twoSpecMap.twoSpecMap);
					type_list_2.setAdapter(adapter2);
					type_list_2.setDividerHeight(16);
					type_list_2.setDividerWidth(12);
					type_list_2.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							Value2 bean = specList.specValueList.get(0).twoSpecMap.twoSpecMap.get(position);
							spec_valid2 = bean.VALUE_ID;
							specValue1 += bean.VALUE_NAME;

							refrushUI();// 刷新界面
						}
					});
					type_list_2.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							// 不做任何处理
							return true;
						}
					});
				}
			}

		}

		refrushUI();
		ImageLoader il = ImageLoader.getInstance();
		String str[] = null;
		if (gdbbean.commodity.commodity.COMMODITY_IMAGE_LIST != null) {
			str = gdbbean.commodity.commodity.COMMODITY_IMAGE_LIST.split(",");
		}

		if (str != null && str.length > 0) {
			il.displayImage(URLs.IMAGE_URL + str[0], goodsImg);// 加载商品图片
		}

	}

	public void setListener() {
		imageView_jianhao.setOnClickListener(this);
		imageView_jiahao.setOnClickListener(this);
		sure_shop_tianjia.setOnClickListener(this);
		et_num.addTextChangedListener(new TextWatcher() {// 给编辑框加监听事件

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
				String point = et_num.getText() + "";
				if (!"".equals(point)) {
					if (Integer.parseInt(goodsDetail.GOODS_STOCKS) < Integer.parseInt(point.trim())) {
						et_num.setText(goodsDetail.GOODS_STOCKS);
					} else if (1 > Integer.parseInt(point.trim())) {
						et_num.setText("1");
					}
				} else {
					et_num.setText("1");
				}
				setEditTextCursorLocation(et_num);
			}

			@Override
			public void beforeTextChanged(CharSequence text, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				setEditTextCursorLocation(et_num);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure_shop_tianjia:// 确定添加//或者购买
			// 处理限购的部分
			LogUtil.i("info", gdbbean.commodity.commodity.QUOTA_FLAG+"");
			if (gdbbean.commodity.commodity.QUOTA_FLAG == 3) {
				Toast.makeText(getActivity(), "该商品只能购买" + gdbbean.commodity.commodity.QUOTA_NUMBER + "次", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (gdbbean.commodity.commodity.QUOTA_FLAG == 1) {
				if (gdbbean.commodity.commodity.QUOTA_QUANTITY > 0
						&& Integer.parseInt(et_num.getText().toString().trim()) > gdbbean.commodity.commodity.QUOTA_QUANTITY) {
					Toast.makeText(getActivity(), "该商品每次只能购买" + gdbbean.commodity.commodity.QUOTA_QUANTITY + "件",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

			userId = AppContext.getInstance().getUserId();// = "9";// //用户名id
			commodityId = gdbbean.commodity.commodity.COMMODITY_ID;// 商品id
			// commodityId ="262";// 商品id
			goods_codes = goodsDetail.CODE;// 货品id
			num = String.valueOf(Integer.parseInt(et_num.getText().toString().trim())); // "12";//购买数量
			type = "1";// 购物车类型（1，app购买，2tv购买）
			commodityIds = "";// 组合商品id（多个用“，”隔开）

			if (GoodsDetailActivity.showMenuType == 1) {
				purchase = "2";
			} else if (GoodsDetailActivity.showMenuType == 0) {
				purchase = "1";
			}
			addShoppingCar();
			break;
		case R.id.imageView_jianhao:
			// 减号按钮
			String num1 = et_num.getText() + "";
			if (!"".equals(num1)) {
				if (1 > Integer.parseInt(num1.trim())) {
					et_num.setText("1");
					return;
				}
				int i = Integer.parseInt(num1) - 1;
				et_num.setText(i + "");
			}
			break;
		case R.id.imageView_jiahao:// 加号按钮
			String num2 = et_num.getText() + "";
			if (!"".equals(num2)) {
				if (Integer.parseInt(goodsDetail.GOODS_STOCKS) < Integer.parseInt(num2.trim())) {
					et_num.setText(goodsDetail.GOODS_STOCKS);
					return;
				}
				int j = Integer.parseInt(num2) + 1;
				et_num.setText(j + "");
			}
			break;
		}
	}

	// 刷新界面数据
	public void refrushUI() {
		if ("".equals(spec_valid1)) {// 没有规格参数
			specIds = "";

			if (gdbbean.commodity.goodsList != null && gdbbean.commodity.goodsList.size() > 0) {
				goodsDetail = gdbbean.commodity.goodsList.get(0);
			} else {
				goodsDetail = new GoodsDetailBeen().new GoodDetials();
			}
		} else if ("".equals(spec_valid2)) {// 只有一个规格参数
			specIds = spec_valid1;
			goodsDetail = changGuigeTo(specIds);
		} else {
			specIds = spec_valid1 + "," + spec_valid2;
			goodsDetail = changGuigeTo(specIds);
		}

		if (MathUtil.stringToInt(gdbbean.commodity.commodity.IS_ADDED) == 1) {
			if (goodsDetail.GOODS_STOCKS == null
					|| (goodsDetail.GOODS_STOCKS != null && Integer.valueOf(goodsDetail.GOODS_STOCKS) < 0)) {
				goodsDetail.GOODS_STOCKS = "0";
				tv_kucun.setText("库存：暂无库存");// 库存
			} else {
				tv_kucun.setText("库存：" + goodsDetail.GOODS_STOCKS + "件");// 库存
			}

			if (goodsDetail.GOODS_PRICE != null) {

				if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("1")) {
					tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PROMOTIONS_PRICE));
				} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("2")) {
					tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PROMOTIONS_PRICE));
				} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("3")) {
					tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.commodity.commodity.PRICE));
				}// 商品价格

			} else {
				tv_yuhujiange_show_show.setText("暂无报价");// 商品价格
			}

			if (Integer.valueOf(goodsDetail.GOODS_STOCKS) <= 0) {
				et_num.setText("0");
				sure_shop_tianjia.setEnabled(false);
				sure_shop_tianjia.setBackgroundResource(R.drawable.common_btn_gray2_selector);
				imageView_jianhao.setEnabled(false);
				et_num.setEnabled(false);
				imageView_jiahao.setEnabled(false);
			}

		} else {
			tv_yuhujiange_show_show.setText("已下架");// 商品价格

			sure_shop_tianjia.setEnabled(false);
			sure_shop_tianjia.setBackgroundResource(R.drawable.common_btn_gray2_selector);
			imageView_jianhao.setEnabled(false);
			et_num.setEnabled(false);
			imageView_jiahao.setEnabled(false);
		}
	}

	// 将EditText的光标定位到字符的最后面
	public void setEditTextCursorLocation(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	/**
	 * 加入购物车
	 * 
	 * */
	public void addShoppingCar() {
		// 参数规格为空
		if (userId == null || userId.equals("")) {
			// 未登录
			checkOutWithoutLogin();
		} else {
			RequstClient.goodsDetailsShoppingCar(userId, commodityId, goods_codes, num, type, purchase, commodityIds, specValue1,
					new CustomResponseHandler(getActivity()) {
						@Override
						public void onSuccess(int statusCode, Header[] headers, String content) {
							UIHelper.cloesLoadDialog();

							analysisAddShopResult(content);
						}
					});
		}
	}

	/***
	 * 已经登录加入购物车的情况处理
	 * */
	private void analysisAddShopResult(String content) {
		LogUtil.i("rightFragment", "加入购物车结果" + content);
		try {
			JSONObject obj = new JSONObject(content);
			String result = obj.getString("type");
			if ("1".equals(result)) {
				String trueMsg = obj.getString("msg");
				if (GoodsDetailActivity.showMenuType == 1) {
					// 立即结算直接进来的
					String shopId = obj.getString("shoppingId");
					Intent intent = new Intent(getActivity(), WriteOrderActivity.class);
					intent.putExtra(WriteOrderActivity.INTENT_KEY_SHOPID, shopId);
					startActivity(intent);
					// ((GoodsDetailActivity) getActivity()).finish();
				} else {
					// 加入购物车
					((GoodsDetailActivity) getActivity()).toggle();
					Toast.makeText(getActivity(), "成功加入购物车", Toast.LENGTH_SHORT).show();
				}
				return;
			} else {
				String errorMsg = obj.getString("msg");
				Toast.makeText(getActivity(), "" + errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 未登录状况的结算、购物车
	 * 
	 * */
	@SuppressWarnings("unchecked")
	private void checkOutWithoutLogin() {
		ShopItem bean = new ShopItem();
		bean.ID = MathUtil.stringToInt(goodsDetail.ID);// 购物车id
		bean.FID = -1;// 购物车父类id
		bean.GOODS_CODE = goods_codes;// 货品号
		bean.GOODS_STOCKS = MathUtil.stringToInt(goodsDetail.GOODS_STOCKS);// 货品库存
		bean.COMMDOTIY_QTY = MathUtil.stringToInt(num);// 购买数量

		bean.COMMODITY_OLD_PRICE = MathUtil.stringToFloat(gdbbean.commodity.commodity.PRICE);// 打折(促销)商品原价
		bean.COMMDOITY_ID = commodityId;// 商品ID

		bean.IMG_PATH = gdbbean.commodity.commodity.COMMODITY_IMAGE_LIST;// 商品介绍主图

		// bean.COMMODITY_PRICE =
		// MathUtil.stringToFloat(goodsDetail.GOODS_PRICE);// 购买商品单价

		if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("1")) {
			bean.COMMODITY_PRICE = MathUtil.stringToFloat(gdbbean.commodity.commodity.PROMOTIONS_PRICE);
		} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("2")) {
			bean.COMMODITY_PRICE = MathUtil.stringToFloat(gdbbean.commodity.commodity.PROMOTIONS_PRICE);
		} else if (gdbbean.commodity.commodity.PROMOTIONS_TYPE.equals("3")) {
			bean.COMMODITY_PRICE = MathUtil.stringToFloat(goodsDetail.GOODS_PRICE);
		}
		bean.COMMODITY_NAME = gdbbean.commodity.commodity.COMMODITY_NAME;// 商品名称
		bean.SPECVALUE = specValue1;// 属性

		// 是否下架
		bean.IS_ADDED = MathUtil.stringToInt(gdbbean.commodity.commodity.IS_ADDED);

		if (GoodsDetailActivity.showMenuType == 1) {
			// 立即结算直接进来的
			// String value = creatJson();

			List<ShopItem> listItems = new ArrayList<ShopItem>();
			listItems.add(bean);

			Intent intent = new Intent(getActivity(), WriteOrderActivity.class);
			intent.putParcelableArrayListExtra(WriteOrderActivity.INTENT_KEY_GOODS_LIST,
					(ArrayList<? extends Parcelable>) listItems);
			startActivity(intent);
			// ((GoodsDetailActivity) getActivity()).finish();

		} else {
			// 加入购物车
			if (AppContext.shopCarLists == null) {
				AppContext.shopCarLists = new ArrayList<ShopItem>();
			}
			// 组合购买完全没有做，才可以这样，做了之后要修改，不然会出隐性Bug 已添加isGroup做判断
			boolean hasit = false;
			for (int i = 0; i < AppContext.shopCarLists.size(); i++) {
				ShopItem temp = AppContext.shopCarLists.get(i);
				if (!temp.isGroup && temp.COMMDOITY_ID.equals(bean.COMMDOITY_ID)) {
					temp.COMMDOTIY_QTY += bean.COMMDOTIY_QTY;
					hasit = true;
				}
			}
			if (!hasit)
				AppContext.shopCarLists.add(bean);

			// 加入购物车
			((GoodsDetailActivity) getActivity()).toggle();
			Toast.makeText(getActivity(), "成功加入购物车", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据ids查找GoodDetials对象
	 * 
	 * @param spec_vals
	 * @return
	 */
	public GoodDetials changGuigeTo(String spec_vals) {
		GoodDetials goodDetials = new GoodsDetailBeen().new GoodDetials();
		for (int i = 0; i < gdbbean.commodity.goodsList.size(); i++) {
			String dis = gdbbean.commodity.goodsList.get(i).SPEC_VALUE_IDS;
			if (spec_vals.equals(dis)) {
				goodDetials = gdbbean.commodity.goodsList.get(i);
			}
		}
		return goodDetials;
	}

}
