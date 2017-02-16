package com.huiyin.ui.seckill;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

public class SeckillTitleBean extends BaseBean {
	private static final long serialVersionUID = 1L;

	@Expose
	private ArrayList<SeckillTitle> seckillTitle = new ArrayList<SeckillTitle>();

	@SerializedName("START_TIME")
	@Expose
	private String STARTTIME;
	@SerializedName("END_TIME")
	@Expose
	private String ENDTIME;
	@Expose
	private String curDate;
	/**
	 * 
	 * @return The seckillTitle
	 */
	public ArrayList<SeckillTitle> getSeckillTitle() {
		return seckillTitle;
	}

	/**
	 * 
	 * @param seckillTitle
	 *            The seckillTitle
	 */
	public void setSeckillTitle(ArrayList<SeckillTitle> seckillTitle) {
		this.seckillTitle = seckillTitle;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public class SeckillTitle {

		@SerializedName("SECKILL_NAME")
		@Expose
		private String SECKILLNAME;
		@Expose
		private Integer ID;
		@SerializedName("START_TIME")
		@Expose
		private String STARTTIME;
		@SerializedName("END_TIME")
		@Expose
		private String ENDTIME;
		@Expose
		private Integer NUM;

		/**
		 * 
		 * @return The SECKILLNAME
		 */
		public String getSECKILLNAME() {
			return SECKILLNAME;
		}

		/**
		 * 
		 * @param SECKILLNAME
		 *            The SECKILL_NAME
		 */
		public void setSECKILLNAME(String SECKILLNAME) {
			this.SECKILLNAME = SECKILLNAME;
		}

		/**
		 * 
		 * @return The ID
		 */
		public Integer getID() {
			return ID;
		}

		/**
		 * 
		 * @param ID
		 *            The ID
		 */
		public void setID(Integer ID) {
			this.ID = ID;
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

		public String getENDTIME() {
			return ENDTIME;
		}

		public void setENDTIME(String eNDTIME) {
			ENDTIME = eNDTIME;
		}
		
	}

	public static SeckillTitleBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			SeckillTitleBean experLightBean = gson.fromJson(json,
					SeckillTitleBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
