package com.huiyin.ui.classic;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.adapter.GoodsDetailGalleryAdapter;
import com.huiyin.anim.DepthPageTransformer;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.GoodsDetailBeen;
import com.huiyin.bean.GoodsDetailBeen.GDBItem;
import com.huiyin.bean.GoodsDetailBeen.GoodDetials;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.rongcloud.RongCloud;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BasicInformationFragment extends Fragment implements
		OnClickListener, OnPageChangeListener {

	private TextView tv_shopping_car_count;// 购物车按钮右上角计数器

	private Button btn_shopping_car, btn_checkout, btn_add, btn_order;// 底部三按钮

	// --------------------产品轮换图---------
	private ViewPager gallery;
	private GoodsDetailGalleryAdapter galleryAdapter;
	private List<String> listGallerys;

	private TextView gallery_count;

	private LinearLayout indicator;
	private List<ImageView> indicatorList;

	// ----------------也许喜欢
	private ViewPager mebyLikeViewPager;
	private List<Fragment> listLikeFragments;
	private FragmentViewPagerAdapter likeViewpagerAdapter;

	private GoodsDetailBeen goodsDetailBeen;

	// -----------------乐虎价格
	private TextView lehu_price;
	private TextView lehu_price1;
	private TextView reference_price;

	private TextView nameTextView, descriptionTextView;
	private CheckBox collection_checkbox;
	private TextView collection_count;
	private int collection;

	private LinearLayout cuxiaojia;
	private LinearLayout cankaojia;

	// 基本信息
	private TextView layout_baseinfo;

	private RatingBar ratingBar;

	private View layout_evaluation;

	private TextView tv_arraw;

	private LinearLayout layout_dianping;

	private View layout_shaidan;

	private View layout_kefu;

	private TextView shaidan_tv;

	// 组合购买
	private LinearLayout group;
	private TextView tv_group_discount;

	// 计时的
	private LinearLayout countTimeLayout;
	private TextView countTime;

	private CountDownTimer mCountDownTimer;

	private int subscribeId;// 预约ID

	// 设置数据
	public void setData(GoodsDetailBeen gdbbean, int subscribeId) {
		this.goodsDetailBeen = gdbbean;
		this.subscribeId = subscribeId;
		if (isCreate) {
			setView();
		}
	}

	private boolean isCreate = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_detail_baseinfo,
				null);
		initData();
		initViews(view);
		isCreate = true;

		if (goodsDetailBeen != null) {
			// 解决一个网络数据与UI操作先后时序的BUG
			setView();
		}

		return view;
	}

	private void initData() {
		listGallerys = new ArrayList<String>();
		indicatorList = new ArrayList<ImageView>();
		listLikeFragments = new ArrayList<Fragment>();
	}

	private void initViews(View view) {

		cuxiaojia = (LinearLayout) view.findViewById(R.id.cuxiaojia);
		cankaojia = (LinearLayout) view.findViewById(R.id.cankaojia);

		lehu_price = (TextView) view.findViewById(R.id.lehu_price);
		lehu_price1 = (TextView) view.findViewById(R.id.lehu_price1);
		reference_price = (TextView) view.findViewById(R.id.reference_price);

		nameTextView = (TextView) view.findViewById(R.id.name);
		descriptionTextView = (TextView) view.findViewById(R.id.description);

		layout_evaluation = view.findViewById(R.id.layout_evaluation);
		layout_evaluation.setOnClickListener(this);

		layout_dianping = (LinearLayout) view
				.findViewById(R.id.layout_dianping);

		layout_shaidan = view.findViewById(R.id.layout_shaidan);
		layout_shaidan.setOnClickListener(this);

		shaidan_tv = (TextView) view.findViewById(R.id.shaidan_tv);

		layout_kefu = view.findViewById(R.id.layout_kefu);
		layout_kefu.setOnClickListener(this);

		collection_checkbox = (CheckBox) view
				.findViewById(R.id.collection_checkbox);
		collection_count = (TextView) view.findViewById(R.id.collection_count);

		tv_shopping_car_count = (TextView) view
				.findViewById(R.id.tv_shopping_car_count);
		btn_shopping_car = (Button) view.findViewById(R.id.btn_shopping_car);
		btn_checkout = (Button) view.findViewById(R.id.btn_checkout);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_order = (Button) view.findViewById(R.id.btn_order);

		btn_shopping_car.setOnClickListener(this);
		btn_checkout.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		btn_order.setOnClickListener(this);

		gallery_count = (TextView) view.findViewById(R.id.gallery_count);

		indicator = (LinearLayout) view.findViewById(R.id.indicator);

		gallery = (ViewPager) view.findViewById(R.id.gallery);
		galleryAdapter = new GoodsDetailGalleryAdapter(listGallerys,
				getActivity());
		gallery.setPageTransformer(true, new DepthPageTransformer());
		gallery.setAdapter(galleryAdapter);
		gallery.setOnPageChangeListener(this);

		group = (LinearLayout) view.findViewById(R.id.zuhe_layout);
		tv_group_discount = (TextView) view
				.findViewById(R.id.tv_group_discount);

		likeViewpagerAdapter = new FragmentViewPagerAdapter(
				getFragmentManager(), listLikeFragments);
		mebyLikeViewPager = (ViewPager) view
				.findViewById(R.id.mebyLikeViewPager);
		mebyLikeViewPager.setAdapter(likeViewpagerAdapter);

		layout_baseinfo = (TextView) view.findViewById(R.id.layout_baseinfo);
		layout_baseinfo.setOnClickListener(this);

		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
		ratingBar.setEnabled(false);
		tv_arraw = (TextView) view.findViewById(R.id.tv_arraw);

		countTimeLayout = (LinearLayout) view
				.findViewById(R.id.countTime_layout);
		countTime = (TextView) view.findViewById(R.id.countTime);
	}

	private void setView() {
		// 产品图
		String listGallerysStr = goodsDetailBeen.commodity.commodity.COMMODITY_IMAGE_LIST;
		String gallerys[] = listGallerysStr.split(",");

		if (listGallerys == null) {
			listGallerys = new ArrayList<String>();
		} else {
			listGallerys.clear();
		}

		for (String string : gallerys) {
			listGallerys.add(URLs.IMAGE_URL + string);
		}
		galleryAdapter
				.setQuota(goodsDetailBeen.commodity.commodity.QUOTA_FLAG != 2
						&& goodsDetailBeen.commodity.commodity.MARK == 1);
		galleryAdapter.notifyDataSetChanged();

		if (goodsDetailBeen.commodity.commodity.MARK == 3
				|| goodsDetailBeen.commodity.commodity.MARK == 4
				|| goodsDetailBeen.commodity.commodity.MARK == 5) {
			if (!StringUtils
					.isBlank(goodsDetailBeen.commodity.commodity.START_TIME)
					&& !StringUtils
							.isBlank(goodsDetailBeen.commodity.commodity.END_TIME)
					&& !StringUtils
							.isBlank(goodsDetailBeen.commodity.commodity.curDate)) {
				countTimeLayout.setVisibility(View.VISIBLE);
				switch (goodsDetailBeen.commodity.commodity.MARK) {
				case 3:
					titleString = "距离秒杀";
					break;
				case 4:
					titleString = "距离闪购";
					break;
				case 5:
					titleString = "距离预约";
					break;
				}
			}
			TextTask(goodsDetailBeen.commodity.commodity.START_TIME,
					goodsDetailBeen.commodity.commodity.END_TIME,
					goodsDetailBeen.commodity.commodity.curDate);
			if (goodsDetailBeen.commodity.commodity.MARK == 5) {
				btn_checkout.setVisibility(View.GONE);
				btn_add.setVisibility(View.GONE);
				btn_order.setVisibility(View.VISIBLE);
				if (goodsDetailBeen.commodity.commodity.BESPEAK_MARK
						.equals("1")) {
					btn_order.setText("已预约");
					btn_order
							.setBackgroundResource(R.drawable.common_btn_gray2_selector);
					btn_order.setEnabled(false);
				} else {
					btn_order.setText("马上预约");
					btn_order
							.setBackgroundResource(R.drawable.common_btn_red_selector);
					btn_order.setEnabled(true);
				}
			}
		}

		String price = goodsDetailBeen.commodity.commodity.PRICE;// 乐虎价格
		String peferencePrice = goodsDetailBeen.commodity.commodity.REFERENCE_PRICE;// 推荐价格

		SpannableString sp = null;
		String title = null;
		if (goodsDetailBeen.commodity.commodity.PROMOTIONS_TYPE.equals("1")) {
			sp = new SpannableString(MathUtil.priceForAppWithSign(price));
			sp.setSpan(new StrikethroughSpan(), 0, price.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			title = MathUtil
					.priceForAppWithSign(goodsDetailBeen.commodity.commodity.PROMOTIONS_PRICE)
					+ " ("
					+ MathUtil
							.stringKeep1Decimal(goodsDetailBeen.commodity.commodity.PROMOTIONS_DISCOUNT)
					+ "折)";
			lehu_price.setText(sp);
			lehu_price1.setText(title);
			lehu_price.setTextColor(getActivity().getResources().getColor(
					R.color.grey_text));
			cuxiaojia.setVisibility(View.VISIBLE);
			cankaojia.setVisibility(View.GONE);
		} else if (goodsDetailBeen.commodity.commodity.PROMOTIONS_TYPE
				.equals("2")) {
			sp = new SpannableString(MathUtil.priceForAppWithSign(price));
			sp.setSpan(new StrikethroughSpan(), 0, price.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			lehu_price.setTextColor(getActivity().getResources().getColor(
					R.color.grey_text));
			title = MathUtil
					.priceForAppWithSign(goodsDetailBeen.commodity.commodity.PROMOTIONS_PRICE)
					+ " (直降"
					+ MathUtil
							.priceForAppWithSign(goodsDetailBeen.commodity.commodity.PROMOTIONS_DISCOUNT)
					+ "元)";
			lehu_price.setText(sp);
			lehu_price1.setText(title);
			cuxiaojia.setVisibility(View.VISIBLE);
			cankaojia.setVisibility(View.GONE);
		} else if (goodsDetailBeen.commodity.commodity.PROMOTIONS_TYPE
				.equals("3")) {
			cuxiaojia.setVisibility(View.GONE);
			cankaojia.setVisibility(View.VISIBLE);
			lehu_price.setText(MathUtil.priceForAppWithSign(price));
		}

		if (goodsDetailBeen.commodity.commodity.MARK == 5) {
			title = MathUtil
					.priceForAppWithSign(goodsDetailBeen.commodity.commodity.PROMOTIONS_PRICE);
			lehu_price1.setText(title);
		}

		reference_price.setText(MathUtil.priceForAppWithSign(peferencePrice));
		reference_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		collection = goodsDetailBeen.commodity.commodity.COLLECT;// 收藏个数
		boolean isCollection = goodsDetailBeen.commodity.commodity.COLLECT_FLAG == 1;// 是否收藏

		collection_count.setText("（" + collection + "人）");
		collection_checkbox.setChecked(isCollection);

		collection_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (AppContext.getInstance().getUserId() == null) {
							Toast.makeText(getActivity(), "请先登录",
									Toast.LENGTH_SHORT).show();
							collection_checkbox.setChecked(false);
							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							intent.putExtra(LoginActivity.TAG,
									LoginActivity.hkdetail_code);
							getActivity().startActivity(intent);
							return;
						}
						if (isChecked) {
							//收藏一个商品
							collect(AppContext.getInstance().getUserId());
						} else {
							//取消收藏一个商品
							unCollect(AppContext.getInstance().getUserId());
						}
					}
				});

		String name = goodsDetailBeen.commodity.commodity.COMMODITY_NAME;// 产品名称
		String description = goodsDetailBeen.commodity.commodity.COMMODITY_AD;// 广告语
		nameTextView.setText(name);
		descriptionTextView.setText(description);

		float score = goodsDetailBeen.commodity.commodityScore;// 评分，人数，百分比

		if (score <= 0) {
			score = 100;
		}
		tv_arraw.setText(MathUtil.keepMost1Decimal(score) + "%("
				+ goodsDetailBeen.commodity.commoditySum + "人评价)");
		ratingBar.setMax(100);
		ratingBar.setProgress((int) score);
		shaidan_tv.setText("商品晒单(" + goodsDetailBeen.commodity.baskCommodtiy
				+ ")");

		layout_dianping.removeAllViews();
		//点评加布局的地方
		if (goodsDetailBeen.commodity.reviewList != null) {

			if (goodsDetailBeen.commodity.reviewList.size() >= 1) {
				layout_dianping
						.addView(addDianPingToLayout(goodsDetailBeen.commodity.reviewList
								.get(0)));
			}
			if (goodsDetailBeen.commodity.reviewList.size() >= 2) {
				layout_dianping
						.addView(addDianPingToLayout(goodsDetailBeen.commodity.reviewList
								.get(1)));
			}
		}
		// 组合购买 未完成屏蔽
		if (false && goodsDetailBeen.commodity.groupCommodityList != null
				&& goodsDetailBeen.commodity.groupCommodityList.size() > 0) {
			group.setVisibility(View.VISIBLE);
			group.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 加入购物车  showMenu 加一个数值
					((GoodsDetailActivity) getActivity()).showMenu(1);
					// showMenuType 等于2
					GoodsDetailActivity.showMenuType = 2;
				}
			});
		}
		// 也许喜欢
		List<GDBItem> listTemp = new ArrayList<GoodsDetailBeen.GDBItem>();
		listLikeFragments.clear();
		List<GDBItem> listLikes = goodsDetailBeen.commodity.reList;
		if (listLikes != null) {
			for (int i = 0; i < listLikes.size(); i++) {
				if (listTemp.size() < 4) {
					listTemp.add(listLikes.get(i));
					if (i >= listLikes.size() - 1) {
						List<GDBItem> listItems = new ArrayList<GoodsDetailBeen.GDBItem>();
						listItems.addAll(listTemp);
						GoodsDetailLikeFragment fragment = new GoodsDetailLikeFragment();
						fragment.setData(listItems);
						listLikeFragments.add(fragment);
					}
				} else {
					List<GDBItem> listItems = new ArrayList<GoodsDetailBeen.GDBItem>();
					listItems.addAll(listTemp);
					GoodsDetailLikeFragment fragment = new GoodsDetailLikeFragment();
					fragment.setData(listItems);
					listLikeFragments.add(fragment);
					listTemp = new ArrayList<GoodsDetailBeen.GDBItem>();
					listTemp.add(listLikes.get(i));
				}
			}

			likeViewpagerAdapter.notifyDataSetChanged();
			if (listLikes.size() > 0)
				mebyLikeViewPager.setVisibility(View.VISIBLE);
		}

		if (listGallerys.size() > 0) {
			gallery_count.setText(1 + "/" + listGallerys.size() + "张");
			for (int i = 0; i < listGallerys.size(); i++) {
				ImageView imageView = new ImageView(getActivity());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT); // , 1是可选写的
				lp.setMargins(5, 0, 5, 0);
				imageView.setLayoutParams(lp);
				if (i == 0) {
					imageView
							.setImageResource(R.drawable.page_indicator_focused);
				} else {
					imageView.setImageResource(R.drawable.page_indicator);
				}
				indicator.addView(imageView);
				indicatorList.add(imageView);
			}
			if (listGallerys.size() > 1) {
				indicator.setVisibility(View.VISIBLE);
			} else {
				indicator.setVisibility(View.GONE);
			}
		} else {
			gallery_count.setText("");
			indicator.setVisibility(View.GONE);
		}

		int count = 0;
		count = MathUtil
				.stringToInt(goodsDetailBeen.commodity.commodity.SHOPPING_CAR);
		if (count <= 0) {
			tv_shopping_car_count.setVisibility(View.GONE);
		} else {
			if (count > 99) {
				tv_shopping_car_count.setText("99+");
			} else {
				tv_shopping_car_count
						.setText(goodsDetailBeen.commodity.commodity.SHOPPING_CAR);
			}
			tv_shopping_car_count.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 点评 那个布局的加载  那个5颗星的 评论.
	 * @param item
	 * @return
	 */
	private View addDianPingToLayout(GoodsDetailBeen.DianPing item) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dian_ping_item, null);

		RatingBar dianping_ratingBar = (RatingBar) view
				.findViewById(R.id.dianping_ratingBar);
		dianping_ratingBar.setEnabled(false);
		dianping_ratingBar.setMax(5);
		dianping_ratingBar.setProgress(MathUtil.stringToInt(item.SCORE));

		TextView dianping_username = (TextView) view
				.findViewById(R.id.dianping_username);
		dianping_username.setText(item.USER_NAME);

		TextView dianping_time = (TextView) view
				.findViewById(R.id.dianping_time);
		dianping_time.setText(item.CREATE_TIME);

		TextView dianping_pingjia = (TextView) view
				.findViewById(R.id.dianping_pingjia);
		dianping_pingjia.setText(item.CONTENT);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
				intent.putExtra(DianPingShaiDan.INTENT_KEY_ID,
						goodsDetailBeen.commodity.commodity.COMMODITY_ID);
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onClick(View view) {
		if (view == layout_baseinfo) {
			((GoodsDetailActivity) getActivity()).mViewPager.setCurrentItem(1);
		} else if (view == btn_add) {
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				getActivity().startActivity(intent);
				return;
			}
			// 加入购物车
			// ((GoodsDetailActivity) getActivity()).showMenu(0);
			GoodsDetailActivity.showMenuType = 0;

			addShoppingCar();
		} else if (view == btn_checkout) {
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				getActivity().startActivity(intent);
				return;
			}
			// 去结算
			((GoodsDetailActivity) getActivity()).showMenu(0);
			GoodsDetailActivity.showMenuType = 1;
		} else if (view == btn_shopping_car) {
			// 购物车
			AppContext.MAIN_TASK = AppContext.SHOPCAR; //标识切换界面的
			Intent i_main = new Intent(getActivity(), MainActivity.class);
			i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i_main);
			getActivity().finish();
		} else if (view == btn_order) {
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				getActivity().startActivity(intent);
				return;
			}
			BespeakRecord();
		} else if (view == layout_evaluation) {
			if (goodsDetailBeen.commodity.commoditySum <= 0) {
				Toast.makeText(getActivity(), "暂无评价", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
				intent.putExtra(DianPingShaiDan.INTENT_KEY_ID,
						goodsDetailBeen.commodity.commodity.COMMODITY_ID);
				startActivity(intent);
			}
		} else if (view == layout_shaidan) {
			// 商品晒单
			if (goodsDetailBeen.commodity.baskCommodtiy <= 0) {
				Toast.makeText(getActivity(), "暂无晒单", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
				intent.putExtra(DianPingShaiDan.INTENT_KEY_ID,
						goodsDetailBeen.commodity.commodity.COMMODITY_ID);
				intent.putExtra(DianPingShaiDan.INTENT_KEY_STATE, 1);
				startActivity(intent);
			}
		} else if (view == layout_kefu) {
			// 在线客服
			RongCloud.getInstance(getActivity()).startCustomerServiceChat();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		gallery_count.setText((position + 1) + "/" + listGallerys.size() + "张");
		for (int i = 0; i < indicatorList.size(); i++) {
			if (i == position) {
				indicatorList.get(i).setImageResource(
						R.drawable.page_indicator_focused);
			} else {
				indicatorList.get(i)
						.setImageResource(R.drawable.page_indicator);
			}
		}
	}

	/**
	 * 收藏商品
	 * 访问网络
	 * */
	private void collect(String userId) {
		CustomResponseHandler handler = new CustomResponseHandler(
				getActivity(), true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					collection++;
					collection_count.setText("（" + collection + "人）");
					Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT)
							.show();
					collection_checkbox.setChecked(false);
				}
			}
		};
		RequstClient.appCollect(userId,
				goodsDetailBeen.commodity.commodity.COMMODITY_ID, handler);
	}

	/**
	 * 取消收藏商品
	 * 
	 * */
	private void unCollect(String userId) {
		CustomResponseHandler handler = new CustomResponseHandler(
				getActivity(), true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					collection--;
					collection_count.setText("（" + collection + "人）");
					Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "取消收藏失败", Toast.LENGTH_SHORT)
							.show();
					collection_checkbox.setChecked(true);
				}
			}

		};
		RequstClient.appCancelCollect(userId,
				goodsDetailBeen.commodity.commodity.COMMODITY_ID, handler);
	}

	private long nowSecond = 0;
	private int flag = 0;
	private String titleString;

	protected void TextTask(final String starttime, final String endtime,
			final String curtime) {
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
			flag = 1;
			mCountDownTimer = new CountDownTimer(startSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int day = (int) (millisUntilFinished / 86400000);
					int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					String temp;
					if (day != 0) {
						temp = titleString + "开始：<font color=\"red\">" + day
								+ "</font>天<font color=\"red\">" + hour
								+ "</font>时<font color=\"red\">" + minute
								+ "</font>分<font color=\"red\">" + second
								+ "</font>秒";
					} else {
						temp = titleString + "开始：<font color=\"red\">" + hour
								+ "</font>时<font color=\"red\">" + minute
								+ "</font>分<font color=\"red\">" + second
								+ "</font>秒";
					}
					countTime.setText(Html.fromHtml(temp));
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
			flag = 0;
			mCountDownTimer = new CountDownTimer(endSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int day = (int) (millisUntilFinished / 86400000);
					int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					String temp;
					if (day != 0) {
						temp = titleString + "结束：<font color=\"red\">" + day
								+ "</font>天<font color=\"red\">" + hour
								+ "</font>时<font color=\"red\">" + minute
								+ "</font>分<font color=\"red\">" + second
								+ "</font>秒";
					} else {
						temp = titleString + "结束：<font color=\"red\">" + hour
								+ "</font>时<font color=\"red\">" + minute
								+ "</font>分<font color=\"red\">" + second
								+ "</font>秒";
					}
					countTime.setText(Html.fromHtml(temp));
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
			flag = 1;
			countTime.setText("活动结束");
		}

	}

	private void BespeakRecord() {
		if (flag == 1) {
			Toast.makeText(getActivity(), "不在预约时间内，不可预约！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		CustomResponseHandler handler = new CustomResponseHandler(
				getActivity(), true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type == 1) {
					// Toast.makeText(getActivity(), "预约成功", Toast.LENGTH_SHORT)
					// .show();
					btn_order.setText("已预约");
					btn_order
							.setBackgroundResource(R.drawable.common_btn_gray2_selector);
					btn_order.setEnabled(false);
					showBespeakDialog();
				} else {
					Toast.makeText(getActivity(), bean.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}

		};
		RequstClient.bespeakRecord(subscribeId, handler);
	}

	private void showBespeakDialog() {
		SingleConfirmDialog dialog = new SingleConfirmDialog(getActivity());
		dialog.setCustomTitle("预约成功");
		dialog.setMessage("恭喜您,预约成功!\n请在"
				+ goodsDetailBeen.commodity.commodity.END_TIME
				+ "后进行购买,我们将会以短信通知您。");
		dialog.setConfirm("确定");
		dialog.show();
	}

	/**
	 * 加入购物车
	 * 
	 * */
	private void addShoppingCar() {
		GoodDetials goodsDetail;
		if (goodsDetailBeen.commodity.goodsList != null
				&& goodsDetailBeen.commodity.goodsList.size() > 0) {
			goodsDetail = goodsDetailBeen.commodity.goodsList.get(0);
		} else {
			goodsDetail = new GoodsDetailBeen().new GoodDetials();
		}
		if (MathUtil.stringToInt(goodsDetailBeen.commodity.commodity.IS_ADDED) != 1) {
			Toast.makeText(getActivity(), "已下架", Toast.LENGTH_SHORT).show();
			return;
		}
		if (MathUtil.stringToInt(goodsDetailBeen.commodity.commodity.IS_ADDED) == 1
				&& MathUtil.stringToInt(goodsDetail.GOODS_STOCKS) <= 0) {
			Toast.makeText(getActivity(), "暂无库存", Toast.LENGTH_SHORT).show();
			return;
		}
		// LogUtil.i("info", goodsDetailBeen.commodity.commodity.QUOTA_FLAG+"");
		// 处理限购的部分
		if (goodsDetailBeen.commodity.commodity.QUOTA_FLAG == 3) {
			Toast.makeText(
					getActivity(),
					"该商品只能购买"
							+ goodsDetailBeen.commodity.commodity.QUOTA_NUMBER
							+ "次", Toast.LENGTH_SHORT).show();
			return;
		}
		// if (goodsDetailBeen.commodity.commodity.QUOTA_FLAG == 1) {
		// if (1 > goodsDetailBeen.commodity.commodity.QUOTA_QUANTITY) {
		// Toast.makeText(getActivity(), "该商品每次只能购买" +
		// goodsDetailBeen.commodity.commodity.QUOTA_QUANTITY + "件",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// }
		RequstClient.goodsDetailsShoppingCar(AppContext.getInstance().getUserId(),
				goodsDetailBeen.commodity.commodity.COMMODITY_ID,
				goodsDetail.CODE, "1", "1", "1", "", "",
				new CustomResponseHandler(getActivity()) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						UIHelper.cloesLoadDialog();

						LogUtil.i("rightFragment", "加入购物车结果" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String result = obj.getString("type");
							if ("1".equals(result)) {
								// 加入购物车
								Toast.makeText(getActivity(), "成功加入购物车",
										Toast.LENGTH_SHORT).show();
								int count = 0;
								if (obj.has("SHOPPING_CAR"))
									count = MathUtil.stringToInt(obj
											.getString("SHOPPING_CAR"));
								if (count <= 0) {
									tv_shopping_car_count
											.setVisibility(View.GONE);
								} else {
									if (count > 99) {
										tv_shopping_car_count.setText("99+");
									} else {
										tv_shopping_car_count.setText(count
												+ "");
									}
									tv_shopping_car_count
											.setVisibility(View.VISIBLE);
								}
								return;
							} else {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getActivity(), "" + errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	public int getCollection_checkbox() {
		return collection_checkbox.isChecked() ? 1 : 0;
	}
}
