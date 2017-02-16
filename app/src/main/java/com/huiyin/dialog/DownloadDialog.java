package com.huiyin.dialog;

import com.huiyin.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadDialog extends Dialog implements OnClickListener {

	private Button btnCancel;
	private TextView title;
	private TextView message;
	private ProgressBar progressBar;

	public DownloadDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		setContentView(R.layout.dialog_download);
		title = (TextView) findViewById(R.id.title);
		message = (TextView) findViewById(R.id.message);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
	}

	public void setMyClickListener(MyCancelClickListener clickListener) {
		cancelClickListener = clickListener;
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

	private MyCancelClickListener cancelClickListener;

	public interface MyCancelClickListener {
		void onCancelClickListener();
	}
	
	
	public TextView getMessage() {
		return message;
	}


	public ProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void onClick(View v) {
		if (v == btnCancel) {
			if (cancelClickListener != null) {
				cancelClickListener.onCancelClickListener();
			}
			dismiss();
		}
	}

}
