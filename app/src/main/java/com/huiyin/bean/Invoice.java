package com.huiyin.bean;

public class Invoice {
	
	public static final String INVOICE_TYPE_NORMAL = "普通发票";
	public static final String INVOICE_TYPE_TAX = "增值税发票";
	
	/**
	 * 发票类型
	 * */
	public enum InvoiceType {
		InvoiceTypeNormal, InvoiceTypeTax
	}

	// 发票类型
	private InvoiceType invoiceType;

	// 发票抬头
	private String title;

	// 是否显示明细
	private boolean isShowDetail;

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isShowDetail() {
		return isShowDetail;
	}

	public void setShowDetail(boolean isShowDetail) {
		this.isShowDetail = isShowDetail;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceType=" + invoiceType + ", title=" + title + ", isShowDetail=" + isShowDetail + "]";
	}

}
