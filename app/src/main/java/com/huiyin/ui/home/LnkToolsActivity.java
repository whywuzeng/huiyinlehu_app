package com.huiyin.ui.home;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.DragAdapter;
import com.huiyin.adapter.OtherAdapter;
import com.huiyin.bean.ChannelItem;
import com.huiyin.bean.ChannelItemComparator;
import com.huiyin.db.LnkToolsDao;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.view.DragGrid;
import com.huiyin.view.OtherGridView;

/**
 * 快捷方式界面  设置导航界面
 **/
public class LnkToolsActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	public static String TAG = "LnkToolsActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	/** 栏目列表 */
	ArrayList<ChannelItem> allChannelList = new ArrayList<ChannelItem>();

	/** 数据库操作类 */

	private LnkToolsDao linkToolsDao;

	private TextView ab_back;
	private TextView ab_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lnktools);
		initView();
		initData();
	}

	/** 初始化数据 */
	@SuppressWarnings("unchecked")
	private void initData() {
		linkToolsDao = new LnkToolsDao();
		allChannelList = (ArrayList<ChannelItem>) getIntent()
				.getSerializableExtra("all_data");
		userChannelList = (ArrayList<ChannelItem>) getIntent()
				.getSerializableExtra("selected_data");

		otherChannelList = new ArrayList<ChannelItem>();

		for (ChannelItem bean : allChannelList) {

			boolean isUse = false;
			for (ChannelItem bean1 : userChannelList) {
				if (bean.getChannelId() == bean1.getChannelId()) {
					isUse = true;
				}
			}

			if (!isUse) {
				otherChannelList.add(bean);
			}

		}

		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}

	/** 初始化布局 */
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);

		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("设置快捷导航");

		ab_back.setOnClickListener(this);
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
		// position为 0，1 的不可以进行任何操作
		// if (position != 0 && position != 1) {
		{
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				LinearLayout layoutItem = (LinearLayout) view
						.findViewById(R.id.layout_item);
				final int[] startLocation = new int[2];
				layoutItem.getLocationInWindow(startLocation);
				final ChannelItem channel = ((DragAdapter) parent.getAdapter())
						.getItem(position);// 获取点击的频道内容
				otherAdapter.setVisible(false);
				// 添加到最后一个
				otherAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							otherGridView.getChildAt(
									otherGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation,
									channel, userGridView);
							userAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
		}
			// }
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				LinearLayout layoutItem = (LinearLayout) view
						.findViewById(R.id.layout_item);
				final int[] startLocation = new int[2];
				layoutItem.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
						.getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(
									userGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation,
									channel, otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		linkToolsDao.deleteAll();// 先删除全部

		ChannelItemComparator comparator = new ChannelItemComparator();
		Collections.sort(userAdapter.getChannnelLst(), comparator);

		Log.i("", "AAA============" + userAdapter.getChannnelLst());
		PreferenceUtil.getInstance(getApplicationContext()).setFirstSet(false);
		linkToolsDao.addAll(userAdapter.getChannnelLst());// 添加已经修改后的快捷列表
		// otherAdapter.getChannnelLst() //其他快捷列表
	}

	@Override
	public void onBackPressed() {
		saveChannel();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		setResult(MainActivity.LNKTOOLS_RESULT, intent);
		finish();
		if (userAdapter.isListChanged()) {

		} else {
			super.onBackPressed();
		}
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onClick(View view) {
		saveChannel();
		if (userAdapter.isListChanged()) {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			setResult(MainActivity.LNKTOOLS_RESULT, intent);
			finish();
		} else {
			super.onBackPressed();
		}
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
