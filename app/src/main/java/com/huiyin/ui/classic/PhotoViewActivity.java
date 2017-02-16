package com.huiyin.ui.classic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.huiyin.R;
import com.huiyin.wight.photoview.HackyViewPager;
import com.huiyin.wight.photoview.PhotoViewActivityItem;
//import com.huiyin.wight.photoview.PhotoViewAttacher.OnPhotoTapListener;
//import com.huiyin.wight.photoview.PhotoViewAttacher.OnViewTapListener;

public class PhotoViewActivity extends Activity implements OnPageChangeListener {

	public static final String INTENT_KEY_PHOTO = "photo";
	public static final String INTENT_KEY_POSITION = "position";

	private Context mContext;
	private HackyViewPager mViewPager;
	private BeautyViewPageAdapter Vadapter;
	private List<String> listStrings;
	private int position;
	private TextView count;
	private ImageView xiazai;
	private TextView ab_back;
	// private OnPhotoTapListener mOnPhotoTapListener;
	// private OnViewTapListener mOnViewTapListener;
	private int currentPosition;

	private Map<Integer, PhotoViewActivityItem> views = new HashMap<Integer, PhotoViewActivityItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoview);
		mContext = this;
		// ImageLoader.getInstance().clearMemoryCache();
		Intent intent = getIntent();
		listStrings = intent.getStringArrayListExtra(INTENT_KEY_PHOTO);
		position = intent.getIntExtra(INTENT_KEY_POSITION, 0);
		findView();
		setListener();
		uptopView();
	}

	private void findView() {
		mViewPager = (HackyViewPager) findViewById(R.id.vPager);
		count = (TextView) findViewById(R.id.count);
		xiazai = (ImageView) findViewById(R.id.xiazai);
		ab_back = (TextView) findViewById(R.id.ab_back);
	}

	private void setListener() {
		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// mOnPhotoTapListener = new OnPhotoTapListener() {
		//
		// @Override
		// public void onPhotoTap(View view, float x, float y) {
		// finish();
		// }
		// };
		// mOnViewTapListener = new OnViewTapListener() {
		//
		// @Override
		// public void onViewTap(View view, float x, float y) {
		// finish();
		// }
		// };
	}

	private void uptopView() {
		if (listStrings != null && listStrings.size() > 0) {
			count.setText(position + 1 + "/" + listStrings.size());
			Vadapter = new BeautyViewPageAdapter();
			mViewPager.setAdapter(Vadapter);
			mViewPager.setCurrentItem(position);
			mViewPager.setOnPageChangeListener(this);
			currentPosition = position;
			xiazai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Bitmap bitmap = getCurrentImageView().getPhotoBitmap();
					if (bitmap != null) {

						File dir = new File(Environment
								.getExternalStorageDirectory().getPath()
								+ "/huiyinlehu/image");
						if (!dir.exists()) {
							dir.mkdirs();
						}
						try {
							String url = listStrings.get(mViewPager
									.getCurrentItem());
							String[] str = url.split("/");
							String FileName = str[str.length - 1];
							File file = new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/huiyinlehu/image", FileName);
							BufferedOutputStream bos = new BufferedOutputStream(
									new FileOutputStream(file));
							bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
							bos.flush();
							bos.close();
							Toast.makeText(PhotoViewActivity.this,
									"当前图片已保存到文件夹:\"" + dir.getPath() + "\"",
									Toast.LENGTH_LONG).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	public class BeautyViewPageAdapter extends PagerAdapter {

		public BeautyViewPageAdapter() {

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			try {
				container.removeView((View) object);// 删除页卡
			} catch (Exception e) {
				// 删除可能会越界
				e.printStackTrace();
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			PhotoViewActivityItem temp = new PhotoViewActivityItem(mContext);
			temp.setImage(listStrings.get(position));
			// temp.setOnPhotoTapListener(mOnPhotoTapListener);
			// temp.setOnViewTapListener(mOnViewTapListener);
			container.addView(temp, 0);// 添加页卡
			views.put(position, temp);
			return temp;
		}

		@Override
		public int getCount() {
			return listStrings.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		count.setText((position + 1) + "/" + listStrings.size());
		currentPosition = position;
	}

	private PhotoViewActivityItem getCurrentImageView() {
		return views.get(currentPosition);
	}

}
