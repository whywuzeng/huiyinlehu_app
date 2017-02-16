package com.huiyin.ui.servicecard;

import org.apache.http.Header;
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

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;

public class UpdateCardPwd extends BaseActivity {

	TextView left_ib, middle_title_tv;
	private EditText oldpwd;
	private EditText newpwd;
	private EditText affirm_pwd;

	private Button btn_bind;

	private Toast mToast;
	private Handler mHandler;
	private final int SHOW_TOSAT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_cardpwd_layout);
		initView();
		setListener();
	}

	private void initView() {
		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("便民生活服务卡密码修改");

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		oldpwd = (EditText) findViewById(R.id.oldpwd);
		newpwd = (EditText) findViewById(R.id.newpwd);
		affirm_pwd = (EditText) findViewById(R.id.affirm_pwd);
		btn_bind = (Button) findViewById(R.id.btn_bind);
	}

	private void initData() {
		String oldpwdText = oldpwd.getText().toString();
		String newpwdText = newpwd.getText().toString();
		RequstClient.appUpdateCardPwd(AppContext.getInstance().getUserId(), AppContext.getInstance().getUserInfo().cardNum, oldpwdText,
				newpwdText, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(getBaseContext(), "修改成功", Toast.LENGTH_SHORT).show();
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
	}

	private void setListener() {
		btn_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkInfo()) {
					initData();
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
	}

	private boolean checkInfo() {
		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		String oldpwdText = oldpwd.getText().toString();
		String newpwdText = newpwd.getText().toString();
		String affirmPwd = affirm_pwd.getText().toString();

		if (TextUtils.isEmpty(oldpwdText)) {
			msg.obj = getString(R.string.pwd_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(newpwdText)) {
			msg.obj = getString(R.string.newpwd_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (newpwdText.length() < 6) {
			msg.obj = getString(R.string.pwd_is_six);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(affirmPwd) || !affirmPwd.equals(newpwdText)) {
			msg.obj = getString(R.string.affirm_pwd_not_same);
			mHandler.sendMessage(msg);
			return false;
		}

		return true;
	}

	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

}
