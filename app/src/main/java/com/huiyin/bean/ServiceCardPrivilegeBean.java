package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ServiceCardPrivilegeBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public ArrayList<PrivilegeItem> data;

	public class PrivilegeItem {
		public int id;// （优惠活动ID）
		public float money;
		public String name;// （名称）
		public String rechargeName;// （活动名称）
	}

	public static ServiceCardPrivilegeBean explainJson(String json,
			Context context) {

		Gson gson = new Gson();
		try {
			ServiceCardPrivilegeBean experLightBean = gson.fromJson(json,
					ServiceCardPrivilegeBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
