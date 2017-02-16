package com.huiyin.bean;

public class ZhuanquGoodbean {

	private String title;
	private String price;
	private String reprice;
	private int id;
	private String imagePath;

	private String saleSum;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSaleSum() {
		return saleSum;
	}

	public void setSaleSum(String saleSum) {
		this.saleSum = saleSum;
	}


	@Override
	public String toString() {
		return "ZhuanquGoodbean [title=" + title + ", price=" + price
				+ ", reprice=" + reprice + ", id=" + id + ", imagePath="
				+ imagePath + ", saleSum=" + saleSum + "]";
	}

	public String getReprice() {
		return reprice;
	}

	public void setReprice(String reprice) {
		this.reprice = reprice;
	}

}
