package com.huiyin.wight;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MyScrollView extends ScrollView {

	Context context;

	public MyScrollView(Context context) {
		super(context);
		this.context = context;
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	private int lastScrollDelta = 0;

	public void resume() {
		overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
		lastScrollDelta = 0;
	}

	int mTop = 10;

	/**
	 * 将targetView滚到最顶端
	 */
	public void scrollTo(View targetView) {
		int oldScrollY = getScrollY();
		int top = targetView.getTop() - mTop;
		int delatY = top - oldScrollY;
		lastScrollDelta = delatY;
		overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
	}

	private int getScrollRange() {
		int scrollRange = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			scrollRange = Math.max(0, child.getHeight() - (getHeight()));
		}
		return scrollRange;
	}

	int currentY;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		return super.onTouchEvent(ev);
	}
}
