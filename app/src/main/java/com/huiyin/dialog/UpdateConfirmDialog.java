package com.huiyin.dialog;

import com.huiyin.R;
import com.huiyin.utils.WebViewUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class UpdateConfirmDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel;
	private TextView title;
	private WebView mWebView;

	public UpdateConfirmDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_update_confirm);
		title = (TextView) findViewById(R.id.title);
		
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
		mWebView = (WebView) findViewById(R.id.mWebView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		mWebView.setBackgroundResource(android.R.color.transparent); // 设置背景色
		mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		
	}

	public void setClickListener(DialogClickListener clickListener) {
		exitClickListener = clickListener;
	}

	public void setCustomTitle(String value) {
		title.setText(value);
	}

	public void setCustomTitle(int value) {
		title.setText(value);
	}

	public void setUpdateContent(String content) {
		if (mWebView != null) {
			mWebView.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(content), "text/html", "utf-8", null);
		}
	}

	public void setConfirm(String value) {
		btnConfirm.setText(value);
	}

	public void setConfirm(int value) {
		btnConfirm.setText(value);
	}

	public void setCancel(String msg) {
		btnCancel.setText(msg);
	}

	public void setCancel(int msg) {
		btnCancel.setText(msg);
	}


	private DialogClickListener exitClickListener;
	
	

	public interface DialogClickListener {
		void onConfirmClickListener();

		void onCancelClickListener();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (exitClickListener != null) {
				exitClickListener.onConfirmClickListener();
			}
			dismiss();
		} else if (v == btnCancel) {
			if (exitClickListener != null) {
				exitClickListener.onCancelClickListener();
			}
			dismiss();
		}
	}

}
