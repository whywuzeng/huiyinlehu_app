package com.huiyin.broadcast;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver {

	private DownloadManager downloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			Toast.makeText(context, "下载完成了....", Toast.LENGTH_LONG).show();
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			// TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
			Query query = new Query();
			query.setFilterById(id);
			downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor cursor = downloadManager.query(query);

			int columnCount = cursor.getColumnCount();
			String local_filename = null;
			// TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
			while (cursor.moveToNext()) {
				for (int j = 0; j < columnCount; j++) {
					String columnName = cursor.getColumnName(j);
					String string = cursor.getString(j);
					if (columnName.equals("local_filename")) {
						local_filename = string;
					}
					if (string != null) {
						System.out.println(columnName + ": " + string);
					} else {
						System.out.println(columnName + ": null");
					}
				}
			}
			cursor.close();

			Intent install = new Intent(Intent.ACTION_VIEW);
			install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			install.setDataAndType(Uri.fromFile(new File(local_filename)), "application/vnd.android.package-archive");
			context.startActivity(install);

		} else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
			Toast.makeText(context, "点击了通知栏", Toast.LENGTH_LONG).show();
		}

	}
}
