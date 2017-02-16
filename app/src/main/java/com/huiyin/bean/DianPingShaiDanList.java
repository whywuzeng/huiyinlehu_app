package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DianPingShaiDanList implements Serializable {

	private static final long serialVersionUID = 1L;
	public String type;
	public String msg;

	public ArrayList<DianPing> reviewList = new ArrayList<DianPing>();// 也许喜欢

	public class DianPing {

		public String PHONE; // 手机号
		public String FACE_IMAGE_PATH; // 用户头像
		public String CONTENT; // 评价内容
		public String SCORE; // 分数
		public String USER_NAME; // 用户名
		public String CREATE_TIME; // 创建时间
		public String BUY_TIME; // 购买时间
		public String COMMODITY_IMAGE_PATH; // 晒图
		public String NUM; // 型号
		public String LEVEL_NAME; // 用户等级
		public String SEPC_VALUE; // 颜色 型号
		public String REPLY_STATUS; // 回复状态 1为已回复
		public String REPLY_TIME; // 回复时间
		public String REPLY_CONTENT; // 回复内容
	}
}
