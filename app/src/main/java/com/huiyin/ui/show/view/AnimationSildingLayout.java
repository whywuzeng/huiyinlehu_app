package com.huiyin.ui.show.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;

import com.huiyin.R;
import com.huiyin.adapter.CategoryLeftViewAdapter;
import com.nineoldandroids.animation.*;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * 自定义可以滑动的RelativeLayout,处理京东分类的滑动效果 用动画实现滚动效果
 * 
 * @author linyanjun
 * @blog http://blog.csdn.net/xiaolinxx
 */
public class AnimationSildingLayout extends RelativeLayout {
	/**
	 * SildingLayout 布局的父布局
	 */
	private ViewGroup parentView;
	private ListView leftView;
	private ListView rightView;
	private float leftMoveWidth;// 减少leftListview的偏移量
	/**
	 * 滑动的最小距离
	 */
	private int mTouchSlop;
	/**
	 * 按下点的X坐标
	 */
	private int downX;
	/**
	 * 按下点的Y坐标
	 */
	private int downY;
	/**
	 * 临时存储X坐标
	 */
	private int tempX;
	/**
	 * 临时存储时间
	 */
	private long tempTime;
	/**
	 * 滑动类
	 */
	private Scroller mScroller;
	/**
	 * SildingLayout的宽度
	 */
	private int viewWidth;

	private int parentViewScrollX;

	private boolean isSilding;

	/**
	 * 速度追踪对象
	 */
	private VelocityTracker velocityTracker;
	private static final int SNAP_VELOCITY = 300;

	private float leftlist_move_rate;
	private float leftlist_img_width;
	private OnSildingFinishListener onSildingFinishListener;

	private int leftViewTotalMove;
	/**
	 * 记录选中的item位置
	 */
	private int selectItemPosition;
	// 箭头图标
	private ImageView arrowView;

