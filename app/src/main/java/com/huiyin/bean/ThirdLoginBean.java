package com.huiyin.bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.UserInfo;

public class ThirdLoginBean extends BaseBean {
	private static final long serialVersionUID = 1L;

	public UserInfo user;

	public static ThirdLoginBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			ThirdLoginBean experLightBean = gson.fromJson(json,
					ThirdLoginBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
