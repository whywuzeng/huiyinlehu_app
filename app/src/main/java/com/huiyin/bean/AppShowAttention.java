package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class AppShowAttention extends BaseBean {
	
	public Spotlight list;
	
	public class Spotlight{
		public ArrayList<Info> pageBean;
	}
	
	public class Info{
		
		public ArrayList<TouXiang> touxiangList;
		
		public String FACE_IMAGE_PATH;//头像
		public String SPO_IMG;//晒图
		public int USER_ID;//用户id,
		public String LIKENUM;//喜欢人数
		public String CONTENT;//内容
		public String USER_NAME;//用户名
		public int ID;
		public String ADDTIME;//时间
		public int STATUS;//是否审核
		public int NUM;
		public String APPRAISENUM;//评价人数
	}
	
	public class TouXiang{//头像列表
		public String FACE_IMAGE_PATH;//头像
		public int USER_ID;//用户ID
		public String USER_NAME;//用户名
		public String SPOTLIGHT_ID;//列表用户ID
		public String ADDTIME;//时间
	}
	
	public static AppShowAttention explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowAttention experLightBean = gson.fromJson(json, AppShowAttention.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowGuanZhu", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
