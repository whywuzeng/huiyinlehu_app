package com.huiyin.wight;


import com.huiyin.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tip {

	private static TipsToast tipsToast;

	public static void showInputError(Context cont, View view, String msg) {
		if (view == null) {
			Toast.makeText(cont, msg, Toast.LENGTH_LONG).show();
		} else {
			int sysVersion = Integer.parseInt(VERSION.SDK);
			if (sysVersion > 13 && view instanceof EditText) {
				String errorTipS = cont.getResources().getString(
						R.string.error_tip);
				((EditText) view).setError(Html.fromHtml(String.format(
						errorTipS, msg)));
			} else {
				Toast.makeText(cont, msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void showTips(Context cont, int iconResId, String msg) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(cont, msg, TipsToast.LENGTH_SHORT);
		}
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msg);
		tipsToast.show();
	}

	public static Dialog mLoadDialog;

	public static void showLoadDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (mLoadDialog != null && mLoadDialog.isShowing()) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View login_doag = inflater.inflate(R.layout.load_doag, null);
		((TextView) login_doag.findViewById(R.id.login_doag_name)).setText(msg);
		mLoadDialog = new Dialog(context, R.style.load_dialog);
		mLoadDialog.setCanceledOnTouchOutside(false);
		mLoadDialog.setCancelable(false);
		mLoadDialog.setContentView(login_doag);
		try {
			mLoadDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void colesLoadDialog() {
		if (mLoadDialog != null && mLoadDialog.isShowing()) {
			mLoadDialog.dismiss();
			mLoadDialog = null;
		}
	}

	public static void showTipDialog(Context context, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

}
