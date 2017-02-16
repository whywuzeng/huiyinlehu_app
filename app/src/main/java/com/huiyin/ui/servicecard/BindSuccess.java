package com.huiyin.ui.servicecard;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
/**
 * 绑定便民服务卡成功
 * @author zhangshuaijun
 *
 */
public class BindSuccess extends BaseActivity{
	
	TextView left_ib,middle_title_tv ;
	private TextView card_number;
	private TextView time;
	private TextView gotoRecharge;
	
	private double balance;
	
	private   Handler mHandler ;
	Timer mTimer = null;
	int sec = 15;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.bindsuccess_layout);
			initView();
			card_number.setText(getIntent().getStringExtra("cardnum"));
			balance = getIntent().getDoubleExtra("cardnum",0.0);
			showResendTip();
		}
		
		private void initView(){
			middle_title_tv = (TextView)findViewById(R.id.ab_title);
			middle_title_tv.setText("绑定便民生活服务卡");
			left_ib = (TextView)findViewById(R.id.ab_back);
			left_ib.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					mTimer.cancel();
				    finish();
				}
			});
			card_number = (TextView) findViewById(R.id.cardnum);
			time = (TextView) findViewById(R.id.time);
			
			gotoRecharge = (TextView) findViewById(R.id.gotoRecharge);
			gotoRecharge.setText(Html.fromHtml("提示:您可以使用此卡进行在线支付,或者进行<font color = \"blue\"><u>在线充值</u></font>"));
			gotoRecharge.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mTimer.cancel();
					startActivity(new Intent(BindSuccess.this,RechargeServiceCard.class));
					finish();
				}
			});
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					}
			};
		}
	
	private void showResendTip() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sec--;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (sec < 1) {
							Intent intent = new Intent(BindSuccess.this,ServiceCardActivity.class);
							intent.putExtra("balance",balance);
							startActivity(intent);
							mTimer.cancel();
							finish();
						} else {
							String tipMsg = String.format(
									getString(R.string.time_tip)+"后自动跳转", sec);
							time.setText(tipMsg);
						}
					}
				});
			}
		}, 0, 1000);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mTimer.cancel();
		}
		return super.onKeyDown(keyCode, event);
	}

}
