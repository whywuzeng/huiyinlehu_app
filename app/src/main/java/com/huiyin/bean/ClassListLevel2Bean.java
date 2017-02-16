package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.huiyin.bean.ClassListLevel1Bean.ItemList;

/**二级分类列表的数据的bean
 * @author smart
 *
 */
public class ClassListLevel2Bean {
	public String msg;
	public String type;
	public List<ItemList>  twoCategories=new ArrayList<ItemList>();
	public class ItemList{
		public String ICON_PATH;
		public String ID;
		public String NUM;
		public String ONE_ICON_PATH;
		public String ONE_ID;
		public String ONE_PARENT_NAME;
		public List<Item> twoList=new ArrayList<Item>();
	}
	public class Item {
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
