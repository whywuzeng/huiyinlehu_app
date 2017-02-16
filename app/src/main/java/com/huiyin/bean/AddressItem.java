package com.huiyin.bean;

import java.io.Serializable;

public class AddressItem implements Serializable {
 
	private static final long serialVersionUID = 1L;
	public String CITY_ID;// 市
	public String ADDRESSID;// 地址条目id
	public String IS_DEFAULT;// 0否1是
	public String CONSIGNEE_PHONE;// 电话号码
	public String ADDRESS;// 详细地址
	public String CONSIGNEE_NAME;// 收件人姓名
	public String POSTAL_CODE;//邮编号码
	public String AREA_ID;// 区
	public String PROVINCE_ID;// 省
	public String LEVELADDR;// 省市县名字
}

 