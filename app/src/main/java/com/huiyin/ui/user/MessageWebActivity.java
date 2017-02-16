package com.huiyin.ui.user;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.flash.FlashPrefectureActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.seckill.SeckillActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.utils.StringUtils;

public class MessageWebActivity extends BaseActivity {

	private WebView webView;
	private ProgressBar progressBar;

	TextView left_ib, middle_title_tv;

	private String htmlContent;
	private String pushFlag;
	private int url_type;
	private int id;
	private int commodityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messageweb);
		initViews();

		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		htmlContent = getIntent().getStringExtra("htmlContent");
		url_type = getIntent().getIntExtra("url_type", 0);
		id = getIntent().getIntExtra("id", 0);
		commodityId = getIntent().getIntExtra("commodityId", 0);

		String data = StringUtils.replaceImgSrc(htmlContent, URLs.IMAGE_URL);
		webView.loadDataWithBaseURL(null, StringUtils.formatHtml(data), "text/html", "utf-8", null);
	}

	@SuppressLint("JavascriptInterface")
	private void initViews() {
		webView = (WebView) findViewById(R.id.web);
		progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
		progressBar.setVisibility(View.VISIBLE);

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("消息内容");

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (pushFlag.equals("1")) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MyMessageActivity.class);
					startActivity(i);
				} else {
					finish();
				}
			}
		});

		WebSettings settings = webView.getSettings();
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(false);
		webView.setBackgroundResource(R.color.transparent);
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
		webView.setWebViewClient(new MyWebViewClient());

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			addImageClickListner();
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			progressBar.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress != 100) {
				progressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
		webView.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\");" + "var imgurl=''; "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "objs[i].index=i; " // 添加索引值
				+ "imgurl+=objs[i].src+',';" + "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage();  " + "    }  " + "}" + "})()");

	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage() {
			if (url_type != 0) {
				switch (url_type) {
				case 1:
					judgeSeckill(id);
					break;
				case 2:
					Intent intent = new Intent(mContext, FlashPrefectureActivity.class);
					intent.putExtra("id", id);
					startActivity(intent);
					break;
				case 3:
					Intent intent1 = new Intent(mContext, GoodsDetailActivity.class);
					intent1.putExtra("goods_detail_id", commodityId + "");
					intent1.putExtra("subscribeId", id);
					startActivity(intent1);
					break;
				case 4:
					Intent intent2 = new Intent(mContext, ZhuanQuActivity.class);
					intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID, id + "");
					intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
					startActivity(intent2);
					break;
				case 5:
					connt();
					break;
				case 6:
					Intent intent6 = new Intent(context, ZhuanQuActivity.class);
					intent6.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
					intent6.putExtra(ZhuanQuActivity.INTENT_KEY_ID, id + "");
					intent6.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
					context.startActivity(intent6);
					break;
				case 7:
					Intent intent5 = new Intent(context, GoodsDetailActivity.class);
					intent5.putExtra("goods_detail_id", id + "");
					context.startActivity(intent5);
					break;
				}

			}

		}

	}

	public void connt() {
		switch (id) {
		case 1:
			// 今日头条
			startActivity(new Intent(mContext, NewsTodayActivity.class));
			break;
		case 2:
			// 乐虎彩票
			startActivity(new Intent(mContext, LotteryActivity.class));
			break;
		case 3:
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(mContext, LoginActivity.class));
			} else {
				// 预约服务
				startActivity(new Intent(mContext, YuYueShenQingActivity.class));
			}
			break;
		case 4:
			// 物流查询
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(mContext, LoginActivity.class));
			} else {
				startActivity(new Intent(mContext, LogisticsQueryActivity.class));
			}
			break;
		case 5:
			// 智慧管家
			AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
			Intent i = new Intent();
			i.setClass(mContext, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			break;
		case 6:
			// 秀场
			startActivity(new Intent(mContext, TheShowActivity.class));
			break;
		case 7:
			// 积分club
			Intent intent = new Intent(mContext, ClubActivity.class);
			intent.putExtra(ClubActivity.INTENT_KEY_TITLE, "积分club");
			startActivity(intent);
			break;
		case 8:
			// 客户中心
			startActivity(new Intent(mContext, KefuCenterActivity.class));
			break;
		case 9:
			startActivity(new Intent(mContext, NearTheShopActivityNew.class));
			break;
		case 10:
			// 服务卡
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(mContext, LoginActivity.class));
			} else {
				Intent fuwu_intent = new Intent();
				if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
					fuwu_intent.setClass(mContext, ServiceCardActivity.class);
				} else {
					fuwu_intent.setClass(mContext, BindServiceCard.class);
				}
				startActivity(fuwu_intent);
			}
			break;
		default:
			break;
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
						Toast.makeText(MessageWebActivity.this, "秒杀已过期!", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(MessageWebActivity.this, SeckillActivity.class);
					intent.putExtra("id", id);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
