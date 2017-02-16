package com.huiyin.ui.show;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.AppShow;
import com.huiyin.bean.AppShowAddAttention;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.bean.AppShowCancelAttention;
import com.huiyin.ui.show.adapter.ShowAdapter;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.ui.show.interf.OnAttentionListener;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class AllFragment extends Fragment {

	private SearchView searchView;
	private MyHandler handler;
	private ScheduledExecutorService scheduledExecutor = Executors
			.newScheduledThreadPool(3);
	private String currentSearchTip;
	private ShowAdapter mAdapter;
	private Context mContext;
	private int pageIndex = 1;
	public String user_id;

	AppShow data;
	AppShowAddLike data1;
	AppShowAddAttention data2;
	AppShowCancelAttention data3;
	XListView mlistview;
	public List<String> list = new ArrayList<String>();
	public List<String> keywordlist = new ArrayList<String>();
	public ArrayAdapter<String> adapter;

	LinearLayout layout_search;

	ListView listView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContext = getActivity();
		user_id = AppContext.getInstance().getUserId();
		View view = inflater.inflate(R.layout.fragment_all, null);

		layout_search = (LinearLayout) view.findViewById(R.id.layout_search);
		layout_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						TheShowSearchActivity.class);
				getActivity().startActivity(intent);
			}
		});

		listView = (ListView) view.findViewById(R.id.listView_search);
		mlistview = (XListView) view.findViewById(R.id.listView_attention);
		handler = new MyHandler();
		adapter = new ArrayAdapter<String>(mContext,
				R.layout.show_search_listview_item, list);
		listView.setAdapter(adapter);
		// 设置当在搜索栏里输入内容时，内容在一个小框框中显示（类似textview），若没有这行代码则没有这样的效果
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					if (keywordlist.get(0) != null
							&& !"".equals(keywordlist.get(0))) {

						SearchData(keywordlist.get(0), 1);
					}
					break;
				case 1:
					if (keywordlist.get(0) != null
							&& !"".equals(keywordlist.get(0))) {

						SearchData(keywordlist.get(0), 2);
					}
					break;
				}

				list.clear();
				keywordlist.clear();
				adapter.notifyDataSetChanged();
				mAdapter.notifyDataSetChanged();
				searchView.clearAnimation();
			}
		});
		searchView = (SearchView) view.findViewById(R.id.search_view);

		searchView.setIconifiedByDefault(false);
		searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				// to avoid click x button and the edittext hidden
				return true;
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			public boolean onQueryTextChange(String newText) {
				if (newText != null && newText.length() > 0) {
					currentSearchTip = newText;
					showSearchTip(newText);
				} else {

					listView.clearTextFilter();
					list.clear();
					adapter.notifyDataSetChanged();
				}
				return true;
			}
		});

		searchView.setSubmitButtonEnabled(false);

		mAdapter = new ShowAdapter(mContext);
		mlistview.setAdapter(mAdapter);
		mlistview.setPullLoadEnable(true);
		mlistview.setPullRefreshEnable(true);
		mlistview.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				initData();
			}

			@Override
			public void onLoadMore() {
				initData();
			}
		});
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

		

		return view;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		pageIndex = 1;
		initData();
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
					pageIndex = 1;
					initData();
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
		if (user_id != null && !"".equals(user_id)) {

			if (user_id.equals(spotlightUserId + "")) {
				Toast.makeText(mContext, "不能关注自己！", Toast.LENGTH_SHORT).show();
			} else {

				RequstClient.appAttention(handler, user_id, spotlightUserId
						+ "");
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
					pageIndex = 1;
					initData();
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
		if (user_id != null && !"".equals(user_id)) {

			RequstClient.appCancelAttention(handler, user_id, spotlightUserId
					+ "");
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
					pageIndex = 1;
					initData();
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
		if (user_id != null && !"".equals(user_id)) {

			RequstClient.appShowLike(handler, user_id, spotlightId + "");
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void SearchData(String keyword, int flag) {
		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

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

	public void showSearchTip(String newText) {
		// excute after 500ms, and when excute, judge current search tip and
		// newText
		schedule(new SearchTipThread(newText), 500);
	}

	class SearchTipThread implements Runnable {

		String newText;

		public SearchTipThread(String newText) {
			this.newText = newText;
		}

		public void run() {
			// keep only one thread to load current search tip, u can get data
			// from network here
			if (newText != null && newText.equals(currentSearchTip)) {
				handler.sendMessage(handler.obtainMessage(1, newText));
			}
		}
	}

	public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
		return scheduledExecutor.schedule(command, delayTimeMills,
				TimeUnit.MILLISECONDS);
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				String str = (String) msg.obj;
				list.clear();
				list.add("会员: " + str);
				list.add("所有: " + str);
				keywordlist.clear();
				keywordlist.add(str);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	}

	private void initData() {
		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {
				mlistview.stopLoadMore();
				mlistview.stopRefresh();
				data = AppShow.explainJson(content, mContext);

				if (data.type == 1) {
					if (data.spotlight != null
							&& data.spotlight.pageBean != null
							&& data.spotlight.pageBean.size() > 0) {

						if (data.spotlight.pageBean.size() < 10) {

							mlistview.hideFooter();
							mlistview.setPullLoadEnable(false);
						} else {

							mlistview.showFooter();
							mlistview.setPullLoadEnable(true);
						}
						if (pageIndex == 1) {
							mAdapter.addItem(data.spotlight.pageBean);
						} else {

							mAdapter.addMoreItem(data.spotlight.pageBean);
						}
					} else {

						if (pageIndex == 1) {

							mAdapter.deleteItem();
							Toast.makeText(getActivity(), "暂无数据！",
									Toast.LENGTH_SHORT).show();
							mlistview.hideFooter();
							mlistview.setPullLoadEnable(false);
						} else {

							Toast.makeText(getActivity(), "已无更多数据！",
									Toast.LENGTH_SHORT).show();
							mlistview.hideFooter();
							mlistview.setPullLoadEnable(false);
						}
					}

					mAdapter.notifyDataSetChanged();
					pageIndex++;
				} else {

					Toast.makeText(mContext, data.msg, 0).show();
					mlistview.hideFooter();
					mlistview.setPullLoadEnable(false);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mlistview.stopLoadMore();
				mlistview.stopRefresh();
				return;
			}
		};
		if (AppContext.getInstance().getUserId() == null) {
			// 未登录状态下 推荐接口
			RequstClient.appShow(handler, "0", 10 + "", pageIndex + "");
		} else {
			// 登录状态下 推荐接口
			RequstClient.appNoShow(handler, "0", AppContext.getInstance().getUserId(), 10 + "",
					pageIndex + "");
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

	}

}
