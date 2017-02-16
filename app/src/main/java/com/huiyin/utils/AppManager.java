package com.huiyin.utils;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: AppManager
 * @Description: 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @Author zengweijie
 * @date 2013-8-6 下午1:37:20
 *************************************************************************** 
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public Activity getActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出所有Activity
	 * 
	 * @return 是否完全退出
	 */
	public boolean exitAllActivity() {
		LogUtil.e("AppManager", "应用程序退出exitAllActivity()");
		boolean isFinished = true;
		for (Activity act : activityStack) {
			if (act != null) {
				act.finish();
			}
		}
		for (Activity act : activityStack) {
			if (!act.isFinishing()) {
				isFinished = false;
			}
		}
		return isFinished;
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			if (exitAllActivity()) {
				activityMgr.killBackgroundProcesses(context.getPackageName());
				System.exit(0);
			}
		} catch (Exception e) {
			LogUtil.e("AppManager", "退出应用程序异常!");
			StackTraceElement[] stacks = e.getStackTrace();
			StringBuffer sb = new StringBuffer();
			for (StackTraceElement stack : stacks) {
				sb.append(stack.toString() + "\n");
			}
			LogUtil.e("AppManager", sb.toString());
			System.exit(0);
		}
	}
}