package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.shoppingcar.DuteSelectActivity;
import com.huiyin.ui.user.MyOrderActivity.OrderBean.GoodItem;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyOrderActivity extends BaseActivity {

	public final static String TAG = "MyOrderActivity";
	private XListView good_xlistview, backgood_xlistview, yuyue_xlistview;
	// 请求返回的数据
	private OrderBean mOrderBean, backgoodBean, yuyueBean;
	// 适配器
	private OrderAdapter mOrderAdapter, backgoodAdapter, yuyueAdapter;
	private TextView left_rb;

	private int page = 1, backgood_page = 1, yuyue_page = 1;
	private TextView good_order_area, backgood_area, yuyue_order_area, middle_title_tv;

	public static final int first = 10, second = 11, third = 12;
	private int current_page = first;
	// 请求标识
	private static final String GOOD_FLAG = "0", BACKGOOD_FLAG = "1", YUYUE_FLAG = "2";
	// 订单状态：未付款1，订单审核2，待发货3，待收货4，交易完成5， 取消订单6
	public static final int UNPAY = 1, AUDIT = 2, WAIT_SEND = 3, WAIT_RECEIVE = 4, FINISH = 5, CANCEL = 6;

	// 退货状态：取消退款7、商家审核8、商家审核通过9、商家审核失败10、
	// 买家发货11、商家收货12、商家退款13、退货成功14

	public static final int BACK_CANCEL = 7, BACK_AUDIT = 8, AUDIT_SUCCESS = 9, AUDIT_UNSUCESS = 10, SEND_BACK = 11,
			BACK_RECEIVE = 12, BACK_MONEY = 13, BACK_FINISH = 14;

	// 换货状态：维权审核15,买家请退货16,维权拒绝17,买家已发货18,
	// 商品检测19,商家发货20,取消换货21,买家收货22,维权完成23

	public static final int CHANGE_AUDIT = 15, CHANGE_BACKOFF = 16, CHANGE_REFUSE = 17, CHANGE_SENDED = 18, CHANGE_DETECT = 19,
			CHANGE_REBACK = 20, CHANGE_CANCEL = 21, CHANGE_ACCEPT = 22, CHANGE_FINISH = 23;

	// 预约状态：预约申请、预约审核中、审核成功、审核失败、取消
	public static final int YY_APPLY = 24, YY_AUDIT = 25, YY_FAIL = 26, YY_SUCCESS = 27, YY_CANCEL = 28;

	public static final int orderObsolete = 30;

	private int pageSize = 10;
	String[] order_status = { "", "未付款", "订单审核", "待发货", "待收货", "交易完成", "取消订单", "取消退款", "商家审核", "商家审核通过", "商家审核失败", "买家发货中",
			"商家检测中", "商家退款中", "退货成功", "维权审核", "买家请退货", "维权拒绝", "买家已发货", "商品检测", "商家待发货", "取消换货", "买家收货", "维权完成", "预约申请", "预约审核中",
			"预约成功", "审核失败", "已取消预约", "买家收款", "订单作废" };

	// 以下三种状态属于商品列表
	public static String WAITPAY = "waitPay"; // 待支付
	public static String WAITRECEIVE = "waitReceive"; // 待收货
	public static String WAITCOMMENT = "waitComment"; // 待评价

	// 退换中属于退换货列表
	public static String BACKCHANGE = "backChange"; // 退换中
	// 三个列表都有这个搜索条件
	public static String SORT = "sort"; // 根据搜索时间和商品订单
	public static String MAIN = "main"; // 商品订单
	public static String CHANGE_BACK = "change_back"; // 退换货订单
	public static String YUYUE = "yuyue";

	// 标记、状态、开始时间、结束时间、搜索订单编号
	String remark, statu, startTime, endTime, searchOrder;
	// 是否可以刷新列表,状态切换时可以刷新列表
	private static boolean isFlush = false;
	LayoutInflater myInflater;
	// 当前的时间控件
	TextView currentTimeTV;
	// 当前页面处于何种搜索状态，默认处于无条件搜索下
	String current_state = MAIN;
	private int request_delete = 0x11011;
	private int request_cancel = 0x00120;
	private int request_logistic = 0x00120;

	private int request_cancel_change = 0x00130;
	private int request_receive = 0x00140;

	private int request_cancel_back = 0x00141;
	private int request_receive_money = 0x00141;
	// 用于删除
	private String orderId = "";
	// 1商品订单，2换货订单，3退货订单,4预约订单
	private int deleteFlag = 1;

	// 记录删除条目的位置，便于删除
	private int order_position;

	private static int chooseList = first;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_all_order);

		initView();

		if (current_state.equals(BACKCHANGE)) {
			// 乐虎界面点击退换货中
			initChangeBack();
		} else {
			// 默认
			getOrderList(page, GOOD_FLAG, true);
		}

	}

	/**
	 * 退换货中
	 */

	private void initChangeBack() {

		current_page = second;

		good_xlistview.setVisibility(View.GONE);
		backgood_xlistview.setVisibility(View.VISIBLE);
		yuyue_xlistview.setVisibility(View.GONE);

		good_order_area.setTextColor(getResources().getColor(R.color.black));
		backgood_area.setTextColor(getResources().getColor(R.color.index_red));   
		yuyue_order_area.setTextColor(getResources().getColor(R.color.black));

		getOrderList(backgood_page, BACKGOOD_FLAG, true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isFlush) {

			if (chooseList == second) {
				setListViewVisible(second);
			} else if (chooseList == third) {
				setListViewVisible(third);
			} else {
				if (mOrderAdapter == null) {
					setListViewVisible(first);
				} else {
					mOrderBean.orderList.clear();
					mOrderAdapter.notifyDataSetChanged();
					setListViewVisible(first);
				}
			}

		}

	}

	/**
	 * 设置isFlush可以刷新
	 */
	public static void setFlush(boolean flush) {
		isFlush = flush;
	}

	/**
	 * 清空数据
	 */
	public void clearData() {
		remark = "";
		statu = startTime = "";
		endTime = searchOrder = "";
	}

	public static void setBackList(int choice) {
		chooseList = choice;
	}

	private void setListViewVisible(int flag) {

		if (flag == first) {

			current_page = first;
			if (!current_state.equals(MAIN)) {
				isFlush = true;
				clearData();
			}
			good_xlistview.setVisibility(View.VISIBLE);
			backgood_xlistview.setVisibility(View.GONE);
			yuyue_xlistview.setVisibility(View.GONE);

			good_order_area.setTextColor(getResources().getColor(R.color.index_red));
			backgood_area.setTextColor(getResources().getColor(R.color.black));
			yuyue_order_area.setTextColor(getResources().getColor(R.color.black));

			if (isFlush) {
				clearData();
				page = 1;
				getOrderList(page, GOOD_FLAG, true);
			} else if (mOrderAdapter == null) {
				getOrderList(page, GOOD_FLAG, true);
			}

		} else if (flag == second) {

			current_page = second;
			if (!current_state.equals(CHANGE_BACK)) {
				isFlush = true;
				clearData();
			}

			good_xlistview.setVisibility(View.GONE);
			backgood_xlistview.setVisibility(View.VISIBLE);
			yuyue_xlistview.setVisibility(View.GONE);

			good_order_area.setTextColor(getResources().getColor(R.color.black));
			backgood_area.setTextColor(getResources().getColor(R.color.index_red));
			yuyue_order_area.setTextColor(getResources().getColor(R.color.black));

			if (isFlush) {
				backgood_page = 1;
				getOrderList(backgood_page, BACKGOOD_FLAG, true);
			} else if (backgoodAdapter == null) {
				getOrderList(backgood_page, BACKGOOD_FLAG, true);
			}

		} else {

			current_page = third;
			if (!current_state.equals(YUYUE)) {
				isFlush = true;
				clearData();
			}
			good_xlistview.setVisibility(View.GONE);
			backgood_xlistview.setVisibility(View.GONE);
			yuyue_xlistview.setVisibility(View.VISIBLE);

			good_order_area.setTextColor(getResources().getColor(R.color.black));
			backgood_area.setTextColor(getResources().getColor(R.color.black));
			yuyue_order_area.setTextColor(getResources().getColor(R.color.index_red));

			if (isFlush) {
				yuyue_page = 1;
				getOrderList(yuyue_page, YUYUE_FLAG, true);
			} else if (yuyueAdapter == null) {
				getOrderList(yuyue_page, YUYUE_FLAG, true);
			}

		}
	}

	/**
	 * 初始化
	 */
	private void initView() {

		isFlush = false;

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("全部订单");

		left_rb = (TextView) findViewById(R.id.left_ib);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		if (getIntent().hasExtra(TAG)) {

			current_state = getIntent().getStringExtra(TAG);
			if (current_state.equals(WAITPAY)) {
				// remark标记值由后台确定，未说明其含义
				remark = "1";
				// 订单状态
				statu = UNPAY + "";
			} else if (current_state.equals(WAITRECEIVE)) {
				remark = "1";
				statu = WAIT_RECEIVE + "";
			} else if (current_state.equals(WAITCOMMENT)) {
				remark = "2";
				statu = FINISH + "";
			} else if (current_state.equals(BACKCHANGE)) {
				remark = "3";
				// 这个两个数值是退换货中的两个订单状态值BACK_MONEY =8,SEND_BACK=9
				statu = "8,9";
			}
		}

		mOrderBean = new OrderBean();
		backgoodBean = new OrderBean();
		yuyueBean = new OrderBean();

		myInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		good_order_area = (TextView) findViewById(R.id.good_order_area);
		backgood_area = (TextView) findViewById(R.id.backgood_area);
		yuyue_order_area = (TextView) findViewById(R.id.yuyue_order_area);
		good_order_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setListViewVisible(first);
			}
		});

		backgood_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setListViewVisible(second);
			}
		});

		yuyue_order_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setListViewVisible(third);
			}
		});

		backgood_xlistview = (XListView) findViewById(R.id.backgood_xlistview);
		backgood_xlistview.setPullLoadEnable(true);
		backgood_xlistview.setPullRefreshEnable(true);
		backgood_xlistview.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				backgood_page = 1;
				getOrderList(backgood_page, BACKGOOD_FLAG, false);
			}

			@Override
			public void onLoadMore() {
				backgood_page = backgoodAdapter.getCount() / pageSize + 1;
				if (backgoodAdapter.getCount() % pageSize > 0)
					backgood_page++;
				getOrderList(backgood_page, BACKGOOD_FLAG, false);
			}
		});

		yuyue_xlistview = (XListView) findViewById(R.id.yuyue_xlistview);
		yuyue_xlistview.setPullLoadEnable(true);
		yuyue_xlistview.setPullRefreshEnable(true);
		yuyue_xlistview.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				yuyue_page = 1;
				getOrderList(yuyue_page, YUYUE_FLAG, false);
			}

			@Override
			public void onLoadMore() {
				yuyue_page = yuyueAdapter.getCount() / pageSize + 1;
				if (yuyueAdapter.getCount() % pageSize > 0)
					yuyue_page++;
				getOrderList(yuyue_page, YUYUE_FLAG, false);
			}
		});

		yuyue_xlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 < 2) {
					return;
				}
				Intent i = new Intent();
				i.setClass(MyOrderActivity.this, YuYueDetailActivity.class);
				i.putExtra("orderId", yuyueBean.orderList.get(arg2 - 2).ID);
				i.putExtra("statu", yuyueBean.orderList.get(arg2 - 2).STATUS);
				startActivity(i);
			}
		});

		// 长按删除
		// yuyue_xlistview.setOnItemLongClickListener(new
		// OnItemLongClickListener() {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// if(arg2<2){
		// return false;
		// }
		// if(yuyueBean.orderList.get(arg2-2).STATUS<YY_CANCEL){
		// Toast.makeText(MyOrderActivity.this, "只有取消状态订单能删除！",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		// order_position = arg2-2;
		// orderId = yuyueBean.orderList.get(arg2-2).ID;
		// Intent i = new
		// Intent(MyOrderActivity.this,CommonConfrimCancelDialog.class);
		// i.putExtra(CommonConfrimCancelDialog.TASK,
		// CommonConfrimCancelDialog.TASK_DELETE_ORDER);
		// startActivityForResult(i, request_delete);
		// return false;
		// }
		// });

		good_xlistview = (XListView) findViewById(R.id.good_xlistview);
		good_xlistview.setPullLoadEnable(true);
		good_xlistview.setPullRefreshEnable(true);
		good_xlistview.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getOrderList(page, GOOD_FLAG, false);
			}

			@Override
			public void onLoadMore() {
				page = mOrderAdapter.getCount() / pageSize + 1;
				if (mOrderAdapter.getCount() % pageSize > 0)
					page++;
				getOrderList(page, GOOD_FLAG, false);
			}
		});
		good_xlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 < 2) {
					return;
				}
				Intent i = new Intent();
				i.setClass(MyOrderActivity.this, MyOrderDetailActivity.class);
				i.putExtra("order_id", mOrderBean.orderList.get(arg2 - 2).ID);
				startActivity(i);
			}
		});

		// 长按删除
		// good_xlistview.setOnItemLongClickListener(new
		// OnItemLongClickListener() {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// if(arg2<2){
		// return false;
		// }
		// int status = mOrderBean.orderList.get(arg2-2).STATUS;
		// if(status!=UNPAY&&status!=FINISH&&status!=CANCEL){
		// Toast.makeText(MyOrderActivity.this, "该订单不能删除！",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		// order_position = arg2-2;
		// orderId = mOrderBean.orderList.get(arg2-2).ID;
		// Intent i = new
		// Intent(MyOrderActivity.this,CommonConfrimCancelDialog.class);
		// if(status==UNPAY){
		// i.putExtra(CommonConfrimCancelDialog.TASK,
		// CommonConfrimCancelDialog.TASK_CANCEL_ORDER);
		// startActivityForResult(i, request_cancel);
		// }else{
		// i.putExtra(CommonConfrimCancelDialog.TASK,
		// CommonConfrimCancelDialog.TASK_DELETE_ORDER);
		// startActivityForResult(i, request_delete);
		// }
		//
		// return false;
		// }
		// });

		// 增加搜索框
		addHeaderSearchBar();

	}

	/**
	 * 在列表头部增加搜索框
	 */
	private void addHeaderSearchBar() {

		for (int i = 0; i < 3; i++) {

			View view = myInflater.inflate(R.layout.search_order_top_layout, null);
			final EditText search_content_ed = (EditText) view.findViewById(R.id.search_content_ed);
			ImageView search_order_iv = (ImageView) view.findViewById(R.id.search_order_iv);
			final TextView search_time_tv = (TextView) view.findViewById(R.id.search_time_tv);
			search_order_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					searchOrder = search_content_ed.getText().toString();
					current_state = SORT;
					remark = statu = "";
					isFlush = true;
					if (current_page == first) {
						getOrderList(page, GOOD_FLAG, true);
					} else if (current_page == second) {
						getOrderList(page, BACKGOOD_FLAG, true);
					} else {
						getOrderList(page, YUYUE_FLAG, true);
					}
				}
			});

			search_time_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentTimeTV = search_time_tv;
					startActivityForResult(
							new Intent(MyOrderActivity.this, DuteSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
							DuteSelectActivity.REQUESET_CODE);
				}
			});
			if (i == 0) {
				good_xlistview.addHeaderView(view);
			} else if (i == 1) {
				backgood_xlistview.addHeaderView(view);
			} else {
				yuyue_xlistview.addHeaderView(view);
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DuteSelectActivity.REQUESET_CODE && resultCode == RESULT_OK) {
			if (data != null) {
				String time = data.getStringExtra("duteTime");
				String[] times = time.split("~");
				startTime = times[0];
				endTime = times[1];
				currentTimeTV.setText(time);
			}
		} else if (requestCode == request_delete && resultCode == RESULT_OK) {
			// 删除订单
			deleteOrder();
		} else if (requestCode == request_cancel && resultCode == RESULT_OK) {
			// 取消订单
			cancelOrder();
		} else if (requestCode == request_logistic && resultCode == RESULT_OK) {
			// 查看物流
			Intent i = new Intent(MyOrderActivity.this, CheckLogisticActivity.class);

			startActivity(i);

		} else if (requestCode == request_cancel_change && resultCode == RESULT_OK) {// 取消退货
			cancelChangeOrder();
		} else if (requestCode == request_receive && resultCode == RESULT_OK) {// 确认收货
			makeSureReceive();
		} else if (requestCode == request_cancel_back && resultCode == RESULT_OK) {// 取消退款
			cancelBackOrder();
		} else if (requestCode == request_receive_money && resultCode == RESULT_OK) {// 确认到账
			sureReceiveMoney();
		}

	}

	/***
	 * 取消退款
	 */
	private void cancelBackOrder() {

		RequstClient.cancelBackOrder(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "cancelBackOrder:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					refleshList();

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
	 * 确认到账
	 */
	private void sureReceiveMoney() {

		RequstClient.sureReceiveMoney(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "sureReceiveMoney:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					refleshList();

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
	 * 删除后刷新列表
	 */
	private void refleshList() {

		if (current_page == first) {
			setFlush(true);
			setListViewVisible(first);
		} else if (current_page == second) {
			setFlush(true);
			setListViewVisible(second);
		} else {
			setFlush(true);
			setListViewVisible(third);
		}
	}

	/***
	 * 取消订单
	 */
	private void cancelOrder() {

		RequstClient.cancelOrder(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "makeSureOrCancel:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					isFlush = true;
					setListViewVisible(first);

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
	 * 删除订单
	 */
	private void deleteOrder() {
		RequstClient.deleteOrder(orderId, deleteFlag, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "deleteOrder:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					refleshList();

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
	 * 取消换货订单
	 */
	private void cancelChangeOrder() {

		RequstClient.cancelChangeOrder(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "cancelChangeOrder:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					refleshList();

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
	 * 确认收货
	 */
	private void makeSureReceive() {

		RequstClient.receiveOrder(orderId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "receiveOrder:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					refleshList();

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
	 * 获取订单列表
	 * 
	 * @param c_page
	 * @param whichList
	 * @param initB
	 */

	private void getOrderList(int c_page, final String flag, final boolean initB) {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.getOrderList(AppContext.getInstance().getUserId(), c_page + "", flag, remark, statu, startTime, endTime, searchOrder,
				new CustomResponseHandler(this) {
					@Override
					public void onStart() {
						if (initB)
							super.onStart();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						good_xlistview.stopLoadMore();
						good_xlistview.stopRefresh();
						backgood_xlistview.stopLoadMore();
						backgood_xlistview.stopRefresh();
						yuyue_xlistview.stopLoadMore();
						yuyue_xlistview.stopRefresh();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "getOrderList:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}

							if (flag.equals(GOOD_FLAG)) {
								if (isFlush) {
									page = 1;
								}
								if (page == 1) {
									mOrderBean = new Gson().fromJson(content, OrderBean.class);
									mOrderAdapter = new OrderAdapter(mOrderBean, first);
									good_xlistview.setAdapter(mOrderAdapter);

									if (mOrderAdapter.getCount() < pageSize) {
										good_xlistview.hideFooter();
									} else {
										good_xlistview.showFooter();
									}

									if (isFlush) {
										current_state = MAIN;
										isFlush = false;
									}

								} else {
									OrderBean orderBean = new Gson().fromJson(content, OrderBean.class);
									if (orderBean.orderList != null) {
										if (orderBean.orderList.size() > 0) {
											mOrderBean.orderList.addAll(orderBean.orderList);
											mOrderAdapter.notifyDataSetChanged();
										} else {
											Toast.makeText(MyOrderActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
											good_xlistview.hideFooter();
										}

									}
									good_xlistview.stopLoadMore();
									good_xlistview.stopRefresh();

								}

							} else if (flag.equals(BACKGOOD_FLAG)) {

								if (isFlush) {
									page = 1;
								}

								if (backgood_page == 1) {

									backgoodBean = new Gson().fromJson(content, OrderBean.class);
									backgoodAdapter = new OrderAdapter(backgoodBean, second);
									backgood_xlistview.setAdapter(backgoodAdapter);

									if (backgoodAdapter.getCount() < pageSize) {
										backgood_xlistview.hideFooter();
									} else {
										backgood_xlistview.showFooter();
									}

									if (isFlush) {
										current_state = CHANGE_BACK;
										isFlush = false;
										chooseList = first;
									}

								} else {
									OrderBean orderBean = new Gson().fromJson(content, OrderBean.class);
									if (orderBean.orderList != null) {
										if (orderBean.orderList.size() > 0) {
											backgoodBean.orderList.addAll(orderBean.orderList);
											backgoodAdapter.notifyDataSetChanged();
										} else {
											Toast.makeText(MyOrderActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
											backgood_xlistview.hideFooter();
										}

									}
									backgood_xlistview.stopLoadMore();
									backgood_xlistview.stopRefresh();
								}
							} else if (flag.equals(YUYUE_FLAG)) {

								if (isFlush) {
									page = 1;
								}
								if (yuyue_page == 1) {
									yuyueBean = new Gson().fromJson(content, OrderBean.class);
									yuyueAdapter = new OrderAdapter(yuyueBean, third);
									yuyue_xlistview.setAdapter(yuyueAdapter);

									if (yuyueAdapter.getCount() < pageSize) {
										yuyue_xlistview.hideFooter();
									} else {
										yuyue_xlistview.showFooter();
									}

									if (isFlush) {
										current_state = YUYUE;
										isFlush = false;
										chooseList = first;
									}

								} else {
									OrderBean orderBean = new Gson().fromJson(content, OrderBean.class);
									if (orderBean.orderList != null) {
										if (orderBean.orderList.size() > 0) {
											yuyueBean.orderList.addAll(orderBean.orderList);
											yuyueAdapter.notifyDataSetChanged();
										} else {
											Toast.makeText(MyOrderActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
											yuyue_xlistview.hideFooter();
										}

									}
									yuyue_xlistview.stopLoadMore();
									yuyue_xlistview.stopRefresh();
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

	class OrderAdapter extends BaseAdapter {

		LayoutInflater inflater;
		OrderBean mOrderBean;
		int whichList;

		public OrderAdapter(OrderBean mOrderBean, int whichList) {
			this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mOrderBean = mOrderBean;
			this.whichList = whichList;
		}

		@Override
		public int getCount() {
			return mOrderBean.orderList == null ? 0 : mOrderBean.orderList.size();
		}

		@Override
		public Object getItem(int position) {
			return mOrderBean.orderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mOrderBean.orderList.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			final OrderBean.OrderItem orderItem = mOrderBean.orderList.get(position);
			convertView = inflater.inflate(R.layout.my_all_order_item, null);
			TextView orderlist_ordernum = (TextView) convertView.findViewById(R.id.orderlist_ordernum);
			TextView orderlist_orderstatu = (TextView) convertView.findViewById(R.id.orderlist_orderstatu);
			TextView orderlist_order_date = (TextView) convertView.findViewById(R.id.orderlist_order_date);
			TextView orderlist_check_logistics = (TextView) convertView.findViewById(R.id.orderlist_check_logistics);

			TextView order_num_title_tv = (TextView) convertView.findViewById(R.id.order_num_title_tv);
			TextView order_type = (TextView) convertView.findViewById(R.id.ordertype);
			TextView xiadan_id = (TextView) convertView.findViewById(R.id.xiadan_id);

			if (whichList == first) {
				if (orderItem.ORDER_TYPE.equals("1")) {
					order_type.setText("(换货)");
				} else if (orderItem.ORDER_TYPE.equals("2")) {
					order_type.setText("(预约)");
				} else if (orderItem.ORDER_TYPE.equals("3")) {
					order_type.setText("(退货)");
				}
			}

			if (whichList == second) {
				if (orderItem.STATUS > BACK_FINISH) {
					order_num_title_tv.setText("换货编号：");
				} else {
					order_num_title_tv.setText("退货编号：");
				}
				xiadan_id.setText("申请时间：");
			} else if (whichList == third) {
				orderlist_check_logistics.setVisibility(View.GONE);
				order_num_title_tv.setText("预约编号：");
				xiadan_id.setText("申请时间：");
			}

			orderlist_ordernum.setText(orderItem.ORDER_CODE);
			// 订单状态：未付款1，订单审核2，待发货3，待收货4，交易完成5， 取消订单6
			orderlist_orderstatu.setText(order_status[orderItem.STATUS]);
			// 待发货之后的订单状态才能查看物流
			if (orderItem.STATUS == WAIT_RECEIVE || orderItem.STATUS == FINISH) {
				orderlist_check_logistics.setText("查看物流");
				orderlist_check_logistics.setVisibility(View.VISIBLE);
			} else if (orderItem.STATUS == CHANGE_AUDIT) {
				orderlist_check_logistics.setText("取消订单");
				orderlist_check_logistics.setVisibility(View.VISIBLE);
			} else if (orderItem.STATUS == CHANGE_ACCEPT) {
				orderlist_check_logistics.setText("确认收货");
				orderlist_check_logistics.setVisibility(View.GONE);
			} else if (orderItem.STATUS == BACK_AUDIT) {
				orderlist_check_logistics.setText("取消退款");
				orderlist_check_logistics.setVisibility(View.VISIBLE);
			} else if (orderItem.STATUS == BACK_MONEY) {
				// orderlist_check_logistics.setText("确认到账");
				orderlist_check_logistics.setVisibility(View.GONE);
			} else if (orderItem.STATUS == UNPAY) {
				orderlist_check_logistics.setText("取消订单");
				orderlist_check_logistics.setVisibility(View.VISIBLE);
			} else if (orderItem.STATUS == CANCEL || orderItem.STATUS == orderObsolete || orderItem.STATUS == CHANGE_CANCEL
					|| orderItem.STATUS == YY_CANCEL||orderItem.STATUS ==BACK_CANCEL) {
				orderlist_check_logistics.setText("删除订单");
				orderlist_check_logistics.setVisibility(View.VISIBLE);
			} else {
				orderlist_check_logistics.setVisibility(View.GONE);
			}

			orderlist_order_date.setText(orderItem.CREATE_DATE);
			orderlist_check_logistics.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (whichList == second) {
						if (orderItem.STATUS == CHANGE_AUDIT) {// 取消订单
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_CHANGE);
							startActivityForResult(i, request_cancel_change);
						} else if (orderItem.STATUS == CHANGE_ACCEPT) {// 确认收货
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_RECEIVE);
							startActivityForResult(i, request_receive);
						} else if (orderItem.STATUS == BACK_AUDIT) {// 取消退款
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_BACK);
							startActivityForResult(i, request_cancel_back);
						} else if (orderItem.STATUS == BACK_MONEY) {// 确认到账
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_RECEIVE_MONEY);
							startActivityForResult(i, request_receive_money);
						} else if (orderItem.STATUS == CHANGE_CANCEL) {
							orderId = orderItem.ID;
							order_position = pos;
							deleteFlag = 2;
							Intent i = new Intent(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
							startActivityForResult(i, request_delete);
						}else if (orderItem.STATUS == BACK_CANCEL) {
							orderId = orderItem.ID;
							order_position = pos;
							deleteFlag = 3;
							Intent i = new Intent(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
							startActivityForResult(i, request_delete);
						}

					} else {

						if (orderItem.STATUS == UNPAY) {
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_ORDER);
							startActivityForResult(i, request_cancel);
						} else if (orderItem.STATUS == CANCEL || orderItem.STATUS == orderObsolete) {
							deleteFlag = 1;
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
							startActivityForResult(i, request_delete);
						} else if (orderItem.STATUS == YY_CANCEL) {
							deleteFlag = 4;
							orderId = orderItem.ID;
							order_position = pos;
							Intent i = new Intent(MyOrderActivity.this, CommonConfrimCancelDialog.class);
							i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
							startActivityForResult(i, request_delete);
						} else if (orderItem.STATUS == WAIT_RECEIVE || orderItem.STATUS == FINISH) {
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, CheckLogisticActivity.class);
							i.putExtra("order_id", orderItem.ID);
							startActivity(i);
						}

					}
				}

			});

			LinearLayout orderlist_add_gooditem = (LinearLayout) convertView.findViewById(R.id.orderlist_add_gooditem);
			for (int i = 0; i < orderItem.orderDetailList.size(); i++) {

				final GoodItem goodItem = orderItem.orderDetailList.get(i);
				View view = inflater.inflate(R.layout.order_goods_item, null);
				ImageView orderlist_img = (ImageView) view.findViewById(R.id.orderlist_img);
				TextView orderlist_good_title = (TextView) view.findViewById(R.id.orderlist_good_title);
				TextView orderlist_good_color = (TextView) view.findViewById(R.id.orderlist_good_color);
				TextView orderlist_good_price = (TextView) view.findViewById(R.id.orderlist_good_price);
				TextView orderlist_good_num = (TextView) view.findViewById(R.id.orderlist_good_num);

				ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + goodItem.COMMODITY_IMAGE_PATH, orderlist_img);

				orderlist_good_title.setText(goodItem.COMMODITY_NAME);
				orderlist_good_color.setText(goodItem.SPECVALUE);
				if(goodItem.PURCHASE_PRICE!=null && !goodItem.PURCHASE_PRICE.equals("")){       //如果折扣价格不为空就显示折扣价格
				    orderlist_good_price.setText(MathUtil.priceForAppWithSign(goodItem.PURCHASE_PRICE));
				}else{
					orderlist_good_price.setText(MathUtil.priceForAppWithSign(goodItem.COMMODITY_PRICE));
				}
				orderlist_good_num.setText("数量：" + goodItem.BUY_QTY);

				if (whichList == second) {
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent i = new Intent();
							i.setClass(MyOrderActivity.this, AfterSaleDetailActivity.class);
							i.putExtra("returnId", orderItem.ID);
							i.putExtra("commodityId", goodItem.ID);
							i.putExtra("goodName", goodItem.COMMODITY_NAME);
							i.putExtra("statu", orderItem.STATUS);
							startActivity(i);
						}
					});
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.DpToPx(
						mContext, 100));
				orderlist_add_gooditem.addView(view, params);
			}
			return convertView;
		}

	}

	public class OrderBean {

		public ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();

		public class OrderItem {
			public String ID;// 订单id
			public String ORDER_CODE; // 订单流水号
			public int STATUS; // 订单状态：1未付款 2 待发货3待收获 4交易完成
			public String number; // 订单数量
			public String TOTAL_PRICE;// 订单总价
			public String CREATE_DATE;// 下单时间
			public String ORDER_TYPE;// 0 普通订单 1 换货订单 2 预约订单 3退货订单
			public ArrayList<GoodItem> orderDetailList = new ArrayList<GoodItem>();
		}

		public class GoodItem {
			public String COMMODITY_ID;// 商品id
			public String COMMODITY_NAME;// 商品名称
			public String SPECVALUE; // 颜色、尺寸
			public String COMMODITY_PRICE;// 商品价格
			public String COMMODITY_IMAGE_PATH;// 商品图标
			public String BUY_QTY;// 数量
			public int STATUS;// 商品状态
			public String PURCHASE_PRICE = "";//折扣价格
			public String ID;
		}
	}

}