package com.huiyin.ui.flash;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

public class FlashPrefectureBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Expose
	private String curDate;
	@Expose
	private ArrayList<ProduceList> produceList = new ArrayList<ProduceList>();

	/**
	 * 
	 * @return The produceList
	 */
	public ArrayList<ProduceList> getProduceList() {
		return produceList;
	}

	/**
	 * 
	 * @param produceList
	 *            The produceList
	 */
	public void setProduceList(ArrayList<ProduceList> produceList) {
		this.produceList = produceList;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public class ProduceList {

		@SerializedName("LH_PRICE")
		@Expose
		private float LHPRICE;
		@Expose
		private float PRICE;
		@SerializedName("PRODUCE_ID")
		@Expose
		private int PRODUCEID;
		@SerializedName("REGION_TITLE")
		@Expose
		private String REGIONTITLE;
		@SerializedName("START_TIME")
		@Expose
		private String STARTTIME;
		@SerializedName("REGION_ID")
		@Expose
		private int REGIONID;
		@SerializedName("JOIN_NUM")
		@Expose
		private int JOINNUM;
		@Expose
		private String BANAER;
		@SerializedName("END_TIME")
		@Expose
		private String ENDTIME;
		@Expose
		private int NUM;
		@Expose
		private float DISCOUNT;
		@Expose
		private String IMG;
		@SerializedName("COMMODITY_NAME")
		@Expose
		private String COMMODITYNAME;

		/**
		 * 
		 * @return The LHPRICE
		 */
		public float getLHPRICE() {
			return LHPRICE;
		}

		/**
		 * 
		 * @param LHPRICE
		 *            The LH_PRICE
		 */
		public void setLHPRICE(float LHPRICE) {
			this.LHPRICE = LHPRICE;
		}

		/**
		 * 
		 * @return The PRICE
		 */
		public float getPRICE() {
			return PRICE;
		}

		/**
		 * 
		 * @param PRICE
		 *            The PRICE
		 */
		public void setPRICE(float PRICE) {
			this.PRICE = PRICE;
		}

		/**
		 * 
		 * @return The PRODUCEID
		 */
		public int getPRODUCEID() {
			return PRODUCEID;
		}

		/**
		 * 
		 * @param PRODUCEID
		 *            The PRODUCE_ID
		 */
		public void setPRODUCEID(int PRODUCEID) {
			this.PRODUCEID = PRODUCEID;
		}

		/**
		 * 
		 * @return The REGIONTITLE
		 */
		public String getREGIONTITLE() {
			return REGIONTITLE;
		}

		/**
		 * 
		 * @param REGIONTITLE
		 *            The REGION_TITLE
		 */
		public void setREGIONTITLE(String REGIONTITLE) {
			this.REGIONTITLE = REGIONTITLE;
		}

		/**
		 * 
		 * @return The STARTTIME
		 */
		public String getSTARTTIME() {
			return STARTTIME;
		}

		/**
		 * 
		 * @param STARTTIME
		 *            The START_TIME
		 */
		public void setSTARTTIME(String STARTTIME) {
			this.STARTTIME = STARTTIME;
		}

		/**
		 * 
		 * @return The REGIONID
		 */
		public int getREGIONID() {
			return REGIONID;
		}

		/**
		 * 
		 * @param REGIONID
		 *            The REGION_ID
		 */
		public void setREGIONID(int REGIONID) {
			this.REGIONID = REGIONID;
		}

		/**
		 * 
		 * @return The JOINNUM
		 */
		public int getJOINNUM() {
			return JOINNUM;
		}

		/**
		 * 
		 * @param JOINNUM
		 *            The JOIN_NUM
		 */
		public void setJOINNUM(int JOINNUM) {
			this.JOINNUM = JOINNUM;
		}

		/**
		 * 
		 * @return The BANAER
		 */
		public String getBANAER() {
			return BANAER;
		}

		/**
		 * 
		 * @param BANAER
		 *            The BANAER
		 */
		public void setBANAER(String BANAER) {
			this.BANAER = BANAER;
		}

		/**
		 * 
		 * @return The ENDTIME
		 */
		public String getENDTIME() {
			return ENDTIME;
		}

		/**
		 * 
		 * @param ENDTIME
		 *            The END_TIME
		 */
		public void setENDTIME(String ENDTIME) {
			this.ENDTIME = ENDTIME;
		}

		/**
		 * 
		 * @return The NUM
		 */
		public int getNUM() {
			return NUM;
		}

		/**
		 * 
		 * @param NUM
		 *            The NUM
		 */
		public void setNUM(int NUM) {
			this.NUM = NUM;
		}

		/**
		 * 
		 * @return The DISCOUNT
		 */
		public float getDISCOUNT() {
			return DISCOUNT;
		}

		/**
		 * 
		 * @param DISCOUNT
		 *            The DISCOUNT
		 */
		public void setDISCOUNT(float DISCOUNT) {
			this.DISCOUNT = DISCOUNT;
		}

		/**
		 * 
		 * @return The IMG
		 */
		public String getIMG() {
			return IMG;
		}

		/**
		 * 
		 * @param IMG
		 *            The IMG
		 */
		public void setIMG(String IMG) {
			this.IMG = IMG;
		}

		/**
		 * 
		 * @return The COMMODITYNAME
		 */
		public String getCOMMODITYNAME() {
			return COMMODITYNAME;
		}

		/**
		 * 
		 * @param COMMODITYNAME
		 *            The COMMODITY_NAME
		 */
		public void setCOMMODITYNAME(String COMMODITYNAME) {
			this.COMMODITYNAME = COMMODITYNAME;
		}

	}
	public static FlashPrefectureBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			FlashPrefectureBean experLightBean = gson.fromJson(json,
					FlashPrefectureBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
