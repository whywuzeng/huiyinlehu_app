package com.huiyin.bean;

public class HouseKeeper {

	private int rowId;// 智慧 管家id
	private String name;// 智慧 管家标题
	private String imageUrl;// 智慧 管家图片
	private String content;// 智慧 管家简介
	private String abstracting;// 摘要

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAbstracting() {
		return abstracting;
	}

	public void setAbstracting(String abstracting) {
		this.abstracting = abstracting;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "HouseKeeper [rowId=" + rowId + ", name=" + name + ", imageUrl=" + imageUrl + ", content=" + content + ", abstracting=" + abstracting + "]";
	}

}
