package com.huiyin.dialog;

import com.huiyin.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SingleConfirmDialog extends Dialog implements OnClickListener {

	private Button btnConfirm;
	private TextView title;
	private TextView message;

	public SingleConfirmDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_single_confirm);
		title = (TextView) findViewById(R.id.title);
		message = (TextView) findViewById(R.id.message);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
	}

	public void setClickListener(DialogClickListener clickListener) {
		exitClickListener = clickListener;
	}

	public void setCustomTitle(String value) {
		title.setText(value);
	}

	public void setCustomTitle(int value) {
		title.setText(value);
	}

	public void setMessage(String msg) {
		message.setText(msg);
	}

	public void setMessage(int msg) {
		message.setText(msg);
	}

	//
	public void setConfirm(String value) {
		btnConfirm.setText(value);
	}

	public void setConfirm(int value) {
		btnConfirm.setText(value);
	}

	private DialogClickListener exitClickListener;

	public interface DialogClickListener {
		void onConfirmClickListener();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (exitClickListener != null) {
				exitClickListener.onConfirmClickListener();
			}
			dismiss();
		}
	}

}
