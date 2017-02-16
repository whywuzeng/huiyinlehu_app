package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class PrefectureThree extends BaseBean {
	
	public mList list;
	
	public class mList{
		public ArrayList<Prefecture> prefecture;
		public ArrayList<Suibian> suibian;
	}
	
	public class Suibian{
		public String IMG1;//小图1
		public String IMG2;//小图2
		public int ID2;//小图2id
		public int ID1;//小图1id
		public int datuId;//大图id
		public String datu;//大图
	}
	
	public class Prefecture{
		public String BIG_IMG;//binner
		public String COMMODITY_ID;
		public String BIG_URL;
		public String LAYOUT;
	}
	
	public static PrefectureThree explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			PrefectureThree experLightBean = gson.fromJson(json, PrefectureThree.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("Prefecture1", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
