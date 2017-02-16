package com.huiyin.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.UpdateVersionTool;

public class AboutActivity extends BaseActivity {

	TextView left_ib, middle_title_tv, phone_bind_tv, vision_textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		initView();
	}

	private void initView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("关于");
		vision_textview = (TextView) findViewById(R.id.vision_textview);
		vision_textview.setText("For Android V"
				+ UpdateVersionTool.getVersionName(this));
	}
}
