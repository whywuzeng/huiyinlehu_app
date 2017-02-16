package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ClassListLevel1Bean;
import com.huiyin.bean.ClassListLevel1Bean.TWOLIST;
import com.huiyin.bean.ClassListLevel2Bean;
import com.huiyin.bean.ClassListLevel2Bean.Item;
import com.huiyin.bean.ClassListLevel2Bean.ItemList;
import com.huiyin.utils.ImageManager;

public class ClassList2RightAdapter extends BaseAdapter {
	private int index = 0;
	private Context context;
	private List<TWOLIST>  list;
	private ClassListLevel1Bean bean1;
	private ClassListLevel2Bean bean2;
	public ClassList2RightAdapter(Context context,List<TWOLIST> list,ClassListLevel1Bean bean1,ClassListLevel2Bean bean2) {
		this.context = context;
		this.list=list;
		this.bean1=bean1;
		this.bean2=bean2;
	}

	public void setPosition(int index) {
		this.index = index;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (v != null) {
			holder = (ViewHolder) v.getTag();
		} else {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(
					R.layout.class_list_level2_ll_lv_right, null);
			holder.class_list_level2_item_title_right1 = (TextView) v
					.findViewById(R.id.class_list_level2_item_title_right1);
			holder.class_list_level2_item_title_right2 = (TextView) v
					.findViewById(R.id.class_list_level2_item_title_right2);
			holder.class_list_level2_ll_img = (ImageView) v
					.findViewById(R.id.class_list_level2_ll_img);
			v.setTag(holder);
		}

//		holder.class_list_level2_item_title_right1.setText(list.get(arg0).TWO_NAME);

//		// 获取图片的路径
//		ItemList itemList =list.get(arg0).;
//		String ICON_PATH = itemList.ICON_PATH;
//		// 加载图片
//		ImageManager.Load(ICON_PATH, holder.class_list_level2_ll_img);
//		//获取三级目录的名称
//		List<Item> tHREELIST = itemList.THREELIST;
//		String THREEE_NAME = "";
//		for (int i = 0; i < tHREELIST.size(); i++) {
//			THREEE_NAME += tHREELIST.get(i).THREEE_NAME + "/";
//			if (i == tHREELIST.size()-1) {
//				THREEE_NAME = THREEE_NAME
//						.substring(0, THREEE_NAME.length() - 1);
//			}
//		}
//		holder.class_list_level2_item_title_right2.setText(THREEE_NAME);

		return v;
	}

	private static class ViewHolder {
		private TextView class_list_level2_item_title_right1;
		private TextView class_list_level2_item_title_right2;
		private ImageView class_list_level2_ll_img;
	}

}
