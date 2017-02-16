package com.huiyin.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.huiyin.AppContext;
import com.huiyin.api.URLs;

public class UploadHelper {

	private static UploadHelper mInstance;

	public static UploadHelper getInstance() {

		if (mInstance == null) {
			mInstance = new UploadHelper();
		}
		return mInstance;
	}

	// flag=1代表上传头像
	public void doUpload(String mCachePath, String imageName, Bitmap mBitmap,
			String flag, UploadCallBack mCallBack) {

		String httpUrl = URLs.IMAGE_URL + "/hy/uploadImage.do" + "?userId="
				+ AppContext.getInstance().getUserId() + "&imageName=" + imageName + "&flag="
				+ flag;
		String cacheName = new SimpleDateFormat("yyyyMMddhhmmss")
				.format(new Date()) + ".jpg";
		Log.i("tag", "httpUrl----" + httpUrl);
		File cachePath = new File(mCachePath);
		if (!cachePath.exists()) {
			cachePath.mkdirs();
		}

		File imageFile = new File(mCachePath + "/" + cacheName);
		FileOutputStream fos = null;
		try {
			if (!imageFile.exists()) {
				imageFile.createNewFile();
			}
			fos = new FileOutputStream(imageFile);
			mCallBack.onStart(imageFile.getAbsoluteFile().toString());
			mBitmap.compress(CompressFormat.JPEG, 100, fos);
		} catch (FileNotFoundException e1) {
			mCallBack.onError("本地缓存文件不存在");
			e1.printStackTrace();
			return;
		} catch (IOException e) {
			mCallBack.onError("缓存文件创建失败");
			e.printStackTrace();
		}
		HttpPost post = new HttpPost(httpUrl);
		HttpClient httpClient = new DefaultHttpClient();
		FileEntity entity = new FileEntity(imageFile, "binary/octet-stream");
		HttpResponse response = null;
		try {
			post.setEntity(entity);
			entity.setContentEncoding("binary/octet-stream");
			response = httpClient.execute(post);

			// 如果返回状态为200，获得返回的结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity mEntity = response.getEntity();
				String content = EntityUtils.toString(mEntity, "UTF-8");
				mCallBack.onFinish(content);
			} else {
				mCallBack.onError("网络不给力");
			}
		} catch (ClientProtocolException e) {
			mCallBack.onError("未知异常");
			e.printStackTrace();
		} catch (ParseException e) {
			mCallBack.onError("返回数据解析异常");
			e.printStackTrace();
		} catch (IOException e) {
			mCallBack.onError("数据流读写异常");
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					imageFile.delete();
					e.printStackTrace();
				}
			}
		}
	}

	public interface UploadCallBack {

		void onStart(String filePath);

		void onFinish(String content);

		void onError(String errorMsg);
	}

}
