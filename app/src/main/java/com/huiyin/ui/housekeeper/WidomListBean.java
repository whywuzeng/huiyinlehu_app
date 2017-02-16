package com.huiyin.ui.housekeeper;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class WidomListBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private List<WidomItem> widomJson;

	public List<WidomItem> getWidomJson() {
		return widomJson;
	}

	public void setWidomJson(List<WidomItem> widomJson) {
		this.widomJson = widomJson;
	}

	public static WidomListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			WidomListBean experLightBean = gson.fromJson(json, WidomListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
