package com.huiyin.ui.housekeeper;

public class WidomItem {
	public int ID;
	public String TITLE;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	@Override
	public String toString() {
		return TITLE;
	}

}
