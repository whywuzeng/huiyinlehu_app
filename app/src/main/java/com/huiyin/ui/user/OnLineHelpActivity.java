package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.OnLineHelpActivity.TitleBean.TitleItem;
import com.huiyin.ui.user.OnLineHelpActivity.TitleBean.TitleList;
import com.huiyin.utils.LogUtil;

public class OnLineHelpActivity extends BaseActivity {

	public final static String TAG = "OnLineHelpActivity";
	TextView left_ib, middle_title_tv;
	String title, flag;// 标题和标题所对应的flag
	public static final String INTRODUCE = "1", BUY_SYSTEM = "2",
			MEMBER_SYSTEM = "3", JIFEN = "4", CODE = "5";
	TitleBean mTitleBean;

	LayoutInflater mInflater;
	LinearLayout help_add_title_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_help);

		initView();
		initData();
	}

	/**
	 * 获取数据
	 */
	private void initData() {

		RequstClient.onLineHelp(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "onLineHelp:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					mTitleBean = new Gson().fromJson(content, TitleBean.class);
					refreshUI();

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * 更新界面
	 */
	private void refreshUI() {

		if (mTitleBean == null) {
			return;
		}
		for (int i = 0; i < mTitleBean.helpTitle.size(); i++) {
			TitleList item = mTitleBean.helpTitle.get(i);
			View help_1 = mInflater.inflate(R.layout.help_layout_1, null);
			TextView help_big_title = (TextView) help_1
					.findViewById(R.id.help_big_title);
			help_big_title.setText(item.CATEGORY_NAME);
			help_add_title_layout.addView(help_1);
			if (item.titleList == null) {
				View help_3 = mInflater.inflate(R.layout.help_layout_3, null);
				help_add_title_layout.addView(help_3);
				continue;

			} else {
				for (int j = 0; j < item.titleList.size(); j++) {
					final TitleItem titleItem = item.titleList.get(j);
					View help_2 = mInflater.inflate(R.layout.help_layout_2,
							null);
					TextView help_small_title = (TextView) help_2
							.findViewById(R.id.help_small_title);
					help_small_title.setText(titleItem.title);
					help_small_title.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							title = titleItem.title;
							flag = titleItem.id;

							Intent i = new Intent(OnLineHelpActivity.this,
									CommonWebPageActivity.class);
							i.putExtra("title", title);
							i.putExtra("flag", flag);
							startActivity(i);
						}
					});
					help_add_title_layout.addView(help_2);
				}
				if (i != mTitleBean.helpTitle.size() - 1) {
					View help_3 = mInflater.inflate(R.layout.help_layout_3,
							null);
					help_add_title_layout.addView(help_3);
				}

			}

		}
	}

	/**
	 * 初始化
	 */
	private void initView() {

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("使用帮助");
		help_add_title_layout = (LinearLayout) findViewById(R.id.help_add_title_layout);

		mTitleBean = new TitleBean();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public class TitleBean {

		ArrayList<TitleList> helpTitle = new ArrayList<TitleList>();

		public class TitleList {
			ArrayList<TitleItem> titleList = new ArrayList<TitleItem>();
			String CATEGORY_NAME;
		}

		public class TitleItem {
			String id;
			String title;
		}

	}

}
