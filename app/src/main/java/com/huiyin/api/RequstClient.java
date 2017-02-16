package com.huiyin.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;

import com.huiyin.AppContext;
import com.huiyin.http.AsyncHttpClient;
import com.huiyin.http.AsyncHttpResponseHandler;
import com.huiyin.http.RequestParams;
import com.huiyin.pay.wxpay.Constants;
import com.huiyin.utils.CryptionUtil;
import com.huiyin.utils.Des3;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;

@SuppressLint("SimpleDateFormat")
public class RequstClient {

	/**
	 * 定义一个异步网络客户端 默认超时未10秒 当超过，默认重连次数为5次 默认最大连接数为10个
	 */
	private static final int TIMEOUT_SECOND = 10000;
	private static final String TAG = "RequstClient";
	private static AsyncHttpClient mClient = null;
	static {
		/* 设置连接和响应超时时间 */
		mClient = new AsyncHttpClient();
		mClient.setTimeout(TIMEOUT_SECOND);
	}

	/**
	 * @param url
	 *            API 地址
	 * @param mHandler
	 *            数据加载状态回调
	 */
	public static void post(String url, AsyncHttpResponseHandler mHandler) {

		post(url, null, mHandler);
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 *            API 地址
	 * @param params
	 *            请求的参数
	 * @param handler
	 *            数据加载状态回调
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler mHandler) {
		/* 将参数顺序传递 */
		if (params != null) {
			LogUtil.i(TAG, "发起post请求:" + url + "?" + params.toString());
		} else {
			LogUtil.i(TAG, "发起post请求:" + url);
		}
		mClient.post(url, params, mHandler);
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 * @param passWord
	 * @param mHandler
	 */
	@SuppressLint("SimpleDateFormat")
	public static void doLogin(String userName, String password, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", userName);
			String mPassword = CryptionUtil.md5(password.trim());
			map.put("password", mPassword);
			String pwd3des = Des3.encode(password.trim());
			map.put("password3des", pwd3des);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			map.put("channelId", AppContext.getInstance().channelIdBai);
			map.put("userId", AppContext.getInstance().userIdBai);
			map.put("nearbyId", AppContext.getInstance().getNearbyId());
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userName", userName);
			params.put("password", mPassword);
			params.put("password3des", pwd3des);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			params.put("channelId", AppContext.getInstance().channelIdBai);
			params.put("userId", AppContext.getInstance().userIdBai);
			params.put("nearbyId", AppContext.getInstance().getNearbyId());
			post(URLs.LOGIN, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我的乐虎
	 * 
	 * @param mHandler
	 */
	public static void doMyLH(AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserId());
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", AppContext.getInstance().getUserId());
			params.put("mKey", paramStr);
			post(URLs.MYLH, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付方式
	 * 
	 */
	public static void doPayWay(AsyncHttpResponseHandler mHandler) {
		try {

			post(URLs.SHOP_PAY, null, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 在线帮助
	 * 
	 */
	public static void onLineHelp(AsyncHttpResponseHandler mHandler) {
		try {

			post(URLs.HELP_TITLE, null, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取运费
	 * 
	 */
	public static void appFreight(float totalPrice, String cityId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalPrice", Des3.encode(totalPrice + ""));
			map.put("cityId", Des3.encode(cityId));
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("totalPrice", Des3.encode(totalPrice + ""));
			params.put("cityId", Des3.encode(cityId));
			params.put("mKey", paramStr);
			post(URLs.FREESHIPPING, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取订单折扣
	 * 
	 */
	public static void appOrderDiscount(float totalPrice, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalPrice", Des3.encode(totalPrice + ""));
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("totalPrice", Des3.encode(totalPrice + ""));
			params.put("mKey", paramStr);
			post(URLs.ORDER_DISCOUNT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 填写订单初始化
	 * 
	 */
	public static void writeOrderInit(String userId, String shopId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("shopId", shopId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("shopId", shopId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.WRITE_ORDER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 删除订单
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	@SuppressLint("SimpleDateFormat")
	public static void deleteOrder(String orderId, int flag, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("flag", String.valueOf(flag));
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderId", orderId);
			params.put("flag", String.valueOf(flag));
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.DELETE_ORDER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 取消换货订单
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	public static void cancelChangeOrder(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("replaceId", orderId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("replaceId", orderId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.CANCEL_ORDER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 确认收货
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	public static void receiveOrder(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("replaceId", orderId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("replaceId", orderId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.RECEIVE_ORDER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/***
	 * 未登录时提交订单
	 * 
	 * @param consignee_name
	 * @param consignee_phone
	 * @param postal_code
	 * @param consignee_address
	 * @param shipping_method
	 * @param payment_method
	 * @param receipt_time
	 * @param invoicing_method
	 * @param invoice_title
	 * @param invoice_content
	 * @param company_name
	 * @param identification_number
	 * @param registered_address
	 * @param registered_phone
	 * @param bank
	 * @param account
	 * @param collector_name
	 * @param collector_phone
	 * @param collector_address
	 * @param commodity
	 * @param delivery_remark
	 * @param province_id
	 * @param city_id
	 * @param area_id
	 * @param freight
	 * @param mHandler
	 */
	public static void commitOrderUnLogin(String consignee_name, String consignee_phone, String postal_code,
			String consignee_address, String shipping_method, String payment_method, String receipt_time,
			String invoicing_method, String invoice_title, String invoice_content, String company_name,
			String identification_number, String registered_address, String registered_phone, String bank, String account,
			String collector_name, String collector_phone, String collector_address, String commodity, String delivery_remark,
			String province_id, String city_id, String area_id, String freight, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("consignee_name", consignee_name);
			map.put("consignee_phone", consignee_phone);
			map.put("postal_code", postal_code);
			map.put("consignee_address", consignee_address);
			map.put("shipping_method", shipping_method);
			map.put("payment_method", payment_method);
			map.put("receipt_time", receipt_time);
			map.put("invoicing_method", invoicing_method);
			map.put("invoice_title", invoice_title);
			map.put("invoice_content", invoice_content);
			map.put("company_name", company_name);
			map.put("identification_number", identification_number);
			map.put("registered_address", registered_address);
			map.put("registered_phone", registered_phone);
			map.put("bank", bank);
			map.put("account", account);
			map.put("collector_name", collector_name);
			map.put("collector_phone", collector_phone);
			map.put("collector_address", collector_address);
			map.put("commodity", commodity);
			map.put("delivery_remark", delivery_remark);
			map.put("province_id", province_id);
			map.put("city_id", city_id);
			map.put("area_id", area_id);
			map.put("freight", freight);
			map.put("channelId", AppContext.getInstance().channelIdBai);
			map.put("userId", AppContext.getInstance().userIdBai);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			map.put("nearbyId", AppContext.getInstance().getNearbyId());
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("consignee_name", consignee_name);
			params.put("consignee_phone", consignee_phone);
			params.put("postal_code", postal_code);
			params.put("consignee_address", consignee_address);
			params.put("shipping_method", shipping_method);
			params.put("payment_method", payment_method);
			params.put("receipt_time", receipt_time);
			params.put("invoicing_method", invoicing_method);
			params.put("invoice_title", invoice_title);
			params.put("invoice_content", invoice_content);
			params.put("company_name", company_name);
			params.put("identification_number", identification_number);
			params.put("registered_address", registered_address);
			params.put("registered_phone", registered_phone);
			params.put("bank", bank);
			params.put("account", account);
			params.put("collector_name", collector_name);
			params.put("collector_phone", collector_phone);
			params.put("collector_address", collector_address);
			params.put("commodity", commodity);
			params.put("delivery_remark", delivery_remark);
			params.put("province_id", province_id);
			params.put("city_id", city_id);
			params.put("area_id", area_id);
			params.put("freight", freight);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			params.put("channelId", AppContext.getInstance().channelIdBai);
			params.put("userId", AppContext.getInstance().userIdBai);
			params.put("nearbyId", AppContext.getInstance().getNearbyId());
			post(URLs.SHOP_UNLOGIN, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 乐虎劵验证
	 * 
	 */
	public static void postCodeValidate(String userId, String code, AsyncHttpResponseHandler mHandler) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("code", code);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("code", code);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.CODE_VALIDATE, params, mHandler);
	}

	/***
	 * 提交订单
	 * 
	 * @param userId
	 * @param shoppingId
	 *            购物车id（多个用“，”隔开）
	 * @param freight
	 *            运费（默认为0）
	 * @param integral_d
	 *            积分抵扣金额
	 * @param integral
	 *            使用的积分
	 * @param delivery_remark
	 *            备注信息
	 * @param address_id
	 *            地址id
	 * @param default_paly_id
	 *            支付方式
	 * @param default_receipt_time
	 *            收货时间
	 * @param invoices_id
	 *            发票ID
	 * @param lh_id
	 *            乐虎券id
	 * @param mHandler
	 */

	public static void postCommitInfo(String userId, String shoppingId, String freight, String integral_d, String integral,
			String delivery_remark, String address_id, String default_paly_id, String default_receipt_time, String invoices_id,
			String lh_id, String sendId, int nearby_mention, String buyMessage, AsyncHttpResponseHandler mHandler) {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			map.put("shoppingId", shoppingId);
			map.put("freight", freight);
			map.put("integral_d", integral_d);
			map.put("integral", integral);
			map.put("delivery_remark", delivery_remark);
			map.put("address_id", address_id);
			map.put("default_paly_id", default_paly_id);
			map.put("default_receipt_time", default_receipt_time);
			map.put("invoices_id", invoices_id);
			map.put("lh_id", lh_id);
			map.put("shipping_method", sendId);
			if (sendId.equals("-1"))
				map.put("nearby_mention", String.valueOf(nearby_mention));
			if (!StringUtils.isBlank(buyMessage)) {
				map.put("buyMessage", buyMessage);
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			map.put("nearbyId", AppContext.getInstance().getNearbyId());
			map.put("origin", "1");
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("user_id", userId);
			params.put("shoppingId", shoppingId);
			params.put("freight", freight);
			params.put("integral_d", integral_d);
			params.put("integral", integral);
			params.put("delivery_remark", delivery_remark);
			params.put("address_id", address_id);
			params.put("default_paly_id", default_paly_id);
			params.put("default_receipt_time", default_receipt_time);
			params.put("invoices_id", invoices_id);
			params.put("lh_id", lh_id);
			params.put("shipping_method", sendId);
			if (sendId.equals("-1"))
				params.put("nearby_mention", String.valueOf(nearby_mention));
			if (!StringUtils.isBlank(buyMessage)) {
				params.put("buyMessage", buyMessage);
			}
			params.put("time", str_time);
			params.put("origin", "1");
			params.put("mKey", paramStr);
			params.put("nearbyId", AppContext.getInstance().getNearbyId());
			post(URLs.COMMIT_ORDER, params, mHandler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增值发票信息
	 * 
	 * @param userId
	 * @param invoicing_method
	 *            0 普通发票 1 增值税发票
	 * @param invoice_content
	 *            发票明细
	 * @param company_name
	 *            增值税发票的单位名称
	 * @param identification_number
	 *            纳税人识别号
	 * @param registered_address
	 *            注册地址
	 * @param registered_phone
	 *            注册电话
	 * @param bank
	 *            开户银行
	 * @param account
	 *            银行账户
	 * @param collector_name
	 *            收货人名称
	 * @param collector_phone
	 *            收货人手机
	 * @param collector_address
	 *            收货人地址
	 * @param mHandler
	 */
	public static void postAddedInvoiceInfo(String userId, String invoicing_method, String invoice_content, String company_name,
			String identification_number, String registered_address, String registered_phone, String bank, String account,
			String collector_name, String collector_phone, String collector_address, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("invoicing_method", invoicing_method);
			map.put("invoice_content", invoice_content);
			map.put("company_name", company_name);
			map.put("identification_number", identification_number);
			map.put("registered_address", registered_address);
			map.put("registered_phone", registered_phone);
			map.put("bank", bank);
			map.put("account", account);
			map.put("collector_name", collector_name);
			map.put("collector_phone", collector_phone);
			map.put("collector_address", collector_address);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("invoicing_method", invoicing_method);
			params.put("invoice_content", invoice_content);
			params.put("company_name", company_name);
			params.put("identification_number", identification_number);
			params.put("registered_address", registered_address);
			params.put("registered_phone", registered_phone);
			params.put("bank", bank);
			params.put("account", account);
			params.put("collector_name", collector_name);
			params.put("collector_phone", collector_phone);
			params.put("time", str_time);
			params.put("collector_address", collector_address);
			params.put("mKey", paramStr);
			post(URLs.IVOICE_INFO, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 普通发票信息
	 * 
	 * @param userId
	 * @param invoicing_method
	 *            0 普通发票 1 增值税发票
	 * @param invoice_title
	 *            发票抬头 :收件人/收货单位名字
	 * @param invoice_content
	 *            发票明细
	 * @param invoice_title_type
	 *            发票抬头类型
	 * @param mHandler
	 */
	public static void postNormalInvoiceInfo(String userId, String invoicing_method, String invoice_title,
			String invoice_content, String invoice_title_type, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("invoicing_method", invoicing_method);
			map.put("invoice_title", invoice_title);
			map.put("invoice_content", invoice_content);
			map.put("invoice_title_type", invoice_title_type);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("invoicing_method", invoicing_method);
			params.put("invoice_title", invoice_title);
			params.put("invoice_content", invoice_content);
			params.put("invoice_title_type", invoice_title_type);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.IVOICE_INFO, params, mHandler);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 购物车列表
	 * 
	 * @param userId
	 * @param type
	 * @param mHandler
	 */
	public static void doShopList(String userId, String type, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("type", type);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("type", type);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.SHOP_LIST, params, mHandler);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 购物车新接口
	 * 
	 * @param userId
	 * @param type
	 * @param mHandler
	 */
	public static void doShopListNew(String userId, String type, AsyncHttpResponseHandler mHandler) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("type", type);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("type", type);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.queryShoppingCarNew, params, mHandler);
	}

	/**
	 * 删除订单
	 * 
	 * @param ids
	 * @param mHandler
	 */
	public static void doDeleteOrder(String ids, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ids", ids);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("ids", ids);
			params.put("time", str_time);
			params.put("mKey", paramStr);

			post(URLs.DELETE_SHOPCAR, params, mHandler);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 修改订单数量
	 * 
	 * @param id
	 * @param num
	 * @param mHandler
	 */
	public static void doModifyOrder(String id, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("id", id);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.MODIFY_ORDER, params, mHandler);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 修改用户名
	 * 
	 * 
	 */
	public static void doModifyUsername(String userId, String userName, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("userName", userName);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("userName", userName);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.MODIFY_USERNAME, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 绑定手机号
	 * 
	 */
	public static void bindPhone(String userId, String phone, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("phone", phone);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("phone", phone);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.BIND_PHONE, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 第三方账户绑定
	 * 
	 * @param password
	 * @param phone
	 * @param openId
	 * @param messageVerifyCode
	 * @param mHandler
	 */
	public static void bindPhone(String password, String phone, String openId, String messageVerifyCode, int flag,
			AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String mPassword = CryptionUtil.md5(password.trim());
			map.put("password", mPassword);
			String pwd3des;
			pwd3des = Des3.encode(password.trim());
			map.put("password3des", pwd3des);
			map.put("phone", phone);
			map.put("openId", openId);
			if (flag != 0)
				map.put("flag", String.valueOf(flag));
			if (!StringUtils.isBlank(messageVerifyCode))
				map.put("messageVerifyCode", messageVerifyCode);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("password", mPassword);
			params.put("password3des", pwd3des);
			params.put("phone", phone);
			params.put("openId", openId);
			if (flag != 0)
				params.put("flag", String.valueOf(flag));
			if (!StringUtils.isBlank(messageVerifyCode))
				params.put("messageVerifyCode", messageVerifyCode);
			params.put("mKey", paramStr);
			post(URLs.BIND_PHONE, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取订单列表
	 * 
	 * @param userId
	 * @param pageindex
	 * @param flag
	 *            0：商品订单 1：退货订单 2：预约订单
	 * @param remark
	 *            1：待支付 收货2：待评价 3：退换货中
	 * @param status
	 *            订单所有状态
	 * @param startTime
	 * @param endTime
	 * @param searchOrder
	 * @param mHandler
	 */
	public static void getOrderList(String userId, String pageindex, String flag, String remark, String status, String startTime,
			String endTime, String searchOrder, AsyncHttpResponseHandler mHandler) {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("pageindex", pageindex);
			map.put("flag", flag);
			if (!StringUtils.isBlank(remark))
				map.put("remark", remark);
			if (!StringUtils.isBlank(status))
				map.put("status", status);
			if (!StringUtils.isBlank(startTime))
				map.put("startTime", startTime);
			if (!StringUtils.isBlank(endTime))
				map.put("endTime", endTime);
			if (!StringUtils.isBlank(searchOrder))
				map.put("searchOrder", searchOrder);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pageindex", pageindex);
			params.put("flag", flag);
			if (!StringUtils.isBlank(remark))
				params.put("remark", remark);
			if (!StringUtils.isBlank(status))
				params.put("status", status);
			if (!StringUtils.isBlank(startTime))
				params.put("startTime", startTime);
			if (!StringUtils.isBlank(endTime))
				params.put("endTime", endTime);
			if (!StringUtils.isBlank(searchOrder))
				params.put("searchOrder", searchOrder);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDERlIST, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 获取订单详情
	 */
	public static void getOrderDetail(String order_id, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ORDER_ID", order_id);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("ORDER_ID", order_id);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDER_DETAIL, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/****
	 * 取消订单
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	public static void cancelOrder(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderId", orderId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDER_CANCEL, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/****
	 * 取消退款
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	public static void cancelBackOrder(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnId", orderId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("returnId", orderId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDER_BACK_CANCEL, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/****
	 * 确认到账
	 * 
	 * @param orderId
	 * @param mHandler
	 */
	public static void sureReceiveMoney(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnId", orderId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("returnId", orderId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDER_MONEY_RETURN, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 确认收货
	 * 
	 * @param orderId
	 * @param commodityId
	 * @param mHandler
	 */
	public static void makeSureOrder(String orderId, String commodityId, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("commodityId", commodityId);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderId", orderId);
			params.put("commodityId", commodityId);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.ORDER_SURE, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 退货
	 * 
	 * @param orderId
	 * @param returnType退款方式
	 * @param reason退货原因
	 * @param bank银行
	 * @param bankBranch银行帐号分行
	 * @param bankRealName开户银行真实姓名
	 * @param bankAccount银行账户
	 * @param phone
	 *            电话号码
	 * @param userId
	 * @param number退货商品数量
	 * @param commodityId退货商品数量
	 * @param mHandler
	 */

	public static void backGood(String orderId, String returnType, String reason, String bank, String bankBranch,
			String bankRealName, String bankAccount, String phone, String userId, String number, String commodityId, String imgs,
			AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("returnType", returnType);
			map.put("reason", reason);
			map.put("bank", bank);
			map.put("bankBranch", bankBranch);
			map.put("bankRealName", bankRealName);
			map.put("bankAccount", bankAccount);
			map.put("phone", phone);
			map.put("userId", userId);
			map.put("number", number);
			map.put("commodityId", commodityId);
			map.put("imgs", imgs);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderId", orderId);
			params.put("returnType", returnType);
			params.put("reason", reason);
			params.put("bank", bank);
			params.put("bankBranch", bankBranch);
			params.put("bankRealName", bankRealName);
			params.put("bankAccount", bankAccount);
			params.put("phone", phone);
			params.put("userId", userId);
			params.put("number", number);
			params.put("commodityId", commodityId);
			params.put("imgs", imgs);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.BACK_GOOD, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 换货
	 * 
	 * @param orderId
	 *            订单id
	 * @param commodityId
	 *            商品id
	 * @param reason退货原因
	 * @param consigneeName收件人姓名
	 * @param consigneePhone收件人电话号码
	 * @param imgs
	 *            图片url
	 * @param number
	 *            换货数量
	 * @param consigneeAddress收件人地址
	 * @param mHandler
	 */

	public static void changeGood(String userId, String orderId, String commodityId, String reason, String consigneeName,
			String consigneePhone, String imgs, String number, String consigneeAddress, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("orderId", orderId);
			map.put("commodityId", commodityId);
			map.put("imgs", imgs);
			map.put("number", number);
			map.put("reason", reason);
			map.put("consigneeName", consigneeName);
			map.put("consigneePhone", consigneePhone);
			map.put("consigneeAddress", consigneeAddress);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("orderId", orderId);
			params.put("reason", reason);
			params.put("commodityId", commodityId);
			params.put("imgs", imgs);
			params.put("number", number);
			params.put("consigneeName", consigneeName);
			params.put("consigneePhone", consigneePhone);
			params.put("consigneeAddress", consigneeAddress);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.CHANGE_GOOD, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 物流信息
	 * 
	 * @param logisticsName
	 * @param logisticsNo
	 * @param commodityReturnId
	 * @param mHandler
	 */
	public static void writeLogistic(String logisticsName, String logisticsNo, String returnId, String commdotiyId,
			String proofImg, String flag, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("logisticsName", logisticsName);
			params.put("logisticsNo", logisticsNo);
			params.put("returnId", returnId);
			params.put("commodityId", commdotiyId);
			params.put("proofImg", proofImg);
			params.put("flag", flag);

			post(URLs.WRITE_LOGISTIC, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 发表评论商品评论
	 */
	public static void postCommentInfo(String userId, String commdotiyId, String score, String content, String order_id,
			String image, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("orderId", order_id);
			map.put("commdotiyId", commdotiyId);
			map.put("score", score);
			map.put("content", content);
			map.put("img", image);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("orderId", order_id);
			params.put("commdotiyId", commdotiyId);
			params.put("score", score);
			params.put("content", content);
			params.put("img", image);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.COMMENT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 售后详情状态及数据
	 */

	public static void postAfterInfo(String userId, String returnId, String commodityId, String flag,
			AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("returnId", returnId);
			params.put("commodityId", commodityId);
			params.put("flag", flag);
			post(URLs.AFTER_DETAIL, params, mHandler);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 发表订单评论
	 */
	public static void postTotalComment(String orderId, String packagingScore, String deliverySpeedScore,
			String serviceDeliveryStaffScore, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("packagingScore", packagingScore);
			map.put("deliverySpeedScore", deliverySpeedScore);
			map.put("serviceDeliveryStaffScore", serviceDeliveryStaffScore);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderId", orderId);
			params.put("packagingScore", packagingScore);
			params.put("deliverySpeedScore", deliverySpeedScore);
			params.put("time", str_time);
			params.put("serviceDeliveryStaffScore", serviceDeliveryStaffScore);
			params.put("mKey", paramStr);
			post(URLs.ORDER_COMMENT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 预约信息
	 * 
	 * @param orderCode
	 * @param mHandler
	 */
	public static void postYuYueInfo(String orderCode, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderCode", orderCode);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			map.put("userId", AppContext.getInstance().getUserId());
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("orderCode", orderCode);
			params.put("time", str_time);
			params.put("userId", AppContext.getInstance().getUserId());
			params.put("mKey", paramStr);
			post(URLs.YUYUE_MSG, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 预约初始化信息
	 * 
	 * @param orderCode
	 * @param mHandler
	 */
	public static void postYuYueInit(String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("ORDER_ID", orderId);
			post(URLs.YUYUE_INIT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 预约取消
	 * 
	 * @param orderCode
	 * @param mHandler
	 */
	public static void postYuYueCancel(String ORDER_ID, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ORDER_ID", ORDER_ID);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("ORDER_ID", ORDER_ID);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.YUYUE_CANCEL, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 提交预约信息
	 * 
	 * @param order_id
	 *            订单id
	 * @param type
	 *            预约类型id
	 * @param phone
	 *            预约信息：手机号码
	 * @param name
	 *            预约信息：联系人
	 * @param address
	 *            预约信息：地址
	 * @param remarks
	 *            预约信息：备注信息
	 * @param userId
	 *            关联用户
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param partId
	 *            所需配件
	 * @param commodity_id
	 *            商品id
	 * @param commodity_num
	 *            商品数量
	 * @param mHandler
	 */
	public static void postCommitYuYue(String order_id, String type, String phone, String name, String address, String remarks,
			String userId, String startTime, String endTime, String partId, String commodity_id, String commodity_num,
			AsyncHttpResponseHandler mHandler) {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ORDER_ID", order_id);
			map.put("RESERVATION_TYPE_ID", type);
			map.put("PHONE", phone);
			map.put("NAME", name);
			map.put("ADDRESS", address);
			map.put("REMARKS", remarks);
			map.put("STARTTIME", startTime);
			map.put("ENDTIME", endTime);
			map.put("USER_ID", userId);
			map.put("PARTS_ID", partId);
			map.put("COMMODITY_ID", commodity_id);
			map.put("COMMODITY_NUM", commodity_num);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("ORDER_ID", order_id);
			params.put("RESERVATION_TYPE_ID", type);
			params.put("PHONE", phone);
			params.put("NAME", name);
			params.put("ADDRESS", address);
			params.put("REMARKS", remarks);
			params.put("USER_ID", userId);
			params.put("STARTTIME", startTime);
			params.put("ENDTIME", endTime);
			params.put("PARTS_ID", partId);
			params.put("COMMODITY_ID", commodity_id);
			params.put("COMMODITY_NUM", commodity_num);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.YUYUE_ADD, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 提交预约信息
	 * 
	 * @param order_id
	 *            订单id
	 * @param type
	 *            预约类型id
	 * @param phone
	 *            预约信息：手机号码
	 * @param name
	 *            预约信息：联系人
	 * @param address
	 *            预约信息：地址
	 * @param remarks
	 *            预约信息：备注信息
	 * @param userId
	 *            关联用户
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param commodity
	 * @param mHandler
	 */
	public static void appAddBespeak(String order_id, String type, String phone, String name, String address, String remarks,
			String userId, String startTime, String endTime, String commodity, AsyncHttpResponseHandler mHandler) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ORDER_ID", order_id);
		map.put("RESERVATION_TYPE_ID", type);
		map.put("PHONE", phone);
		map.put("NAME", name);
		map.put("ADDRESS", address);
		map.put("REMARKS", remarks);
		map.put("STARTTIME", startTime);
		map.put("ENDTIME", endTime);
		map.put("USER_ID", userId);
		map.put("commodity", commodity);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("ORDER_ID", order_id);
		params.put("RESERVATION_TYPE_ID", type);
		params.put("PHONE", phone);
		params.put("NAME", name);
		params.put("ADDRESS", address);
		params.put("REMARKS", remarks);
		params.put("USER_ID", userId);
		params.put("STARTTIME", startTime);
		params.put("ENDTIME", endTime);
		params.put("commodity", commodity);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appAddBespeak, params, mHandler);
	}

	/***
	 * 获取地址列表
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void getAddrList(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pageindex", pageindex);
			post(URLs.ADDR_LIST, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 修改地址
	 * 
	 * @param userId
	 * @param CITY_ID
	 * @param CONSIGNEE_PHONE
	 * @param ADDRESS
	 * @param CONSIGNEE_NAME
	 * @param POSTAL_CODE
	 * @param AREA_ID
	 * @param PROVINCE_ID
	 * @param ADDRESSID
	 * @param IS_DEFAULT
	 * @param mHandler
	 */

	public static void postModifyAddress(String userId, String CITY_ID, String CONSIGNEE_PHONE, String ADDRESS,
			String CONSIGNEE_NAME, String POSTAL_CODE, String AREA_ID, String PROVINCE_ID, String ADDRESSID, String IS_DEFAULT,
			String levelAddr, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("CITY_ID", CITY_ID);
			params.put("CONSIGNEE_PHONE", CONSIGNEE_PHONE);
			params.put("ADDRESS", ADDRESS);
			params.put("CONSIGNEE_NAME", CONSIGNEE_NAME);
			params.put("POSTAL_CODE", POSTAL_CODE);
			params.put("AREA_ID", AREA_ID);
			params.put("PROVINCE_ID", PROVINCE_ID);
			params.put("ADDRESSID", ADDRESSID);
			params.put("IS_DEFAULT", IS_DEFAULT);
			params.put("levelAddr", levelAddr);

			post(URLs.ADDR_MODIFY, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 删除地址
	 * 
	 * @param userId
	 * @param addressId
	 * @param mHandler
	 */
	public static void postDeleteAddress(String userId, String addressId, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("addressId", addressId);
			post(URLs.ADDR_DELETE, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 获取留言类型列表
	 * 
	 * @param mHandler
	 */
	public static void getMsgTypeList(AsyncHttpResponseHandler mHandler) {
		try {

			post(URLs.MSG_TYPE, null, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 获取帮助信息
	 * 
	 * @param flag
	 * @param mHandler
	 */
	public static void getHelpInfo(String flag, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			if (flag.equals("server")) { // 满足这个条件是服务协议接口
				post(URLs.SERVICE_AGGREMENT, null, mHandler);
			} else if (flag.equals("4")) {
				post(URLs.INTGRAL_EXPLAIN, null, mHandler);
			} else {
				params.put("flag", flag);
				post(URLs.ONLINE_HELP, params, mHandler);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 获取活动介绍
	 * 
	 * @param flag
	 * @param mHandler
	 */
	public static void getBannerIntroduce(String flag, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("bannerId", flag);
			post(URLs.BANNER_INTRODUCE, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 留言
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void postMsg(String userId, String content, String name, String type_id, String phone, String mail,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("CONTENT", content);
			params.put("NAME", name);
			params.put("TYPE_ID", type_id);
			params.put("PHONE", phone);
			params.put("MAILBOX", mail);
			post(URLs.MAKE_MSG, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 获取收藏列表
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void getCollectList(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pageindex", pageindex);
			post(URLs.COLLECT_LIST, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 取消收藏
	 * 
	 * @param userId
	 * @param commodityId
	 *            商品id
	 * @param mHandler
	 */
	public static void cancelFoucs(String userId, String commodityId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("USER_ID", userId);
		params.put("COMMODITY_ID", commodityId);
		post(URLs.CANCEL_FOUCS, params, mHandler);

	}

	/***
	 * 获取乐虎劵列表
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void getLeHuList(String userId, String flag, String pageIndex, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("flag", flag);
			map.put("pageindex", pageIndex);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("flag", flag);
			params.put("pageindex", pageIndex);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.LEHUJUAN_LIST, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 获取积分
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void getPoint(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("pageindex", pageindex);
			map.put("pagesize", "10");
			// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			// String str_time = sf.format(new Date());
			// map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pageindex", pageindex);
			params.put("pagesize", "10");
			// params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.GET_POINT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param registerName
	 * @param registerType
	 * @param mHandler
	 */
	public static void getRegeditCode(String registerPhone, String flag, AsyncHttpResponseHandler mHandler) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", registerPhone);
		map.put("flag", flag);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("phone", registerPhone);
		params.put("flag", flag);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.GET_REGEIDT_CODE, params, mHandler);
	}

	/***
	 * 验证验证码
	 * 
	 * @param phone
	 * @param messageVerifyCode
	 * @param mHandler
	 */
	public static void postCode(String phone, String messageVerifyCode, AsyncHttpResponseHandler mHandler) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("messageVerifyCode", messageVerifyCode);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("messageVerifyCode", messageVerifyCode);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.POST_CODE, params, mHandler);
	}

	/**
	 * 找回密码
	 * 
	 * @param phone
	 * @param code
	 * @param mHandler
	 */
	public static void postFindPsw(String phone, String password, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", phone);
			String mPassword = CryptionUtil.md5(password.trim());
			map.put("password", mPassword);
			String pwd3des;
			pwd3des = Des3.encode(password.trim());
			map.put("password3des", pwd3des);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("phone", phone);
			params.put("password", mPassword);
			params.put("password3des", pwd3des);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.FIND_PSW, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册
	 * 
	 * @param registerPhone
	 * @param password
	 * @param sendCodeInfo
	 * @param mHandler
	 */
	public static void doRegedit(String registerPhone, String password, String sendCode, String access_token, String openid,
			int flag, String userIfoJson, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", registerPhone);
			map.put("messageVerifyCode", sendCode);
			String mPassword = CryptionUtil.md5(password.trim());
			map.put("password", mPassword);
			String pwd3des = Des3.encode(password.trim());
			map.put("password3des", pwd3des);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			map.put("channelId", AppContext.getInstance().channelIdBai);
			map.put("userId", AppContext.getInstance().userIdBai);
			map.put("nearbyId", AppContext.getInstance().getNearbyId());
			if (!StringUtils.isBlank(access_token))
				map.put("access_token", access_token);
			if (!StringUtils.isBlank(openid))
				map.put("openid", openid);
			if (flag != 0)
				map.put("flag", String.valueOf(flag));
			if (!StringUtils.isBlank(userIfoJson))
				map.put("userIfoJson", userIfoJson);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("phone", registerPhone);
			params.put("messageVerifyCode", sendCode);
			params.put("password", mPassword);
			params.put("password3des", pwd3des);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			params.put("channelId", AppContext.getInstance().channelIdBai);
			params.put("userId", AppContext.getInstance().userIdBai);
			params.put("nearbyId", AppContext.getInstance().getNearbyId());
			if (!StringUtils.isBlank(access_token))
				params.put("access_token", access_token);
			if (!StringUtils.isBlank(openid))
				params.put("openid", openid);
			if (flag != 0)
				params.put("flag", String.valueOf(flag));
			if (!StringUtils.isBlank(userIfoJson))
				params.put("userIfoJson", userIfoJson);
			post(URLs.REGISTER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 快捷登录
	 * 
	 * @param uid
	 *            微博/QQ登录Id
	 * @param nickName
	 * @param type
	 *            1:微博登录 2:QQ登录
	 * @param mHandler
	 */
	public static void suserLogin(String uid, String nickName, String type, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("nickName", nickName);
		params.put("type", type);
		// mPost(URLs.SUSER_LOGIN, params, mHandler);
	}

	// 今日头条
	public static void appHeadlines(MyCustomResponseHandler mHandler) {
		post(URLs.appHeadlines, mHandler);
	}

	// 今日头条
	public static void appHeadlines2(MyCustomResponseHandler mHandler, String id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		post(URLs.appHeadlines, params, mHandler);
	}

	// 专区
	public static void appPrefecture(MyCustomResponseHandler mHandler, String id, String pageSize, String pageIndex) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		post(URLs.appPrefecture, params, mHandler);
	}

	// 秀场喜欢
	public static void appLike(MyCustomResponseHandler mHandler, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("mKey", paramStr);
		params.put("time", str_time);
		post(URLs.appLike, params, mHandler);
	}

	// 秀场喜欢添加
	public static void appShowLike(MyCustomResponseHandler mHandler, String userId, String spotlightId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("spotlightId", spotlightId);
		post(URLs.appShowLike, params, mHandler);
	}

	// 秀场评论
	public static void appAppraise(MyCustomResponseHandler mHandler, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		map.put("userId", AppContext.getInstance().getUserId());
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("time", str_time);
		params.put("userId", AppContext.getInstance().getUserId());
		params.put("mKey", paramStr);
		post(URLs.appAppraise, params, mHandler);
	}

	// 秀场评论
	public static void appAddAppraise(MyCustomResponseHandler mHandler, String spotlight_id, String user_id, String reply_id,
			String content) {
		RequestParams params = new RequestParams();
		params.put("spotlight_id", spotlight_id);
		params.put("user_id", user_id);
		params.put("reply_id", reply_id);
		params.put("content", content);
		post(URLs.appAddAppraise, params, mHandler);
	}

	// 未登录情况下的推荐

	public static void appNoShow(MyCustomResponseHandler mHandler, String recommend, String use_id, String pageSize,
			String pageIndex) {
		RequestParams params = new RequestParams();
		params.put("recommend", recommend);
		params.put("user_id", use_id);
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		post(URLs.appShow, params, mHandler);
	}

	// 秀场 1推荐0全部
	public static void appShow(MyCustomResponseHandler mHandler, String recommend, String pageSize, String pageIndex) {
		RequestParams params = new RequestParams();
		params.put("recommend", recommend);
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		post(URLs.appShow, params, mHandler);
	}

	// 秀场 全部搜索
	public static void appShowSearch(MyCustomResponseHandler mHandler, String keyword, String flag) {
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		params.put("flag", flag);
		if (AppContext.getInstance().getUserId() != null) {
			params.put("user_id", AppContext.getInstance().getUserId());
		}
		post(URLs.appShowSearch, params, mHandler);
	}

	// 秀场 关注
	public static void appShowAttention(MyCustomResponseHandler mHandler, String id, String pageSize, String pageIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USER_ID", id);
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("USER_ID", id);
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appShowAttention, params, mHandler);
	}

	// 秀场 添加关注
	public static void appAttention(MyCustomResponseHandler mHandler, String id, String spotlightId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USER_ID", id);
		map.put("ATTENTION_ID", spotlightId);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("USER_ID", id);
		params.put("ATTENTION_ID", spotlightId);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appAttention, params, mHandler);
	}

	// 秀场 取消关注
	public static void appCancelAttention(MyCustomResponseHandler mHandler, String id, String spotlightId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USER_ID", id);
		map.put("ATTENTION_ID", spotlightId);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("USER_ID", id);
		params.put("ATTENTION_ID", spotlightId);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appCancelAttention, params, mHandler);
	}

	// 秀场发布(用户的ID和评论内容上传到服务器)
	public static void appPublish(MyCustomResponseHandler mHandler, String user_id, String content, String spo_img) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("content", content);
		map.put("spo_img", spo_img);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);

		String paramStr = CryptionUtil.getSignData(map);
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("content", content);
		params.put("spo_img", spo_img);
		params.put("time", str_time);

		params.put("mKey", paramStr);
		post(URLs.appPublish, params, mHandler);
	}

	/**
	 * 获取一级分类列表的数据
	 * 
	 * @param url
	 * @param mHandler
	 */
	public static void getClassLists(String url, AsyncHttpResponseHandler mHandler) {
		post(url, mHandler);
	}

	/***
	 * 智慧管家首页
	 * 
	 * 
	 * @param housekeeperId
	 * @param housekeeperIMG
	 * @param housekeeperTitle
	 */
	public static void getTitle(AsyncHttpResponseHandler mHandler) {

		post(URLs.HOUSE_KEERER_shouye, mHandler);

	}

	/***
	 * 
	 * 智慧管家预约类型
	 * 
	 * 
	 * 
	 */

	public static void getType(AsyncHttpResponseHandler mHandler) {

		try {

			post(URLs.HOUSE_YUYUE_TYPE, null, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 智慧管家列表
	 * 
	 * @param pageindex
	 * @param mHandler
	 */
	public static void houseKeeper(String pageindex, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("pageindex", pageindex);
			post(URLs.HOUSE_KEERER, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 智慧管家详情
	 * 
	 * @param id
	 * @param mHandler
	 */
	public static void houseKeeperDeatils(String id, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("id", id);
			post(URLs.HOUSE_KEERER_DETAILS, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/***
	 * 智慧管家预约
	 * 
	 * wisdom_id, 预约id name,预约人姓名 phone,电话 address,地址 invoice_img,发票图片
	 * nameplate_img,铭牌图片 Remarks评论
	 * 
	 */
	public static void houseKeepMakePointment(String userId, String wisdom_id, String name, String phone, String address,
			String nameplate_img, String invoice_img, String remarks, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("wisdom_id", wisdom_id);
			map.put("name", name);
			map.put("phone", phone);
			map.put("address", address);

			map.put("invoice_img", invoice_img);
			map.put("nameplate_img", nameplate_img);
			map.put("remarks", remarks);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("wisdom_id", wisdom_id);
			params.put("name", name);
			params.put("phone", phone);
			params.put("address", address);

			params.put("invoice_img", invoice_img);
			params.put("nameplate_img", nameplate_img);
			params.put("remarks", remarks);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.HOUSE_KEERER_YUYUE, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /***
	// * 三级分类列表商品详情页
	// *
	// * @param bannerList
	// * banner图区域数据
	// * @param fastList
	// * 快捷服务区域数据
	// * @param prommotionLayout
	// * 促销活动区域数据 chartpositionList 销售排行榜
	// */
	// public static void ShouYeHP(String bannerList, String fastList, String
	// prommotionLayout, String chartpositionList,
	// AsyncHttpResponseHandler mHandler) {
	//
	// try {
	// RequestParams params = new RequestParams();
	// params.put("bannerList", bannerList);
	// params.put("fastList", fastList);
	// params.put("prommotionLayout", prommotionLayout);
	// params.put("chartpositionList", chartpositionList);
	//
	// post(URLs.SERVER_URL1, params, mHandler);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * 商品详情的加入购物车 1
	 * 
	 * @param userId
	 *            ：用户名id commodityId：商品id goods_codes：货品id num：购买数量
	 *            type：购物车类型（1，app购买，2tv购买）
	 *            purchase：购物类型（（1，加入购物车，2，立即结算，3组合商品））
	 *            commodityIds：组合商品id（多个用“，”隔开）
	 * 
	 * 
	 */

	public static void goodsDetailsShoppingCar(String userId, String commodityId, String goods_codes, String num, String type,
			String purchase, String commodityIds, String specValue, AsyncHttpResponseHandler mHandler) {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("commodityId", commodityId);
			map.put("goods_codes", goods_codes);
			map.put("num", num);
			map.put("type", type);
			map.put("purchase", purchase);
			map.put("commodityIds", commodityIds);
			map.put("specValue", specValue);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("commodityId", commodityId);
			params.put("goods_codes", goods_codes);
			params.put("num", num);
			params.put("type", type);
			params.put("purchase", purchase);
			params.put("commodityIds", commodityIds);
			params.put("specValue", specValue);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR, params, mHandler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 商品详情的加入购物车 2
	 * 
	 * @param TV_PRICE
	 * 
	 *            TV_PRICE": 11, 规格值对应的商品价格 "SPEC_VALUE": 规格值名称,
	 *            "SPEC_VALUE_ID": 规格值id
	 * 
	 *            "SPEC_NAME": 规格名称, "SPEC_NAME_ID": 规格名id
	 * 
	 * 
	 */
	public static void goodsDetailsAddcar(String commodityId, String tv_price, String spec_value, String spec_value_id,
			String spec_name, String spec_name_id, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("commodityId", commodityId);
			params.put("tv_price", tv_price);
			params.put("spec_value", spec_value);
			params.put("spec_value_id", spec_value_id);
			params.put("spec_name", spec_name);
			params.put("spec_name_id", spec_name_id);
			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR_add, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 三级分类列表商品详情页
	 * 
	 * @param PRICE
	 *            乐虎价格, COMMODITY_NAME 商品名称, COMMODITY_ID 商品id, COMMODITY_AD
	 *            商品广告语,
	 * 
	 *            NUM COMMODITY_NAME
	 * 
	 */
	public static void goodsDetailsHttp(String userId, String commodity_id, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			if (userId != null) {
				params.put("userId", userId);
			}
			params.put("COMMDOITY_ID", commodity_id);
			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// -----------------------------------------启动页------------------------------------
	public static void appOpenPicture(AsyncHttpResponseHandler mHandler) {
		post(URLs.appOpenPicture, mHandler);
	}

	public static void goodsDetailsHttpNew(String userId, String commodity_id, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			if (userId != null) {
				params.put("userId", userId);
			}
			params.put("COMMDOITY_ID", commodity_id);
			post(URLs.appCommdoityDataById, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// -----------------------------------------首页------------------------------------
	/**
	 * 首页
	 * 
	 * @param mHandler
	 */
	public static void appIndexFirst(AsyncHttpResponseHandler mHandler) {
		post(URLs.appIndexFirst, null, mHandler);
	}

	public static void appIndexPoly(AsyncHttpResponseHandler mHandler) {
		post(URLs.appIndexPoly, null, mHandler);
	}

	/**
	 * 获取二级分类列表的数据
	 * 
	 * @param url
	 * @param mHandler
	 */
	public static void getClassListLevel2(String url, String id, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("TWO_ID", id);
		post(url, params, mHandler);
	}

	/**
	 * 获取三级分类列表的数据
	 * 
	 * @param url
	 * @param mHandler
	 */
	public static void getClassListLevel3(String url, String id, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("THREEE_ID", id);
		post(url, params, mHandler);
	}

	public static void appAllFast(AsyncHttpResponseHandler mHandler) {
		post(URLs.appAllFast, null, mHandler);
	}

	public static void appAddFast(AsyncHttpResponseHandler mHandler, String userId, String fastIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("fastIds", fastIds);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("fastIds", fastIds);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appAddFast, null, mHandler);
	}

	// -----------------------------------------首页-结束-----------------------------------

	/**
	 * 三级分类列表商品详情点评晒单
	 * 
	 * @param PHONE
	 *            "15980847932",用户手机号（如果用户名为空，则用户名用手机号替代）, FACE_IMAGE_PATH 用户头像,
	 *            CONTENT "商品不错，真的好喜欢",评价内容, SCORE 5 分数, CREATE_TIME
	 *            "2014-08-14 08:08:36",创建时间, USER_NAME 用户名, SIZEDIMENSION "1",
	 *            大小, COLOR "红色",颜色 COMMODITY_IMAGE_PATH 晒图, NUM : 1, LEVEL_NAME
	 *            : 用户等级 reviewScore 好评率,
	 * @param
	 */

	/**
	 * 三级分类列表商品详情商品图片
	 * 
	 * @param COMMODITY_IMAGE_LIST
	 * 
	 */

	public static void recommentSunSingle(String commodity_id, String commodity_image_list, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("commodity_id", commodity_id);
			params.put("commodity_image_list", commodity_image_list);
			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_RECOMMENT, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 四级分类列表商品筛选 1， 类别属性
	 * 
	 * @param PROPERTYNAME
	 *            PROPERTYID
	 */

	public static void selecteAttributes(String attributeId, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("THREE_ID", attributeId);
			post(URLs.SELECTTION_URL, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 四级分类列表商品筛选 2， 类别属性值
	 * 
	 * @param PROPERTYNAME
	 *            属性名, PROPERTYID 属性名对应id BRAND_ID 品牌id, BRAND_NAME 品牌名称
	 * 
	 * 
	 */

	public static void ScreeningAttributeValue(String attributeValueId, String propertyName, String propertyId, String brandId,
			String bandName, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("attributeValueId", attributeValueId);
			params.put("propertyName", propertyName);
			params.put("propertyId", propertyId);
			params.put("brandId", brandId);
			params.put("bandName", bandName);
			post(URLs.SCTRRNING_VALUE, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 商品点评晒单评论数
	 * 
	 * @param commodity_id
	 *            商品id
	 * 
	 */
	public static void getDianPingShu(String commodity_id, AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("COMMDOITY_ID", commodity_id);
			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 商品点评晒单评论内容
	 * 
	 * @param commodity_id
	 *            商品id
	 * @param pageSize
	 *            每页大小
	 * @param pageIndex
	 *            当前页
	 * @param Flag
	 *            0全部，1好评，2中评, 3差评
	 * 
	 */
	public static void sunSingle(String pageSize, String pageIndex, String commodity_id, String Flag,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("pageSize", pageSize);
			params.put("pageIndex", pageIndex);
			params.put("COMMDOITY_ID", commodity_id);
			params.put("flag", Flag);

			post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN_RECOMMENT, params, mHandler);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// 分类列表图片模式

	public static void getClassListTuPianMode(String url, String id, String flag, String sort, String pageIndex,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("category_id", id);
		params.put("flag", flag);
		params.put("sort", sort);
		params.put("pageIndex", pageIndex);
		post(url, params, mHandler);
	}

	/**
	 * 四级商品列表
	 * 
	 * @param sales_volume
	 *            1销量2价格3评价4人气
	 * @param pageSize
	 *            每页大小 10
	 * @param pageIndex
	 *            当前页 1
	 * @param category_id
	 *            三级类目id
	 * @param propertyId
	 *            属性id,多个属性以逗号隔开
	 * @param BRAND_ID
	 *            品牌id
	 * @param priceStart
	 *            价格起始值
	 * @param priceEnd
	 *            价格结束值
	 * @param mark
	 *            4表示，是通过筛选
	 * @param keyword
	 *            站内搜索关键词
	 * @param two_category_id
	 *            二级分类id
	 * @param sort
	 *            1,升序，2降序
	 * @param mHandler
	 */

	public static void getFourClassList(String sales_volume, String pageSize, String pageIndex, String category_id,
			String propertyId, String BRAND_ID, String priceStart, String priceEnd, String mark, String keyword,
			String two_category_id, String sort, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("sales_volume", sales_volume);
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		params.put("sort", sort);
		if (null != keyword && !"".equals(keyword)) {// 关键词搜索
			params.put("keyword", keyword);
		} else {// 非关键词搜索
			params.put("category_id", category_id);// 根据三级分类id查询
			if (null != mark && "4".equals(mark)) {// 属性值帅选
				params.put("propertyId", propertyId);
				params.put("BRAND_ID", BRAND_ID);
				if (null != priceStart && !"".equals(priceStart)) {
					params.put("priceStart", priceStart);
					params.put("priceEnd", priceEnd);
				}
				params.put("mark", mark);
			} else if (null != two_category_id && !"".equals(two_category_id)) {// 根据二级分类id查询
				params.put("two_category_id", two_category_id);
			}
		}
		post(URLs.SORT_LIST_LEVEL_TUPIAN, params, mHandler);
	}

	/**
	 * 银联支付
	 * 
	 * @param url
	 * @param orderId
	 * @param mHandler
	 */
	public static void payForUP(String url, String orderId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		post(url, params, mHandler);
	}

	/**
	 * 支付宝支付
	 * 
	 * @param url
	 * @param orderId
	 * @param mHandler
	 */
	public static void payForAlipay(String url, String orderId, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			Map<String, Object> map = new HashMap<String, Object>();
			String mOrderId;
			mOrderId = Des3.encode(orderId);
			map.put("orderId", mOrderId);
			params.put("orderId", mOrderId);
			String paramStr = CryptionUtil.getSignData(map);
			params.put("mKey", paramStr);
			post(url, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信支付，获取后台签名和相关数据
	 * 
	 * @param url
	 * @param orderId
	 * @param mHandler
	 */
	public static void payForWX(String url, String orderId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		post(url, params, mHandler);
	}

	/**
	 * 微信支付，获取access_token
	 * 
	 * @param url
	 *            https://api.weixin.qq.com/cgi-bin/token
	 * @param grant_type
	 *            获取access_toke，此处填写client_credential
	 * @param appid
	 *            APP唯一凭证
	 * @param secret
	 *            应用密钥，在微信开放平台提交应用审核通过后获得
	 * @param mHandler
	 */
	public static void getAccess_token(String appid, String secret, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("grant_type", "client_credential");
		params.put("appid", appid);
		params.put("secret", secret);
		post(URLs.WXPAY_ACCESS_TOKEN, params, mHandler);
	}

	/**
	 * 微信支付，生成预支付订单
	 * 
	 * @param url
	 *            https://api.weixin.qq.com/pay/genprepay
	 * @param access_token
	 *            getAccess_token方法获取的ACCESS_TOKEN
	 * @param appid
	 *            应用唯一标识，在微信开放平台提交应用审核通过后获得
	 * @param traceid
	 *            商家对用户的唯一标识,如果用微信SSO，此处建议填写授权用户的openid
	 * @param noncestr
	 *            32位内的随机串，防重发
	 * @param packagename
	 *            订单详情（具体生成方法见后文）
	 * @param timestamp
	 *            时间戳，为1970年1月1日00:00到请求发起时间的秒数
	 * @param app_signature
	 *            签名（具体生成方法见后文）
	 * @param sign_method
	 *            加密方式，默认为sha1
	 * @param mHandler
	 */
	public static void getOrderInfo(String access_token, String appid, String traceid, String noncestr, String packagename,
			String timestamp, String app_signature, String sign_method, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("access_token", access_token);
		params.put("appid", appid);
		params.put("traceid", traceid);
		params.put("noncestr", noncestr);
		params.put("package", packagename);
		params.put("timestamp", timestamp);
		params.put("app_signature", app_signature);
		params.put("sign_method", sign_method);
		post(URLs.WXPAY_ORDER_INFO, params, mHandler);
	}

	/**
	 * 支付回调
	 * 
	 * @param type
	 *            支付类型 1.银联支付 2.支付宝3.微信支付
	 * @param orderId
	 *            订单号
	 * @param tn
	 *            流水号
	 * @param mHandler
	 */
	public static void toRequestWebResult(int type, String orderId, String tn, AsyncHttpResponseHandler mHandler) {
		StringBuffer url = new StringBuffer();
		url.append(URLs.SERVER_URL);
		Map<String, Object> map = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		if (1 == type) {// 银联支付
			map.put("orderNumber", orderId);
			map.put("tn", tn);
			params.put("orderNumber", orderId);
			params.put("tn", tn);
			url.append("alipayUnionpayAfter.do");
		} else if (2 == type) {
			System.out.println("加了orderid------" + orderId);
			map.put("orderId", orderId);
			params.put("orderId", orderId);
			url.append("alipayAfter.do");
		} else if (3 == type) {
			System.out.println("加了orderid------" + orderId);
			map.put("orderId", orderId);
			params.put("orderId", orderId);
			url.append("alipayAfter1.do");
		}
		String paramStr = CryptionUtil.getSignData(map);
		params.put("mKey", paramStr);
		System.out.println(url + "");
		post(url.toString(), params, mHandler);
	}

	/**
	 * 筛选点击完成
	 * 
	 * @param "PRICE": 商品价格, "COMMODITY_IMAGE_PATH": 商品图片, COMMODITY_NAME": 商品名称
	 *        PROPERTYID:属性名对应id（多个id以逗号隔开） BRAND_ID 品牌id priceStart 价格起始值
	 *        priceEnd价格结束值
	 */

	public static void FinishValue(String brand_id, String propertyid, String priceStart, String priceEnd, String price,
			String commodity_image_path, String commodity_name, AsyncHttpResponseHandler mHandler) {

		try {

			RequestParams params = new RequestParams();
			params.put("brand_id", propertyid);
			params.put("propertyid", propertyid);
			params.put("priceStart", priceStart);
			params.put("priceEnd", priceEnd);
			params.put("price", price);
			params.put("commodity_image_path", commodity_image_path);
			params.put("commodity_name", commodity_name);
			post(URLs.SCTRRNING_VALUE_finish, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 热销排行商品名称接口

	public static void getHLVHttp(AsyncHttpResponseHandler mHandler) {
		try {
			post(URLs.SALERANK, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 热销排行商品列表接口

	/*
	 * @two_parent_class pageSize pageNum
	 */

	public static void getVlvHttp(String id, String pageIndex, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("two_parent_class", id);
			params.put("pageNum", pageIndex);
			post(URLs.SALERANK_list, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 站内搜索初始化
	/**
	 * USER_ID 用户Id KEYWORD 搜索关键字
	 */

	public static void SearchInit(AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			post(URLs.SEARCH_INIT, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 免运费
	 * 
	 */

	public static void FreeShipping(AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			post(URLs.FREESHIPPING, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 站内搜索下拉框
	/**
	 * 
	 * 
	 * @param url
	 * @param mHandler
	 */

	public static void searchHistroyList(String key_word, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("keyword", key_word);
			post(URLs.SEARCH_HISTROYLIST, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 周边商店
	 * 
	 * @param cityName
	 * @param pageIndex
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param mHandler
	 */
	public static void appNearbyStore(int cityId, String cityName, int pageIndex, double longitude, double latitude,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		// params.put("cityId", String.valueOf(cityId));
		params.put("cityName", cityName);
		params.put("pageIndex", String.valueOf(pageIndex));
		params.put("longitude", String.valueOf(longitude));
		params.put("latitude", String.valueOf(latitude));
		post(URLs.appNearbyStore, params, mHandler);
	}

	/**
	 * 获取版本
	 * 
	 * @param mHandler
	 */
	public static void getVersion(AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("t", "1");
		params.put("i", AppContext.getInstance().getNearbyId());
		post(URLs.GET_VERSION, params, mHandler);
	}

	// 获取专区一和专区二的数据
	public static void getZhuanQu(String url, String id, String pageIndex, String flag, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("pageIndex", pageIndex);
		params.put("flag", flag);
		post(url, params, mHandler);
	}

	/**
	 * 获取积分Club的数据
	 * 
	 * */
	public static void getClub(String userId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		post(URLs.GET_CLUB_DATA, params, mHandler);
	}

	/**
	 * 提交积分Club的数据
	 * 
	 * */
	public static void submitClub(String userId, String id, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("luckId", id);
		post(URLs.SUBMIT_CLUB_DATA, params, mHandler);
	}

	/**
	 * 通过二维码或者一维码获取商品ID
	 * 
	 * @param code
	 * @param mHandler
	 */
	public static void scanningCode(String code, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("code", code);
		post(URLs.scanningCode, params, mHandler);
	}

	/**
	 * 商品详情收藏商品
	 * 
	 * @param USER_ID
	 * @param COMMODITY_ID
	 * @param mHandler
	 */
	public static void appCollect(String USER_ID, String COMMODITY_ID, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("USER_ID", String.valueOf(USER_ID));
		params.put("COMMODITY_ID", COMMODITY_ID);
		post(URLs.appCollect, params, mHandler);
	}

	/**
	 * 商品详情取消收藏
	 * 
	 * @param USER_ID
	 * @param COMMODITY_ID
	 * @param mHandler
	 */
	public static void appCancelCollect(String USER_ID, String COMMODITY_ID, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("USER_ID", String.valueOf(USER_ID));
		params.put("COMMODITY_ID", COMMODITY_ID);
		post(URLs.appCancelCollect, params, mHandler);
	}

	/**
	 * 数据统计，统计用户量
	 * 
	 * @param imei
	 * @param verision
	 * @param mHandler
	 */
	public static void sendMobileInfo(String imei, String version, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("imei", imei);
		params.put("model", android.os.Build.MODEL);
		params.put("sysVersion", android.os.Build.VERSION.RELEASE);
		params.put("version", version);
		params.put("type", 1 + "");// 1代表安卓，2代表iOS
		params.put("nearbyId", AppContext.getInstance().getNearbyId());
		post(URLs.SEND_MOBILE_INFO, params, mHandler);
	}

	/**
	 * 融云
	 * 
	 * @param mHandler
	 */
	public static void sendRongCloud(AsyncHttpResponseHandler mHandler) {
		post(URLs.RONGCLOUD_TOKEN, mHandler);
	}

	/**
	 * 物流列表查询
	 * 
	 * @param mHandler
	 * @param userId
	 */
	public static void appQueryLogistics(AsyncHttpResponseHandler mHandler, String userId, String orderId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		if (!StringUtils.isBlank(orderId))
			params.put("orderId", orderId);
		post(URLs.appQueryLogistics, params, mHandler);
	}

	/**
	 * 车辆定位
	 * 
	 * @param mHandler
	 * @param id
	 */
	public static void carLocattion(AsyncHttpResponseHandler mHandler, int id) {
		RequestParams params = new RequestParams();
		params.put("id", String.valueOf(id));
		post(URLs.connected, params, mHandler);
	}

	/**
	 * @param mHandler
	 * @param type
	 *            （类型，默认不传，查询全部，1：充值 2：消费 3：退款）
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param token
	 *            （令牌，没有传空）
	 * @param cardNum
	 * @param curPage
	 *            （当前页）
	 */
	public static void appPrepaidCardsList(AsyncHttpResponseHandler mHandler, int type, String startTime, String endTime,
			String userId, String token, String cardNum, int curPage) {
		RequestParams params = new RequestParams();
		if (type != 0)
			params.put("type", String.valueOf(type));
		if (!StringUtils.isBlank(startTime))
			params.put("startTime", startTime);
		if (!StringUtils.isBlank(endTime))
			params.put("endTime", endTime);
		params.put("userId", userId);
		if (!StringUtils.isBlank(token))
			params.put("token", token);
		if (!StringUtils.isBlank(cardNum))
			params.put("cardNum", cardNum);
		params.put("curPage", String.valueOf(curPage));
		String val = cardNum + 123 + curPage + 123 + userId + 123 + type;
		val = CryptionUtil.md5(val);
		params.put("val", val);
		post(URLs.appPrepaidCardsList, params, mHandler);
	}

	/***
	 * 获取系统消息
	 * 
	 * @param userId
	 * @param mHandler
	 */
	public static void getSystemMsg(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("pageIndex", pageindex);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_time = sf.format(new Date());
			map.put("time", str_time);
			String paramStr = CryptionUtil.getSignData(map);

			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pageIndex", pageindex);
			params.put("time", str_time);
			params.put("mKey", paramStr);
			post(URLs.SYSTEMMESSAGE_LIST, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 修改系统消息状态
	 * 
	 * @param letterId
	 * @param mHandler
	 */
	public static void appLetterReader(String letterId, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("letterId", letterId);
			post(URLs.SYSTEMMESSAGE_STUATS, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 绑定生活服务卡
	 * 
	 * @param userId
	 * @param cardNum
	 * @param password
	 * @param mHandler
	 * @throws Exception
	 */
	public static void appBindCardInfo(String userId, String cardNum, String password, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("cardNum", cardNum);
			String mPassword;
			mPassword = Des3.encode(password);
			params.put("password", mPassword);
			String val = cardNum + 123 + mPassword + 123 + userId;
			val = CryptionUtil.md5(val);
			params.put("val", val);
			post(URLs.appBindCardInfo, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 生活服务卡修改密码
	 * 
	 * @param userId
	 * @param cardNum
	 * @param oldPwd
	 * @param newPwd
	 * @param mHandler
	 */
	public static void appUpdateCardPwd(String userId, String cardNum, String oldPwd, String newPwd,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("cardNum", cardNum);
			oldPwd = Des3.encode(oldPwd);
			params.put("oldPwd", oldPwd);
			newPwd = Des3.encode(newPwd);
			params.put("newPwd", newPwd);
			String val = cardNum + 123 + oldPwd + 123 + newPwd + 123 + userId;
			val = CryptionUtil.md5(val);
			params.put("val", val);
			post(URLs.appUpdateCardPwd, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 申请生活服务卡
	 * 
	 * @param userId
	 * @param tel
	 * @param vCard
	 * @param userName
	 * @param sCard
	 * @param address
	 * @param mHandler
	 */
	public static void appApplyCard(String userId, String tel, String vCard, String userName, String sCard, String address,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("tel", tel);
			params.put("vCard", vCard);
			userName = Des3.encode(userName);
			params.put("userName", userName);
			params.put("sCard", sCard);
			address = Des3.encode(address);
			params.put("address", address);
			String val = userId + 123 + tel + 123 + vCard + 123 + userName + 123 + address;
			val = CryptionUtil.md5(val);
			params.put("val", val);
			post(URLs.appApplyCard, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 验证生活服务卡号
	 * 
	 * @param userId
	 * @param tel
	 * @param vCard
	 * @param userName
	 * @param sCard
	 * @param address
	 * @param mHandler
	 */
	public static void appValidationCardInfo(String cardNum, String token, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("cardNum", cardNum);
		params.put("token", token);
		String val = CryptionUtil.md5(cardNum);
		params.put("val", val);
		post(URLs.appValidationCardInfo, params, mHandler);
	}

	/***
	 * 充值生活服务卡
	 * 
	 * @param userId
	 * @param cardNum
	 * @param money
	 * @param token
	 * @param mHandler
	 */
	public static void appAddRechargeOrder(String userId, String cardNum, String money, String token, String type, String pid,
			AsyncHttpResponseHandler mHandler) {
		try {
			String val = userId + "123" + money + "123" + cardNum + "123" + type + "123" + pid;
			val = CryptionUtil.md5(val);
			RequestParams params = new RequestParams();
			params.put("id", userId);
			params.put("cardNum", cardNum);
			params.put("money", money);
			params.put("token", token);
			params.put("type", type);
			params.put("pid", pid);
			params.put("val", val);
			post(URLs.appAddRechargeOrder, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解除服务卡绑定
	 * 
	 * @param mHandler
	 * @param userId
	 * @param cardNum
	 * @param password
	 */
	public static void appRelieveBindCard(AsyncHttpResponseHandler mHandler, String userId, String cardNum, String password) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("cardNum", cardNum);
			password = Des3.encode(password);
			params.put("password", password);
			String val = cardNum + 123 + userId + 123 + password;
			val = CryptionUtil.md5(val);
			params.put("val", val);
			post(URLs.appRelieveBindCard, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务卡支付
	 * 
	 * @param mHandler
	 * @param userId
	 * @param cardNum
	 * @param password
	 * @param money
	 * @param orderId
	 * @param token
	 */
	public static void appCardRedeem(AsyncHttpResponseHandler mHandler, String userId, String cardNum, String password,
			String money, String orderId, String token, String id) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("cardNum", cardNum);
			password = Des3.encode(password);
			params.put("password", password);
			params.put("money", money);
			params.put("orderId", orderId);
			params.put("token", token);
			params.put("id", id);
			String val = cardNum + 123 + userId + 123 + password + 123 + id;
			val = CryptionUtil.md5(val);
			params.put("val", val);
			post(URLs.appCardRedeem, params, mHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 充值优惠活动列表
	 * 
	 * @param mHandler
	 */
	public static void appCardPromotionsList(AsyncHttpResponseHandler mHandler) {
		post(URLs.appCardPromotionsList, mHandler);
	}

	/**
	 * 验证是否能申请售后
	 * 
	 * @param orderId
	 * @param commodityId
	 * @param mHandler
	 */
	public static void appVerifyChangeAndReturn(String orderId, String commodityId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("commodityId", commodityId);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_time = sf.format(new Date());
		map.put("time", str_time);
		String paramStr = CryptionUtil.getSignData(map);

		params.put("orderId", orderId);
		params.put("commodityId", commodityId);
		params.put("time", str_time);
		params.put("mKey", paramStr);
		post(URLs.appVerifyChangeAndReturn, params, mHandler);
	}

	public static void getWeChatToken(String code, AsyncHttpResponseHandler mHandler) {
		String url = String.format(
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%1s&secret=%2s&code=%3s&grant_type=authorization_code",
				Constants.APP_ID, Constants.APP_SECRET, code);
		Log.d("WXEntryActivity", "get access token, url = " + url);
		mClient.get(url, mHandler);
	}

	/**
	 * 自提门店列表
	 * 
	 * @param cityId
	 *            城市ID
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param mHandler
	 */
	public static void appNearShop(int cityId, double longitude, double latitude, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("cityId", String.valueOf(cityId));
		params.put("longitude", String.valueOf(longitude));
		params.put("latitude", String.valueOf(latitude));
		post(URLs.appNearShop, params, mHandler);
	}

	/**
	 * 微信检测账户接口
	 * 
	 * @param openid
	 * @param mHandler
	 */
	public static void appOpenIDOAuthPhone(String openid, int flag, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("openid", openid);
		if (flag != 0)
			params.put("flag", String.valueOf(flag));
		post(URLs.appOpenIDOAuthPhone, params, mHandler);
	}

	/**
	 * 秒杀标题
	 * 
	 * @param mHandler
	 */
	public static void seckillLateralTitle(AsyncHttpResponseHandler mHandler) {
		post(URLs.seckillLateralTitle, mHandler);
	}

	/**
	 * 秒杀列表
	 * 
	 * @param seckillId
	 * @param mHandler
	 */
	public static void allSecKill(int seckillId, int pageIndex, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("seckillId", String.valueOf(seckillId));
		params.put("pageIndex", String.valueOf(pageIndex));
		if (!StringUtils.isBlank(AppContext.getInstance().getUserId()))
			params.put("userId", AppContext.getInstance().getUserId());
		post(URLs.allSecKill, params, mHandler);
	}

	/**
	 * 闪购列表
	 * 
	 * @param pageIndex
	 * @param mHandler
	 */
	public static void flashSaleActive(int pageIndex, int type, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(pageIndex));
		params.put("type", String.valueOf(type));
		post(URLs.flashSaleActive, params, mHandler);
	}

	/**
	 * 闪购专区
	 * 
	 * @param pageIndex
	 * @param id
	 * @param mHandler
	 */
	public static void flashSaleRegion(int pageIndex, int id, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(pageIndex));
		params.put("id", String.valueOf(id));
		post(URLs.flashSaleRegion, params, mHandler);
	}

	/**
	 * 新品预约分类
	 * 
	 * @param mHandler
	 */
	public static void bespeakType(AsyncHttpResponseHandler mHandler) {
		post(URLs.bespeakType, mHandler);
	}

	/**
	 * 新品预约列表 type 新品预约分类ID (不传 或传-1 表示 全部)
	 * 
	 * @param mHandler
	 */
	public static void bespeak(int type, int pageIndex, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("type", String.valueOf(type));
		params.put("pageIndex", String.valueOf(pageIndex));
		if (AppContext.getInstance().getUserId() != null) {
			params.put("userId", AppContext.getInstance().getUserId());
		}
		post(URLs.bespeak, params, mHandler);
	}

	/**
	 * 新品预约结果
	 * 
	 * @param mHandler
	 */
	public static void bespeakRecord(int Id, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Id", String.valueOf(Id));
		params.put("userId", AppContext.getInstance().getUserId());
		post(URLs.bespeakRecord, params, mHandler);
	}

	/**
	 * 我的预约列表
	 * 
	 * @param mHandler
	 */
	public static void bespeakRecordList(int pageIndex, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("userId", AppContext.getInstance().getUserId());
		params.put("pageIndex", String.valueOf(pageIndex));
		post(URLs.bespeakRecordList, params, mHandler);
	}

	/**
	 * 介绍的统一
	 * 
	 * @param introduceId
	 * @param mHandler
	 */
	public static void introduceId(int introduceId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("introduceId", String.valueOf(introduceId));
		post(URLs.introduceId, params, mHandler);
	}

	/**
	 * judgeSeckill
	 * 
	 * @param seckillId
	 * @param mHandler
	 */
	public static void judgeSeckill(int seckillId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("seckillId", String.valueOf(seckillId));
		post(URLs.judgeSeckill, params, mHandler);
	}

	/**
	 * 发票信息
	 * 
	 * @param invoiceId
	 * @param mHandler
	 */
	public static void invoiceInfo(int invoiceId, AsyncHttpResponseHandler mHandler) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("invoiceId", String.valueOf(invoiceId));
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("invoiceId", String.valueOf(invoiceId));
		params.put("mKey", paramStr);
		post(URLs.invoiceInfo, params, mHandler);
	}

	/**
	 * 热搜商品
	 * 
	 */
	public static void hotSearch(int pageIndex, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("pageIndex", String.valueOf(pageIndex));
		post(URLs.HOT_SEARCH, params, mHandler);
	}

	/**
	 * 乐虎券删除
	 * 
	 * @param mHandler
	 * @param id
	 */
	public static void appMyCouponDelete(AsyncHttpResponseHandler mHandler, int id) {
		RequestParams params = new RequestParams();
		params.put("Id", String.valueOf(id));
		post(URLs.appMyCouponDelete, params, mHandler);
	}

	/**
	 * 图片上传
	 * 
	 * @param handler
	 * @param data
	 * @param fileType
	 */
	public static void imgUpload(AsyncHttpResponseHandler handler, String data, String fileType, String userId, int flag) {
		RequestParams params = new RequestParams();
		params.put("data", data);
		params.put("fileType", fileType);
		params.put("userId", userId);
		params.put("flag", String.valueOf(flag));
		post(URLs.appImgUpload, params, handler);
	}

	/**
	 * 会员等级
	 * 
	 * @param handler
	 * @param userId
	 */
	public static void appUserRank(AsyncHttpResponseHandler handler, String userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		post(URLs.appUserRank, params, handler);
	}

	/**
	 * 根据ID查询订单价格
	 * 
	 * @param handler
	 * @param orderId
	 */
	public static void orderPrice(AsyncHttpResponseHandler handler, String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		String paramStr = CryptionUtil.getSignData(map);

		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		params.put("mKey", paramStr);
		post(URLs.orderPrice, params, handler);
	}

}
