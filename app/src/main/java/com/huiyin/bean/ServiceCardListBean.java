package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ServiceCardListBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ArrayList<OperationData> data;
	public double balance;// 当前余额

	public class OperationData {
		public String ORDERNUM;// 订单号
		public double BALANCE;// 金额
		public String TYPE;// 类型
		public String ADDDATE;// 日期
		public String TXNID;// 流水号
		public String NAME;//
		public int STAUTS;

		public String getORDERNUM() {
			return ORDERNUM;
		}

		public void setORDERNUM(String oRDERNUM) {
			ORDERNUM = oRDERNUM;
		}

		public double getBALANCE() {
			return BALANCE;
		}

		public void setBALANCE(double bALANCE) {
			BALANCE = bALANCE;
		}

		public String getTYPE() {
			return TYPE;
		}

		public void setTYPE(String tYPE) {
			TYPE = tYPE;
		}

		public String getADDDATE() {
			return ADDDATE;
		}

		public void setADDDATE(String aDDDATE) {
			ADDDATE = aDDDATE;
		}

		public String getTXNID() {
			return TXNID;
		}

		public void setTXNID(String tXNID) {
			TXNID = tXNID;
		}

		public String getNAME() {
			return NAME;
		}

		public void setNAME(String nAME) {
			NAME = nAME;
		}

		public int getSTAUTS() {
			return STAUTS;
		}

		public void setSTAUTS(int sTAUTS) {
			STAUTS = sTAUTS;
		}

	}

	public static ServiceCardListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			ServiceCardListBean experLightBean = gson.fromJson(json,
					ServiceCardListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
