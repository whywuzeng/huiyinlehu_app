package com.huiyin.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//分类列表一的json数据
public class ClassListLevel1Bean{
	public String type;
	public String msg;
	public List<ItemList> oneCategories = new ArrayList<ItemList>();

	public class ItemList {
		public String ICON_PATH;
		public String ID;
		public String NUM;
		public String ONE_ICON_PATH;
		public String ONE_ID;
		public String ONE_PARENT_NAME;
		public List<TWOLIST> twoList=new ArrayList<TWOLIST>();
	}

	public class TWOLIST {
		public String CATEGORY_NAME;
		public String ICON_PATH;
		public String ID;
		public String NUM;
		public String ONE_PARENT_CLASS;
		public List<THREELIST> THREELIST=new ArrayList<THREELIST>();
	}
	
	public class THREELIST{
		public String THREEE_ID;
		public String THREEE_NAME;
	}
}
