package com.huiyin.ui.shoppingcar;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class OrderPriceBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	public float totalPrice;
	public float perpaidCard;

	public static OrderPriceBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			OrderPriceBean experLightBean = gson.fromJson(json, OrderPriceBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}
