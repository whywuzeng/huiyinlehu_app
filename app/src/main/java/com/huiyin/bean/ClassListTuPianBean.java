package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

public class ClassListTuPianBean {
	public String type;
	public String msg;
	public String totalPageNum;
	public List<CommodityList> commodityList = new ArrayList<CommodityList>();

	public class CommodityList {
		public String COMMODITY_IMAGE_PATH;
		public String COMMODITY_NAME;
		public String ID;
		public String NUM;
		public String PRICE;
		public String REFERENCE_PRICE;
		public String COUNTREVIEW;
		public String SALES_VOLUME;

		public int MARK;
		public String PROMOTIONS_PRICE;

		public String REVIEW_NUMBER;

	}
}
