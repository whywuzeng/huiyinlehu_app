package com.huiyin.ui.seckill;

import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.home.SiteSearchActivity;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.ui.seckill.SeckillTitleBean.SeckillTitle;
import com.huiyin.ui.shoppingcar.DatePickPopMenu;
import com.huiyin.utils.ListViewUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.DateTimePicker;
import com.huiyin.wight.StickyScrollView;
import com.huiyin.wight.StickyScrollView.OnBorderListener;

public class SeckillActivity extends BaseActivity {

	// Content View Elements

	private TextView ab_back;
	private TextView ab_title;
	private Button ab_right;
	private StickyScrollView scrollView;
	private LinearLayout ab_search;
	private ImageView ab_fenlei;
	private ImageView activity_index_sousuo_iv;
	private TextView activity_index_search_content;
	private RadioGroup service_card_layout_RadioGroup;
	private RadioButton first_RadioButton;
	private RadioButton second_RadioButton;
	private RadioButton third_RadioButton;
	private RadioButton four_RadioButton;
	private TextView seckill_status;
	private TextView seckill_info;
	private LinearLayout time_count_layout;
	private TextView hourTextView;
	private TextView minuteTextView;
	private TextView secondTextView;
	private ListView seckill_listview;
	private TextView shownext;

	// End Of Content View Elements

	private SeckillTitleBean titleBean;
	private SeckillListBean listBean;
	private Boolean status;
	private int type;
	/** 当前页 */
	private int mPageindex;

	private SeckillAdapter mAdapter;

