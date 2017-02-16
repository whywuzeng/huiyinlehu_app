package com.huiyin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Preference数据处理类
 * 
 * @author
 * */
public class PreferenceUtil {

	public SharedPreferences preference;

	// ----------------Preference-------------------
	public static String PRFERENCE_IS_FIRST_START = "is_first_start";
	public static String PRFERENCE_IS_FIRST_SET = "is_first_set";
	public static String PRFERENCE_HOTLINE = "hotline";
	
	public static String PRFERENCE_IS_SEND_MOBILE_INFO = "is_send_mobile_info";
	
	public static String PRFERENCE_RONGICLOUD_TOKEN = "rongicloud_token";
	
	public static String PRFERENCE_QQ_OPENID = "qqOpenid";
	
	public static String PRFERENCE_SINA_UID = "sinaUid";
	
	public static String PRFERENCE_WECHAT_UID = "wxOpenid";
	
	public static String LAST_LOGIN_TIME="lastTime";
	
	private static PreferenceUtil instance;

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 *            context
	 * */
	public static synchronized PreferenceUtil getInstance(Context context) {
		if (instance == null) {
			instance = new PreferenceUtil(context);
		}
		return instance;
	}

	public PreferenceUtil(Context context) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setFirstStart(boolean isfirst) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(PRFERENCE_IS_FIRST_START, isfirst);
		editor.commit();
	}

	public boolean isFirstStart() {
		return preference.getBoolean(PRFERENCE_IS_FIRST_START, true);
	}

	public void setFirstSet(boolean isfirst) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(PRFERENCE_IS_FIRST_SET, isfirst);
		editor.commit();
	}

	public boolean isFirstSet() {
		return preference.getBoolean(PRFERENCE_IS_FIRST_SET, true);
	}
	
	public void setSendMobileInfo(boolean isSend) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(PRFERENCE_IS_SEND_MOBILE_INFO, isSend);
		editor.commit();
	}

	public boolean isSendMobileInfo() {
		return preference.getBoolean(PRFERENCE_IS_SEND_MOBILE_INFO, false);
	}

	public void setHotLine(String telPhone) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_HOTLINE, telPhone);
		editor.commit();
	}
	
	
	public String getToken() {
		return preference.getString(PRFERENCE_RONGICLOUD_TOKEN, "");
	}

	public void setToken(String token) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_RONGICLOUD_TOKEN, token);
		editor.commit();
	}

	public String getHotLine() {
		return preference.getString(PRFERENCE_HOTLINE, null);
	}
	
	/**保存QQopenid
	 * @param openid
	 */
	public void setQQOpenid(String openid){
		Editor editor = preference.edit();
		editor.putString(PRFERENCE_QQ_OPENID,openid);
		editor.commit();
	}
	
	/**获取QQopenid
	 * @return
	 */
	public String getQQOpenid(){
		return preference.getString(PRFERENCE_QQ_OPENID, "");
	}
	
	/**保存SinaUid
	 * @param openid
	 */
	public void setSinaUid(String uid){
		Editor editor = preference.edit();
		editor.putString(PRFERENCE_SINA_UID,uid);
		editor.commit();
	}
	
	/**获取SinaUid
	 * @return
	 */
	public String getSinaUid(){
		return preference.getString(PRFERENCE_SINA_UID, "");
	}
	
	/**保存用户登录的时间
	 * @param openid
	 */
	public void setUserLastLoginTime(String lastTime){
		Editor editor = preference.edit();
		editor.putString(LAST_LOGIN_TIME,lastTime);
		editor.commit();
	}
	
	/**获取用户登录的时间
	 * @return
	 */
	public String getUserLastLoginTime(){
		return preference.getString(LAST_LOGIN_TIME, "");
	}
	
	public String getWeChatOpenid() {
		return preference.getString(PRFERENCE_WECHAT_UID, "");
	}

	public void setWeChatOpenid(String Openid) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_WECHAT_UID, Openid);
		editor.commit();
	}
}
