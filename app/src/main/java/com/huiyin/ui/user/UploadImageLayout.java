package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huiyin.R;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.wight.MyGridView;

public class UploadImageLayout extends LinearLayout {

	public LinearLayout mContainer;
	public MyGridView update_grid_view;
	public ImageView add_pic;
	public Activity context;
	public MyGridAdapter myGridAdapter;
	List<Bitmap> images = new ArrayList<Bitmap>();
	// 图片集合
	private String pic_urls = "";
	private int which;

	public UploadImageLayout(Context context) {
		super(context);
		this.context = (Activity) context;
		initView();
	}

	public UploadImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity) context;
		initView();
	}

	public void setPos(int which_pos) {
		this.which = which_pos;
	}

	private void initView() {

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.image_base_layout, null);
		addView(mContainer, lp);

		update_grid_view = (MyGridView) mContainer.findViewById(R.id.update_grid_view);
		add_pic = (ImageView) mContainer.findViewById(R.id.add_pic);
		add_pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
				context.startActivityForResult(i, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
				OrderCommentActivity.setPos(which);
			}
		});
	}

	/**
	 * 增加url地址
	 * 
	 * @param url
	 */
	public void addUrls(String url) {
		pic_urls += url + ",";
	}

	/**
	 * 返回地址窜
	 * 
	 * @return
	 */
	public String getUrls() {
		if (pic_urls.endsWith(",")) {
			pic_urls = pic_urls.substring(0, pic_urls.length() - 1);
		}
		return pic_urls;
	}

	/**
	 * 增加图片
	 * 
	 * @param map
	 */
	public void addPics(Bitmap map) {

		if (images.size() < 1) {
			images.add(map);
			myGridAdapter = new MyGridAdapter(context);
			update_grid_view.setAdapter(myGridAdapter);
		} else {
			images.add(map);
			myGridAdapter.notifyDataSetChanged();
			if (images.size() >= 5) {
				add_pic.setVisibility(View.GONE);
			}
		}

	}

	public class MyGridAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MyGridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return images == null ? 0 : images.size();
		}

		@Override
		public Object getItem(int position) {
			return images.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.upload_image_item, null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView.findViewById(R.id.upload_img);
				AbsListView.LayoutParams param = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						100);
				convertView.setLayoutParams(param);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.image.setImageBitmap(images.get(position));
			return convertView;
		}

	}

	class ViewHolder {
		public ImageView image;
	}

}
