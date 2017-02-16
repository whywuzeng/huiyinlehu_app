package com.huiyin.bean;

public class TopList {

	private String imageUrl;

	private int rowId;

	private int flag;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "TopList [imageUrl=" + imageUrl + ", rowId=" + rowId + ", flag="
				+ flag + "]";
	}

}
