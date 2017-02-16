package com.huiyin.bean;

/**
 * 配送方式
 * 
 * @author lixiaobin
 * 
 * */
public class Distribution {

	private int id;

	private String name;

	private String startTime;// 配送起始时间

	private String endTime;// 配送截止时间

	private String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Distribution [id=" + id + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", time=" + time + "]";
	}

}
