package com.huiyin.bean;

import java.io.Serializable;

public class BrowseItem implements Serializable {

	public String PRICE; // 商品价格
	public String COMMODITY_ID; // 商品id
	public String COMMODITY_IMAGE_PATH; // 商品图标
	public String COMMODITY_NAME; // 商品名称

	@Override
	public String toString() {
		return "BrowseItem [PRICE=" + PRICE + ", COMMODITY_ID=" + COMMODITY_ID + ", COMMODITY_IMAGE_PATH=" + COMMODITY_IMAGE_PATH + ", COMMODITY_NAME=" + COMMODITY_NAME + "]";
	}

}