	private CountDownTimer mCountDownTimer;

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seckill_layout);
		findView();
		setListener();
		InitData();
		id = getIntent().getIntExtra("id", 0);
		if (id != 0) {
			judgeSeckill(id);
		}

	}

	private void findView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_right = (Button) findViewById(R.id.ab_right);
		ab_fenlei = (ImageView) findViewById(R.id.ab_fenlei);
		ab_search = (LinearLayout) findViewById(R.id.ab_search);
		activity_index_sousuo_iv = (ImageView) findViewById(R.id.activity_index_sousuo_iv);
		activity_index_search_content = (TextView) findViewById(R.id.activity_index_search_content);
		scrollView = (StickyScrollView) findViewById(R.id.scrollview);
		service_card_layout_RadioGroup = (RadioGroup) findViewById(R.id.service_card_layout_RadioGroup);
		first_RadioButton = (RadioButton) findViewById(R.id.first_RadioButton);
		second_RadioButton = (RadioButton) findViewById(R.id.second_RadioButton);
		third_RadioButton = (RadioButton) findViewById(R.id.third_RadioButton);
		four_RadioButton = (RadioButton) findViewById(R.id.four_RadioButton);
		seckill_status = (TextView) findViewById(R.id.seckill_status);
		seckill_info = (TextView) findViewById(R.id.seckill_info);
		time_count_layout = (LinearLayout) findViewById(R.id.time_count_layout);
		hourTextView = (TextView) findViewById(R.id.hourTextView);
		minuteTextView = (TextView) findViewById(R.id.minuteTextView);
		secondTextView = (TextView) findViewById(R.id.secondTextView);
		seckill_listview = (ListView) findViewById(R.id.seckill_listview);
		mAdapter = new SeckillAdapter(mContext);
		seckill_listview.setAdapter(mAdapter);
		shownext = (TextView) findViewById(R.id.shownext);

	}

	private void setListener() {
		ab_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}
		});
		ab_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, IntroduceActivity.class);
				intent.putExtra("id", -40);
				startActivity(intent);
			}
		});
		ab_fenlei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppContext.MAIN_TASK = AppContext.CLASSIC;
				Intent i_main = new Intent(mContext, MainActivity.class);
				i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i_main);
				finish();
			}
		});
		ab_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(mContext, SiteSearchActivity.class);
				i.putExtra("content", "");
				startActivity(i);
			}
		});
		first_RadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					status = true;
					shownext.setVisibility(View.GONE);
					type = (Integer) buttonView.getTag();
					getTheList(1);
				}
			}
		});
		second_RadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					status = true;
					shownext.setVisibility(View.GONE);
					type = (Integer) buttonView.getTag();
					getTheList(1);
				}
			}
		});
		third_RadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					status = true;
					shownext.setVisibility(View.GONE);
					type = (Integer) buttonView.getTag();
					getTheList(1);
				}
			}
		});
		four_RadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					status = true;
					shownext.setVisibility(View.GONE);
					type = (Integer) buttonView.getTag();
					getTheList(1);
				}
			}
		});

		seckill_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int ID = mAdapter.getId(position);
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, String.valueOf(ID));
				intent.putExtra("position", position);
				startActivityForResult(intent, 69);
			}
		});
		scrollView.setOnBorderListener(new OnBorderListener() {

			@Override
			public void onother() {

			}

			@Override
			public void onTop() {

			}

			@Override
			public void onBottom() {
				// Log.i("status:", "---------------------------------------" +
				// status.toString());
				if (status) {
					shownext.setVisibility(View.GONE);
					getTheList(2);
					status = false;
				}
			}
		});
		shownext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scrollView.smoothScrollTo(0, 0);
				int id = service_card_layout_RadioGroup.getCheckedRadioButtonId();
				switch (id) {
				case R.id.first_RadioButton:
					if (titleBean.getSeckillTitle().size() > 1) {
						service_card_layout_RadioGroup.check(R.id.second_RadioButton);
					}
					break;
				case R.id.second_RadioButton:
					if (titleBean.getSeckillTitle().size() > 2) {
						service_card_layout_RadioGroup.check(R.id.third_RadioButton);
					} else {
						service_card_layout_RadioGroup.check(R.id.first_RadioButton);
					}
					break;
				case R.id.third_RadioButton:
					if (titleBean.getSeckillTitle().size() > 3) {
						service_card_layout_RadioGroup.check(R.id.four_RadioButton);
					} else {
						service_card_layout_RadioGroup.check(R.id.first_RadioButton);
					}
					break;
				case R.id.four_RadioButton:
					service_card_layout_RadioGroup.check(R.id.first_RadioButton);
					break;
				}
				status = false;
			}
		});
	}

	private void InitData() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				titleBean = SeckillTitleBean.explainJson(content, mContext);
				if (titleBean.type > 0) {
					RefreshTitle(titleBean.getSeckillTitle());
				}
			}
		};
		RequstClient.seckillLateralTitle(handler);
	}

	private void RefreshTitle(List<SeckillTitle> seckillTitle) {
		if (seckillTitle.size() >= 1) {
			SeckillTitle temp = seckillTitle.get(0);
			String info = temp.getSECKILLNAME() + "<br/>"
					+ StringUtils.toHour(temp.getSTARTTIME(), temp.getENDTIME(), titleBean.getCurDate());
			first_RadioButton.setText(Html.fromHtml(info));
			first_RadioButton.setTag(temp.getID());
			first_RadioButton.setVisibility(View.VISIBLE);

			status = true;
			shownext.setVisibility(View.GONE);
			type = temp.getID();
			getTheList(1);
		}
		if (seckillTitle.size() >= 2) {
			SeckillTitle temp = seckillTitle.get(1);
			String info = temp.getSECKILLNAME() + "<br/>"
					+ StringUtils.toHour(temp.getSTARTTIME(), temp.getENDTIME(), titleBean.getCurDate());
			second_RadioButton.setText(Html.fromHtml(info));
			second_RadioButton.setTag(temp.getID());
			second_RadioButton.setVisibility(View.VISIBLE);
		}
		if (seckillTitle.size() >= 3) {
			SeckillTitle temp = seckillTitle.get(2);
			String info = temp.getSECKILLNAME() + "<br/>"
					+ StringUtils.toHour(temp.getSTARTTIME(), temp.getENDTIME(), titleBean.getCurDate());
			third_RadioButton.setText(Html.fromHtml(info));
			third_RadioButton.setTag(temp.getID());
			third_RadioButton.setVisibility(View.VISIBLE);
		}
		if (seckillTitle.size() >= 4) {
			SeckillTitle temp = seckillTitle.get(3);
			String info = temp.getSECKILLNAME() + "<br/>"
					+ StringUtils.toHour(temp.getSTARTTIME(), temp.getENDTIME(), titleBean.getCurDate());
			four_RadioButton.setText(Html.fromHtml(info));
			four_RadioButton.setTag(temp.getID());
			four_RadioButton.setVisibility(View.VISIBLE);
		}
	}

	private void getTheList(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				listBean = SeckillListBean.explainJson(content, mContext);
				if (listBean.type > 0) {
					if (listBean.getSeckillList().size() > 0) {
						if (mPageindex == 1) {
							mAdapter.deleteItem();
						}
						mAdapter.addItem(listBean.getSeckillList());
						ListViewUtil.setListViewHight(seckill_listview);
						if (listBean.getSeckillList().size() < 10) {
							status = false;
							shownext.setVisibility(View.VISIBLE);
						} else {
							status = true;
						}
					} else {
						shownext.setVisibility(View.VISIBLE);
					}
					if (mPageindex == 1) {
						scrollView.smoothScrollTo(0, 0);
						TextTask(listBean.getSTARTTIME(), listBean.getENDTIME(), titleBean.getCurDate());
					}

				}
			}

		};
		RequstClient.allSecKill(type, mPageindex, handler);
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

		// Log.i("time", "nowSecond:" + nowSecond);
		// Log.i("time", "startSecond:" + startSecond);
		// Log.i("time", "endSecond:" + endSecond);

		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
		if (nowSecond < startSecond) {
			seckill_status.setText("即将开始");
			seckill_info.setText("距离本场开始还有：");
			mCountDownTimer = new CountDownTimer(startSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					hourTextView.setText(MathUtil.intForTwoSize(hour));
					minuteTextView.setText(MathUtil.intForTwoSize(minute));
					secondTextView.setText(MathUtil.intForTwoSize(second));
					nowSecond += 1000;
					// Log.i("time", "nowSecond:" + nowSecond);
					// Log.i("time", "millisUntilFinished:" +
					// millisUntilFinished);

				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond < endSecond) {
			seckill_status.setText("抢购中");
			seckill_info.setText("距离本场结束还有：");
			mCountDownTimer = new CountDownTimer(endSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					hourTextView.setText(MathUtil.intForTwoSize(hour));
					minuteTextView.setText(MathUtil.intForTwoSize(minute));
					secondTextView.setText(MathUtil.intForTwoSize(second));
					nowSecond += 1000;
					// Log.i("time", "nowSecond:" + nowSecond);
					// Log.i("time", "millisUntilFinished:" +
					// millisUntilFinished);
				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond >= endSecond) {
			// Log.i("time", "nowSecond:" + nowSecond);
			// Log.i("time", "endSecond:" + endSecond);
			seckill_status.setText("抢购结束");
			seckill_info.setText("");
			time_count_layout.setVisibility(View.GONE);
		}

	}

	private void judgeSeckill(int letterId) {
		RequstClient.judgeSeckill(letterId, new CustomResponseHandler(this, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						Toast.makeText(SeckillActivity.this, "秒杀已过期!", Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void finish() {
		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 69) {
			int position = data.getIntExtra("positon", -1);
			int isCollect = data.getIntExtra("isCollect", 0);
			if (position != -1) {
//				listBean.getSeckillList().get(position).setCOLLECTFLAG(isCollect);
//				mAdapter.notifyDataSetChanged();
				mAdapter.setCollect(position, isCollect);
			}
		}
	}

}
