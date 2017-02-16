package com.huiyin.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import com.huiyin.AppContext;

public class LHUtils {

	public static final String TAG = "CJUtils";
	private final static String PHONE = "^1[3,5,8,7]\\d{9}$";
	private static ConnectivityManager manager;

	/**
	 * 判断是否为手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		return phoneNumber.matches(PHONE);
	}

	 

	/**
	 * 过滤掉字符串中的html字符
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr) {
		if (htmlStr == null) {
			return "";
		}
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		if (htmlStr.contains("&nbsp;"))
			htmlStr = htmlStr.replaceAll("&nbsp;", "");
		return htmlStr.trim(); // 返回文本字符串
	}

	 
	/**
	 * 是否为WIFI网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWIFINet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mInfo = manager.getActiveNetworkInfo();
		if (mInfo != null && mInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 更新媒体库指定文件
	 * 
	 * @param context
	 * @param filePath
	 */
	public static void updataMeidaInfo(Context context, String filePath) {
		MediaScannerConnection.scanFile(context, new String[] { filePath },
				null, null);
	}
 
 
   
	/**
	 * 返回当前时间字符串: 例如2014年5月16日=20140516
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 返回当前时间指定格式format字符串
	 * 
	 * @return
	 */
	public static String getCurrentTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	  
 
	public static int getDuteWeek(Date date) {
		if (date == null) {
			return -1;
		}
		String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return getDuteWeek(time);
	}

	/**
	 * 获取当前孕周
	 * 
	 * @param duteTime
	 * @return
	 */
	public static int getDuteWeek(String duteTime) {
		int week = 1;
		int weekMMS = 7 * 24 * 60 * 60 * 1000;
		long totalWeekLong = 10 * 31 * 24 * 60 * 60 * 1000L;
		try {
			System.out.println("预产期:" + duteTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long duteLong = sdf.parse(duteTime + " 23:59:59").getTime();
			long currTimeLong = System.currentTimeMillis();
			/* 预产期-40周=怀孕时间 */
			long huaiyunTime = duteLong - totalWeekLong;
			/* 当前时间-怀孕时间=怀孕时长 */
			long hyWeek = currTimeLong - huaiyunTime;
			long lastTime = currTimeLong + totalWeekLong;
			if (currTimeLong < huaiyunTime || duteLong > lastTime
					|| duteLong < currTimeLong) {
				System.out.println("超出范围!");
				return -1;
			}
			// System.out.println("hyWeek=" + hyWeek + ";week=" + weekMMS);
			BigDecimal hyDecimal = new BigDecimal(hyWeek);
			BigDecimal weekDecimal = new BigDecimal(weekMMS);
			week = hyDecimal.divide(weekDecimal, 1, BigDecimal.ROUND_HALF_UP)
					.intValue();
			if (week == 0) {
				week = 1;
			} else if (week > 40) {
				week = 40;
			}
			System.out.println("孕周:" + week);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return week;
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetWorkEnable() {
		if (manager == null) {
			manager = (ConnectivityManager) AppContext.getInstance()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo[] mInfos = manager.getAllNetworkInfo();
		if (mInfos != null) {
			for (NetworkInfo mInfo : mInfos) {
				if (mInfo != null && mInfo.isAvailable() && mInfo.isConnected()) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否连接WIFI
	 * 
	 * @return
	 */
	public static boolean isWifiEnable() {
		if (manager == null) {
			manager = (ConnectivityManager) AppContext.getInstance()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo mInfo = manager.getActiveNetworkInfo();
		if (mInfo != null && mInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			return false;
		}
	}
  
}





