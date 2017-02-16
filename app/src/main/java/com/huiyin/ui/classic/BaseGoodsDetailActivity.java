package com.huiyin.ui.classic;

import com.huiyin.R;
import com.huiyin.wight.SlidingFragmentActivity;
import com.huiyin.wight.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseGoodsDetailActivity extends SlidingFragmentActivity {
	//初始化 Sliding
	protected GoodsDetailRightFragment menuFragment;
//	protected GoodsDetailGroupFragment groupFragment;

	private int rightmenu;// 0为购买的菜单 1为组合的菜单
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setBehindContentView(R.layout.menu_frame);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.RIGHT);
		sm.setShadowDrawable(R.drawable.shadowright);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		//禁止触摸模式，不能够通过触摸打开，只能够通过SlidingMenu().toggle()打开或者是关闭
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		menuFragment = new GoodsDetailRightFragment();
		t.replace(R.id.menu_frame, menuFragment);

		rightmenu = 0;

//		groupFragment = new GoodsDetailGroupFragment();
		// t.replace(R.id.menu_frame_two, groupFragment);
		t.commit();
	}

	public void showMenu(int type) {
		if (type == rightmenu) {
			showMenu();
			return;
		}
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		switch (type) {
		case 0:
			t.replace(R.id.menu_frame, menuFragment);
			rightmenu = 0;
			break;
		case 1:
//			t.replace(R.id.menu_frame, groupFragment);
			rightmenu = 1;
			break;
		}
		t.commit();
		showMenu();
	}

}
