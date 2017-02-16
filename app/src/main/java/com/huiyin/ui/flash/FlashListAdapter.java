package com.huiyin.ui.flash;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("HandlerLeak")
public class FlashListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<RegionList> list;

	private DisplayImageOptions options;

	public FlashListAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<RegionList>();
		timer = new Timer(true);

		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(0)
				.showImageOnFail(0).showStubImage(0).displayer(new RoundedBitmapDisplayer(0)).build();

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				nowSecond += 1000;
				notifyDataSetChanged();
			}
		};
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
		RegionList temp = list.get(position);

		if (temp.getID() == -1) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.flash_layout_bottom, parent, false);
			}
			TextView textView_bottom = ViewHolder.get(convertView, R.id.textView_bottom);
			textView_bottom.setText(temp.getREGIONTITLE());
		} else {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.flash_list_item_layout, parent, false);
			}
			ImageView image = ViewHolder.get(convertView, R.id.imageView);
			TextView name = ViewHolder.get(convertView, R.id.textView1);
			TextView discount = ViewHolder.get(convertView, R.id.textView2);
			TextView countDown = ViewHolder.get(convertView, R.id.textView3);
			if (isRun) {
				countDown.setText(TextTask(temp.getSTARTTIME(), temp.getENDTIME()));
			}
			name.setText(temp.getREGIONTITLE());
			discount.setText(temp.getDISCOUNT() + "折起");
			ImageManager.LoadWithServer(temp.getIMG(), image, options);
		}

		return convertView;
	}

	public void deleteItem() {
		list.clear();
		// notifyDataSetChanged();
	}

	public void addItem(ArrayList<RegionList> temp, String nextTitle, String curtime) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
			if (!StringUtils.isBlank(nextTitle)) {
				RegionList test = new RegionList();
				test.setID(-1);
				test.setREGIONTITLE(nextTitle);
				list.add(test);
			}
		}
		if (!isRun && list.size() > 0) {
			Date nowTime = StringUtils.toDate(curtime);
			nowSecond = nowTime.getTime();
			notifyDataSetChanged();
			timer.schedule(task, 1000, 1000);
			isRun = true;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return ((RegionList) getItem(position)).getID() != -1 ? 0 : 1;
	}

	public int getID(int position) {
		return ((RegionList) getItem(position - 1)).getID();
	}

	private Handler mHandler;
	private Timer timer;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(69);
		}
	};
	private boolean isRun = false;
	private long nowSecond;

	protected String TextTask(String starttime, String endtime) {
		if (StringUtils.isBlank(starttime) || StringUtils.isBlank(endtime))
			return null;
		Date start = StringUtils.toDate(starttime);
		Date end = StringUtils.toDate(endtime);

		long startSecond = start.getTime();
		long endSecond = end.getTime();

		if (nowSecond < startSecond) {
			long millisUntilFinished = startSecond - nowSecond;

			int hour = (int) (millisUntilFinished / 3600000);
			int minute = (int) ((millisUntilFinished % 3600000) / 60000);
			int second = (int) ((millisUntilFinished % 60000) / 1000);
			String temp = "距离开始:" + hour + "时" + minute + "分" + second + "秒";
			return temp;
		} else if (nowSecond < endSecond) {
			long millisUntilFinished = endSecond - nowSecond;

			int hour = (int) (millisUntilFinished / 3600000);
			int minute = (int) ((millisUntilFinished % 3600000) / 60000);
			int second = (int) ((millisUntilFinished % 60000) / 1000);
			String temp = "距离结束:" + hour + "时" + minute + "分" + second + "秒";
			return temp;
		} else if (nowSecond >= endSecond) {
			return "已经结束";
		}
		return null;
	}
}
