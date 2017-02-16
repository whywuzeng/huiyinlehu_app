package com.huiyin.utils.imageupload;

import java.io.Serializable;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ImageUrlBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String msg;
	public String imgurl;
	
	public static ImageUrlBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			ImageUrlBean baen = gson.fromJson(json, ImageUrlBean.class);
			return baen;
		} catch (Exception e) {
			Log.d("DataBean", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}
