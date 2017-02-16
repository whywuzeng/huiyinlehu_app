package com.huiyin.utils;

import android.app.Activity;
import cn.bidaround.point.YtLog;
import cn.bidaround.youtui_template.YouTuiViewType;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.ErrorInfo;
import cn.bidaround.ytcore.YtShareListener;
import cn.bidaround.ytcore.data.ShareData;
import cn.bidaround.ytcore.data.YtPlatform;

public class YtShare {

	private YtTemplate temp=null;
	
	private static Activity act;

	private YtShare(Activity act){
		this.act=act;
		YtTemplate.init(act);
	}
	
	private static YtShare ytShare=null;
	
	public static YtShare createInstance(Activity act){
		if(ytShare==null){
			synchronized (YtShare.class) {
				if(ytShare==null){
					ytShare=new YtShare(act);
				}
			}
		}
		return ytShare;
	}
	
	
	
	public void share(){
		// 打开白色样式的分享
		YtShareListener listener = new YtShareListener() {
			@Override
			public void onSuccess(ErrorInfo arg0) {
				YtLog.w("sina share onSuccess", arg0.getErrorMessage());
			}

			@Override
			public void onPreShare() {
				YtLog.w("sina share onPreShare", "onPreShare");
			}

			@Override
			public void onError(ErrorInfo arg0) {
				YtLog.w("sina share onError", arg0.getErrorMessage());
			}

			@Override
			public void onCancel() {
				YtLog.i("sina share Cancel", "cancle");
			}
		};

		ShareData contentShareData = new ShareData();
		contentShareData.isAppShare = false;
		contentShareData.setDescription("分享");
		contentShareData.setTitle("扬州汇银家电分享");
		contentShareData.setText("通过扬州汇银APP你就可以实现手机移动端购买您心意已经的家电，快来试试吧！ ");
		contentShareData.setTarget_url("http://www.lehumall.com/");
		
		contentShareData.setImageUrl("http://youtui.mobi/media/image/youtui.png");
		
		YtTemplate whiteTemplate = new YtTemplate(act, YouTuiViewType.WHITE_LIST, false);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_WECHATMOMENTS);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_MESSAGE);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_RENN);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_SCREENCAP);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_QRCORE);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_COPYLINK);
		whiteTemplate.removePlatform(YtPlatform.PLATFORM_EMAIL);
		
		whiteTemplate.setShareData(contentShareData);
		/** 分别为每个平台添加监听事件,如果不需要可以不添加 */
		whiteTemplate.addListener(YtPlatform.PLATFORM_QQ, listener);
		whiteTemplate.addListener(YtPlatform.PLATFORM_QZONE, listener);
		whiteTemplate.addListener(YtPlatform.PLATFORM_SINAWEIBO, listener);
		whiteTemplate.addListener(YtPlatform.PLATFORM_TENCENTWEIBO, listener);
		whiteTemplate.addListener(YtPlatform.PLATFORM_WECHAT, listener);
		whiteTemplate.addListener(YtPlatform.PLATFORM_MORE_SHARE, listener);
		/**
		 * 分别给每个平台添加分享数据,如果不设置 分享的数据为blackTemp.setShareData(shareData)中设置的数据
		 */
		whiteTemplate.addData(YtPlatform.PLATFORM_QQ, contentShareData);
		whiteTemplate.addData(YtPlatform.PLATFORM_QZONE, contentShareData);
		whiteTemplate.addData(YtPlatform.PLATFORM_SINAWEIBO, contentShareData);
		whiteTemplate.addData(YtPlatform.PLATFORM_TENCENTWEIBO, contentShareData);
		whiteTemplate.addData(YtPlatform.PLATFORM_WECHAT, contentShareData);
		whiteTemplate.addData(YtPlatform.PLATFORM_MORE_SHARE, contentShareData);
		whiteTemplate.setScreencapVisible(false);
		// 调出分享界面
		whiteTemplate.show();
		temp = whiteTemplate;
	}
	
	public void destroy(){
		YtTemplate.release(act);
	}
}
