package com.huiyin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.text.format.Time;

public class TimeUtils {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final SimpleDateFormat MD_FORMAT_DATE = new SimpleDateFormat(
			"MM/dd");

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	public static String getTime(Date date, SimpleDateFormat dateFormat) {
		return dateFormat.format(date);
	}

	public static String getTime(String strdate, SimpleDateFormat dateFormat) {
		Date strtodate = null;
		try {
			strtodate = DEFAULT_DATE_FORMAT.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.format(strtodate);
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * 获得当前时间,以毫秒为单位
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 获取得本周的开始和结束
	 * 
	 * @return
	 */
	public static HashMap<String, String> getWeekByDate() {
		HashMap<String, String> dayMap = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = DATE_FORMAT_DATE.format(cal.getTime());
		dayMap.put("Monday", imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = DATE_FORMAT_DATE.format(cal.getTime());
		dayMap.put("SunDay", imptimeEnd);
		return dayMap;
	}

	public static String getMonthStartDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONDAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String thisMonthStart = DATE_FORMAT_DATE.format(cal.getTime());// 本月开始（本月1号）
		cal.clear();
		HashMap<String, String> dayMap = new HashMap<String, String>();
		return thisMonthStart;
	}

	public static String getYesterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String yesterdayStart = DATE_FORMAT_DATE.format(cal.getTime());
		yesterdayStart = yesterdayStart;// 昨天开始
		cal.clear();
		return yesterdayStart;
	}

	public static boolean isTime(String s1) {
		// 时间格式转换
		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s2 = df.format(new Date());

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
		} catch (java.text.ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		if (result == 0) {
			return false;
		} else if (result < 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String getNowTime() {
		Date time = new Date();
		return DEFAULT_DATE_FORMAT.format(time);
	}
}
