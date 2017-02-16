package com.huiyin.ui.home.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huiyin.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TheMoreActivity extends Activity {
	private List<HashMap<String, Object>> dataSourceList;
	private List<HashMap<String, Object>> dataSourceList2;

	DragAdapter mDragAdapter;
	DragAdapter2 mDragAdapter2;
	DragGridView mDragGridView, mDragGridView2;
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	LayoutAnimationController controller;
	HashMap<String, Object> a;
	HashMap<String, Object> b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themore_more);
		Animation animation = (Animation) AnimationUtils.loadAnimation(
				TheMoreActivity.this, R.anim.alpha);

		controller = new LayoutAnimationController(animation);

		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

		controller.setDelay(0.5f);

		// =========================================================================================11111111111111
		mDragGridView = (DragGridView) findViewById(R.id.dragGridView);

		dataSourceList = getData();

		mDragAdapter = new DragAdapter(this, dataSourceList);

		mDragGridView.setAdapter(mDragAdapter);
		mDragGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				showInfo(position);
				mDragGridView.setLayoutAnimation(controller);
			}
		});
		// =======================================================================================2222222222222222222222222222222222222
		mDragGridView2 = (DragGridView) findViewById(R.id.dragGridView2);

		dataSourceList2 = getData2();

		mDragAdapter2 = new DragAdapter2(this, dataSourceList2);

		mDragGridView2.setAdapter(mDragAdapter2);
		mDragGridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position2, long arg3) {
				// TODO Auto-generated method stub

				showInfo2(position2);
				mDragGridView2.setLayoutAnimation(controller);
				// AnimationDrawable ad =
				// (AnimationDrawable)mDragGridView2.getBackground();
				// ad.start();
			}
		});

		// /////////////////////////////////////////////////////////
	}

	private List<HashMap<String, Object>> getData() {

		List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
		String text = null;
		int img = R.drawable.ic_launcher;
		for (int i = 0; i < 7; i++) {
			
			switch (i) {
			case 0:
				text = "今日头条";
				img = R.drawable.icon_jinritoutiao;
				break;
			case 1:
				text = "乐虎彩票";
				img = R.drawable.icon_yuehucaipiao;
				break;
			case 2:
				text = "智慧管家";
				img = R.drawable.icon_zhihuiguanjia;

				break;
			case 3:
				text = "积分CLUB";
				img = R.drawable.icon_jifenclb;
				break;
			case 4:
				text = "预约服务";
				img = R.drawable.icon_yuyuefuwu;
				break;
			case 5:
				text = "物流查询";
				img = R.drawable.icon_wuliuchaxun;
				break;
			case 6:
				text = "时尚秀场";
				img = R.drawable.icon_xiuchangyuan;
				break;

			default:
				break;
			}

			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_image", img);
			itemHashMap.put("item_text", text);
			dataSourceList.add(itemHashMap);

		}

		return dataSourceList;

	}

	public void showInfo(final int position) {
		new AlertDialog.Builder(this).setTitle("我的提示").setMessage("确定要删除吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						a = dataSourceList.get(position);
						dataSourceList.remove(position);
						// 通过程序我们知道删除了，但是怎么刷新ListView呢？
						// 只需要重新设置一下adapter
						mDragGridView.setAdapter(mDragAdapter);
						dataSourceList2.add(a);
						mDragGridView2.setAdapter(mDragAdapter2);
					}
				}).show();
	}

	// =================================================================================================================================================2

	private List<HashMap<String, Object>> getData2() {

		List<HashMap<String, Object>> dataSourceList2 = new ArrayList<HashMap<String, Object>>();
		String text = null;
		int img = R.drawable.icon_kefu_center;
		for (int i = 0; i < 4; i++) {
			
			switch (i) {
			case 0:
				text = "客服中心";
				img = R.drawable.icon_kefu_center;
				break;
			case 1:
				text = "附近门店";
				img = R.drawable.icon_fujianmendian;
				break;
			case 2:
				text = "缴费充值";
				img = R.drawable.icon_jiaofeichongzhi;

				break;
			case 3:
				text = "积分兑换";
				img = R.drawable.icon_jifenduihuan;
				break;

			default:
				break;
			}
			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_image",img);
			itemHashMap.put("item_text", text);
			dataSourceList2.add(itemHashMap);

		}

		return dataSourceList2;

	}

	public void showInfo2(final int position2) {
		new AlertDialog.Builder(this).setTitle("我的提示").setMessage("确定要添加吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						b = dataSourceList2.get(position2);
						dataSourceList2.remove(position2);
						// 通过程序我们知道删除了，但是怎么刷新ListView呢？
						// 只需要重新设置一下adapter
						mDragGridView2.setAdapter(mDragAdapter2);
						dataSourceList.add(b);
						mDragGridView.setAdapter(mDragAdapter);
					}
				}).show();
	}

}
