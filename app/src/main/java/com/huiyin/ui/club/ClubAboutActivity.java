package com.huiyin.ui.club;

import com.huiyin.R;
import com.huiyin.utils.WebViewUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ClubAboutActivity extends Activity implements OnClickListener {

	public static final String INTENT_KEY_CONTENT = "content";
	public static final String INTENT_LEY_ABOUT = "about";

	private TextView ab_back;
	private TextView ab_title;

	private WebView mWebView;

	private String content;
	private String about;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_club_about);

		content = getIntent().getStringExtra(INTENT_KEY_CONTENT);
		about = getIntent().getStringExtra(INTENT_LEY_ABOUT);
		title = getIntent().getStringExtra(ClubActivity.INTENT_KEY_TITLE);

		initView();

		loadWebData(content, about);

	}

	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("积分说明");
		ab_back.setOnClickListener(this);

		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		mWebView.setBackgroundResource(android.R.color.transparent); // 设置背景色
		mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
			}
		});
	}

	private void loadWebData(String content, String about) {
		Log.i("", "dfasfasd " + content + "|" + about);
		if (content != null && about != null) {
			mWebView.loadDataWithBaseURL(null,
					WebViewUtil.initWebViewForClub(content, about),
					"text/html", "utf-8", null);
		}
	}

	@Override
	public void onClick(View view) {
		finish();
	}

	@Override
	public void finish() {
		Intent intent = new Intent(this, ClubActivity.class);
		intent.putExtra(ClubActivity.INTENT_KEY_TITLE, title);
		startActivity(intent);
		super.finish();
	}
	
}
