package com.huiyin.ui.user;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.login.AuthListener;
import cn.bidaround.ytcore.login.AuthLogin;
import cn.bidaround.ytcore.login.AuthUserInfo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LoginInfo;
import com.huiyin.bean.ThirdLoginBean;
import com.huiyin.pay.wxpay.Constants;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.flash.FlashPrefectureActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.seckill.SeckillActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.ui.show.TheShowCommentActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.wight.Tip;
import com.huiyin.wxapi.TokenBean;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LoginActivity extends BaseActivity implements OnClickListener {

	public final static String TAG = "LoginActivity";
	public static String hkdetail_code = "hkdetail";
	public static String account_code = "account_code";
	private TextView login_forget_pwd, left_ib, middle_title_tv;
	private EditText login_user_et, login_pwd_et;
	private Toast mToast;
	private Button login_btn;
	private CheckBox login_check_box;
	private LoginInfo mLoginInfo;
	private String userName, password;
	// 判断从哪个页面跳转到这个页面
	private String code;
	private String pushFlag;
	private String customContentString;
	private String description;
	private int loginTimes = 0;
	private boolean isChecked = false;
	private TextView ab_right;

	private ImageView zfb, wx, wb, qq;
	private int loginType = 0;

	private PreferenceUtil instance = null;

	public static Map<String, String> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_layout);
		instance = PreferenceUtil.getInstance(mContext);
		initView();
		YtTemplate.init(this);
	}

	private void initView() {

		zfb = (ImageView) findViewById(R.id.login_zfb);
		zfb.setOnClickListener(this);
		wx = (ImageView) findViewById(R.id.login_wx);
		wx.setOnClickListener(this);
		wb = (ImageView) findViewById(R.id.login_wb);
		wb.setOnClickListener(this);
		qq = (ImageView) findViewById(R.id.login_qq);
		qq.setOnClickListener(this);

		login_forget_pwd = (TextView) findViewById(R.id.login_forget_pwd);
		login_forget_pwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(LoginActivity.this, BackPswActivity.class);
				startActivity(i);
				finish();
			}

		});

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (pushFlag.equals("1")) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
				} else {
					finish();
				}
			}

		});

		code = getIntent().getStringExtra(TAG);
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		customContentString = getIntent().getStringExtra("customContentString");
		description = getIntent().getStringExtra("description");

		mLoginInfo = new LoginInfo();

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("登录");

		ab_right = (TextView) findViewById(R.id.ab_right);
		ab_right.setText("注册");
		ab_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

		login_user_et = (EditText) findViewById(R.id.login_user_et);
		login_pwd_et = (EditText) findViewById(R.id.login_pwd_et);

		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doLogin();
			}
		});

		login_check_box = (CheckBox) findViewById(R.id.login_check_box);
		login_check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				isChecked = arg1;
				if (arg1) {
					login_check_box.setTextColor(getResources().getColor(R.color.black));
				} else {
					login_check_box.setTextColor(getResources().getColor(R.color.grey1));
				}
			}
		});

		if (login_check_box.isChecked()) {
			isChecked = true;
			login_check_box.setTextColor(getResources().getColor(R.color.black));
		} else {
			isChecked = false;
			login_check_box.setTextColor(getResources().getColor(R.color.grey1));
		}

	}

	private void showToast(int resId) {

		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	private boolean checkLoginInfo(String userName, String password) {

		if (TextUtils.isEmpty(userName)) {
			showToast(R.string.user_is_null);
			return false;
		} else if (TextUtils.isEmpty(password)) {
			showToast(R.string.pwd_is_null);
			return false;
		}
		return true;

	}

	/**
	 * 登录后刷新数据
	 */
	private void refreshData() {

		mLoginInfo.psw = password;
		mLoginInfo.userName = userName;
		mLoginInfo.isChecked = isChecked;
		AppContext.saveLoginInfo(getApplicationContext(), mLoginInfo);

		// 推送消息的处理，尼玛坑爹呢。
		if (pushFlag.equals("1")) {
			SwitchActivity();
			return;
		}

		if (code != null && code.equals(hkdetail_code)) {
			LoginActivity.this.finish();
		} else {
			Intent i = new Intent();
			i.setClass(LoginActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			LoginActivity.this.finish();
		}

	}

	private void doLogin() {

		userName = login_user_et.getText().toString();
		password = login_pwd_et.getText().toString();
		if (!checkLoginInfo(userName, password)) {
			return;
		}
		RequstClient.doLogin(userName, password, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doLogin:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						UserinfoPreferenceUtil.setLoginFail(mContext);
						return;
					}

					UserInfo mUserInfo = new Gson().fromJson(new JSONObject(content).getJSONObject("user").toString(),
							UserInfo.class);
					
					
//					AppContext.mUserInfo = mUserInfo;
//					AppContext.userId = mUserInfo.userId;

					//做购物车底下的数量 先用广播的，后来直接取刷新界面了
					// Intent intent = new Intent();
					// intent.putExtra("shoppcar", mUserInfo.SHOPPING_CAR);
					// intent.putExtra("message",
					// Integer.valueOf(mUserInfo.systemMessage));
					// intent.setAction("com.login.success");
					// sendBroadcast(intent);

					UserinfoPreferenceUtil.setLoginSuccess(mContext, mUserInfo);
					
					refreshData();

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 支付宝第三方登录
		case R.id.login_zfb:

			break;
		// 微信第三方登录
		case R.id.login_wx:
			onWXClickLogin();
			break;
		// 新浪微博第三方登录
		case R.id.login_wb:
			onSinaClickLogin();
			break;
		// QQ第三方登录
		case R.id.login_qq:
			onQQClickLogin();
			break;
		default:
			break;
		}
	}

	/**
	 * 微信登录
	 */
	private void onWXClickLogin() {
		loginType = 1;
		IWXAPI api;
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
			Toast.makeText(mContext, "微信未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			return;
		}
		// 注册到微信
		if (api.registerApp(Constants.APP_ID)) {
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "com.huiyin.login";

			api.sendReq(req);
		}
	}

	/**
	 * 新浪登录
	 */
	private void onSinaClickLogin() {
		loginType = 2;
		AuthLogin authSinaLogin = new AuthLogin();
		AuthListener SinaListener = new AuthListener() {
			@Override
			public void onAuthFail(Activity act) {
				Toast.makeText(act, "新浪微博未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAuthCancel(Activity act) {
			}

			@Override
			public void onAuthSucess(Activity act, AuthUserInfo info) {
				instance.setQQOpenid(info.getQqOpenid());
				Gson gson = new Gson();
				String i = gson.toJson(info, AuthUserInfo.class);
				oauthUser(null, info.getSinaUid(), i);
			}
		};
		authSinaLogin.sinaAuth(this, SinaListener);
	}

	/**
	 * QQ登录
	 */
	private void onQQClickLogin() {
		loginType = 3;
		AuthLogin authQQLogin = new AuthLogin();
		AuthListener QQListener = new AuthListener() {

			@Override
			public void onAuthFail(Activity act) {
				Toast.makeText(act, "QQ未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAuthCancel(Activity act) {

			}

			@Override
			public void onAuthSucess(Activity act, AuthUserInfo info) {
				instance.setQQOpenid(info.getQqOpenid());
				Gson gson = new Gson();
				String i = gson.toJson(info, AuthUserInfo.class);
				oauthUser(null, info.getQqOpenid(), i);
			}
		};
		authQQLogin.qqAuth(this, QQListener);
	}

	@Override
	protected void onDestroy() {
		YtTemplate.release(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		String code = intent.getStringExtra("code");
		if (code != null) {
			getAssessToken(code);
		}
	}

	private void getAssessToken(String code) {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onStart() {
				super.onStart();
				Tip.showLoadDialog(mContext, "正在加载...");
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
			}

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				TokenBean bean = TokenBean.explainJson(content, mContext);
				if (bean.access_token != null && bean.openid != null) {
					oauthUser(bean, null, null);
				} else {
					Tip.colesLoadDialog();
					Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.getWeChatToken(code, handler);
	}

	private void oauthUser(final TokenBean bean, final String openId, final String userinfo) {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
			}

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Tip.colesLoadDialog();
				ThirdLoginBean data = ThirdLoginBean.explainJson(content, mContext);
				if (data.type <= 0) {
					Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
					return;
				}
				if (data.type == 1) {
//					AppContext.getInstance().mUserInfo = data.user;
//					AppContext.getInstance().userId = data.user.userId;

					UserinfoPreferenceUtil.setLoginSuccess(mContext, data.user);
					
					Intent i = new Intent();
					i.setClass(LoginActivity.this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					LoginActivity.this.finish();
					return;
				}
				if (data.type == 2) {
					showChoseDialog(bean, openId, userinfo);
					return;
				}
			}
		};
		switch (loginType) {
		case 1:
			RequstClient.appOpenIDOAuthPhone(bean.openid, loginType, handler);
			break;
		case 2:
			RequstClient.appOpenIDOAuthPhone(openId, loginType, handler);
			break;
		case 3:
			RequstClient.appOpenIDOAuthPhone(openId, loginType, handler);
			break;
		}

	}

	private Dialog mDialog;

	private void showChoseDialog(final TokenBean bean, final String openId, final String userinfo) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.chose_bind_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent i = new Intent();
				i.setClass(LoginActivity.this, RegisterActivity.class);
				i.putExtra("LoginType", loginType);
				switch (loginType) {
				case 1:
					i.putExtra("access_token", bean.access_token);
					i.putExtra("openid", bean.openid);
					break;
				case 2:
					i.putExtra("userinfo", userinfo);
					break;
				case 3:
					i.putExtra("userinfo", userinfo);
					break;
				}
				startActivity(i);
				LoginActivity.this.finish();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent i = new Intent();
				i.setClass(LoginActivity.this, ThirdBindActivity.class);
				i.putExtra("LoginType", loginType);
				switch (loginType) {
				case 1:
					i.putExtra("openid", bean.openid);
					break;
				case 2:
					i.putExtra("openid", openId);
					break;
				case 3:
					i.putExtra("openid", openId);
					break;
				}
				startActivity(i);
				LoginActivity.this.finish();
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		mDialog.show();
	}

	private void SwitchActivity() {
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				if (customJson.has("msgType")) {
					int msgType = customJson.getInt("msgType");
					JSONObject jumpDate = null;
					if (customJson.has("jumpDate")) {
						jumpDate = customJson.getJSONObject("jumpDate");
					}
					if (AppContext.getInstance().getUserId() != null) {
						switch (msgType) {
						case 7:
						case 8:
						case 10:
						case 11:
						case 12:
						case 13:
						case 14:
						case 15:
						case 16:
						case 17: // 跳转到订单详情页
							Intent intent = new Intent();
							intent.putExtra("order_id", jumpDate.getString("id"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(mContext, MyOrderDetailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							break;
						case 19:
						case 20:
						case 21: // 跳转到预约详情页
							Intent intentYuYue = new Intent();
							intentYuYue.putExtra("orderId", jumpDate.getString("id"));
							intentYuYue.putExtra("pushFlag", "1");
							intentYuYue.setClass(mContext, YuYueDetailActivity.class);
							intentYuYue.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intentYuYue);
							break;
						case 28:
							Intent intentWeb = new Intent();
							intentWeb.putExtra("htmlContent", description);
							if (jumpDate.has("url_type") && jumpDate.has("id")) {
								intentWeb.putExtra("url_type", jumpDate.getInt("url_type"));
								intentWeb.putExtra("id", jumpDate.getInt("id"));
								intentWeb.putExtra("commodityId", jumpDate.getInt("commodityId"));
							}
							intentWeb.putExtra("pushFlag", "1");
							intentWeb.setClass(mContext, MessageWebActivity.class);
							intentWeb.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intentWeb);
							break;
						case 29:
						case 30:
						case 31:
						case 32:
						case 33:
						case 34:
						case 35:
						case 36:
						case 37:
						case 38:
						case 39:
						case 40:
						case 41:
						case 42: // 跳转到换货详情页
							Intent intentHuanhuo = new Intent();
							intentHuanhuo.putExtra("returnId", jumpDate.getString("id"));
							intentHuanhuo.putExtra("commodityId", jumpDate.getString("commodityId"));
							intentHuanhuo.putExtra("goodName", jumpDate.getString("commodityName"));
							intentHuanhuo.putExtra("statu", Integer.parseInt(jumpDate.getString("status")));
							intentHuanhuo.putExtra("pushFlag", "1");
							intentHuanhuo.setClass(mContext, AfterSaleDetailActivity.class);
							intentHuanhuo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intentHuanhuo);
							break;
						case 43:
						case 44:
							Intent intent1 = new Intent();
							intent1.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
							intent1.putExtra("username", jumpDate.getString("spotlightName"));
							intent1.putExtra("pushFlag", "1");
							intent1.setClass(mContext, TheShowCommentActivity.class);
							intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent1);
							break;
						case 51:
							switch (jumpDate.getInt("url_type")) {
							// 1.秒杀 2.闪购 3.新品预约/商品详情 4.分类聚合 5.快捷服务 6.促销活动
							case 1:
								Intent intent7 = new Intent(mContext, SeckillActivity.class);
								intent7.putExtra("id", jumpDate.getInt("id"));
								intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent7);
								break;
							case 2:
								Intent intent4 = new Intent(mContext, FlashPrefectureActivity.class);
								intent4.putExtra("id", jumpDate.getInt("id"));
								intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent4);
								break;
							case 3:
								Intent intent3 = new Intent(mContext, GoodsDetailActivity.class);
								intent3.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
								intent3.putExtra("subscribeId", jumpDate.getInt("id"));
								intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent3);
								break;
							case 4:
								Intent intent2 = new Intent(mContext, ZhuanQuActivity.class);
								intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
								intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
								intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent2);
								break;
							case 5:
								connt(jumpDate.getInt("id"));
								break;
							case 6:
								Intent intent6 = new Intent(mContext, ZhuanQuActivity.class);
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
								intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent6);
								break;
							case 7:
								Intent intent5 = new Intent(mContext, GoodsDetailActivity.class);
								intent5.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
								intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent5);
								break;
							}
							break;
						}
					}
				}
				finish();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void connt(int id) {
		switch (id) {
		case 1:
			// 今日头条
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(mContext, NewsTodayActivity.class);
			startActivity(intent);
			break;
		case 2:
			// 乐虎彩票
			Intent intent1 = new Intent();
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.setClass(mContext, LotteryActivity.class);
			startActivity(intent1);
			break;
		case 3:
			if (!StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				// 预约服务
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(mContext, YuYueShenQingActivity.class);
				startActivity(intent2);
			}
			break;
		case 4:
			// 物流查询
			if (!StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(mContext, LogisticsQueryActivity.class);
				startActivity(intent2);
			}
			break;
		case 5:
			// 智慧管家
			AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
			Intent i = new Intent();
			i.setClass(mContext, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		case 6:
			// 秀场
			Intent intent2 = new Intent();
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.setClass(mContext, TheShowActivity.class);
			startActivity(intent2);
			break;
		case 7:
			// 积分club
			Intent intent3 = new Intent();
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent3.setClass(mContext, ClubActivity.class);
			startActivity(intent3);
			break;
		case 8:
			// 客户中心
			Intent intent4 = new Intent();
			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent4.setClass(mContext, KefuCenterActivity.class);
			startActivity(intent4);
			break;
		case 9:
			Intent intent5 = new Intent();
			intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent5.setClass(mContext, NearTheShopActivityNew.class);
			startActivity(intent5);
			break;
		case 10:
			// 服务卡
			if (!StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Intent fuwu_intent = new Intent();
				if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
					fuwu_intent.setClass(mContext, ServiceCardActivity.class);
				} else {
					fuwu_intent.setClass(mContext, BindServiceCard.class);
				}
				fuwu_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(fuwu_intent);
			}
			break;
		default:
			break;
		}
	}
}
