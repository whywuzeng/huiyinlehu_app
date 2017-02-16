package com.huiyin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.ui.MainActivity;
import com.huiyin.ui.WelcomeActivity;
import com.huiyin.ui.nearshop.NearShopLineActivity;
import com.huiyin.ui.nearshop.NearShopListBean;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.nearshop.NearTheShopInfoActivity;
import com.huiyin.ui.nearshop.NearTheShopMapActivity;
import com.huiyin.ui.nearshop.StoreListItem;
import com.huiyin.utils.AppManager;

public class UIHelper {

	private static Toast mToast;
	public static Dialog mLoadDialog;
	private static int count;

	/**
	 * 显示登录界面
	 * 
	 * @param ac
	 */
	public static void showMainAc(Context ac) {
		Intent intent = new Intent(ac, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity) ac).startActivity(intent);
		AppManager.getAppManager().finishActivity(ac.getClass());
	}

	public static void showLoadDialog(Context context) {
		showLoadDialog(context, null);
	}

	public static void showLoadDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (context.isRestricted())
			if (mLoadDialog != null && mLoadDialog.isShowing())
				return;
		count++;
		if (count > 1) {
			count = 0;
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View login_dialog = inflater.inflate(R.layout.load_doag, null);
		mLoadDialog = new Dialog(context, R.style.load_dialog);
		mLoadDialog.setCanceledOnTouchOutside(false);
		if (!TextUtils.isEmpty(msg)) {
			TextView messageTV = (TextView) login_dialog
					.findViewById(R.id.login_doag_name);
			messageTV.setText(msg);
		}
		mLoadDialog.setContentView(login_dialog);
		try {
			mLoadDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void cloesLoadDialog() {
		try {

			if (mLoadDialog != null && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
				count = 0;
				mLoadDialog = null;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 显示欢迎结界面
	 * 
	 * @param ac
	 */
	public static void showWelcomeAc(Context ac) {
		Intent intent = new Intent(ac, WelcomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity) ac).startActivity(intent);
	}

	public static void showToast(int resId) {
		if (mToast == null)
			mToast = Toast.makeText(AppContext.getInstance(), AppContext
					.getInstance().getResources().getString(resId),
					Toast.LENGTH_LONG);
		mToast.setText(resId);
		mToast.show();
	}

	public static void showToast(String msg) {
		if (mToast == null)
			mToast = Toast.makeText(AppContext.getInstance(), msg,
					Toast.LENGTH_LONG);
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 显示周边商店
	 * 
	 * @param ac
	 */
	public static void showNearTheShopActivityNew(Context ac) {
		Intent intent = new Intent(ac, NearTheShopActivityNew.class);
		((Activity) ac).startActivity(intent);
	}

	/**
	 * 显示周边商店地图
	 * 
	 * @param ac
	 */
	public static void showNearTheShopMapActivity(Context ac,
			NearShopListBean data) {
		Intent intent = new Intent(ac, NearTheShopMapActivity.class);
		intent.putExtra("NearShopListBean", data);
		((Activity) ac).startActivity(intent);
	}

	/**
	 * 显示周边商店路线
	 * 
	 * @param ac
	 * @param latLng
	 */
	public static void showNearShopLineActivity(Context ac, double latitude,
			double longitude, String endPoint) {
		Intent intent = new Intent(ac, NearShopLineActivity.class);
		intent.putExtra("lat", latitude);
		intent.putExtra("lng", longitude);
		intent.putExtra("endPoint", endPoint);
		((Activity) ac).startActivity(intent);
	}

	/**
	 * 显示周边商店路线
	 * 
	 * @param ac
	 * @param latLng
	 */
	public static void showNearTheShopInfoActivity(Context ac,
			StoreListItem data) {
		Intent intent = new Intent(ac, NearTheShopInfoActivity.class);
		intent.putExtra("StoreListItem", data);
		((Activity) ac).startActivity(intent);
	}

}
