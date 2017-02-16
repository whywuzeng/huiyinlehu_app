package com.huiyin.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ClassListLevel3PagerAdapter extends PagerAdapter {

	private List<View> listView;
	public ClassListLevel3PagerAdapter(List<View> listView){
		this.listView=listView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listView.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(listView.get(position));
	}

	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(listView.get(position));//添加页卡
		return listView.get(position);
		
	}

}
