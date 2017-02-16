package com.huiyin.bean;

public class ZhuanquGallery {

	private String id;

	private String imageUrl;
	
	private String bigFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ZhuanquGallery [id=" + id + ", imageUrl=" + imageUrl
				+ ", bigFlag=" + bigFlag + "]";
	}

	public String getBigFlag() {
		return bigFlag;
	}

	public void setBigFlag(String bigFlag) {
		this.bigFlag = bigFlag;
	}

}
