package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ChannelItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * */
public class LnkToolsGridViewAdapter extends BaseAdapter {

	private List<ChannelItem> listDatas;
	public LayoutInflater layoutInflater;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private int APP_PAGE_SIZE = 8;
	int page;
	
	public LnkToolsGridViewAdapter(Context context,
			List<ChannelItem> listDatas1, int page) {
		layoutInflater = LayoutInflater.from(context);
		// this.listDatas = listDatas;
        this.page = page;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();
		
		if(page == 0){
			APP_PAGE_SIZE = 7;
		}

		listDatas = new ArrayList<ChannelItem>();
		int i;
		if(page * APP_PAGE_SIZE > 0){
			i = page * APP_PAGE_SIZE - 1;
		}else{
		    i = page * APP_PAGE_SIZE;
		}
		int iEnd = i + APP_PAGE_SIZE;
		while ((i < listDatas1.size()) && (i < iEnd)) {
			listDatas.add(listDatas1.get(i));
			i++;
		}

	}
	
	
	public LnkToolsGridViewAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
		this.page = 1;
		ChannelItem channelItem = new ChannelItem();
		channelItem.setChannelId(11);
		channelItem.setImageUrl("");
		channelItem.setName("更多");
		listDatas = new ArrayList<ChannelItem>();
		listDatas.add(channelItem);
	}

	@Override
	public int getCount() {
		if (page==0) {
			return listDatas.size() + 1;
		}
		return listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		holder = new ViewHolder();
		convertView = layoutInflater.inflate(R.layout.fragment_lnktools_item,
				null);
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.image = (ImageView) convertView.findViewById(R.id.image);
		// holder.line_right = convertView.findViewById(R.id.line_right);
		// holder.line_top = convertView.findViewById(R.id.line_top);
		// holder.line_left = convertView.findViewById(R.id.line_left);
		// holder.line_bottom = convertView.findViewById(R.id.line_bottom);

		ChannelItem bean = null;
		if (page == 0 && position >= listDatas.size()) {
			bean = new ChannelItem();
			bean.setChannelId(11);
			bean.setImageUrl("");
			bean.setName("更多");
		} else {
			bean = listDatas.get(position);
		}

		holder.name.setText(bean.getName());

		if (position >= listDatas.size() || bean.getImageUrl().equals("")
				|| bean.getName().equals("更多")) {
			holder.image.setImageResource(R.drawable.ic_tools_add);
		} else {
			imageLoader.displayImage(bean.getImageUrl(), holder.image, options);
		}

		// if (position / 4 < 1) {
		// holder.line_top.setVisibility(View.GONE);
		// } else {
		// holder.line_top.setVisibility(View.VISIBLE);
		// }
		//
		// if (position % 4 == 0) {
		// holder.line_left.setVisibility(View.GONE);
		// } else {
		// holder.line_left.setVisibility(View.VISIBLE);
		// }
		//
		// if (position == listDatas.size() && position % 4 != 3) {
		// holder.line_right.setVisibility(View.VISIBLE);
		// } else {
		// holder.line_right.setVisibility(View.GONE);
		// }
		//
		// if (position + 4 > listDatas.size()
		// && (position / 4) * 4 + 4 < listDatas.size()) {
		// holder.line_bottom.setVisibility(View.VISIBLE);
		// } else {
		// holder.line_bottom.setVisibility(View.GONE);
		// }

		convertView.setOnClickListener(new MyClickListener(bean));

		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		TextView name;
		ImageView image;

		View line_right;
		View line_top;
		View line_left;
		View line_bottom;
	}

	public interface BackItemClickListener {
		void backitenClick(ChannelItem bean);
	}

	BackItemClickListener backItemClickListener;

	public void setBackItemClickListener(
			BackItemClickListener backItemClickListener) {
		this.backItemClickListener = backItemClickListener;
	}

	class MyClickListener implements View.OnClickListener {
		ChannelItem bean;

		public MyClickListener(ChannelItem bean) {
			this.bean = bean;
		}

		@Override
		public void onClick(View v) {
			backItemClickListener.backitenClick(bean);
		}

	}

}
