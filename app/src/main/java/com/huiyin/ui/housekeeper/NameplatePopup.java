package com.huiyin.ui.housekeeper;

import com.huiyin.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NameplatePopup extends PopupWindow {

	private View mMenuView;
	private TextView left_ib, middle_title_tv, textView_dingyis_mingpai, right_ib;

	public NameplatePopup(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.nameplate_pop, null);
		// 设置按钮监听
		left_ib = (TextView) mMenuView.findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 销毁弹出框
				dismiss();
			}
		});
		middle_title_tv = (TextView) mMenuView.findViewById(R.id.middle_title_tv);
		textView_dingyis_mingpai = (TextView) mMenuView.findViewById(R.id.textView_dingyis_mingpai);
		right_ib = (TextView) mMenuView.findViewById(R.id.right_ib);
		right_ib.setVisibility(View.INVISIBLE);
		textView_dingyis_mingpai.setText(R.string.ming_pai_ding_yi);
		middle_title_tv.setText("铭牌");
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popwin_anim_style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(Color.WHITE);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
}
