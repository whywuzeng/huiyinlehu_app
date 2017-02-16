package com.huiyin.ui.bespeak;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.ui.bespeak.BespeakTitleBean.BespeakBannar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BespeakViewflowAdapter extends BaseAdapter {

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Context mContext;

	private ArrayList<BespeakBannar> listDatas;

	public BespeakViewflowAdapter(Context context,
			ArrayList<BespeakBannar> listDatas) {
		mContext = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_gallery)
				.showImageForEmptyUri(R.drawable.image_default_gallery)
				.showImageOnFail(R.drawable.image_default_gallery)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(0)).build();

		imageLoader = ImageLoader.getInstance();
		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BespeakBannar bean = listDatas.get(position % listDatas.size());
		imageLoader.displayImage(URLs.IMAGE_URL + bean.IMG,
				viewHolder.imageView, options);

		viewHolder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
	}
}
