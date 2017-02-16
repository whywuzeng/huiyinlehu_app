package com.huiyin.ui.shoppingcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.user.MyOrderDetailActivity;

public class PaymentSuccessActivity extends Activity implements OnClickListener {

	public static final String INTENT_KEY_ORDER_ID = "order_id";
	public static final String INTENT_KEY_ORDER_NUM = "order_num";

	private String orderId;
	
	private String number;

	private TextView ab_title;

	private TextView tv_orderid, timer;

	private Button btn_shopping, btn_order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_success);

		initData();

		initView();
	}

	private void initData() {
		orderId = getIntent().getStringExtra(INTENT_KEY_ORDER_ID);
		number = getIntent().getStringExtra(INTENT_KEY_ORDER_NUM);
	}

	private void initView() {
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("支付成功");

		timer = (TextView) findViewById(R.id.timer);

		tv_orderid = (TextView) findViewById(R.id.tv_orderid);
		tv_orderid.setText("订单编号：" + number);
		
		btn_shopping = (Button) findViewById(R.id.btn_shopping);
		btn_shopping.setOnClickListener(this);
		
		btn_order = (Button) findViewById(R.id.btn_order);
		btn_order.setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_shopping:// 查看订单
			continueShopping();
			break;
		case R.id.btn_order:// 继续购物
			findOrderInfo();
			break;
		}
	}

	/**
	 * 查看订单
	 */
	public void findOrderInfo() {
		Intent i = new Intent();
		i.setClass(PaymentSuccessActivity.this, MyOrderDetailActivity.class);
		i.putExtra("order_id", orderId);
		startActivity(i);
		PaymentSuccessActivity.this.finish();
	}

	/**
	 * 继续购物
	 */
	public void continueShopping() {
		Intent i = new Intent();
		i.setClass(PaymentSuccessActivity.this, MainActivity.class);
		startActivity(i);
		PaymentSuccessActivity.this.finish();

	}

	private int count = 10;

	private static final int HANDLER_TIME_FINISH = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_TIME_FINISH:
				if (count > 0) {
					count--;
					mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
					timer.setText(count + "s后将自动返回订单详情");
				} else {
					findOrderInfo();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(HANDLER_TIME_FINISH);
		super.onDestroy();
	};
}