package com.huiyin.ui.bespeak;

import java.util.ArrayList;

import com.huiyin.bean.BaseBean;

public class BespeakListBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public ArrayList<BespeakListItem> content = new ArrayList<BespeakListBean.BespeakListItem>();

	public class BespeakListItem {
		public String START_BESPEAK_TIME;// 开始预约时间
		public String END_BESPEAK_TIME;// 结束预约时间
		public String NEW_PRODUCT_PICTURE;// 新品图片
		public String COMMODITY_NAME;// 商品名称
		public String SLOGAN;// 广告标语
		public String NEW_PRODUCT_DESCRIBE;// 新品描述
		public String BESPEAK_PRICE;// 预约价
		public String BESPEAK_NUMBER;// 预约数量
		public String COMMODITY_ID;// 商品ID
		public int ID;// 预约ID
		public String BESPEAK_MARK;// 预约状态 1 为 已预约
	}
}
