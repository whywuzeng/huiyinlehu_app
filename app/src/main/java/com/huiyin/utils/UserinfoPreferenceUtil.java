package com.huiyin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.huiyin.AppContext;
import com.huiyin.UserInfo;
import com.huiyin.bean.LoginInfo;

public class UserinfoPreferenceUtil {

	/**
	 * 登陆成功 ，并将已经登陆的用户信息进行存储
	 * 
	 * @param mContext
	 */
	public static void setLoginSuccess(Context mContext, UserInfo mUserInfo) {
		if (mContext == null)
			return;
		setLoginSuccess(mContext, true);
		saveUserInfo(mContext, mUserInfo);
	}

	/**
	 * 登陆失败
	 * 
	 * @param mContext
	 */
	public static void setLoginFail(Context mContext) {
		if (mContext == null)
			return;
		setLoginSuccess(mContext, false);
		AppContext.getInstance().setLoginfalse();
		File data = mContext.getFileStreamPath("cache_userinfo_data");
		if (data.exists())
			data.delete();
	}

	private static void setLoginSuccess(Context mContext, boolean isLoginSuccess) {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putBoolean("isLoginSuccess", isLoginSuccess);
		mEditor.commit();

	}

	/**
	 * 判断是否登陆成功
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isLoginSuccess(Context mContext) {
		if (mContext == null)
			return false;
		return mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE).getBoolean("isLoginSuccess", false);
	}

	/**
	 * 将已经登陆的用户信息进行存储
	 * 
	 * @param mContext
	 * @param mUserInfo
	 */
	public static void saveUserInfo(Context mContext, UserInfo mUserInfo) {
		if (!isLoginSuccess(mContext))
			return;
		String cacheName = "cache_userinfo_data";
		saveObject(mContext, mUserInfo, cacheName);
	}

	/**
	 * 获取本地存储的用户信息 使用之前一定要调用isLoginSuccess 进行判断
	 * 
	 * @param mContext
	 * @return
	 */
	public static UserInfo getUserInfo(Context mContext) {
		if (!isLoginSuccess(mContext))
			return null;
		String cacheName = "cache_userinfo_data";
		return (UserInfo) readObject(mContext, cacheName);
	}

	public static void saveLoginInfo(Context mContext, LoginInfo mLoginInfo) {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

		Editor mEditor = mPreferences.edit();
		mEditor.putString("userName", mLoginInfo.userName);
		mEditor.putString("psw", mLoginInfo.psw);
		mEditor.putBoolean("isChecked", mLoginInfo.isChecked);

		mEditor.commit();

	}

	public static LoginInfo getLoginInfo(Context mContext) {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.userName = mPreferences.getString("userName", "");
		loginInfo.psw = mPreferences.getString("psw", "");
		loginInfo.isChecked = mPreferences.getBoolean("isChecked", false);

		return loginInfo;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public static boolean saveObject(Context mContext, Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = mContext.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObject(Context mContext, String file) {
		if (!isExistDataCache(mContext, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = mContext.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = mContext.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private static boolean isExistDataCache(Context mContext, String cachefile) {
		boolean exist = false;
		File data = mContext.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}
}
