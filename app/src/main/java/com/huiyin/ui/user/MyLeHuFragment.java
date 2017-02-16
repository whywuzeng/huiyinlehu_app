package com.huiyin.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.BrowseItem;
import com.huiyin.constants.Config;
import com.huiyin.db.ScanRecordDao;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.PageSelectedListener;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.RechargeServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.UploadHelper;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.utils.UploadHelper.UploadCallBack;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyLeHuFragment extends Fragment implements PageSelectedListener {

	public static final String TAG = "MyLeHuFragment";
	Button lehu_login_id, lehu_register_id;
	// 全部订单、我的乐虎劵、收藏、系统消息、安装预约、账户管理、用户中心条目
	RelativeLayout all_order_layout, my_lhj_layout, my_collect_layout, message_layout, yuyue_layout, account_manage_layout,
			user_center_layout, lehu_fuwu_layout, lehu_my_bespeak;

	View rootView;
	// 头像、设置
	CircleImageView login_head_iv;
	ImageView user_set_iv;
	// 手机号、等级、积分、最后登录
	TextView user_phone_tv, user_level_tv, user_jifen_tv, lastest_login_tv;
	// 待支付数、待收货数、待评价数、退换货中数
	TextView waitpay_num_tv, waitreceive_num_tv, waitcomment_num_tv, backgood_num_tv;
	// 申请预约数、即将过期乐虎劵数、总的乐虎劵数、系统消息数
	TextView sq_num_tv, lehuquan_past, lehuquan_total_tv, lehu_msg_tv, lehu_fuwu_info;
	// 未登录部分
	RelativeLayout unlogin_part_layout;
	// 登录部分
	LinearLayout login_part1_layout, last_login_layout;
	// 跳转到订单列表的四个部分(待支付、待收货、待评价、退换货中)及查看明细
	TextView waitpay_orderlist, waitreceive_orderlist, waitcomment_orderlist, lehu_user_jifen_detail, backgood_orderlist;

	// 浏览记录部分
	ViewPager browse_content_pager;
	private LayoutInflater myInflater;
	private static int request_code = 0x000012;
	UserInfo mUserInfo;

	// 浏览记录的存储数据库
	ScanRecordDao mScanRecordDao;
	ArrayList<BrowseItem> browsingHistoryList;
	// private String imagePath = "";

	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;
	private File imageFile;
	private ImageUpload mIUpload;

	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 0) {
	// UIHelper.showLoadDialog(getActivity(),
	// getActivity().getString(R.string.loading));
	// } else {
	// DisplayImageOptions options = new
	// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
	// .showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
	// .showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	// .bitmapConfig(Bitmap.Config.RGB_565).displayer(new
	// RoundedBitmapDisplayer(100)).build();
	// ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + imagePath,
	// login_head_iv, options);
	//
	// if (AppContext.mUserInfo != null) {
	// AppContext.mUserInfo.img = imagePath;
	// }
	// UIHelper.cloesLoadDialog();
	// }
	// }
	//
	// };

	// Bitmap curImagePic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.my_lehu_layout, null);
		initView();
		initData();

		return rootView;

	}

	private void initData() {

		String reservationType = String.format(getString(R.string.shenqin_commit), "0");
		sq_num_tv.setText(reservationType);
		String myCouponOut = String.format(getString(R.string.lehujuan_past), "0");

		lehuquan_past.setText(myCouponOut);
		String myCouponAll = String.format(getString(R.string.lehujuan_total), "0");
		lehuquan_total_tv.setText(myCouponAll);
		String systemMessage = String.format(getString(R.string.system_msg), "0");
		lehu_msg_tv.setText(systemMessage);
		waitpay_num_tv.setVisibility(View.GONE);
		waitreceive_num_tv.setVisibility(View.GONE);
		waitcomment_num_tv.setVisibility(View.GONE);
		backgood_num_tv.setVisibility(View.GONE);
		// 浏览记录
		initViewPage();

		if (AppContext.getInstance().getUserInfo() != null) {
			unlogin_part_layout.setVisibility(View.GONE);
			login_part1_layout.setVisibility(View.VISIBLE);
			last_login_layout.setVisibility(View.VISIBLE);
			refreshScreen();
		}

	}

	private void initView() {

		unlogin_part_layout = (RelativeLayout) rootView.findViewById(R.id.unlogin_part_layout);
		login_part1_layout = (LinearLayout) rootView.findViewById(R.id.login_part1_layout);
		last_login_layout = (LinearLayout) rootView.findViewById(R.id.last_login_layout);

		/**
		 * 需要更新的控件begin
		 */
		login_head_iv = (CircleImageView) rootView.findViewById(R.id.lehu_login_head);
		waitpay_num_tv = (TextView) rootView.findViewById(R.id.waitpay_ordernum);
		waitreceive_num_tv = (TextView) rootView.findViewById(R.id.waitreceive_ordernum);
		waitcomment_num_tv = (TextView) rootView.findViewById(R.id.waitcomment_ordernum);
		backgood_num_tv = (TextView) rootView.findViewById(R.id.backgood_ordernum);

		sq_num_tv = (TextView) rootView.findViewById(R.id.lehu_sq_num);
		lehuquan_past = (TextView) rootView.findViewById(R.id.lehuquan_past);
		lehuquan_total_tv = (TextView) rootView.findViewById(R.id.lehuquan_total_tv);
		lehu_fuwu_info = (TextView) rootView.findViewById(R.id.lehu_fuwu_info);
		lehu_msg_tv = (TextView) rootView.findViewById(R.id.lehu_msg_tv);

		user_phone_tv = (TextView) rootView.findViewById(R.id.lehu_user_phone);
		user_level_tv = (TextView) rootView.findViewById(R.id.lehu_user_level);
		user_jifen_tv = (TextView) rootView.findViewById(R.id.lehu_user_jifen);
		lastest_login_tv = (TextView) rootView.findViewById(R.id.lehu_user_lastest_login);

		/**
		 * 需要更新的控件end
		 */

		login_head_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), CommonConfrimCancelDialog.class);
				i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
				startActivityForResult(i, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
			}
		});

		lehu_user_jifen_detail = (TextView) rootView.findViewById(R.id.lehu_user_jifen_detail);
		lehu_user_jifen_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		lehu_login_id = (Button) rootView.findViewById(R.id.lehu_login_id);
		lehu_register_id = (Button) rootView.findViewById(R.id.lehu_register_id);
		lehu_login_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(getActivity(), LoginActivity.class);
				startActivityForResult(i, request_code);
			}

		});
		lehu_register_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(getActivity(), RegisterActivity.class);
				startActivityForResult(i, request_code);
			}
		});

		all_order_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_all_order);
		my_lhj_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_my_lhj);
		my_collect_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_my_collect);
		message_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_message);
		yuyue_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_yuyue);
		account_manage_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_account_manage);
		user_center_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_user_center);
		lehu_fuwu_layout = (RelativeLayout) rootView.findViewById(R.id.lehu_fuwu);
		lehu_my_bespeak = (RelativeLayout) rootView.findViewById(R.id.lehu_my_bespeak);
		user_set_iv = (ImageView) rootView.findViewById(R.id.user_setting);

		MyOnClickListener onclick = new MyOnClickListener();
		MyOnClick click = new MyOnClick();

		user_center_layout.setOnClickListener(click);
		user_set_iv.setOnClickListener(click);

		all_order_layout.setOnClickListener(onclick);
		my_lhj_layout.setOnClickListener(onclick);
		my_collect_layout.setOnClickListener(onclick);
		message_layout.setOnClickListener(onclick);
		yuyue_layout.setOnClickListener(onclick);
		lehu_user_jifen_detail.setOnClickListener(onclick);
		lehu_fuwu_layout.setOnClickListener(onclick);
		lehu_my_bespeak.setOnClickListener(onclick);

		waitpay_orderlist = (TextView) rootView.findViewById(R.id.waitpay_orderlist);
		waitreceive_orderlist = (TextView) rootView.findViewById(R.id.waitreceive_orderlist);
		waitcomment_orderlist = (TextView) rootView.findViewById(R.id.waitcomment_orderlist);
		backgood_orderlist = (TextView) rootView.findViewById(R.id.backgood_orderlist);

		waitpay_orderlist.setOnClickListener(onclick);
		waitreceive_orderlist.setOnClickListener(onclick);
		waitcomment_orderlist.setOnClickListener(onclick);
		backgood_orderlist.setOnClickListener(onclick);
		account_manage_layout.setOnClickListener(onclick);

		browse_content_pager = (ViewPager) rootView.findViewById(R.id.browse_content_pager);
		myInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mScanRecordDao = new ScanRecordDao();

	}

	/**
	 * 数据请求后刷新界面
	 */
	public void refreshScreen() {

		if (AppContext.getInstance().getUserInfo() == null) {
			return;
		}
		mUserInfo = AppContext.getInstance().getUserInfo();
		if (mUserInfo.img != null) {
//			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
//					.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
//					.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//					.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
			ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + mUserInfo.img, login_head_iv);

		} else {
			login_head_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_head));
		}
		if (mUserInfo.countWaitPay.equals("") || mUserInfo.countWaitPay.equals("0")) {
			waitpay_num_tv.setVisibility(View.GONE);
		} else {
			waitpay_num_tv.setText(mUserInfo.countWaitPay);
			waitpay_num_tv.setVisibility(View.VISIBLE);
		}

		if (mUserInfo.countDelivery.equals("") || mUserInfo.countDelivery.equals("0")) {
			waitreceive_num_tv.setVisibility(View.GONE);
		} else {
			if (MathUtil.stringToInt(mUserInfo.countDelivery) > 99) {
				waitreceive_num_tv.setText("99+");
			} else {
				waitreceive_num_tv.setText(mUserInfo.countDelivery);
			}
			waitreceive_num_tv.setVisibility(View.VISIBLE);
		}

		if (mUserInfo.commentsStatus.equals("") || mUserInfo.commentsStatus.equals("0")) {
			waitcomment_num_tv.setVisibility(View.GONE);
		} else {
			if (MathUtil.stringToInt(mUserInfo.commentsStatus) > 99) {
				waitcomment_num_tv.setText("99+");
			} else {
				waitcomment_num_tv.setText(mUserInfo.commentsStatus);
			}
			waitcomment_num_tv.setVisibility(View.VISIBLE);
		}

		if (mUserInfo.countChangeReplace.equals("") || mUserInfo.countChangeReplace.equals("0")) {
			backgood_num_tv.setVisibility(View.GONE);
		} else {
			if (MathUtil.stringToInt(mUserInfo.countChangeReplace) > 99) {
				backgood_num_tv.setText("99+");
			} else {
				backgood_num_tv.setText(mUserInfo.countChangeReplace);
			}
			backgood_num_tv.setVisibility(View.VISIBLE);
		}

		String reservationType = String.format(getString(R.string.shenqin_commit), mUserInfo.countReservationType);
		sq_num_tv.setText(reservationType);
		String myCouponOut = String.format(getString(R.string.lehujuan_past), mUserInfo.myCouponOut);
		lehuquan_past.setText(myCouponOut);
		String myCouponAll = String.format(getString(R.string.lehujuan_total), mUserInfo.myCouponAll);
		lehuquan_total_tv.setText(myCouponAll);
		String systemMessage = String.format(getString(R.string.system_msg), mUserInfo.systemMessage);
		lehu_msg_tv.setText(systemMessage);

		if (mUserInfo.bdStatus == 1) {
			lehu_fuwu_info.setText(Html.fromHtml("<u>充值</u>"));
			lehu_fuwu_info.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent fuwu_intent = new Intent();
					fuwu_intent.setClass(getActivity(), RechargeServiceCard.class);
					getActivity().startActivity(fuwu_intent);
				}
			});
		} else {
			lehu_fuwu_info.setText("您还未绑定");
			lehu_fuwu_info.setOnClickListener(null);
		}

		user_phone_tv.setText(mUserInfo.userName);
		user_level_tv.setText(mUserInfo.level);
		user_jifen_tv.setText(mUserInfo.integral);
		lastest_login_tv.setText(mUserInfo.lastDate);
	}

	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			if (mScanRecordDao != null && browse_content_pager != null)
				initViewPage();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * 浏览记录
	 */
	private void initViewPage() {
		browsingHistoryList = mScanRecordDao.fetcheAll();

		List<BrowseItem> listRecords = new ArrayList<BrowseItem>();
		List<Fragment> listFragments = new ArrayList<Fragment>();
		for (int i = 0; i < browsingHistoryList.size(); i++) {
			if (listRecords.size() < 8) {
				listRecords.add(browsingHistoryList.get(i));
			} else {
				BrowseRecordFragment fragment = new BrowseRecordFragment();

				List<BrowseItem> listTemps = new ArrayList<BrowseItem>();
				listTemps.addAll(listRecords);
				fragment.setListDatas(listTemps);
				listFragments.add(fragment);
				listRecords = new ArrayList<BrowseItem>();
				listRecords.add(browsingHistoryList.get(i));
			}
		}

		if (listRecords.size() > 0) {
			BrowseRecordFragment fragment = new BrowseRecordFragment();
			List<BrowseItem> listTemps = new ArrayList<BrowseItem>();
			listTemps.addAll(listRecords);
			fragment.setListDatas(listTemps);
			listFragments.add(fragment);
		}
		FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getFragmentManager(), listFragments);
		browse_content_pager.setAdapter(adapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_code && resultCode == Activity.RESULT_OK) {

			unlogin_part_layout.setVisibility(View.GONE);
			login_part1_layout.setVisibility(View.VISIBLE);
			last_login_layout.setVisibility(View.VISIBLE);

			refreshScreen();

		} else if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE && resultCode == Activity.RESULT_OK) {
			/* 选择图片来源 */
			String uploadType = data.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}

		} else if (resultCode == Activity.RESULT_OK && requestCode == CAMER_CODE) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(imageFile), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 100);
			intent.putExtra("outputY", 100);
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("scale", true);
			imageFile = ImageFolder.getTempImageName();
			intent.putExtra("output", Uri.fromFile(imageFile));
			intent.putExtra("outputFormat", "JPEG");// 返回格式
			startActivityForResult(intent, LOCAL_CODE);
		} else if (resultCode == Activity.RESULT_OK && requestCode == LOCAL_CODE) {
			Log.i(TAG, "获取图片返回成功");
			// uploadGoodsImage(data);
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(imageFile.getPath());
			mIUpload = new ImageUpload(getActivity(), temp, mUpImageListener);
			mIUpload.upLoadHeadImage();
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppContext.getInstance().getUserInfo() != null) {
			if (login_part1_layout.getVisibility() == View.GONE) {
				unlogin_part_layout.setVisibility(View.GONE);
				login_part1_layout.setVisibility(View.VISIBLE);
				last_login_layout.setVisibility(View.VISIBLE);
			}
			RequstClient.doMyLH(new CustomResponseHandler(getActivity()) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {

					super.onSuccess(statusCode, headers, content);
					try {
						JSONObject obj = new JSONObject(content);
						if (!obj.getString("type").equals("1")) {
							String errorMsg = obj.getString("msg");
							Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
							return;
						}

						UserInfo mUserInfo = new Gson().fromJson(new JSONObject(content).getJSONObject("list").toString(),
								UserInfo.class);
//						AppContext.mUserInfo = mUserInfo;

						UserinfoPreferenceUtil.saveUserInfo(getActivity(), mUserInfo);
						refreshScreen();

					} catch (JsonSyntaxException e) {

						e.printStackTrace();
					} catch (JSONException e) {

						e.printStackTrace();
					} catch (Exception e) {

						e.printStackTrace();
					}
				}

			});
		} else {
			if (login_part1_layout.getVisibility() == View.VISIBLE) {
				unlogin_part_layout.setVisibility(View.VISIBLE);
				login_part1_layout.setVisibility(View.GONE);
				last_login_layout.setVisibility(View.GONE);
				initData();
			}
		}

		// 浏览记录
		if (AppContext.hasNewScanRecord) {
			initViewPage();
			AppContext.hasNewScanRecord = false;
		}
	}

	class MyOnClick implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.lehu_account_manage:
				Intent am_intent = new Intent();
				am_intent.setClass(getActivity(), AccountManageActivity.class);
				getActivity().startActivity(am_intent);
				break;
			case R.id.lehu_user_center:
				Intent kf_intent = new Intent();
				kf_intent.setClass(getActivity(), KefuCenterActivity.class);
				getActivity().startActivity(kf_intent);
				break;

			case R.id.user_setting:
				Intent set_i = new Intent();
				set_i.setClass(getActivity(), SettingActivity.class);
				startActivity(set_i);
				break;
			}
		}
	};

	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			if (AppContext.getInstance().getUserId() == null) {
				Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG).show();
				Intent i = new Intent();
				i.setClass(getActivity(), LoginActivity.class);
				startActivityForResult(i, request_code);
				return;
			}
			switch (arg0.getId()) {
			case R.id.lehu_all_order:
				// 全部订单
				Intent intent = new Intent();
				intent.setClass(getActivity(), MyOrderActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.lehu_my_lhj:
				// 乐虎劵
				Intent i = new Intent();
				i.putExtra("myCouponOut", mUserInfo.myCouponOut);
				i.setClass(getActivity(), LeHuJuanActivity.class);
				getActivity().startActivity(i);
				break;
			case R.id.lehu_my_bespeak:
				// 我的预约
				Intent bespeak_intent = new Intent();
				bespeak_intent.setClass(getActivity(), MyBespeakActivity.class);
				getActivity().startActivity(bespeak_intent);
				break;
			case R.id.lehu_my_collect:
				// 我的收藏
				Intent c_intent = new Intent();
				c_intent.setClass(getActivity(), CollectActivity.class);
				getActivity().startActivity(c_intent);
				break;
			case R.id.lehu_message:
				// 系统消息
				Intent m_intent = new Intent();
				m_intent.setClass(getActivity(), MyMessageActivity.class);
				getActivity().startActivity(m_intent);
				break;
			case R.id.lehu_yuyue:
				// 安装维修预约
				Intent yy_intent = new Intent();
				yy_intent.setClass(getActivity(), YuYueShenQingActivity.class);
				getActivity().startActivity(yy_intent);
				break;
			case R.id.lehu_fuwu:
				// 安装维修预约
				Intent fuwu_intent = new Intent();
				if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
					fuwu_intent.setClass(getActivity(), ServiceCardActivity.class);
				} else {
					fuwu_intent.setClass(getActivity(), BindServiceCard.class);
				}
				getActivity().startActivity(fuwu_intent);
				break;
			case R.id.lehu_account_manage:
				// 账户管理
				Intent am_intent = new Intent();
				am_intent.setClass(getActivity(), AccountManageActivity.class);
				startActivity(am_intent);
				break;
			case R.id.lehu_user_center:
				// 客户中心
				Intent kf_intent = new Intent();
				kf_intent.setClass(getActivity(), KefuCenterActivity.class);
				getActivity().startActivity(kf_intent);
				break;
			case R.id.lehu_user_jifen_detail:
				Intent jf_intent = new Intent();
				jf_intent.setClass(getActivity(), MyJiFenActivity.class);
				getActivity().startActivity(jf_intent);
				break;
			case R.id.user_setting:
				Intent set_i = new Intent();
				set_i.setClass(getActivity(), SettingActivity.class);
				startActivity(set_i);
				break;
			case R.id.waitpay_orderlist:
				Intent wp_i = new Intent();
				wp_i.setClass(getActivity(), MyOrderActivity.class);
				wp_i.putExtra(MyOrderActivity.TAG, MyOrderActivity.WAITPAY);
				startActivity(wp_i);
				break;
			case R.id.waitreceive_orderlist:
				Intent wr_i = new Intent();
				wr_i.setClass(getActivity(), MyOrderActivity.class);
				wr_i.putExtra(MyOrderActivity.TAG, MyOrderActivity.WAITRECEIVE);
				startActivity(wr_i);
				break;
			case R.id.waitcomment_orderlist:
				Intent wo_i = new Intent();
				wo_i.setClass(getActivity(), MyOrderActivity.class);
				wo_i.putExtra(MyOrderActivity.TAG, MyOrderActivity.WAITCOMMENT);
				startActivity(wo_i);
				break;
			case R.id.backgood_orderlist:
				Intent bg_i = new Intent();
				bg_i.setClass(getActivity(), MyOrderActivity.class);
				bg_i.putExtra(MyOrderActivity.TAG, MyOrderActivity.BACKCHANGE);
				startActivity(bg_i);
				break;

			}
		}
	};

	/***
	 * 上传图片
	 * 
	 * @param data
	 */
	// private void uploadGoodsImage(Intent data) {
	//
	// Uri uri;
	// if (data.getData() != null) {
	// uri = data.getData();
	// try {
	// curImagePic =
	// MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
	// uri);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// } else {
	// Bundle bundle = data.getExtras();
	// curImagePic = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
	// uri =
	// Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
	// curImagePic, null, null));
	// }
	// final String imageName = new
	// SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
	// final File savepath = new File(Config.APP_SAVE_PATH + "CacheImage/");
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// UploadHelper.getInstance().doUpload(savepath.getAbsolutePath(),
	// imageName, curImagePic, "1",
	// new UploadCallBack() {
	// @Override
	// public void onStart(String filePath) {
	// Message msg = new Message();
	// msg.what = 0;
	// mHandler.sendMessage(msg);
	// }
	//
	// @Override
	// public void onFinish(String content) {
	// LogUtil.i(TAG, "uploadGoodsImage:" + content);
	// try {
	// final JSONObject obj = new JSONObject(content);
	// String returnId = obj.getString("returnId");
	// returnId = TextUtils.isEmpty(returnId) ? "0" : returnId;
	// if (Integer.parseInt(returnId) > 0) {
	// imagePath = obj.getString("saveUrl");
	// if (null != curImagePic) {
	// Message msg = new Message();
	// msg.what = 1;
	// mHandler.sendMessage(msg);
	// }
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onError(final String errorMsg) {
	// }
	// });
	// }
	//
	// }).start();
	//
	// }

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
		// Intent localIntent = new Intent(Intent.ACTION_GET_CONTENT);
		// localIntent.addCategory(Intent.CATEGORY_OPENABLE);
		// localIntent.setType("image/*");
		// // 是否调用系统裁剪
		// localIntent.putExtra("crop", "true");
		//
		// /* 图片宽高的像素值 */
		// localIntent.putExtra("outputX", 100);
		// localIntent.putExtra("outputY", 100);
		// localIntent.putExtra("return-data", true);
		// startActivityForResult(localIntent, LOCAL_CODE);
		imageFile = ImageFolder.getTempImageName();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true"); // 开启剪切
		intent.putExtra("aspectX", 1); // 剪切的宽高比为1：1
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100); // 保存图片的宽和高
		intent.putExtra("outputY", 100);
		intent.putExtra("output", Uri.fromFile(imageFile)); // 保存路径
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(intent, LOCAL_CODE);
	}

	private UpLoadImageListener mUpImageListener = new UpLoadImageListener() {

		@Override
		public void UpLoadSuccess(ArrayList<String> netimageurls) {
			if (netimageurls == null || netimageurls.size() == 0)
				return;
			if (AppContext.getInstance().getUserInfo() != null) {
				AppContext.getInstance().getUserInfo().img = netimageurls.get(0);
				
				UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
				UserinfoPreferenceUtil.saveUserInfo(getActivity(), mUserInfo);
			}
//			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
//					.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
//					.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//					.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
			ImageManager.LoadWithServer(netimageurls.get(0), login_head_iv);
		}

		@Override
		public void UpLoadFail() {

		}
	};

	@Override
	public void pageSelected(int selectedId) {
		
		if (selectedId == MainActivity.MY_LEHU_INDEX) {
			onResume();
		}
	}
}
