package com.huiyin.ui.show.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.utils.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ShowSharePicGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> list;
	private ArrayList<String> listDatas;
	float scaleWidth;
	float scaleHeight;
	Bitmap bp;
	int h;
	boolean num = false;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private int width;

	public ShowSharePicGridViewAdapter(Context mContext) {
		this.mContext = mContext;
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth()
				- DensityUtil.dip2px(mContext, 20);
		list = new ArrayList<String>();
		listDatas = new ArrayList<String>();

		imageLoader = ImageLoader.getInstance();

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

		return list.size();

	}

	@Override
	public Object getItem(int position) {

		return list.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShareViewHolder holder = null;
		if (convertView == null) {
			holder = new ShareViewHolder();
			convertView = View.inflate(mContext, R.layout.share_iamge_item,
					null);
			holder.shareimg = (ImageView) convertView
					.findViewById(R.id.iv_share_image_item);
			convertView.setTag(holder);
		} else {
			holder = (ShareViewHolder) convertView.getTag();
		}

		if (list.size() == 1 || list.size() == 2 || list.size() == 4) {
			convertView.setLayoutParams(new GridView.LayoutParams((width / 2)
					- DensityUtil.dip2px(mContext, 2), width / 2));
		} else {
			convertView.setLayoutParams(new GridView.LayoutParams((width / 3)
					- DensityUtil.dip2px(mContext, 2), width / 3));
		}

		String imageUrl = listDatas.get(position);
		imageLoader.displayImage(imageUrl, holder.shareimg, options);

		return convertView;
	}

	class ShareViewHolder {
		ImageView shareimg;
	}

	public void addItem(ArrayList<String> temp) {
		if (temp == null || temp.size() == 0) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
			for (String string : temp) {
				listDatas.add(URLs.IMAGE_URL + string);
			}
		}
		notifyDataSetChanged();
	}

	public ArrayList<String> getListDatas() {
		return listDatas;
	}
}
