package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.bean.SearchHistroyListResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchListAdapter extends BaseAdapter {

	private Context context;
	private SearchHistroyListResult bean;

	public SearchListAdapter(Context context, SearchHistroyListResult bean3) {
		this.context = context;
		this.bean = bean3;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bean.resultList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bean.resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (v != null) {
			holder = (ViewHolder) v.getTag();
		} else {
			v = LayoutInflater.from(context).inflate(R.layout.search_list_item, null);
			holder = new ViewHolder();
			holder.tv_searchlist = (TextView) v.findViewById(R.id.tv_searchlist);
			v.setTag(holder);
		}
		// 三级分类列表的标题
		if (bean != null) {
			holder.tv_searchlist.setText(bean.resultList.get(position).CATEGORY_NAME);
		} else {
			Toast.makeText(context, "数据异常哦...", 0).show();
		}
		holder.tv_searchlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onItemClick(bean.resultList.get(position).CATEGORY_NAME);
				}
			}
		});

		return v;
	}

	private static class ViewHolder {
		private TextView tv_searchlist;
	}

	public interface MyOnItemClickListener {
		void onItemClick(String value);
	}

	private MyOnItemClickListener listener;

	public void setOnMyOnItemClickListener(MyOnItemClickListener listener) {
		this.listener = listener;
	}
}
