package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUserRankBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@Expose
	private String userIntegral;
	@Expose
	private int memberRank;
	@Expose
	private ArrayList<RankInfo> rankInfo = new ArrayList<RankInfo>();
	@Expose
	private String apartPrice;
	@Expose
	private String nextLevelName;
	@Expose
	private String curLevelName;
	@Expose
	private String curLevelImg;

	/**
	 * 
	 * @return The userIntegral
	 */
	public String getUserIntegral() {
		return userIntegral;
	}

	/**
	 * 
	 * @param userIntegral
	 *            The userIntegral
	 */
	public void setUserIntegral(String userIntegral) {
		this.userIntegral = userIntegral;
	}

	/**
	 * 
	 * @return The memberRank
	 */
	public int getMemberRank() {
		return memberRank;
	}

	/**
	 * 
	 * @param memberRank
	 *            The memberRank
	 */
	public void setMemberRank(int memberRank) {
		this.memberRank = memberRank;
	}

	/**
	 * 
	 * @return The rankInfo
	 */
	public ArrayList<RankInfo> getRankInfo() {
		return rankInfo;
	}

	/**
	 * 
	 * @param rankInfo
	 *            The rankInfo
	 */
	public void setRankInfo(ArrayList<RankInfo> rankInfo) {
		this.rankInfo = rankInfo;
	}

	/**
	 * 
	 * @return The apartPrice
	 */
	public String getApartPrice() {
		return apartPrice;
	}

	/**
	 * 
	 * @param apartPrice
	 *            The apartPrice
	 */
	public void setApartPrice(String apartPrice) {
		this.apartPrice = apartPrice;
	}

	/**
	 * 
	 * @return The nextLevelName
	 */
	public String getNextLevelName() {
		return nextLevelName;
	}

	/**
	 * 
	 * @param nextLevelName
	 *            The nextLevelName
	 */
	public void setNextLevelName(String nextLevelName) {
		this.nextLevelName = nextLevelName;
	}

	/**
	 * 
	 * @return The curLevelName
	 */
	public String getCurLevelName() {
		return curLevelName;
	}

	/**
	 * 
	 * @param curLevelName
	 *            The curLevelName
	 */
	public void setCurLevelName(String curLevelName) {
		this.curLevelName = curLevelName;
	}

	/**
	 * 
	 * @return The curLevelImg
	 */
	public String getCurLevelImg() {
		return curLevelImg;
	}

	/**
	 * 
	 * @param curLevelImg
	 *            The curLevelImg
	 */
	public void setCurLevelImg(String curLevelImg) {
		this.curLevelImg = curLevelImg;
	}

	public class RankInfo {

		@SerializedName("LEVEL_IMG_PATH")
		@Expose
		private String LEVELIMGPATH;
		@SerializedName("LEVEL_INTEGRAL")
		@Expose
		private float LEVELINTEGRAL;
		@Expose
		private int ID;
		@Expose
		private float DISCOUNT;
		@SerializedName("LEVEL_NAME")
		@Expose
		private String LEVELNAME;

		/**
		 * 
		 * @return The LEVELIMGPATH
		 */
		public String getLEVELIMGPATH() {
			return LEVELIMGPATH;
		}

		/**
		 * 
		 * @param LEVELIMGPATH
		 *            The LEVEL_IMG_PATH
		 */
		public void setLEVELIMGPATH(String LEVELIMGPATH) {
			this.LEVELIMGPATH = LEVELIMGPATH;
		}

		/**
		 * 
		 * @return The LEVELINTEGRAL
		 */
		public float getLEVELINTEGRAL() {
			return LEVELINTEGRAL;
		}

		/**
		 * 
		 * @param LEVELINTEGRAL
		 *            The LEVEL_INTEGRAL
		 */
		public void setLEVELINTEGRAL(float LEVELINTEGRAL) {
			this.LEVELINTEGRAL = LEVELINTEGRAL;
		}

		/**
		 * 
		 * @return The ID
		 */
		public int getID() {
			return ID;
		}

		/**
		 * 
		 * @param ID
		 *            The ID
		 */
		public void setID(int ID) {
			this.ID = ID;
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
		 * @return The LEVELNAME
		 */
		public String getLEVELNAME() {
			return LEVELNAME;
		}

		/**
		 * 
		 * @param LEVELNAME
		 *            The LEVEL_NAME
		 */
		public void setLEVELNAME(String LEVELNAME) {
			this.LEVELNAME = LEVELNAME;
		}

	}

	public static AppUserRankBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppUserRankBean experLightBean = gson.fromJson(json, AppUserRankBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
