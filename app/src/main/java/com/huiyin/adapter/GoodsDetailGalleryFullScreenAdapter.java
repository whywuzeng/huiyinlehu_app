package com.huiyin.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.huiyin.wight.ImageViewTouch;
import com.huiyin.wight.PagerAdapter;
import com.huiyin.wight.ViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GoodsDetailGalleryFullScreenAdapter extends PagerAdapter {

	@SuppressLint("UseSparseArrays")
	private List<String> listDatas;
	public LayoutInflater layoutInflater;

	public Map<Integer, ImageViewTouch> views = new HashMap<Integer, ImageViewTouch>();

	private Context mContext;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public GoodsDetailGalleryFullScreenAdapter(Context context,
			List<String> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		this.listDatas = listDatas;
		mContext = context;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return listDatas.size();
	}

	@Override
	public Object instantiateItem(View view, final int position) {
		final ImageViewTouch imageView = new ImageViewTouch(mContext);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		imageView.setBackgroundColor(Color.TRANSPARENT);
		imageView.setFocusableInTouchMode(true);
		String imageUrl = listDatas.get(position);
		imageLoader.displayImage(imageUrl, imageView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String str = listDatas.get(position);
						str = "assets://image_default_square.png";
						imageLoader.displayImage(str, imageView);
						notifyDataSetChanged();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						((ImageViewTouch) view).setImageBitmapResetBase(
								loadedImage, true);
						imageView.setTag(loadedImage);
					}
				});

		views.put(position, imageView);

		((ViewPager) view).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
		views.remove(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}
}