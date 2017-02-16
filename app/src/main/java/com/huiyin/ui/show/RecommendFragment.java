package com.huiyin.ui.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.AppShowAddAttention;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.bean.AppShowAttention;
import com.huiyin.bean.AppShowCancelAttention;
import com.huiyin.ui.show.adapter.ShowAttentionAdapter;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.ui.show.interf.OnAttentionListener;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class RecommendFragment extends Fragment {

	private ShowAttentionAdapter mAdapter;
	private Context mContext;
	private int pageIndex = 1;
	public String userid = "";

	AppShowAttention data;
	AppShowAddLike data1;
	AppShowAddAttention data2;
	AppShowCancelAttention data3;
	private XListView listview;

	boolean load_flag;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View view1 = inflater.inflate(R.layout.fragment_attention1, null);
		listview = (XListView) view1.findViewById(R.id.listView_attention);
		load_flag = false;
		mAdapter = new ShowAttentionAdapter(mContext);
		listview.setAdapter(mAdapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(new IXListViewListener() {

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
			public void addCancelAttention(int spotlightId) {
				CancelAttentionData(spotlightId);
			}

			public void addAttention(int spotlightId) {
				AttentionData(spotlightId);
			}
		});

		return view1;
	}

	@Override
	public void onResume() {
		super.onResume();
		userid = AppContext.getInstance().getUserId();
		pageIndex = 1;
		initData();
	}

	private void AttentionData(int spotlightId) {
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
		if (userid != null && !"".equals(userid)) {

			if (userid.equals(spotlightId + "")) {
				Toast.makeText(mContext, "不能关注自己！", Toast.LENGTH_SHORT).show();
			} else {

				RequstClient.appAttention(handler, userid, spotlightId + "");
			}
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void CancelAttentionData(int spotlightId) {
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
		if (userid != null && !"".equals(userid)) {

			RequstClient.appCancelAttention(handler, userid, spotlightId + "");
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
					Toast.makeText(mContext, data1.msg, Toast.LENGTH_SHORT)
							.show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (userid != null && !"".equals(userid)) {

			RequstClient.appShowLike(handler, userid, spotlightId + "");
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_SHORT).show();
		}
	}

	private void initData() {
		if (userid != null && !"".equals(userid)) {
			// 加载网络
			MyCustomResponseHandler handler = new MyCustomResponseHandler(
					mContext, true) {

				@SuppressLint("NewApi")
				@Override
				public void onRefreshData(String content) {
					listview.stopLoadMore();
					listview.stopRefresh();
					data = AppShowAttention.explainJson(content, mContext);

					if (data.type == 1) {
						if (data.list != null && data.list.pageBean != null
								&& data.list.pageBean.size() > 0) {

							if (data.list.pageBean.size() < 10) {

								listview.hideFooter();
								load_flag = true;
								listview.setPullLoadEnable(false);
							} else {

								listview.showFooter();
								listview.setPullLoadEnable(true);
							}
							if (pageIndex == 1) {
								mAdapter.addItem(data.list.pageBean);
							} else {
								mAdapter.addMoreItem(data.list.pageBean);
							}
						} else {
							if (pageIndex == 1) {

								mAdapter.deleteItem();
								Toast.makeText(getActivity(), "暂无数据！",
										Toast.LENGTH_SHORT).show();
								listview.hideFooter();
								load_flag = true;
								listview.setPullLoadEnable(false);
							} else {

								Toast.makeText(getActivity(), "已无更多数据！",
										Toast.LENGTH_SHORT).show();
								listview.hideFooter();
								load_flag = true;
								listview.setPullLoadEnable(false);
							}
						}
						pageIndex++;
					} else {
						Toast.makeText(mContext, data.msg, 0).show();
						listview.hideFooter();
						load_flag = true;
						listview.setPullLoadEnable(false);
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					return;
				}
			};
			RequstClient.appShowAttention(handler, userid, 10 + "", pageIndex
					+ "");
		} else {
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_LONG).show();
			return;
		}
	}
}
