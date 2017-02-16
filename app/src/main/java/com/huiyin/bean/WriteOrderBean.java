package com.huiyin.bean;

import java.util.ArrayList;

public class WriteOrderBean {

	public Address shoppingAddress = new Address();
	public UserMap userMap = new UserMap();
	public float price;
	// 支付
	public Method payMethod;
	// 配送
	public DiscountManagement distributionManagementList;
	// 配送时间
	public String deliveryTime;

	public ArrayList<Discount> discountList = new ArrayList<Discount>();
	public ArrayList<ShopItem> shoppingCar = new ArrayList<ShopItem>();

	public float freight = 0;// 满38元免运费，有可能不传这个字段，使用时做空判断。
	public String promotions_name;// 运费优惠信息
	public String discount ;// 折扣系数

	public class Address {

		public String CONSIGNEE_PHONE; // 电话
		public String ADDRESS; // 详细地址
		public String ID; // 地址id
		public String CONSIGNEE_NAME; // 收货人
		public String PROVINCE;//省
		public String CITY;//市
		public String AREA;//区

	}

	public class UserMap {
		public String INTEGRAL; // 积分
		public String USERID; // 用户id

		public String integerOffset;
		public String integerDiscount;// 积分换算率
	}

	public class Discount {
		public String ENDTIME;// 截止时间
		public String PRICE;// 面值
		public String NAME;// 名称
		public String ID;// 优惠券ID
		public String DEMAND;// 优惠券要求
		public String TYPE;// 优惠券类型：1满额，2面值

	}

	public class Method {
		public String PAYMETHOD_ID;// 支付id
		public String PAYMETHOD_NAME;// 在线支付
	}

	public class DiscountManagement {
		public String DIS_MANAG_ID; // 配送id
		public String DIS_MANAG_NAME;// 配送方式
	}

	@Override
	public String toString() {
		return "WriteOrderBean [shoppingAddress=" + shoppingAddress + ", userMap=" + userMap + ", price=" + price + ", payMethod=" + payMethod + ", distributionManagementList="
						+ distributionManagementList + ", deliveryTime=" + deliveryTime + ", discountList=" + discountList + ", shoppingCar=" + shoppingCar + "]";
	}

}