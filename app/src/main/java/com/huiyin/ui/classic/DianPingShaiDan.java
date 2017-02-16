package com.huiyin.ui.classic;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.DianPingShaiDanAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.DianPingShaiDanList;
import com.huiyin.bean.DianPingShaiDanList.DianPing;
import com.huiyin.wight.MyListView;

public class DianPingShaiDan extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "DianPingShaiDan";

	public static final String INTENT_KEY_ID = "commodity_id";

	public static final String INTENT_KEY_STATE = "state";

	/** 商品的id */
	private String commodity_id;

	private int state;

	private TextView left_ib;

	private String flag;
	private int index;

	private LinearLayout dian_ping_shai_dan_ll;

	private MyListView dian_ping_shai_dan_xv;
	private DianPingShaiDanAdapter adapter;

	private TextView dian_ping_shai_dan_pinglun;
	private TextView dian_ping_shai_dan_haoping;
	private TextView dian_ping_shai_dan_zhongping;
	private TextView dian_ping_shai_dan_chaping;
	private TextView dian_ping_shai_dan_shaidan;
	private TextView dian_ping_shai_dan_pinglun_num;
	private TextView dian_ping_shai_dan_haopinglu;
	private TextView dian_ping_shai_dan_zhongpinglu;
	private TextView dian_ping_shai_dan_chapinglu;
	private TextView dian_ping_shai_dan_shaidanlu;
	private LinearLayout dian_ping_shai_dan_pinglun_ll;
	private LinearLayout dian_ping_shai_dan_haoping_ll;
	private LinearLayout dian_ping_shai_dan_zhongping_ll;
	private LinearLayout dian_ping_shai_dan_chaping_ll;
	private LinearLayout dian_ping_shai_dan_shaidan_ll;

	private ProgressBar mProgressBar;
	private TextView mHintView;

	private View view;

	private class DianPingBean implements Serializable {

		private static final long serialVersionUID = 1L;

		public String ZNUM; // 全部评论
		public String HAONUM; // 好评数
		public String HAOPING; // 好评率
		public String ZHONGNUM; // 中评数
		public String ZHONGPING; // 中评率
		public String CHANUM; // 差评数
		public String CHAPING; // 差评率
		public String SHAIDANNUM;// 晒单数
		public String SHAIDANPING;// 晒单率
	}

	private DianPingBean dianPingBean;

	private DianPingShaiDanList testBean;

	private HorizontalScrollView dian_ping_shai_dan_hScrollview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dian_ping_shai_dan);
		initView();

	}

	private void initView() {

		flag = "0";
		index = 1;
		commodity_id = getIntent().getStringExtra(INTENT_KEY_ID);
		state = getIntent().getIntExtra(INTENT_KEY_STATE, 0);

		left_ib = (TextView) this.findViewById(R.id.left_ib);
		left_ib.setOnClickListener(this);
		TextView tv_title = (TextView) findViewById(R.id.middle_title_tv);
		tv_title.setText("商品评价");
		TextView tv_title_right = (TextView) findViewById(R.id.right_ib);
		tv_title_right.setVisibility(View.INVISIBLE);

		footer();

		dian_ping_shai_dan_xv = (MyListView) findViewById(R.id.dian_ping_shai_dan_xv);

		adapter = new DianPingShaiDanAdapter(this);
		dian_ping_shai_dan_xv.setAdapter(adapter);

		dian_ping_shai_dan_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_ll);
		dian_ping_shai_dan_ll.addView(view);

		dian_ping_shai_dan_pinglun = (TextView) findViewById(R.id.dian_ping_shai_dan_pinglun);
		dian_ping_shai_dan_haoping = (TextView) findViewById(R.id.dian_ping_shai_dan_haoping);
		dian_ping_shai_dan_zhongping = (TextView) findViewById(R.id.dian_ping_shai_dan_zhongping);
		dian_ping_shai_dan_chaping = (TextView) findViewById(R.id.dian_ping_shai_dan_chaping);
		dian_ping_shai_dan_shaidan = (TextView) findViewById(R.id.dian_ping_shai_dan_shaidan);

		dian_ping_shai_dan_pinglun_num = (TextView) findViewById(R.id.dian_ping_shai_dan_pinglun_num);
		dian_ping_shai_dan_haopinglu = (TextView) findViewById(R.id.dian_ping_shai_dan_haopinglu);
		dian_ping_shai_dan_zhongpinglu = (TextView) findViewById(R.id.dian_ping_shai_dan_zhongpinglu);
		dian_ping_shai_dan_chapinglu = (TextView) findViewById(R.id.dian_ping_shai_dan_chapinglu);
		dian_ping_shai_dan_shaidanlu = (TextView) findViewById(R.id.dian_ping_shai_dan_shaidanlu);

		dian_ping_shai_dan_pinglun_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_pinglun_ll);
		dian_ping_shai_dan_haoping_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_haoping_ll);
		dian_ping_shai_dan_zhongping_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_zhongping_ll);
		dian_ping_shai_dan_chaping_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_chaping_ll);
		dian_ping_shai_dan_shaidan_ll = (LinearLayout) findViewById(R.id.dian_ping_shai_dan_shaidan_ll);
		dian_ping_shai_dan_pinglun_ll.setOnClickListener(this);
		dian_ping_shai_dan_haoping_ll.setOnClickListener(this);
		dian_ping_shai_dan_zhongping_ll.setOnClickListener(this);
		dian_ping_shai_dan_chaping_ll.setOnClickListener(this);
		dian_ping_shai_dan_shaidan_ll.setOnClickListener(this);

		dian_ping_shai_dan_hScrollview = (HorizontalScrollView) findViewById(R.id.dian_ping_shai_dan_hScrollview);

		if (state == 0) {

			dian_ping_shai_dan_pinglun.setSelected(true);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(false);
		} else {

			flag = "4";

			dian_ping_shai_dan_pinglun.setSelected(false);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(true);

			dian_ping_shai_dan_hScrollview.post(new Runnable() {

				@Override
				public void run() {

					dian_ping_shai_dan_hScrollview
							.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			});
		}

		dianPingHttp();
		getData(index, flag);
	}

	private void initData() {

		dian_ping_shai_dan_pinglun_num.setText("(" + dianPingBean.ZNUM + ")");
		dian_ping_shai_dan_haoping.setText("好评(" + dianPingBean.HAONUM + ")");
		dian_ping_shai_dan_haopinglu.setText(dianPingBean.HAOPING);
		dian_ping_shai_dan_zhongping.setText("中评(" + dianPingBean.ZHONGNUM
				+ ")");
		dian_ping_shai_dan_zhongpinglu.setText(dianPingBean.ZHONGPING);
		dian_ping_shai_dan_chaping.setText("差评(" + dianPingBean.CHANUM + ")");
		dian_ping_shai_dan_chapinglu.setText(dianPingBean.CHAPING);
		dian_ping_shai_dan_shaidan.setText("晒单(" + dianPingBean.SHAIDANNUM
				+ ")");
		dian_ping_shai_dan_shaidanlu.setText(dianPingBean.SHAIDANPING);

	}

	public void dianPingHttp() {

		RequstClient.getDianPingShu(commodity_id, new CustomResponseHandler(
				this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				boolean isSuccess = true;
				try {
					JSONObject obj = new JSONObject(content);
					String result = obj.getString("type");
					if ("1".equals(result)) {
						// 请求成功
						dianPingBean = new Gson().fromJson(
								obj.getString("review"), DianPingBean.class);
						isSuccess = true;
					} else {
						isSuccess = false;
						Toast.makeText(getApplicationContext(),
								obj.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					isSuccess = false;
				}

				if (isSuccess) {

					initData();
				}
			}
		});

	}

	public void getData(final int pageIndex, String flag) {

		RequstClient.sunSingle(10 + "", pageIndex + "", commodity_id, flag,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						normal();
						try {
							JSONObject obj = new JSONObject(content);
							String result = obj.getString("type");
							if ("1".equals(result)) {
								// 请求成功
								testBean = new Gson().fromJson(content,
										DianPingShaiDanList.class);
								ArrayList<DianPing> list = testBean.reviewList;
								index++;

								if (list != null && list.size() > 0) {

									if (list.size() < 10) {

										hide();
									} else {

										show();
									}
									if (pageIndex == 1) {

										adapter.addDianPingList(list);
									} else {

										adapter.addMoreDianPingList(list);
									}
								} else {
									Toast.makeText(DianPingShaiDan.this,
											"已无更多数据！", Toast.LENGTH_SHORT)
											.show();
									hide();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										obj.getString("msg"), Toast.LENGTH_LONG)
										.show();
							}
						} catch (Exception e) {

						}

					}
				});
	}

	private void footer() {

		view = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.xlistview_footer, null);
		mProgressBar = (ProgressBar) view
				.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) view
				.findViewById(R.id.xlistview_footer_hint_textview);
		mHintView.setOnClickListener(this);

		view.setVisibility(View.GONE);

	}

	public void normal() {

		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);

	}

	public void loading() {

		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);

	}

	public void hide() {

		view.setVisibility(View.GONE);
	}

	public void show() {

		view.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {

		case R.id.left_ib:
			finish();
			break;

		case R.id.xlistview_footer_hint_textview:
			getData(index, flag);
			loading();
			break;

		case R.id.dian_ping_shai_dan_pinglun_ll:
			// 切换时的颜色变化
			dian_ping_shai_dan_pinglun.setSelected(true);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(false);

			adapter.clearDianPingList();
			flag = "0";
			index = 1;
			getData(index, flag);

			break;
		case R.id.dian_ping_shai_dan_haoping_ll:
			// 切换时的颜色变化
			dian_ping_shai_dan_pinglun.setSelected(false);
			dian_ping_shai_dan_haoping.setSelected(true);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(false);

			adapter.clearDianPingList();
			flag = "1";
			index = 1;
			getData(index, flag);

			break;
		case R.id.dian_ping_shai_dan_zhongping_ll:
			// 切换时的颜色变化
			dian_ping_shai_dan_pinglun.setSelected(false);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(true);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(false);

			adapter.clearDianPingList();
			flag = "2";
			index = 1;
			getData(index, flag);

			break;
		case R.id.dian_ping_shai_dan_chaping_ll:
			// 切换时的颜色变化
			dian_ping_shai_dan_pinglun.setSelected(false);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(true);
			dian_ping_shai_dan_shaidan.setSelected(false);

			adapter.clearDianPingList();
			flag = "3";
			index = 1;
			getData(index, flag);

			break;
		case R.id.dian_ping_shai_dan_shaidan_ll:
			// 切换时的颜色变化
			dian_ping_shai_dan_pinglun.setSelected(false);
			dian_ping_shai_dan_haoping.setSelected(false);
			dian_ping_shai_dan_zhongping.setSelected(false);
			dian_ping_shai_dan_chaping.setSelected(false);
			dian_ping_shai_dan_shaidan.setSelected(true);

			adapter.clearDianPingList();
			flag = "4";
			index = 1;
			getData(index, flag);

			break;
		default:
			break;
		}
	}

}
