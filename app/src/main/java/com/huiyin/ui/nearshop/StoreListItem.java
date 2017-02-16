package com.huiyin.ui.nearshop;

import java.io.Serializable;

import com.baidu.mapapi.model.LatLng;

public class StoreListItem implements Serializable {

	private static final long serialVersionUID = 1L;
	public String SCALE;// 店铺规模
	public String NAME;// 门店名称
	public String AREA;// 区
	public String PROVINCE;// 省
	public String ADDRESS;// 门店地址
	public int ID;// 5
	public String BUSINESS;// 营业时间
	public double LONGITUDE;// 经度
	public String CITY;// 市
	public int NUM;// 1
	public String TELEPHONE;// 联系电话
	public double LATITUDE;// 纬度
	public double distance;// 距离当前位置的距离

	public String getSCALE() {
		return SCALE;
	}

	public void setSCALE(String sCALE) {
		SCALE = sCALE;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getAREA() {
		return AREA;
	}

	public void setAREA(String aREA) {
		AREA = aREA;
	}

	public String getPROVINCE() {
		return PROVINCE;
	}

	public void setPROVINCE(String pROVINCE) {
		PROVINCE = pROVINCE;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getBUSINESS() {
		return BUSINESS;
	}

	public void setBUSINESS(String bUSINESS) {
		BUSINESS = bUSINESS;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
	}

	public int getNUM() {
		return NUM;
	}

	public void setNUM(int nUM) {
		NUM = nUM;
	}

	public String getTELEPHONE() {
		return TELEPHONE;
	}

	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}

	public LatLng getLatLng() {
		try {
			return new LatLng(LATITUDE, LONGITUDE);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return String.valueOf(distance);
	}

}