	public AnimationSildingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimationSildingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);
		leftlist_img_width = context.getResources().getDimension(R.dimen.leftlist_img_width);
		leftMoveWidth = context.getResources().getDimension(R.dimen.leftMoveWidth);
		Log.d("LogonActivity", "SildingLayout() mTouchSlop:" + mTouchSlop);
	}

	public void initLayout(final ListView leftView, ListView rightView) {
		this.leftView = leftView;
		this.rightView = rightView;
		this.parentView = (ViewGroup) rightView.getParent();
		parentView.setVisibility(INVISIBLE);
		arrowView = (ImageView) findViewById(R.id.img_arrow);
		leftView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// To change body of implemented methods use File | Settings |
				// File Templates.
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (leftView.getChildAt(0) != null) {
					int deltaY = tempScrollY - getScroll();
					tempScrollY = getScroll();
					//加上那个高度的Y值
					arrowView.setY(arrowView.getY() + deltaY);
				}
			}
		});

	}

	private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();
	private int tempScrollY;

	//把不是第一个 但是整个listview的高度计算出来
	private int getScroll() {
		View c = leftView.getChildAt(0); // this is the first visible row
		int scrollY = -c.getTop();
		listViewItemHeights.put(leftView.getFirstVisiblePosition(), c.getHeight());
		for (int i = 0; i < leftView.getFirstVisiblePosition(); ++i) {
			if (listViewItemHeights.get(i) != null) // (this is a sanity check)
				scrollY += listViewItemHeights.get(i); // add all heights of the
														// views that are gone
		}
		return scrollY;
	}

	/**
	 * 事件拦截操作
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// addVelocityTracker(ev);
			downX = tempX = (int) ev.getRawX();
			downY = (int) ev.getRawY();
			tempTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:

			int moveX = (int) ev.getRawX();
			// 满足此条件屏蔽SildingLayout里面子类的touch事件
			if ((Math.abs(moveX - downX) > mTouchSlop && Math.abs((int) ev.getRawY() - downY) < mTouchSlop)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			// recycleVelocityTracker();
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			if (parentView.getVisibility() == View.INVISIBLE) {
				break;
			}
			// 在移动时候leftview始终可见
			setLeftViewVisiable();
			int moveX = (int) event.getRawX();
			int deltaX = tempX - moveX;
			leftViewTotalMove += deltaX;
			long nowTime = System.currentTimeMillis();
			long deltaTime = nowTime - tempTime;
			tempTime = nowTime;
			tempX = moveX;
			if (Math.abs(moveX - downX) > mTouchSlop && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
				isSilding = true;
			}

			if (moveX - downX >= 0 && isSilding) {
				parentView.setX(parentView.getX() - deltaX);

				// 更新listview的文本
				changeListView();
				if (Math.abs(leftViewTotalMove * leftlist_move_rate) > 1) {
					float targetX = leftView.getX() - leftViewTotalMove * leftlist_move_rate;
					if (targetX >= -leftlist_img_width && leftView.getX() <= 0) {
						changeListView();
						leftView.setX(targetX);
					}
					leftViewTotalMove = 0;
				}

			}
			break;
		case MotionEvent.ACTION_UP:
			if (parentView.getVisibility() == View.INVISIBLE) {
				break;
			}
			leftViewTotalMove = 0;
			isSilding = false;
			if (parentView.getX() >= viewWidth / 3) {// 移动继续超过1/3就向右滑动
				//右边的出来
				scrollRight();
				extendLeftView();
			} else {
				//左边的出来
				scrollOrigin();
				leftViewSliding((int) leftView.getX(), -(int) leftlist_img_width, leftView, 500l);
			}

			break;
		case MotionEvent.ACTION_CANCEL:

			break;
		}

		return true;
	}

	private void onLeftViewShorted() {
		CategoryLeftViewAdapter ba = (CategoryLeftViewAdapter) leftView.getAdapter();
		ba.setHideFlag(true);
		ba.notifyDataSetChanged();
		leftView.setBackgroundColor(getResources().getColor(R.color.gray7));
	}

	//左边的listview 显示的展示
	private void onLeftViewExtended() {
		CategoryLeftViewAdapter ba = setLeftViewVisiable();
		if(ba!=null){
			ba.notifyDataSetChanged();
		}
		leftView.setBackgroundColor(getResources().getColor(R.color.white));
		// CatalogueActivity.isShow=false;
	}

	//左边的listview 显示的展示
	private CategoryLeftViewAdapter setLeftViewVisiable() {
		CategoryLeftViewAdapter ba = (CategoryLeftViewAdapter) leftView.getAdapter();
		if (ba != null && ba.isHideFlag()) {
			ba.setHideFlag(false);
			leftView.setBackgroundColor(getResources().getColor(R.color.feed_item_comment_background));
			ba.notifyDataSetChanged();
		}
		return ba;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 获取rightView所在布局的父布局
			parentView = (ViewGroup) rightView.getParent();
			viewWidth = this.getWidth();  //左边listview的图片的宽度
			leftlist_move_rate = leftlist_img_width / (viewWidth - leftlist_img_width);
			Log.d("LogonActivity", "SildingLayout：onLayout: leftlist_move_rate" + leftlist_move_rate);

		}
	}

	/**
	 * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
	 * 
	 * @param onSildingFinishListener
	 */
	public void setOnSildingFinishListener(OnSildingFinishListener onSildingFinishListener) {
		this.onSildingFinishListener = onSildingFinishListener;
	}

	/**
	 * 滚动出界面
	 */
	private void scrollRight() {
		parentViewSliding((int) parentView.getX(), viewWidth, parentView, 500l, new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				onLeftViewExtended();
				if (onSildingFinishListener != null) {
					onSildingFinishListener.onSildingFinish();
				}
			}
		});
	}

	/**
	 * 滚动到起始位置
	 */
	public void scrollOrigin() {
		parentViewSliding((int) parentView.getX(), 0, parentView, 500l, new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				parentViewScrollX = 0;
			}
		});
	}

	/**
	 * 滚动到起始位置
	 */
	public void scrollInit() {
		try {
			setLeftViewVisiable();
			scrollRight();
			extendLeftView();
		} catch (Exception e) {

		}
	}
