package com.huiyin.ui.user;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.UpdataInfo;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.utils.AppManager;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UpdateManager;
import com.huiyin.utils.UpdateVersionTool;

public class KefuCenterActivity extends BaseActivity {

	LinearLayout kefu_setting, kefu_online_help, kefu_online_msg,
			kefu_telephone, kefu_check_version, kefu_about,
			kefu_server_agreement;

	TextView left_ib, middle_title_tv;
	
	TextView tel;
	
	TextView versionCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kefu_center);

		initView();
	}

	private void initView() {

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}

		});

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("客服中心");

		kefu_setting = (LinearLayout) findViewById(R.id.kefu_setting);
		kefu_online_help = (LinearLayout) findViewById(R.id.kefu_online_help);
		kefu_online_msg = (LinearLayout) findViewById(R.id.kefu_online_msg);
		kefu_telephone = (LinearLayout) findViewById(R.id.kefu_telephone);
		tel = (TextView) findViewById(R.id.tel);
		tel.setText("电话客服："+PreferenceUtil.getInstance(mContext).getHotLine());
		kefu_check_version = (LinearLayout) findViewById(R.id.kefu_check_version);
		versionCode = (TextView) findViewById(R.id.kefu_version_code);
		versionCode.setText("检查更新 (V"+UpdateVersionTool.getVersionName(mContext)+")");
		kefu_about = (LinearLayout) findViewById(R.id.kefu_about);
		kefu_server_agreement = (LinearLayout) findViewById(R.id.kefu_server_agreement);

		KefuOnClickListener onClick = new KefuOnClickListener();

		kefu_setting.setOnClickListener(onClick);
		kefu_online_help.setOnClickListener(onClick);
		kefu_online_msg.setOnClickListener(onClick);
		kefu_telephone.setOnClickListener(onClick);
		kefu_check_version.setOnClickListener(onClick);
		kefu_about.setOnClickListener(onClick);
		kefu_server_agreement.setOnClickListener(onClick);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CommonConfrimCancelDialog.EXIT_APP_CODE
				&& resultCode == RESULT_OK) {
			AppManager.getAppManager().AppExit(KefuCenterActivity.this);
		}
	}

	class KefuOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {

			case R.id.kefu_setting:
				Intent i = new Intent();
				i.setClass(KefuCenterActivity.this, SettingActivity.class);
				startActivity(i);
				break;
			case R.id.kefu_online_help:
				Intent intent = new Intent();
				intent.setClass(KefuCenterActivity.this,
						OnLineHelpActivity.class);
				startActivity(intent);
				break;
			case R.id.kefu_online_msg:
				Intent msg_intent = new Intent();
				msg_intent.setClass(KefuCenterActivity.this,
						OnLineMsgActivity.class);
				startActivity(msg_intent);
				break;
			case R.id.kefu_telephone:
				String telPhone = tel.getText().toString();
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(telPhone);
				telPhone = m.replaceAll("").trim();
				Log.i("解析结果", "====" + telPhone);
				
				if(StringUtils.isBlank(telPhone))
					return;
				Intent d_intent = new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:"+telPhone));
				d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(d_intent);
				break;
			case R.id.kefu_check_version:
				requestVersion();
				break;
			case R.id.kefu_about:
				Intent about_intent = new Intent(KefuCenterActivity.this,AboutActivity.class);
//				Intent about_intent = new Intent(KefuCenterActivity.this,IntroduceActivity.class);
//				about_intent.putExtra("id", -20);
				startActivity(about_intent);
				break;
			case R.id.kefu_server_agreement:
				Intent c_intent = new Intent(KefuCenterActivity.this,IntroduceActivity.class);
				c_intent.putExtra("id", 1);
				startActivity(c_intent);
				break;
			default:
				break;
			}
		}

	}

	/***
	 * 检测版本
	 * */
	private void requestVersion() {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				analyticalVersionData(content);
			}
		};
		RequstClient.getVersion(handler);
	}

	/***
	 * 解析数据
	 * */
	private void analyticalVersionData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				String url = roots.getString("editionUrl");
				String versionName = roots.getString("editionName");
				String versionVersion = roots.getString("editionExplain");
				
				int versionCode = Integer.valueOf(roots.getString("edition"));
				UpdataInfo updateInfo = new UpdataInfo();
				updateInfo.setUrl(url);
				updateInfo.setVersion(versionName);
				updateInfo.setVersionCode(versionCode);
				updateInfo.setDescription(versionVersion);
				
				updateApk(updateInfo);

			} else {
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void updateApk(final UpdataInfo updateInfo) {
		if (UpdateVersionTool.getVersionCode(this) >= updateInfo
				.getVersionCode()) {
			Toast.makeText(mContext, "已是最新版本！", Toast.LENGTH_SHORT).show();
			return;
		} else {

			UpdateManager manager = new UpdateManager(this, "huiyin",
					updateInfo.getUrl());
			manager.checkUpdate(updateInfo.getVersionCode(), updateInfo.getDescription());

		}
	}
}
