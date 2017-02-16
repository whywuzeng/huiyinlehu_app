package com.huiyin.wight.rongcloud;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.BaseBean;
import com.huiyin.http.AsyncHttpResponseHandler;
import com.huiyin.wight.Tip;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class RongCloud {
	private static RongCloud instance;
	private String token;
	private Context mContext;
	private boolean connectSuccess = false;

	public static RongCloud getInstance(Context mContext) {
		if (instance == null) {
			instance = new RongCloud();
		}
		instance.mContext = mContext;
		return instance;
	}

	private void getTokenFromAppServer() {
		RequstClient.sendRongCloud(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.getString("type").equals("1")) {
						obj = obj.getJSONObject("rongCloud");
						token = obj.getString("token");

						// PreferenceUtil.getInstance(mContext).setToken(token);
						Connect(token);
						return;
					} else {
						// 返回失败信息
						token = null;
						String errorMsg = obj.getString("msg");
						Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JsonSyntaxException e) {
					token = null;
					Tip.colesLoadDialog();
					e.printStackTrace();
				} catch (JSONException e) {
					token = null;
					Tip.colesLoadDialog();
					e.printStackTrace();
				} catch (Exception e) {
					token = null;
					Tip.colesLoadDialog();
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				Tip.showLoadDialog(mContext, "正在连接");
			}

			@Override
			public void onFailure(Throwable error, String errorMessage) {
				super.onFailure(error, errorMessage);
				token = null;
				Tip.colesLoadDialog();
				Toast.makeText(mContext, "服务器连接失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void Connect(String token) {
		// 连接融云服务器。
		RongIM.connect(token, new RongIMClient.ConnectCallback() {

			@Override
			public void onSuccess(String s) {
				// 此处处理连接成功。
				Log.d("Connect:", "Login successfully.");
				connectSuccess = true;
				Tip.colesLoadDialog();
				startCustomerServiceChat();
			}

			@Override
			public void onError(ErrorCode errorCode) {
				// 此处处理连接错误。
				Tip.colesLoadDialog();
				Log.d("Connect:", "Login failed.");
				// Toast.makeText(mContext, "服务器连接失败",
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void startCustomerServiceChat() {
		if (connectSuccess) {
			RongIM.getInstance().startCustomerServiceChat(mContext,
					URLs.customerServiceId, "在线客服");
		} else {
			getTokenFromAppServer();
			// String token = PreferenceUtil.getInstance(mContext).getToken();
			// if (token != null && !token.equals("")) {
			// Connect(token);
			// } else {
			//
			// }
		}
	}

	public class rongCloud extends BaseBean {
		private static final long serialVersionUID = 1L;
		public int code;
		public String userId;
		public String token;
	}
}
