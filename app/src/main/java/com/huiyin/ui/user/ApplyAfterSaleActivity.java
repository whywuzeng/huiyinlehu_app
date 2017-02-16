package com.huiyin.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.constants.Config;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UploadHelper;
import com.huiyin.utils.UploadHelper.UploadCallBack;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.utils.imageupload.ImageUploadGridView;
import com.huiyin.utils.imageupload.ImageUploadGridView.addPicListener;

public class ApplyAfterSaleActivity extends BaseActivity {

	public final static String TAG = "ApplyAfterSaleActivity";
	private Button after_sale_commit_btn;
	private TextView left_ib, middle_title_tv;
	// 退款、退货
	private TextView back_change_good, getback_money;
	// 退货、换货各种独有信息部分
	private LinearLayout apply_after_changegood_info, apply_after_backmoney_info;
	private static int GOODTYPE = 1;
	private static int MONEYTYPE = 2;

	Drawable img_drawable;
	// 收货人、收货人手机号码、地址、原因
	EditText after_receiver, after_phone, after_addr, after_reason;

	// 银行账户、开户用户名、支行、银行
	EditText bank_account_ed, bank_username_ed, branch_bank_ed, bank_ed;
	// 财付通账号、姓名、手机号码
	EditText cft_account, cft_realname, cft_phone_number;
	// 支付宝账号、姓名、手机号码
	EditText zfb_account, zfb_realname, zfb_phone_number;
	// 支付宝、财付通、银行
	TextView apply_after_zfb, apply_after_cft, apply_after_yh;
	// 银行、财付通、支付宝布局内容
	LinearLayout bank_choice_layout, cft_choice_layout, zfb_choice_layout;
	// 三种支付类型
	private static int ZFB_TYPE = 0, CFT_TYPE = 1, BANK_TYPE = 2;

	private int PAY_TYPE = 2;
	// 订单id、商品id,总价
	private String orderId, goodId, totalPrice, returnRate;
	private String flag;
	private Toast mToast;
	// 收货人、手机号、地址、原因信息
	String receiver, phone, addr, reason;
	// 银行、支行、用户名、银行账号信息
	String bank, branch_bank, username, bank_account;
	// 财付通账号、姓名、手机号码
	String cft_account_str, cft_realname_str, cft_phone_number_str;
	// 支付宝账号、姓名、手机号码
	String zfb_account_str, zfb_realname_str, zfb_phone_number_str;

	// 输入的字数提示
	TextView after_reason_tip;
	// 上传图片的url字符串
	private String picUrl = "";
	// 上传凭证布局
	LinearLayout apply_upload_layout;
	// 退回的money
	TextView back_money_info1;

	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;
	Bitmap curImagePic;

