package com.huiyin.bean;

public class UnloginBean {

	public String consignee_name; // 收货人姓名未正常接收
	public String consignee_phone; // 收 货人电话未正常接收
	public String postal_code; // 收货人邮编未正常接收
	public String consignee_address; // 收货人地址未正常接收
	public String shipping_method; // 配送方式未正常接收
	public String payment_method; // 支付方式未正常接收
	public String receipt_time; // 收货时间未正常接收
	public String invoicing_method; // 发票开具方式未正常接收
	public String invoice_title; // 发票抬头未正常接收
	public String invoice_content; // 发票明细内容未正常接收
	public String company_name; // 增值税发票的单位名称未正常接收
	public String identification_number; // 纳税人识别号未正常接收
	public String registered_address; // 注册地址未正常接收
	public String registered_phone; // 注册电话未正常接收
	public String bank; // 开户银行未正常接收
	public String account; // 银行账户未正常接收
	public String collector_name; // 收票人名称未正常接收
	public String collector_phone; // 收货人手机未正常接收
	public String collector_address; // 收货人地址未正常接收
	public String commodity; // 商品信息（JSONUtils）未正常接收
	public String delivery_remark; // 留言未正常接收
	public String province_id;
	public String city_id;
	public String area_id;
	public String freight; // 运费

	@Override
	public String toString() {
		return "UnloginBean [consignee_name=" + consignee_name + ", consignee_phone=" + consignee_phone + ", postal_code=" + postal_code + ", consignee_address=" + consignee_address
						+ ", shipping_method=" + shipping_method + ", payment_method=" + payment_method + ", receipt_time=" + receipt_time + ", invoicing_method=" + invoicing_method
						+ ", invoice_title=" + invoice_title + ", invoice_content=" + invoice_content + ", company_name=" + company_name + ", identification_number=" + identification_number
						+ ", registered_address=" + registered_address + ", registered_phone=" + registered_phone + ", bank=" + bank + ", account=" + account + ", collector_name=" + collector_name
						+ ", collector_phone=" + collector_phone + ", collector_address=" + collector_address + ", commodity=" + commodity + ", delivery_remark=" + delivery_remark + ", province_id="
						+ province_id + ", city_id=" + city_id + ", area_id=" + area_id + ", freight=" + freight + "]";
	}

}
