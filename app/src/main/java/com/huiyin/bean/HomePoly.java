package com.huiyin.bean;

import java.util.List;

public class HomePoly {

	/** 分类聚合图片 */
	private String imageUrl;
	/** 分类聚合的id */
	private int id;
	/** 分类聚合所属类别id */
	private int typeId;
	/** 布局字段 */
	private int layout;

	private String name;

	private String typeImageUrl;

	private String linkFlag;

	private String linkId;

	private List<HomePoly> listPolies;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeImageUrl() {
		return typeImageUrl;
	}

	public void setTypeImageUrl(String typeImageUrl) {
		this.typeImageUrl = typeImageUrl;
	}

	public List<HomePoly> getListPolies() {
		return listPolies;
	}

	public void setListPolies(List<HomePoly> listPolies) {
		this.listPolies = listPolies;
	}

	public String getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	@Override
	public String toString() {
		return "HomePoly [imageUrl=" + imageUrl + ", id=" + id + ", typeId="
				+ typeId + ", layout=" + layout + ", name=" + name
				+ ", typeImageUrl=" + typeImageUrl + ", linkFlag=" + linkFlag
				+ ", linkId=" + linkId + ", listPolies=" + listPolies + "]";
	}
}
