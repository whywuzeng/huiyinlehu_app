package com.huiyin.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Lehujuan implements Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// 主键

	private float amount;// 面值

	private int count;// 共几张劵

	private String date;

	private boolean isChecked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "Lehujuan [id=" + id + ", amount=" + amount + ", count=" + count + ", date=" + date + ", isChecked=" + isChecked + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeFloat(this.amount);
		dest.writeInt(this.count);
		dest.writeString(this.date);
		dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
	}

	public Lehujuan() {
	}

	protected Lehujuan(Parcel in) {
		this.id = in.readInt();
		this.amount = in.readFloat();
		this.count = in.readInt();
		this.date = in.readString();
		this.isChecked = in.readByte() != 0;
	}

	public static final Parcelable.Creator<Lehujuan> CREATOR = new Parcelable.Creator<Lehujuan>() {
		@Override
		public Lehujuan createFromParcel(Parcel source) {
			return new Lehujuan(source);
		}

		@Override
		public Lehujuan[] newArray(int size) {
			return new Lehujuan[size];
		}
	};
}
