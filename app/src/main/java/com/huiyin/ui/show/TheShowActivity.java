package com.huiyin.ui.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.ui.user.LoginActivity;

public class TheShowActivity extends FragmentActivity implements
		OnClickListener, OnCheckedChangeListener {

	private TextView ab_back, ab_title;
	private ImageView ab_right_btn;

	private FragmentManager fragmentManager;
	private AttentionFragment attentionFragment;
	private RecommendFragment recommendFragment;
	private AllFragment allFragment;

	private RadioGroup main_radio;

	private int currentIndex = R.id.theshow_all;

	private RadioButton theshow_tuijian, theshow_guanzhu, theshow_all;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_the_show);

		fragmentManager = getSupportFragmentManager();

		initView();
	}

	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
		ab_right_btn.setImageResource(R.drawable.ab_ic_add);
		ab_back.setOnClickListener(this);
		ab_right_btn.setOnClickListener(this);
		ab_title.setText("秀场");

		main_radio = (RadioGroup) findViewById(R.id.main_radio);

		main_radio.setOnCheckedChangeListener(this);
		theshow_tuijian = (RadioButton) findViewById(R.id.theshow_tuijian);
		theshow_guanzhu = (RadioButton) findViewById(R.id.theshow_guanzhu);
		theshow_all = (RadioButton) findViewById(R.id.theshow_all);

		main_radio.check(currentIndex);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ab_back:
			finish();
			break;
		case R.id.ab_right_btn:
			// 登录判断
			if (AppContext.getInstance().getUserId() == null) {
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
				return;
			} else {
				startActivity(new Intent(getApplicationContext(),
						TheShowReleaseActivity.class));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.theshow_tuijian:
			if (attentionFragment == null) {
				attentionFragment = new AttentionFragment();
			}
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.attion, attentionFragment);
			transaction.commit();
			theshow_tuijian.setSelected(true);
			theshow_guanzhu.setSelected(false);
			theshow_all.setSelected(false);
			break;
		case R.id.theshow_guanzhu:
			if (AppContext.getInstance().getUserId() == null) {
				// 判断登录
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
			}
			if (recommendFragment == null) {
				recommendFragment = new RecommendFragment();
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.attion, recommendFragment);
			transaction.commit();
			theshow_tuijian.setSelected(false);
			theshow_guanzhu.setSelected(true);
			theshow_all.setSelected(false);
			break;
		case R.id.theshow_all:
			if (allFragment == null) {
				allFragment = new AllFragment();
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.attion, allFragment);
			transaction.commit();

			theshow_tuijian.setSelected(false);
			theshow_guanzhu.setSelected(false);
			theshow_all.setSelected(true);
			break;
		}
		currentIndex = checkedId;
	}

}
