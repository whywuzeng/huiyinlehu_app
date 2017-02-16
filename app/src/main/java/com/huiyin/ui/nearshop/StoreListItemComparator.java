package com.huiyin.ui.nearshop;

import java.util.Comparator;

public class StoreListItemComparator implements Comparator<StoreListItem> {

	@Override
	public int compare(StoreListItem o1, StoreListItem o2) {
		return o1.getDistance() > o2.getDistance()?1:-1;
	}
}
