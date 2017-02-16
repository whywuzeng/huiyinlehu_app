package com.huiyin.constants;

import android.os.Environment;

public class Config {

	public final static int PAGESIZE = 10;
	public static final String APP_SAVE_PATH = Environment
			.getExternalStorageDirectory().toString() + "/huiyin/";

	/**
	 * 接口加密key
	 */
	public static String KEY = "abc123wm456de789";// 密钥

	// public static String DES3_KEY = "huiyinjiadianluhuwang";
	// public static String DES3_IV = "20141103";

	public static String DES3_KEY = "eimseimseim@wm100$#365#$";
	public static String DES3_IV = "20141109";

}
