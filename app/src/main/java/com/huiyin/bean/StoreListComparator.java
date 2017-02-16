package com.huiyin.bean;

import java.util.Comparator;

import com.huiyin.bean.NearShopBean.ShopMention;

public class StoreListComparator implements Comparator<ShopMention> {

	@Override
	public int compare(ShopMention o1, ShopMention o2) {
		return o1.distance > o2.distance ? 1 : -1;
	}

}
