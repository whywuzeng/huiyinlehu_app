package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.BaseBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class LeHuJuanActivity extends BaseActivity {

	private final static String TAG = "LeHuJuanActivity";
	// 所有、未使用、已使用、已过期列表
	private XListView xlistview_all, xlistview_unuse, xlistview_use, xlistview_past;

	private LeHuJuanBean mAllBean, mUseBean, mUnuseBean, mPastBean;
	LeHuAdapter mAdapterAll, mAdapterUnuse, mAdapterUse, mAdapterPast;
	TextView left_rb, middle_title_tv;

	private int page_all = 1, page_unuse = 1, page_use = 1, page_past = 1;
	private int page_size = 10;
	TextView lhj_past_tv, lhj_use_tv, lhj_unuse_tv, lhj_all_tv;
	// 所有的、未使用的、已使用、已过期、即将过期
	private static final int ALL_TYPE = 0, UNUSE_TYPE = 1, USE_TYPE = 2, PAST_TYPE = 3, ALMOST_PAST_TYPE = 4;

	// 处于哪个列表
	private int current_pos = 0, current_type = 0;
	// 即将过期
	private int myCouponOut;

	private int index_red, black_color;

	LayoutInflater myInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lehujuan_list_layout);

		initView();
		getLeHuList(ALL_TYPE, page_all, true);

	}

	/****
	 * @param which_type
	 *            类型
	 * @param pageIndex
	 *            页码
	 */
	private void getLeHuList(final int which_type, int pageIndex, final boolean initB) {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.getLeHuList(AppContext.getInstance().getUserId(), which_type + "", pageIndex + "", new CustomResponseHandler(this) {
			@Override
			public void onStart() {
				if (initB)
					super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();

				xlistview_all.stopLoadMore();
				xlistview_all.stopRefresh();

				xlistview_unuse.stopLoadMore();
				xlistview_unuse.stopRefresh();

				xlistview_use.stopLoadMore();
				xlistview_use.stopRefresh();

				xlistview_past.stopLoadMore();
				xlistview_past.stopRefresh();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "getLeHuList:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					if (which_type == ALL_TYPE || which_type == ALMOST_PAST_TYPE) {

						if (page_all == 1) {

							mAllBean = new Gson().fromJson(content, LeHuJuanBean.class);
							mAdapterAll = new LeHuAdapter(mAllBean);
							xlistview_all.setAdapter(mAdapterAll);
							if (mAdapterAll.getCount() < page_size) {
								xlistview_all.hideFooter();
							} else {
								xlistview_all.showFooter();
							}
						} else {

							LeHuJuanBean mLhjBean = new Gson().fromJson(content, LeHuJuanBean.class);
							if (mLhjBean.couponList != null) {
								if (mLhjBean.couponList.size() > 0) {
									mAllBean.couponList.addAll(mLhjBean.couponList);
									mAdapterAll.notifyDataSetChanged();
								} else {
									Toast.makeText(LeHuJuanActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
									xlistview_all.hideFooter();
								}
							}
							xlistview_all.stopLoadMore();
							xlistview_all.stopRefresh();
						}

					} else if (which_type == UNUSE_TYPE || which_type == ALMOST_PAST_TYPE) {

						if (page_unuse == 1) {
							mUnuseBean = new Gson().fromJson(content, LeHuJuanBean.class);
							mAdapterUnuse = new LeHuAdapter(mUnuseBean);
							xlistview_unuse.setAdapter(mAdapterUnuse);
							if (mAdapterUnuse.getCount() < page_size) {
								xlistview_unuse.hideFooter();
							} else {
								xlistview_unuse.showFooter();
							}
						} else {
							LeHuJuanBean mLhjBean = new Gson().fromJson(content, LeHuJuanBean.class);
							if (mLhjBean.couponList != null) {
								if (mLhjBean.couponList.size() > 0) {
									mUnuseBean.couponList.addAll(mLhjBean.couponList);
									mAdapterUnuse.notifyDataSetChanged();
								} else {
									Toast.makeText(LeHuJuanActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
									xlistview_unuse.hideFooter();
								}

							}
							xlistview_unuse.stopLoadMore();
							xlistview_unuse.stopRefresh();
						}

					} else if (which_type == USE_TYPE) {

						if (page_use == 1) {
							mUseBean = new Gson().fromJson(content, LeHuJuanBean.class);
							mAdapterUse = new LeHuAdapter(mUseBean);
							xlistview_use.setAdapter(mAdapterUse);

							if (mAdapterUse.getCount() < page_size) {
								xlistview_use.hideFooter();
							} else {
								xlistview_use.showFooter();
							}

						} else {
							LeHuJuanBean mLhjBean = new Gson().fromJson(content, LeHuJuanBean.class);
							if (mLhjBean.couponList != null) {
								if (mLhjBean.couponList.size() > 0) {
									mUseBean.couponList.addAll(mLhjBean.couponList);
									mAdapterUse.notifyDataSetChanged();
								} else {
									Toast.makeText(LeHuJuanActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
									xlistview_use.hideFooter();
								}

							}
							xlistview_use.stopLoadMore();
							xlistview_use.stopRefresh();

						}

					} else if (which_type == PAST_TYPE) {

						if (page_past == 1) {

							mPastBean = new Gson().fromJson(content, LeHuJuanBean.class);
							mAdapterPast = new LeHuAdapter(mPastBean);
							xlistview_past.setAdapter(mAdapterPast);

							if (mAdapterPast.getCount() < page_size) {
								xlistview_past.hideFooter();
							} else {
								xlistview_past.showFooter();
							}

						} else {
							LeHuJuanBean mLhjBean = new Gson().fromJson(content, LeHuJuanBean.class);

							if (mLhjBean.couponList != null) {
								if (mLhjBean.couponList.size() > 0) {
									mPastBean.couponList.addAll(mLhjBean.couponList);
									mAdapterPast.notifyDataSetChanged();
								} else {
									Toast.makeText(LeHuJuanActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
									xlistview_past.hideFooter();
								}
							}
							xlistview_past.stopLoadMore();
							xlistview_past.stopRefresh();
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

	private void addAllTitle() {
		View view = myInflater.inflate(R.layout.all_lehuquan_title, null);
		EditText mEditText = (EditText) view.findViewById(R.id.lehuquan_editText);
		Button confirm = (Button) view.findViewById(R.id.lehuquan_button);
		confirm.setOnClickListener(new OnClick(mEditText));
		xlistview_all.addHeaderView(view);
	}

	/**
	 * 增加即将过期的在列表头部
	 */
	private void addHeader() {

		for (int i = 0; i < 2; i++) {

			View view = myInflater.inflate(R.layout.lhj_title, null);
			TextView lhj_tip = (TextView) view.findViewById(R.id.lhj_title);
			lhj_tip.setText(String.format(getString(R.string.lhj_tip), myCouponOut + ""));
			lhj_tip.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (current_pos == ALL_TYPE) {
						page_all = 1;
						current_type = ALMOST_PAST_TYPE;
						getLeHuList(ALMOST_PAST_TYPE, page_all, true);
					} else if (current_pos == UNUSE_TYPE) {
						page_all = 1;
						current_type = ALMOST_PAST_TYPE;
						
						lhj_all_tv.setTextColor(index_red);
						lhj_use_tv.setTextColor(black_color);
						lhj_unuse_tv.setTextColor(black_color);
						lhj_past_tv.setTextColor(black_color);

						xlistview_all.setVisibility(View.VISIBLE);
						xlistview_unuse.setVisibility(View.GONE);
						xlistview_use.setVisibility(View.GONE);
						xlistview_past.setVisibility(View.GONE);
						
						getLeHuList(ALMOST_PAST_TYPE, page_all, true);
					}
//					} else if (current_pos == UNUSE_TYPE) {
//						page_unuse = 1;
//						current_type = ALMOST_PAST_TYPE;
//						getLeHuList(ALMOST_PAST_TYPE, page_unuse, true);
//					}
				}
			});

			if (i == 0) {
				xlistview_all.addHeaderView(view);
			} else {
				xlistview_unuse.addHeaderView(view);
			}
		}
	}

	/**
	 * 初始化
	 */
	private void initView() {

		index_red = getResources().getColor(R.color.index_red);
		black_color = getResources().getColor(R.color.black);

		myInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("我的乐虎红包(券)");

		String couponOut = getIntent().getStringExtra("myCouponOut");

		if (couponOut != null) {
			myCouponOut = Integer.parseInt(couponOut);
		} else {
			myCouponOut = 0;
		}

		left_rb = (TextView) findViewById(R.id.left_ib);
		left_rb.setVisibility(View.VISIBLE);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mAllBean = new LeHuJuanBean();
		mUseBean = new LeHuJuanBean();
		mUnuseBean = new LeHuJuanBean();
		mPastBean = new LeHuJuanBean();

		xlistview_all = (XListView) findViewById(R.id.xlistview_all);
		xlistview_all.setPullLoadEnable(true);
		xlistview_all.setPullRefreshEnable(true);
		xlistview_all.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page_all = 1;
				if (current_type == ALMOST_PAST_TYPE) {
					getLeHuList(ALMOST_PAST_TYPE, page_all, false);
				} else {
					getLeHuList(ALL_TYPE, page_all, false);
				}

			}

			@Override
			public void onLoadMore() {
				page_all = mAdapterAll.getCount() / page_size + 1;
				if (mAdapterAll.getCount() % page_size > 0)
					page_all++;

				if (current_type == ALMOST_PAST_TYPE) {
					getLeHuList(ALMOST_PAST_TYPE, page_all, false);
				} else {
					getLeHuList(ALL_TYPE, page_all, false);
				}
			}

		});

		xlistview_all.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemId;
				if (myCouponOut > 0) {
					itemId = mAdapterAll.getId(position - 3);
				} else {
					itemId = mAdapterAll.getId(position - 2);
				}
				showDeleteDialog(itemId, ALL_TYPE);
			}
		});

		xlistview_unuse = (XListView) findViewById(R.id.xlistview_unuse);
		xlistview_unuse.setPullLoadEnable(true);
		xlistview_unuse.setPullRefreshEnable(true);
		xlistview_unuse.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page_unuse = 1;

				if (current_type == ALMOST_PAST_TYPE) {
					getLeHuList(ALMOST_PAST_TYPE, page_unuse, false);
				} else {
					getLeHuList(UNUSE_TYPE, page_unuse, false);
				}

			}

			@Override
			public void onLoadMore() {
				page_unuse = mAdapterUnuse.getCount() / page_size + 1;
				if (mAdapterUnuse.getCount() % page_size > 0)
					page_unuse++;

				if (current_type == ALMOST_PAST_TYPE) {
					getLeHuList(ALMOST_PAST_TYPE, page_unuse, false);
				} else {
					getLeHuList(UNUSE_TYPE, page_unuse, false);
				}

			}

		});

		xlistview_unuse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemId;
				if (myCouponOut > 0) {
					itemId = mAdapterUnuse.getId(position - 2);
				} else {
					itemId = mAdapterUnuse.getId(position - 1);
				}
				showDeleteDialog(itemId, UNUSE_TYPE);
			}
		});

		addAllTitle();
		if (myCouponOut > 0) {
			addHeader();
		}

		xlistview_use = (XListView) findViewById(R.id.xlistview_use);
		xlistview_use.setPullLoadEnable(true);
		xlistview_use.setPullRefreshEnable(true);
		xlistview_use.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page_use = 1;
				getLeHuList(USE_TYPE, page_use, false);
			}

			@Override
			public void onLoadMore() {
				page_use = mAdapterUse.getCount() / page_size + 1;
				if (mAdapterUse.getCount() % page_size > 0)
					page_use++;
				getLeHuList(USE_TYPE, page_use, false);
			}

		});

		xlistview_use.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemId;
				itemId = mAdapterUse.getId(position - 1);
				showDeleteDialog(itemId, USE_TYPE);
			}
		});

		xlistview_past = (XListView) findViewById(R.id.xlistview_past);
		xlistview_past.setPullLoadEnable(true);
		xlistview_past.setPullRefreshEnable(true);
		xlistview_past.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page_past = 1;
				getLeHuList(PAST_TYPE, page_past, false);

			}

			@Override
			public void onLoadMore() {
				page_past = mAdapterPast.getCount() / page_size + 1;
				if (mAdapterPast.getCount() % page_size > 0)
					page_past++;
				getLeHuList(PAST_TYPE, page_past, false);
			}

		});

		xlistview_past.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemId;
				itemId = mAdapterPast.getId(position - 1);
				showDeleteDialog(itemId, PAST_TYPE);
			}
		});

		lhj_past_tv = (TextView) findViewById(R.id.lhj_past_tv);
		lhj_use_tv = (TextView) findViewById(R.id.lhj_use_tv);
		lhj_unuse_tv = (TextView) findViewById(R.id.lhj_unuse_tv);
		lhj_all_tv = (TextView) findViewById(R.id.lhj_all_tv);

		ViewOnClick onClick = new ViewOnClick();

		lhj_past_tv.setOnClickListener(onClick);
		lhj_use_tv.setOnClickListener(onClick);
		lhj_unuse_tv.setOnClickListener(onClick);
		lhj_all_tv.setOnClickListener(onClick);

	}

	private void setVisibleList(int which) {

		switch (which) {
		case ALL_TYPE:

			lhj_all_tv.setTextColor(index_red);
			lhj_use_tv.setTextColor(black_color);
			lhj_unuse_tv.setTextColor(black_color);
			lhj_past_tv.setTextColor(black_color);

			xlistview_all.setVisibility(View.VISIBLE);
			xlistview_unuse.setVisibility(View.GONE);
			xlistview_use.setVisibility(View.GONE);
			xlistview_past.setVisibility(View.GONE);
			if (mAdapterAll == null || mAdapterAll.getCount() < 1) {
				getLeHuList(ALL_TYPE, page_all, true);
			} else if (current_type == ALMOST_PAST_TYPE) {
				page_all = 1;
				getLeHuList(ALL_TYPE, page_all, true);
			}
			current_type = current_pos = ALL_TYPE;

			break;

		case UNUSE_TYPE:

			lhj_all_tv.setTextColor(black_color);
			lhj_use_tv.setTextColor(black_color);
			lhj_unuse_tv.setTextColor(index_red);
			lhj_past_tv.setTextColor(black_color);

			xlistview_all.setVisibility(View.GONE);
			xlistview_unuse.setVisibility(View.VISIBLE);
			xlistview_use.setVisibility(View.GONE);
			xlistview_past.setVisibility(View.GONE);
			if (mAdapterUnuse == null || mAdapterUnuse.getCount() < 1) {
				getLeHuList(UNUSE_TYPE, page_unuse, true);
			} else if (current_type == ALMOST_PAST_TYPE) {
				page_unuse = 1;
				getLeHuList(UNUSE_TYPE, page_unuse, true);
			}
			current_type = current_pos = UNUSE_TYPE;

			break;

		case USE_TYPE:
			current_type = current_pos = USE_TYPE;

			lhj_all_tv.setTextColor(black_color);
			lhj_use_tv.setTextColor(index_red);
			lhj_unuse_tv.setTextColor(black_color);
			lhj_past_tv.setTextColor(black_color);

			xlistview_all.setVisibility(View.GONE);
			xlistview_unuse.setVisibility(View.GONE);
			xlistview_use.setVisibility(View.VISIBLE);
			xlistview_past.setVisibility(View.GONE);
			if (mAdapterUse == null || mAdapterUse.getCount() < 1) {
				getLeHuList(USE_TYPE, page_use, true);
			}
			break;

		case PAST_TYPE:
			current_type = current_pos = PAST_TYPE;

			lhj_all_tv.setTextColor(black_color);
			lhj_use_tv.setTextColor(black_color);
			lhj_unuse_tv.setTextColor(black_color);
			lhj_past_tv.setTextColor(index_red);

			xlistview_all.setVisibility(View.GONE);
			xlistview_unuse.setVisibility(View.GONE);
			xlistview_use.setVisibility(View.GONE);
			xlistview_past.setVisibility(View.VISIBLE);
			if (mAdapterPast == null || mAdapterPast.getCount() < 1) {
				getLeHuList(PAST_TYPE, page_past, true);
			}
			break;

		default:
			break;
		}

	}

	class ViewOnClick implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {

			case R.id.lhj_past_tv:
				setVisibleList(PAST_TYPE);
				break;
			case R.id.lhj_use_tv:
				setVisibleList(USE_TYPE);
				break;
			case R.id.lhj_unuse_tv:
				setVisibleList(UNUSE_TYPE);
				break;
			case R.id.lhj_all_tv:
				setVisibleList(ALL_TYPE);
				break;

			default:
				break;
			}
		}
	}

	class LeHuAdapter extends BaseAdapter {

		LayoutInflater inflater;
		LeHuJuanBean lhBean;

		public LeHuAdapter(LeHuJuanBean lhBean) {
			this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.lhBean = lhBean;
		}

		@Override
		public int getCount() {

			return lhBean.couponList == null ? 0 : lhBean.couponList.size();
		}

		@Override
		public Object getItem(int position) {
			return lhBean.couponList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return lhBean.couponList.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LeHuJuanBean.LeHuJuanItem item = lhBean.couponList.get(position);
			convertView = inflater.inflate(R.layout.lehujuan_listitem, null);
			String demand;

			TextView lehujuan_title_id = (TextView) convertView.findViewById(R.id.lehujuan_title_id);
			TextView lehujuan_time_limit_id = (TextView) convertView.findViewById(R.id.lehujuan_time_limit_id);
			TextView lehujuan_price_id = (TextView) convertView.findViewById(R.id.lehujuan_price_id);
			TextView lehujuan_condition_id = (TextView) convertView.findViewById(R.id.lehujuan_condition_id);

			lehujuan_title_id.setText(item.NAME);
			lehujuan_time_limit_id.setText("使用期限：" + item.STARTTIME + "--" + item.ENDTIME);
			lehujuan_price_id.setText("¥" + item.PRICE);
			if (item.DEMAND == null || item.DEMAND.equals("")) {
				demand = String.format(getString(R.string.lhj_demand), 0);
			} else {
				demand = String.format(getString(R.string.lhj_demand), item.DEMAND);
			}
			lehujuan_condition_id.setText(demand);

			return convertView;
		}

		public int getId(int positon) {
			return lhBean.couponList.get(positon).ID;
		}
	}

	public class LeHuJuanBean {

		public ArrayList<LeHuJuanItem> couponList = new ArrayList<LeHuJuanItem>();

		public class LeHuJuanItem {
			public String IMG;// 图标
			public String NAME;// 优惠劵名称
			public String DEMAND;// 如果是面值优惠券类型，要求订单满多少钱才可以使用
			public String PRICE;// 优惠券值
			public String STARTTIME;// 开始时间
			public String ENDTIME;// 结束时间
			public int ID;
		}
	}

	private class OnClick implements OnClickListener {
		private EditText temp;

		OnClick(EditText temp) {
			this.temp = temp;
		}

		@Override
		public void onClick(View v) {
			String code = temp.getText().toString();
			if (StringUtils.isBlank(code)) {
				Toast.makeText(mContext, "请输入乐虎红包(券)编号", Toast.LENGTH_SHORT).show();
				return;
			}
			MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

				@Override
				public void onRefreshData(String content) {
					super.onRefreshData(content);
					Gson gson = new Gson();
					BaseBean bean = gson.fromJson(content, BaseBean.class);
					if (bean.type > 0) {
						temp.setText("");
						Toast.makeText(mContext, "乐虎券激活成功!", Toast.LENGTH_SHORT).show();
						getLeHuList(ALL_TYPE, page_all, true);
					} else {
						Toast.makeText(mContext, bean.msg, Toast.LENGTH_SHORT).show();
					}
				}

			};
			RequstClient.postCodeValidate(AppContext.getInstance().getUserId(), code, handler);
		}

	}

	private Dialog mDialog;

	private void showDeleteDialog(final int id, final int type) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.service_card_pay_fail_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		TextView pwd = (TextView) mView.findViewById(R.id.com_message_tv);
		pwd.setText("确认删除乐虎红包(券)？");
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		yes.setText("确定");
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setText("取消");
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Unbind(id, type);
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);

		// Window dialogWindow = mDialog.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// lp.height = DensityUtil.dip2px(mContext, 350); // 高度设置为屏幕的0.2
		// lp.width = DensityUtil.dip2px(mContext, 590); // 宽度设置为屏幕的0.8
		// dialogWindow.setAttributes(lp);

		mDialog.show();
	}

	private void Unbind(final int id, final int type) {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
					reflashList(type);
				} else {
					Toast.makeText(mContext, bean.msg, Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.appMyCouponDelete(handler, id);
	}

	private void reflashList(int type) {
		switch (type) {
		case ALL_TYPE:
			getLeHuList(ALL_TYPE, page_all, true);
			break;
		case UNUSE_TYPE:
			getLeHuList(UNUSE_TYPE, page_unuse, true);
			break;
		case USE_TYPE:
			getLeHuList(USE_TYPE, page_use, true);
			break;
		case PAST_TYPE:
			getLeHuList(PAST_TYPE, page_past, true);
			break;
		}
	}
}
