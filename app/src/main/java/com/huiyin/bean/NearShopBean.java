package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class NearShopBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public ArrayList<ShopMention> nearbyList;

	public class ShopMention {
		public double distance;// 距离
		public String NAME;//
		public int ID;// 店铺ID
		public double LONGITUDE;// 经度
		public double LATITUDE;// 纬度
	}

	public static NearShopBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			NearShopBean experLightBean = gson.fromJson(json,
					NearShopBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
