package com.huiyin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ITEM的对应可序化队列属性
 * */
public class ChannelItem implements Parcelable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/**
	 * 栏目对应ID
	 * */
	public int id;
	/**
	 * 栏目对应NAME
	 * */
	public String name;
	/**
	 * 栏目在整体中的排序顺序 rank
	 * */
	public int orderId;
	/**
	 * 栏目是否选中
	 * */
	public int selected;

	public int channelId;

	public String imageUrl;

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int orderId, int selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}

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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "ChannelItem [id=" + id + ", name=" + name + ", orderId="
				+ orderId + ", selected=" + selected + ", channelId="
				+ channelId + ", imageUrl=" + imageUrl + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.orderId);
		dest.writeInt(this.selected);
		dest.writeInt(this.channelId);
		dest.writeString(this.imageUrl);
	}

	protected ChannelItem(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.orderId = in.readInt();
		this.selected = in.readInt();
		this.channelId = in.readInt();
		this.imageUrl = in.readString();
	}

	public static final Parcelable.Creator<ChannelItem> CREATOR = new Parcelable.Creator<ChannelItem>() {
		@Override
		public ChannelItem createFromParcel(Parcel source) {
			return new ChannelItem(source);
		}

		@Override
		public ChannelItem[] newArray(int size) {
			return new ChannelItem[size];
		}
	};
}