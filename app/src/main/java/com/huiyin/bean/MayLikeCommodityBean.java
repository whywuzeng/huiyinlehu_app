package com.huiyin.bean;

import java.util.ArrayList;


public class MayLikeCommodityBean {
	public ArrayList<SearchBean> commodity = new ArrayList<SearchBean>();
	public class SearchBean {
		public String PRICE;// 商品价格
		public String ID;// 商品ID
		public String COMMODITY_IMAGE_PATH;// 图片
		public String NUM;
	}
}
