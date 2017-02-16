package com.huiyin.bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class InvoiceInfoBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	public InfoBean invoiceInfo;

	public class InfoBean {
		private int USER_ID;// 1",
		private String COMPANY_NAME;// 增值税发票的单位名称
		private String COLLECTOR_NAME;// 收票人名称
		private String ACCOUNT;// 银行账户
		private String REGISTERED_PHONE;// 注册电话
		private String BANK;// 开户银行
		private String COLLECTOR_ADDRESS;// 收票人地址
		private String INVOICE_TITLE;// 发票抬头 收件人 或者 收货单位名字
		private int INVOICE_TITLE_TYPE;// 发票抬头类型：1个人，2单位
		private String COLLECTOR_PHONE;// 收票人手机
		private String REGISTERED_ADDRESS;// 注册地址
		private int INVOICING_METHOD;// 发票开具方式：0 普通发票1 增值税发票
		private int ID;// 1",
		private String IDENTIFICATION_NUMBER;// 纳税人识别号
		private int INVOICE_CONTENT;// 发票明细内容

		public int getUSER_ID() {
			return USER_ID;
		}

		public void setUSER_ID(int uSER_ID) {
			USER_ID = uSER_ID;
		}

		public String getCOMPANY_NAME() {
			return COMPANY_NAME;
		}

		public void setCOMPANY_NAME(String cOMPANY_NAME) {
			COMPANY_NAME = cOMPANY_NAME;
		}

		public String getCOLLECTOR_NAME() {
			return COLLECTOR_NAME;
		}

		public void setCOLLECTOR_NAME(String cOLLECTOR_NAME) {
			COLLECTOR_NAME = cOLLECTOR_NAME;
		}

		public String getACCOUNT() {
			return ACCOUNT;
		}

		public void setACCOUNT(String aCCOUNT) {
			ACCOUNT = aCCOUNT;
		}

		public String getREGISTERED_PHONE() {
			return REGISTERED_PHONE;
		}

		public void setREGISTERED_PHONE(String rEGISTERED_PHONE) {
			REGISTERED_PHONE = rEGISTERED_PHONE;
		}

		public String getBANK() {
			return BANK;
		}

		public void setBANK(String bANK) {
			BANK = bANK;
		}

		public String getCOLLECTOR_ADDRESS() {
			return COLLECTOR_ADDRESS;
		}

		public void setCOLLECTOR_ADDRESS(String cOLLECTOR_ADDRESS) {
			COLLECTOR_ADDRESS = cOLLECTOR_ADDRESS;
		}

		public String getINVOICE_TITLE() {
			return INVOICE_TITLE;
		}

		public void setINVOICE_TITLE(String iNVOICE_TITLE) {
			INVOICE_TITLE = iNVOICE_TITLE;
		}

		public int getINVOICE_TITLE_TYPE() {
			return INVOICE_TITLE_TYPE;
		}

		public void setINVOICE_TITLE_TYPE(int iNVOICE_TITLE_TYPE) {
			INVOICE_TITLE_TYPE = iNVOICE_TITLE_TYPE;
		}

		public String getCOLLECTOR_PHONE() {
			return COLLECTOR_PHONE;
		}

		public void setCOLLECTOR_PHONE(String cOLLECTOR_PHONE) {
			COLLECTOR_PHONE = cOLLECTOR_PHONE;
		}

		public String getREGISTERED_ADDRESS() {
			return REGISTERED_ADDRESS;
		}

		public void setREGISTERED_ADDRESS(String rEGISTERED_ADDRESS) {
			REGISTERED_ADDRESS = rEGISTERED_ADDRESS;
		}

		public int getINVOICING_METHOD() {
			return INVOICING_METHOD;
		}

		public void setINVOICING_METHOD(int iNVOICING_METHOD) {
			INVOICING_METHOD = iNVOICING_METHOD;
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}

		public String getIDENTIFICATION_NUMBER() {
			return IDENTIFICATION_NUMBER;
		}

		public void setIDENTIFICATION_NUMBER(String iDENTIFICATION_NUMBER) {
			IDENTIFICATION_NUMBER = iDENTIFICATION_NUMBER;
		}

		public int getINVOICE_CONTENT() {
			return INVOICE_CONTENT;
		}

		public void setINVOICE_CONTENT(int iNVOICE_CONTENT) {
			INVOICE_CONTENT = iNVOICE_CONTENT;
		}

	}

	public static InvoiceInfoBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			InvoiceInfoBean experLightBean = gson.fromJson(json,
					InvoiceInfoBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
