package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AddressItem;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.shoppingcar.WriteOrderActivity;
import com.huiyin.utils.LogUtil;

public class AddressManagementActivity extends BaseActivity {

	public static String TAG = "AddressManagementActivity";
	private TextView mTextView;
	ListView mListView;
	private AddressBean mAddressBean;
	private AddressAdapter mAddressAdapter;
	// 修改默认地址的那个item在列表中的位置
	private int defaultPosition;
	private TextView left_rb, middle_title_tv;

	private ImageView right_rb;

	private int request_modify_add = 0x01112, request_delete = 0x11111,
			request_default_addr = 0x11112;
	// 1订单编辑进来、2账户管理进来
	public static String order = "1", addr = "2";
	String which;
	// 要删除的地址id
	private String addressId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_address_list);

		initView();

		initData();
	}

	/**
	 * 初始化
	 */
	private void initData() {

		if (AppContext.getInstance().getUserId() == null) {
			Toast.makeText(AddressManagementActivity.this, "请先登录！",
					Toast.LENGTH_SHORT).show();
			return;
		}// 1参数无效，这里一次性返回所有地址
		RequstClient.getAddrList(AppContext.getInstance().getUserId(), "1",
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "getAddrList:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							mAddressBean = new Gson().fromJson(content,
									AddressBean.class);
							mAddressAdapter = new AddressAdapter(mAddressBean);
							mListView.setAdapter(mAddressAdapter);
							if (!obj.has("shippingAddress")) {
								mTextView.setText("暂无收货地址");
							} else if (mAddressBean.shippingAddress.size() > 0) {
								mTextView.setText("长按设为默认");
							} else {
								mTextView.setText("暂无收货地址");
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	/**
	 * 初始化
	 */

	private void initView() {

		left_rb = (TextView) findViewById(R.id.ab_back);
		right_rb = (ImageView) findViewById(R.id.ab_right_btn);
		right_rb.setImageResource(R.drawable.ab_ic_add);

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("地址管理");

		mTextView = (TextView) findViewById(R.id.mTextView);

		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		right_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(AddressManagementActivity.this,
						AddressModifyActivity.class);
				i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.ADD);
				startActivityForResult(i, request_modify_add);
			}
		});

		mAddressBean = new AddressBean();
		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (which.equals(order)) {
					AddressItem item = mAddressBean.shippingAddress.get(arg2);
					Intent i = new Intent();
					i.setClass(AddressManagementActivity.this,
							WriteOrderActivity.class);
					i.putExtra("addr", item);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				if (!which.equals(order)) {
					AddressItem item = mAddressBean.shippingAddress.get(arg2);
					defaultPosition = arg2;
					addressId = item.ADDRESSID;
					Intent i = new Intent(AddressManagementActivity.this,
							CommonConfrimCancelDialog.class);
					i.putExtra(CommonConfrimCancelDialog.TASK,
							CommonConfrimCancelDialog.TASK_DEFAULT_ADDR);
					startActivityForResult(i, request_default_addr);
//				}
				return true;
			}
		});

		which = getIntent().getStringExtra(TAG);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == request_modify_add
				&& resultCode == Activity.RESULT_OK) {// 不论增加、修改刷新列表
			initData();
		} else if (requestCode == request_delete
				&& resultCode == Activity.RESULT_OK) {
			doDeleteAddress(addressId);
		} else if (requestCode == request_default_addr
				&& resultCode == Activity.RESULT_OK) {
			doModifyDefaultAddr(addressId, "1");
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 删除
	 * 
	 * @param addressId
	 */
	private void doDeleteAddress(String addressId) {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.postDeleteAddress(AppContext.getInstance().getUserId(), addressId,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postDeleteAddress:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							initData();

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	/***
	 * 刷新列表
	 */

	private void refreshList() {

		if (mAddressBean == null) {
			return;
		}
		for (int i = 0; i < mAddressBean.shippingAddress.size(); i++) {
			AddressItem item = mAddressBean.shippingAddress.get(i);
			if (i == defaultPosition) {
				item.IS_DEFAULT = "1";
			} else {
				item.IS_DEFAULT = "0";
			}
		}
		mAddressAdapter.notifyDataSetChanged();

	}

	/***
	 * 
	 * 修改默认地址（和修改、新增同一个接口, 只传userId、addressId、 isDefault三个字段）
	 * 
	 * @param addressId
	 * @param isDefault
	 */

	private void doModifyDefaultAddr(String addressId, String isDefault) {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}

		RequstClient.postModifyAddress(AppContext.getInstance().getUserId(), "", "", "", "", "",
				"", "", addressId, isDefault, "", new CustomResponseHandler(
						this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postModifyAddress:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(getBaseContext(), "默认地址修改成功",
									Toast.LENGTH_SHORT).show();

							refreshList();

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	class AddressAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public AddressAdapter(AddressBean mAddressBean) {
			this.inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAddressBean.shippingAddress == null ? 0
					: mAddressBean.shippingAddress.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAddressBean.shippingAddress.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mAddressBean.shippingAddress.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final AddressItem item = mAddressBean.shippingAddress.get(position);
			convertView = inflater.inflate(
					R.layout.address_management_listitem, null);
			final int pos = position;
			TextView addr_username = (TextView) convertView
					.findViewById(R.id.addr_username);
			TextView addr_user_phone = (TextView) convertView
					.findViewById(R.id.addr_user_phone);
			TextView addr_address_info = (TextView) convertView
					.findViewById(R.id.addr_address_info);
			ImageView addr_modify = (ImageView) convertView
					.findViewById(R.id.addr_modify);
			ImageView addr_delete = (ImageView) convertView
					.findViewById(R.id.addr_delete);
			TextView addr_default_tv = (TextView) convertView
					.findViewById(R.id.addr_default_tv);

			if (item.IS_DEFAULT == null) {
				addr_default_tv.setVisibility(View.GONE);
			} else if (item.IS_DEFAULT.equals("1")) {
				addr_default_tv.setVisibility(View.VISIBLE);
			} else {
				addr_default_tv.setVisibility(View.GONE);
			}
			// addr_default_tv.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// defaultPosition = pos;
			// doModifyDefaultAddr(item.ADDRESSID, "1");
			// }
			// });
			addr_modify.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent();
					i.setClass(AddressManagementActivity.this,
							AddressModifyActivity.class);
					i.putExtra(AddressModifyActivity.TAG,
							AddressModifyActivity.MODIFY);
					i.putExtra("addressItem", item);
					startActivityForResult(i, request_modify_add);
				}

			});
			addr_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					addressId = item.ADDRESSID;
					Intent i = new Intent(AddressManagementActivity.this,
							CommonConfrimCancelDialog.class);
					i.putExtra(CommonConfrimCancelDialog.TASK,
							CommonConfrimCancelDialog.TASK_DELETE_ADDR);
					startActivityForResult(i, request_delete);
				}

			});
			if (which.equals(order)) {
				addr_delete.setVisibility(View.GONE);
			} else {
				addr_delete.setVisibility(View.VISIBLE);
			}

			addr_username.setText(item.CONSIGNEE_NAME);
			addr_user_phone.setText(item.CONSIGNEE_PHONE);
			addr_address_info.setText(item.LEVELADDR + item.ADDRESS);

			return convertView;
		}

	}

	public class AddressBean {

		public ArrayList<AddressItem> shippingAddress = new ArrayList<AddressItem>();

	}

}
