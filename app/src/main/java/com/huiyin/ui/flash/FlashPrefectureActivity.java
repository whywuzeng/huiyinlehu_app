package com.huiyin.ui.flash;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.flash.FlashPrefectureBean.ProduceList;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.huiyin.wight.MyGridView;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshScrollView;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.Mode;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

@SuppressWarnings("rawtypes")
public class FlashPrefectureActivity extends BaseActivity implements OnRefreshListener2 {

	private PullToRefreshScrollView scrollview;
	private MyGridView gv;
	private TextView ab_title, ab_back;
	private ImageView bannerImage;
	private TextView timeCount;

	private CountDownTimer mCountDownTimer;

	private FlashPrefectureBean data;
	private FlashGridAdapter mAdapter;
	/** 当前页 */
	private int mPageindex;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_prefecture_layout);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		findView();
		setListener();
		InitData();
	}

	private void findView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		timeCount = (TextView) findViewById(R.id.textView_count);

		scrollview = (PullToRefreshScrollView) findViewById(R.id.zhuan_qu_body_scrollview);
		bannerImage = (ImageView) findViewById(R.id.zhuanqu_barner_img);
		gv = (MyGridView) findViewById(R.id.zhuan_qu_body_gv);
		// mAdapter = new FlashGridAdapter(mContext);
		// gv.setAdapter(mAdapter);
		// 设定上下拉刷新
		scrollview.setMode(Mode.BOTH);
		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		scrollview.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		scrollview.setOnRefreshListener(this);
	}

	private void setListener() {
		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int ID = mAdapter.getID(position);
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, String.valueOf(ID));
				startActivity(intent);
			}
		});
	}

	private void InitData() {
		loadPageData(1);
	}

	private void loadPageData(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		CustomResponseHandler handler = new CustomResponseHandler(this, false) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = FlashPrefectureBean.explainJson(content, mContext);
				if (data.type > 0 && data.getProduceList() != null && data.getProduceList().size() > 0) {
					updataView();
					if (mPageindex == 1) {
						mAdapter = new FlashGridAdapter(mContext, data.getProduceList());
						gv.setAdapter(mAdapter);
						// mAdapter.deleteItem();
					} else {
						mAdapter.addItem(data.getProduceList());
					}
					gv.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(mContext, "已无更多商品！", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				scrollview.onRefreshComplete();
			}

		};
		RequstClient.flashSaleRegion(mPageindex, id, handler);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		loadPageData(1);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		loadPageData(2);
	}

	private void updataView() {
		ProduceList temp = data.getProduceList().get(0);
		if (!StringUtils.isBlank(temp.getBANAER())) {
			ImageManager.LoadWithServer(temp.getBANAER(), bannerImage);
		}
		if (!StringUtils.isBlank(temp.getREGIONTITLE())) {
			ab_title.setText(temp.getREGIONTITLE());
		}
		if (!StringUtils.isBlank(temp.getSTARTTIME()) && !StringUtils.isBlank(temp.getENDTIME())
				&& !StringUtils.isBlank(data.getCurDate())) {
			TextTask(temp.getSTARTTIME(), temp.getENDTIME(), data.getCurDate());
		}
	}

	private long nowSecond = 0;

	protected void TextTask(final String starttime, final String endtime, final String curtime) {
		Date start = StringUtils.toDate(starttime);
		Date end = StringUtils.toDate(endtime);
		Date nowTime = StringUtils.toDate(curtime);

		long startSecond = start.getTime();
		long endSecond = end.getTime();
		if (nowSecond == 0)
			nowSecond = nowTime.getTime();

		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
		if (nowSecond < startSecond) {
			mCountDownTimer = new CountDownTimer(startSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					String temp = hour + "时" + minute + "分" + second + "秒";
					timeCount.setText("即将开始" + temp);
					nowSecond += 1000;
				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond < endSecond) {
			mCountDownTimer = new CountDownTimer(endSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					String temp = hour + "时" + minute + "分" + second + "秒";
					timeCount.setText("还剩" + temp);
					nowSecond += 1000;
				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond >= endSecond) {
			timeCount.setText("活动结束");
		}

	}
}
