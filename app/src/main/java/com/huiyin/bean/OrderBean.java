package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();

	@Override
	public String toString() {
		return "OrderBean [orderList=" + orderList + "]";
	}

}