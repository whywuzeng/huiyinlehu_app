package com.huiyin.bean;

/**
 * 支付方式
 * 
 * @author lixiaobin
 * */
public class Payment {

	private int id;
	// 支付方式名称
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", name=" + name + "]";
	}

}
