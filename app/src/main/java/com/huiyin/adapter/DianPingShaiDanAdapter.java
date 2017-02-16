package com.huiyin.adapter;

import java.util.ArrayList;
import com.huiyin.R;
import com.huiyin.bean.DianPingShaiDanList;
import com.huiyin.bean.DianPingShaiDanList.DianPing;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.ui.show.adapter.ShowSharePicGridViewAdapter;
import com.huiyin.wight.MyGridView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DianPingShaiDanAdapter extends BaseAdapter {

	private Context content;
	private LayoutInflater inflater;
	private ArrayList<DianPing> dianPingList;

	public DianPingShaiDanAdapter(Context content) {

		this.content = content;
		dianPingList = new ArrayList<DianPingShaiDanList.DianPing>();
		inflater = LayoutInflater.from(content);
	}

	public void addDianPingList(ArrayList<DianPing> list) {

		if (list == null || list.size() == 0) {
			return;
		}
		if (list instanceof ArrayList) {
			dianPingList.clear();
			dianPingList.addAll(list);
			notifyDataSetChanged();
		}
	}

	public void addMoreDianPingList(ArrayList<DianPing> list) {

		if (list == null || list.size() == 0) {
			return;
		}
		if (list instanceof ArrayList) {
			dianPingList.addAll(list);
			notifyDataSetChanged();
		}
	}

	public void clearDianPingList() {

		dianPingList.clear();
		notifyDataSetChanged();
	}

	public ArrayList<String> getIMGList(String str) {
		ArrayList<String> slist = new ArrayList<String>();
		slist.clear();
		String[] s = str.split(",");
		for (int i = 0; i < s.length; i++) {
			slist.add(s[i]);
		}
		return slist;
	}

	@Override
	public int getCount() {

		return dianPingList.size();
	}

	@Override
	public Object getItem(int position) {

		return dianPingList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.dian_ping_shai_dan_lv, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_username);
			holder.iv_pingxing = (ImageView) convertView
					.findViewById(R.id.dian_ping_shai_dan_pingxing);
			holder.tv_create_time = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_create_time);
			holder.share_pic = (MyGridView) convertView
					.findViewById(R.id.grv_share_pic);
			holder.tv_pingjia = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_pingjia);
			holder.tv_sepc_value = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_sepc_value);
			holder.tv_buy_time = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_buy_time);
			holder.reply_layout = (LinearLayout) convertView
					.findViewById(R.id.dian_ping_shai_dan_reply);
			holder.reply_time = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_reply_time);
			holder.reply_content = (TextView) convertView
					.findViewById(R.id.dian_ping_shai_dan_reply_content);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		DianPing dianPing = dianPingList.get(position);

		if (dianPing.USER_NAME != null) {

			holder.tv_name.setText(dianPing.USER_NAME);
		} else {

			holder.tv_name.setText(dianPing.PHONE);
		}

		String score = dianPing.SCORE == null ? "" : dianPing.SCORE;
		if (score.equals("5")) {
			holder.iv_pingxing.setImageResource(R.drawable.px_5);
		} else if (score.equals("4")) {
			holder.iv_pingxing.setImageResource(R.drawable.px_4);
		} else if (score.equals("3")) {
			holder.iv_pingxing.setImageResource(R.drawable.px_3);
		} else if (score.equals("2")) {
			holder.iv_pingxing.setImageResource(R.drawable.px_2);
		} else if (score.equals("1")) {
			holder.iv_pingxing.setImageResource(R.drawable.px_1);
		} else {
			holder.iv_pingxing.setImageResource(R.drawable.px_0);
		}

		holder.tv_create_time.setText(dianPing.CREATE_TIME);
		holder.tv_pingjia.setText(dianPing.CONTENT);

		String sepc_value = dianPing.SEPC_VALUE;
		if (sepc_value == null || "".equals(sepc_value)) {
			holder.tv_sepc_value.setVisibility(View.GONE);
		} else {
			holder.tv_sepc_value.setVisibility(View.VISIBLE);
			holder.tv_sepc_value.setText(dianPing.CONTENT);
		}

		holder.tv_buy_time.setText(dianPing.BUY_TIME);

		String pic = dianPing.COMMODITY_IMAGE_PATH;

		final ShowSharePicGridViewAdapter spadapter = new ShowSharePicGridViewAdapter(
				content);
		if (pic != null) {
			spadapter.addItem(getIMGList(pic));
			if (getIMGList(pic).size() == 2) {
				holder.share_pic.setNumColumns(2);
			} else if (getIMGList(pic).size() == 4) {
				holder.share_pic.setNumColumns(2);
			} else if (getIMGList(pic).size() == 1) {
				holder.share_pic.setNumColumns(2);
			} else {
				holder.share_pic.setNumColumns(3);
			}
			holder.share_pic.setAdapter(spadapter);
			holder.share_pic.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.setClass(content, PhotoViewActivity.class);
					intent.putStringArrayListExtra(
							PhotoViewActivity.INTENT_KEY_PHOTO,
							spadapter.getListDatas());
					intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION,
							position);
					content.startActivity(intent);
				}
			});
		} else {
			holder.share_pic.setAdapter(spadapter);
		}

		if (dianPing.REPLY_STATUS != null && dianPing.REPLY_STATUS.equals("1")) {
			holder.reply_layout.setVisibility(View.VISIBLE);
			holder.reply_time.setText("管理员" + dianPing.REPLY_TIME + "回复：");
			holder.reply_content.setText(dianPing.REPLY_CONTENT);
		} else {
			holder.reply_layout.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		ImageView iv_pingxing;
		TextView tv_create_time;
		MyGridView share_pic;
		TextView tv_pingjia;
		TextView tv_sepc_value;
		TextView tv_buy_time;
		LinearLayout reply_layout;
		TextView reply_time;
		TextView reply_content;
	}

}
