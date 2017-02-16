package com.huiyin.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.InvoiceInfoBean;
import com.huiyin.bean.InvoiceInfoBean.InfoBean;
import com.huiyin.utils.MyCustomResponseHandler;

public class InvoiceDetailActivity extends BaseActivity {

	// Content View Elements

	private TextView ab_back;
	private TextView ab_title;
	private TextView normal_inv_tv;
	private LinearLayout normal_inv_layout;
	private TextView inv_title_type;
	private TextView inv_title;
	private LinearLayout added_inv_layout_1;
	private TextView inv_company;
	private TextView inv_identify;
	private TextView inv_regist_addr;
	private TextView inv_phone;
	private TextView inv_bank;
	private TextView inv_account;
	private TextView inv_detail;
	private LinearLayout added_inv_layout_2;
	private TextView inv_receiver;
	private TextView inv_receiver_phone;
	private TextView inv_receiver_addr;

	// End Of Content View Elements

	private InvoiceInfoBean bean;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invoice_detail_layout);
		Intent intent = getIntent();
		id = intent.getIntExtra("InvoiceId", 0);
		findView();
		setListener();
		InitData();
	}

	private void findView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		normal_inv_tv = (TextView) findViewById(R.id.normal_inv_tv);
		normal_inv_layout = (LinearLayout) findViewById(R.id.normal_inv_layout);
		inv_title_type = (TextView) findViewById(R.id.inv_title_type);
		inv_title = (TextView) findViewById(R.id.inv_title);
		added_inv_layout_1 = (LinearLayout) findViewById(R.id.added_inv_layout_1);
		inv_company = (TextView) findViewById(R.id.inv_company);
		inv_identify = (TextView) findViewById(R.id.inv_identify);
		inv_regist_addr = (TextView) findViewById(R.id.inv_regist_addr);
		inv_phone = (TextView) findViewById(R.id.inv_phone);
		inv_bank = (TextView) findViewById(R.id.inv_bank);
		inv_account = (TextView) findViewById(R.id.inv_account);
		inv_detail = (TextView) findViewById(R.id.inv_detail);
		added_inv_layout_2 = (LinearLayout) findViewById(R.id.added_inv_layout_2);
		inv_receiver = (TextView) findViewById(R.id.inv_receiver);
		inv_receiver_phone = (TextView) findViewById(R.id.inv_receiver_phone);
		inv_receiver_addr = (TextView) findViewById(R.id.inv_receiver_addr);
	}

	private void setListener() {
		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void InitData() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				bean = InvoiceInfoBean.explainJson(content, mContext);
				if (bean.type > 0) {
					RefreshView(bean.invoiceInfo);
				} else {
					Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.invoiceInfo(id, handler);
	}

	private void RefreshView(InfoBean invoiceInfo) {
		if (invoiceInfo.getINVOICING_METHOD() == 0) {
			normal_inv_tv.setText("普通发票");
			if (invoiceInfo.getINVOICE_TITLE_TYPE() != 1) {
				inv_title_type.setText("单位");
			}
			inv_title.setText(invoiceInfo.getINVOICE_TITLE());
		} else if (invoiceInfo.getINVOICING_METHOD() == 1) {
			normal_inv_tv.setText("增值税发票");
			normal_inv_layout.setVisibility(View.GONE);
			added_inv_layout_1.setVisibility(View.VISIBLE);
			added_inv_layout_2.setVisibility(View.VISIBLE);

			inv_company.setText(invoiceInfo.getCOMPANY_NAME());
			inv_identify.setText(invoiceInfo.getIDENTIFICATION_NUMBER());
			inv_regist_addr.setText(invoiceInfo.getREGISTERED_ADDRESS());
			inv_phone.setText(invoiceInfo.getREGISTERED_PHONE());
			inv_bank.setText(invoiceInfo.getBANK());
			inv_account.setText(invoiceInfo.getACCOUNT());
			inv_receiver.setText(invoiceInfo.getCOLLECTOR_NAME());
			inv_receiver_phone.setText(invoiceInfo.getCOLLECTOR_PHONE());
			inv_receiver_addr.setText(invoiceInfo.getCOLLECTOR_ADDRESS());
		}
		boolean temp = invoiceInfo.getINVOICE_CONTENT() == 1;
		inv_detail.setSelected(invoiceInfo.getINVOICE_CONTENT() == 1);
	}
}
