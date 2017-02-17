package com.huiyin.ui.shoppingcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ShoppingCarOrderAdapter;
import com.huiyin.adapter.ShoppingCarOrderAdapter.OrderDataChangeListener;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.OrderBean;
import com.huiyin.bean.OrderItem;
import com.huiyin.bean.ShopItem;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.PageSelectedListener;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.MathUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 购物车
 * 
 * @author lixiaobin
 * */
public class ShoppingCarFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OrderDataChangeListener,
		PageSelectedListener {
//单选按钮RadioGroup、复选框CheckBox都有OnCheckedChangeListener事件
	public static final String TAG = "ShoppingCarFragment";

	private ImageView btnDelete;// 删除按钮

	private Button btnLogin;// 登陆按钮

	// 登录布局
	private LinearLayout shop_top_login_layout;

	private ViewSwitcher mViewSwitch;
	// 订单列表
	private ListView mListview;

	private OrderBean mOrderBean;//

	private ShoppingCarOrderAdapter mOrderAdapter;// 订单适配器

	private ShopBean mServerBean;// 网络购物车数据

	private String shopId = "";

	private Button btnCheckOut;// 结算

	private TextView shop_total_price_tv;// ,共计

	private CheckBox shop_check_all;// 全选框

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.shopping_layout_new, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		initData();

		initView(rootView);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadShopList();// 加载网络的或者本地的购物车数据
		if (shop_check_all != null) {
			shop_check_all.setTag(true);
			shop_check_all.setChecked(false);
		}
	}

	/**
	 * 初始化视图
	 * */
	private void initData() {
		mServerBean = new ShopBean();
		mOrderBean = new OrderBean();
	}

	/**
	 * 初始化
	 */
	private void initView(View rootView) {

		btnCheckOut = (Button) rootView.findViewById(R.id.btnCheckOut);// 结算按钮
		btnCheckOut.setText(String.format(getString(R.string.settlement), 0));// 初始0

		btnCheckOut.setOnClickListener(this);

		mViewSwitch = (ViewSwitcher) rootView.findViewById(R.id.shopping_viewSwitcher);

		mListview = (ListView) rootView.findViewById(R.id.m_listview);

		btnDelete = (ImageView) rootView.findViewById(R.id.ab_delete);
		btnDelete.setOnClickListener(this);

		// 登录条
		shop_top_login_layout = (LinearLayout) rootView.findViewById(R.id.shop_top_login_layout);
		btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		// 共计
		shop_total_price_tv = (TextView) rootView.findViewById(R.id.shop_total_price_tv);
		shop_total_price_tv.setText(MathUtil.priceForAppWithSign(0));

		// 全选
		shop_check_all = (CheckBox) rootView.findViewById(R.id.shop_check_all);
		shop_check_all.setOnCheckedChangeListener(this);

		//显示那个登录的一条Item
		if (AppContext.getInstance().getUserId() == null) {
			shop_top_login_layout.setVisibility(View.VISIBLE);
		} else {
			shop_top_login_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 生成shopId, 并检查shopId的值的可用性
	 */
	private boolean checkShopId() {
		shopId = "";
		for (int i = 0; i < mOrderBean.orderList.size(); i++) {
			if (mOrderBean.orderList.get(i).isCheck) {
				shopId += mOrderBean.orderList.get(i).shopItem.ID + ",";
			}
		}
		if (shopId.contains(",")) {
			shopId = shopId.substring(0, shopId.length() - 1);
			return true;
		} else if (shopId.equals("")) {
			Toast.makeText(getActivity(), "请选择商品！", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void noGoodsDialog() {
		SingleConfirmDialog dialog = new SingleConfirmDialog(getActivity());
		dialog.setCustomTitle("提交失败");
		dialog.setMessage("商品已下架");
		dialog.setCancelable(false);
		dialog.setConfirm("重新选择");
		dialog.setClickListener(new SingleConfirmDialog.DialogClickListener() {
			@Override
			public void onConfirmClickListener() {
			}
		});
		dialog.show();
	}

	/**
	 * 未登录下删除订单
	 */
	private void deleteOrderUnLogin() {
		String[] shop = shopId.split(",");
		List<ShopItem> lists = new ArrayList<ShopItem>();
		for (ShopItem item : AppContext.shopCarLists) {
			for (int i = 0; i < shop.length; i++) {
				if (item.ID == Integer.parseInt(shop[i]) || item.FID == Integer.parseInt(shop[i])) {
					lists.add(item);
				}
			}
		}
		AppContext.shopCarLists.removeAll(lists);
	}

	/**
	 * 登录下删除订单
	 */
	private void deleteOrderLogin() {
		String[] shop = shopId.split(",");
		List<ShopItem> lists = new ArrayList<ShopItem>();
		for (ShopItem item : mServerBean.shoppingCar) {
			for (int i = 0; i < shop.length; i++) {
				if (item.ID == Integer.parseInt(shop[i]) || item.FID == Integer.parseInt(shop[i])) {
					lists.add(item);
				}
			}
		}
		mServerBean.shoppingCar.removeAll(lists);
	}

	/***
	 * 登陆状态修改订单
	 * 
	 * */
	private void modifyOrderNum(String json) {
		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.doModifyOrder(json, new CustomResponseHandler(getActivity()) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					// 结算 - 修改数量成功后跳转
					Intent i = new Intent();
					i.putExtra(WriteOrderActivity.INTENT_KEY_SHOPID, shopId);
					i.setClass(getActivity(), WriteOrderActivity.class);
					startActivity(i);

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/***
	 * 删除服务器内保存的订单
	 * 
	 * */
	private void deleteOrder() {
		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.doDeleteOrder(shopId, new CustomResponseHandler(getActivity()) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					} else {
						// 删除成功、刷新界面
						deleteOrderLogin();

						rebuildData();
						mOrderAdapter = new ShoppingCarOrderAdapter(getActivity(), mOrderBean);
						mOrderAdapter.setOnOrderDataChangeListener(ShoppingCarFragment.this);
						mListview.setAdapter(mOrderAdapter);
						if (mListview.getAdapter().getCount() < 1) {
							//转换
							mViewSwitch.setDisplayedChild(0);
							btnDelete.setVisibility(View.INVISIBLE);
							((MainActivity) getActivity()).setTheShoppcar(0);
						} else {
							//转换
							mViewSwitch.setDisplayedChild(1);
							btnDelete.setVisibility(View.VISIBLE);

							int count = 0;
							for (OrderItem item : mOrderBean.orderList) {
								count += item.good_qty;
							}
							((MainActivity) getActivity()).setTheShoppcar(count);
						}
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 重新组装数据，ShopBean 转 OrderBean;
	 */
	private void rebuildData() {
		mOrderBean = new OrderBean();
		for (int i = 0; i < mServerBean.shoppingCar.size(); i++) {
			if (mServerBean.shoppingCar.get(i).FID < 0) {
				OrderItem order_item = new OrderItem();
				ShopItem shopItem = mServerBean.shoppingCar.get(i);// 服务器原始数据
				ArrayList<ShopItem> goodList = new ArrayList<ShopItem>();
				float totalPrice = shopItem.COMMODITY_PRICE;
				if (shopItem.PROMOTIONS_TYPE.equals("1") || shopItem.PROMOTIONS_TYPE.equals("2")) {
					totalPrice = Float.valueOf(shopItem.PROMOTIONS_PRICE);
				}
				if (shopItem.isGroup) {
					for (int j = 0; j < mServerBean.shoppingCar.size(); j++) {
						if (MathUtil.stringToInt(shopItem.COMMDOITY_ID) == mServerBean.shoppingCar.get(j).FID) {
							goodList.add(mServerBean.shoppingCar.get(j));
							totalPrice += mServerBean.shoppingCar.get(j).COMMODITY_PRICE;
						}
					}
					totalPrice = totalPrice * shopItem.disCount / 100;
				}
				order_item.goodList = goodList;
				order_item.total_price = totalPrice;
				order_item.shopItem = shopItem;
				order_item.good_qty = order_item.shopItem.COMMDOTIY_QTY;
				mOrderBean.orderList.add(order_item);
			}
		}
		// 每次更新要刷新底部结算栏
		updatePrice();
	}

	/**
	 * 刷新底部结算栏
	 */
	private void updatePrice() {
		if (mOrderBean == null) {
			return;
		}

		int count = 0;
		int dibucount = 0;
		float total_price = 0;
		for (OrderItem item : mOrderBean.orderList) {
			if (item.isCheck) {
				count += item.good_qty;
				total_price += item.total_price * item.good_qty;
			}
			dibucount += item.good_qty;
		}

		btnCheckOut.setText(String.format(getString(R.string.settlement), count));// 结算按钮
		shop_total_price_tv.setText(MathUtil.priceForAppWithSign(total_price));// 总价格

		((MainActivity) getActivity()).setTheShoppcar(dibucount);
	}

	/***
	 * 加载网络购物车列表  在界面初始化之后加载数据
	 */
	private void loadShopList() {
		if (null == AppContext.getInstance().getUserId() || AppContext.getInstance().getUserId().equals("")) {
			// 如果无用户登录
			shop_top_login_layout.setVisibility(View.VISIBLE);
			if (mOrderAdapter != null) {
				mOrderAdapter.deleteItem();  //那其他就不要显示
			}
			if (AppContext.shopCarLists != null) {
				mServerBean.shoppingCar = AppContext.shopCarLists;
				rebuildData();
				mOrderAdapter = new ShoppingCarOrderAdapter(getActivity(), mOrderBean);
				mOrderAdapter.setOnOrderDataChangeListener(this);
				//mListview  填充数据 这个 ShoppingCarorderAdapter
				mListview.setAdapter(mOrderAdapter);
				if (mListview.getAdapter().getCount() < 1) {
					mViewSwitch.setDisplayedChild(0);
					btnDelete.setVisibility(View.INVISIBLE);
					//设置数值
					((MainActivity) getActivity()).setTheShoppcar(0);
				} else {
					mViewSwitch.setDisplayedChild(1);
					btnDelete.setVisibility(View.VISIBLE);

					int count = 0;
					for (OrderItem item : mOrderBean.orderList) {
						count += item.good_qty;
					}
					((MainActivity) getActivity()).setTheShoppcar(count);
				}
			}
		} else {
			// // 如果有用户登录
			shop_top_login_layout.setVisibility(View.GONE);
			requestShopCarList();
		}
	}

	/**
	 * 获取购物车列表数据 1 app查询 / 2 tv查询
	 */
	private void requestShopCarList() {
		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.doShopListNew(AppContext.getInstance().getUserId(), "1", new CustomResponseHandler(getActivity()) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {

				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					mServerBean = new Gson().fromJson(content, ShopBean.class);
					if (mServerBean == null) {
						return;
					}

					rebuildData();
					mOrderAdapter = new ShoppingCarOrderAdapter(getActivity(), mOrderBean);
					mOrderAdapter.setOnOrderDataChangeListener(ShoppingCarFragment.this);
					//价格和数量
					mListview.setAdapter(mOrderAdapter);
					if (mListview.getAdapter().getCount() < 1) {
						mViewSwitch.setDisplayedChild(0);
						btnDelete.setVisibility(View.INVISIBLE);
						((MainActivity) getActivity()).setTheShoppcar(0);
					} else {
						mViewSwitch.setDisplayedChild(1);
						btnDelete.setVisibility(View.VISIBLE);
						int count = 0;
						for (OrderItem item : mOrderBean.orderList) {
							count += item.good_qty;
						}
						((MainActivity) getActivity()).setTheShoppcar(count);
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// RequstClient.doShopList(AppContext.userId, "1",
		// new CustomResponseHandler(getActivity()) {
		// @Override
		// public void onSuccess(int statusCode, Header[] headers,
		// String content) {
		//
		// super.onSuccess(statusCode, headers, content);
		// try {
		// JSONObject obj = new JSONObject(content);
		// if (!obj.getString("type").equals("1")) {
		// String errorMsg = obj.getString("msg");
		// Toast.makeText(getActivity(), errorMsg,
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		//
		// mServerBean = new Gson().fromJson(content,
		// ShopBean.class);
		// if (mServerBean == null) {
		// return;
		// }
		//
		// rebuildData();
		// mOrderAdapter = new ShoppingCarOrderAdapter(
		// getActivity(), mOrderBean);
		// mOrderAdapter
		// .setOnOrderDataChangeListener(ShoppingCarFragment.this);
		// mListview.setAdapter(mOrderAdapter);
		// if(mListview.getAdapter().getCount()<1) {
		// mViewSwitch.setDisplayedChild(0);
		// btnDelete.setVisibility(View.INVISIBLE);
		// } else {
		// mViewSwitch.setDisplayedChild(1);
		// btnDelete.setVisibility(View.VISIBLE);
		// }
		// } catch (JsonSyntaxException e) {
		// e.printStackTrace();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}

	public class ShopBean {
		public ArrayList<ShopItem> shoppingCar = new ArrayList<ShopItem>();
	}

	/**
	 * 登陆结算（如果修改了数量，将数量转json发送给服务器，修改数量，修改完成后跳转过订单）
	 * 
	 * */
	private void checkOut() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mOrderBean.orderList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (mOrderBean.orderList.get(i).good_qty != mOrderBean.orderList.get(i).shopItem.COMMDOTIY_QTY) {
				map.put("shoppingId", mOrderBean.orderList.get(i).shopItem.ID);
				map.put("num", mOrderBean.orderList.get(i).good_qty);
				list.add(map);
			}
		}
		String js = "[";
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			int id = (Integer) map.get("shoppingId");
			int num = (Integer) map.get("num");
			js += "{\"shoppingId\":" + id + ",\"num\":" + num + "}";
			if (i < list.size() - 1) {
				js += ",";
			}
		}

		js += "]";

		// 如果调用修改数量接口，修改成功后再跳到填写订单界面
		if (list.size() > 0) {
			// 如果修改了数量, 需要修改商品数量
			modifyOrderNum(js.toString());
		} else {
			// 结算
			Intent i = new Intent();
			i.putExtra(WriteOrderActivity.INTENT_KEY_SHOPID, shopId);
			i.setClass(getActivity(), WriteOrderActivity.class);
			startActivity(i);
		}
	}

	/**
	 * 未登陆结算
	 * 
	 * */
	private void checkOutWithOutLogin() {
		// 如果调用修改数量接口，修改成功后再跳到填写订单界面
		OrderBean bean = new OrderBean();
		ArrayList<OrderItem> temps = new ArrayList<OrderItem>();
		for (OrderItem item : mOrderBean.orderList) {
			if (item.isCheck) {
				temps.add(item);
			}
		}
		bean.orderList = temps;

		Intent i = new Intent();
		i.putExtra(WriteOrderActivity.INTENT_KEY_ORDER_BEAN, bean);
		i.setClass(getActivity(), WriteOrderActivity.class);
		startActivity(i);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {

		boolean isByCheckBoxSelf = (Boolean) shop_check_all.getTag();
		if (isByCheckBoxSelf) {
			for (OrderItem item : mOrderBean.orderList) {
				item.isCheck = isChecked;
			}

			if (mOrderAdapter != null) {
				mOrderAdapter.notifyDataSetChanged();
			}
		} else {
			shop_check_all.setTag(true);
		}

		// 是否全选
		if (!isChecked) {
			shop_check_all.setTextColor(getResources().getColor(R.color.grey1));
		} else {
			shop_check_all.setTextColor(getResources().getColor(R.color.black));
		}
		updatePrice();
	}

	@Override
	public void onOrderDataChangeListener() {
		boolean isAllCheck = true;
		for (OrderItem item : mOrderBean.orderList) {
			if (!item.isCheck) {
				isAllCheck = false;
			}
		}
		// Log.i("isAllCheck", String.valueOf(isAllCheck));
		// Log.i("isChecked", String.valueOf(shop_check_all.isChecked()));
		// Log.i("dfsfasdfasf", String.valueOf(isAllCheck ^
		// shop_check_all.isChecked()));
		if (isAllCheck ^ shop_check_all.isChecked()) {
			shop_check_all.setTag(false);
			shop_check_all.setChecked(isAllCheck);
		} else {
			updatePrice();
		}
		// Log.i("true true", String.valueOf(true ^ true));
		// Log.i("true false", String.valueOf(true ^ false));
		// Log.i("false true", String.valueOf(false ^ true));
		// Log.i("false false", String.valueOf(false ^ false));
	}

	@Override
	public void onClick(View view) {
		if (btnCheckOut == view) {
			if (!checkShopId()) {
				return;
			} else {
				for (int i = 0; i < mOrderBean.orderList.size(); i++) {
					// 商品已下架
					if (mOrderBean.orderList.get(i).isCheck) {
						if (mOrderBean.orderList.get(i).shopItem.IS_ADDED != 1) {
							noGoodsDialog();
							return;
						}
					}
				}

				if (AppContext.getInstance().getUserId() == null || AppContext.getInstance().getUserId().equals("")) {
					checkOutWithOutLogin();
				} else {
					checkOut();
				}
				shop_check_all.setTag(true);
				shop_check_all.setChecked(false);
			}
		} else if (view == btnDelete) {
			// 弹出对话框
			if (checkShopId()) {
				ConfirmDialog dialog = new ConfirmDialog(getActivity());
				dialog.setCustomTitle("删除商品");
				dialog.setMessage("您确定删除商品？");
				dialog.setConfirm("确定");
				dialog.setCancel("取消");
				dialog.setClickListener(new DialogClickListener() {
					@Override
					public void onConfirmClickListener() {
						if (AppContext.getInstance().getUserId() == null) {
							// 删除订单，
							deleteOrderUnLogin();
							loadShopList();
						} else {
							deleteOrder();
						}
					}

					@Override
					public void onCancelClickListener() {

					}
				});
				dialog.show();
			}
		} else if (view == btnLogin) {
			Intent i = new Intent();
			i.setClass(getActivity(), LoginActivity.class);
			startActivity(i);
		}
	}

	@Override
	public void pageSelected(int selectedId) {

		if (selectedId == MainActivity.SHOPPING_CAR_INDEX) {
			onResume();
		}
	}

}
