package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

public class HomeBean {

	private List<GalleryAd> listGallerys;// 广告轮换图

	private List<ChannelItem> listChannelItems;
	
	private String fastImg;

	private List<TopList> listTopLists;

	private List<HomePoly> listPolies;

	private HomeSeckillBean seckillList;

	private List<SalesPromotion> listSalesPromotions;

	private HomeFlashBean mHomeFlashBean;

	private ProductBespeakBean productBespeakBean;

	public List<GalleryAd> getListGallerys() {
		if (listGallerys == null) {
			listGallerys = new ArrayList<GalleryAd>();
		}
		return listGallerys;
	}

	public void setListGallerys(List<GalleryAd> listGallerys) {
		this.listGallerys = listGallerys;
	}

	public List<ChannelItem> getListChannelItems() {
		return listChannelItems;
	}

	public void setListChannelItems(List<ChannelItem> listChannelItems) {
		this.listChannelItems = listChannelItems;
	}

	public List<TopList> getListTopLists() {
		return listTopLists;
	}

	public void setListTopLists(List<TopList> listTopLists) {
		this.listTopLists = listTopLists;
	}

	public List<SalesPromotion> getListSalesPromotions() {
		return listSalesPromotions;
	}

	public void setListSalesPromotions(List<SalesPromotion> listSalesPromotions) {
		this.listSalesPromotions = listSalesPromotions;
	}

	public List<HomePoly> getListPolies() {
		return listPolies;
	}

	public void setListPolies(List<HomePoly> listPolies) {
		this.listPolies = listPolies;
	}

	public HomeSeckillBean getSeckillList() {
		return seckillList;
	}

	public void setSeckillList(HomeSeckillBean seckillList) {
		this.seckillList = seckillList;
	}

	public HomeFlashBean getmHomeFlashBean() {
		return mHomeFlashBean;
	}

	public void setmHomeFlashBean(HomeFlashBean mHomeFlashBean) {
		this.mHomeFlashBean = mHomeFlashBean;
	}

	public ProductBespeakBean getProductBespeakBean() {
		return productBespeakBean;
	}

	public void setProductBespeakBean(ProductBespeakBean productBespeakBean) {
		this.productBespeakBean = productBespeakBean;
	}
	
	public String getFastImg() {
		return fastImg;
	}

	public void setFastImg(String fastImg) {
		this.fastImg = fastImg;
	}

	@Override
	public String toString() {
		return "HomeBean [listGallerys=" + listGallerys + ", listChannelItems="
				+ listChannelItems + ", listTopLists=" + listTopLists
				+ ", listPolies=" + listPolies + ", listSalesPromotions="
				+ listSalesPromotions + "]";
	}

}
