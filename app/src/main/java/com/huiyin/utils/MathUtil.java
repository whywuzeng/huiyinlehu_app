package com.huiyin.utils;

import java.text.DecimalFormat;

/**
 * 数学、价格科学转换类
 * 
 * @author lixiaobin
 * */
public class MathUtil {

	/**
	 * String 转 Int
	 * 
	 * @param value
	 *            需要转换的值
	 */
	public static int stringToInt(String value) {
		if (value != null && !value.equals("")) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return (int) stringToDouble(value);
			}
		}
		return 0;
	}

	/**
	 * String 转 Double
	 * 
	 * @param value
	 *            需要转换的值
	 */
	public static double stringToDouble(String value) {
		if (value != null && !value.equals("")) {
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * String 转 Float
	 * 
	 * @param value
	 *            需要转换的值
	 */
	public static float stringToFloat(String value) {
		if (value != null && !value.equals("")) {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * boolean 转 int
	 * 
	 * @param value
	 *            需要转换的值
	 */
	public static int booleanToInt(boolean value) {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 最多保留一位小数点，省略小数点后面多余的0
	 */
	public static String keepMost1Decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.#");
		return df.format(value);
	}

	/**
	 * 最多保留两个小数点，省略小数点后面多余的0
	 */
	public static String keepMost2Decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.##");
		return df.format(value);
	}

	/**
	 * 保留一个小数点
	 */
	public static String keep1decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.0");
		return df.format(value);
	}

	/**
	 * 保留两个小数点
	 */
	public static String keep2decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(value);
	}

	/**
	 * 字符串保留一位小数点
	 */
	public static String stringKeep1Decimal(String value) {
		DecimalFormat df = new DecimalFormat("##0.0");
		return df.format(stringToDouble(value));
	}

	/**
	 * 字符串保留两个小数点
	 */
	public static String stringKeep2Decimal(String value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(stringToDouble(value));
	}

	/**
	 * App带人民币符号的商品的价格格式：￥1.00
	 * 
	 * @param value
	 *            需要转换的价格
	 * 
	 * */
	public static String priceForAppWithSign(String value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return "¥" + df.format(stringToDouble(value));
	}

	/**
	 * App带人民币符号的商品的价格格式：￥1.00
	 * 
	 * @param value
	 *            需要转换的价格
	 * 
	 * */
	public static String priceForAppWithSign(double value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return "¥" + df.format(value);
	}

	/**
	 * App未带人民币符号的商品的价格格式：1.00
	 * 
	 * @param value
	 *            需要转换的价格
	 * */
	public static String priceForAppWithOutSign(String value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(stringToDouble(value));
	}

	/**
	 * App未带人民币符号的商品的价格格式：1.00
	 * 
	 * @param value
	 *            需要转换的价格
	 * 
	 * */
	public static String priceForAppWithOutSign(double value) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(value);
	}

	/**
	 * 不足两位首位添0
	 * 
	 * @param value
	 * @return
	 */
	public static String intForTwoSize(int value) {
		String temp = String.valueOf(value);
		if (temp.length() <= 1) {
			return "0" + temp;
		}
		return temp;
	}
}
