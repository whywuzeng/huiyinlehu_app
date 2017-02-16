package com.huiyin.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreferenceUtil {

	public static String PREFERENCE_NAME = "setting";
	private SharedPreferences mPreferences;
	private static SettingPreferenceUtil instance;

	public static synchronized SettingPreferenceUtil getInstance(Context context) {
		if (instance == null) {
			instance = new SettingPreferenceUtil(context);
		}
		return instance;
	}

	private SettingPreferenceUtil(Context context) {
		mPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	public SettingItem getSettingItem() {
		SettingItem mSettingItem = new SettingItem();
		mSettingItem.message = isMessage();
		mSettingItem.messageTime = isMessageTime();
		mSettingItem.messageSound = isMessageSound();
		mSettingItem.interType = getInterType();
		// mSettingItem.normal = isNormal();
		// mSettingItem.high_quality = isHigh_quality();
		// mSettingItem.auto = isAuto();
		return mSettingItem;
	}

	public void setSettingItem(SettingItem mSettingItem) {
		android.content.SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean("message", mSettingItem.message);
		editor.putBoolean("messageTime", mSettingItem.messageTime);
		editor.putBoolean("messageSound", mSettingItem.messageSound);
		editor.putInt("interType", mSettingItem.interType);
		// editor.putBoolean("normal", mSettingItem.normal);
		// editor.putBoolean("high_quality", mSettingItem.high_quality);
		// editor.putBoolean("auto", mSettingItem.auto);
		editor.commit();
		
	}

	public boolean isMessage() {
		return mPreferences.getBoolean("message", true);
	}

	public boolean isMessageTime() {
		return mPreferences.getBoolean("messageTime", true);
	}

	public boolean isMessageSound() {
		return mPreferences.getBoolean("messageSound", true);
	}

	public int getInterType() {
		return mPreferences.getInt("interType", 3);
	}

	public boolean isNormal() {
		return mPreferences.getBoolean("normal", false);
	}

	public boolean isHigh_quality() {
		return mPreferences.getBoolean("high_quality", false);
	}

	public boolean isAuto() {
		return mPreferences.getBoolean("auto", true);
	}

	public class SettingItem {
		public boolean message;
		public boolean messageTime;
		public boolean messageSound;
		public int interType;// 1:normal 2:high_quality 3:auto
		// public boolean normal;
		// public boolean high_quality;
		// public boolean auto;

		public boolean isMessage() {
			return message;
		}

		public boolean isMessageTime() {
			return messageTime;
		}

		public boolean isMessageSound() {
			return messageSound;
		}

		public boolean isNormal() {
			return interType == 1;
		}

		public boolean isHigh_quality() {
			return interType == 2;
		}

		public boolean isAuto() {
			return interType == 3;
		}

	}
}
