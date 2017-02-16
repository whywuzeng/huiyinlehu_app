package com.huiyin.ui.show;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AppShow;
import com.huiyin.bean.AppShowAddAttention;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.bean.AppShowCancelAttention;
import com.huiyin.ui.show.adapter.ShowAdapter;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.ui.show.interf.OnAttentionListener;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;

public class TheShowSearchActivity extends BaseActivity {

	public static String TAG = "TheShowSearchActivity";

	LinearLayout show_search_option;
	EditText show_search_content;
	ImageView show_search_clear;
	TextView show_search_cancel;

	TextView show_search_tv;

	ListView show_search_listView;

	AlertDialog dialog;

	int flag = 2;
	String keyword;

	AppShow data;
	AppShowAddLike data1;
	AppShowAddAttention data2;
	AppShowCancelAttention data3;

	ShowAdapter mAdapter;

	String searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.theshow_search);
		initView();
		initListener();
	}

	private void initView() {

		show_search_option = (LinearLayout) findViewById(R.id.show_search_option);
		show_search_content = (EditText) findViewById(R.id.show_search_content);
		show_search_clear = (ImageView) findViewById(R.id.show_search_clear);
		show_search_cancel = (TextView) findViewById(R.id.show_search_cancel);

		show_search_tv = (TextView) findViewById(R.id.show_search_tv);

		show_search_listView = (ListView) findViewById(R.id.show_search_listView);
		mAdapter = new ShowAdapter(mContext);
		show_search_listView.setAdapter(mAdapter);
		mAdapter.setmOnAddShowLikeListener(new OnAddShowLikeListener() {

			@Override
			public void addShowLike(int spotlightId) {

				addLikeData(spotlightId);
			}
		});

		mAdapter.setmOnAttentionListener(new OnAttentionListener() {

			@Override
			public void addCancelAttention(int spotlightUserId) {
				CancelAttentionData(spotlightUserId);
			}

			@Override
			public void addAttention(int spotlightUserId) {
				AttentionData(spotlightUserId);

			}
		});
	}

	private void initListener() {

		show_search_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString().isEmpty()) {
					show_search_clear.setVisibility(View.GONE);
				} else {
					show_search_clear.setVisibility(View.VISIBLE);
				}
				mAdapter.deleteItem();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		show_search_content
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						String text;
						text = show_search_content.getText().toString().trim();
						if (StringUtils.isBlank(text)) {
							Toast.makeText(mContext, "搜索内容不能为空!",
									Toast.LENGTH_LONG).show();
							return false;
						}
						/* 判断是否是“搜索”键 */
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							/* 隐藏软键盘 */
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							if (imm.isActive()) {
								imm.hideSoftInputFromWindow(
										v.getApplicationWindowToken(), 0);
							}

							keyword = text;
							SearchData();
							return true;
						}
						return false;
					}
				});

		show_search_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				show_search_content.setText("");
			}
		});

		show_search_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		show_search_option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new Builder(mContext);
				alert.setItems(R.array.show_search_option,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String text;
								switch (which) {
								case 0:
									show_search_content.setHint("搜索");
									flag = 2;
									text = show_search_content.getText()
											.toString().trim();
									if (!StringUtils.isBlank(text)) {
										keyword = text;
										SearchData();
									}
									break;
								case 1:
									show_search_content.setHint("搜会员");
									flag = 1;
									text = show_search_content.getText()
											.toString().trim();
									if (!StringUtils.isBlank(text.trim())) {
										keyword = text;
										SearchData();
									}
									break;
								}
							}
						});
				dialog = alert.create();
				dialog.show();
			}
		});
	}

	private void AttentionData(int spotlightUserId) {
		// 关注网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data2 = AppShowAddAttention.explainJson(content, mContext);

				if (data2.type == 1) {

					Toast.makeText(mContext, "谢谢你关注我", Toast.LENGTH_SHORT)
							.show();
					SearchData();
				} else {

					Toast.makeText(mContext, data2.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (AppContext.getInstance().getUserId() != null && !"".equals(AppContext.getInstance().getUserId())) {

			if (AppContext.getInstance().getUserId().equals(spotlightUserId + "")) {
				Toast.makeText(mContext, "不能关注自己！", Toast.LENGTH_SHORT).show();
			} else {

				RequstClient.appAttention(handler, AppContext.getInstance().getUserId(),
						spotlightUserId + "");
			}
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void CancelAttentionData(int spotlightUserId) {
		// 取消关注网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data3 = AppShowCancelAttention.explainJson(content, mContext);

				if (data3.type == 1) {

					Toast.makeText(mContext, "亲,希望你能够再次关注我", Toast.LENGTH_SHORT)
							.show();
					SearchData();
				} else {

					Toast.makeText(mContext, data3.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (AppContext.getInstance().getUserId() != null && !"".equals(AppContext.getInstance().getUserId())) {

			RequstClient.appCancelAttention(handler, AppContext.getInstance().getUserId(),
					spotlightUserId + "");
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void addLikeData(int spotlightId) {
		// 添加喜欢网络请求
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data1 = AppShowAddLike.explainJson(content, mContext);

				if (data1.type == 1) {

					Toast.makeText(mContext, "谢谢你喜欢我", Toast.LENGTH_SHORT)
							.show();
					SearchData();
				}
				if (data1.type == 2) {
					Toast.makeText(mContext, "亲,你" + data1.msg,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (AppContext.getInstance().getUserId() != null && !"".equals(AppContext.getInstance().getUserId())) {

			RequstClient.appShowLike(handler, AppContext.getInstance().getUserId(), spotlightId
					+ "");
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void SearchData() {
		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				if (getCurrentFocus() != null) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
				}

				data = AppShow.explainJson(content, mContext);

				if (data.type == 1) {
					if (data.spotlight != null
							&& data.spotlight.pageBean != null
							&& data.spotlight.pageBean.size() > 0) {
						mAdapter.deleteItem();

						mAdapter.addItem(data.spotlight.pageBean);
						Toast.makeText(mContext, "数据搜索完成", Toast.LENGTH_SHORT)
								.show();
					} else {
						mAdapter.deleteItem();
						Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT)
								.show();
					}
					mAdapter.notifyDataSetChanged();

				} else {
					mAdapter.deleteItem();
					Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}

		};
		RequstClient.appShowSearch(handler, keyword, flag + "");

	}
}
