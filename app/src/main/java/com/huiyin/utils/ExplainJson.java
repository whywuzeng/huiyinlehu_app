package com.huiyin.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


@SuppressWarnings("serial")
public class ExplainJson implements Serializable {
	
	/**json数据*/
	public String resultJson;
	public HashMap<String, String> resultMap;
	
	public ExplainJson(){
	}
	
	public static ExplainJson responseJson(String responseStr){
		ExplainJson responseBean = new ExplainJson();
		responseBean.resultJson = responseStr;
		return responseBean;
	}
	
	@SuppressWarnings("rawtypes")
    public static ExplainJson jsonToHashMap(String responseStr){
		ExplainJson responseBean = new ExplainJson();
		responseBean.resultMap = new HashMap<String,String>();
		try {
			JSONObject jsonObject = new JSONObject(responseStr);
			for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
				String key = (String)iter.next();
				String val = jsonObject.getString(key);
				responseBean.resultMap.put(key, val);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseBean;
	}
	
	/**
	 * 解释带有数据集合的json数据
	 * @param json
	 * @return  
	 */
	public static HashMap<String, Object> parseJson(String json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (json != null) {
                JSONObject object = new JSONObject(json);
                Iterator<String> iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    Object obj = object.get(key);
                    if (obj instanceof String) {
                        String value = obj.toString();
                        if ("null".equals(value)) {
                            value = "";
                        }
                        map.put(key, value);
                    } else if (obj instanceof JSONObject) {
                        HashMap<String, Object> map2 = parseJson(obj.toString());
                        if (map2 != null) {
                            map.put(key, map2);
                        }
                    } else if (obj instanceof Integer) {
                        int value = Integer.parseInt(obj.toString());
                        map.put(key, value);
                    } else if (obj instanceof Long) {
                        long value = Long.parseLong(obj.toString());
                        map.put(key, value);
                    } else if (obj instanceof Float) {
                        float value = Float.parseFloat(obj.toString());
                        map.put(key, value);
                    } else if (obj instanceof Boolean) {
                        boolean value = (Boolean) obj;
                        map.put(key, value);
                    } else if (obj instanceof JSONArray) {
                        ArrayList<HashMap<String, Object>> mapList = getArrayList((JSONArray) obj);
                        map.put(key, mapList);
                    } else {
                        map.put(key, "");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
	
	public static ArrayList<HashMap<String, Object>> getArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        int m = jsonArray.length();
        if (m > 0) {
            for (int j = 0; j < m; j++) {
                Object object2 = jsonArray.get(j);
                if (object2 instanceof JSONObject) {
                    JSONObject o = jsonArray.getJSONObject(j);
                    mapList.add(parseJson(o.toString()));
                } else if (object2 instanceof String) {
                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("keyName", object2.toString());
                    mapList.add(map2);
                }
            }
        }
        return mapList;
    }
	
	public int getIntVar(String key){
	    if (resultMap == null) {
            return -1;
        }
	    Object object = resultMap.get(key);
	    if (object == null) {
	        return -1;
        } else {
            return Integer.valueOf((String) object);
        }
	}
	
	public String getStrVar(String key){
        if (resultMap == null) {
            return "";
        }
        Object object = resultMap.get(key);
        if (object == null) {
            return "";
        } else {
            return (String) object;
        }
    }
}