	ImageUploadGridView mUploadImageLayout;
	private ImageUpload mImageUpload;
	private UpLoadImageListener mUpLoadImageListener;

	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	//
	// if (msg.what == 0) {
	// mUploadImageLayout.addPics(curImagePic);
	// UIHelper.cloesLoadDialog();
	// } else if (msg.what == 1) {
	// UIHelper.showLoadDialog(ApplyAfterSaleActivity.this,
	// ApplyAfterSaleActivity.this.getString(R.string.loading));
	// }
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_after_sale);
		initView();
	}

	private void initView() {
		orderId = getIntent().getStringExtra("orderId");
		goodId = getIntent().getStringExtra("goodId");
		totalPrice = getIntent().getStringExtra("totalPrice");
		flag = getIntent().getStringExtra("flag");

		receiver = getIntent().getStringExtra("consignee_name");
		phone = getIntent().getStringExtra("consignee_phone");
		addr = getIntent().getStringExtra("consignee_address");
		returnRate = getIntent().getStringExtra("returnRate");

		// 退款成功后，我们最多返还给您3799.0，手续费3%
//		 String info = "退款成功后，我们最多返还给您<font color=\"#3592e2\">";
//		 if (!StringUtils.isBlank(returnRate)) {
//		 // 退款的计算
//		 double price = Float.parseFloat(totalPrice) * (100 -
//		 Float.parseFloat(returnRate)) / 100;
//		 totalPrice = MathUtil.priceForAppWithSign(price);
//		 info = info + totalPrice + "</font>" + "手续费<font color=\"#3592e2\">"
//		 + returnRate + "%</font>";
//		 } else {
//		 info = info + totalPrice + "</font>元";
//		 }
		String info = "";
		if (!StringUtils.isBlank(returnRate)) {
			// 退款的计算
			info = "退款成功后，我们将收取手续费<font color=\"#3592e2\">" + returnRate + "%</font>";
		} else {
			info = "退款成功后，我们将收取手续费<font color=\"#3592e2\">3%</font>";
		}

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("申请售后");

		back_money_info1 = (TextView) findViewById(R.id.back_money_info1);

		back_money_info1.setText(Html.fromHtml(info));

		// 提交订单
		after_sale_commit_btn = (Button) findViewById(R.id.after_sale_commit_btn);
		after_sale_commit_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (apply_after_backmoney_info.getVisibility() == View.VISIBLE) {
					// 退货
					if (!checkBackInfo()) {
						return;
					}
					if (mUploadImageLayout.getImagesHostUrl() != null && mUploadImageLayout.getImagesHostUrl().size() > 0) {
						mImageUpload = new ImageUpload(mContext, mUploadImageLayout.getImagesHostUrl(), mUpLoadImageListener);
						mImageUpload.startLoad();
						return;
					}
					if (PAY_TYPE == ZFB_TYPE) {
						postBackGoodInfo(zfb_realname_str, zfb_account_str, zfb_phone_number_str);
					} else if (PAY_TYPE == CFT_TYPE) {
						postBackGoodInfo(cft_realname_str, cft_account_str, cft_phone_number_str);
					} else {
						postBackGoodInfo(username, bank_account, "");
					}
				} else {
					// 换货
					if (!checkChangeInfo()) {
						return;
					}
					if (mUploadImageLayout.getImagesHostUrl() != null && mUploadImageLayout.getImagesHostUrl().size() > 0) {
						mImageUpload = new ImageUpload(mContext, mUploadImageLayout.getImagesHostUrl(), mUpLoadImageListener);
						mImageUpload.startLoad();
						return;
					}
					postChangeGoodInfo();
				}
			}
		});

		mUpLoadImageListener = new UpLoadImageListener() {

			@Override
			public void UpLoadSuccess(ArrayList<String> netimageurls) {
				picUrl = "";
				for (int i = 0; i < netimageurls.size(); i++) {
					picUrl += netimageurls.get(i);
					picUrl += ",";
				}
				if (apply_after_backmoney_info.getVisibility() == View.VISIBLE) {
					// 退货
					if (PAY_TYPE == ZFB_TYPE) {
						postBackGoodInfo(zfb_realname_str, zfb_account_str, zfb_phone_number_str);
					} else if (PAY_TYPE == CFT_TYPE) {
						postBackGoodInfo(cft_realname_str, cft_account_str, cft_phone_number_str);
					} else {
						postBackGoodInfo(username, bank_account, "");
					}
				} else {
					// 换货
					postChangeGoodInfo();
				}
			}

			@Override
			public void UpLoadFail() {

			}
		};
		back_change_good = (TextView) findViewById(R.id.back_change_good);
		getback_money = (TextView) findViewById(R.id.getback_money);
		mUploadImageLayout = (ImageUploadGridView) findViewById(R.id.upload_layout);

		mUploadImageLayout.setListener(new addPicListener() {

			@Override
			public void addPicOnClick() {
				Intent i = new Intent(mContext, CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
				startActivityForResult(i, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
			}
		});

		apply_after_changegood_info = (LinearLayout) findViewById(R.id.apply_after_changegood_info);
		apply_after_backmoney_info = (LinearLayout) findViewById(R.id.apply_after_backmoney_info);

		apply_upload_layout = (LinearLayout) findViewById(R.id.apply_upload_layout);

		// 换货
		back_change_good.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setInfoByChoice(GOODTYPE);
			}
		});

		// 退货
		getback_money.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setInfoByChoice(MONEYTYPE);
			}
		});

		img_drawable = getResources().getDrawable(R.drawable.icon_choice);
		img_drawable.setBounds(0, 0, img_drawable.getMinimumWidth(), img_drawable.getMinimumHeight());

		after_receiver = (EditText) findViewById(R.id.after_receiver);
		after_phone = (EditText) findViewById(R.id.after_phone);
		after_addr = (EditText) findViewById(R.id.after_addr);

		after_receiver.setText(receiver);
		after_phone.setText(phone);
		after_addr.setText(addr);
		// 银行
		bank_account_ed = (EditText) findViewById(R.id.bank_account_ed);
		bank_username_ed = (EditText) findViewById(R.id.bank_username_ed);
		branch_bank_ed = (EditText) findViewById(R.id.branch_bank_ed);
		bank_ed = (EditText) findViewById(R.id.bank_ed);
		// 财付通
		cft_account = (EditText) findViewById(R.id.cft_account);
		cft_realname = (EditText) findViewById(R.id.cft_realname);
		cft_phone_number = (EditText) findViewById(R.id.cft_phone_number);
		// 支付宝
		zfb_account = (EditText) findViewById(R.id.zfb_account);
		zfb_realname = (EditText) findViewById(R.id.zfb_realname);
		zfb_phone_number = (EditText) findViewById(R.id.zfb_phone_number);
		// 支付宝、财付通、银行
		apply_after_zfb = (TextView) findViewById(R.id.apply_after_zfb);
		apply_after_cft = (TextView) findViewById(R.id.apply_after_cft);
		apply_after_yh = (TextView) findViewById(R.id.apply_after_yh);

		// 支付类型的布局内容
		bank_choice_layout = (LinearLayout) findViewById(R.id.bank_choice_layout);
		cft_choice_layout = (LinearLayout) findViewById(R.id.cft_choice_layout);
		zfb_choice_layout = (LinearLayout) findViewById(R.id.zfb_choice_layout);
		// 银行
		apply_after_yh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				setChoiceLayout(BANK_TYPE);
			}
		});
		// 财付通
		apply_after_cft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				setChoiceLayout(CFT_TYPE);
			}
		});
		// 支付宝
		apply_after_zfb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				setChoiceLayout(ZFB_TYPE);
			}
		});
		after_reason = (EditText) findViewById(R.id.after_reason);
		after_reason_tip = (TextView) findViewById(R.id.after_reason_tip);
		after_reason.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				after_reason_tip.setText(arg0.length() + "/300字");
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

	}

	/**
	 * 选择支付方式
	 */
	private void setChoiceLayout(int type) {

		if (ZFB_TYPE == type) {// 支付宝
			PAY_TYPE = ZFB_TYPE;

			apply_after_zfb.setBackgroundResource(R.color.blue_color);
			apply_after_cft.setBackgroundResource(R.color.white);
			apply_after_yh.setBackgroundResource(R.color.white);

			apply_after_zfb.setTextColor(getResources().getColor(R.color.white));
			apply_after_cft.setTextColor(getResources().getColor(R.color.black));
			apply_after_yh.setTextColor(getResources().getColor(R.color.black));

			bank_choice_layout.setVisibility(View.GONE);
			cft_choice_layout.setVisibility(View.GONE);
			zfb_choice_layout.setVisibility(View.VISIBLE);

		} else if (CFT_TYPE == type) {// 财付通
			PAY_TYPE = CFT_TYPE;
			apply_after_zfb.setBackgroundResource(R.color.white);
			apply_after_zfb.setTextColor(getResources().getColor(R.color.black));
			apply_after_cft.setBackgroundResource(R.color.blue_color);
			apply_after_cft.setTextColor(getResources().getColor(R.color.white));
			apply_after_yh.setBackgroundResource(R.color.white);
			apply_after_yh.setTextColor(getResources().getColor(R.color.black));
			bank_choice_layout.setVisibility(View.GONE);
			cft_choice_layout.setVisibility(View.VISIBLE);
			zfb_choice_layout.setVisibility(View.GONE);
		} else if (BANK_TYPE == type) {
			PAY_TYPE = BANK_TYPE;
			apply_after_zfb.setBackgroundResource(R.color.white);
			apply_after_zfb.setTextColor(getResources().getColor(R.color.black));
			apply_after_cft.setBackgroundResource(R.color.white);
			apply_after_cft.setTextColor(getResources().getColor(R.color.black));
			apply_after_yh.setBackgroundResource(R.color.blue_color);
			apply_after_yh.setTextColor(getResources().getColor(R.color.white));
			bank_choice_layout.setVisibility(View.VISIBLE);
			cft_choice_layout.setVisibility(View.GONE);
			zfb_choice_layout.setVisibility(View.GONE);
		}
	}

	/***
	 * 切换信息
	 * 
	 * @param type
	 */

	private void setInfoByChoice(int type) {
		if (GOODTYPE == type) {
			back_change_good.setCompoundDrawables(null, null, img_drawable, null);
			getback_money.setCompoundDrawables(null, null, null, null);
			apply_after_changegood_info.setVisibility(View.VISIBLE);
			apply_after_backmoney_info.setVisibility(View.GONE);
		} else {
			back_change_good.setCompoundDrawables(null, null, null, null);
			getback_money.setCompoundDrawables(null, null, img_drawable, null);
			apply_after_changegood_info.setVisibility(View.GONE);
			apply_after_backmoney_info.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 检验换货信息
	 * 
	 * @return
	 */
	private boolean checkChangeInfo() {
		receiver = after_receiver.getText().toString();
		phone = after_phone.getText().toString();
		addr = after_addr.getText().toString();
		reason = after_reason.getText().toString();
		if (TextUtils.isEmpty(receiver)) {
			showToast(R.string.contact_is_null);
			return false;
		} else if (TextUtils.isEmpty(phone)) {
			showToast(R.string.phone_is_null);
			return false;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			showToast(R.string.yuyue_phone_number_regx);
			return false;
		} else if (TextUtils.isEmpty(addr)) {
			showToast(R.string.addr_is_null);
			return false;
		} else if (TextUtils.isEmpty(reason)) {
			showToast(R.string.reason_is_null);
			return false;
		} else if (reason.length() > 300) {
			Toast.makeText(getBaseContext(), "原因字数不能多余300字", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	/**
	 * 检验退款信息
	 * 
	 * @return
	 */
	private boolean checkBackInfo() {
		reason = after_reason.getText().toString();
		// 银行
		bank = bank_ed.getText().toString();
		branch_bank = branch_bank_ed.getText().toString();
		username = bank_username_ed.getText().toString();
		bank_account = bank_account_ed.getText().toString();
		// 支付宝
		zfb_account_str = zfb_account.getText().toString();
		zfb_realname_str = zfb_realname.getText().toString();
		zfb_phone_number_str = zfb_phone_number.getText().toString();
		// 财付通
		cft_account_str = cft_account.getText().toString();
		cft_realname_str = cft_realname.getText().toString();
		cft_phone_number_str = cft_phone_number.getText().toString();
		if (PAY_TYPE == ZFB_TYPE) {
			if (TextUtils.isEmpty(zfb_account_str)) {
				showToast(R.string.account_is_null);
				return false;
			} else if (TextUtils.isEmpty(zfb_realname_str)) {
				showToast(R.string.name_is_null);
				return false;
			} else if (TextUtils.isEmpty(zfb_phone_number_str)) {
				showToast(R.string.phone_is_null);
				return false;
			} else if (!StringUtils.isPhoneNumber(zfb_phone_number_str)) {
				showToast(R.string.yuyue_phone_number_regx);
				return false;
			} else if (TextUtils.isEmpty(reason)) {
				showToast(R.string.reason_is_null);
				return false;
			}
		} else if (PAY_TYPE == CFT_TYPE) {
			if (TextUtils.isEmpty(cft_account_str)) {
				showToast(R.string.account_is_null);
				return false;
			} else if (TextUtils.isEmpty(cft_realname_str)) {
				showToast(R.string.name_is_null);
				return false;
			} else if (TextUtils.isEmpty(cft_phone_number_str)) {
				showToast(R.string.phone_is_null);
				return false;
			} else if (!StringUtils.isPhoneNumber(cft_phone_number_str)) {
				showToast(R.string.yuyue_phone_number_regx);
				return false;
			} else if (TextUtils.isEmpty(reason)) {
				showToast(R.string.reason_is_null);
				return false;
			}
		} else {
			if (TextUtils.isEmpty(bank)) {
				showToast(R.string.bank_is_null);
				return false;
			} else if (TextUtils.isEmpty(branch_bank)) {
				showToast(R.string.branch_is_null);
				return false;
			} else if (TextUtils.isEmpty(username)) {
				showToast(R.string.name_is_null);
				return false;
			} else if (TextUtils.isEmpty(bank_account)) {
				showToast(R.string.account_is_null);
				return false;
			} else if (TextUtils.isEmpty(reason)) {
				showToast(R.string.reason_is_null);
				return false;
			}
		}
		return true;
	}

	/***
	 * 退款
	 */

	private void postBackGoodInfo(String name, String account, String phone) {
		if (picUrl.endsWith(",")) {
			picUrl = picUrl.substring(0, picUrl.length() - 1);
		}
		RequstClient.backGood(orderId, PAY_TYPE + "", reason, bank, branch_bank, name, account, phone, AppContext.getInstance().getUserId(), "1",
				goodId, picUrl, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "backGood:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(getBaseContext(), "售后订单已提交审核中", Toast.LENGTH_SHORT).show();
							MyOrderActivity.setFlush(true);
							MyOrderActivity.setBackList(MyOrderActivity.second);
							finish();
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

	/**
	 * 换货
	 */
	private void postChangeGoodInfo() {
		if (picUrl.endsWith(",")) {
			picUrl = picUrl.substring(0, picUrl.length() - 1);
		}
		RequstClient.changeGood(AppContext.getInstance().getUserId(), orderId, goodId, reason, receiver, phone, picUrl, "1", addr,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "changeGood:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(getBaseContext(), "售后订单已提交审核中", Toast.LENGTH_SHORT).show();
							MyOrderActivity.setFlush(true);
							MyOrderActivity.setBackList(MyOrderActivity.second);
							finish();
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

	/***
	 * 上传图片
	 * 
	 * @param data
	 */
	// private void uploadGoodsImage(Intent data) {
	// Uri uri;
	// if (data.getData() != null) {
	// uri = data.getData();
	// try {
	// curImagePic =
	// MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// } else {
	// Bundle bundle = data.getExtras();
	// curImagePic = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
	// uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
	// curImagePic, null, null));
	// }
	// final String imageName = new
	// SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
	// final File savepath = new File(Config.APP_SAVE_PATH + "CacheImage/");
	// if (null == curImagePic) {
	// return;
	// }
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// UploadHelper.getInstance().doUpload(savepath.getAbsolutePath(),
	// imageName, curImagePic, "0",
	// new UploadCallBack() {
	// @Override
	// public void onStart(String filePath) {
	// Message msg = new Message();
	// msg.what = 1;
	// mHandler.sendMessage(msg);
	// }
	//
	// @Override
	// public void onFinish(String content) {
	// LogUtil.i(TAG, "uploadGoodsImage:" + content);
	// try {
	// JSONObject obj = new JSONObject(content);
	// String returnId = obj.getString("returnId");
	// returnId = TextUtils.isEmpty(returnId) ? "0" : returnId;
	// if (Integer.parseInt(returnId) > 0) {
	// final String imagePath = obj.getString("saveUrl");
	// picUrl += imagePath + ",";
	// if (null != curImagePic) {
	//
	// Message msg = new Message();
	// msg.what = 0;
	// mHandler.sendMessage(msg);
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onError(final String errorMsg) {
	// Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
	// }
	// });
	// }
	// }).start();
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bm;
		if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE && resultCode == RESULT_OK) {
			/* 选择图片来源 */
			String uploadType = data.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}
		} else if (resultCode == RESULT_OK && requestCode == CAMER_CODE) {
			bm = BitmapUtils.showimageFull(imageFile.getPath(), 50, 50);
			showimage(bm);
		} else if (resultCode == RESULT_OK && requestCode == LOCAL_CODE) {
			Uri selectedImage = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			bm = BitmapUtils.showimageFull(picturePath, 50, 50);
			showimage(bm, picturePath);
			// try {
			// bm = MediaStore.Images.Media.getBitmap(resolver, selectedImage);
			// bm = BitmapUtils.zoomBitmap(bm, 50, 50);
			// showimage(bm, picturePath);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	}

	private void showimage(Bitmap bm, String picturePath) {
		mUploadImageLayout.addPics(bm, picturePath);
	}

	private void showimage(Bitmap bm) {
		mUploadImageLayout.addPics(bm, imageFile.getPath());
	}

	private File imageFile;

	private void callCamerImage() {
		Log.i(TAG, "启动照相机");
		imageFile = ImageFolder.getTempImageName();
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(imageFile);
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		captureIntent.putExtra("return-data", true);
		startActivityForResult(captureIntent, CAMER_CODE);
	}

	private void callLocalImage() {
		Log.i(TAG, "打开图库");
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, LOCAL_CODE);
	}

	// private void callCamerImage() {
	// Log.i(TAG, "启动照相机");
	// Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// captureIntent.putExtra("return-data", true);
	// startActivityForResult(captureIntent, CAMER_CODE);
	// }
	//
	// private void callLocalImage() {
	// Log.i(TAG, "打开图库");
	// Intent localIntent = new Intent(Intent.ACTION_GET_CONTENT);
	// localIntent.addCategory(Intent.CATEGORY_OPENABLE);
	// localIntent.setType("image/*");
	// // 是否调用系统裁剪
	// localIntent.putExtra("crop", "true");
	// /* 图片宽高的像素值 */
	// localIntent.putExtra("outputX", 100);
	// localIntent.putExtra("outputY", 100);
	// localIntent.putExtra("return-data", true);
	// startActivityForResult(localIntent, LOCAL_CODE);
	// }
}
