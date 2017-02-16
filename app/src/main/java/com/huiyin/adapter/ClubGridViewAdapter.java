package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;

public class ClubGridViewAdapter extends BaseAdapter {
	private List<String> lists = new ArrayList<String>();;
	public String str = "0";
	private Context ct;

	public ClubGridViewAdapter() {

	}

	public ClubGridViewAdapter(Context ct) {
		this.ct = ct;
	}

	public ClubGridViewAdapter(Context ct, String str) {
		this.ct = ct;
		if (str != null) {
			this.str = str;
			toNumber(str);
		}
	}

	/*
	 * public NewTodayListAdapter(Context ct, List<String> newlists) { this.ct =
	 * ct; this.lists = newlists; }
	 */

	@Override
	public int getCount() {
		if (lists.size() > 0) {
			return lists.size();
		} else {
			return 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
		// return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ct, R.layout.lottery_num_item, null);
		TextView lottery_num = (TextView) view.findViewById(R.id.tv_lottery_num);
		if (lists.size() > 0) {
			lottery_num.setText(lists.get(position));
		} else {
			lottery_num.setText("0");
		}
		return view;
	}

	public void toNumber(String str) {
		String s = replaceBlank(str);
		for (int i = 0; i < s.length(); i++) {
			lists.add(s.charAt(i) + "");
		}
	}

	public String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll(" ");
		}
		return dest;
	}
}
