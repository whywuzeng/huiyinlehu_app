package com.huiyin.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.DataCleanManager;
import com.huiyin.utils.SettingPreferenceUtil;
import com.huiyin.utils.Utils;
import com.huiyin.utils.SettingPreferenceUtil.SettingItem;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class SettingActivity extends BaseActivity {

	// Content View Elements
	private TextView left_ib, middle_title_tv, right_ib;

	private RelativeLayout message;
	private TextView message_textview;
	private CheckBox message_checkBox;
	private RelativeLayout message_time;
	private CheckBox message_time_checkBox;
	private RelativeLayout message_sound;
	private CheckBox message_sound_checkBox;

	private RelativeLayout normal;
	private CheckBox normal_checkBox;
	private RelativeLayout high_quality;
	private CheckBox high_quality_checkBox;
	private RelativeLayout auto;
	private CheckBox auto_checkBox;
	private TextView clean_image;

	// End Of Content View Elements
	private Context mContext;
	private SettingItem mSettingItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mContext = this;
		mSettingItem = SettingPreferenceUtil.getInstance(mContext).getSettingItem();
		initView();
		setListener();
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
		middle_title_tv.setText("设置");

		message = (RelativeLayout) findViewById(R.id.receive_message);
		message_textview = (TextView) findViewById(R.id.receive_message_textview);
		if (!mSettingItem.isMessage()) {
			message_textview.setText("已关闭消息推送通知");
		}
		message_checkBox = (CheckBox) findViewById(R.id.receive_message_checkBox);
		message_checkBox.setChecked(mSettingItem.isMessage());
		message_time = (RelativeLayout) findViewById(R.id.receive_message_time);
		message_time_checkBox = (CheckBox) findViewById(R.id.receive_message_time_checkBox);
		message_time_checkBox.setChecked(mSettingItem.isMessageTime());
		message_sound = (RelativeLayout) findViewById(R.id.receive_message_sound);
		message_sound_checkBox = (CheckBox) findViewById(R.id.receive_message_sound_checkBox);
		message_sound_checkBox.setChecked(mSettingItem.isMessageSound());
		normal = (RelativeLayout) findViewById(R.id.internet_normal);
		normal_checkBox = (CheckBox) findViewById(R.id.internet_normal_checkBox);
		normal_checkBox.setChecked(mSettingItem.isNormal());
		high_quality = (RelativeLayout) findViewById(R.id.internet_high_quality);
		high_quality_checkBox = (CheckBox) findViewById(R.id.internet_high_quality_checkBox);
		high_quality_checkBox.setChecked(mSettingItem.isHigh_quality());
		auto = (RelativeLayout) findViewById(R.id.internet_auto);
		auto_checkBox = (CheckBox) findViewById(R.id.internet_auto_checkBox);
		auto_checkBox.setChecked(mSettingItem.isAuto());
		clean_image = (TextView) findViewById(R.id.clean_image);

	}

	private void setListener() {
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				message_checkBox.setChecked(message_checkBox.isChecked() ? false : true);
			}
		});
		message_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSettingItem.message = isChecked;
				if (!isChecked) {
					message_time_checkBox.setChecked(false);
					message_sound_checkBox.setChecked(false);
					message_time_checkBox.setClickable(false);
					message_sound_checkBox.setClickable(false);
					message_time.setClickable(false);
					message_sound.setClickable(false);
					PushManager.stopWork(SettingActivity.this);// 关闭推送
					message_textview.setText("已关闭消息推送通知");
				} else {
					message_time_checkBox.setClickable(true);
					message_sound_checkBox.setClickable(true);
					message_time.setClickable(true);
					message_sound.setClickable(true);
					// 开启推送
					PushManager.startWork(SettingActivity.this, PushConstants.LOGIN_TYPE_API_KEY,
							Utils.getMetaValue(SettingActivity.this, "api_key"));
					message_textview.setText("已开启消息推送通知");
				}
			}
		});
		message_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				message_time_checkBox.setChecked(message_time_checkBox.isChecked() ? false : true);
			}
		});
		message_time_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSettingItem.messageTime = isChecked;
				if (isChecked)
					PushManager.setNoDisturbMode(SettingActivity.this, 23, 00, 06, 50);
				else
					PushManager.setNoDisturbMode(SettingActivity.this, 00, 01, 23, 59);
			}
		});
		message_sound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				message_sound_checkBox.setChecked(message_sound_checkBox.isChecked() ? false : true);
			}
		});
		message_sound_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSettingItem.messageSound = isChecked;
			}
		});
		normal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(normal_checkBox.isChecked())
					return;
				normal_checkBox.setChecked(normal_checkBox.isChecked() ? false : true);
			}
		});
		normal_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mSettingItem.interType = 1;
					high_quality_checkBox.setChecked(false);
					auto_checkBox.setChecked(false);
				}
			}
		});
		high_quality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(high_quality_checkBox.isChecked())
					return;
				high_quality_checkBox.setChecked(high_quality_checkBox.isChecked() ? false : true);
			}
		});
		high_quality_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mSettingItem.interType = 2;
					normal_checkBox.setChecked(false);
					auto_checkBox.setChecked(false);
				}
			}
		});
		auto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(auto_checkBox.isChecked())
					return;
				auto_checkBox.setChecked(auto_checkBox.isChecked() ? false : true);
			}
		});
		auto_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mSettingItem.interType = 3;
					normal_checkBox.setChecked(false);
					high_quality_checkBox.setChecked(false);
				}
			}
		});
		clean_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DataCleanManager.deleteFilesByDirectory(StorageUtils.getOwnCacheDirectory(getApplicationContext(),
						"huiyinlehu/cache/ImageCache"));
				DataCleanManager.deleteFilesByDirectory(StorageUtils.getOwnCacheDirectory(getApplicationContext(),
						"huiyinlehu/cache/tempImage"));
				Toast.makeText(mContext, "图片清除完毕", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onPause() {
		SettingPreferenceUtil.getInstance(mContext).setSettingItem(mSettingItem);
		super.onPause();
	}

}
