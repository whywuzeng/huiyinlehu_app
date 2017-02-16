package com.huiyin.ui.servicecard;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.adapter.ServiceCareAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.ServiceCardListBean;
import com.huiyin.ui.shoppingcar.DuteSelectActivity;
import com.huiyin.ui.user.OnLineHelpActivity;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

@SuppressWarnings("static-access")
public class ServiceCardActivity extends Activity implements IXListViewListener {
	private Context mContext;

	// Content View Elementsf
	private TextView left_ib, middle_title_tv;

	private TextView top_cardNum;
	private TextView top_price;
	private TextView top_infomation;
	private Button recharge_button;
	private Button alter_button;
	private Button unbind_button;
	private TextView time_editText;

	private RadioButton all;
	private RadioButton recharge;
	private RadioButton expense;
	private RadioButton refund;
	private XListView mXlistview;

	// End Of Content View Elements

	private Dialog mDialog;

	private String userId;
	private ServiceCardListBean data;
	private ServiceCareAdapter mAdapter;

	/** 当前页 */
	private int mPageindex;
	private int type = 2;// 类型
	private String startTime = null;
	private String endTime = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_card_layout);
		mContext = this;
		userId = AppContext.getInstance().getUserId();
		// userId = "452";
		findView();
		setListener();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		String cardNum = AppContext.getInstance().getUserInfo().cardNum;
		cardNum = "卡号：<font color=\"#3592e2\">" + cardNum + "</font>";
		top_cardNum.setText(Html.fromHtml(cardNum));
		
		double balance = AppContext.getInstance().getUserInfo().balance;
		String priceT = "余额：<font color=\"red\">"
				+ MathUtil.priceForAppWithSign(balance) + "</font>";
		top_price.setText(Html.fromHtml(priceT));
		initData();
	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("便民生活服务卡");

		top_cardNum = (TextView) findViewById(R.id.top_card_bum);
		top_price = (TextView) findViewById(R.id.top_price);
		top_infomation = (TextView) findViewById(R.id.top_infomation);
		top_infomation.setText(Html.fromHtml("<u>使用帮助?</u>"));
		recharge_button = (Button) findViewById(R.id.recharge_button);
		alter_button = (Button) findViewById(R.id.alter_button);
		unbind_button = (Button) findViewById(R.id.unbind_button);
		time_editText = (TextView) findViewById(R.id.time_editText);

		all = (RadioButton) findViewById(R.id.all_RadioButton);
		recharge = (RadioButton) findViewById(R.id.recharge_RadioButton);
		expense = (RadioButton) findViewById(R.id.expense_RadioButton);
		refund = (RadioButton) findViewById(R.id.refund_RadioButton);
		mXlistview = (XListView) findViewById(R.id.xlistview);

		mXlistview.setPullLoadEnable(false);
		mXlistview.setPullRefreshEnable(true);
		mXlistview.setXListViewListener(this);

		mAdapter = new ServiceCareAdapter(mContext);
		mXlistview.setAdapter(mAdapter);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					type = 2;
					laodPageData(1);
				}
			}
		});
		recharge.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					type = 1;
					laodPageData(1);
				}
			}
		});
		expense.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					type = 2;
					laodPageData(1);
				}
			}
		});
		refund.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					type = 3;
					laodPageData(1);
				}
			}
		});
		top_infomation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, OnLineHelpActivity.class));
			}
		});
		alter_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, UpdateCardPwd.class));
			}
		});
		unbind_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showUnbindDialog();
			}
		});
		time_editText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mContext,
						DuteSelectActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
						DuteSelectActivity.REQUESET_CODE);
			}
		});
		recharge_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, RechargeServiceCard.class));
			}
		});
	}

	private void initData() {
		laodPageData(1);
	}

	private void laodPageData(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = ServiceCardListBean.explainJson(content, mContext);
				if (data.type == 1) {
					if (data.balance != 0) {
						AppContext.getInstance().getUserInfo().balance = data.balance;
						
						UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
						UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
						String priceT = "余额：<font color=\"red\">"
								+ MathUtil.priceForAppWithSign(data.balance)
								+ "</font>";
						top_price.setText(Html.fromHtml(priceT));
					}
					if (data.data != null && data.data.size() > 0) {
						if (mPageindex == 1) {
							mAdapter.deleteItem();
						}
						mAdapter.addItem(data.data);
						if (data.data.size() >= 10) {
							mXlistview.setPullLoadEnable(true);
						} else {
							mXlistview.setPullLoadEnable(false);
						}
					}
				} else {
					Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
					mXlistview.setPullLoadEnable(false);
					if (mPageindex == 1) {
						mAdapter.deleteItem();
					}
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				endUpData();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				endUpData();
			}

		};
		String token = AppContext.getInstance().getUserInfo().token;
		String cardNum = AppContext.getInstance().getUserInfo().cardNum;
		RequstClient.appPrepaidCardsList(handler, type, startTime, endTime,
				userId, token, cardNum, mPageindex);
	}

	@Override
	public void onRefresh() {
		laodPageData(1);
	}

	@Override
	public void onLoadMore() {
		laodPageData(2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DuteSelectActivity.REQUESET_CODE
				&& resultCode == RESULT_OK) {
			if (data != null) {
				String time = data.getStringExtra("duteTime");
				String[] times = time.split("~");
				startTime = times[0].replaceAll("-", "");
				endTime = times[1].replaceAll("-", "");
				time_editText.setText(time);
			}
		}
	}

	private void endUpData() {
		mXlistview.stopLoadMore(1);
		mXlistview.stopRefresh();
		mXlistview.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss",
				new Date()).toString());
	}

	private void showUnbindDialog() {
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.service_card_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		final EditText pwd = (EditText) mView.findViewById(R.id.com_message_tv);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = pwd.getText().toString();
				if (StringUtils.isBlank(password)) {
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				mDialog.dismiss();
				Unbind(password);
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

	private void Unbind(String pwd) {
		String cardNum = AppContext.getInstance().getUserInfo().cardNum;
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					Toast.makeText(mContext, "解除绑定成功", Toast.LENGTH_SHORT)
							.show();
					AppContext.getInstance().getUserInfo().bdStatus = 0;
					
					UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
					UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
					
					finish();
				} else {
					Toast.makeText(mContext, "解除绑定失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
		RequstClient.appRelieveBindCard(handler, userId, cardNum, pwd);
	}
}
