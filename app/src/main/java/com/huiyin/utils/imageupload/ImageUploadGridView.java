package com.huiyin.utils.imageupload;

import java.util.ArrayList;
import java.util.List;

import com.huiyin.R;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.ViewHolder;
import com.huiyin.wight.MyGridView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageUploadGridView extends LinearLayout {
	private MyGridView update_grid_view;
	private ImageView add_pic;
	private Context mContext;
	private List<Bitmap> imagesBitmap;
	private ArrayList<String> imagesHostUrl;
	private MyGridAdapter mAdapter;

	public ImageUploadGridView(Context context) {
		super(context);
		mContext = context;
		findView();
		SetListener();
		InitView();

	}

	public ImageUploadGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		findView();
		SetListener();
		InitView();

	}

	public ImageUploadGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		findView();
		SetListener();
		InitView();
	}

	private void findView() {
		imagesBitmap = new ArrayList<Bitmap>();
		imagesHostUrl = new ArrayList<String>();

		LayoutInflater.from(mContext).inflate(R.layout.image_base_layout, this, true);

		update_grid_view = (MyGridView) findViewById(R.id.update_grid_view);
		add_pic = (ImageView) findViewById(R.id.add_pic);

	}

	private void SetListener() {
		add_pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					//增加一个接口
					listener.addPicOnClick();
				}
			}
		});
	}

	/**
	 * 增加图片
	 * 
	 * @param map
	 */
	public void addPics(Bitmap map, String hostPath) {

		if (map == null || StringUtils.isBlank(hostPath))
			return;
		imagesBitmap.add(map);
		imagesHostUrl.add(hostPath);
		if (imagesBitmap.size() <= 1) {
			mAdapter = new MyGridAdapter();
			update_grid_view.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
			if (imagesBitmap.size() >= 5) {
				add_pic.setVisibility(View.GONE);
			}
		}

	}

	public ArrayList<String> getImagesHostUrl() {
		return imagesHostUrl;
	}

	private void InitView() {

	}

	private addPicListener listener;

	public interface addPicListener {
		public void addPicOnClick();
	}

	public addPicListener getListener() {
		return listener;
	}

	public void setListener(addPicListener listener) {
		this.listener = listener;
	}

	public class MyGridAdapter extends BaseAdapter {

		public MyGridAdapter() {

		}

		@Override
		public int getCount() {
			return imagesBitmap == null ? 0 : imagesBitmap.size();
		}

		@Override
		public Object getItem(int position) {
			return imagesBitmap.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.image_view, null);
			}
			ImageView imageView = ViewHolder.get(convertView, R.id.img);
			ImageView del = ViewHolder.get(convertView, R.id.del);
			imageView.setImageBitmap(imagesBitmap.get(position));
			del.setOnClickListener(new delOnClick(position));
			return convertView;
		}

		public class delOnClick implements View.OnClickListener {

			int positon;

			public delOnClick(int positon) {
				this.positon = positon;
			}

			@Override
			public void onClick(View v) {
				imagesBitmap.remove(positon);
				imagesHostUrl.remove(positon);
				notifyDataSetChanged();
				add_pic.setVisibility(View.VISIBLE);
			}
		}
	}
}
