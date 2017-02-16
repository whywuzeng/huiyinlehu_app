package com.huiyin.ui.bespeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.bespeak.BespeakListBean.BespeakListItem;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BespeakListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<BespeakListItem> list;

	private DisplayImageOptions options;

	public BespeakListAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<BespeakListBean.BespeakListItem>();
		timer = new Timer(true);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_square)
				.showImageForEmptyUri(R.drawable.image_default_square)
				.showImageOnFail(R.drawable.image_default_square)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(0)).build();

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				nowSecond = nowSecond + 1000;
				notifyDataSetChanged();
			}
		};
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public BespeakListItem getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public void deleteItem() {
		list.clear();
		// notifyDataSetChanged();
	}

	public void addItem(ArrayList<BespeakListItem> temp, String date) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
			if (date != null) {
				Date now = StringUtils.toDate(date);
				nowSecond = now.getTime();
			} else {

				Date nowTime = new Date();
				nowSecond = nowTime.getTime();
			}
			// notifyDataSetChanged();
		}
		if (!isRun && list.size() > 0) {
			notifyDataSetChanged();
			timer.schedule(task, 1000, 1000);
			isRun = true;
		}
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BespeakListItem item = list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bespeak_item, null);
			holder = new ViewHolder();
			holder.bespeak_image = (ImageView) convertView
					.findViewById(R.id.bespeak_image);
			holder.mybespeak_title = (TextView) convertView
					.findViewById(R.id.mybespeak_title);
			holder.bespeak_slogan = (TextView) convertView
					.findViewById(R.id.bespeak_slogan);
			holder.bespeak_describe = (TextView) convertView
					.findViewById(R.id.bespeak_describe);
			holder.mybespeak_price = (TextView) convertView
					.findViewById(R.id.mybespeak_price);
			holder.bespeak_btn = (TextView) convertView
					.findViewById(R.id.bespeak_btn);
			holder.bespeak_time = (TextView) convertView
					.findViewById(R.id.bespeak_time);
			holder.bespeak_number = (TextView) convertView
					.findViewById(R.id.bespeak_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageManager.LoadWithServer(item.NEW_PRODUCT_PICTURE,
				holder.bespeak_image, options);
		holder.mybespeak_title.setText(item.COMMODITY_NAME);
		holder.bespeak_slogan.setText(item.SLOGAN);
		if (item.NEW_PRODUCT_DESCRIBE != null) {
			String str = item.NEW_PRODUCT_DESCRIBE.replaceAll("%", "\n");
			holder.bespeak_describe.setText(str);
			holder.bespeak_describe.setVisibility(View.VISIBLE);
		} else {
			holder.bespeak_describe.setVisibility(View.GONE);
		}
		holder.mybespeak_price.setText(MathUtil
				.priceForAppWithSign(item.BESPEAK_PRICE));
		if (isRun) {
			holder.bespeak_time.setText(TextTask(item.START_BESPEAK_TIME,
					item.END_BESPEAK_TIME));
		}
		holder.bespeak_number.setText(MathUtil.stringToInt(item.BESPEAK_NUMBER)
				+ "人已预约");
		if (item.BESPEAK_MARK != null && item.BESPEAK_MARK.equals("1")) {
			holder.bespeak_btn.setText("已预约");
		} else {
			holder.bespeak_btn.setText("立即预约");
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView bespeak_image;
		TextView mybespeak_title;
		TextView bespeak_slogan;
		TextView bespeak_describe;
		TextView mybespeak_price;
		TextView bespeak_btn;
		TextView bespeak_time;
		TextView bespeak_number;
	}

	public void destroyTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
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

	protected Spanned TextTask(String starttime, String endtime) {
		if (StringUtils.isBlank(starttime) || StringUtils.isBlank(endtime))
			return null;
		Date start = StringUtils.toDate(starttime);
		Date end = StringUtils.toDate(endtime);

		long startSecond = start.getTime();
		long endSecond = end.getTime();

		if (nowSecond < startSecond) {
			long millisUntilFinished = startSecond - nowSecond;

			int day = (int) (millisUntilFinished / 86400000);
			int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
			int minute = (int) ((millisUntilFinished % 3600000) / 60000);
			int second = (int) ((millisUntilFinished % 60000) / 1000);
			String temp = "距离预约开始：<font color=\"red\">" + day
					+ "</font>天<font color=\"red\">" + hour
					+ "</font>时<font color=\"red\">" + minute
					+ "</font>分<font color=\"red\">" + second + "</font>秒";
			return Html.fromHtml(temp);
		} else if (nowSecond < endSecond) {
			long millisUntilFinished = endSecond - nowSecond;

			int day = (int) (millisUntilFinished / 86400000);
			int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
			int minute = (int) ((millisUntilFinished % 3600000) / 60000);
			int second = (int) ((millisUntilFinished % 60000) / 1000);
			String temp = "距离预约结束：<font color=\"red\">" + day
					+ "</font>天<font color=\"red\">" + hour
					+ "</font>时<font color=\"red\">" + minute
					+ "</font>分<font color=\"red\">" + second + "</font>秒";
			return Html.fromHtml(temp);
		} else if (nowSecond >= endSecond) {
			return Html.fromHtml("已经结束<font color=\"red\">");
		}
		return null;
	}
}
