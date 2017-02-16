package com.huiyin.wight;

import com.huiyin.R;
import com.huiyin.utils.ImageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageViewWithDel extends RelativeLayout {

	private Context mContext;
	private ImageView imageView;
	private ImageView del;
	private String path;
	public MyImageViewCallBack callBack;

	public ImageViewWithDel(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.image_view, this, true);
		findView();
	}

	public ImageViewWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.image_view, this, true);
		findView();
	}

	private void findView() {
		imageView = (ImageView) findViewById(R.id.img);
		del = (ImageView) findViewById(R.id.del);
		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callBack.del(ImageViewWithDel.this);
			}
		});
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callBack.imgClick();
			}
		});
		del.setVisibility(View.GONE);
	}

	/**
	 * 给图片路径加载 描述这个方法的作用
	 * 
	 * @param path
	 * @Exception 异常对象
	 */
	public void setImage(String path) {
		ImageManager.LoadWithServer(path, imageView);
		this.path = path;
		// setTag(path);
	}

	public void setImage(Drawable dr) {
		imageView.setImageDrawable(dr);
		this.path = null;
		// setTag(path);
	}

	public void setImage(Bitmap bm, String path) {
		imageView.setImageBitmap(bm);
		this.path = path;
		// setTag(path);
	}

	public String getImagePath() {
		return path;
		// return getTag().toString();
	}

	/**
	 * 删除回调 描述这个方法的作用
	 * 
	 * @param imageViewCallBack
	 * @Exception 异常对象
	 */
	public void setCallback(MyImageViewCallBack imageViewCallBack) {
		callBack = imageViewCallBack;
	}

	public void setVisibility(int visibility) {
		del.setVisibility(visibility);
	}

	public interface MyImageViewCallBack {
		public void del(View v);

		public void imgClick();
	}
}
