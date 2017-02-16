package com.huiyin.bean;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class TodayHeadLine extends BaseBean {

	public Headlines headlines;
	public List<Commodity> commodity;
	public List<HeadLineTitle> title;

	public class Headlines {
		public String CONTENT;// 编辑器内容
		public String IMAGE;// binner图URL
		public String TITLE;// 今日头条标题

	}

	public class Commodity {
		public String PRICE;// 商品价格
		public int ID;// 商品id
		public int SALES_VOLUME;//
		public String COMMODITY_IMAGE_PATH;// 图片URL
		public String COMMODITY_NAME;// 商品名字
	}

	public class HeadLineTitle {
		public String TIME;// "2014-10-23 12:10:00",
		public int ID;// 头条id
		public String TITLE;// 往日头条标题
	}

	public static TodayHeadLine explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			TodayHeadLine experLightBean = gson.fromJson(json, TodayHeadLine.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("TodayHeadLine", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}

	@Override
	public String toString() {
		return "TodayHeadLine [headlines=" + headlines + ", commodity=" + commodity + ", title=" + title + "]";
	}

}
