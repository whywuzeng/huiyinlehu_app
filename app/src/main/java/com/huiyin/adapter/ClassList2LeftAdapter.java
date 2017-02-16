package com.huiyin.adapter;

import java.util.List;

import com.huiyin.R;
import com.huiyin.bean.ClassListLevel1Bean;
import com.huiyin.bean.ClassListLevel1Bean.ItemList;
import com.huiyin.bean.ClassListLevel2Bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassList2LeftAdapter extends BaseAdapter {

	private int index = 0;
	private Context context;
	private List<ItemList> list;
	public ClassList2LeftAdapter(Context context,List<ItemList> list) {
		this.context = context;
		this.list=list;
	}

	public void setPosition(int index) {
		this.index = index;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
					R.layout.class_list_level2_ll_lv_left, null);
			holder.class_list_level2_item_title = (TextView) v
					.findViewById(R.id.class_list_level2_item_title);
			holder.class_list_level2_left_r_iv=(ImageView) v.findViewById(R.id.class_list_level2_left_r_iv);
			v.setTag(holder);
		}
		if (index == arg0) {
			holder.class_list_level2_left_r_iv.setImageResource(R.drawable.c_left_select);
		} else {
			holder.class_list_level2_left_r_iv.setImageDrawable(null);
		}
		holder.class_list_level2_item_title.setText(list.get(arg0).ONE_PARENT_NAME);
		return v;
	}

	private static class ViewHolder {
		private TextView class_list_level2_item_title;
		private ImageView class_list_level2_left_r_iv;
	}

}
