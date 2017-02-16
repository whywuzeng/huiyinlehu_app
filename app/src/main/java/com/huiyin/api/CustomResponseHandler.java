package com.huiyin.api;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.http.AsyncHttpResponseHandler;

public class CustomResponseHandler extends AsyncHttpResponseHandler {
	public static final String NETWORK_NOT_GOOD = "7770";
	public static final String SERVICE_TIMEOUT = "7771";
	public boolean isShowLoading = true;
	private Activity mActivity;
	private Context mContext;

	public CustomResponseHandler(Activity mActivity, boolean isShowLoading) {
		this.isShowLoading = isShowLoading;
		this.mActivity = mActivity;
	}

	public CustomResponseHandler(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public CustomResponseHandler(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onStart() {
		super.onStart();
		try {
			if (isShowLoading && !mActivity.isFinishing()) {
				UIHelper.showLoadDialog(mActivity, mActivity.getString(R.string.loading));
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onFinish() {
		super.onFinish();
		try {
			if (!mActivity.isFinishing()) {
				UIHelper.cloesLoadDialog();
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		if(error instanceof HttpException) {
			onFailure("",mActivity.getString(R.string.networkFailure));// 网络异常
		}else if (error instanceof SocketTimeoutException) {
			onFailure("",mActivity.getString(R.string.responseTimeout)); // 响应超时
		}else if (error instanceof ConnectTimeoutException) {
			onFailure("",mActivity.getString(R.string.requestTimeout));// 请求超时
		}else if (error instanceof IOException) {
			onFailure("",mActivity.getString(R.string.networkError)); // 网络异常
		}else {
			onFailure("",content); // 无法连接网络
		}
	}
	
	
	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		super.onSuccess(statusCode, headers, content);
		System.out.println("得到的返回码" + content);
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
			// Tip.showTips(mContext, R.drawable.tips_toast_error, "网络不给力！");
		}
	}

	
	/**
	 * 出错
	 * @param error
	 * @param errorMessage
	 */
	public void onFailure(String error, String errorMessage) {
		try {
			if (isShowLoading) {
				UIHelper.cloesLoadDialog();
			}
		} catch (Exception e) {
		}
		Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT).show();
	}

	//该方法可以重写  更新ui
	public void onRefreshData(String content) {
	}
}
