package com.huiyin.ui.servicecard;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
/**
 * 绑定便民服务卡
 * @author zhangshuaijun
 *
 */
public class BindServiceCard extends BaseActivity{
	
	TextView left_ib,middle_title_tv ;
	private EditText card_number;
	private EditText bindservice_pwd;
	private TextView phone;
	
	
	private Button btn_bind;
	private Button notcard_btn;
	
	private Boolean cardType = false;
	
	private Toast mToast;
	private   Handler mHandler ;
	private final int SHOW_TOSAT = 0;
	public static BindServiceCard card;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.bindservice_layout);
			card = this;
			initView();
			setListener();
			
		}
		
		private void initView(){
			middle_title_tv = (TextView)findViewById(R.id.ab_title);
			phone = (TextView)findViewById(R.id.phone);
			phone.setText(Html.fromHtml("提示:便民生活服务卡热线 <u>"+ PreferenceUtil.getInstance(mContext).getHotLine() +"</u>"));
			phone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent d_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+PreferenceUtil.getInstance(mContext).getHotLine()));
					d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(d_intent);
					
				}
			});
			middle_title_tv.setText("绑定便民生活服务卡");
			left_ib = (TextView)findViewById(R.id.ab_back);
			left_ib.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
						finish();
				}
			});
			
			card_number = (EditText) findViewById(R.id.card_number);
			bindservice_pwd = (EditText) findViewById(R.id.bindservice_pwd);
			
			btn_bind= (Button)findViewById(R.id.btn_bind);
			notcard_btn= (Button)findViewById(R.id.notcard_btn);
		}
	
	
	private void initData(){
		String cardText = card_number.getText().toString();
		String pwdText = bindservice_pwd.getText().toString();
		RequstClient.appBindCardInfo(AppContext.getInstance().getUserId(),cardText,pwdText, new CustomResponseHandler(this) {
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
					if(obj.has("token")){
						AppContext.getInstance().getUserInfo().token = obj.getString("token");
						AppContext.getInstance().getUserInfo().cardNum = card_number.getText().toString();
						AppContext.getInstance().getUserInfo().bdStatus = 1;
						AppContext.getInstance().getUserInfo().balance = obj.getDouble("balance");
						
						UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
						UserinfoPreferenceUtil.saveUserInfo(mContext, mUserInfo);
					}
					Intent intent = new Intent(BindServiceCard.this,BindSuccess.class);
					intent.putExtra("cardnum",card_number.getText().toString());
					intent.putExtra("balance",obj.getDouble("balance"));
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	private void setListener(){
		card_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!s.equals("")){
					cardType = false;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		card_number.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && !card_number.getText().toString().equals("")){
					if(!cardType){
					   validationCard();
					}
				}
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
		
		notcard_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(BindServiceCard.this,ApplyServiceCard.class));
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
	
	
	private void validationCard(){
		String cardText = card_number.getText().toString();
		RequstClient.appValidationCardInfo(cardText,AppContext.getInstance().getUserInfo().token, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						Toast.makeText(getBaseContext(), getString(R.string.card_disenable),Toast.LENGTH_SHORT).show();
						return;
					}
					cardType = true;
				} catch (Exception e) {
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
		
		String cardText = card_number.getText().toString();
		String pwdText = bindservice_pwd.getText().toString();
		
		if (TextUtils.isEmpty(cardText)) {
			msg.obj = getString(R.string.card_is_null);
			mHandler.sendMessage(msg);
			return false;
		}else 
			if (!cardType) {
			msg.obj = getString(R.string.card_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else
			if (StringUtils.isEmpty(pwdText)) {
			msg.obj = getString(R.string.pwd_is_null);
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
	
	
	

}
