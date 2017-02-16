package com.huiyin.ui.club;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.adapter.ClubGridViewAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.Club;
import com.huiyin.bean.Prize;
import com.huiyin.bean.PrizeRecord;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.view.LotteryView;
import com.huiyin.view.RotateListener;

public class ClubActivity extends Activity implements OnClickListener, RotateListener {

	public final static String INTENT_KEY_TITLE = "title";

	private ClubGridViewAdapter gridadapter;
	private GridView gridView;

	private TextView ab_title;

	private TextView tv_ad;
	private TextView tv_count;
	private Button btn_info;

	private TextView ab_back;

	private RelativeLayout lotteryView_layout;

	// 抽奖转盘
	private LotteryView lotteryView;
	// 指针按钮
	private ImageView arrowBtn;

	private Club club;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_club);

		initData();

		initViews();

		requestData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (lotteryView != null) {
			lotteryView.rotateEnable();
			lotteryView.start();
		}
	}

	@Override
	protected void onPause() {
		if (lotteryView != null) {
			lotteryView.rotateDisable();
			lotteryView.stopRotate();
		}
		super.onPause();
		// finish();
	}

	/**
	 * 
	 * Description:初始化转盘的颜色，文字
	 * 
	 */
	private void initData() {
		club = new Club();
	}

	private void initViews() {
		gridView = (GridView) findViewById(R.id.mGridView);

		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText(getIntent().getStringExtra(INTENT_KEY_TITLE));
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);

		arrowBtn = (ImageView) this.findViewById(R.id.arrowBtn);
		lotteryView_layout = (RelativeLayout) this.findViewById(R.id.lotteryView_layout);
		lotteryView_layout.setVisibility(View.GONE);
		lotteryView = (LotteryView) findViewById(R.id.lotteryView);

		arrowBtn.setOnClickListener(this);

		tv_ad = (TextView) findViewById(R.id.tv_ad);
		tv_count = (TextView) findViewById(R.id.tv_count);

		btn_info = (Button) findViewById(R.id.btn_info);
		btn_info.setOnClickListener(this);
	}

	private void setView() {

		Log.i("", club + "sadfasdfas");
		setCount();

		setPrizeView();

		lotteryView_layout.setVisibility(View.VISIBLE);

		lotteryView.initAll(club.getListPrizes());
		lotteryView.setRotateListener(this);
		lotteryView.start();

		// 参与人数
		if ((club.getQuantity() == null) || (club.getQuantity().length() == 0)) {
			club.setQuantity("0");
		}

		LayoutParams params = new LayoutParams(club.getQuantity().length() * (24 + 6), LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(26);
		gridView.setHorizontalSpacing(4);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(club.getQuantity().length());
		gridadapter = new ClubGridViewAdapter(this, club.getQuantity());
		gridView.setAdapter(gridadapter);

		initDate();
	}

	private boolean initDate() {
		if (club.isShut()) {
			// 关闭
			showDialog("活动已关闭", "感谢您的参与，活动已经关闭了！");
			return false;
		} else {
			if (compareDate(club.getCurrentTime(), club.getStartTime()) < 0) {
				// 活动未开始
				showDialog("活动未开始", "活动开始时间：" + club.getStartTime());
				return false;
			} else if (compareDate(club.getCurrentTime(), club.getStartTime()) >= 0
					&& compareDate(club.getCurrentTime(), club.getEndTime()) <= 0) {
				// 活动进行中
				return true;
			} else {
				showDialog("活动已经结束", "下次活动开始时间：" + club.getNextTime());
				return false;
			}
		}

	}

	private void setCount() {
		if (AppContext.getInstance().getUserId() != null && !AppContext.getInstance().getUserId().equals("")) {
			// 登陆了
			if (club.getUserNum() < club.getNum() && club.getNum() >= 0 && club.getUserNum() >= 0) {
				tv_count.setText(Html.fromHtml(String.format(getString(R.string.club_count), club.getNum() - club.getUserNum())));
			} else {
				if (club.getUserIntegral() >= club.getIntegral()) {
					tv_count.setText(Html.fromHtml("您的剩余积分为<u>" + club.getUserIntegral() + "</u>,每次抽奖需要扣除<u>"
							+ club.getIntegral() + "</u>,还可参加<u>" + club.getUserIntegral() / club.getIntegral()
							+ "</u>次抽奖，大奖等着你哟！"));
				} else {
					tv_count.setText(Html.fromHtml("您的剩余积分为<u>" + club.getUserIntegral() + "</u>,每次抽奖需要扣除<u>"
							+ club.getIntegral() + "</u>,您积分不足,赶紧赢取积分再来试试吧！"));
				}
			}
		} else {
			// 未登陆
			tv_count.setText(Html.fromHtml(String.format(getString(R.string.club_count_without_login), club.getNum())));
		}
	}

	/**
	 * 设置奖品滚动条
	 * 
	 * */
	private void setPrizeView() {
		if (club.getListPrizeRecords() != null && club.getListPrizeRecords().size() > 0) {
			tv_ad.setVisibility(View.VISIBLE);
			String temp = club.getListPrizeRecords().get(prize_count).getUserName();
			if (StringUtils.isMobileNO(temp)) {
				temp = temp.substring(0, 3) + "****" + temp.substring(7);
			}
			tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
					+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");

			handler.sendEmptyMessageDelayed(HANDLER_PRIZE, 5000);
		} else {
			tv_ad.setVisibility(View.INVISIBLE);
		}
	}

	/***
	 * 初始化抽奖数据
	 * 
	 * */
	private void requestData() {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				analyticalData(content);
			}
		};
		RequstClient.getClub(AppContext.getInstance().getUserId(), handler);
	}

	/***
	 * 解析初始化数据
	 * 
	 * */
	private void analyticalData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONObject integralClub = roots.getJSONObject("integralClub");
				club = new Club();
				club.setStartTime(integralClub.getString("STARTTIME"));
				club.setEndTime(integralClub.getString("ENDTIME"));
				club.setContent(integralClub.getString("CONTENT"));
				club.setId(integralClub.getString("ID"));
				club.setPrizeSet(integralClub.getString("WINNING"));
				club.setQuantity(integralClub.getString("QUANTITY"));// 参与人数
				club.setNextTime(integralClub.getString("TWO_START_TIME"));
				club.setCurrentTime(roots.getString("datetime"));

				if (roots.getString("userIntegral") != null && !roots.getString("userIntegral").equals("")) {
					club.setUserIntegral(Integer.valueOf(roots.getString("userIntegral")));
				}
				club.setIntegral(roots.getInt("integral"));

				if (roots.getString("userNum") != null && !roots.getString("userNum").equals("")) {
					club.setUserNum(Integer.valueOf(roots.getString("userNum")));
				}
				club.setNum(roots.getInt("num"));

				List<Prize> listPrizes = new ArrayList<Prize>();
				JSONArray luckProbabilityList = roots.getJSONArray("luckProbabilityList");
				for (int i = 0; i < luckProbabilityList.length(); i++) {
					JSONObject obj = luckProbabilityList.getJSONObject(i);
					Prize prize = new Prize();
					prize.setId(obj.getInt("ID"));
					prize.setName(obj.getString("PRIZENAME"));
					prize.setProbability(obj.getInt("PROBABILITY"));
					prize.setSpoil(obj.getInt("SPOIL"));
					listPrizes.add(prize);
				}
				club.setListPrizes(listPrizes);

				List<PrizeRecord> listPrizeRecords = new ArrayList<PrizeRecord>();
				JSONArray zhongJiangJiLu = roots.getJSONArray("zhongJiangJiLu");
				for (int i = 0; i < zhongJiangJiLu.length(); i++) {
					JSONObject obj = zhongJiangJiLu.getJSONObject(i);
					PrizeRecord prize = new PrizeRecord();
					prize.setUserName(obj.getString("USER_NAME"));
					prize.setPrizeName(obj.getString("PRIZE"));
					prize.setAddTime(obj.getString("ADDTIME"));
					listPrizeRecords.add(prize);
				}
				club.setListPrizeRecords(listPrizeRecords);
				if (roots.getInt("shut") < 0) {
					club.setShut(true);
				} else {
					club.setShut(false);
				}

				setView();
			} else {
				String errorMsg = roots.getString("msg");
				Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Description:转盘开始旋转
	 * 
	 * @param sp
	 * @param isRoating
	 */
	public void begin(float speed, int group, boolean isRoating) {
		lotteryView.setDirection(speed, group, isRoating);
		lotteryView.rotateEnable();
	}

	private final int HANDLER_PRIZE = 1;
	private final int HANDLER_PRIZE_RESULT = 2;
	private final int HANDLER_PRIZE_STOP = 3;

	private int prize_count = 0;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_PRIZE:
				if (club.getListPrizes() != null && club.getListPrizes().size() > 0) {
					prize_count++;
					String temp = club.getListPrizeRecords().get(prize_count).getUserName();
					if (StringUtils.isMobileNO(temp)) {
						temp = temp.substring(0, 3) + "****" + temp.substring(7);
					}
					if (prize_count < club.getListPrizeRecords().size() - 1) {
						tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
								+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");
					} else {
						prize_count = 0;
						tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
								+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");
					}
					handler.sendEmptyMessageDelayed(HANDLER_PRIZE, 5000);
				}
				break;
			case HANDLER_PRIZE_RESULT:
				if (!lotteryView.isRotateEnabled()) {
					if (AppContext.getInstance().getUserId() != null && !AppContext.getInstance().getUserId().equals("")) {
						if (isSuccess) {
							setCount();
							if ((int) msg.arg1 == 2) {
								// 非奖品
								showPrizeDialog("很遗憾", "谢谢参与！");
							} else {
								showPrizeDialog("恭喜您", "恭喜您获得" + (CharSequence) msg.obj);
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), "您还未登录", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case HANDLER_PRIZE_STOP:
				// 一直旋转状态
				if (!lotteryView.isRoating()) {
					// 设置中奖项(随机的话请注释)
					if (msg.arg2 == 1) {
						lotteryView.setAwards(msg.arg1, true);
					} else {
						// 抽奖失败
						if (msg.arg1 == -1) {
							showDialog("温馨提示", "活动已经结束");
						} else {
							lotteryView.setAwards(msg.arg1, false);
							Toast.makeText(getApplicationContext(), "获取奖品失败", Toast.LENGTH_LONG).show();
						}
					}
					// 设置为缓慢停止
					lotteryView.setRoating(true);
				}
				break;
			default:
				break;
			}
		};
	};

	/***
	 * 提交抽奖
	 * */
	private void submitData() {
		CustomResponseHandler handler = new CustomResponseHandler(this, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				getSubmitData(content);
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				getDataFail();
			}
		};
		RequstClient.submitClub(AppContext.getInstance().getUserId(), club.getId(), handler);
	}

	/**
	 * 获取抽奖结果
	 * 
	 * */
	private void getSubmitData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				int integral = roots.getInt("integral");
				int sumNum = roots.getInt("sumNum");
				int index = roots.getInt("num");

				AppContext.getInstance().getUserInfo().integral = integral + "";

				UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
				UserinfoPreferenceUtil.saveUserInfo(ClubActivity.this, mUserInfo);

				isSuccess = true;
				club.setUserNum(sumNum);
				club.setUserIntegral(integral);

				if (index == -1) {
					// 活动已结束
					Message message = new Message();
					message.what = HANDLER_PRIZE_STOP;
					message.arg1 = index;
					message.arg2 = 0;//
					handler.sendMessageDelayed(message, 3000);
				} else {
					Message message = new Message();
					message.what = HANDLER_PRIZE_STOP;
					message.arg1 = index;
					message.arg2 = 1;//
					handler.sendMessageDelayed(message, 3000);
				}

			} else {
				getDataFail();
			}
		} catch (JSONException e) {
			// Json解析失败
			getDataFail();
		}
	}

	private void getDataFail() {
		Message message = new Message();
		message.what = HANDLER_PRIZE_STOP;
		message.arg1 = 0;
		message.arg2 = 0;// 失败
		message.obj = "获取奖品失败";
		handler.sendMessageDelayed(message, 3000);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == ab_back) {
			finish();
		} else if (arg0 == arrowBtn) {
			if (initDate()) {
				if (!lotteryView.isRotateEnabled()) {
					isSuccess = false;
					// 没有旋转状态
					if (AppContext.getInstance().getUserId() != null && !AppContext.getInstance().getUserId().equals("")) {
						// 已经登陆
						if (club.getNum() >= 0 && club.getUserNum() >= 0 && club.getUserNum() < club.getNum()) {
							// 当前用户免费抽奖次数小于每天免费的抽奖次数
							// 提交抽奖
							begin(Math.abs(20), 10, false);

							isSuccess = false;

							submitData();
						} else {
							// 积分抽奖
							if (club.getUserIntegral() >= club.getIntegral()) {
								// 当前用户积分大于每次抽奖的积分，积分抽奖
								begin(Math.abs(20), 10, false);

								submitData();
							} else {
								// 无积分，无免费
								showDialog("温馨提示", "亲，您免费机会已经用完，赶紧去购物赢取积分吧！");
							}
						}
					} else {
						// 未登陆
						begin(Math.abs(20), 10, false);

						Message message = new Message();
						message.what = HANDLER_PRIZE_STOP;
						message.arg1 = (int) (Math.random() * club.getListPrizeRecords().size());
						message.arg2 = 1;
						handler.sendMessageDelayed(message, 3000);

					}

				}
			}
		} else if (arg0 == btn_info) {
			Intent intent = new Intent(this, ClubAboutActivity.class);
			if (club.getContent() == null) {
				club.setContent("");
			}

			if (club.getPrizeSet() == null) {
				club.setPrizeSet("");
			}
			intent.putExtra(ClubAboutActivity.INTENT_KEY_CONTENT, club.getContent());
			intent.putExtra(ClubAboutActivity.INTENT_LEY_ABOUT, club.getPrizeSet());
			intent.putExtra(ClubActivity.INTENT_KEY_TITLE, getIntent().getStringExtra(INTENT_KEY_TITLE));
			startActivity(intent);
			finish();
		}
	}

	private void showDialog(String title, String msg) {
		ConfirmDialog dialog = new ConfirmDialog(this);
		dialog.setCustomTitle(title);
		dialog.setMessage(msg);
		dialog.setConfirm("去逛逛");
		dialog.setCancel("再看看");
		dialog.setClickListener(new DialogClickListener() {

			@Override
			public void onConfirmClickListener() {
				finish();
			}

			@Override
			public void onCancelClickListener() {

			}
		});
		dialog.show();
	}

	private void showPrizeDialog(String title, String msg) {
		isSuccess = false;
		SingleConfirmDialog dialog = new SingleConfirmDialog(this);
		dialog.setCustomTitle(title);
		dialog.setMessage(msg);
		dialog.setConfirm("确定");
		dialog.show();
	}

	@Override
	public void showEndRotate(Prize prize) {
		// 返回当前所值的奖品

		Message msg = new Message();
		msg.what = HANDLER_PRIZE_RESULT;
		msg.obj = prize.getName();
		msg.arg1 = prize.getSpoil();
		handler.sendMessage(msg);

	}

	private boolean isSuccess;

	@SuppressLint("SimpleDateFormat")
	public int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

}
