package com.huiyin.ui.show;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ShowLike;
import com.huiyin.ui.show.adapter.TheShowLikeAdapter;
import com.huiyin.utils.MyCustomResponseHandler;

public class TheShowlikeActivity extends BaseActivity implements
		OnClickListener {
	private ListView lv;
	private TextView left_ib;
	public ShowLike data;
	public String id;

	TheShowLikeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theshow_like);
		id = getIntent().getStringExtra("userid");
		lv = (ListView) this.findViewById(R.id.theshow_like_lv);
		left_ib = (TextView) this.findViewById(R.id.left_ib);

		left_ib.setOnClickListener(this);
		adapter = new TheShowLikeAdapter(this);
		lv.setAdapter(adapter);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.theshow_like_title);
		TextView tv_title = (TextView) rl.findViewById(R.id.middle_title_tv);
		tv_title.setText("喜欢过的用户");
		TextView tv_title_right = (TextView) rl.findViewById(R.id.right_ib);
		tv_title_right.setVisibility(View.INVISIBLE);

		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data = ShowLike.explainJson(content, mContext);

				if (data.type == 1) {
					if (data.like != null && data.like.size() > 0) {
						adapter.addItem(data.like);
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		RequstClient.appLike(handler, id + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_ib:
			finish();
			break;

		}

	}

}
