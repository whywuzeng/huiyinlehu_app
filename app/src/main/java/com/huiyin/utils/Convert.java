package com.huiyin.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Convert {

	/**
	 * 参数排序签名
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String getSignData(Map<String, String> params)
			throws Exception {
		StringBuffer content = new StringBuffer();
		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			// 具体逻辑修改下面的路径进行拼接
			if (value != null) {
				content.append(value);
			} else {
				content.append("");
			}
		}
		return content.toString();
	}

	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ArrayList<HashMap<String, String>> removeDuplicateWithOrder(ArrayList<HashMap<String, String>> list) {
        Set<HashMap<String, String>> set = new HashSet<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
        for (Iterator<HashMap<String, String>> iter = list.iterator(); iter.hasNext();) {
            HashMap<String, String> element = (HashMap<String, String>) iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }

}
