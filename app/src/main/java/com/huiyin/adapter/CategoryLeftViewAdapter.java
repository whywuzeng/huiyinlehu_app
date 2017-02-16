package com.huiyin.adapter;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.CategoryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class CategoryLeftViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<CategoryBean> categories;
	private int selectedPosition = -1;
	// 用来标示是否需要隐藏图片和描述文字

	private boolean hideFlag;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public CategoryLeftViewAdapter(Context context, ArrayList<CategoryBean> categories) {
		this.context = context;
		this.categories = categories;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)
						.displayer(new RoundedBitmapDisplayer(0)).build();
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_left_list_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textview);
			holder.lineimage = (ImageView) convertView.findViewById(R.id.line_listiem);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.colorlayout);
			holder.description = (TextView) convertView.findViewById(R.id.txt_description);
			holder.jiantou_listiem = (ImageView) convertView.findViewById(R.id.jiantou_listiem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CategoryBean bean = categories.get(position);

		imageLoader.displayImage(URLs.IMAGE_URL + bean.getCategoryImgUri(), holder.imageView, options);

		holder.textView.setText(categories.get(position).getCategoryName());

		StringBuffer descrip = new StringBuffer();
		if (categories.get(position).getCategorys().size() > 3) {
			for (int i = 0; i < 3; i++) {
				String categoryName = categories.get(position).getCategorys().get(i).getCategoryName();
				descrip.append(i <= 1 ? categoryName + "/" : categoryName);
			}
		} else {
			for (int i = 0; i < categories.get(position).getCategorys().size(); i++) {
				String categoryName = categories.get(position).getCategorys().get(i).getCategoryName();
				descrip.append(i <= categories.get(position).getCategorys().size() - 2 ? categoryName + "/" : categoryName);
			}
		}
		holder.description.setText(descrip.toString());

		if (hideFlag) {
			// 设置选中效果
			if (selectedPosition == position) {
				holder.textView.setTextColor(context.getResources().getColor(R.color.classic_selected_category_color));

			} else {
				holder.textView.setTextColor(context.getResources().getColor(R.color.classic_normal_category_color));

			}

			holder.layout.setBackgroundColor(context.getResources().getColor(R.color.classic_item_selected_category_color));
			holder.lineimage.setVisibility(View.INVISIBLE);
			holder.imageView.setVisibility(View.INVISIBLE);
			holder.jiantou_listiem.setVisibility(View.INVISIBLE);
			holder.description.setVisibility(View.INVISIBLE);
		} else {
			holder.textView.setTextColor(context.getResources().getColor(R.color.classic_selected_category_color));
			holder.layout.setBackgroundColor(Color.TRANSPARENT);

			holder.lineimage.setVisibility(View.VISIBLE);
			holder.imageView.setVisibility(View.VISIBLE);
			holder.jiantou_listiem.setVisibility(View.VISIBLE);
			holder.description.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	public class ViewHolder {
		public TextView textView;
		public ImageView imageView;
		public RelativeLayout layout;
		public TextView description;
		public ImageView lineimage;
		public ImageView jiantou_listiem;
	}

	public boolean isHideFlag() {
		return hideFlag;
	}

	public void setHideFlag(boolean hideFlag) {
		this.hideFlag = hideFlag;
	}
}
