package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.List;
import com.huiyin.R;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.wight.MyGridView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UploadImageLinearLayout extends LinearLayout {

	public LinearLayout mContainer;
	public MyGridView update_grid_view;
	public ImageView add_pic;
	public Activity context;
	public GridAdapter myGridAdapter;
	ArrayList<String> imgPathlist = new ArrayList<String>();
	List<Bitmap> images = new ArrayList<Bitmap>();
	private int size;
	private boolean checkSize = false;

	public UploadImageLinearLayout(Context context) {
		super(context);
		this.context = (Activity) context;
		initView();
	}

	public UploadImageLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity) context;
		initView();
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
			}
		});

		myGridAdapter = new GridAdapter(context);
		update_grid_view.setAdapter(myGridAdapter);

	}

	/**
	 * 设置最大张数
	 * 
	 * @param size
	 */
	public void setMaxSize(int size) {
		this.size = size;
		checkSize = true;
	}

	private void setAddButtonVisiviblity(int count) {

		if (checkSize && count >= size)
			add_pic.setVisibility(View.GONE);
		else
			add_pic.setVisibility(View.VISIBLE);
	}

	/**
	 * 增加url地址
	 * 
	 * @param url
	 */
	public void addUrls(String url) {
		imgPathlist.add(url);
	}

	/**
	 * 返回地址窜
	 * 
	 * @return
	 */
	public ArrayList<String> getUrls() {

		return imgPathlist;
	}

	/**
	 * 增加图片
	 * 
	 * @param map
	 */
	public void addPics(Bitmap map) {

		images.add(map);
		myGridAdapter.notifyDataSetChanged();
		setAddButtonVisiviblity(images.size());

	}

	public class GridAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public GridAdapter(Context context) {
			super();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.add_iamge_item, null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_add_image_item);
				viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.iv_delete_jianhao);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.image.setImageBitmap(images.get(position));
			viewHolder.img_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					images.remove(position);
					imgPathlist.remove(position);

					notifyDataSetChanged();
					setAddButtonVisiviblity(images.size());
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		public ImageView image;
		public ImageView img_delete;
	}
}
