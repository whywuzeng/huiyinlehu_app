package com.huiyin.utils.imageupload;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.api.RequstClient;
import com.huiyin.utils.Base64;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.wight.Tip;

public class ImageUpload {
	private Context mContext;
	private ArrayList<String> hostimageurls = null;
	private ArrayList<String> netimageurls = null;
	private int upimage = 0;
	private UpLoadImageListener mUpLoadImageListener;
	private int flag = 2;

	public ImageUpload(Context mContext, ArrayList<String> hostimageurls, UpLoadImageListener mUpLoadImageListener) {
		this.mContext = mContext;
		this.hostimageurls = hostimageurls;
		this.mUpLoadImageListener = mUpLoadImageListener;
		netimageurls = new ArrayList<String>();
	}

	public void reLoad() {
		if (hostimageurls != null && hostimageurls.size() > 0) {
			Tip.showLoadDialog(mContext, "正在上传");
			if (netimageurls == null)
				netimageurls = new ArrayList<String>();
			imageup(hostimageurls.get(upimage));
		} else if (mUpLoadImageListener != null) {
			mUpLoadImageListener.UpLoadFail();
		}
	}

	public void startLoad() {
		this.reLoad();
	}

	private void imageup(String path) {
		String data;
		Bitmap temp = BitmapUtils.showimageFull(path, 1024, 1024);
		data = Base64.imgToBase64(temp);
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// try {
		// fis = new FileInputStream(path);
		// byte[] tempBuffer = new byte[2048];
		// int count = 0;
		// while ((count = fis.read(tempBuffer)) >= 0)
		// {
		// baos.write(tempBuffer, 0, count);
		// }
		// fis.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// Tip.colesLoadDialog();
		// }
		// data = new String(Base64.encode(baos.toByteArray()));

		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onRefreshData(String content) {
				ImageUrlBean bean = ImageUrlBean.explainJson(content, mContext);
				if (bean.type == 1 && bean.imgurl != null) {
					netimageurls.add(bean.imgurl);
					if (hostimageurls.size() - 1 > upimage) {
						upimage++;
						imageup(hostimageurls.get(upimage));
					} else {
						Tip.colesLoadDialog();
						if (mUpLoadImageListener != null) {
							mUpLoadImageListener.UpLoadSuccess(netimageurls);
						}
					}
				} else {
					Tip.colesLoadDialog();
					Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
					if (mUpLoadImageListener != null) {
						mUpLoadImageListener.UpLoadFail();
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
				Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
				if (mUpLoadImageListener != null) {
					mUpLoadImageListener.UpLoadFail();
				}
				return;
			}
		};
		String userId = AppContext.getInstance().getUserId();
		RequstClient.imgUpload(handler, data, "jpg", userId, flag);
	}

	public interface UpLoadImageListener {

		void UpLoadSuccess(ArrayList<String> netimageurls);

		void UpLoadFail();

	}

	public void upLoadHeadImage() {
		flag = 1;
		this.reLoad();
	}
}
