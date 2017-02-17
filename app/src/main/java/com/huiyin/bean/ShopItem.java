package com.huiyin.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopItem implements Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int ID;// 购物车id
	public int FID;// 购物车父类id
	public String GOODS_CODE;// 货品号
	public int GOODS_STOCKS;// 货品库存
	public int COMMDOTIY_QTY;// 购买数量
	public float COMMODITY_OLD_PRICE;// 打折(促销)商品原价
	public String COMMDOITY_ID;// 商品ID
	public String IMG_PATH;// 商品介绍主图
	public float COMMODITY_PRICE; // 购买商品单价
	public String COMMODITY_NAME;// 商品名称
	public String SPECVALUE;// 属性
	public int IS_ADDED;
	public boolean isGroup = false;
	public float disCount;//折扣
	
	public String PROMOTIONS_TYPE; //商品折扣类型：1打折，2直降 ，3啥都没有
	public String PROMOTIONS_PRICE; //折后价格

	public int QUOTA_FLAG;//限购 1表示参与了，2表示没有,3表示用户已经没有购买此间商品的机会
	public String QUOTA_NUMBER;//可以购买的次数
	public String QUOTA_QUANTITY;//每次可以购买的数量

	@Override
	public String toString() {
		return "ShopItem [ID=" + ID + ", FID=" + FID + ", GOODS_CODE="
				+ GOODS_CODE + ", GOODS_STOCKS=" + GOODS_STOCKS
				+ ", COMMDOTIY_QTY=" + COMMDOTIY_QTY + ", COMMODITY_OLD_PRICE="
				+ COMMODITY_OLD_PRICE + ", COMMDOITY_ID=" + COMMDOITY_ID
				+ ", IMG_PATH=" + IMG_PATH + ", COMMODITY_PRICE="
				+ COMMODITY_PRICE + ", COMMODITY_NAME=" + COMMODITY_NAME
				+ ", SPECVALUE=" + SPECVALUE + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ID);
		dest.writeInt(this.FID);
		dest.writeString(this.GOODS_CODE);
		dest.writeInt(this.GOODS_STOCKS);
		dest.writeInt(this.COMMDOTIY_QTY);
		dest.writeFloat(this.COMMODITY_OLD_PRICE);
		dest.writeString(this.COMMDOITY_ID);
		dest.writeString(this.IMG_PATH);
		dest.writeFloat(this.COMMODITY_PRICE);
		dest.writeString(this.COMMODITY_NAME);
		dest.writeString(this.SPECVALUE);
		dest.writeInt(this.IS_ADDED);
		dest.writeByte(this.isGroup ? (byte) 1 : (byte) 0);
		dest.writeFloat(this.disCount);
		dest.writeString(this.PROMOTIONS_TYPE);
		dest.writeString(this.PROMOTIONS_PRICE);
		dest.writeInt(this.QUOTA_FLAG);
		dest.writeString(this.QUOTA_NUMBER);
		dest.writeString(this.QUOTA_QUANTITY);
	}

	public ShopItem() {
	}

	protected ShopItem(Parcel in) {
		this.ID = in.readInt();
		this.FID = in.readInt();
		this.GOODS_CODE = in.readString();
		this.GOODS_STOCKS = in.readInt();
		this.COMMDOTIY_QTY = in.readInt();
		this.COMMODITY_OLD_PRICE = in.readFloat();
		this.COMMDOITY_ID = in.readString();
		this.IMG_PATH = in.readString();
		this.COMMODITY_PRICE = in.readFloat();
		this.COMMODITY_NAME = in.readString();
		this.SPECVALUE = in.readString();
		this.IS_ADDED = in.readInt();
		this.isGroup = in.readByte() != 0;
		this.disCount = in.readFloat();
		this.PROMOTIONS_TYPE = in.readString();
		this.PROMOTIONS_PRICE = in.readString();
		this.QUOTA_FLAG = in.readInt();
		this.QUOTA_NUMBER = in.readString();
		this.QUOTA_QUANTITY = in.readString();
	}

	public static final Parcelable.Creator<ShopItem> CREATOR = new Parcelable.Creator<ShopItem>() {
		@Override
		public ShopItem createFromParcel(Parcel source) {
			return new ShopItem(source);
		}

		@Override
		public ShopItem[] newArray(int size) {
			return new ShopItem[size];
		}
	};
}
