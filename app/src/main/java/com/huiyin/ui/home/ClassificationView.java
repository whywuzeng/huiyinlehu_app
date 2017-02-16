package com.huiyin.ui.home;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseXAdapter;
import com.huiyin.utils.ImageManager;

public class ClassificationView extends LinearLayout {

	private CassificationViewCoallBack mCoallBack;
	private Context mContext;
	private MyGridView mGridView;
	private ClassificationAdapter mAdapter;

	public ClassificationView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public ClassificationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater.from(mContext).inflate(R.layout.chaoliujingxuan, this, true);

		mAdapter = new ClassificationAdapter(mContext);
		mGridView = (MyGridView) findViewById(R.id.gridView1);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (mCoallBack != null) {
					Cassification cassification = (Cassification) mAdapter.getItem(arg2);
					mCoallBack.onItemClick(cassification);
				}
			}
		});
	}

	public void setData(ArrayList<Cassification> arr) {
		if (mAdapter != null) {
			mAdapter.addItems(arr);
		}
	}

	/**
	 * 设置模块名称 最外面的模块名字
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		TextView classification_name_tv = (TextView) findViewById(R.id.classification_name_tv);
		classification_name_tv.setText(name);
	}

	public class ClassificationAdapter extends BaseXAdapter<Cassification> {

		private LayoutInflater mInflater;

		public ClassificationAdapter(Context context) {
			super(context);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Cassification data = mListData.get(position);
			convertView = mInflater.inflate(R.layout.home_classification_grid_item, null);
			ImageView iconIv = (ImageView) convertView.findViewById(R.id.home_classification_iv);
			View line_buttom = convertView.findViewById(R.id.line_buttom);
			View line_right = convertView.findViewById(R.id.line_right);

			ImageManager.Load(data.iconPath, iconIv);

			if (mListData.size() % 2 != 0) {
				// 奇数个
				if (position >= mListData.size() - 1) {
					line_buttom.setVisibility(View.INVISIBLE);
				} else {
					line_buttom.setVisibility(View.VISIBLE);
				}
			} else {
				// 偶数个
				if (position >= mListData.size() - 2) {
					line_buttom.setVisibility(View.INVISIBLE);
				} else {
					line_buttom.setVisibility(View.VISIBLE);
				}
			}

			if (position % 2 == 0) {
				line_right.setVisibility(View.VISIBLE);
			} else {
				line_right.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}

	public void setCoallBack(CassificationViewCoallBack coallBack) {
		mCoallBack = coallBack;
	}

	public interface CassificationViewCoallBack {
		public void onItemClick(Cassification arg0);
	}

}

class Cassification {
	/** 分类聚合的id */
	public int id;
	public String name;
	public String desc;
	public String icon;
	public String color;
	/** 分类聚合图片 */
	public String iconPath;
	/** 布局字段 */
	public int layout;

	public String linkFlag;

	public String linkId;
}
