package com.huiyin.ui.user;

import java.util.ArrayList;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.MyOrderDetailActivity.OrderDetailBean.GoodItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderCommentActivity extends BaseActivity {

	private final static String TAG = "OrderCommentActivity";
	TextView middle_title_tv, left_ib;
	LinearLayout order_add_comment_item;
	public ArrayList<GoodItem> orderDetailList;
	LayoutInflater inflater;
	// 商品包装、送货速度、配送满意度
	ImageView good_package_iv, send_speed_iv, distribute_iv;
	// 订单评价
	TextView all_comment_tv;
	// 订单id
	String order_id;
	LinearLayout all_comment_linearlayout;

	public static final int COMMODITY_COMMENT = 005;

	private static int which_poc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_comment_layout);

		initView();

	}

	public static void setPos(int which) {
		which_poc = which;
	}

	private void initView() {

		setPos(0);
		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("订单评价");
		order_add_comment_item = (LinearLayout) findViewById(R.id.order_add_comment_item);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		orderDetailList = (ArrayList<GoodItem>) getIntent()
				.getSerializableExtra("goodlist");
		if (orderDetailList == null) {
			return;
		}

		// 订单id
		order_id = orderDetailList.get(0).ORDER_ID;

		all_comment_linearlayout = (LinearLayout) findViewById(R.id.all_comment_linearlayout);

		for (int i = 0; i < orderDetailList.size(); i++) {

			final int pos = i;
			final GoodItem good_item = orderDetailList.get(i);
			View view_item = inflater
					.inflate(R.layout.order_comment_item, null);

			final TextView comment_commit_tv = (TextView) view_item
					.findViewById(R.id.comment_commit_tv);

			if (good_item.COMMENTS_STATUS.equals("1")) {
				comment_commit_tv.setText("已评价");
				comment_commit_tv.setTextColor(getResources().getColor(
						R.color.grey));
				comment_commit_tv
						.setBackgroundResource(R.drawable.grey_cornor_bg);
				comment_commit_tv.setEnabled(false);
			} else {
				comment_commit_tv.setText("评价晒单");
				comment_commit_tv.setTextColor(getResources().getColor(
						R.color.white));
				comment_commit_tv
						.setBackgroundResource(R.drawable.red_cornor_bg);
				comment_commit_tv.setEnabled(true);
			}

			comment_commit_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					setPos(pos);
					Intent intent = new Intent(OrderCommentActivity.this,
							CommodityCommentActivity.class);
					intent.putExtra("commodity", good_item);
					startActivityForResult(intent, COMMODITY_COMMENT);
				}
			});

			ImageView commodity_img = (ImageView) view_item
					.findViewById(R.id.commodity_img);
			TextView commodity_title = (TextView) view_item
					.findViewById(R.id.commodity_title);

			commodity_title.setText(good_item.COMMODITY_NAME);
			ImageLoader.getInstance().displayImage(
					URLs.IMAGE_URL + good_item.COMMODITY_IMAGE_PATH,
					commodity_img);

			order_add_comment_item.addView(view_item);

		}

		good_package_iv = (ImageView) findViewById(R.id.good_package_iv);
		send_speed_iv = (ImageView) findViewById(R.id.send_speed_iv);
		distribute_iv = (ImageView) findViewById(R.id.distribute_iv);

		good_package_iv.setTag("5");
		good_package_iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_DOWN:
					float x = event.getX();

					float widthRate = x / arg0.getWidth();
					if (widthRate > 0 && widthRate < 0.2) {
						good_package_iv.setBackgroundResource(R.drawable.px_1);
						good_package_iv.setTag("1");
					} else if (widthRate > 0.2 && widthRate < 0.4) {
						good_package_iv.setBackgroundResource(R.drawable.px_2);
						good_package_iv.setTag("2");
					} else if (widthRate > 0.4 && widthRate < 0.6) {
						good_package_iv.setBackgroundResource(R.drawable.px_3);
						good_package_iv.setTag("3");
					} else if (widthRate > 0.6 && widthRate < 0.8) {
						good_package_iv.setBackgroundResource(R.drawable.px_4);
						good_package_iv.setTag("4");
					} else if (widthRate > 0.8 && widthRate < 1) {
						good_package_iv.setBackgroundResource(R.drawable.px_5);
						good_package_iv.setTag("5");
					}
					break;

				}
				return false;

			}

		});

		send_speed_iv.setTag("5");
		send_speed_iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_DOWN:
					float x = event.getX();

					float widthRate = x / arg0.getWidth();
					if (widthRate > 0 && widthRate < 0.2) {
						send_speed_iv.setBackgroundResource(R.drawable.px_1);
						send_speed_iv.setTag("1");
					} else if (widthRate > 0.2 && widthRate < 0.4) {
						send_speed_iv.setBackgroundResource(R.drawable.px_2);
						send_speed_iv.setTag("2");
					} else if (widthRate > 0.4 && widthRate < 0.6) {
						send_speed_iv.setBackgroundResource(R.drawable.px_3);
						send_speed_iv.setTag("3");
					} else if (widthRate > 0.6 && widthRate < 0.8) {
						send_speed_iv.setBackgroundResource(R.drawable.px_4);
						send_speed_iv.setTag("4");
					} else if (widthRate > 0.8 && widthRate < 1) {
						send_speed_iv.setBackgroundResource(R.drawable.px_5);
						send_speed_iv.setTag("5");
					}
					break;

				}
				return false;

			}

		});

		distribute_iv.setTag("5");
		distribute_iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_DOWN:
					float x = event.getX();

					float widthRate = x / arg0.getWidth();
					if (widthRate > 0 && widthRate < 0.2) {
						distribute_iv.setBackgroundResource(R.drawable.px_1);
						distribute_iv.setTag("1");
					} else if (widthRate > 0.2 && widthRate < 0.4) {
						distribute_iv.setBackgroundResource(R.drawable.px_2);
						distribute_iv.setTag("2");
					} else if (widthRate > 0.4 && widthRate < 0.6) {
						distribute_iv.setBackgroundResource(R.drawable.px_3);
						distribute_iv.setTag("3");
					} else if (widthRate > 0.6 && widthRate < 0.8) {
						distribute_iv.setBackgroundResource(R.drawable.px_4);
						distribute_iv.setTag("4");
					} else if (widthRate > 0.8 && widthRate < 1) {
						distribute_iv.setBackgroundResource(R.drawable.px_5);
						distribute_iv.setTag("5");
					}
					break;

				}
				return false;

			}

		});

		all_comment_tv = (TextView) findViewById(R.id.all_comment_tv);

		if (getIntent().getIntExtra("flag", 0) == 1) {

			all_comment_linearlayout.setVisibility(View.GONE);
			all_comment_tv.setEnabled(false);
		} else {
			all_comment_linearlayout.setVisibility(View.VISIBLE);
			all_comment_tv.setEnabled(true);
		}

		all_comment_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				for (int i = 0; i < order_add_comment_item.getChildCount(); i++) {
					TextView comment_commit_tv = (TextView) order_add_comment_item
							.getChildAt(i).findViewById(R.id.comment_commit_tv);
					if (comment_commit_tv.isEnabled()) {
						Toast.makeText(getBaseContext(), "请先对商品进行评论！",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				String startNum1 = (String) good_package_iv.getTag();
				String startNum2 = (String) send_speed_iv.getTag();
				String startNum3 = (String) distribute_iv.getTag();
				if (startNum1.equals("0") || startNum2.equals("0")
						|| startNum3.equals("0")) {
					Toast.makeText(getBaseContext(), "请选择评论星数！",
							Toast.LENGTH_SHORT).show();

				} else {
					postTotalComment(startNum1, startNum2, startNum3);
				}
			}
		});
	}

	/**
	 * 提交订单评价
	 * 
	 * @param num1
	 * @param num2
	 * @param num3
	 */
	private void postTotalComment(String num1, String num2, String num3) {

		RequstClient.postTotalComment(order_id, num1, num2, num3,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {

						super.onSuccess(statusCode, headers, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							all_comment_tv.setText("已评价");
							all_comment_tv.setEnabled(false);
							MyOrderActivity.setFlush(true);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == COMMODITY_COMMENT) {
			TextView comment_commit_tv = (TextView) order_add_comment_item
					.getChildAt(which_poc).findViewById(R.id.comment_commit_tv);
			if (comment_commit_tv.isEnabled()) {
				comment_commit_tv.setText("已评价");
				comment_commit_tv.setTextColor(getResources().getColor(
						R.color.grey));
				comment_commit_tv
						.setBackgroundResource(R.drawable.grey_cornor_bg);
				comment_commit_tv.setEnabled(false);
			}
		}
	}

}
