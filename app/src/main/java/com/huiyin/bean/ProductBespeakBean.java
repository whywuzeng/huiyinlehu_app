package com.huiyin.bean;

public class ProductBespeakBean {

	public int PID1;
	public String IMG1;
	public int CID1;
	public int PID2;
	public String IMG2;
	public int CID2;
	public int PID3;
	public String IMG3;
	public int CID3;
	public String ADVER;
	public String ANAME;

	public int getPID1() {
		return PID1;
	}

	public void setPID1(int pID1) {
		PID1 = pID1;
	}

	public String getIMG1() {
		return IMG1;
	}

	public void setIMG1(String iMG1) {
		IMG1 = iMG1;
	}

	public int getPID2() {
		return PID2;
	}

	public void setPID2(int pID2) {
		PID2 = pID2;
	}

	public String getIMG2() {
		return IMG2;
	}

	public void setIMG2(String iMG2) {
		IMG2 = iMG2;
	}

	public int getPID3() {
		return PID3;
	}

	public void setPID3(int pID3) {
		PID3 = pID3;
	}

	public String getIMG3() {
		return IMG3;
	}

	public void setIMG3(String iMG3) {
		IMG3 = iMG3;
	}

	public int getCID1() {
		return CID1;
	}

	public void setCID1(int cID1) {
		CID1 = cID1;
	}

	public int getCID2() {
		return CID2;
	}

	public void setCID2(int cID2) {
		CID2 = cID2;
	}

	public int getCID3() {
		return CID3;
	}

	public void setCID3(int cID3) {
		CID3 = cID3;
	}

	public String getADVER() {
		return ADVER;
	}

	public void setADVER(String aDVER) {
		ADVER = aDVER;
	}

	public String getANAME() {
		return ANAME;
	}

	public void setANAME(String aNAME) {
		ANAME = aNAME;
	}

	@Override
	public String toString() {
		return "ProductBespeakBean [PID1=" + PID1 + ", IMG1=" + IMG1
				+ ", PID2=" + PID2 + ", IMG2=" + IMG2 + ", PID3=" + PID3
				+ ", IMG3=" + IMG3 + ", ADVER=" + ADVER + ", ANAME=" + ANAME
				+ "]";
	}

}
