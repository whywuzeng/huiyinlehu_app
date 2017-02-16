package com.huiyin.ui.flash;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.bean.BaseBean;

public class FlashListBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Expose
	private ArrayList<RegionList> regionList = new ArrayList<RegionList>();
	@Expose
	private String nowTitle;
	@Expose
	private String nextTitle;
	@Expose
	private String curDate;

	/**
	 * 
	 * @return The regionList
	 */
	public ArrayList<RegionList> getRegionList() {
		return regionList;
	}

	/**
	 * 
	 * @param regionList
	 *            The regionList
	 */
	public void setRegionList(ArrayList<RegionList> regionList) {
		this.regionList = regionList;
	}

	/**
	 * 
	 * @return The nowTitle
	 */
	public String getNowTitle() {
		return nowTitle;
	}

	/**
	 * 
	 * @param nowTitle
	 *            The nowTitle
	 */
	public void setNowTitle(String nowTitle) {
		this.nowTitle = nowTitle;
	}

	/**
	 * @return The NextTitle
	 */
	public String getNextTitle() {
		return nextTitle;
	}

	/**
	 * @param nextTitle
	 */
	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}

	/**
	 * @return The CurDate
	 */
	public String getCurDate() {
		return curDate;
	}

	/**
	 * @param curDate
	 */
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public static FlashListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			FlashListBean experLightBean = gson.fromJson(json,
					FlashListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}