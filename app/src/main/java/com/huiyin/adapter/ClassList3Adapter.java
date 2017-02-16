package com.huiyin.adapter;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.bean.ClassListLevel3Bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClassList3Adapter extends BaseAdapter {

	private Context context;
	private ClassListLevel3Bean bean3;

	public ClassList3Adapter(Context context, ClassListLevel3Bean bean3) {
		this.context = context;
		this.bean3 = bean3;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bean3.category.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bean3.category.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (v != null) {
			holder = (ViewHolder) v.getTag();
		} else {
			v = LayoutInflater.from(context).inflate(R.layout.class_list_level3_lv_ll, null);
			holder = new ViewHolder();
			holder.class_list_level3_tv = (TextView) v.findViewById(R.id.class_list_level3_tv);

			v.setTag(holder);
		}
		// 三级分类列表的标题
		if (bean3 != null) {
			if (bean3.category.get(position).CATEGORY_NAME != null) {
				holder.class_list_level3_tv.setText(bean3.category.get(position).CATEGORY_NAME);
			}
		} else {
			holder.class_list_level3_tv.setText("");
		}
		return v;
	}

	private static class ViewHolder {
		private TextView class_list_level3_tv;
	}
}
