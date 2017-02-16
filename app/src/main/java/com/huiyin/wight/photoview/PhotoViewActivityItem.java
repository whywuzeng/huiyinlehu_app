package com.huiyin.wight.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.huiyin.R;
import com.huiyin.wight.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.huiyin.wight.photoview.PhotoViewAttacher.OnViewTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class PhotoViewActivityItem extends RelativeLayout {

	private Context mContext;
	private PhotoView mPhotoView;
	private DisplayImageOptions mOptions;
	private Bitmap photoBitmap;

	public PhotoViewActivityItem(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.photoview_item, this,
				true);
		findView();
		initData();
	}

	public PhotoViewActivityItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.photoview_item, this,
				true);
		findView();
		initData();
	}

	public PhotoViewActivityItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.photoview_item, this,
				true);
		findView();
		initData();
	}

	private void findView() {
		mPhotoView = (PhotoView) findViewById(R.id.photoView);
	}

	private void initData() {

		Options bitmapOptions = new Options();
		bitmapOptions.inSampleSize = 8;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.color.transparent)
				.showImageForEmptyUri(R.drawable.image_default_square)
				.decodingOptions(bitmapOptions)
				.showImageOnFail(R.drawable.image_default_square).build();

	}

	public void setImage(String url) {
		ImageLoader.getInstance().displayImage(url, mPhotoView, mOptions,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						photoBitmap = loadedImage;
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
	}

	public Bitmap getPhotoBitmap() {
		return photoBitmap;
	}

	public void CancleImage(String url) {
		ImageLoader.getInstance().getMemoryCache().remove(url);
		// ImageLoader.getInstance().cancelDisplayTask(mPhotoView);
	}

	public void setOnPhotoTapListener(OnPhotoTapListener mOnPhotoTapListener) {
		mPhotoView.setOnPhotoTapListener(mOnPhotoTapListener);
	}

	public void setOnViewTapListener(OnViewTapListener mOnViewTapListener) {
		mPhotoView.setOnViewTapListener(mOnViewTapListener);
	}
}
