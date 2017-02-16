package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.CategoryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CategoryRightListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private ArrayList<CategoryBean> categories;
	public int foodpoition;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public CategoryRightListAdapter(Context context, ArrayList<CategoryBean> categories, int position) {
		this.categories = categories;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.foodpoition = position;

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

		return getItem(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.category_right_list_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textview);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.colorlayout);
			holder.description = (TextView) convertView.findViewById(R.id.txt_description);
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
		
		return convertView;
	}

	public static class ViewHolder {
		public TextView textView;
		public ImageView imageView;
		public RelativeLayout layout;
		public TextView description;
	}

}
