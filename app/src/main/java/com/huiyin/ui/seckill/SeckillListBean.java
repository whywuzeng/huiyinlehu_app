package com.huiyin.ui.seckill;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

public class SeckillListBean extends BaseBean {
	private static final long serialVersionUID = 1L;
	@Expose
	private ArrayList<SeckillList> seckillList = new ArrayList<SeckillList>();

	@SerializedName("START_TIME")
	@Expose
	private String STARTTIME;
	@SerializedName("END_TIME")
	@Expose
	private String ENDTIME;

	/**
	 * 
	 * @return The seckillList
	 */
	public ArrayList<SeckillList> getSeckillList() {
		return seckillList;
	}

	/**
	 * 
	 * @param seckillList
	 *            The seckillList
	 */
	public void setSeckillList(ArrayList<SeckillList> seckillList) {
		this.seckillList = seckillList;
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

	public class SeckillList {

		@Expose
		private Float PRICE;
		@SerializedName("SECKILL_QUANTITY")
		@Expose
		private Integer SECKILLQUANTITY;
		@SerializedName("COMMODITY_ID")
		@Expose
		private Integer COMMODITYID;
		@SerializedName("COLLECT_FLAG")
		@Expose
		private Integer COLLECTFLAG;
		@SerializedName("COMMODITY_IMAGE_PATH")
		@Expose
		private String COMMODITYIMAGEPATH;
		@Expose
		private Integer NUM;
		@Expose
		private Float DISCOUNT;
		@SerializedName("COMMODITY_NAME")
		@Expose
		private String COMMODITYNAME;
		@SerializedName("SECKILL_NUMBER")
		@Expose
		private Integer SECKILLNUMBER;

		/**
		 * 
		 * @return The PRICE
		 */
		public Float getPRICE() {
			return PRICE;
		}

		/**
		 * 
		 * @param PRICE
		 *            The PRICE
		 */
		public void setPRICE(Float PRICE) {
			this.PRICE = PRICE;
		}

		/**
		 * 
		 * @return The SECKILLQUANTITY
		 */
		public Integer getSECKILLQUANTITY() {
			return SECKILLQUANTITY;
		}

		/**
		 * 
		 * @param SECKILLQUANTITY
		 *            The SECKILL_QUANTITY
		 */
		public void setSECKILLQUANTITY(Integer SECKILLQUANTITY) {
			this.SECKILLQUANTITY = SECKILLQUANTITY;
		}

		/**
		 * 
		 * @return The COMMODITYID
		 */
		public Integer getCOMMODITYID() {
			return COMMODITYID;
		}

		/**
		 * 
		 * @param COMMODITYID
		 *            The COMMODITY_ID
		 */
		public void setCOMMODITYID(Integer COMMODITYID) {
			this.COMMODITYID = COMMODITYID;
		}

		/**
		 * 
		 * @return The COMMODITYIMAGEPATH
		 */
		public String getCOMMODITYIMAGEPATH() {
			return COMMODITYIMAGEPATH;
		}

		/**
		 * 
		 * @param COMMODITYIMAGEPATH
		 *            The COMMODITY_IMAGE_PATH
		 */
		public void setCOMMODITYIMAGEPATH(String COMMODITYIMAGEPATH) {
			this.COMMODITYIMAGEPATH = COMMODITYIMAGEPATH;
		}

		/**
		 * 
		 * @return The NUM
		 */
		public Integer getNUM() {
			return NUM;
		}

		/**
		 * 
		 * @param NUM
		 *            The NUM
		 */
		public void setNUM(Integer NUM) {
			this.NUM = NUM;
		}

		/**
		 * 
		 * @return The DISCOUNT
		 */
		public Float getDISCOUNT() {
			return DISCOUNT;
		}

		/**
		 * 
		 * @param DISCOUNT
		 *            The DISCOUNT
		 */
		public void setDISCOUNT(Float DISCOUNT) {
			this.DISCOUNT = DISCOUNT;
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

		/**
		 * 
		 * @return The SECKILLNUMBER
		 */
		public Integer getSECKILLNUMBER() {
			return SECKILLNUMBER;
		}

		/**
		 * 
		 * @param SECKILLNUMBER
		 *            The SECKILL_NUMBER
		 */
		public void setSECKILLNUMBER(Integer SECKILLNUMBER) {
			this.SECKILLNUMBER = SECKILLNUMBER;
		}

		/**
		 * 
		 * @return The COLLECTFLAG
		 */
		public Integer getCOLLECTFLAG() {
			return COLLECTFLAG;
		}

		/**
		 * 
		 * @param COLLECTFLAG
		 *            The COLLECT_FLAG
		 */
		public void setCOLLECTFLAG(Integer COLLECTFLAG) {
			this.COLLECTFLAG = COLLECTFLAG;
		}
	}

	public static SeckillListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			SeckillListBean experLightBean = gson.fromJson(json,
					SeckillListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
