package com.huiyin.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.huiyin.bean.UpdataInfo;

public class UpdateVersionTool {
	protected static final String TAG = "MainActivity";
	private static PackageInfo info;
	private static UpdataInfo info2;
	private static File file;

	/**
	 * 获取当前应用的版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "获取版本号失败！";
		}
		return info.versionName;
	}
	
	/**
	 * 获取当前应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.versionCode;
	}

	/**
	 * 获取服务其中的版本信息
	 * 
	 * @param is
	 * @return
	 */
	public static UpdataInfo getUpdataInfo(InputStream is) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "utf-8");// 设置解析的数据源
			int type = parser.getEventType();
			info2 = new UpdataInfo();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						info2.setVersion(parser.nextText()); // 获取版本号
					} else if ("url".equals(parser.getName())) {
						info2.setUrl(parser.nextText()); // 获取要升级的APK文件
					} else if ("description".equals(parser.getName())) {
						info2.setDescription(parser.nextText()); // 获取该文件的信息
					}
					break;
				}
				type = parser.next();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return info2;
	}

	// 从服务器下载apk:
	public static File getFileFromServer(String path, ProgressDialog pd,String apkName)
			 {
		try {
			// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				// 获取到文件的大小
				pd.setMax(conn.getContentLength());
				InputStream is = conn.getInputStream();
				file = new File(Environment.getExternalStorageDirectory(),
						apkName+".apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len;
				int total = 0;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;
					// 获取当前下载量
					pd.setProgress(total);
				}
				fos.close();
				bis.close();
				is.close();
			
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	
	
    /*  
     *   
     * 弹出对话框通知用户更新程序   
     *   
     * 弹出对话框的步骤：  
     *  1.创建alertDialog的builder.    
     *  2.要给builder设置属性, 对话框的内容,样式,按钮  
     *  3.通过builder 创建一个对话框  
     *  4.对话框show()出来    
     */    
    public static void showUpdataDialog(Context context,UpdataInfo info) {    
        AlertDialog.Builder builer = new Builder(context) ;     
        builer.setTitle("版本升级");    
        builer.setMessage(info.getDescription());    
        //当点确定按钮时从服务器上下载 新的apk 然后安装      
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {    
        public void onClick(DialogInterface dialog, int which) {    
                Log.i(TAG,"下载apk,更新");        
            }       
        });    
        //当点取消按钮时进行登录     
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {    
            public void onClick(DialogInterface dialog, int which) {    
                // TODO Auto-generated method stub     
              //  LoginMain(); //登录到主界面   
            }   
        });    
        AlertDialog dialog = builer.create();    
        dialog.show();    
    }   

    /*  
     * 进入程序的主界面  
     */    
    public static void LoginMain(Context context,Class clazz,Activity activity){    
        Intent intent = new Intent(context,clazz);    
        context.startActivity(intent);    
        //结束掉当前的activity      
        activity.finish();    
    }  
    
    
    //安装apk      
    public static void installApk(File file,Context context) {    
        Intent intent = new Intent();    
        //执行动作     
        intent.setAction(Intent.ACTION_VIEW);    
        //执行的数据类型     
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了      
        context.startActivity(intent);    
    }    
}
