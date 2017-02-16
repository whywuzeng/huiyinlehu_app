package com.huiyin.ui.classic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huiyin.R;
import com.huiyin.utils.WebViewUtil;


/**
 * 销量排行榜
 * 
 * */
public class SaleServiceFragment extends Fragment {

	private WebView webview;

	private String dataHtml;

	public SaleServiceFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_detail_parameter,
				container, false);

		initViews(view);

		if (dataHtml != null && !dataHtml.equals("")) {
			loadWebData(dataHtml);
		}
		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		webview = (WebView) view.findViewById(R.id.webview);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webview.setBackgroundResource(android.R.color.transparent); // 设置背景色
		webview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		webview.setVisibility(View.INVISIBLE);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				webview.setVisibility(View.VISIBLE);
			}

		});
	}

	public void loadWebData(String dataHtml) {
		this.dataHtml = dataHtml;
		if (webview != null) {
			webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtml), "text/html", "utf-8", null);
		}
	}

}