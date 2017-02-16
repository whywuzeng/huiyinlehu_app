package com.huiyin.ui.servicecard;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.OnLineHelpActivity;
import com.huiyin.utils.StringUtils;

/**
 * 申请便民服务卡
 * @author zhangshuaijun
 *
 */
public class ApplyServiceCard extends BaseActivity {

	TextView left_ib, middle_title_tv;
	private EditText mobile;
	private EditText code;
	private EditText name;
	private EditText idcode;
	private EditText address;

	private TextView get_code_tv;
	private Button btn_bind;
	
	private CheckBox cb_agree;
	private TextView agree_textView;
	
	Timer mTimer = null;
	int sec = 60;
	private   Handler mHandler ;
	private Toast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applyservice_layout);
		initView();
		setListener();
	}

	private void initView() {
		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("申请便民生活服务卡");

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mobile = (EditText) findViewById(R.id.mobile);
		code = (EditText) findViewById(R.id.code);
		name = (EditText) findViewById(R.id.name);
		idcode = (EditText) findViewById(R.id.idcode);
		address = (EditText) findViewById(R.id.address);

		get_code_tv = (TextView) findViewById(R.id.get_code_tv);
		btn_bind = (Button) findViewById(R.id.btn_bind);
		
		cb_agree = (CheckBox) findViewById(R.id.cb_agree);
		
		
		mobile.setText(AppContext.getInstance().getUserInfo().phone);
		agree_textView = (TextView) findViewById(R.id.agree_textView);
		agree_textView.setText(Html.fromHtml("同意《<font color = \"blue\"><u>便民生活服务卡申请协议</u></font>》"));
	}

	private void setListener() {
		get_code_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(StringUtils.isFastDoubleClick()){
					 return;  
				}
				getVerifyCode();
			}
		});
		
		btn_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkInfo()) {
					initDatas();
				}
			}
		});
		 mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
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
			agree_textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(mContext, OnLineHelpActivity.class));
				}
			});
	}
	
	
	private void initDatas(){
		String mobileText = mobile.getText().toString();
		String codeText = code.getText().toString();
		String nameText = name.getText().toString();
		String idcodeText = idcode.getText().toString();
		String addressText = address.getText().toString();
		RequstClient.appApplyCard(AppContext.getInstance().getUserId(),mobileText,codeText,nameText,idcodeText,addressText, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_SHORT).show();
						return;
					}
					if(obj.has("cardNum")){
						AppContext.getInstance().getUserInfo().cardNum= obj.getString("cardNum");
						AppContext.getInstance().getUserInfo().bdStatus = 1;
						Intent intent = new Intent(ApplyServiceCard.this,ApplyServiceCardSuccessActivity.class);
						intent.putExtra("cardPwd", obj.getString("cardPwd"));
						intent.putExtra("cardNum", obj.getString("cardNum"));
						startActivity(intent);
						BindServiceCard.card.finish();
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	
	private final int SHOW_TOSAT = 0;
	
	/**
	 * 验证信息
	 * @return
	 */
	private boolean checkInfo() {
		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		
		String mobileText = mobile.getText().toString();
		String codeText = code.getText().toString();
		String nameText = name.getText().toString();
		String addressText = address.getText().toString();
		
		if (TextUtils.isEmpty(mobileText)) {
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(mobileText)) {
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(nameText)) {
				msg.obj = getString(R.string.name_is_null);
				mHandler.sendMessage(msg);
				return false;
		}else if (TextUtils.isEmpty(addressText)) {
				msg.obj = getString(R.string.addr_detail_is_null);
				mHandler.sendMessage(msg);
				return false;
			}
		else if (TextUtils.isEmpty(codeText)) {
			msg.obj = getString(R.string.ver_code_is_null);
			mHandler.sendMessage(msg);
			return false;
		}else if(!cb_agree.isChecked()){
			msg.obj = getString(R.string.agreement);
			mHandler.sendMessage(msg);
			return false;
		}
		
		return true;
	}
	
	
	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT) ;
		}
		mToast.setText(msg);
		mToast.show();
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

	/**
	 * 获取验证码
	 */	
	private void getVerifyCode() {
		String phone = mobile.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		RequstClient.getRegeditCode(phone, "2",
				new CustomResponseHandler(ApplyServiceCard.this) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						try {
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

}
