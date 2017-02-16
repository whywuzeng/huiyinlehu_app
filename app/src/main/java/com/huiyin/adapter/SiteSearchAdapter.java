package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.MayLikeCommodityBean;
import com.huiyin.bean.MayLikeCommodityBean.SearchBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteSearchAdapter extends BaseAdapter {

	private MayLikeCommodityBean mayLikeList;

	private Context context;

	int size;

	private DisplayImageOptions options;

	public SiteSearchAdapter(Context context, MayLikeCommodityBean mayLikeList) {
		this.context = context;
		this.mayLikeList = mayLikeList;
		size = mayLikeList.commodity.size();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_square)
				.showImageForEmptyUri(R.drawable.image_default_square)
				.showImageOnFail(R.drawable.image_default_square)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {

		return size;
	}

	@Override
	public SearchBean getItem(int position) {

		return mayLikeList.commodity.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {

		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.site_search_gv_ll, null);
			holder = new ViewHolder();
			holder.iv_search_maylike_item = (ImageView) convertView
					.findViewById(R.id.iv_search_maylike_item);
			holder.tv_search_maylike_item = (TextView) convertView
					.findViewById(R.id.tv_search_maylike_item);
			convertView.setTag(holder);
		}
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(
				URLs.IMAGE_URL
						+ mayLikeList.commodity.get(position).COMMODITY_IMAGE_PATH,
				holder.iv_search_maylike_item, options);
		holder.tv_search_maylike_item.setText("￥"
				+ mayLikeList.commodity.get(position).PRICE);

		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_search_maylike_item;
		private TextView tv_search_maylike_item;
	}

}