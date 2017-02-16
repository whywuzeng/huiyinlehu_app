package com.huiyin.bean;

public class PrizeRecord {

	private String userName;
	private String prizeName;
	private String addTime;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Override
	public String toString() {
		return "PrizeRecord [userName=" + userName + ", prizeName=" + prizeName + "]";
	}

}
