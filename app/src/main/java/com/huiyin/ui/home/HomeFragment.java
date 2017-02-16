package com.huiyin.ui.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.GalleryViewflowAdapter;
import com.huiyin.adapter.LnkToolsGridViewAdapter;
import com.huiyin.adapter.LnkToolsGridViewAdapter.BackItemClickListener;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.ChannelItem;
import com.huiyin.bean.GalleryAd;
import com.huiyin.bean.HomeBean;
import com.huiyin.bean.HomeFlashBean;
import com.huiyin.bean.HomePoly;
import com.huiyin.bean.HomeSeckillBean;
import com.huiyin.bean.HomeSeckillListItemBean;
import com.huiyin.bean.ProductBespeakBean;
import com.huiyin.bean.SalesPromotion;
import com.huiyin.bean.TopList;
import com.huiyin.db.LnkToolsDao;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.classic.SaleRankActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.home.ClassificationView.CassificationViewCoallBack;
import com.huiyin.ui.home.CuXiaoHuodongView.CuXiaoHuodongViewCoallBack;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.ui.user.KefuCenterActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.YuYueShenQingActivity;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.NetworkUtils;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.huiyin.wight.PageControlView;
import com.huiyin.wight.ScrollLayout;
import com.huiyin.wight.ScrollLayout.OnScreenChangeListenerDataLoad;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.Mode;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.huiyin.wight.pulltorefresh.PullToRefreshScrollView;
import com.huiyin.wight.viewflow.CircleFlowIndicator;
import com.huiyin.wight.viewflow.ViewFlow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxing.scan.activity.ZxingCodeActivity;

