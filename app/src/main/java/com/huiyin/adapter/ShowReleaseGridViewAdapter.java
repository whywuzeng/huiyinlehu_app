package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.huiyin.R;

public class ShowReleaseGridViewAdapter extends BaseAdapter {
	private ArrayList<String> imgPathlist;
	private List<Bitmap> lists;
	private Context ct;

	public ShowReleaseGridViewAdapter() {

	}

	public ShowReleaseGridViewAdapter(Context ct) {
		this.ct = ct;
		imgPathlist = new ArrayList<String>();
		lists = new ArrayList<Bitmap>();
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(ct, R.layout.add_iamge_item, null);
		}
		final ImageView img = (ImageView) convertView
				.findViewById(R.id.iv_add_image_item);

		ImageView img_delete = (ImageView) convertView
				.findViewById(R.id.iv_delete_jianhao);
		if (lists != null & lists.size() > 0) {
			// img.setLayoutParams(new GridView.LayoutParams(180,
			// 180));//设置ImageView对象布局
			// img.setImageBitmap(lists.get(position));
			// img.setLayoutParams(new GridView.LayoutParams(180, 180));
//			img.setPadding(4, 4, 4, 4);// 设置间距
//			img.setAdjustViewBounds(false);// 设置边界对齐
//			img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			img.setImageBitmap(lists.get(position));
			// 点击删除上传的图片
			img_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					lists.remove(position);
					imgPathlist.remove(position);

					notifyDataSetChanged();
				}
			});

		}

		return convertView;
	}

	/**
	 * 添加图片
	 * 
	 * @param bitmap
	 */
	public void addPhoto(Bitmap loadImage) {
		lists.add(loadImage);
		notifyDataSetChanged();
	}

	/**
	 * 添加上传图片的路径
	 * 
	 */
	public void addImagePath(String path) {

		imgPathlist.add(path);
	}
	
	public ArrayList<String> getImagePathList() {
		return imgPathlist;
	}

	/**
	 * 获取上传图片的路径集合
	 * 
	 */
	public String getImagePath() {
		if (imgPathlist.size() == 0) {

			return "";
		}
		String imagePath = "";
		for (int i = 0; i < imgPathlist.size(); i++) {

			if (i == imgPathlist.size() - 1) {

				imagePath += imgPathlist.get(i);
				break;
			}
			imagePath += imgPathlist.get(i) + ",";
		}

		return imagePath;
	}

}
