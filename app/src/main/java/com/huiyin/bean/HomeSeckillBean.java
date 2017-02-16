package com.huiyin.bean;

import java.util.ArrayList;

public class HomeSeckillBean {
	public String SECKILL_NAME;// 名称
	public int ID;// 当点点击“更多”的时候，需要传递的就是这个id
	public String START_TIME;// 活动开始时间
	public String END_TIME;// 结束时间
	public String curDate;//服务器时间
	public ArrayList<HomeSeckillListItemBean> COMMODITY_LIST;

	public String getSECKILL_NAME() {
		return SECKILL_NAME;
	}

	public void setSECKILL_NAME(String sECKILL_NAME) {
		SECKILL_NAME = sECKILL_NAME;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSTART_TIME() {
		return START_TIME;
	}

	public void setSTART_TIME(String sTART_TIME) {
		START_TIME = sTART_TIME;
	}

	public String getEND_TIME() {
		return END_TIME;
	}

	public void setEND_TIME(String eND_TIME) {
		END_TIME = eND_TIME;
	}

	public ArrayList<HomeSeckillListItemBean> getCOMMODITY_LIST() {
		return COMMODITY_LIST;
	}

	public void setCOMMODITY_LIST(
			ArrayList<HomeSeckillListItemBean> cOMMODITY_LIST) {
		COMMODITY_LIST = cOMMODITY_LIST;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
}
