package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class AppShow extends BaseBean {
	
	public Spotlight spotlight;
	
	public class Spotlight{
		public ArrayList<Info> pageBean;
	}
	
	public class Info{
		
		
		public String FACE_IMAGE_PATH;//头像
		public int USER_ID;//用户id,
		public String SPO_IMG;//晒图
		public String ADDTIME;//时间
		public String STATUS;//是否审核
		
		public ArrayList<TouXiang> touxiangList;
		
		public int ATTENTION_ID; //是否关注
		public String CONTENT;//内容
		public String LIKENUM;//喜欢人数
		public String USER_NAME;//用户名
		public int ID;//本item 的SPOTLIGHT_ID,
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
	
	public static AppShow explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShow experLightBean = gson.fromJson(json, AppShow.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShow", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
