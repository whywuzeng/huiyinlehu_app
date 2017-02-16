package com.huiyin.utils;

import java.net.SocketTimeoutException;

import org.apache.http.Header;

import android.content.Context;
import android.widget.Toast;

import com.huiyin.wight.Tip;
import com.huiyin.http.AsyncHttpResponseHandler;

public class MyCustomResponseHandler extends AsyncHttpResponseHandler {
	public static final String NETWORK_NOT_GOOD = "7770";
	public static final String SERVICE_TIMEOUT = "7771";
	public boolean isShowLoading = true;
	private Context mContext;

	public MyCustomResponseHandler(Context context, boolean isShowLoading) {
		this.isShowLoading = isShowLoading;
		this.mContext = context;
	}

	public MyCustomResponseHandler(Context context) {
		this.mContext = context;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isShowLoading) {
			Tip.showLoadDialog(mContext, "正在加载...");
		}
	}

	@Override
	public void onFinish() {
		super.onFinish();
		if (isShowLoading)
			Tip.colesLoadDialog();
	}

	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		Tip.colesLoadDialog();
		if (error instanceof SocketTimeoutException) {
			/* 网络超时 */
			Toast.makeText(mContext, "网络超时", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext, "连接错误", Toast.LENGTH_LONG).show();
			// onFailure(NETWORK_NOT_GOOD, content);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		super.onSuccess(statusCode, headers, content);
		System.out.println("得到返回码：" + content);
		try {
			switch (statusCode) {
			case 200:
				onRefreshData(content);
				break;
			case 401:
				// onFailure("401", mActivity.getString(R.string.error_401));
				break;
			case 403:
				// onFailure("404", mActivity.getString(R.string.error_404));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onRefreshData(String content) {
	}
}
