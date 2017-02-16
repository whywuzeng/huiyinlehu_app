package com.huiyin.ui.classic;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.widget.LinearLayout.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.custom.vg.list.CustomListView;
import com.custom.vg.list.OnItemClickListener;
import com.custom.vg.list.OnItemLongClickListener;
import com.google.gson.Gson;
import com.huiyin.wight.pulltorefresh.PullToRefreshGridView;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.AttributeListAdapter;
import com.huiyin.adapter.AttributeListNameAdapter;
import com.huiyin.adapter.GoodsDetailSpecValue2Adapter;
import com.huiyin.adapter.SearchHistoryAdapter;
import com.huiyin.adapter.SearchListAdapter;
import com.huiyin.adapter.SelecteAttributeAdapter;
import com.huiyin.adapter.SiteSearchAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.Attribute;
import com.huiyin.bean.CommodityTypeName;
import com.huiyin.bean.MayLikeCommodityBean;
import com.huiyin.bean.SearchHistroyBean;
import com.huiyin.bean.SearchHistroyListResult;
import com.huiyin.bean.CommodityTypeName.TypeList;
import com.huiyin.bean.CommodityTypeName.TypeName;
import com.huiyin.bean.GoodsDetailBeen.Values1;
import com.huiyin.bean.MayLikeCommodityBean.SearchBean;
import com.huiyin.db.SearchRecordDao;
import com.huiyin.ui.classic.CategorySearchActivity;
import com.huiyin.ui.housekeeper.Dict;
import com.huiyin.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("ResourceAsColor")
public class SortListSelection extends BaseActivity implements OnClickListener {
	private static final String TAG = "SortListSelection";
	private TextView tv_select_title;// 页面的标题
	private TextView tv_commit;// 提交
	private LinearLayout layout_back;// 返回
	private LinearLayout delete_all;// 清除全部
	private CustomListView clv_select;// 已选择的参数
	private ListView lv_attribute;// 属性列表
	private String attributeId;// 三级类目id
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();// 已选的属性列表数据
	private CommodityTypeName commodityTypeName;// 商品属性列表数据
	private SelecteAttributeAdapter adapter;
	private AttributeListAdapter adapterListView;

	private LinearLayout layout_mian;// 主布局
	private PopupWindow popWindow;
	private TextView tv_back;// 返回按钮
	private LinearLayout layout_price;// 隐藏的价格输入框

