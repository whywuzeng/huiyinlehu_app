package com.huiyin.bean;

public class Prize {

	private int id;
	private String name;
	private int probability;// 概率

	private int spoil;

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

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public int getSpoil() {
		return spoil;
	}

	public void setSpoil(int spoil) {
		this.spoil = spoil;
	}

	@Override
	public String toString() {
		return "Prize [id=" + id + ", name=" + name + ", probability=" + probability + ", spoil=" + spoil + "]";
	}

}
