package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float total_price;// 总价
	public ShopItem shopItem = new ShopItem();
	public ArrayList<ShopItem> goodList = new ArrayList<ShopItem>();
	public boolean isCheck = false;
	// public boolean isNumChange = false;
	public int good_qty;

	@Override
	public String toString() {
		return "OrderItem [total_price=" + total_price + ", shopItem=" + shopItem + ", goodList=" + goodList + ", isCheck=" + isCheck + ", good_qty=" + good_qty + "]";
	}

}