package com.huiyin.base;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseXAdapter<T> extends BaseAdapter {

	public Context mContext;
	public ArrayList<T> mListData;

	public BaseXAdapter(Context context) {
		this.mContext = context;
		mListData = new ArrayList<T>();
	}

	public void cleanSelect() {

	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mListData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return -1;
	}

	public int getId(int arg0) {
		return -1;
	}

	public ArrayList<T> getList() {
		return mListData;
	}

	public void deleteItem() {
		mListData.clear();
		notifyDataSetChanged();
	}
	
	public void addItem(T t){
		mListData.add(t);
		notifyDataSetChanged();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addItems(Collection<? extends T> t) {
		if (t == null) {
			return;
		}
		if (t instanceof ArrayList) {
			mListData.addAll(t);
		} else {
			mListData.add((T) t);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}




