package com.huiyin.ui.user;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.ui.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ExitSuccessActivity extends Activity implements OnClickListener {

	private TextView ab_title, timer;
	private Button btn_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit_success);
		initView();
	}

	private void initView() {

		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("系统提示");

		timer = (TextView) findViewById(R.id.timer);

		btn_home = (Button) findViewById(R.id.btn_home);
		btn_home.setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_home:// 返回首页
			backToMainActivity();
			break;
		}
	}

	private void backToMainActivity() {
		AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	private int count = 5;

	private static final int HANDLER_TIME_FINISH = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_TIME_FINISH:
				if (count > 0) {
					count--;
					mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
					timer.setText(count + "s后将带您返回首页");
				} else {
					backToMainActivity();
				}
				break;
			default:
				break;
			}
		}
	};

	public void onBackPressed() {
		backToMainActivity();
	};

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(HANDLER_TIME_FINISH);
		super.onDestroy();
	};
}
