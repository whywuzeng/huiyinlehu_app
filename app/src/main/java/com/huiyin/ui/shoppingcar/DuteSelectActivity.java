package com.huiyin.ui.shoppingcar;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.wight.CalendarView;
import com.huiyin.wight.CalendarView.OnItemClickListener;

public class DuteSelectActivity extends BaseActivity {

	public static final int REQUESET_CODE = 0x00001;
	private CalendarView mCalendarView;
	private ImageView mCloseIV;
	private View mNexMonthView, mPreMonthView;
	private TextView mCurrentMonthTV;
	private int count = 1;
	private String duteTime_1, duteTime_2;
	private Date date1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dute_select_layout);
		mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
		mCalendarView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void OnItemClick(Date date) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (count == 1) {
					duteTime_1 = sdf.format(date);
					date1 = date;
					count++;
				} else {
					duteTime_2 = sdf.format(date);
					if (date1.before(date)) {
						setResult(RESULT_OK, new Intent().putExtra("duteTime", duteTime_1 + "~" + duteTime_2));
					} else {
						setResult(RESULT_OK, new Intent().putExtra("duteTime", duteTime_2 + "~" + duteTime_1));
					}
					finish();
				}

			}
		});
		mCloseIV = (ImageView) findViewById(R.id.close_iv);
		mCloseIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mCurrentMonthTV = (TextView) findViewById(R.id.month_tv);
		setCurrentTime();
		mPreMonthView = findViewById(R.id.pre_month_tv);
		mPreMonthView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCalendarView.clickLeftMonth();
				setCurrentTime();
			}
		});
		mNexMonthView = findViewById(R.id.next_month_tv);
		mNexMonthView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCalendarView.clickRightMonth();
				setCurrentTime();
			}
		});
	}

	private void setCurrentTime() {

		String year = mCalendarView.getCurrentYear();
		year = year + "年";
		int monthIndex = mCalendarView.getCurrentMonth();
		switch (monthIndex) {
		case 0: {
			mCurrentMonthTV.setText(year + "一月");
		}
			break;
		case 1: {
			mCurrentMonthTV.setText(year + "二月");
		}
			break;
		case 2: {
			mCurrentMonthTV.setText(year + "三月");
		}
			break;
		case 3: {
			mCurrentMonthTV.setText(year + "四月");
		}
			break;
		case 4: {
			mCurrentMonthTV.setText(year + "五月");
		}
			break;
		case 5: {
			mCurrentMonthTV.setText(year + "六月");
		}
			break;
		case 6: {
			mCurrentMonthTV.setText(year + "七月");
		}
			break;
		case 7: {
			mCurrentMonthTV.setText(year + "八月");
		}
			break;
		case 8: {
			mCurrentMonthTV.setText(year + "九月");
		}
			break;
		case 9: {
			mCurrentMonthTV.setText(year + "十月");
		}
			break;
		case 10: {
			mCurrentMonthTV.setText(year + "十一月");
		}
			break;
		case 11: {
			mCurrentMonthTV.setText(year + "十二月");
		}
			break;
		}
	}
}
