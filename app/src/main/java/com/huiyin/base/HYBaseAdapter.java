package com.huiyin.base;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class HYBaseAdapter <T> extends BaseAdapter {

	protected List<T> list;
	protected Context context;
	public HYBaseAdapter(List<T> list,Context context){
		this.list=list;
		this.context=context;
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
}
