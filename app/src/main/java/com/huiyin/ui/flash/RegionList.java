package com.huiyin.ui.flash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegionList {

	@Expose
	private int INDEXS;
	@SerializedName("REGION_TITLE")
	@Expose
	private String REGIONTITLE;
	@Expose
	private int ID;
	@SerializedName("COMMODITY_IDS")
	@Expose
	private String COMMODITYIDS;
	@SerializedName("START_TIME")
	@Expose
	private String STARTTIME;
	@SerializedName("END_TIME")
	@Expose
	private String ENDTIME;
	@Expose
	private int STATUS;
	@Expose
	private int NUM;
	@SerializedName("ACTIVE_NAME")
	@Expose
	private String ACTIVENAME;
	@Expose
	private float DISCOUNT;
	@SerializedName("ACTIVE_ID")
	@Expose
	private int ACTIVEID;
	@Expose
	private String IMG;

	/**
	 * 
	 * @return The INDEXS
	 */
	public int getINDEXS() {
		return INDEXS;
	}

	/**
	 * 
	 * @param INDEXS
	 *            The INDEXS
	 */
	public void setINDEXS(int INDEXS) {
		this.INDEXS = INDEXS;
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
	 * @return The COMMODITYIDS
	 */
	public String getCOMMODITYIDS() {
		return COMMODITYIDS;
	}

	/**
	 * 
	 * @param COMMODITYIDS
	 *            The COMMODITY_IDS
	 */
	public void setCOMMODITYIDS(String COMMODITYIDS) {
		this.COMMODITYIDS = COMMODITYIDS;
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
	 * @return The STATUS
	 */
	public int getSTATUS() {
		return STATUS;
	}

	/**
	 * 
	 * @param STATUS
	 *            The STATUS
	 */
	public void setSTATUS(int STATUS) {
		this.STATUS = STATUS;
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
	 * @return The ACTIVENAME
	 */
	public String getACTIVENAME() {
		return ACTIVENAME;
	}

	/**
	 * 
	 * @param ACTIVENAME
	 *            The ACTIVE_NAME
	 */
	public void setACTIVENAME(String ACTIVENAME) {
		this.ACTIVENAME = ACTIVENAME;
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
	 * @return The ACTIVEID
	 */
	public int getACTIVEID() {
		return ACTIVEID;
	}

	/**
	 * 
	 * @param ACTIVEID
	 *            The ACTIVE_ID
	 */
	public void setACTIVEID(int ACTIVEID) {
		this.ACTIVEID = ACTIVEID;
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
}
