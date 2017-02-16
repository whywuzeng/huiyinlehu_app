package com.huiyin.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.NewTodayListAdapter;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.TodayHeadLine;
import com.huiyin.bean.TodayHeadLine.Commodity;
import com.huiyin.bean.TodayHeadLine.HeadLineTitle;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class NewsTodayActivity extends BaseActivity implements OnClickListener, OnItemClickListener, ImageLoadingListener {

	public static final String INTENT_KEY_ID = "id";

	private TextView ab_back;
	private TextView ab_title, attention_goods;
	private ImageView ab_right_btn;

	private ScrollView refresh_scrollview;

	private ImageView mImageView1;
	private ImageView mImageView2;

	private LinearLayout spaid;
	private View h_line;

	private ListView mListView1;
	private ListView mListView2;

	private TodayHeadLine data;

	public ImageView btnLeft;// 上一页
	public ImageView btnRight;// 下一页

	public Context mContext;

	public int VIEW_COUNT = 5;


	public ArrayList<HeadLineTitle> newListDatas = new ArrayList<HeadLineTitle>();
	public List<Commodity> goodsListDatas = new ArrayList<Commodity>();

	public MoreAdapter moreAdapter;
	public NewTodayListAdapter goodsAdapter;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.news_today);

		id = getIntent().getIntExtra(INTENT_KEY_ID, 0);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();

		initView();
		requestData();
	}

	private void requestData() {

		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {
				data = TodayHeadLine.explainJson(content, mContext);
				if (data.type == 1) {

					boolean isHas = false;
					if (!StringUtils.isBlank(data.headlines.IMAGE)) {
						imageLoader.displayImage(URLs.IMAGE_URL + data.headlines.IMAGE, mImageView1, options,
								NewsTodayActivity.this);
						isHas = true;
					}

					if (!StringUtils.isBlank(data.headlines.CONTENT)) {
						imageLoader.displayImage(URLs.IMAGE_URL + data.headlines.CONTENT, mImageView2, options,
								NewsTodayActivity.this);
						isHas = true;
					}

					if (data.title != null) {
						if (newListDatas != null & newListDatas.size() > 0) {
							newListDatas.clear();
						}
						for (HeadLineTitle title : data.title) {
							newListDatas.add(title);
						}

						LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 10
								* DensityUtil.dip2px(mContext, 32));
						mListView2.setLayoutParams(params);
						//moreAdapter.notifyDataSetChanged();
						moreAdapter.refresh(newListDatas,AppContext.getInstance().newsTodayPage);

						checkButton();
					}
					if (data.commodity != null) {

						for (Commodity commodity : data.commodity) {
							goodsListDatas.add(commodity);

						}
						if (goodsListDatas != null && goodsListDatas.size() > 0) {
							attention_goods.setVisibility(View.VISIBLE);
							isHas = true;
						}
						LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, goodsListDatas.size()
								* DensityUtil.dip2px(mContext, 250));
						mListView1.setLayoutParams(params);

						goodsAdapter.notifyDataSetChanged();

					}

					h_line.setVisibility(isHas ? View.VISIBLE : View.GONE);
					spaid.setVisibility(isHas ? View.VISIBLE : View.GONE);

					refresh_scrollview.post(new Runnable() {

						@Override
						public void run() {
							refresh_scrollview.fullScroll(ScrollView.FOCUS_UP);
						}
					});

				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};

		if (id == 0) {
			RequstClient.appHeadlines(handler);
		} else {
			RequstClient.appHeadlines2(handler, id + "");
		}

	}

	@SuppressLint({ "SimpleDateFormat", "SetJavaScriptEnabled" })
	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("今日头条");

		refresh_scrollview = (ScrollView) findViewById(R.id.refresh_scrollview);

		h_line = (View) findViewById(R.id.h_line);
		spaid = (LinearLayout) findViewById(R.id.spaid);
		ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
		attention_goods = (TextView) findViewById(R.id.attention_goods);
		mImageView1 = (ImageView) findViewById(R.id.image1);
		mImageView2 = (ImageView) findViewById(R.id.image2);

		mListView1 = (ListView) findViewById(R.id.mListView1);
		mListView2 = (ListView) findViewById(R.id.mListView2);
		mListView2.setOnItemClickListener(this);

		btnLeft = (ImageView) findViewById(R.id.btn_pre);
		btnRight = (ImageView) findViewById(R.id.btn_next);

