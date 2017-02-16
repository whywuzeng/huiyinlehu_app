package com.huiyin.bean;

import java.util.List;

/**
 * 订单（填写订单）
 * 
 * @author lixiaobin
 * */
public class Order {

	private int id;// 订单id

	private float totalAmount;// 总额

	private float shouldPay;// 应付金额

	private Address address;// 收货人地址

	private Payment payment;// 支付方式

	private Distribution distribution;// 配送方式

	private Invoice invoice;// 发票

	private Integral integral;// 积分

	private Lehujuan lehujuan;// 乐虎劵

	private List<ShopItem> listShopItems;// 商品详情列表

	private float freight;// 运费

	private float freeFreight;// 免运费起点

	private float hujuanAmount;// 使用了的虎劵金额

	private float integralAmount;// 使用了的积分金额

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public float getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(float shouldPay) {
		this.shouldPay = shouldPay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Integral getIntegral() {
		return integral;
	}

	public void setIntegral(Integral integral) {
		this.integral = integral;
	}

	public Lehujuan getLehujuan() {
		return lehujuan;
	}

	public void setLehujuan(Lehujuan lehujuan) {
		this.lehujuan = lehujuan;
	}

	public List<ShopItem> getListShopItems() {
		return listShopItems;
	}

	public void setListShopItems(List<ShopItem> listShopItems) {
		this.listShopItems = listShopItems;
	}

	public float getFreight() {
		return freight;
	}

	public void setFreight(float freight) {
		this.freight = freight;
	}

	public float getFreeFreight() {
		return freeFreight;
	}

	public void setFreeFreight(float freeFreight) {
		this.freeFreight = freeFreight;
	}

	public float getHujuanAmount() {
		return hujuanAmount;
	}

	public void setHujuanAmount(float hujuanAmount) {
		this.hujuanAmount = hujuanAmount;
	}

	public float getIntegralAmount() {
		return integralAmount;
	}

	public void setIntegralAmount(float integralAmount) {
		this.integralAmount = integralAmount;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", totalAmount=" + totalAmount + ", shouldPay=" + shouldPay + ", address=" + address + ", payment=" + payment + ", distribution=" + distribution + ", invoice="
						+ invoice + ", integral=" + integral + ", lehujuan=" + lehujuan + ", listShopItems=" + listShopItems + ", freight=" + freight + ", freeFreight=" + freeFreight
						+ ", hujuanAmount=" + hujuanAmount + ", integralAmount=" + integralAmount + "]";
	}

	private String creatJson(List<OrderItem> orderLists) {
		String json = "[";
		for (OrderItem order : orderLists) {
			int num = order.good_qty;// 数量
			String id1 = order.shopItem.COMMDOITY_ID;
			long fid1 = order.shopItem.FID;
			String code1 = order.shopItem.GOODS_CODE;
			if (json.endsWith("}")) {
				json += ",";
			}
			String bean = "{" + "\"GOODS_CODE\":\"" + code1 + "\",\"ID\":\"" + id1 + "\",\"COMMDOTIY_QTY\":\"" + num + "\",\"FID\":\"" + fid1 + "\"}";
			json += bean;
		}
		json += "]";
		return json;
	}

}
