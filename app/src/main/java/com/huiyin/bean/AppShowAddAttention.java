package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class AppShowAddAttention implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String msg;
	
	public static AppShowAddAttention explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowAddAttention experLightBean = gson.fromJson(json, AppShowAddAttention.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAttention", e.toString());
			//Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
