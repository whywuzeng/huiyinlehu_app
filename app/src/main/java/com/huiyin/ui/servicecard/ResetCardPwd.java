package com.huiyin.ui.servicecard;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.RegisterActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;
/**
 * 重置便民服务卡密码
 * @author zhangshuaijun
 *
 */
public class ResetCardPwd extends BaseActivity{
	
	TextView left_ib,middle_title_tv ;
	private EditText mobile;
	private EditText code;
	private EditText newpwd;
	private EditText affirm_pwd;
	
	private Button btn_bind;
	private boolean isGetRegeditCode;
	
	private TextView get_code_tv;
	
	private Toast mToast;
	private   Handler mHandler ;
	Timer mTimer = null;
	int sec = 60;
	private  final int SHOW_TOSAT = 0;
	
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.reset_cardpwd_layout);
			initView();
			initData();
			setListener();
		}
		
		private void initView(){
			middle_title_tv = (TextView)findViewById(R.id.ab_title);
			middle_title_tv.setText("便民生活服务卡密码重置");
			
			
			left_ib = (TextView)findViewById(R.id.ab_back);
			left_ib.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
						finish();
				}
			});
			
			
			mobile = (EditText) findViewById(R.id.mobile);
			code = (EditText) findViewById(R.id.code);
			newpwd = (EditText) findViewById(R.id.name);
			affirm_pwd = (EditText) findViewById(R.id.affirm_pwd);
			
			get_code_tv = (TextView)findViewById(R.id.get_code_tv);
			btn_bind= (Button) findViewById(R.id.btn_bind);
		}
	
	
	private void initData(){
		
		
	}
	
	private void setListener(){
		 mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case SHOW_TOSAT: {
						String message = (String) msg.obj;
						showToast(message);
					}
						break;
					}
				}
			};
			get_code_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					getVerifyCode();
				}
			});
			
			btn_bind.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					initData();
				}
			});
	}
	
	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT) ;
		}
		mToast.setText(msg);
		mToast.show();
	}
	
	
	/**
	 * 获取验证码
	 */
	private void getVerifyCode(){
		String registerPhone = mobile.getText().toString();
		if (TextUtils.isEmpty(registerPhone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(registerPhone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		RequstClient.getRegeditCode(registerPhone, "0",
				new CustomResponseHandler(ResetCardPwd.this) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						isGetRegeditCode = false;
					}
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							isGetRegeditCode = true;
							JSONObject result = new JSONObject(content);
							if (result.getString("type").equals("1")) {
								showResendTip();
								String msg = result.getString("msg");
								UIHelper.showToast(msg);
							} else {
								String msg = result.getString("msg");
								UIHelper.showToast(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		
	}
	
	
	
	
	/**
	 * 验证信息
	 * @return
	 */
	private boolean checkInfo() {
		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		String phoneNumber = mobile.getText().toString();
		String pwd = newpwd.getText().toString();
		String affirmPwd = affirm_pwd.getText().toString();
		String phoneCode = code.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(phoneNumber)) {
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(pwd)) {
			msg.obj = getString(R.string.pwd_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(affirmPwd) || !affirmPwd.equals(pwd)) {
			msg.obj = getString(R.string.affirm_pwd_not_same);
			mHandler.sendMessage(msg);
			return false;
		} 
		else if (TextUtils.isEmpty(phoneCode)) {
			msg.obj = getString(R.string.ver_code_is_null);
			mHandler.sendMessage(msg);
			return false;
		}
		return true;
	}
	
	
  private void showResendTip() {
		mTimer = new Timer();
		get_code_tv.setEnabled(false);
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sec--;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (sec < 1) {
							get_code_tv.setEnabled(true);
							get_code_tv.setText("重新获取"); 
							sec = 60;
							mTimer.cancel();
						} else {
							String tipMsg = String.format(
									getString(R.string.time_tip), sec);
							get_code_tv.setText(tipMsg);
						}
					}
				});
			}
		}, 0, 1000);
	}
	
}