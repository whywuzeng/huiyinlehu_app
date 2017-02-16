package com.huiyin.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> mFragmentList;

	public FragmentViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
		super(fm);
		this.mFragmentList = mFragmentList;
	}

	@Override
	public int getCount() {
		return mFragmentList == null ? 0 : mFragmentList.size();
	}

	// 得到每个item
	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

//	// 初始化每个页卡选项
//	@Override
//	public Object instantiateItem(ViewGroup arg0, int arg1) {
//		return super.instantiateItem(arg0, arg1);
//	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
	}

	
}
