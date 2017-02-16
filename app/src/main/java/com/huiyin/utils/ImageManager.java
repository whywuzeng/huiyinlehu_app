package com.huiyin.utils;

import android.widget.ImageView;

import com.huiyin.api.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageManager {
	
	public static void Load(String imgUrl, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imgUrl, imageView);
	}

	public static void Load(String imgUrl, ImageView imageView, DisplayImageOptions o) {
		ImageLoader.getInstance().displayImage(imgUrl, imageView, o);
	}

	public static void LoadWithServer(String imgUrl, ImageView imageView) {
		ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imgUrl, imageView);
	}

	public static void LoadWithServer(String imgUrl, ImageView imageView, DisplayImageOptions o) {
		ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imgUrl, imageView, o);
	}
}
