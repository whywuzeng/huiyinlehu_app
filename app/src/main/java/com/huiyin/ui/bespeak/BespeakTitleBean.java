package com.huiyin.ui.bespeak;

import java.util.ArrayList;

import com.huiyin.bean.BaseBean;

public class BespeakTitleBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	//新品预约分类
	public ArrayList<BespeakTitle> content = new ArrayList<BespeakTitleBean.BespeakTitle>();
	//bannar图
	public ArrayList<BespeakBannar> appCode = new ArrayList<BespeakTitleBean.BespeakBannar>();

	public class BespeakTitle {
		public String TYPE_NAME;// 分类名称
		public Integer ID;// 分类ID
	}
	
	public class BespeakBannar {
		public String IMG;// bannar图url
	}
	
	public BespeakTitle getAllTitle() {
		BespeakTitle title = new BespeakTitle();
		title.ID = -1;
		title.TYPE_NAME = "全部";
		return title;
	}

}
