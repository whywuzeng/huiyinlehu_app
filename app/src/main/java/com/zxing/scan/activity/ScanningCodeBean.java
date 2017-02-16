package com.zxing.scan.activity;

import java.io.Serializable;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class ScanningCodeBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public String commodityId;// 商品ID

	public static ScanningCodeBean explainJson(String json) {

		Gson gson = new Gson();
		try {
			ScanningCodeBean experLightBean = gson.fromJson(json,
					ScanningCodeBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("ScanningCodeBean", e.toString());
			return null;
		}
	}
}
