package com.huiyin.ui.shoppingcar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.AppContext.LocationCallBack;
import com.huiyin.adapter.NearShopListAdapter;
import com.huiyin.adapter.NearShopListSendWayAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.NearShopBean;
import com.huiyin.db.SQLOpearteImpl;
import com.huiyin.ui.shoppingcar.DatePickPopMenu.ConfirmClick;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.DateTimePicker;
import com.huiyin.wight.DateTimePicker.OnDateTimeSetListener;

public class PayWayActivity extends BaseActivity {

	private final static String TAG = "PayWayActivity";
	TextView left_rb, middle_title_tv, right_rb;
	Spinner spin_pay_way, spin_send_way;
	EditText spin_member_num;
	TextView spin_send_time;
	TextView spin_send_way_new;
	PayWayBean mPayWayBean;
	Button payway_btn;

	List<String> listPay = new ArrayList<String>();
	ArrayList<String> listSend = new ArrayList<String>();

	String payWayId, sendWayId, sendtime, member_num, pay, send;
	int shopId;
	private Context mContext;
	// 定位相关
	private LocationClient mLocationClient;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;
	private int cityId;
	private NearShopBean bean;
	private boolean isHH = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payway_layout);
		mContext = this;
		Intent i = getIntent();
		sendWayId = i.getStringExtra("sendWayId");
		shopId = i.getIntExtra("shopId", -1);
		send = i.getStringExtra("send");
		initView();
		initData();

		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;
		if (lng == 0.0 || lat == 0.0 || StringUtils.isBlank(cityName)) {
			StartLoacation();
		} else {
			SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
			cityId = temp.checkIdByName(cityName);
			temp.CloseDB();
		}
	}

	private void initView() {

		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		right_rb.setVisibility(View.GONE);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("支付及配送方式");

		spin_pay_way = (Spinner) findViewById(R.id.spin_pay_way);
		spin_send_way = (Spinner) findViewById(R.id.spin_send_way);
		spin_send_way_new = (TextView) findViewById(R.id.spin_send_way_new);
		spin_send_way_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showListDialog(1);
			}
		});

		spin_send_time = (TextView) findViewById(R.id.spin_send_time);
		spin_send_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickPopMenu menu = new DatePickPopMenu(mContext);
				menu.setmClickListener(new ConfirmClick() {

					@Override
					public void OnConfirmClick(String temp) {
						spin_send_time.setText(temp);
					}
				});
				menu.showAtLocation(spin_send_time, Gravity.BOTTOM, 0, 0);
				// OnDateTimeSetListener mListener = new OnDateTimeSetListener()
				// {
				//
				// @Override
				// public void onDateTimeSet(Time start, Time end) {
				// spin_send_time.setText(start.year + "-" + start.month + "-" +
				// start.monthDay + " " + start.hour + ":"
				// + start.minute + "~" + end.year + "-" + end.month + "-" +
				// end.monthDay + " " + end.hour + ":"
				// + end.minute);
				// }
				// };
				// Time nowtime = new Time();
				// nowtime.setToNow();
				// DateTimePicker dateTimePicker = new DateTimePicker(mContext,
				// mListener, nowtime.year, nowtime.month,
				// nowtime.monthDay, nowtime.hour, nowtime.minute, true);
				// dateTimePicker.show();
				// startActivityForResult(
				// new Intent(PayWayActivity.this,
				// DuteSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
				// DuteSelectActivity.REQUESET_CODE);
			}
		});
		spin_member_num = (EditText) findViewById(R.id.spin_member_num);

		payway_btn = (Button) findViewById(R.id.payway_btn);
		payway_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendtime = spin_send_time.getText().toString();
				if (sendtime.equals("")) {
					Toast.makeText(PayWayActivity.this, "送货时间不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				Intent i = new Intent(PayWayActivity.this, WriteOrderActivity.class);
				i.putExtra("payWayId", payWayId);
				i.putExtra("pay", pay);
				i.putExtra("sendWayId", sendWayId);
				i.putExtra("send", send);
				i.putExtra("sendtime", sendtime);
				i.putExtra("shopId", shopId);
				setResult(RESULT_OK, i);
				finish();
			}
		});

	}

	private void initData() {

		RequstClient.doPayWay(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doPayWay:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					mPayWayBean = new Gson().fromJson(content, PayWayBean.class);
					refleshUI();

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

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (requestCode == DuteSelectActivity.REQUESET_CODE && resultCode ==
	// RESULT_OK) {
	// if (data != null) {
	// String time = data.getStringExtra("duteTime");
	// spin_send_time.setText(time);
	// }
	// }
	// }

	private void refleshUI() {

		for (int i = 0; i < mPayWayBean.payMethodList.size(); i++) {
			listPay.add(mPayWayBean.payMethodList.get(i).METHOD_NAME);
		}
		for (int i = 0; i < mPayWayBean.distributionManagementList.size(); i++) {
			listSend.add(mPayWayBean.distributionManagementList.get(i).DISMANNAME);
		}
		listSend.add("上门自提");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPay);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_pay_way.setAdapter(adapter);
		spin_pay_way.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				payWayId = mPayWayBean.payMethodList.get(arg2).PAYMETHODID;
				pay = listPay.get(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ArrayAdapter<String> adapter_send = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSend);
		adapter_send.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_send_way.setAdapter(adapter_send);
		spin_send_way.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (isHH) {
					isHH = false;
					return;
				}
				if (arg2 != listSend.size() - 1) {
					send = listSend.get(arg2);
					sendWayId = mPayWayBean.distributionManagementList.get(arg2).DISMANID;
				} else {
					getNearShop();
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		payWayId = mPayWayBean.payMethodList.get(0).PAYMETHODID;
		if (sendWayId.equals("-1")) {
			spin_send_way.setSelection(adapter_send.getCount() - 1);
			isHH = true;
		} else {
			send = mPayWayBean.distributionManagementList.get(0).DISMANNAME;
			sendWayId = mPayWayBean.distributionManagementList.get(0).DISMANID;
		}
		spin_send_way_new.setText(send);
	}

	public class PayWayBean {

		ArrayList<PayItem> payMethodList = new ArrayList<PayItem>();
		ArrayList<SendItem> distributionManagementList = new ArrayList<SendItem>();

		public class PayItem {
			String PAYMETHODID;
			String METHOD_NAME;
		}

		public class SendItem {
			String DISMANNAME;
			String DISMANID;
		}

	}

	private void StartLoacation() {
		AppContext.getInstance().initLocationClient();
		if (mLocationClient == null)
			mLocationClient = AppContext.getInstance().mLocationClient;
		if (mLocationClient.isStarted()) {
			return;
		}
		mLocationClient.start();
		AppContext.getInstance().setLocationCallBack(new LocationCallBack() {

			@Override
			public void getPoistion(BDLocation location) {
				Toast.makeText(mContext, "定位成功", Toast.LENGTH_SHORT).show();
				lng = location.getLongitude();
				lat = location.getLatitude();
				cityName = location.getCity();
				if (lng != 0.0 && lat != 0.0) {
					mLocationClient.stop();
					SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
					cityId = temp.checkIdByName(cityName);
					temp.CloseDB();
				}
			}
		});
	}

	private void getNearShop() {
		if (bean != null && bean.type > 0) {
			showListDialog();
			return;
		}
		if (cityId == -1)
			return;
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				bean = NearShopBean.explainJson(content, mContext);
				if (bean.type > 0) {
					showListDialog();
				}
			}
		};
		RequstClient.appNearShop(cityId, lng, lat, handler);
	}

	private AlertDialog mDialog;

	private void showListDialog() {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_listview, null);
		ListView mListView = (ListView) mView.findViewById(R.id.listView);
		NearShopListAdapter mAdapter = new NearShopListAdapter(mContext);
		mAdapter.addItem(bean.nearbyList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				sendWayId = "-1";
				send = "上门自提(" + ((NearShopListAdapter) parent.getAdapter()).getShopName(position) + ")";
				shopId = ((NearShopListAdapter) parent.getAdapter()).getShopId(position);
				spin_send_way_new.setText(send);
				mDialog.dismiss();
			}
		});
		mDialog = new AlertDialog.Builder(mContext, 3).setTitle("选择门店").setView(mView)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
						spin_send_way.setSelection(0);
						send = listSend.get(0);
						sendWayId = mPayWayBean.distributionManagementList.get(0).DISMANID;
						spin_send_way_new.setText(send);
					}
				}).create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}

	private void showListDialog(int i) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_listview, null);
		ListView mListView = (ListView) mView.findViewById(R.id.listView);
		NearShopListSendWayAdapter adapter_send = new NearShopListSendWayAdapter(mContext);
		adapter_send.addItem(listSend);
		mListView.setAdapter(adapter_send);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != listSend.size() - 1) {
					send = listSend.get(position);
					sendWayId = mPayWayBean.distributionManagementList.get(position).DISMANID;
					mDialog.dismiss();
				} else {
					mDialog.dismiss();
					getNearShop();
				}
				spin_send_way_new.setText(send);
			}
		});
		mDialog = new AlertDialog.Builder(mContext, 3).setTitle("选择配送方式").setView(mView)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
					}
				}).create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}