/**
 * 更新右边的listive
 */
	
	private void changeListView() {
		int visiblePosition = leftView.getFirstVisiblePosition();
		int lastVisiblePosition = leftView.getLastVisiblePosition();
		int selectedItemNum = selectItemPosition - visiblePosition;
		// Log.d("LogonActivity",
		// String.format("SildingLayout：visiblePosition:%d ,lastVisiblePosition %d",visiblePosition,lastVisiblePosition));
		for (int i = 0; i <= lastVisiblePosition - visiblePosition; i++)// 获取ListView的所有Item数目
		{
			// LinearLayout linearlayout =
			// (LinearLayout)mListView.getChildAt(i);
			View view = leftView.getChildAt(i);

			if (view != null && view.getTag() != null) {
				CategoryLeftViewAdapter.ViewHolder holder = (CategoryLeftViewAdapter.ViewHolder) view.getTag();
				// holder.textView.setText(String.valueOf(parentViewScrollX));
				holder.description.setAlpha(Math.abs(parentView.getX()) / viewWidth);
			}
		}
	}

	public void startSildingInAnimation(int position) {
		this.selectItemPosition = position;
		arrowView.setY(leftView.getChildAt(position - leftView.getFirstVisiblePosition()).getTop()-10);
		tempScrollY = (int) getScroll();
		if (Math.abs(leftView.getX()) < leftlist_img_width * 0.2) { // 如果leftview大部分都显示出来，就执行动画

			parentViewSliding(viewWidth, 0, parentView, 500l, new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					// CatalogueActivity.isShow=true;
				}

				@Override
				public void onAnimationStart(Animator animation) {
					super.onAnimationStart(animation);
					parentView.setVisibility(VISIBLE);
				}
			});
			//这个是什么动画？
			leftViewSliding(0, -(int) leftlist_img_width, leftView, 500l);
		}
	}
	
	/**
	 * 右边滚动出界面 4、其实还有更简单的方式，实现一个动画更改多个效果：使用propertyValuesHolder
	 */
	private void parentViewSliding(int fromx, int tox, View view, long duration, AnimatorListenerAdapter listener) {
		//动画的集合
		PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("x", fromx, tox);
		//一个动画  ObjectAnimator 让我执行
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvTY).setDuration(duration);
		objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// parentViewScrollX = ((Float)
				// valueAnimator.getAnimatedValue()).intValue();
				//更新右边的listview的数据
				changeListView();
			}
		});
		objectAnimator.addListener(listener);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.start();
	}

	/**
	 * 左边变暗 缩小
	 */
	
	private void leftViewSliding(int formx, int tox, View view, long duration) {

		PropertyValuesHolder leftXPv = PropertyValuesHolder.ofFloat("x", formx, tox + leftMoveWidth);
		ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(view, leftXPv).setDuration(duration);
		leftOA.setInterpolator(new DecelerateInterpolator());
		leftOA.start();
		leftOA.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				//让左边 遍暗
				onLeftViewShorted();
			}
		});

	}
	/**
	 * 两个listview 四个布局 这个是左边那个变宽么？
	 */
	
	private void extendLeftView() {
		PropertyValuesHolder leftXPv;
		if (leftView.getX() < 0) {
			leftXPv = PropertyValuesHolder.ofFloat("x", leftView.getX(), 0);
		} else {
			return;
		}

		ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(leftView, leftXPv).setDuration(500);
		leftOA.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				//左边的回来 调用适配器里面的方法
				onLeftViewExtended();
			}
		});
		leftOA.setInterpolator(new DecelerateInterpolator());
		leftOA.start();

	}

	public interface OnSildingFinishListener {
		public void onSildingFinish();
	}

	/**
	 * 添加用户的速度跟踪器
	 * 
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}

		velocityTracker.addMovement(event);
	}

	/**
	 * 移除用户速度跟踪器
	 */
	private void recycleVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	/**
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

}