//		btnLeft.setVisibility(View.GONE);
//		btnRight.setVisibility(View.GONE);

		ab_back.setOnClickListener(this);
		ab_right_btn.setOnClickListener(this);

		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);

		// 检查2个Button是否是可用的
		checkButton();

		// 设置ListView的Adapter
		goodsAdapter = new NewTodayListAdapter(this, goodsListDatas);
		mListView1.setAdapter(goodsAdapter);

		mListView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getApplicationContext(), com.huiyin.ui.classic.GoodsDetailActivity.class);
				intent.putExtra("goods_detail_id", goodsListDatas.get(position).ID + "");
				startActivity(intent);
			}
		});
		
		 

		moreAdapter = new MoreAdapter(this);
		mListView2.setAdapter(moreAdapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ab_back:
			this.finish();
			break;
		case R.id.ab_right_btn:
			Toast.makeText(mContext, "分享...", 0).show();
			break;
		case R.id.ib_back:
			leftView();
			break;
		case R.id.ib_next:
			rightView();
			break;
		case R.id.btn_pre:
			leftView();
			break;
		case R.id.btn_next:
			rightView();
			break;

		default:
			break;
		}
	}

	// 点击左边的Button，表示向前翻页，索引值要减1.
	public void leftView() {
		if((AppContext.getInstance().newsTodayPage - 1) > -1){
			AppContext.getInstance().newsTodayPage = AppContext.getInstance().newsTodayPage - 1;
			moreAdapter.refresh(newListDatas,AppContext.getInstance().newsTodayPage);
		}
	}

	// 点击右边的Button，表示向后翻页，索引值要加1.
	public void rightView() {
		int pageNo = (int) Math.ceil(newListDatas.size() / 10.0f);
		if((AppContext.getInstance().newsTodayPage + 1) <= (pageNo-1)){
			AppContext.getInstance().newsTodayPage = AppContext.getInstance().newsTodayPage + 1;
			moreAdapter.refresh(newListDatas,AppContext.getInstance().newsTodayPage);
		}else{
			Toast.makeText(mContext, "没有更多了", Toast.LENGTH_LONG).show();
		}
	}

	public void checkButton() {

	}

	// ListView的Adapter，这个是关键的导致可以分页的根本原因。
	private class MoreAdapter extends BaseAdapter {
		private Context ct;
		private LayoutInflater inflater = null;
		private ArrayList<HeadLineTitle> data = new ArrayList<HeadLineTitle>();

		public MoreAdapter(Context context) {
			this.ct = context;
			inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		
		private void pagination(List<HeadLineTitle> datas,int page){
	        data = new ArrayList<HeadLineTitle>();
			int i = page * 10;
			int iEnd = i + 10;
			while ((i < datas.size()) && (i < iEnd)) {
				data.add(datas.get(i));
				i++;
			}
		}
		public void refresh(ArrayList<HeadLineTitle> arrayList,int page) {
			this.data.clear();
			this.data = null;
			pagination(arrayList,page);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// return data.size() < 5 ? data.size() : 5;
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler hodler = null;
			if (convertView == null) {
				hodler = new ViewHodler();
				convertView = inflater.inflate(R.layout.news_list_item1, null);
				hodler.tv = (TextView) convertView.findViewById(R.id.tv_topline_list_title);
				convertView.setTag(hodler);
			} else {
				hodler = (ViewHodler) convertView.getTag();
			}

			final HeadLineTitle bean = data.get(position);
			hodler.tv.setText(bean.TIME + " " + bean.TITLE);
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					id = bean.ID;
					Intent intent = new Intent();
					intent.setClass(mContext, NewsTodayActivity.class);
					intent.putExtra(INTENT_KEY_ID, id);
					startActivity(intent);
					finish();
				}
			});

			return convertView;
		}

		class ViewHodler {
			TextView tv;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		HeadLineTitle bean = newListDatas.get(position);
//		id = bean.ID;
//
//		Intent intent = new Intent();
//		intent.setClass(mContext, NewsTodayActivity.class);
//		intent.putExtra(INTENT_KEY_ID, id);
//		startActivity(intent);
//		finish();

	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {

	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		int width = DensityUtil.getScreenWidth(NewsTodayActivity.this);
		int height = width * loadedImage.getHeight() / loadedImage.getWidth();
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(width, height);
		view.setLayoutParams(params);

	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {

	}
}
