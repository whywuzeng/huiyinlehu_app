package com.huiyin.utils;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.CountDownTimer;

/**
 * @author lian
 * 
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static String PHONE = "^1[3,5,8]\\d{9}$";

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> HourFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期什么的最烦人了
	 * 
	 * @param sdata
	 * @return
	 */
	public static String toHour(String starttime, String endtime,String curtime) {

		Date start = StringUtils.toDate(starttime);
		Date end = StringUtils.toDate(endtime);
		Date nowTime = StringUtils.toDate(curtime);

		long daymin = 1000 * 60 * 60 * 24;
		long startSecond = start.getTime();
		long endSecond = end.getTime();
		long nowSecond = nowTime.getTime();

		if (nowSecond < startSecond) {
			long sec = startSecond - nowSecond;
			if (sec > 0 && sec < daymin) {
				return HourFormater.get().format(start);
			}
			if (sec > 0 && sec < daymin * 2) {
				return "明天" + HourFormater.get().format(start);
			}
			if (sec > 0 && sec < daymin * 3) {
				return "后天" + HourFormater.get().format(start);
			}
			if (sec > 0) {
				return "即将开始";
			}
		} else if (nowSecond < endSecond) {
			long sec = nowSecond - startSecond;
			if (sec > 0 && sec < daymin) {
				return HourFormater.get().format(start);
			}
			if (sec > 0 && sec < daymin * 2) {
				return "昨天" + HourFormater.get().format(start);
			}
			if (sec > 0 && sec < daymin * 3) {
				return "前天" + HourFormater.get().format(start);
			}
			if (sec > 0) {
				return "正在进行";
			}
		} else if (nowSecond > endSecond) {
			return "已经结束";
		}

//		Date startTime = toDate(starttime);
//		Date today = new Date();
//		Date endTime = toDate(endtime);
//		long sec = startTime.getTime() - today.getTime();
//		if (sec > 0 && sec < 1000 * 60 * 60 * 24) {
//			return HourFormater.get().format(startTime);
//		}
//		if (sec > 0 && sec < 1000 * 60 * 60 * 24 * 2) {
//			return "明天" + HourFormater.get().format(startTime);
//		}
//		if (sec > 0 && sec < 1000 * 60 * 60 * 24 * 3) {
//			return "后天" + HourFormater.get().format(startTime);
//		}
//		if (sec > 0) {
//			return "即将开始";
//		}
//		if (sec < 0 && sec > -1000 * 60 * 60 * 24) {
//			return "昨天" + HourFormater.get().format(startTime);
//		}
//		if (sec < 0 && sec > -1000 * 60 * 60 * 24 * 2) {
//			return "前天" + HourFormater.get().format(startTime);
//		}
//		if (sec < 0) {
//			return "已经结束";
//		}
		return "Unknown";
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input) || "null".equals(input)
				|| "-1".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(Object arg0) {
		if (arg0 == null)
			return true;
		return false;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {

		return phoneNumber.matches(PHONE);
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	public static String getURLEncoder(String eStr) {
		String str = "";
		try {
			str = URLEncoder.encode(eStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * is null or its length is 0 or it is made by space
	 * 
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 * 
	 * @param str
	 * @return if string is null or its size is 0 or it is made by space, return
	 *         true, else return false.
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 技能人周期转换 描述这个方法的作用
	 * 
	 * @param arg0
	 * @return
	 * @Exception 异常对象
	 */
	public static String formapOrderDateForDay(String arg0) {
		String temp = "";

		if (arg0 == null) {
			return temp;
		}

		String[] tempArr = arg0.split(",");

		if (tempArr.length == 0) {
			return temp;
		}

		for (String string : tempArr) {
			String _tempName = "";
			int tempIndex = Integer.parseInt(string);
			switch (tempIndex) {
			case 1:
				_tempName = "周一";
				break;
			case 2:
				_tempName = "周二";
				break;
			case 3:
				_tempName = "周三";
				break;
			case 4:
				_tempName = "周四";
				break;
			case 5:
				_tempName = "周五";
				break;
			case 6:
				_tempName = "周六";
				break;
			case 7:
				_tempName = "周日";
				break;

			default:
				break;
			}
			temp = temp + _tempName + "、";
		}

		return temp.substring(0, temp.lastIndexOf("、"));
	}

	/**
	 * 像素值转换成dp
	 * 
	 * @param mContext
	 * @param px
	 * @return
	 */
	public static int PxToDp(Context mContext, int px) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * DP转成像素
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int DpToPx(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale);
	}

	public static String formapOrderDateForDayTag(String arg0) {
		String temp = "";

		if (arg0 == null) {
			return temp;
		}

		String[] tempArr = arg0.split(",");

		if (tempArr.length == 0) {
			return temp;
		}

		for (String string : tempArr) {
			temp = temp + string + ",";
		}

		return temp.substring(0, temp.lastIndexOf(","));
	}

	private static long lastClickTime;

	/**
	 * 防止按钮连续点击,导致界面或方法执行多次
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	// 将绝对路径转为
	public static String replaceImgSrc(String content, String replaceHttp) {
		// content = content.replace("\n", "");
		// content = content.replace("\r", "");
		// content = content.replace("\t", "");
		// String patternStr = "^.*<img\\s*.*\\s*src=\\\"(.*?)\\\"\\s*.*>.*$";
		// Pattern pattern = Pattern.compile(patternStr);
		if (content != null) {
			Pattern pattern = Pattern.compile(
					"<img[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			// 如果匹配到了img
			while (matcher.find()) {
				if (matcher.group(1).indexOf("http") < 0)
					content = content.replaceAll(matcher.group(1),
							(replaceHttp + matcher.group(1)));
			}
		}
		return content;
	}

	public static String formatHtml(String content) {
		String data = "<style>img{ max-width:100%;height:auto;} </style>"
				+ "<body>" + content + "</body";
		return data;
	}

}
