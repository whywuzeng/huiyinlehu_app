package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.Lehujuan;
import com.huiyin.utils.MathUtil;

public class ChooseLehjuanAdapter extends BaseAdapter {

	private Context mContext;

	private List<Lehujuan> listDatas;
	public LayoutInflater layoutInflater;

	public ChooseLehjuanAdapter(Context context, List<Lehujuan> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.activity_lehujuan_choose_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Lehujuan bean = listDatas.get(position);
		holder.name.setText("面值" + MathUtil.priceForAppWithSign(bean.getAmount()) + "乐虎劵");
		holder.date.setText("到期时间：" + bean.getDate());

		if (bean.isChecked()) {
			holder.checkbox.setChecked(true);
		} else {
			holder.checkbox.setChecked(false);
		}

		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				listener.onCheckChage(arg1, position);
			}
		});

		return convertView;
	}

	private LehuCheckChange listener;

	public interface LehuCheckChange {
		void onCheckChage(boolean check, int position);
	}

	public void setOnLehuCheckChange(LehuCheckChange lehuCheckChange) {
		this.listener = lehuCheckChange;
	}

	private class ViewHolder {
		TextView name;
		TextView date;
		CheckBox checkbox;
	}

}