package com.huiyin.wxapi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class TokenBean {
	public String access_token;// 接口调用凭证
	public String expires_in;// access_token接口调用凭证超时时间，单位（秒）
	public String refresh_token;// 用户刷新access_token
	public String openid; // 授权用户唯一标识
	public String scope;// 用户授权的作用域，使用逗号（,）分隔
	public String errcode;
	public String errmsg;

	public static TokenBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			TokenBean experLightBean = gson.fromJson(json, TokenBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
