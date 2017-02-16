package com.huiyin.ui.bespeak;

import java.util.ArrayList;
import com.huiyin.R;
import com.huiyin.ui.bespeak.BespeakTitleBean.BespeakTitle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BespeakHorizontialListViewAdapter extends BaseAdapter {

	private ArrayList<BespeakTitle> list;
	private Activity act;
	private int lv_hSelect = 0;

	public BespeakHorizontialListViewAdapter(Activity act,
			ArrayList<BespeakTitle> title) {

		this.act = act;
		list = new ArrayList<BespeakTitleBean.BespeakTitle>();
		BespeakTitleBean bean = new BespeakTitleBean();
		list.add(bean.getAllTitle());
		list.addAll(title);
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public BespeakTitle getItem(int arg0) {

		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	public void setCurrent(int positon) {
		lv_hSelect = positon;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BespeakTitle title = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(act).inflate(
					R.layout.h_listview_item, null);
			holder.title = (TextView) convertView
					.findViewById(R.id.h_list_item);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(title.TYPE_NAME);
		holder.title.setSelected(position == lv_hSelect ? true : false);
		convertView
				.setBackgroundResource(position == lv_hSelect ? R.color.index_red
						: R.color.white);
		return convertView;
	}

	private class ViewHolder {
		TextView title;
	}
}
