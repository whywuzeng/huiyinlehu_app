package com.huiyin.bean;

import java.io.Serializable;

/**
 * 商品属性
 * @author kuangyong
 *
 */
public class Attribute implements Serializable{
	public String  partnerId;//父id
	public String  partnerName;//父名称
	public AttributeValue value;
	public class AttributeValue{
		public String id;//属性id
		public String name;//名称
		@Override
		public String toString() {
			return "AttributeValue [id=" + id + ", name=" + name + "]";
		}
	}
	@Override
	public String toString() {
		return "Attribute [partnerId=" + partnerId + ", partnerNmae="
				+ partnerName + ", value=" + value + "]";
	}
}
