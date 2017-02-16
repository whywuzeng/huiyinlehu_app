package com.huiyin.wight;

import com.huiyin.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ViewFlipper;

public class DateTimePicker extends AlertDialog implements OnTimeChangedListener, OnDateChangedListener {
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private static final String YEAR2 = "year2";
	private static final String MONTH2 = "month2";
	private static final String DAY2 = "day2";

	private DatePicker mDatePicker;
	private DatePicker mDatePicker2;

	private static final String HOUR = "hour";
	private static final String MINUTE = "minute";
	private static final String IS_24_HOUR = "is24hour";

	private static final String HOUR2 = "hour2";
	private static final String MINUTE2 = "minute2";
	private static final String IS_24_HOUR2 = "is24hour2";

	private TimePicker mTimePicker;
	private TimePicker mTimePicker2;
	int mInitialHourOfDay;
	int mInitialMinute;
	boolean mIs24HourView;

	private Button comfirm;

	private View view;
	private ViewFlipper mViewFlipper;
	private int showCount;
	private OnDateTimeSetListener mCallBack;

	public interface OnDateTimeSetListener {

//		void onDateTimeSet(DatePicker mDatePicker, int year, int monthOfYear, int dayOfMonth, TimePicker mTimePicker,
//				int hourOfDay, int minute);

		void onDateTimeSet(Time start, Time end);
	}

	public DateTimePicker(Context context, OnDateTimeSetListener callBack, int year, int monthOfYear, int dayOfMonth,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, 0);
		setTitle("选择时间");

		mCallBack = callBack;
		mInitialHourOfDay = hourOfDay;
		mInitialMinute = minute;
		mIs24HourView = is24HourView;

		Context themeContext = getContext();
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.date_time_dialog, null);
		setView(view);

		mViewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		showCount = 0;
		mViewFlipper.setDisplayedChild(0);

		mDatePicker = (DatePicker) view.findViewById(R.id.date_picker1);
		mDatePicker2 = (DatePicker) view.findViewById(R.id.date_picker2);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
		mDatePicker2.init(year, monthOfYear, dayOfMonth, this);

		mTimePicker = (TimePicker) view.findViewById(R.id.time_picker1);
		mTimePicker2 = (TimePicker) view.findViewById(R.id.time_picker2);

		mTimePicker.setIs24HourView(mIs24HourView);
		mTimePicker.setCurrentHour(mInitialHourOfDay);
		mTimePicker.setCurrentMinute(mInitialMinute);
		mTimePicker.setOnTimeChangedListener(this);

		mTimePicker2.setIs24HourView(mIs24HourView);
		mTimePicker2.setCurrentHour(mInitialHourOfDay);
		mTimePicker2.setCurrentMinute(mInitialMinute);
		mTimePicker2.setOnTimeChangedListener(this);

		comfirm = (Button) view.findViewById(R.id.data_time_comfirm);
		comfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showCount < 3) {
					showCount++;
					mViewFlipper.setDisplayedChild(showCount);
					return;
				}

				if (mCallBack != null) {
					mDatePicker.clearFocus();
					mDatePicker2.clearFocus();
					mTimePicker.clearFocus();
					mTimePicker2.clearFocus();
					// mCallBack.onDateTimeSet(mDatePicker,
					// mDatePicker.getYear(), mDatePicker.getMonth(),
					// mDatePicker.getDayOfMonth(), mTimePicker,
					// mTimePicker.getCurrentHour(),
					// mTimePicker.getCurrentMinute());
					Time start = new Time();
					start.set(0, mTimePicker.getCurrentMinute(), mTimePicker.getCurrentHour(), mDatePicker.getDayOfMonth(),
							mDatePicker.getMonth()+1, mDatePicker.getYear());
					Time end = new Time();
					end.set(0, mTimePicker2.getCurrentMinute(), mTimePicker2.getCurrentHour(), mDatePicker2.getDayOfMonth(),
							mDatePicker2.getMonth()+1, mDatePicker2.getYear());
					if (start.before(end)) {
						mCallBack.onDateTimeSet(start, end);
					} else {
						mCallBack.onDateTimeSet(end, start);
					}

				}
				dismiss();
			}
		});
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		view.init(year, monthOfYear, dayOfMonth, null);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		state.putInt(HOUR, mTimePicker.getCurrentHour());
		state.putInt(MINUTE, mTimePicker.getCurrentMinute());
		state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());

		state.putInt(YEAR2, mDatePicker2.getYear());
		state.putInt(MONTH2, mDatePicker2.getMonth());
		state.putInt(DAY2, mDatePicker2.getDayOfMonth());
		state.putInt(HOUR2, mTimePicker2.getCurrentHour());
		state.putInt(MINUTE2, mTimePicker2.getCurrentMinute());
		state.putBoolean(IS_24_HOUR2, mTimePicker2.is24HourView());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
		int hour = savedInstanceState.getInt(HOUR);
		int minute = savedInstanceState.getInt(MINUTE);
		mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);

		int year2 = savedInstanceState.getInt(YEAR2);
		int month2 = savedInstanceState.getInt(MONTH2);
		int day2 = savedInstanceState.getInt(DAY2);
		mDatePicker2.init(year2, month2, day2, this);
		int hour2 = savedInstanceState.getInt(HOUR2);
		int minute2 = savedInstanceState.getInt(MINUTE2);
		mTimePicker2.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR2));
		mTimePicker2.setCurrentHour(hour2);
		mTimePicker2.setCurrentMinute(minute2);
	}

	// public void updateDate(int year, int monthOfYear, int dayOfMonth) {
	// mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	// }
	//
	// public void updateTime(int hourOfDay, int minutOfHour) {
	// mTimePicker.setCurrentHour(hourOfDay);
	// mTimePicker.setCurrentMinute(minutOfHour);
	// }

}
