package com.huiyin.bean;

/**
 * 积分
 * 
 * @author lixiaobin
 * 
 * */
public class Integral {

	private float amount;// 积分总数
	private float unitAmount;// 折扣数量单位
	private float discount;// 积分折扣

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(float unitAmount) {
		this.unitAmount = unitAmount;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "Integral [amount=" + amount + ", unitAmount=" + unitAmount + ", discount=" + discount + "]";
	}

}
