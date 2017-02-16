package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ShowLike extends BaseBean {
	
	public ArrayList<LikeUser> like;
	public class LikeUser{
		public String FACE_IMAGE_PATH;//头像
		public String USER_NAME;//用户名
		public String ADDTIME;//添加时间
	}
	
	public static ShowLike explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			ShowLike experLightBean = gson.fromJson(json, ShowLike.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("Prefecture1", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
