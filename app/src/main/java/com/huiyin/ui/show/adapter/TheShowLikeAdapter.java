package com.huiyin.ui.show.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ShowLike.LikeUser;
import com.huiyin.ui.show.view.CircularImage;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TheShowLikeAdapter extends BaseAdapter {

	private Context mContext;
	private List<LikeUser> list;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public TheShowLikeAdapter(Context mContext) {

		this.mContext = mContext;
		list = new ArrayList<LikeUser>();

		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.drawable.default_head)
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
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

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.theshow_like_item,
					null);
			holder.headimage = (CircularImage) convertView
					.findViewById(R.id.theshow_like_touxiang);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		imageLoader
				.displayImage(URLs.IMAGE_URL
						+ list.get(position).FACE_IMAGE_PATH, holder.headimage,
						options);

		holder.tv_name.setText(list.get(position).USER_NAME);
		holder.tv_time
				.setText(StringUtils.friendly_time(list.get(position).ADDTIME));

		return convertView;

	}

	class ViewHolder {
		CircularImage headimage;
		TextView tv_name;
		TextView tv_time;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<LikeUser> temp) {
		if (temp == null || temp.size() == 0) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}

}
