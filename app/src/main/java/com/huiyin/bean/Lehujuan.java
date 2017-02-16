package com.huiyin.bean;

import java.io.Serializable;

public class Lehujuan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// 主键

	private float amount;// 面值

	private int count;// 共几张劵

	private String date;

	private boolean isChecked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "Lehujuan [id=" + id + ", amount=" + amount + ", count=" + count + ", date=" + date + ", isChecked=" + isChecked + "]";
	}

}
