package com.huiyin.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.base.BaseActivity;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.utils.PreferenceUtil;

public class WelcomeActivity extends BaseActivity {
	
	private ViewPager welcome_main_viewpager;
	private LayoutInflater inflater;
	private List<View> viewList;
	private Button wlecome_guide3_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_main);
		inite();
	}

	private void inite() {
		
		welcome_main_viewpager = (ViewPager) findViewById(R.id.welcome_main_viewpager);
		inflater = getLayoutInflater().from(this);
		View welcome_guid1 = inflater.inflate(R.layout.welcome_guid1, null);
		View welcome_guid2 = inflater.inflate(R.layout.welcome_guid2, null);
		View welcome_guid3 = inflater.inflate(R.layout.welcome_guid3, null);
		viewList = new ArrayList<View>();
		viewList.add(welcome_guid1);
		viewList.add(welcome_guid2);
		viewList.add(welcome_guid3);
		wlecome_guide3_start = (Button) welcome_guid3
				.findViewById(R.id.wlecome_guide3_start);
		MypagerAdapter adapter = new MypagerAdapter();
		welcome_main_viewpager.setAdapter(adapter);

		wlecome_guide3_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceUtil.getInstance(getApplicationContext()).setFirstStart(false);
				
				UIHelper.showMainAc(mContext);
				finish();
			}
		});
	}

	private class MypagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));// 添加页卡
			return viewList.get(position);
		}
	}
}
