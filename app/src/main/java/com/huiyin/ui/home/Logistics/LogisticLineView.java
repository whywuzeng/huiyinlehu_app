package com.huiyin.ui.home.Logistics;

import com.huiyin.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogisticLineView extends LinearLayout {
	private Context mContext;
	private OrderLogisticsListBean data;
	private LinearLayout addto;
	private TextView time, dis;
	private String[] miaoshuType = { "商品未出库", "商品已发货", "商品已签收" };

	public LogisticLineView(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater.from(context).inflate(
				R.layout.logistics_query_line_base, this, true);
		findView();

	}

	public LogisticLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(
				R.layout.logistics_query_line_base, this, true);
		findView();

	}

	public LogisticLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		LayoutInflater.from(context).inflate(
				R.layout.logistics_query_line_base, this, true);
		findView();

	}

	private void findView() {

		addto = (LinearLayout) findViewById(R.id.add_to);
		time = (TextView) findViewById(R.id.textView_time);
		dis = (TextView) findViewById(R.id.textView_miaoshu);
	}

	private void InitView() {
		if (data.getTYPE() == 0) {
			time.setText(data.getDELIVERYTIME().trim());
			dis.setText(miaoshuType[0]);
			return;
		}
		if (data.getTYPE() == 1) {
			addto.removeAllViews();
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.logistics_query_line, null);
			TextView timeTextView = (TextView) v
					.findViewById(R.id.textView_time);
			TextView disTextView = (TextView) v
					.findViewById(R.id.textView_miaoshu);
			timeTextView.setText(data.getDELIVERYTIME().trim());
			disTextView.setText(miaoshuType[0]);
			addto.addView(v);

			time.setText(data.getWORKTIME().trim());
			dis.setText(miaoshuType[1]);
			return;
		}
		if (data.getTYPE() == 2) {
			addto.removeAllViews();
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.logistics_query_line, null);
			TextView timeTextView = (TextView) v
					.findViewById(R.id.textView_time);
			TextView disTextView = (TextView) v
					.findViewById(R.id.textView_miaoshu);
			timeTextView.setText(data.getDELIVERYTIME().trim());
			disTextView.setText(miaoshuType[0]);
			addto.addView(v);

			View v1 = LayoutInflater.from(mContext).inflate(
					R.layout.logistics_query_line, null);
			TextView timeTextView1 = (TextView) v1
					.findViewById(R.id.textView_time);
			TextView disTextView1 = (TextView) v1
					.findViewById(R.id.textView_miaoshu);
			timeTextView1.setText(data.getWORKTIME().trim());
			disTextView1.setText(miaoshuType[1]);
			addto.addView(v1);

			time.setText(data.getRETURNTIME().trim());
			dis.setText(miaoshuType[2]);
			return;
		}
	}

	public void setData(OrderLogisticsListBean data) {
		this.data = data;
		InitView();
	}
}
