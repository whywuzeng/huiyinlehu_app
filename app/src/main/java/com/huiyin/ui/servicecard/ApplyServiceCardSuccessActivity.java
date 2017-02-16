package com.huiyin.ui.servicecard;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;

public class ApplyServiceCardSuccessActivity extends Activity {
	private Context mContext;

	private TextView left_ib, middle_title_tv;

	private TextView card_num;
	private TextView card_password;
	private ViewSwitcher mviewSwitcher;
	private Button card_recharge;
	private Button updata_card_password;
	private EditText oldpwd;
	private EditText newpwd;
	private EditText affirm_pwd;
	private Button btn_bind;

	private String userId;
	
	private String cardNum;
	private String cardPwd;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_service_card_layout);
		mContext = this;
		userId = AppContext.getInstance().getUserId();
		findView();
		setListener();
		
		cardNum = getIntent().getStringExtra("cardNum");
		cardPwd = getIntent().getStringExtra("cardPwd");

		card_num.setText(cardNum);
		card_password.setText(cardPwd);
	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("便民生活服务卡");

		card_num = (TextView) findViewById(R.id.card_num);
		card_password = (TextView) findViewById(R.id.card_password);

		mviewSwitcher = (ViewSwitcher) findViewById(R.id.mviewSwitcher);
		card_recharge = (Button) findViewById(R.id.card_recharge);
		updata_card_password = (Button) findViewById(R.id.updata_card_password);

		oldpwd = (EditText) findViewById(R.id.oldpwd);
		newpwd = (EditText) findViewById(R.id.newpwd);
		affirm_pwd = (EditText) findViewById(R.id.affirm_pwd);
		btn_bind = (Button) findViewById(R.id.btn_bind);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		card_recharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, RechargeServiceCard.class);
				startActivity(i);
				finish();
			}
		});
		updata_card_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mviewSwitcher.setDisplayedChild(1);
			}
		});
		btn_bind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInfo()){
					initData();
				}
			}
		});
	}

	private void initData(){
		String oldpwdText = oldpwd.getText().toString();
		String newpwdText = newpwd.getText().toString();
		RequstClient.appUpdateCardPwd(AppContext.getInstance().getUserId(),AppContext.getInstance().getUserInfo().cardNum,oldpwdText,newpwdText, new CustomResponseHandler(this) {
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
					Toast.makeText(getBaseContext(), "修改成功",Toast.LENGTH_SHORT).show();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	
	private boolean checkInfo() {
		String oldpwdText = oldpwd.getText().toString();
		String newpwdText = newpwd.getText().toString();
		String affirmPwd = affirm_pwd.getText().toString();
		if (TextUtils.isEmpty(oldpwdText)) {
			Toast.makeText(mContext, getString(R.string.pwd_is_null), Toast.LENGTH_LONG).show();
			return false;
		} else  if (TextUtils.isEmpty(newpwdText)) {
			Toast.makeText(mContext, getString(R.string.newpwd_is_null), Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(affirmPwd) || !affirmPwd.equals(newpwdText)) {
			Toast.makeText(mContext, getString(R.string.affirm_pwd_not_same), Toast.LENGTH_LONG).show();
			return false;
		} 
		
		return true;
	}
}
