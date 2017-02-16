package com.huiyin.bean;

import java.util.List;

public class Club {

	private String id;

	private String startTime;// STARTTIME 开始时间
	private String currentTime;// datetime 当前时间
	private String endTime;// ENDTIME 介绍时间
	private String nextTime;// TWO_START_TIME
	private String content;// CONTENT(Html) 活动介绍
	private String prizeSet;// WINNING 奖品设置(Html)
	private String quantity;// QUANTITY 参与人数

	private int userIntegral;// 用户积分
	private int userNum;// 用户今天抽奖次数

	private int integral;// 每次抽奖的积分
	private int num;// 一天抽奖的免费次数

	private List<Prize> listPrizes;

	private List<PrizeRecord> listPrizeRecords;

	private boolean isShut;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrizeSet() {
		return prizeSet;
	}

	public void setPrizeSet(String prizeSet) {
		this.prizeSet = prizeSet;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getUserIntegral() {
		return userIntegral;
	}

	public void setUserIntegral(int userIntegral) {
		this.userIntegral = userIntegral;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public List<Prize> getListPrizes() {
		return listPrizes;
	}

	public void setListPrizes(List<Prize> listPrizes) {
		this.listPrizes = listPrizes;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<PrizeRecord> getListPrizeRecords() {
		return listPrizeRecords;
	}

	public void setListPrizeRecords(List<PrizeRecord> listPrizeRecords) {
		this.listPrizeRecords = listPrizeRecords;
	}

	public boolean isShut() {
		return isShut;
	}

	public void setShut(boolean isShut) {
		this.isShut = isShut;
	}

	@Override
	public String toString() {
		return "Club [id=" + id + ", startTime=" + startTime + ", currentTime=" + currentTime + ", endTime=" + endTime + ", nextTime=" + nextTime + ", content=" + content + ", prizeSet=" + prizeSet
						+ ", quantity=" + quantity + ", userIntegral=" + userIntegral + ", userNum=" + userNum + ", integral=" + integral + ", num=" + num + ", listPrizes=" + listPrizes
						+ ", listPrizeRecords=" + listPrizeRecords + ", isShut=" + isShut + "]";
	}

}
