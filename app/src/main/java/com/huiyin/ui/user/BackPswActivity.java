package com.huiyin.ui.user;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

public class BackPswActivity extends BaseActivity {

	private final static String TAG = "BackPswActivity";
	TextView back_psw_commit, left_ib, middle_title_tv, right_ib;
	TextView get_code_tv, register_phone;
	private Handler mHandler;
	private Toast mToast;
	private static final int SHOW_TOSAT = 0;
	String registerPhone;
	private boolean isSuccess = false;// 验证是否通过
	RelativeLayout msg_mm_again_layout, msg_mm_layout;
	LinearLayout msg_yz_layout;
	Timer mTimer = null;
	int sec = 60;

	EditText back_pwd_again, back_pwd, registr_code_id;
	String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_psw_layout);

		initView();

	}

	private void initView() {

		back_pwd_again = (EditText) findViewById(R.id.back_pwd_again);
		back_pwd = (EditText) findViewById(R.id.back_pwd);
		back_psw_commit = (TextView) findViewById(R.id.back_psw_commit);
		back_psw_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isSuccess) {
					if (checkCode()) {
						postValidateCode(registerPhone, code);
					}
				} else if (checkInfo()) {
					postNewPsw();
				}
			}
		});

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}

		});

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("找回密码");

		registr_code_id = (EditText) findViewById(R.id.registr_code_id);

		right_ib = (TextView) findViewById(R.id.ab_right);
		right_ib.setBackgroundColor(getResources().getColor(R.color.orange));
		right_ib.setText("注册");
		right_ib.setBackgroundColor(getResources().getColor(R.color.index_red));
		right_ib.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(BackPswActivity.this, RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

		get_code_tv = (TextView) findViewById(R.id.get_code_tv);
		register_phone = (EditText) findViewById(R.id.register_phone);
		get_code_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkVerifyInfo()) {
					getVerifyCode();
				}

			}
		});

		msg_mm_again_layout = (RelativeLayout) findViewById(R.id.msg_mm_again_layout);
		msg_mm_layout = (RelativeLayout) findViewById(R.id.msg_mm_layout);
		msg_yz_layout = (LinearLayout) findViewById(R.id.msg_yz_layout);

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

	}

	private boolean checkInfo() {

		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		/* 手机注册 */
		String phoneNumber = register_phone.getText().toString();
		String psw_again = back_pwd_again.getText().toString();
		String psw = back_pwd.getText().toString();
		// String code = registr_code_id.getText().toString();

		if (TextUtils.isEmpty(phoneNumber)) {
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(phoneNumber)) {
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(psw_again) || !psw_again.equals(psw)) {
			msg.obj = getString(R.string.affirm_pwd_not_same);
			mHandler.sendMessage(msg);
			return false;
		}

		return true;
	}

	private void postNewPsw() {

		String phoneNumber = register_phone.getText().toString();
		String psw = back_pwd.getText().toString();

		RequstClient.postFindPsw(phoneNumber, psw, new CustomResponseHandler(
				this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postFindPsw:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					// showToast(getString(R.string.modify_success));
					// AppContext.MAIN_TASK = AppContext.LEHU;
					// Intent i = new Intent();
					// i.setClass(BackPswActivity.this, MainActivity.class);
					// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// startActivity(i);
					Intent i = new Intent(BackPswActivity.this, BackPswSuccessActivity.class);
					startActivity(i);
					BackPswActivity.this.finish();
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

	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

	private void setMakeNewPswLayout() {
		msg_mm_again_layout.setVisibility(View.VISIBLE);
		msg_mm_layout.setVisibility(View.VISIBLE);
		msg_yz_layout.setVisibility(View.GONE);
		// 失焦不可编辑
		register_phone.setFocusable(false);
	}

	/**
	 * 验证信息
	 * 
	 * @return
	 */
	private boolean checkVerifyInfo() {

		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		/* 手机注册 */
		registerPhone = register_phone.getText().toString();
		if (TextUtils.isEmpty(registerPhone)) {
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(registerPhone)) {
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		}

		return true;
	}

	/**
	 * 验证
	 * 
	 * @return
	 */
	private boolean checkCode() {

		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		/* 手机注册 */
		registerPhone = register_phone.getText().toString();
		code = registr_code_id.getText().toString();

		if (TextUtils.isEmpty(registerPhone)) {
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(registerPhone)) {
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(code)) {
			msg.obj = getString(R.string.ver_code_is_null);
			mHandler.sendMessage(msg);
			return false;
		}

		return true;
	}

	/**
	 * 获取验证码
	 */

	private void getVerifyCode() {

		RequstClient.getRegeditCode(registerPhone, "1",
				new CustomResponseHandler(BackPswActivity.this) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							LogUtil.i(TAG, "getRegeditCode:" + content);
							JSONObject result = new JSONObject(content);
							if (!result.getString("type").equals("1")) {

								String msg = result.getString("msg");
								UIHelper.showToast(msg);

							} else {
								showResendTip();
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
	 * 短信提醒
	 */
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

	/***
	 * 验证验证码
	 */
	private void postValidateCode(String phone, String code) {

		RequstClient.postCode(phone, code, new CustomResponseHandler(
				BackPswActivity.this) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					LogUtil.i(TAG, "postValidateCode:" + content);
					JSONObject result = new JSONObject(content);
					if (!result.getString("type").equals("1")) {
						String errorMsg = result.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					String msg = result.getString("msg");
					UIHelper.showToast(msg);
					isSuccess = true;
					setMakeNewPswLayout();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}