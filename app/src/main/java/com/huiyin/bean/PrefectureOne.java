package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class PrefectureOne extends BaseBean {
	
	public mList list;
	
	public class mList{
		public ArrayList<Commodity> pageBean;
		public ArrayList<Prefecture> prefecture;
	}
	
	public class Commodity{
		public String PRICE;//商品价格
		public int ID;
		public int SALES_VOLUME;
		public String COMMODITY_IMAGE_PATH;//商品图片
		public int NUM;
		public String COMMODITY_NAME;//商品名字
	}
	
	public class Prefecture{
		public String BIG_IMG;//binner
		public String COMMODITY_ID;
		public String BIG_URL;
		public String LAYOUT;
	}
	
	public static PrefectureOne explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			PrefectureOne experLightBean = gson.fromJson(json, PrefectureOne.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("Prefecture1", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
