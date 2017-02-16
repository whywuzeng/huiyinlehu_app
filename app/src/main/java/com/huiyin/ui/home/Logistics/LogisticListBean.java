package com.huiyin.ui.home.Logistics;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class LogisticListBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public ArrayList<orderLogisticsItem> orderLogistics;

	public ArrayList<ReLikeItem> reList;

	public class orderLogisticsItem {
		public int USER_ID;// 532, 用户id
		public String ORDER_CODE;// LH201411180000008, 订单编号
		public OrderLogisticsListBean orderLogisticsList;
		public int STATUS;// 3, 订单状态
		public String SPECVALUE;// null, 商品规格
		public int ORDER_ID;// 888, 订单id
		public double PURCHASE_PRICE;// 0.01, 商品单价
		public int ID;// 819, 订单详情id
		public double TOTAL_PRICE;// 0, 订单总价
		public String CREATE_DATE;// 2014-11-18, 下单时间
		public String COMMODITY_IMAGE_PATH;// 商品图片/hy/attached/image/2014/20141105/201411051014453811.jpg,
		public int BUY_QTY;// 1 数量
		public String COMMODITY_NAME;// 商品名称
	}

	public class ReLikeItem {
		float PRICE;//
		int ID;//
		String COMMODITY_IMAGE_PATH;// 商品图片
		int NUM;//
		String COMMODITY_NAME;// 商品名称
	}

	public static LogisticListBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			LogisticListBean experLightBean = gson.fromJson(json,
					LogisticListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}

	public ArrayList<orderLogisticsItem> getOrderLogistics() {
		return orderLogistics;
	}

	public void setOrderLogistics(ArrayList<orderLogisticsItem> orderLogistics) {
		this.orderLogistics = orderLogistics;
	}
}
