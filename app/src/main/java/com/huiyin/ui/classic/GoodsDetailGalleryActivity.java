package com.huiyin.ui.classic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.adapter.GoodsDetailGalleryFullScreenAdapter;
import com.huiyin.wight.GestureDetector;
import com.huiyin.wight.ImageViewTouch;
import com.huiyin.wight.ScaleGestureDetector;
import com.huiyin.wight.ViewPager;
import com.huiyin.wight.ViewPager.OnPageChangeListener;

public class GoodsDetailGalleryActivity extends Activity implements
		OnPageChangeListener, OnClickListener {

	public static final String INTENT_KEY_GALLERYS = "gallerys";
	public static final String INTENT_KEY_POSITION = "position";

	private ViewPager mViewPager;
	private GoodsDetailGalleryFullScreenAdapter mPagerAdapter;

	private ImageView xiazai;

	private TextView ab_back;

	private TextView count;

	private List<String> listStrings;

	private int position;

	private static final int PAGER_MARGIN_DP = 40;

	private boolean mPaused;
	private boolean isPlay = false;
	private boolean mOnScale = false;
	private boolean mOnPagerScoll = false;
	private boolean mControlsShow = false;
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_goods_detail_gallery);

		listStrings = getIntent().getStringArrayListExtra(INTENT_KEY_GALLERYS);

		if (listStrings != null && listStrings.size() > 0) {
			mPagerAdapter = new GoodsDetailGalleryFullScreenAdapter(this,
					listStrings);
		}

		position = getIntent().getIntExtra(INTENT_KEY_POSITION, 0);

		initView();

	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		setupOnTouchListeners(mViewPager);
		float scale = getResources().getDisplayMetrics().density;
		int pagerMarginPixels = (int) (PAGER_MARGIN_DP * scale + 0.5f);
		mViewPager.setPageMargin(pagerMarginPixels);
		mViewPager.setPageMarginDrawable(new ColorDrawable(Color.TRANSPARENT));
		mViewPager.setOnPageChangeListener(this);

		count = (TextView) findViewById(R.id.count);

		mViewPager.setOnPageChangeListener(this);
		if (mPagerAdapter != null) {
			mViewPager.setAdapter(mPagerAdapter);
			mViewPager.setCurrentItem(position, false);
			count.setText(position + 1 + "/" + listStrings.size());
		}

		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);

		xiazai = (ImageView) findViewById(R.id.xiazai);
		xiazai.setOnClickListener(this);
	}

	private void setupOnTouchListeners(View rootView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
			mScaleGestureDetector = new ScaleGestureDetector(this,
					new MyOnScaleGestureListener());
		}
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		OnTouchListener rootListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// NOTE: gestureDetector may handle onScroll..
				if (!isPlay) {
					if (!mOnScale) {
						if (!mOnPagerScoll) {
							mGestureDetector.onTouchEvent(event);
						}
					}

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
						if (!mOnPagerScoll) {
							mScaleGestureDetector.onTouchEvent(event);
						}
					}

					ImageViewTouch imageView = getCurrentImageView();
					if (imageView == null) {
						return true;
					}

					if (!mOnScale && imageView.mBitmapDisplayed != null
							&& imageView.mBitmapDisplayed.getBitmap() != null) {
						Matrix m = imageView.getImageViewMatrix();
						RectF rect = new RectF(0, 0, imageView.mBitmapDisplayed
								.getBitmap().getWidth(),
								imageView.mBitmapDisplayed.getBitmap()
										.getHeight());
						m.mapRect(rect);
						// 图片超出屏幕范围后移动
						if (!(rect.right > imageView.getWidth() + 0.1 && rect.left < -0.1)) {
							try {
								mViewPager.onTouchEvent(event);
							} catch (ArrayIndexOutOfBoundsException e) {
								// why?
							}
						}
					}
				}
				return true;
			}
		};
		rootView.setOnTouchListener(rootListener);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position, int prePosition) {
		// TODO Auto-generated method stub
		count.setText((position + 1) + "/" + listStrings.size());
	}

	private class MyOnScaleGestureListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		float currentScale;
		float currentMiddleX;
		float currentMiddleY;

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			if (!isPlay) {
				final ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return;
				}
				if (currentScale > imageView.mMaxZoom) {
					// 当前比例超过了最大比例
					imageView.zoomToNoCenterWithAni(currentScale
							/ imageView.mMaxZoom, 1, currentMiddleX,
							currentMiddleY);
					currentScale = imageView.mMaxZoom;
					imageView.zoomToNoCenterValue(currentScale, currentMiddleX,
							currentMiddleY);
				} else if (currentScale < imageView.mMinZoom) {
					// 当前比例小于最小比例
					imageView.zoomToNoCenterWithAni(currentScale,
							imageView.mMinZoom, currentMiddleX, currentMiddleY);
					currentScale = imageView.mMinZoom;
					imageView.zoomToNoCenterValue(currentScale, currentMiddleX,
							currentMiddleY);
				} else {
					imageView.zoomToNoCenter(currentScale, currentMiddleX,
							currentMiddleY);
				}
				imageView.center(true, true);

				// NOTE: 延迟修正缩放后可能移动问题
				imageView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mOnScale = false;
					}
				}, 300);
			}
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mOnScale = true;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector, float mx, float my) {
			if (!isPlay) {
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				float ns = imageView.getScale() * detector.getScaleFactor();

				currentScale = ns;
				currentMiddleX = mx;
				currentMiddleY = my;

				if (detector.isInProgress()) {
					imageView.zoomToNoCenter(ns, mx, my);
				}
			}
			return true;
		}
	}

	private class MyGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!isPlay) {
				if (mOnScale) {
					return true;
				}
				if (mPaused) {
					return false;
				}
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				imageView.panBy(-distanceX, -distanceY);
				// 超出边界效果去掉这个
				imageView.center(true, true);
			}
			return true;
		}

		@Override
		public boolean onUp(MotionEvent e) {
			return super.onUp(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mControlsShow) {
			} else {
			}
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!isPlay) {
				if (mPaused) {
					return false;
				}
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				// Switch between the original scale and 3x
				// scale.

				if (imageView.mBaseZoom < 1) {
					if (imageView.getScale() > 2F) {

						imageView.zoomTo(1f);
					} else {
						imageView.zoomToPoint(3f, e.getX(), e.getY());
					}
				} else {
					if (imageView.getScale() > (imageView.mMinZoom + imageView.mMaxZoom) / 2f) {
						imageView.zoomTo(imageView.mMinZoom);
					} else {
						imageView.zoomToPoint(imageView.mMaxZoom, e.getX(),
								e.getY());
					}
				}
			}
			return true;
		}
	}

	private ImageViewTouch getCurrentImageView() {
		return (ImageViewTouch) mPagerAdapter.views.get((mViewPager
				.getCurrentItem()));
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == ab_back) {
			finish();
		} else if (arg0 == xiazai) {
			Bitmap bitmap = (Bitmap) getCurrentImageView().getTag();
			if (bitmap != null) {

				File dir = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/huiyinlehu/image");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				try {
					String url = listStrings.get(mViewPager.getCurrentItem());
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
					Toast.makeText(GoodsDetailGalleryActivity.this,
							"当前图片已保存到文件夹:\"" + dir.getPath() + "\"",
							Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
