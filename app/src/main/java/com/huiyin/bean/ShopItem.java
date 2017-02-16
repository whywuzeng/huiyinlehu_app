package com.huiyin.bean;

import java.io.Serializable;

public class ShopItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int ID;// 购物车id
	public int FID;// 购物车父类id
	public String GOODS_CODE;// 货品号
	public int GOODS_STOCKS;// 货品库存
	public int COMMDOTIY_QTY;// 购买数量
	public float COMMODITY_OLD_PRICE;// 打折(促销)商品原价
	public String COMMDOITY_ID;// 商品ID
	public String IMG_PATH;// 商品介绍主图
	public float COMMODITY_PRICE; // 购买商品单价
	public String COMMODITY_NAME;// 商品名称
	public String SPECVALUE;// 属性
	public int IS_ADDED;
	public boolean isGroup = false;
	public float disCount;//折扣
	
	public String PROMOTIONS_TYPE; //商品折扣类型：1打折，2直降 ，3啥都没有
	public String PROMOTIONS_PRICE; //折后价格

	public int QUOTA_FLAG;//限购 1表示参与了，2表示没有,3表示用户已经没有购买此间商品的机会
	public String QUOTA_NUMBER;//可以购买的次数
	public String QUOTA_QUANTITY;//每次可以购买的数量

	@Override
	public String toString() {
		return "ShopItem [ID=" + ID + ", FID=" + FID + ", GOODS_CODE="
				+ GOODS_CODE + ", GOODS_STOCKS=" + GOODS_STOCKS
				+ ", COMMDOTIY_QTY=" + COMMDOTIY_QTY + ", COMMODITY_OLD_PRICE="
				+ COMMODITY_OLD_PRICE + ", COMMDOITY_ID=" + COMMDOITY_ID
				+ ", IMG_PATH=" + IMG_PATH + ", COMMODITY_PRICE="
				+ COMMODITY_PRICE + ", COMMODITY_NAME=" + COMMODITY_NAME
				+ ", SPECVALUE=" + SPECVALUE + "]";
	}

}
