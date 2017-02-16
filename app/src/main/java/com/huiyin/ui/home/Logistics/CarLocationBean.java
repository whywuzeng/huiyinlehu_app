package com.huiyin.ui.home.Logistics;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class CarLocationBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public carPosition value;

	public class carPosition {
		int result;// "0", 标注位 （0表示成功）
		double longitude;// "119.602127", 经度
		double latitude;// "32.458374" 纬度

		public LatLng getLatLng() {
			try {
				return new LatLng(latitude, longitude);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static CarLocationBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			CarLocationBean experLightBean = gson.fromJson(json,
					CarLocationBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
