package com.huiyin.bean;

import java.util.ArrayList;

public class GoodsDetailBeen extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GDBCommon commodity = new GDBCommon();

	public class GDBCommon {
		public float commodityScore;// 评分
		public String DISCOUNT;// 折扣
		public int commoditySum;// 好评人数
		public int baskCommodtiy;// 晒单数
		public ArrayList<DianPing> reviewList;// 评论
		public ArrayList<GDBItem> reList = new ArrayList<GDBItem>();//也许喜欢
		public ArrayList<CombineProduct> groupCommodityList = new ArrayList<CombineProduct>();//组合购买
		public SpecialValue1 specList = new SpecialValue1();
		public ArrayList<GoodDetials> goodsList = new ArrayList<GoodDetials>();//规格参数
		public Commodity commodity;
	}

	public class DianPing {
		public String CREATE_TIME;// 创建时间
		public String CONTENT;// 评论内容
		public String SCORE;// 评分
		public String USER_NAME;// 用户名
	}
	
	public class GDBItem {
		public String PRICE;
		public String COMMODITY_IMAGE_PATH;
		public String NUM;
		public String COMMODITY_NAME;
		public String ID;
	}

	public class CombineProduct {
		public float PRICE;
		public float REFERENCE_PRICE;
		public int SALES_VOLUME;
		public String COMMODITY_IMAGE_PATH;
		public String COMMODITY_NAME;
		public String ID;
		public String CODE;
	}

	public class SpecialValue1 {
		public ArrayList<Values1> specValueList = new ArrayList<Values1>();
		public String NAME;
		public String NAME_ID;
	}

	public class Values1 {
		public SpecialValues2 twoSpecMap = new SpecialValues2();
		public String VALUE_NAME;
		public String VALUE_ID;
	}

	public class SpecialValues2 {
		public ArrayList<Value2> twoSpecMap = new ArrayList<Value2>();
		public String NAME;
		public String NAME_ID;
	}

	public class Value2 {
		public String VALUE_NAME;
		public String VALUE_ID;
	}

	public class GoodDetials {
		public String SPEC_VALUE_IDS;// 组合id
		public String GOODS_PRICE;// 货品价格
		public String GOODS_STOCKS;// 库存
		public String COMMODITY_ID;// 商品id
		public String ID;// 规格编号
		public String CODE;// 货品id
	}

	public class Commodity {
		public String IS_ADDED;// 商品状态
		public String PRICE;// 乐虎价格
		public String COMMODITY_ID;// 商品id
		public String CATEGORY_ID;// 32,
		public String REFERENCE_PRICE;// 推荐价格,
		public String COMMODITY_AD;// 商品广告语
		public String DETAILS_INTRODUCTION;// 图文详情
		public String SPECIFICATIONS;// 规格参数
		public String ONLINE_DEMO;// 包装清单
		public String INSTALLATION_DIAGRAM;// 售后服务
		public String COMMODITY_IMAGE_LIST;// 产品图片
		public String GOODS_PRICE;// 货品号价格·：当规格参数集合为-1的时候，取的就是这里的值
		public String GOODS_STOCKS;// 库存 货品号库存：当规格参数集合为-1的时候，取的就是这里的值
		public String COMMODITY_NAME;// 商品名称
		public String CODE;// 货品id 货品号库存：当规格参数集合为-1的时候，取的就是这里的值
		public String INTEGRAL;// 商品积分
		public String SALES_VOLUME;// 总销售
		public int COLLECT;//收藏的人数 
		public int COLLECT_FLAG;//是否收藏  0未收藏1收藏

		public String PROMOTIONS_TYPE; //商品折扣类型：1打折，2直降 ，3啥都没有
		public String PROMOTIONS_PRICE; //折后价格
		public String PROMOTIONS_DISCOUNT; //折

		public int QUOTA_FLAG;//限购 1表示参与了，2表示没有,3表示用户已经没有购买此间商品的机会
		public int QUOTA_NUMBER;//可以购买的次数
		public int QUOTA_QUANTITY;//每次购买的数量
		
		public int MARK;//1：限购2：商品促销3：秒杀4：闪购5: 新品预约
		public String START_TIME;//开始时间
		public String END_TIME;//结束时间
		public String curDate;//服务器时间
		public String BESPEAK_MARK;// 预约状态 1 为 已预约
		
		public String SHOPPING_CAR;//购物车商品数量
	}
}
