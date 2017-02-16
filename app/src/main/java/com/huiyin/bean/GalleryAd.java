package com.huiyin.bean;

public class GalleryAd {

	private int rowId;

	private String imageUrl;

	private int flag;// banner跳转标志 1活动介绍,2商品详细页,3专区,4快捷服务
	
	private int huodongId;// 活动id

	public int getHuodongId() {
		return huodongId;
	}

	public void setHuodongId(int huodongId) {
		this.huodongId = huodongId;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "GalleryAd [rowId=" + rowId + ", imageUrl=" + imageUrl
				+ ", flag=" + flag + ", huodongId=" + huodongId + "]";
	}

}
