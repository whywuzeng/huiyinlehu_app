package com.huiyin.ui.servicecard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
/**
 * 便民服务卡充值成功/失败
 * @author zhangshuaijun
 *
 */
public class RechargeSuccess extends BaseActivity{
	
	TextView left_ib,middle_title_tv ;
	
	private int resultType; //2失败 1成功
	private int payMode;
	private String msg;
	
	private TextView title;// 标题
	private TextView tv_pay_tip1;// 支付提示
	private TextView tv_pay_tip2;// 您已成功使用支付宝支付完成交易
	private TextView tv_pay_info;
	
	private LinearLayout tv_countdown;
	
	private Button pay_continueshop;
	private Button pay_find_orderinfo;
	
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.recharge_success_layout);
			Intent intent = getIntent();
			resultType = intent.getIntExtra("resultType",1);
			payMode = intent.getIntExtra("payMode",1);
			msg = intent.getStringExtra("msg");
			
			initView();
			setListener();
			
		}
		
		private void initView(){
			title = (TextView) findViewById(R.id.title);
			tv_pay_tip1 = (TextView) findViewById(R.id.tv_pay_tip1);
			tv_pay_tip2 = (TextView) findViewById(R.id.tv_pay_tip2);
			tv_pay_info = (TextView) findViewById(R.id.tv_pay_info);
			if(!com.huiyin.utils.StringUtils.isBlank(msg)) {
				tv_pay_info.setVisibility(View.VISIBLE);
				tv_pay_info.setText(msg);
			}
			tv_countdown = (LinearLayout)findViewById(R.id.tv_countdown);
			
			pay_continueshop = (Button) findViewById(R.id.pay_continueshop);
			pay_find_orderinfo = (Button) findViewById(R.id.pay_find_orderinfo);
			
			
			if(resultType == 1){
				title.setText("充值成功");
				tv_pay_tip1.setText("充值成功!");
				if(payMode == 1){
					tv_pay_tip2.setText("您已成功使用支付宝充值");
				}else{
					tv_pay_tip2.setText("您已成功使用中国银联充值");
				}
				tv_countdown.setVisibility(View.INVISIBLE);
			}else{
				title.setText("充值失败");
				tv_pay_tip1.setText("充值失败,请重新充值!");
			    tv_pay_tip2.setText(" ");
				pay_continueshop.setText("选择其他平台充值");
				pay_find_orderinfo.setText("返回列表查看");
			}
			
			pay_continueshop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(resultType == 1){
						continueShopping();
					}else{
						startActivity(new Intent(RechargeSuccess.this, RechargeServiceCard.class));
					    finish();
					}
				}
			});
			
		}
	
	private void setListener(){
		
		pay_find_orderinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				findOrderInfo();
			}
		});
		
	}
	

	
	public void findOrderInfo() {
		startActivity(new Intent(RechargeSuccess.this, ServiceCardActivity.class));
		finish();
	}
	
	/**
	 * 继续购物
	 */
	public void continueShopping() {
		Intent i = new Intent();
		i.setClass(RechargeSuccess.this, MainActivity.class);
		startActivity(i);
		finish();
	}
	
}
