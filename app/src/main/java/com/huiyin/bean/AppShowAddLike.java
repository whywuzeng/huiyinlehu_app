package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class AppShowAddLike implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String msg;
	
	public static AppShowAddLike explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowAddLike experLightBean = gson.fromJson(json, AppShowAddLike.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddLike", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
