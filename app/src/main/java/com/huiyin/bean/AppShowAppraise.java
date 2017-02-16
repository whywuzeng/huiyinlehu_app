package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;


public class AppShowAppraise extends BaseBean {
	
	public ArrayList<Info> spotlight;
	public ArrayList<Item> appraise;
	
	public class Info{
		
		public ArrayList<TouXiang> touxiangList;
		
		public String FACE_IMAGE_PATH;//头像
		public String SPO_IMG;//晒图
		public int USER_ID;//用户id,
		public String LIKENUM;//喜欢人数
		public String CONTENT;//内容
		public String USER_NAME;//用户名
		public String ID;//本页面的SPOTLIGHT_ID
		public String ADDTIME;//时间
		public int STATUS;//是否审核
		public String APPRAISENUM;//评价人数
		public String ATTENTION_ID; //是否关注 为 "-1" 未关注
	}
	
	public class TouXiang{//头像列表
		public String FACE_IMAGE_PATH;//头像
		public int USER_ID;//用户ID
		public String USER_NAME;//用户名
		public String SPOTLIGHT_ID;//列表用户ID
		public String ADDTIME;//时间
	}
	
	public class Item {
		public String FACE_IMAGE_PATH;//评论列表头像
		public int REPLY_ID;//被评论的id
		public int USER_ID;//提交被评论的id
		public String CONTENT;//评论的内容
		public String USER_NAME;//用户名
		public String DATETIME;//日期
		public int NUM;
		public String REPLY_NAME;//被评论的名字 null为评论，其他为回复
    }
	
	public static AppShowAppraise explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowAppraise experLightBean = gson.fromJson(json, AppShowAppraise.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
