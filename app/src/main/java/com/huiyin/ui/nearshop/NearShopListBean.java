package com.huiyin.ui.nearshop;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class NearShopListBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public ArrayList<StoreListItem> nearbyStoreList;

	public ArrayList<StoreListItem> getNearbyStoreList() {
		return nearbyStoreList;
	}

	public void setNearbyStoreList(ArrayList<StoreListItem> nearbyStoreList) {
		this.nearbyStoreList = nearbyStoreList;
	}

	public static NearShopListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			NearShopListBean experLightBean = gson.fromJson(json,
					NearShopListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
