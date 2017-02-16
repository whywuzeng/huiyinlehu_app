package com.huiyin.bean;

import java.util.Comparator;

public class ChannelItemComparator implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {
		ChannelItem item1 = (ChannelItem) arg0;
		ChannelItem item2 = (ChannelItem) arg1;

		// 首先比较排序的id
		if (item1.getOrderId() > item2.getOrderId()) {
			return 1;
		} else if (item1.getOrderId() == item2.getOrderId()) {
			return 0;
		} else {
			return -1;
		}
	}
}
