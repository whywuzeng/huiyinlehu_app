package com.huiyin.adapter;

import java.util.List;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.HouseKeeper;
import com.huiyin.ui.housekeeper.HKYuYueActivity;
import com.huiyin.ui.housekeeper.HkDetailActivity;
import com.huiyin.ui.housekeeper.HouseKeeperOrderActivity;
import com.huiyin.ui.user.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HouseKeeperListAdapter extends BaseAdapter {

	private List<HouseKeeper> listDatas;

	private Context mContext;

	private LayoutInflater layoutInflater;

	private DisplayImageOptions options;

	public HouseKeeperListAdapter(Context context, List<HouseKeeper> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(0)).build();

		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return listDatas == null ? 0 : listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final HouseKeeper bean = listDatas.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.activity_housekeeper_list_item, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.hk_list_image);
			holder.content = (TextView) convertView
					.findViewById(R.id.hk_list_content);
			holder.image_label = (TextView) convertView
					.findViewById(R.id.hk_list_image_label);
			holder.btn_yuyue = (TextView) convertView
					.findViewById(R.id.btn_yuyue);
			holder.btn_detail = (TextView) convertView
					.findViewById(R.id.btn_detail);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.content.setText(bean.getAbstracting());
		holder.image_label.setText(bean.getName());
		ImageLoader.getInstance().displayImage(
				URLs.IMAGE_URL + bean.getImageUrl(), holder.image, options);

		holder.btn_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v1) {
				Intent intent = new Intent();
				intent.putExtra("id", bean.getRowId() + "");
				intent.putExtra("title", bean.getName());
				intent.setClass(mContext, HkDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				mContext.startActivity(intent);
			}
		});

		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v1) {
				Intent intent = new Intent();
				intent.putExtra("id", bean.getRowId() + "");
				intent.putExtra("title", bean.getName());
				intent.setClass(mContext, HkDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				mContext.startActivity(intent);
			}
		});

		holder.btn_yuyue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (AppContext.getInstance().getUserId() == null) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					return;
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, HouseKeeperOrderActivity.class);
					intent.putExtra("id", bean.getRowId() + "");
					intent.putExtra("title", bean.getName());
					intent.putExtra("ABSTRACTING", bean.getAbstracting());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		TextView content;
		TextView image_label;
		TextView btn_yuyue;
		TextView btn_detail;
	}

}
