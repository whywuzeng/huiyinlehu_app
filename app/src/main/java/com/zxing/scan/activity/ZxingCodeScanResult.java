package com.zxing.scan.activity;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;

public class ZxingCodeScanResult extends BaseActivity implements OnClickListener {

	private TextView ab_title, ab_back;

	private TextView reuslt;

	private String resultValue;

	private View item;

	private ImageView zxiv;

	private Bitmap img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zxing_code_scan_result);

		Intent intent = getIntent();

		resultValue = intent.getStringExtra("result");
		
		img = intent.getParcelableExtra("bitmap");
		
		initView();

		setView();
	}

	private void setView() {
		reuslt.setText(resultValue);
		zxiv.setImageBitmap(img);
	}

	private void initView() {
		zxiv = (ImageView) findViewById(R.id.zxing_code_scan_result_iv);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_back = (TextView) findViewById(R.id.ab_back);
		reuslt = (TextView) findViewById(R.id.result);
		ab_back.setOnClickListener(this);

		item = findViewById(R.id.item);

		ab_title.setText("扫描结果");

		if (resultValue.startsWith("http") || resultValue.startsWith("www")) {
			item.setOnClickListener(this);
		}

	}

	private void downloadApk(String resultString) {
		DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(resultString);
		Request request = new Request(uri);
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);
		// 不显示下载界面
		request.setVisibleInDownloadsUi(false);
		/*
		 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
		 * 在/mnt/sdcard
		 * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错
		 * ，不设置，下载后的文件在/cache这个 目录下面
		 */
		// request.setDestinationInExternalFilesDir(this, null, "tar.apk");
		long id = downloadManager.enqueue(request);
		// TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
	}

	@Override
	public void onClick(View view) {
		if (view == ab_back) {
			finish();
		} else if (view == item) {
			Uri uri = Uri.parse(resultValue);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
		}
	}
}
