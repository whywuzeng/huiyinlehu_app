package com.huiyin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.huiyin.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class UpdateVersion extends Activity {
	private static final String TAG = "Update";
	public ProgressDialog pBar;
	private Handler handler = new Handler();
	private Config config;
	private static String packgeName;
	private int newVerCode = 0;
	private String newVerName = "";
	private int newSize = 0;

	String UPDATE_SERVER = "http://192.168.0.40:8080/code/version/";
	String UPDATE_APKNAME = "UpdateDemo.apk";
	String UPDATE_VERJSON = "ver.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		packgeName = this.getPackageName();
		config = new Config(UPDATE_SERVER, UPDATE_APKNAME, UPDATE_VERJSON);
		if (getServerVerCode()) {
			int vercode = UpdateVersion.getVerCode(this);
			if (newVerCode > vercode) {
				doNewVersionUpdate();
			} else {
				notNewVersionShow();
			}
		}

	}

	private boolean getServerVerCode() {
		try {
			String verjson = getContent(config.UPDATE_SERVER
					+ config.UPDATE_VERJSON);
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("versionCode"));
					newVerName = obj.getString("versionName");
					newSize = Integer.parseInt(obj.getString("apkSize"));
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					newSize = -1;
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void notNewVersionShow() {
		int verCode = UpdateVersion.getVerCode(this);
		String verName = UpdateVersion.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",\n已是最新版,无需更新!");
		Dialog dialog = new AlertDialog.Builder(UpdateVersion.this)
				.setTitle("软件更新").setMessage(sb.toString())// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate() {
		int verCode = UpdateVersion.getVerCode(this);
		String verName = UpdateVersion.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(", 是否更新?");
		Dialog dialog = new AlertDialog.Builder(UpdateVersion.this)
				.setIcon(R.drawable.icon)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(UpdateVersion.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								downFile(config.UPDATE_SERVER
										+ config.UPDATE_APKNAME);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File destDir = new File(
								Environment.getExternalStorageDirectory(),
								"angool/download");
						if (!destDir.exists()) {
							destDir.mkdirs();
						}

						File file = new File(
								Environment.getExternalStorageDirectory(),
								"angool/download/" + config.UPDATE_APKNAME);

						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;

						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							int size = (int) (count / (newSize/(1024/10)));
							System.out.println(size);
							pBar.setProgress(size);
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "angool/download/"
				+ config.UPDATE_APKNAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public class Config {

		public String UPDATE_SERVER;
		public String UPDATE_APKNAME;
		public String UPDATE_VERJSON;

		public Config(String UPDATE_SERVER, String UPDATE_APKNAME,
				String UPDATE_VERJSON) {
			this.UPDATE_APKNAME = UPDATE_APKNAME;
			this.UPDATE_SERVER = UPDATE_SERVER;
			this.UPDATE_VERJSON = UPDATE_VERJSON;
		}

		public String getUPDATE_SERVER() {
			return UPDATE_SERVER;
		}

		public void setUPDATE_SERVER(String uPDATESERVER) {
			UPDATE_SERVER = uPDATESERVER;
		}

		public String getUPDATE_APKNAME() {
			return UPDATE_APKNAME;
		}

		public void setUPDATE_APKNAME(String uPDATEAPKNAME) {
			UPDATE_APKNAME = uPDATEAPKNAME;
		}

		public String getUPDATE_VERJSON() {
			return UPDATE_VERJSON;
		}

		public void setUPDATE_VERJSON(String uPDATEVERJSON) {
			UPDATE_VERJSON = uPDATEVERJSON;
		}

	}

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(packgeName, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(packgeName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;

	}

	/**
	 * 获取网址内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String getContent(String url) throws Exception {
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}
}