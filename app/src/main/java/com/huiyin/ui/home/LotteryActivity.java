package com.huiyin.ui.home;

import com.huiyin.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class LotteryActivity extends Activity {

	private WebView mWebView;

	private TextView ab_title;
	private TextView ab_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery);

		initViews();

		loadWebData();
	}

	public void initViews() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webSettings.setAllowContentAccess(false);
		webSettings.setLightTouchEnabled(false);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setNeedInitialFocus(true);
	
		mWebView.setWebViewClient(new WebViewClient() {
			// 这个方法在用户试图点开页面上的某个链接时被调用
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null) {
					// 如果想继续加载目标页面则调用下面的语句
					view.loadUrl(url);
					// 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
				}
				// 返回true表示停留在本WebView（不跳转到系统的浏览器）
				return true;
			}
		});

		ab_title = (TextView) findViewById(R.id.ab_title);
	}

	public void loadWebData() {
		mWebView.loadUrl("http://wap.cp.lehumall.com");
		ab_title.setText("乐虎彩票");
	}

}
