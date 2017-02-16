package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryBean implements Serializable{
	private int categoryID;// 商品id
	private String categoryName;// 商品名称
	private String categoryImgUri;// 产品图片  
	private ArrayList<CategoryBean> categorys;//包含CategoryBean
	public ArrayList<CategoryBean> getCategorys() {
		return categorys;
	}
	public void setCategorys(ArrayList<CategoryBean> categorys) {
		this.categorys = categorys;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryImgUri() {
		return categoryImgUri;
	}
	public void setCategoryImgUri(String categoryImgUri) {
		this.categoryImgUri = categoryImgUri;
	}
}
