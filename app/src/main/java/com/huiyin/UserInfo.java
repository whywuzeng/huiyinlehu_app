package com.huiyin;

import java.io.Serializable;

public class UserInfo implements Serializable {

	public static final String TAG = "UserInfo";
	private static final long serialVersionUID = 1L;

	public String myCouponOut; // 即将过期的优惠券
	public String level; // 用户等级
	public String countWaitPay; // 待支付数量
	public String countDelivery; // 待收货数量
	public String commentsStatus; // 待评价数量
	public String countChangeReplace; // 退换货中数量
	public String userId; // 用户id
	public String myCouponAll; // 所有的优惠券
	public String img; // 用户头像
	public String lastDate;// 最后登录时间
	public String userName;// 用户名
	public String integral;// 积分
	public String countReservationType;// 预约申请数
	public String systemMessage;// 系统消息
	public String createDate;// 注册时间
	public String phone;// 电话号码
	public int bdStatus;// 便民生活卡是否绑定 绑定状态，0：未绑定 1：绑定
	public String cardNum;// 卡号
	public String token = "";// 令牌
	public double balance;// 便民服务卡金额
	public int SHOPPING_CAR;//购物车数量
}
