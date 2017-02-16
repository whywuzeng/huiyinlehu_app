package com.huiyin.ui.home.Logistics;

import java.io.Serializable;

public class OrderLogisticsListBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String PRODUCTNAME;// ERP商品名称
	public String RETURNTIME;// 回执时间
	public int ORDER_ID;// 订单id
	public String WORKTIME;// 派工时间
	public String DELIVERYMAN;// 送货人
	public String MOBILE;// 送货人电话
	public String DELIVERYTIME;// 2014-11-18 12:11:00 送货时间
	public String ERP_CODE;// 单号（ERP返回的送货单号）
	public int ID;// 物流id
	public int TYPE = -1;// 状态（0 未出库，1已发货，2 已收货）

	public String getPRODUCTNAME() {
		return PRODUCTNAME;
	}

	public void setPRODUCTNAME(String pRODUCTNAME) {
		PRODUCTNAME = pRODUCTNAME;
	}

	public String getRETURNTIME() {
		return RETURNTIME;
	}

	public void setRETURNTIME(String rETURNTIME) {
		RETURNTIME = rETURNTIME;
	}

	public int getORDER_ID() {
		return ORDER_ID;
	}

	public void setORDER_ID(int oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}

	public String getWORKTIME() {
		return WORKTIME;
	}

	public void setWORKTIME(String wORKTIME) {
		WORKTIME = wORKTIME;
	}

	public String getDELIVERYMAN() {
		return DELIVERYMAN;
	}

	public void setDELIVERYMAN(String dELIVERYMAN) {
		DELIVERYMAN = dELIVERYMAN;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getDELIVERYTIME() {
		return DELIVERYTIME;
	}

	public void setDELIVERYTIME(String dELIVERYTIME) {
		DELIVERYTIME = dELIVERYTIME;
	}

	public String getERP_CODE() {
		return ERP_CODE;
	}

	public void setERP_CODE(String eRP_CODE) {
		ERP_CODE = eRP_CODE;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getTYPE() {
		return TYPE;
	}

	public void setTYPE(int tYPE) {
		TYPE = tYPE;
	}

}
