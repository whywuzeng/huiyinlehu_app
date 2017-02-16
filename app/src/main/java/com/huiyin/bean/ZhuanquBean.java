package com.huiyin.bean;

import java.util.List;

public class ZhuanquBean {
	
	private String title;

	private String layout;
	
	private List<ZhuanquGoodbean> listGoodbeans;

	private List<ZhuanquGallery> listGalleries;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ZhuanquGoodbean> getListGoodbeans() {
		return listGoodbeans;
	}

	public void setListGoodbeans(List<ZhuanquGoodbean> listGoodbeans) {
		this.listGoodbeans = listGoodbeans;
	}

	public List<ZhuanquGallery> getListGalleries() {
		return listGalleries;
	}

	public void setListGalleries(List<ZhuanquGallery> listGalleries) {
		this.listGalleries = listGalleries;
	}

	

	@Override
	public String toString() {
		return "ZhuanquBean [title=" + title + ", layout=" + layout
				+ ", listGoodbeans=" + listGoodbeans + ", listGalleries="
				+ listGalleries + "]";
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

}
