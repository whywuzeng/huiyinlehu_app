package com.huiyin.bean;

public class HomeSeckillListItemBean {
	public float PRICE;// 商品价格
	public float DISCOUNT;// 折扣
	public int COMMODITY_ID;// 商品id ,
	public String COMMODITY_IMAGE_PATH;// 商品主图
	public int NUM;// 1
	public String COMMODITY_NAME;// 商品名称

	public float getPRICE() {
		return PRICE;
	}

	public void setPRICE(float pRICE) {
		PRICE = pRICE;
	}

	public int getCOMMODITY_ID() {
		return COMMODITY_ID;
	}

	public void setCOMMODITY_ID(int cOMMODITY_ID) {
		COMMODITY_ID = cOMMODITY_ID;
	}

	public String getCOMMODITY_IMAGE_PATH() {
		return COMMODITY_IMAGE_PATH;
	}

	public void setCOMMODITY_IMAGE_PATH(String cOMMODITY_IMAGE_PATH) {
		COMMODITY_IMAGE_PATH = cOMMODITY_IMAGE_PATH;
	}

	public int getNUM() {
		return NUM;
	}

	public void setNUM(int nUM) {
		NUM = nUM;
	}

	public String getCOMMODITY_NAME() {
		return COMMODITY_NAME;
	}

	public void setCOMMODITY_NAME(String cOMMODITY_NAME) {
		COMMODITY_NAME = cOMMODITY_NAME;
	}

	public float getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(float dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

}
