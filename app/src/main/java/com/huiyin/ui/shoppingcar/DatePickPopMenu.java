package com.huiyin.ui.shoppingcar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.wight.wheel.OnWheelChangedListener;
import com.huiyin.wight.wheel.OnWheelScrollListener;
import com.huiyin.wight.wheel.WheelView;
import com.huiyin.wight.wheel.adapters.AbstractWheelTextAdapter;
import com.huiyin.wight.wheel.adapters.ArrayWheelAdapter;

public class DatePickPopMenu extends PopupWindow {

	private TextView text;
	private Button confirm;
	private WheelView day, hour;
	private View view;
	private Context mContext;
	private ConfirmClick mClickListener;
	private boolean scrolling = false;
	private OnWheelScrollListener mOnWheelScrollListener;
	private OnWheelChangedListener mChangedListener;
	private DayArrayAdapter mDayArrayAdapter;

	public DatePickPopMenu(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		findView();
		initView();
		setlistener();
	}

	private void findView() {
		view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.date_time_pick_popmenu, null);
		text = (TextView) view.findViewById(R.id.time);
		confirm = (Button) view.findViewById(R.id.confirm);
		day = (WheelView) view.findViewById(R.id.wheelView1);
		hour = (WheelView) view.findViewById(R.id.wheelView2);
	}

	private void initView() {

		ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(mContext, new String[] { "9:00-12:00",
				"14:00-19:00" });
		ampmAdapter.setItemResource(R.layout.wheel_text_item);
		ampmAdapter.setItemTextResource(R.id.text);
		hour.setViewAdapter(ampmAdapter);

		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		mDayArrayAdapter = new DayArrayAdapter(mContext, calendar);
		day.setViewAdapter(mDayArrayAdapter);

		
		String temp;
		int select = day.getCurrentItem();
		temp = mDayArrayAdapter.getItemTextS(select);
		int hourselext = hour.getCurrentItem();
		if (hourselext == 0) {
			temp += "9:00-12:00";
		}
		if (hourselext == 1) {
			temp += "14:00-19:00";
		}
		text.setText(temp);
		
		setContentView(view);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setAnimationStyle(R.style.PopupAnimation);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
		setOutsideTouchable(true);
		update();
	}

	private void setlistener() {
		mOnWheelScrollListener = new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				String temp;
				int select = day.getCurrentItem();
				temp = mDayArrayAdapter.getItemTextS(select);
				int hourselext = hour.getCurrentItem();
				if (hourselext == 0) {
					temp += "9:00-12:00";
				}
				if (hourselext == 1) {
					temp += "14:00-19:00";
				}
				text.setText(temp);
			}
		};

		mChangedListener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					String temp;
					int select = day.getCurrentItem();
					temp = mDayArrayAdapter.getItemTextS(select);
					int hourselext = hour.getCurrentItem();
					if (hourselext == 0) {
						temp += "9:00-12:00";
					}
					if (hourselext == 1) {
						temp += "14:00-19:00";
					}
					text.setText(temp);
				}
			}
		};

		day.addScrollingListener(mOnWheelScrollListener);
		day.addChangingListener(mChangedListener);
		hour.addScrollingListener(mOnWheelScrollListener);
		hour.addChangingListener(mChangedListener);

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mClickListener != null) {
					mClickListener.OnConfirmClick(text.getText().toString());
				}
				dismiss();
			}
		});
	}

	public void setmClickListener(ConfirmClick mClickListener) {
		this.mClickListener = mClickListener;
	}

	public interface ConfirmClick {
		public void OnConfirmClick(String temp);
	}

	/**
	 * Day adapter
	 * 
	 */
	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		// Count of days to be shown
		private final int daysCount = 7;

		// Calendar
		Calendar calendar;

		/**
		 * Constructor
		 */
		protected DayArrayAdapter(Context context, Calendar calendar) {
			super(context, R.layout.wheel_text_item, NO_RESOURCE);
			this.calendar = calendar;

			setItemTextResource(R.id.text);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			int day = index;
			Calendar newCalendar = (Calendar) calendar.clone();
			newCalendar.roll(Calendar.DAY_OF_YEAR, day);

			View view = super.getItem(index, cachedView, parent);
			TextView text = (TextView) view.findViewById(R.id.text);
			String temp = "";
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			temp += format.format(newCalendar.getTime());
			DateFormat format1 = new SimpleDateFormat("(EEEE)");
			temp += format1.format(newCalendar.getTime());
			text.setText(temp);
			// TextView weekday = (TextView)
			// view.findViewById(R.id.time2_weekday);
			// if (day == 0) {
			// weekday.setText("");
			// } else {
			// DateFormat format = new SimpleDateFormat("(EEEE)");
			// weekday.setText(format.format(newCalendar.getTime()));
			// }
			//
			// TextView monthday = (TextView)
			// view.findViewById(R.id.time2_monthday);
			// if (day == 0) {
			// monthday.setText("Today");
			// monthday.setTextColor(0xFF0000F0);
			// } else {
			// DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// monthday.setText(format.format(newCalendar.getTime()));
			// monthday.setTextColor(0xFF111111);
			// }

			return view;
		}

		@Override
		public int getItemsCount() {
			return daysCount;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		public String getItemTextS(int index) {
			int day = index;
			Calendar newCalendar = (Calendar) calendar.clone();
			newCalendar.roll(Calendar.DAY_OF_YEAR, day);
			String temp = "";
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			temp += format.format(newCalendar.getTime());
			DateFormat format1 = new SimpleDateFormat("(EEEE)");
			temp += format1.format(newCalendar.getTime());
			text.setText(temp);
			return temp;
		}
	}
}