public class HomeFragment extends Fragment implements OnClickListener, OnItemClickListener, OnRefreshListener2,
		BackItemClickListener {

	private PullToRefreshScrollView scrollview;
	boolean requestFlag, requestPolyFlag;

	// 广告轮换图
	private ViewFlow viewFlow;
	private CircleFlowIndicator indic;
	private GalleryViewflowAdapter galleryViewflowAdapter;

	private HomeBean homeBean;// 整个主页的实体类

	private List<GalleryAd> listGalleryAds;

	// private GridView mGridView;

	// private LnkToolsGridViewAdapter toolsGridViewAdapter;
	private ScrollLayout mScrollLayout;
	private PageControlView pageControl;
	private List<ChannelItem> listChannelItems;
	private DataLoading dataLoad;
	
	private ImageView fastImg;

	// 销量排行榜
	private ImageView list_top;

	private ImageLoader imageLoader;

	private LinearLayout layout_seckill;

	private LinearLayout layout_cuxiao;

	// private CuXiaoHuodongView layout_prommotion1;
	// private CuXiaoHuodongView layout_prommotion2;
	// private CuXiaoHuodongView layout_prommotion3;
	// private CuXiaoHuodongView layout_prommotion4;

	// 分类聚合
	private LinearLayout home_classification_linlayout;

	private View ab_search;
	private ImageView activity_index_code_scan;

	private LnkToolsDao lnkToolsDao;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_home, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		requestData();// 请求主页数据

		requestPolyData();// 请求分类聚合的数据

		initData();

		initViews(view);

		dataLoad = new DataLoading();

		String indexCache = AppContext.getInstance().getCacheAppindexFirstData();
		if (indexCache != null && !indexCache.equals("")) {
			analyticalHomeData(indexCache, true);
		}
		String polyindexCache = AppContext.getInstance().getCacheAppIndexPolyFirstData();
		if (polyindexCache != null && !polyindexCache.equals("")) {
			analyticalPolyData(polyindexCache, true);
		}

		return view;
	}

	private void initData() {
		lnkToolsDao = new LnkToolsDao();

		homeBean = new HomeBean();
		listGalleryAds = new ArrayList<GalleryAd>();
		listChannelItems = new ArrayList<ChannelItem>();

		imageLoader = ImageLoader.getInstance();
	}

	private void initViews(View view) {

		scrollview = (PullToRefreshScrollView) view.findViewById(R.id.home_body_scrollview);
		scrollview.setMode(Mode.PULL_FROM_START);
		scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		scrollview.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		scrollview.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		scrollview.setOnRefreshListener(this);

		viewFlow = (ViewFlow) view.findViewById(R.id.mHomeViewflow);// 获得viewFlow对象
		indic = (CircleFlowIndicator) view.findViewById(R.id.mHomeViewflowindic); // viewFlow下的indic
		indic.setVisibility(View.GONE);

		// mGridView = (GridView) view.findViewById(R.id.mHomeGridView);
		// toolsGridViewAdapter = new LnkToolsGridViewAdapter(getActivity(),
		// listChannelItems);
		// mGridView.setAdapter(toolsGridViewAdapter);

		// mGridView.setOnItemClickListener(this);

		mScrollLayout = (ScrollLayout) view.findViewById(R.id.ScrollLayoutTest);
		
		fastImg = (ImageView) view.findViewById(R.id.fastImg);

		list_top = (ImageView) view.findViewById(R.id.list_top);

		layout_seckill = (LinearLayout) view.findViewById(R.id.layout_seckill);
		layout_cuxiao = (LinearLayout) view.findViewById(R.id.layout_cuxiao);

		// layout_prommotion1 = (CuXiaoHuodongView) view
		// .findViewById(R.id.layout_prommotion1);
		// layout_prommotion2 = (CuXiaoHuodongView) view
		// .findViewById(R.id.layout_prommotion2);
		// layout_prommotion3 = (CuXiaoHuodongView) view
		// .findViewById(R.id.layout_prommotion3);
		// layout_prommotion4 = (CuXiaoHuodongView) view
		// .findViewById(R.id.layout_prommotion4);

		list_top.setOnClickListener(this);

		home_classification_linlayout = (LinearLayout) view.findViewById(R.id.home_classification_linlayout);

		ab_search = view.findViewById(R.id.ab_search);

		ab_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(getActivity(), SiteSearchActivity.class);
				i.putExtra("content", "");
				startActivity(i);
			}
		});

		activity_index_code_scan = (ImageView) view.findViewById(R.id.activity_index_code_scan);
		activity_index_code_scan.setOnClickListener(this);
	}

	/***
	 * 首页数据（除分类聚合的数据）
	 * */
	private void requestData() {
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), false) {

			@Override
			public void onFinish() {
				super.onFinish();
				requestFlag = true;
				if (requestFlag && requestPolyFlag)
					scrollview.onRefreshComplete();
			}

			@Override
			public void onRefreshData(String content) {
				scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
				layout_seckill.removeAllViews();
				layout_cuxiao.removeAllViews();
				analyticalHomeData(content, false);// 解析数据
			}
		};
		RequstClient.appIndexFirst(handler);
	}

	/***
	 * 分类聚合的数据
	 * */
	private void requestPolyData() {
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), false) {
			@Override
			public void onFinish() {
				super.onFinish();
				requestPolyFlag = true;
				if (requestFlag && requestPolyFlag)
					scrollview.onRefreshComplete();
			}

			@Override
			public void onRefreshData(String content) {
				home_classification_linlayout.removeAllViews();
				analyticalPolyData(content, false); //解析数据
			}
		};
		RequstClient.appIndexPoly(handler);
	}

	/**
	 * 解析首页数据（除分类聚合）
	 * 
	 * @param content
	 * 
	 *            网络返回的数据
	 * */
	private void analyticalHomeData(String content, boolean isCache) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONArray bannerArrays = roots.getJSONArray("bannerList");
				//滚的图片
				List<GalleryAd> listGalleryAds = new ArrayList<GalleryAd>();
				for (int i = 0; i < bannerArrays.length(); i++) {
					//多条数据 循环解析
					JSONObject obj = bannerArrays.getJSONObject(i);
					GalleryAd bean = new GalleryAd();
					if (obj.has("BANNER_IMG"))
						bean.setImageUrl(URLs.IMAGE_URL + obj.getString("BANNER_IMG"));
					if (obj.has("BANNER_JUMP_FLAG"))
						bean.setFlag(obj.getInt("BANNER_JUMP_FLAG"));
					if (bean.getFlag() != 1 && obj.has("BANNER_JUMP_ID"))
						bean.setRowId(obj.getInt("BANNER_JUMP_ID"));
					if (bean.getFlag() == 1 && obj.has("ID"))
						bean.setHuodongId(obj.getInt("ID"));

					listGalleryAds.add(bean);
				}

				List<ChannelItem> listChannelItems = new ArrayList<ChannelItem>();
				JSONArray channelArrays = roots.getJSONArray("fastList");
				for (int i = 0; i < channelArrays.length(); i++) {
					JSONObject obj = channelArrays.getJSONObject(i);
					ChannelItem bean = new ChannelItem();
					bean.setImageUrl(URLs.IMAGE_URL + obj.getString("FAST_IMG"));
					bean.setName(obj.getString("FAST_NAME"));
					bean.setChannelId(obj.getInt("ID"));
					listChannelItems.add(bean);
				}

				homeBean.setListGallerys(listGalleryAds);
				homeBean.setListChannelItems(listChannelItems);
				
				if (roots.has("fastImg")
						&& !StringUtils.isEmpty(roots.getString("fastImg"))) {
					homeBean.setFastImg(URLs.IMAGE_URL
							+ roots.getString("fastImg"));
				} else {
					homeBean.setFastImg(null);
				}

				// 促销排行版
				List<TopList> listTopLists = new ArrayList<TopList>();
				JSONArray chartpositionArrays = roots.getJSONArray("chartpositionList");
				for (int i = 0; i < chartpositionArrays.length(); i++) {
					JSONObject obj = chartpositionArrays.getJSONObject(i);
					TopList bean = new TopList();
					bean.setImageUrl(URLs.IMAGE_URL + obj.getString("IMG"));
					// bean.setRowId(obj.getString("LINK_ID"));
					// bean.setFlag(obj.getInt("LINK_FLAG"));
					listTopLists.add(bean);
				}
				JSONArray prommotionObj = roots.getJSONArray("prommotionLayout");
				//添加对象
				List<SalesPromotion> listSalesPromotions = new ArrayList<SalesPromotion>();
				for (int i = 0; i < prommotionObj.length(); i++) {
					JSONObject obj = prommotionObj.getJSONObject(i);
					SalesPromotion salesPromotion = new SalesPromotion();
					salesPromotion.setId(obj.getString("id"));
					salesPromotion.setPromotion_name(obj.getString("promotion_name"));
					salesPromotion.setPromotion_adv(obj.getString("promotion_adv"));
					salesPromotion.setLayoutType(obj.getInt("promotion_layout"));
					JSONArray detailObj = obj.getJSONArray("detail");
					//里面都是图片
					if (salesPromotion.getLayoutType() == 1 || salesPromotion.getLayoutType() == 3) {
						salesPromotion.setImageUrl1(detailObj.getJSONObject(0).getString("IMG1"));
						salesPromotion.setImageUrl2(detailObj.getJSONObject(1).getString("IMG2"));
						salesPromotion.setImageUrl3(detailObj.getJSONObject(2).getString("IMG3"));
						// salesPromotion.setUrl1(obj.getString("IMG1URL"));
						// salesPromotion.setUrl2(obj.getString("IMG2URL"));
						// salesPromotion.setUrl3(obj.getString("IMG3URL"));
						salesPromotion.setImage1Id(detailObj.getJSONObject(0).getString("COMMODITY_ID1"));//COMMODITY 商品的id
						salesPromotion.setImage2Id(detailObj.getJSONObject(1).getString("COMMODITY_ID2"));
						salesPromotion.setImage3Id(detailObj.getJSONObject(2).getString("COMMODITY_ID3"));

					} else if (salesPromotion.getLayoutType() == 2) {
						salesPromotion.setImageUrl1(detailObj.getJSONObject(0).getString("IMG1"));
						salesPromotion.setImageUrl2(detailObj.getJSONObject(1).getString("IMG2"));
						// salesPromotion.setUrl1(obj.getString("IMG1URL"));
						// salesPromotion.setUrl2(obj.getString("IMG2URL"));
						salesPromotion.setImage1Id(detailObj.getJSONObject(0).getString("COMMODITY_ID1"));
						salesPromotion.setImage2Id(detailObj.getJSONObject(1).getString("COMMODITY_ID2"));
					} else if (salesPromotion.getLayoutType() == 5) {
						salesPromotion.setImageUrl1(detailObj.getJSONObject(0).getString("IMG1"));
						salesPromotion.setImageUrl2(detailObj.getJSONObject(1).getString("IMG2"));
						salesPromotion.setImageUrl3(detailObj.getJSONObject(2).getString("IMG3"));
						salesPromotion.setImageUrl4(detailObj.getJSONObject(3).getString("IMG4"));
						// salesPromotion.setUrl1(obj.getString("IMG1URL"));
						// salesPromotion.setUrl2(obj.getString("IMG2URL"));
						salesPromotion.setImage1Id(detailObj.getJSONObject(0).getString("COMMODITY_ID1"));
						salesPromotion.setImage2Id(detailObj.getJSONObject(1).getString("COMMODITY_ID2"));
						salesPromotion.setImage3Id(detailObj.getJSONObject(2).getString("COMMODITY_ID3"));
						salesPromotion.setImage4Id(detailObj.getJSONObject(3).getString("COMMODITY_ID4"));
					} else if (salesPromotion.getLayoutType() == 4) {
						continue;
						// salesPromotion.setKEY1(obj.getString("KEY1"));
						// salesPromotion.setKEY2(obj.getString("KEY2"));
						// salesPromotion.setKEY3(obj.getString("KEY3"));
						// salesPromotion.setKEY4(obj.getString("KEY4"));
						// salesPromotion.setKEY5(obj.getString("KEY5"));
						// salesPromotion.setKEY6(obj.getString("KEY6"));
						// salesPromotion.setKEY7(obj.getString("KEY7"));
						// salesPromotion.setKEY8(obj.getString("KEY8"));
						// salesPromotion.setKEY9(obj.getString("KEY9"));
						// salesPromotion.setKEY10(obj.getString("KEY10"));
						// salesPromotion.setKEY11(obj.getString("KEY11"));
						// salesPromotion.setKEY12(obj.getString("KEY12"));
						//
						// salesPromotion.setKEY1URL(obj.getString("KEY1URL"));
						// salesPromotion.setKEY2URL(obj.getString("KEY2URL"));
						// salesPromotion.setKEY3URL(obj.getString("KEY3URL"));
						// salesPromotion.setKEY4URL(obj.getString("KEY4URL"));
						// salesPromotion.setKEY5URL(obj.getString("KEY5URL"));
						// salesPromotion.setKEY6URL(obj.getString("KEY6URL"));
						// salesPromotion.setKEY7URL(obj.getString("KEY7URL"));
						// salesPromotion.setKEY8URL(obj.getString("KEY8URL"));
						// salesPromotion.setKEY9URL(obj.getString("KEY9URL"));
						// salesPromotion.setKEY10URL(obj.getString("KEY10URL"));
						// salesPromotion.setKEY11URL(obj.getString("KEY11URL"));
						// salesPromotion.setKEY12URL(obj.getString("KEY12URL"));
						//
						// salesPromotion.setImageUrl1(obj.getString("IMG1"));
						// salesPromotion.setImageUrl2(obj.getString("IMG2"));
						// salesPromotion.setUrl1(obj.getString("IMG1URL"));
						// salesPromotion.setUrl2(obj.getString("IMG2URL"));
						// salesPromotion.setImage1Id(obj.getString("IMG1ID"));
						// salesPromotion.setImage2Id(obj.getString("IMG2ID"));
						//
						// salesPromotion.setDETAIL1ID(obj.getString("DETAIL1ID"));
						// salesPromotion.setDETAIL2ID(obj.getString("DETAIL2ID"));
						// salesPromotion.setDETAIL3ID(obj.getString("DETAIL3ID"));
						// salesPromotion.setDETAIL4ID(obj.getString("DETAIL4ID"));
						// salesPromotion.setDETAIL5ID(obj.getString("DETAIL5ID"));
						// salesPromotion.setDETAIL6ID(obj.getString("DETAIL6ID"));
						// salesPromotion.setDETAIL7ID(obj.getString("DETAIL7ID"));
						// salesPromotion.setDETAIL8ID(obj.getString("DETAIL8ID"));
						// salesPromotion.setDETAIL9ID(obj.getString("DETAIL9ID"));
						// salesPromotion.setDETAIL10ID(obj
						// .getString("DETAIL10ID"));
						// salesPromotion.setDETAIL11ID(obj
						// .getString("DETAIL11ID"));
						// salesPromotion.setDETAIL12ID(obj
						// .getString("DETAIL12ID"));
					}
					Log.i("", "aaa>>>>>d1>>>" + isCache);
					listSalesPromotions.add(salesPromotion);
					Log.i("", "aaa>>>>>d2>>>" + isCache);
				}
				Log.i("", "aaa>>>>>d3>>>" + isCache);
				homeBean.setListTopLists(listTopLists);

				homeBean.setListSalesPromotions(listSalesPromotions);
				Log.i("", "aaa>>>>>d4>>>" + isCache);
				if (!isCache) {
					//如果没缓存,有15个布局
					Log.i("", "aaa>>>>>dddddd>>>如果没缓存,有15个布局" + homeBean.getListSalesPromotions());
				}
				Log.i("", "aaa>>>>>d5>>>" + isCache);
				if (roots.has("hotline")) {
					PreferenceUtil.getInstance(getActivity()).setHotLine(roots.getString("hotline"));
				}

				// 秒杀 英语要过关啊
				if (!isCache && roots.has("seckillList") && !roots.isNull("seckillList")) {
					HomeSeckillBean mHomeSeckillBean = new HomeSeckillBean();
					JSONObject obj = roots.getJSONObject("seckillList");
					if (!obj.isNull("ID"))
						mHomeSeckillBean.setID(MathUtil.stringToInt(obj.getString("ID")));
					if (!obj.isNull("END_TIME"))
						mHomeSeckillBean.setEND_TIME(obj.getString("END_TIME"));
					if (!obj.isNull("SECKILL_NAME"))
						mHomeSeckillBean.setSECKILL_NAME(obj.getString("SECKILL_NAME"));
					if (!obj.isNull("START_TIME"))
						mHomeSeckillBean.setSTART_TIME(obj.getString("START_TIME"));
					if (!obj.isNull("curDate"))
						mHomeSeckillBean.setCurDate(obj.getString("curDate"));
					if (!obj.isNull("COMMODITY_LIST")) {
						JSONArray array = obj.getJSONArray("COMMODITY_LIST");
						ArrayList<HomeSeckillListItemBean> mList = new ArrayList<HomeSeckillListItemBean>();
						for (int i = 0; i < array.length(); i++) {
							HomeSeckillListItemBean mBean = new HomeSeckillListItemBean();
							JSONObject obj1 = array.getJSONObject(i);
							if (!obj1.isNull("PRICE"))
								mBean.setPRICE(Float.valueOf(obj1.getString("PRICE")));
							if (!obj1.isNull("COMMODITY_ID"))
								mBean.setCOMMODITY_ID(Integer.valueOf(obj1.getString("COMMODITY_ID")));
							if (!obj1.isNull("COMMODITY_IMAGE_PATH"))
								mBean.setCOMMODITY_IMAGE_PATH(obj1.getString("COMMODITY_IMAGE_PATH"));
							if (!obj1.isNull("NUM"))
								mBean.setNUM(Integer.valueOf(obj1.getString("NUM")));
							if (!obj1.isNull("COMMODITY_NAME"))
								mBean.setCOMMODITY_NAME(obj1.getString("COMMODITY_NAME"));
							if (!obj1.isNull("DISCOUNT"))
								mBean.setDISCOUNT(Float.valueOf(obj1.getString("DISCOUNT")));
							mList.add(mBean);
						}
						mHomeSeckillBean.setCOMMODITY_LIST(mList);
					}
					homeBean.setSeckillList(mHomeSeckillBean);
				} else {
					homeBean.setSeckillList(null);
				}
				if (!isCache && roots.has("flashActive") && !roots.isNull("flashActive")
						&& roots.getJSONObject("flashActive").length() > 0) {
					HomeFlashBean mHomeFlsahBean = new HomeFlashBean();
					JSONObject obj = roots.getJSONObject("flashActive");
					if (!obj.isNull("ADVER"))
						mHomeFlsahBean.setADVER(obj.getString("ADVER"));
					if (!obj.isNull("START_TIME"))
						mHomeFlsahBean.setSTART_TIME(obj.getString("START_TIME"));
					if (!obj.isNull("NUM"))
						mHomeFlsahBean.setNUM(MathUtil.stringToInt(obj.getString("NUM")));
					if (!obj.isNull("ACTIVE_NAME"))
						mHomeFlsahBean.setACTIVE_NAME(obj.getString("ACTIVE_NAME"));
					if (!obj.isNull("PID1"))
						mHomeFlsahBean.setPID1(MathUtil.stringToInt(obj.getString("PID1")));
					if (!obj.isNull("IMG1"))
						mHomeFlsahBean.setIMG1(obj.getString("IMG1"));
					if (!obj.isNull("PID2"))
						mHomeFlsahBean.setPID2(MathUtil.stringToInt(obj.getString("PID2")));
					if (!obj.isNull("IMG2"))
						mHomeFlsahBean.setIMG2(obj.getString("IMG2"));
					if (!obj.isNull("PID3"))
						mHomeFlsahBean.setPID3(MathUtil.stringToInt(obj.getString("PID3")));
					if (!obj.isNull("IMG3"))
						mHomeFlsahBean.setIMG3(obj.getString("IMG3"));
					if (!obj.isNull("PID4"))
						mHomeFlsahBean.setPID4(MathUtil.stringToInt(obj.getString("PID4")));
					if (!obj.isNull("IMG4"))
						mHomeFlsahBean.setIMG4(obj.getString("IMG4"));
					homeBean.setmHomeFlashBean(mHomeFlsahBean);
				} else {
					homeBean.setmHomeFlashBean(null);
				}
				// 新品预约
				if (!isCache && roots.has("productBespeak") && !roots.isNull("productBespeak")
						&& roots.getJSONObject("productBespeak").length() > 0) {
					ProductBespeakBean productBespeakBean = new ProductBespeakBean();
					JSONObject obj = roots.getJSONObject("productBespeak");
					if (!obj.isNull("ADVER"))
						productBespeakBean.setADVER(obj.getString("ADVER"));
					if (!obj.isNull("ANAME"))
						productBespeakBean.setANAME(obj.getString("ANAME"));
					if (!obj.isNull("PID1"))
						productBespeakBean.setPID1(MathUtil.stringToInt(obj.getString("PID1")));
					if (!obj.isNull("CID1"))
						productBespeakBean.setCID1(MathUtil.stringToInt(obj.getString("CID1")));
					if (!obj.isNull("IMG1"))
						productBespeakBean.setIMG1(obj.getString("IMG1"));
					if (!obj.isNull("PID2"))
						productBespeakBean.setPID2(MathUtil.stringToInt(obj.getString("PID2")));
					if (!obj.isNull("CID2"))
						productBespeakBean.setCID2(MathUtil.stringToInt(obj.getString("CID2")));
					if (!obj.isNull("IMG2"))
						productBespeakBean.setIMG2(obj.getString("IMG2"));
					if (!obj.isNull("PID3"))
						productBespeakBean.setPID3(MathUtil.stringToInt(obj.getString("PID3")));
					if (!obj.isNull("CID3"))
						productBespeakBean.setCID3(MathUtil.stringToInt(obj.getString("CID3")));
					if (!obj.isNull("IMG3"))
						productBespeakBean.setIMG3(obj.getString("IMG3"));
					homeBean.setProductBespeakBean(productBespeakBean);
				} else {
					homeBean.setProductBespeakBean(null);
				}

				if (!isCache) {
					AppContext.getInstance().saveCacheAppindexFirstData(content);
					layout_cuxiao.removeAllViews();
					// layout_prommotion1.removeAllViews();
					// layout_prommotion2.removeAllViews();
					// layout_prommotion3.removeAllViews();
					// layout_prommotion4.removeAllViews();
				}

				loadHomeData();
			} else {
				layout_seckill.removeAllViews();
				layout_cuxiao.removeAllViews();
				String errorMsg = roots.getString("msg");
				Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析分类聚合
	 * 
	 * @param content
	 * 
	 *            网络返回的数据
	 * */
	private void analyticalPolyData(String content, boolean isCache) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONArray polyLists = roots.getJSONArray("polyList");
				List<HomePoly> listPolyLists = new ArrayList<HomePoly>();

				for (int i = 0; i < polyLists.length(); i++) {

					JSONObject obj = polyLists.getJSONObject(i);

					HomePoly bean = new HomePoly();
					bean.setTypeId(obj.getInt("POLY_TYPE_ID"));
					bean.setTypeImageUrl(obj.getString("POLY_TYPE_IMG"));
					bean.setName(obj.getString("POLY_TYPE_NAME"));

					JSONArray lists = obj.getJSONArray("polyList");
					List<HomePoly> listChilds = new ArrayList<HomePoly>();
					for (int j = 0; j < lists.length(); j++) {
						JSONObject objj = lists.getJSONObject(j);
						HomePoly object = new HomePoly();
						object.setTypeId(objj.getInt("POLY_TYPE_ID"));
						object.setImageUrl(objj.getString("POLY_IMG"));
						object.setId(objj.getInt("ID"));
						object.setLayout(objj.getInt("LAYOUT"));
						object.setName(objj.getString("POLY_NAME"));
						object.setLinkFlag(objj.getString("LINK_FLAG"));
						object.setLinkId(objj.getString("LINK_ID"));
						listChilds.add(object);
					}
					bean.setListPolies(listChilds);
					listPolyLists.add(bean);
				}

				homeBean.setListPolies(listPolyLists);
				initClassificationData(homeBean.getListPolies());

				if (!isCache) {
					AppContext.getInstance().saveCacheAppIndexPolyData(content);
				}
			} else {
				String errorMsg = roots.getString("msg");
				Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadHomeData() {
		listGalleryAds.clear();
		listGalleryAds.addAll(homeBean.getListGallerys());

		updateLnkTools();

		// 广告图
		galleryViewflowAdapter = new GalleryViewflowAdapter(getActivity(), listGalleryAds);
		if (listGalleryAds.size() < 1) {
			indic.setVisibility(View.GONE);
		} else {
			indic.setVisibility(View.VISIBLE);
			viewFlow.setAdapter(galleryViewflowAdapter); // 对viewFlow添加图片
			viewFlow.setmSideBuffer(listGalleryAds.size());
			viewFlow.setFlowIndicator(indic);
			viewFlow.setTimeSpan(5000);
			viewFlow.setSelection(listGalleryAds.size() * 10); // 设置初始位置
			viewFlow.stopAutoFlowTimer();
			if (listGalleryAds.size() <= 1) {
				// 图片大于1才有指示器
				indic.setVisibility(View.GONE);
			} else {
				viewFlow.startAutoFlowTimer(); // 启动自动播放
			}
		}
		
		// 销量排行榜          // Display Options的主要职责就是记录相关的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_gallery).showImageForEmptyUri(R.drawable.image_default_gallery)
				.showImageOnFail(R.drawable.image_default_gallery).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		if (homeBean.getListTopLists().size() > 0) {
			//加图片
			imageLoader.displayImage(homeBean.getListTopLists().get(0).getImageUrl(), list_top, options);
		}
		
		if (homeBean.getFastImg() != null) {
			//加图片
			imageLoader.displayImage(homeBean.getFastImg(), fastImg, options);
			fastImg.setVisibility(View.VISIBLE);
		} else {
			fastImg.setVisibility(View.GONE);
		}

		layout_seckill.removeAllViews();
		// 今日秒杀
		if (homeBean.getSeckillList() != null && homeBean.getSeckillList().getCOMMODITY_LIST() != null
				&& homeBean.getSeckillList().getCOMMODITY_LIST().size() > 0) {
			HomeSeckillBean temp = homeBean.getSeckillList();
			//判断是什么？
			SeckillView view = new SeckillView(getActivity(), temp);
			layout_seckill.addView(view);
			int num_count=layout_seckill.getChildCount();
			layout_seckill.setVisibility(View.VISIBLE);
		}

		// 新品预约
		if (homeBean.getProductBespeakBean() != null) {
			BespeakView view = new BespeakView(getActivity(), homeBean.getProductBespeakBean());
			layout_seckill.addView(view);
			layout_seckill.setVisibility(View.VISIBLE);
		}

		// 闪购
		if (homeBean.getmHomeFlashBean() != null) {
			FlashView view = new FlashView(getActivity(), homeBean.getmHomeFlashBean());
			layout_seckill.addView(view);
			layout_seckill.setVisibility(View.VISIBLE);
		}
		int num_count=layout_seckill.getChildCount();
		layout_cuxiao.removeAllViews();
		for (int i = 0; i < homeBean.getListSalesPromotions().size(); i++) {
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.prommotion_view, null);
			TextView promotion_name = (TextView) view.findViewById(R.id.promotion_name);
			TextView promotion_adv = (TextView) view.findViewById(R.id.promotion_adv);

			TextView promotion_more = (TextView) view.findViewById(R.id.promotion_more);

			CuXiaoHuodongView huodongView = (CuXiaoHuodongView) view.findViewById(R.id.layout_prommotion);
			final SalesPromotion promotion = homeBean.getListSalesPromotions().get(i);
			promotion_name.setText(promotion.getPromotion_name());
			promotion_adv.setText(promotion.getPromotion_adv());
			setPrommotion(huodongView, i);
			promotion_more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getActivity(), ZhuanQuActivity.class);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, promotion.getId());
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
					startActivity(intent);
				}
			});
			layout_cuxiao.addView(view);
			int childCount = layout_cuxiao.getChildCount();
		}

		// if (homeBean.getListSalesPromotions().size() >= 1) {
		// setPrommotion(layout_prommotion1, 0);
		// } else {
		// layout_prommotion1.setVisibility(View.GONE);
		// }
		//
		// if (homeBean.getListSalesPromotions().size() >= 2) {
		// setPrommotion(layout_prommotion2, 1);
		// } else {
		// layout_prommotion2.setVisibility(View.GONE);
		// }
		//
		// if (homeBean.getListSalesPromotions().size() >= 3) {
		// setPrommotion(layout_prommotion3, 2);
		// } else {
		// layout_prommotion3.setVisibility(View.GONE);
		// }
		//
		// if (homeBean.getListSalesPromotions().size() >= 4) {
		// setPrommotion(layout_prommotion4, 3);
		// } else {
		// layout_prommotion4.setVisibility(View.GONE);
		// }

	}

	private void setPrommotion(CuXiaoHuodongView view, final int position) {
		view.setCoallBack(new CuXiaoHuodongViewCoallBack() {

			// 促销活动的四种布局的各个条目的点击事件,返回一个id
			@Override
			public void onCuxiaoClick(int id) {

				SalesPromotion bean = homeBean.getListSalesPromotions().get(position);

				Log.i("", position + "bbbb>>>>>>>" + homeBean.getListSalesPromotions());
				// String layoutType = "";
				String _id = "";
				switch (id) {
				case 1:
					// layoutType = bean.getUrl1();
					_id = bean.getImage1Id();
					break;
				case 2:
					// layoutType = bean.getUrl2();
					_id = bean.getImage2Id();
					break;
				case 3:
					// layoutType = bean.getUrl3();
					_id = bean.getImage3Id();
					break;
				case 4:
					_id = bean.getImage4Id();
					break;
				}

				// if (layoutType.equals("1")) {
				// Intent intent = new Intent(getActivity(),
				// ZhuanQuActivity.class);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, _id);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, layoutType);
				// startActivity(intent);
				// } else if (layoutType.equals("2")) {
				// Intent intent = new Intent(getActivity(),
				// ZhuanQuActivity.class);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, _id);
				// intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, layoutType);
				// startActivity(intent);
				// } else if (layoutType.equals("3")) {
				Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
				//把物品的ID传过去
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, _id);
				getActivity().startActivity(intent);
				// }
			}

			// 第四种布局的条目的单击事件
			@Override
			public void onkeywordClick(String keyword, String layoutType, String _id) {
				Log.i("", "sdgsdsfasdf= =====" + _id);
				if (layoutType.equals("1")) {
					Intent intent = new Intent(getActivity(), ZhuanQuActivity.class);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, _id);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_NAME, keyword);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, layoutType);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_BARNER_ID, _id);
					startActivity(intent);
				} else if (layoutType.equals("2")) {
					Intent intent = new Intent(getActivity(), ZhuanQuActivity.class);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, _id);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_NAME, keyword);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, layoutType);

					intent.putExtra(ZhuanQuActivity.INTENT_KEY_BARNER_ID, _id);
					startActivity(intent);
				} else if (layoutType.equals("3")) {
					Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
					intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, _id);
					getActivity().startActivity(intent);
				}
			}
		});
		SalesPromotion salesPromotion = homeBean.getListSalesPromotions().get(position);
		view.setData(homeBean.getListSalesPromotions().get(position));
	}

	/**
	 * 填充分类的数据
	 * 
	 * @param array
	 */
	private void initClassificationData(List<HomePoly> array) {
		Cassification cassification;
		for (int i = 0; i < array.size(); i++) {
			HomePoly polys = array.get(i);
			List<HomePoly> polyList = polys.getListPolies();
			ArrayList<Cassification> arrayList = new ArrayList<Cassification>();
			for (HomePoly poly : polyList) {
				cassification = new Cassification();
				cassification.color = "#f4feef";
				cassification.iconPath = URLs.IMAGE_URL + poly.getImageUrl();
				cassification.name = poly.getName();
				cassification.id = poly.getId();
				cassification.layout = poly.getLayout();
				arrayList.add(cassification);
			}
			if (arrayList.size() > 0) {
				ClassificationView view = new ClassificationView(getActivity());
				view.setName(polys.getName());
				view.setData(arrayList);
				view.setCoallBack(mCassificationViewCoallBack);
				home_classification_linlayout.addView(view);
			}
		}
	}

	CassificationViewCoallBack mCassificationViewCoallBack = new CassificationViewCoallBack() {
		@Override
		public void onItemClick(Cassification arg0) {
			arg0.linkFlag = "1";
			switch (arg0.layout) {
			case 1:
				Intent intent = new Intent(getActivity(), ZhuanQuActivity.class);
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, arg0.id + "");
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_NAME, arg0.name + "");
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, arg0.linkFlag);
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_BARNER_ID, arg0.linkId);
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
				startActivity(intent);
				break;
			case 2:
				Intent intent1 = new Intent(getActivity(), ZhuanQuActivity.class);
				intent1.putExtra(ZhuanQuActivity.INTENT_KEY_ID, arg0.id + "");
				intent1.putExtra(ZhuanQuActivity.INTENT_KEY_NAME, arg0.name + "");
				intent1.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, arg0.linkFlag);
				intent1.putExtra(ZhuanQuActivity.INTENT_KEY_BARNER_ID, arg0.linkId);
				intent1.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
				startActivity(intent1);
				break;
			case 3:
				Intent intent2 = new Intent(getActivity(), ZhuanQuActivity.class);
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID, arg0.id + "");
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_NAME, arg0.name + "");
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, arg0.linkFlag);
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_BARNER_ID, arg0.linkId);
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
				startActivity(intent2);
				break;
			}

		}
	};

	@Override
	public void onClick(View view) {
		if (list_top == view) {
			// 销量排行榜
			homeBean.getListTopLists();
			if (homeBean.getListTopLists() != null && homeBean.getListTopLists().size() > 0) {
				Intent intent = new Intent(getActivity(), SaleRankActivity.class);
				startActivity(intent);
			}
		} else if (view == activity_index_code_scan) {
			
			startActivity(new Intent(getActivity(), ZxingCodeActivity.class));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if (position >= listChannelItems.size()) {
			// 点击的加号 要添加导航
			Intent intent = new Intent(getActivity(), LnkToolsActivity.class);
			intent.putParcelableArrayListExtra("selected_data", (ArrayList<? extends Parcelable>) listChannelItems);

			if (homeBean != null && homeBean.getListChannelItems() != null) {
				intent.putParcelableArrayListExtra("all_data", (ArrayList<? extends Parcelable>) homeBean.getListChannelItems());
			} else {
				List<ChannelItem> temps = new ArrayList<ChannelItem>();
				intent.putParcelableArrayListExtra("all_data", (ArrayList<? extends Parcelable>) temps);
			}

			startActivityForResult(intent, MainActivity.LNKTOOLS_REQUEST);
			//Activity的切换动画指的是从一个activity跳转到另外一个activity时的动画。
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else {
			ChannelItem bean = listChannelItems.get(position);
			switch (bean.getChannelId()) {
			case 1:
				// 今日头条
				startActivity(new Intent(getActivity(), NewsTodayActivity.class));
				break;
			case 2:
				// 乐虎彩票
				startActivity(new Intent(getActivity(), LotteryActivity.class));
				break;
			case 3:
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					// 预约服务
					startActivity(new Intent(getActivity(), YuYueShenQingActivity.class));
				}
				break;
			case 4:
				// 物流查询
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					startActivity(new Intent(getActivity(), LogisticsQueryActivity.class));
				}
				break;
			case 5:
				// 智慧管家
				AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
				Intent i = new Intent();
				i.setClass(getActivity(), MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(i);
				break;
			case 6:
				// 秀场
				startActivity(new Intent(getActivity(), TheShowActivity.class));
				break;
			case 7:
				// 积分club
				Intent intent = new Intent(getActivity(), ClubActivity.class);
				intent.putExtra(ClubActivity.INTENT_KEY_TITLE, bean.getName());
				startActivity(intent);
				break;
			case 8:
				// 客户中心
				startActivity(new Intent(getActivity(), KefuCenterActivity.class));
				break;
			case 9:
				startActivity(new Intent(getActivity(), NearTheShopActivityNew.class));
				break;
			case 10:
				// 服务卡
				// 物流查询
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					Intent fuwu_intent = new Intent();
					if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
						fuwu_intent.setClass(getActivity(), ServiceCardActivity.class);
					} else {
						fuwu_intent.setClass(getActivity(), BindServiceCard.class);
					}
					startActivity(fuwu_intent);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MainActivity.LNKTOOLS_REQUEST) {
			//添加的导航  跟新
			updateLnkTools();
		}
	}

	public void updateLnkTools() {
		List<ChannelItem> lists = new ArrayList<ChannelItem>();
		if (homeBean != null && homeBean.getListChannelItems() != null) {
			lists = homeBean.getListChannelItems();
		}

		listChannelItems.clear();

		List<ChannelItem> listDatas = lnkToolsDao.fetcheAll();// 数据库存取的包含了数据的顺序

		if (listDatas.size() <= 0 && PreferenceUtil.getInstance(getActivity()).isFirstSet()) {
			for (int i = 0; i < lists.size(); i++) {
				listChannelItems.add(lists.get(i));
			}
		} else {
			// for (ChannelItem channelItem : listDatas) {
			// for (ChannelItem bean : lists) {
			// // 服务器
			// if (channelItem.getChannelId() == bean.getChannelId()) {
			// listChannelItems.add(bean);
			// }
			// }
			// }
			for (int i = 0; i < listDatas.size(); i++) {
				for (ChannelItem bean : lists) {
					if (listDatas.get(i).getChannelId() == bean.getChannelId()) {
						listChannelItems.add(bean);
					}
				}
			}
		}

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(
				R.dimen.fragment_home_tools_height_half)
				* ((listChannelItems.size()) / 4 + 1));

		// mGridView.setLayoutParams(params);
		//
		// // 快捷工具
		// toolsGridViewAdapter.notifyDataSetChanged();

		mScrollLayout.removeAllViews();
		//初始化在第一页
		mScrollLayout.snapToScreen(0);
		//返回大于或者等于指定表达式的最小整数
		int pageNo = (int) Math.ceil(listChannelItems.size() / 7.0f);
		for (int i = 0; i < pageNo; i++) {
			GridView appPage = new GridView(getActivity());
			LnkToolsGridViewAdapter lnkToolsGridViewAdapter;
			lnkToolsGridViewAdapter = new LnkToolsGridViewAdapter(getActivity(), listChannelItems, i);
			appPage.setAdapter(lnkToolsGridViewAdapter);
			appPage.setNumColumns(4);
			lnkToolsGridViewAdapter.setBackItemClickListener(this);
			mScrollLayout.addView(appPage);
		}
		if(pageNo <= 0){
			GridView appPage = new GridView(getActivity());
			LnkToolsGridViewAdapter lnkToolsGridViewAdapter = new LnkToolsGridViewAdapter(getActivity());
			appPage.setAdapter(lnkToolsGridViewAdapter);
			appPage.setNumColumns(4);
			lnkToolsGridViewAdapter.setBackItemClickListener(this);
			mScrollLayout.addView(appPage);
		}
		
		// 加载分页  设置gridview下面的， 两个点导航
		pageControl = (PageControlView) view.findViewById(R.id.pageControl);
		pageControl.bindScrollViewGroup(mScrollLayout);
		if (pageNo > 1) {
			pageControl.setVisibility(View.VISIBLE);
		} else {
			pageControl.setVisibility(View.GONE);
		}
		// 加载分页数据
		dataLoad.bindScrollViewGroup(mScrollLayout);

	}

	// 分页数据
	class DataLoading {
		private int count;

		public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
			this.count = scrollViewGroup.getChildCount();
			scrollViewGroup.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
				public void onScreenChange(int currentIndex) {
				}
			});
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {

		if (!NetworkUtils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "网络故障，请先检查网络连接!", Toast.LENGTH_LONG).show();
		}

		requestFlag = false;
		requestPolyFlag = false;

		requestData();// 请求主页数据
		requestPolyData();// 请求分类聚合的数据
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void backitenClick(ChannelItem bean) {
		if (bean.getChannelId() >= 11) {
			// 点击的加号
			Intent intent = new Intent(getActivity(), LnkToolsActivity.class);
			intent.putParcelableArrayListExtra("selected_data", (ArrayList<? extends Parcelable>) listChannelItems);

			if (homeBean != null && homeBean.getListChannelItems() != null) {
				intent.putParcelableArrayListExtra("all_data", (ArrayList<? extends Parcelable>) homeBean.getListChannelItems());
			} else {
				List<ChannelItem> temps = new ArrayList<ChannelItem>();
				intent.putParcelableArrayListExtra("all_data", (ArrayList<? extends Parcelable>) temps);
			}

			startActivityForResult(intent, MainActivity.LNKTOOLS_REQUEST);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else {
			switch (bean.getChannelId()) {
			case 1:
				// 今日头条
				startActivity(new Intent(getActivity(), NewsTodayActivity.class));
				break;
			case 2:
				// 乐虎彩票
				startActivity(new Intent(getActivity(), LotteryActivity.class));
				break;
			case 3:
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					// 预约服务
					startActivity(new Intent(getActivity(), YuYueShenQingActivity.class));
				}
				break;
			case 4:
				// 物流查询
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					startActivity(new Intent(getActivity(), LogisticsQueryActivity.class));
				}
				break;
			case 5:
				// 智慧管家
				AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
				Intent i = new Intent();
				i.setClass(getActivity(), MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(i);
				break;
			case 6:
				// 秀场
				startActivity(new Intent(getActivity(), TheShowActivity.class));
				break;
			case 7:
				// 积分club
				Intent intent = new Intent(getActivity(), ClubActivity.class);
				intent.putExtra(ClubActivity.INTENT_KEY_TITLE, bean.getName());
				startActivity(intent);
				break;
			case 8:
				// 客户中心
				startActivity(new Intent(getActivity(), KefuCenterActivity.class));
				break;
			case 9:
				startActivity(new Intent(getActivity(), NearTheShopActivityNew.class));
				break;
			case 10:
				// 服务卡
				// 物流查询
				if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					Intent fuwu_intent = new Intent();
					if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
						fuwu_intent.setClass(getActivity(), ServiceCardActivity.class);
					} else {
						fuwu_intent.setClass(getActivity(), BindServiceCard.class);
					}
					startActivity(fuwu_intent);
				}
				break;
			default:
				break;
			}
		}
	}
}
