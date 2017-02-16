package com.huiyin.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.huiyin.R;
import com.huiyin.adapter.CategoryLeftViewAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 自定义可以滑动的RelativeLayout,处理京东分类的滑动效果
 *
 * @author linyanjun
 * @blog http://blog.csdn.net/xiaolinxx
 */
public class SildingLayout extends RelativeLayout {
    /**
     * SildingLayout 布局的父布局
     */
    private ViewGroup parentView;
    private ListView leftView;
    private ListView rightView;
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
    private boolean isFinish;

    private boolean leftViewIsShort;
    private boolean leftViewIsFull;
    private boolean leftViewIsTranslating=true;

    private int leftViewTotalMove;
    private int leftViewTotalMove2;
    public SildingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        leftlist_img_width= context.getResources().getDimension(R.dimen.leftlist_img_width);
        Log.d("LogonActivity", "SildingLayout() mTouchSlop:" + mTouchSlop);

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
                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = (int) ev.getRawX();
                //满足此条件屏蔽SildingLayout里面子类的touch事件
                if ((Math.abs(moveX - downX) > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //recycleVelocityTracker();
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_MOVE:
                //在移动时候leftview始终可见
                setLeftViewVisiable();
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;

                tempX = moveX;
                if (Math.abs(moveX - downX) > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;
                }

                if (moveX - downX >= 0 && isSilding) {
                   // Log.d("LogonActivity", "MotionEvent.ACTION_MOVE deltaX" + deltaX);

                    parentView.scrollBy(deltaX, 0);
                    //更新listview的文本
                    parentViewScrollX+=deltaX;
                    changeListView();
                    if(!leftViewIsFull&&deltaX<0&&Math.abs(deltaX)>2){//向右移动且leftview未完全显示
                        leftViewTotalMove+=deltaX;
                        leftViewTotalMove2=0;
                        leftViewIsTranslating=true;
                        if(Math.abs(leftViewTotalMove)<=leftlist_img_width){
                            leftView.setX(leftView.getX() - deltaX);


                        } else{
                            Log.d("LogonActivity", "向右移动结束MotionEvent.Math.abs(leftViewTotalMove)<=leftlist_img_width " + (Math.abs(leftViewTotalMove)-(int)leftlist_img_width) +" x:"+leftView.getX());
                           // leftView.scrollBy(-(Math.abs(leftViewTotalMove)-(int)leftlist_img_width),0);

                            leftView.setX(0); //todo setx(0) 左边会有白边，因为scroll的关系 上面不用scrollby的话就是0
                            leftViewIsFull=true;
                            leftViewIsShort=false;
                            leftViewTotalMove=0;
                            leftViewTotalMove2=0;
                            leftViewIsTranslating=false;
                        }
                    }
                    if(!leftViewIsShort&&deltaX>0&&Math.abs(deltaX)>2){//向左移动且leftview完全显示
                        leftViewTotalMove2+=deltaX;
                        leftViewTotalMove=0;
                        leftViewIsTranslating=true;
                        //Log.d("LogonActivity", "MotionEvent.向左移动且leftview完全显示 deltaX|leftViewTotalMove" + deltaX+"|"+leftViewTotalMove);
                        if(Math.abs(leftViewTotalMove2)<=leftlist_img_width){
//                            leftView.scrollBy(deltaX,0);
                            leftView.setX(leftView.getX()-deltaX);

                        } else{
                            Log.d("LogonActivity", "向左移动结束MotionEvent.Math.abs(leftViewTotalMove2)<=leftlist_img_width" + (Math.abs(leftViewTotalMove2)-(int)leftlist_img_width)+" x:"+leftView.getX());
                           // leftView.scrollBy((Math.abs(leftViewTotalMove2)-(int)leftlist_img_width),0);
                            leftView.setX(-leftlist_img_width);
                            leftViewIsShort=true;
                            leftViewIsFull=false;
                            leftViewTotalMove=0;
                            leftViewTotalMove2=0;
                            leftViewIsTranslating=false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollVelocity = getScrollVelocity();
                //Log.d("LogonActivity", "MotionEvent.ACTION_UP：getScrollVelocity():" + scrollVelocity);
//                if(scrollVelocity > SNAP_VELOCITY){
//                    isFinish = true;
//                    scrollRight();
//                    recycleVelocityTracker();
//                    break;
//                }
                isSilding = false;
                if (parentView.getScrollX() <= -viewWidth / 2) {
                    isFinish = true;
                    scrollRight();
                    if(leftViewIsTranslating||leftViewIsShort){

                        extendLeftView();
                    }

                } else {
                    scrollOrigin();
                    if(leftViewIsTranslating||leftViewIsFull){

                        shortLeftView();
                    }
                    isFinish = false;
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("LogonActivity", "MotionEvent.ACTION_CANCEL：getScrollVelocity():" + getScrollVelocity());
                recycleVelocityTracker();
                break;
        }

        return true;
    }
    //左边变窄
    private void onLeftViewShorted() {
        CategoryLeftViewAdapter ba= (CategoryLeftViewAdapter) leftView.getAdapter();
        ba.setHideFlag(true);
        ba.notifyDataSetChanged();
    }
    
    /**
     * 右边没有
     */
    
    private void onLeftViewExtended() {
        CategoryLeftViewAdapter ba = setLeftViewVisiable();
        ba.notifyDataSetChanged();
    }

    private CategoryLeftViewAdapter setLeftViewVisiable() {
        CategoryLeftViewAdapter ba= (CategoryLeftViewAdapter) leftView.getAdapter();
        if (ba.isHideFlag()) {
            ba.setHideFlag(false);
            ba.notifyDataSetChanged();
        }
        return ba;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (parentView!=null) {
            parentViewScrollX=parentView.getScrollX();
            //todo 这里有个问题 getX getLeft rightview getx rightView getLeft 都获取不到值
            //Log.d("LogonActivity", "SildingLayout：onLayout:" +changed+",parentView.getScrollX():"+ parentView.getScrollX() +"/"+this.getWidth());
        }
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 获取SildingLayout所在布局的父布局
            parentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
            leftlist_move_rate=leftlist_img_width/viewWidth;
            //重置记录
            parentViewScrollX=0;
        }
    }

    /**
     * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
     *
     * @param onSildingFinishListener
     */
    public void setOnSildingFinishListener(
            OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;
    }


    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + parentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(parentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    public void scrollOrigin() {
        int delta = parentView.getScrollX();
        mScroller.startScroll(parentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));

        postInvalidate();
    }


    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            parentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            //在rightview滚动的时候改变leftview的内容
                changeListView();
            if (mScroller.isFinished() && isFinish) {
                Log.d("LogonActivity", "SildingFinishLayout：mScroller.isFinished() leftViewIsFull/leftViewIsShort:" +leftViewIsFull +"/"+leftViewIsShort);
                //滚动完成后更新leftview
                 if(leftViewIsFull){
                     onLeftViewExtended();
                 }
                if(leftViewIsShort){
                    onLeftViewShorted();
                }
                if (onSildingFinishListener != null) {
                    onSildingFinishListener.onSildingFinish();

                } else {
                    //没有设置OnSildingFinishListener，让其滚动到其实位置
                    scrollOrigin();
                    shortLeftView();
                    isFinish = false;
                }
            }
        }
    }

    private void changeListView() {
        int visiblePosition = leftView.getFirstVisiblePosition();
        int lastVisiblePosition = leftView.getLastVisiblePosition();
        // Log.d("LogonActivity", String.format("SildingLayout：visiblePosition:%d ,lastVisiblePosition %d",visiblePosition,lastVisiblePosition));
        for(int i = 0; i <=lastVisiblePosition-visiblePosition; i++)//获取ListView的所有Item数目
        {
            //    LinearLayout linearlayout = (LinearLayout)mListView.getChildAt(i);
            View view = leftView.getChildAt(i);
            if (view!=null&&view.getTag()!=null) {
                CategoryLeftViewAdapter.ViewHolder holder = (CategoryLeftViewAdapter.ViewHolder) view.getTag();
               // holder.textView.setText(String.valueOf(parentViewScrollX));
                holder.description.setAlpha(Math.abs(parentViewScrollX)/(float)viewWidth);
            }
        }
    }

    public void startSildingInAnimation() {
        if (isFinish) {
            setLeftViewVisiable();
            scrollOrigin();
            resetFinishFlag();
            shortLeftView();
        }else{
//                    ViewPropertyAnimator.animate(rightList)
//                            .translationX(-(list.getWidth()/4))//y轴方向的移动距离
//                            .setDuration(1000)
//                            .start();

            PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("x", leftView.getWidth(),
                    0);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(rightView, pvTY).setDuration(500);
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    parentViewScrollX=((Float)valueAnimator.getAnimatedValue()).intValue();
                    changeListView();
                }
            });
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            parentViewScrollX=0;
                        }

                    });
//
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.start();
            //移动leftview

            shortLeftView();

        }

    }



    private void shortLeftView() {

        PropertyValuesHolder leftXPv = PropertyValuesHolder.ofFloat("x", 0,
                -leftlist_img_width);
        ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(leftView, leftXPv).setDuration(500);
        leftOA.setInterpolator(new DecelerateInterpolator());
        leftOA.start();
        leftOA.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onLeftViewShorted();
                leftViewIsShort=true;
                leftViewIsFull=false;
            }
        });

    }


    private void extendLeftView() {
        PropertyValuesHolder leftXPv = PropertyValuesHolder.ofFloat("x", -leftlist_img_width,
                0);
        ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(leftView, leftXPv).setDuration(500);
        leftOA.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onLeftViewExtended();
                leftViewIsShort=false;
                leftViewIsFull=true;
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

    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }

    public ListView getLeftView() {
        return leftView;
    }

    public void setLeftView(ListView leftView) {
        this.leftView = leftView;
    }

    public void resetFinishFlag(){
        isFinish=false;
    }



    public ListView getRightView() {
        return rightView;
    }

    public void setRightView(ListView rightView) {
        this.rightView = rightView;
    }
}
