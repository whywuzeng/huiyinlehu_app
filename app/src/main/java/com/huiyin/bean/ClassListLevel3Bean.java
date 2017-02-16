package com.huiyin.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//分类列表一的json数据
public class ClassListLevel3Bean{
	public String type;
	public String msg;
	public List<Category> category = new ArrayList<Category>();
	public List<Hotcommodity> hotcommodity = new ArrayList<Hotcommodity>();

	public class Category {
		public String CATEGORY_NAME;
		public String ID;
		public String ONE_PARENT_CLASS;
		public String TWO_PARENT_CLASS;
	}

	public class Hotcommodity {
		public String COMMODITY_NAME;
		public String COMMODITY_IMAGE_PATH;
		public String ID;
		public String PRICE;
	}
}