	// 价格栏的输入框和按钮
	private TextView tv_pop_title;// 属性名称
	private TextView btn_confirm;// 确认按钮
	private EditText et_startprice;// 起始价格
	private EditText et_endprice;// 最高价格

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_selection);
		attributeId = getIntent().getStringExtra("category_id");
		LogUtil.i(TAG, "属性筛选" + attributeId);
		findView();
		setListener();
		initView();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		layout_mian = (LinearLayout) findViewById(R.id.layout_mian);
		clv_select = (CustomListView) findViewById(R.id.clv_select);
		tv_select_title = (TextView) findViewById(R.id.tv_select_title);
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		tv_back = (TextView) findViewById(R.id.tv_back);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		delete_all = (LinearLayout) findViewById(R.id.delete_all);
		lv_attribute = (ListView) findViewById(R.id.lv_attribute);
	}

	/**
	 * 添加监听
	 */
	public void setListener() {
		tv_commit.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		delete_all.setOnClickListener(this);
		delete_all.setOnClickListener(this);
		delete_all.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		adapter = new SelecteAttributeAdapter(mContext, attributes);
		adapter.setOnItemDeleteListner(new SelecteAttributeAdapter.OnItemDeleteListner() {

			@Override
			public void onItemDelete(int position) {
				// TODO Auto-generated method stub
				Attribute attr = attributes.get(position);
				attributes.remove(position);
				String parentName = attr.partnerName;
				List<TypeList> lists = commodityTypeName.commodityTypeNameList;
				for (int i = 0; i < lists.size(); i++) {
					if (parentName.equals(lists.get(i).PROPERTY_NAME)) {// 匹配父属性
						ArrayList<TypeName> propertValueList = lists.get(i).propertValueList;
						for (int j = 0; j < propertValueList.size(); j++) {// 将该属性的所有子选项设置为未选中
							propertValueList.get(j).setSelected(false);
						}
					}
				}
				adapter.notifyDataSetChanged();// 刷新ui
			}
		});
		clv_select.setDividerHeight(16);
		clv_select.setDividerWidth(12);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 10, 20, 10);
		clv_select.setLayoutParams(params);
		clv_select.setAdapter(adapter);
		clv_select.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			}
		});
		clv_select.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// 不做任何处理
				return true;
			}
		});
	}

	/**
	 * 创建popWindow
	 * */
	private void createPopWindow(final int positionPop) {
		LayoutInflater inflact = LayoutInflater.from(SortListSelection.this);

		View view = inflact.inflate(R.layout.pop_attributes, null);
		ListView popList = (ListView) view.findViewById(R.id.luck_pop_listView);
		layout_price = (LinearLayout) view.findViewById(R.id.layout_price);
		btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
		tv_pop_title = (TextView) view.findViewById(R.id.tv_pop_title);
		final String partnerName = commodityTypeName.commodityTypeNameList.get(positionPop).PROPERTY_NAME;
		tv_pop_title.setText(partnerName);
		boolean confirm = partnerName.equals("价格范围");
		if (confirm) {// 父级名称为价格范围显示价格布局栏
			et_startprice = (EditText) view.findViewById(R.id.et_startprice);// 起始价格
			et_endprice = (EditText) view.findViewById(R.id.et_endprice);// 最高价格
			btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);// 确认按钮
			for (int j = 0; j < attributes.size(); j++) {
				String parentName1 = attributes.get(j).partnerName;
				if ("价格范围".equals(parentName1)) {
					et_startprice.setText(attributes.get(j).value.name.split("-")[0]);
					et_endprice.setText(attributes.get(j).value.name.split("-")[1]);
				}
			}
			btn_confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 取出输入框内的数，拼接成字属性符串
					// TODO Auto-generated method stub
					String startprice = et_startprice.getText().toString();
					String endprice = et_endprice.getText().toString();
					long startPrice = -1;
					long endPrice = -1;
					if ("".equals(startprice)) {
						Toast.makeText(mContext, "请输入起始价格", Toast.LENGTH_LONG).show();
					} else {
						startPrice = Integer.parseInt(startprice);
					}
					if ("".equals(endprice)) {
						Toast.makeText(mContext, "请输入最高价格", Toast.LENGTH_LONG).show();
					} else {
						startPrice = Long.parseLong(startprice);
						endPrice = Long.parseLong(endprice);
						if (startPrice < endPrice) {// 起始价格小于最高价格
							String price = startPrice + "-" + endPrice;
							TypeName type = commodityTypeName.new TypeName();
							type.NAME = price;
							addAttribute(positionPop, type);
						} else {
							Toast.makeText(mContext, "最高价格要高于起始价格", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		} else {
			layout_price.setVisibility(View.INVISIBLE);
			btn_confirm.setVisibility(View.INVISIBLE);
		}
		final AttributeListNameAdapter adapterName = new AttributeListNameAdapter(mContext,
				commodityTypeName.commodityTypeNameList.get(positionPop).propertValueList);

		adapterName.setOnMyItemClickListener(new AttributeListNameAdapter.OnMyItemClickListener() {
			@Override
			public void onItemclickListener(int position) {
				// TODO Auto-generated method stub
				// 将选中的item设置选中状态
				TypeName type = commodityTypeName.commodityTypeNameList.get(positionPop).propertValueList.get(position);
				List<TypeName> lists = commodityTypeName.commodityTypeNameList.get(positionPop).propertValueList;
				for (int i = 0; i < lists.size(); i++) {
					if (i != position) {
						lists.get(i).setSelected(false);
					} else {
						type.setSelected(true);
					}
				}
				adapterName.notifyDataSetChanged();
				addAttribute(positionPop, type);
			}
		});
		popList.setAdapter(adapterName);
		popWindow = new PopupWindow(view, layout_mian.getWidth() - 200, layout_mian.getWidth() - 200);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		popWindow.showAtLocation(layout_mian, Gravity.CENTER, 0, 0);
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		popList.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						popWindow = null;
					}
					break;
				}
				return true;
			}
		});

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}
				return true;
			}
		});
	}

	/**
	 * // 将选中的属性加入已选属性列表
	 * 
	 * @param positionPop
	 *            属性位置
	 * @param type
	 *            属性对象
	 */
	public void addAttribute(final int positionPop, TypeName type) {
		Attribute bean = new Attribute();
		String partnerId = commodityTypeName.commodityTypeNameList.get(positionPop).PROPERTY_ID;
		String partnerName = commodityTypeName.commodityTypeNameList.get(positionPop).PROPERTY_NAME;
		for (int i = 0; i < attributes.size(); i++) {
			String pName = attributes.get(i).partnerName;
			boolean flag = pName.equals(partnerName);
			if (flag) {// 已有的属性
				attributes.remove(i);
			}
		}
		bean.partnerId = partnerId;
		bean.partnerName = partnerName;
		bean.value = bean.new AttributeValue();
		bean.value.id = type.ID;
		bean.value.name = type.NAME;
		attributes.add(bean);// 将选中的属性加入已选中的列表list里面
		initView();
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
			popWindow = null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_commit:// 提交
			Intent intent = new Intent(mContext, CategorySearchActivity.class);
			String BRAN_ID = "";// 品牌
			StringBuffer properties = new StringBuffer();// 属性id,多个属性以逗号隔开
			String priceStart = "";// 价格起始值
			String priceEnd = "";// 价格结束值
			String propertyId = "";
			for (int i = 0; i < attributes.size(); i++) {
				String parentName = attributes.get(i).partnerName;
				if ("品牌".equals(parentName)) {// 品牌
					BRAN_ID = attributes.get(i).value.id;
				} else if ("价格范围".equals(parentName)) {// 起始价，最高价
					String[] prices = attributes.get(i).value.name.split("-");
					priceStart = prices[0];
					priceEnd = prices[1];
				} else {
					properties.append(attributes.get(i).value.id + ",");
					propertyId = properties.substring(0, properties.length() - 1);
				}
			}
			intent.putExtra(CategorySearchActivity.BUNDLE_BRAND_ID, BRAN_ID);
			intent.putExtra(CategorySearchActivity.BUNDLE_PROPERTY_ID, propertyId);
			intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_START, priceStart);
			intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_END, priceEnd);
			intent.putExtra(CategorySearchActivity.BUNDLE_MARK, "4");
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.layout_back:// 返回
			this.finish();
			break;
		case R.id.delete_all:// 清除历史搜索
			attributes.removeAll(attributes);
			List<TypeList> lists = commodityTypeName.commodityTypeNameList;
			for (int i = 0; i < lists.size(); i++) {
				ArrayList<TypeName> propertValueList = lists.get(i).propertValueList;
				for (int j = 0; j < propertValueList.size(); j++) {// 将该属性的所有子选项设置为未选中
					propertValueList.get(j).setSelected(false);
				}
			}
			adapter.notifyDataSetChanged();// 刷新ui
			break;
		}
	}

	/**
	 * 将光标置于文本之后
	 * 
	 * @param editText
	 */
	public void setEditTextCursorLocation(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spannable = (Spannable) text;
			Selection.setSelection(spannable, text.length());
		}
	}

	/**
	 * 初始化商品属性列表
	 */
	public void initData() {
		RequstClient.selecteAttributes(attributeId, new CustomResponseHandler(this) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, content);

				try {

					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {// 数据错误
						String errorMsg = obj.getString("msg");
						Toast.makeText(SortListSelection.this, errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					commodityTypeName = new Gson().fromJson(content, CommodityTypeName.class);
					adapterListView = new AttributeListAdapter(mContext, commodityTypeName);
					lv_attribute.setAdapter(adapterListView);
					adapterListView.setMyOnItemClickListener(new AttributeListAdapter.MyOnItemClickListener() {

						@Override
						public void onItemmClickListener(int position) {
							// TODO Auto-generated method stub
							createPopWindow(position);// 根据不同的商品属性展示popwindow
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
}
