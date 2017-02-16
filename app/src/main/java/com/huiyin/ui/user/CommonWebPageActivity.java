package com.huiyin.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.WebViewUtil;

public class CommonWebPageActivity extends BaseActivity {
	private final static String TAG = "CommonWebPageActivity";
	WebView mWebView;
	TextView left_ib, middle_title_tv;
	String flag,title;
	String html_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webpage);

		initView();
		initData();

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {

		if(getIntent().hasExtra("server")){
			title  = getIntent().getStringExtra("server");
			flag = getIntent().getStringExtra("flag");
		}else{
			title = getIntent().getStringExtra("title");
			flag = getIntent().getStringExtra("flag");
		}
		
		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}

		});

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		mWebView.setBackgroundResource(android.R.color.transparent); // 设置背景色
		mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText(title);

	}

	/**
	 * 获取数据
	 */
	private void initData() {

		RequstClient.getHelpInfo(flag, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "getHelpInfo:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					html_text = obj.getString("content");
					loadWebData(html_text);

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

	private void loadWebData(String dataHtml) {
		mWebView.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtml), "text/html", "utf-8", null);
	}

}
