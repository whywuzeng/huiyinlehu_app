package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.GoodsDetailBeen.CombineProduct;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.ViewHolder;

public class GoodsDetailGroupAdapter extends BaseAdapter {
	private ArrayList<CombineProduct> list;
	private Context mContext;
	final static int TYPE_UNSELECT = 0;
	final static int TYPE_SELECT = 1;
	private int[] checklist;
	private OnSelectListener monSelectListener;

	public GoodsDetailGroupAdapter(Context mContext) {
		list = new ArrayList<CombineProduct>();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.activity_goods_detail_groupmenu_listitem, parent,
					false);
		}
		ImageView image = ViewHolder
				.get(convertView, R.id.imageView_chanpin_ic);
		TextView name = ViewHolder.get(convertView, R.id.tv_name_shangping);
		TextView price = ViewHolder.get(convertView,
				R.id.tv_yuhujiange_show_show);
		TextView count = ViewHolder.get(convertView, R.id.tv_zxs);
		CheckBox check = ViewHolder.get(convertView, R.id.group_checkBox);

		CombineProduct temp = list.get(position);

		if (!StringUtils.isBlank(temp.COMMODITY_IMAGE_PATH))
			ImageManager.LoadWithServer(temp.COMMODITY_IMAGE_PATH, image);
		name.setText(temp.COMMODITY_NAME);
		price.setText(MathUtil.priceForAppWithSign(temp.PRICE));
		count.setText("已售出 " + temp.SALES_VOLUME + " 件");
		switch (checklist[position]) {
		case TYPE_UNSELECT:
			check.setChecked(false);
			break;
		case TYPE_SELECT:
			check.setChecked(true);
			break;
		}
		check.setOnCheckedChangeListener(null);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSelected = checklist[position] == TYPE_SELECT;
				checklist[position] = isSelected ? TYPE_UNSELECT : TYPE_SELECT;
				notifyDataSetChanged();
				if(monSelectListener!=null)
					monSelectListener.OnSelectChange(position,!isSelected);
			}
		});
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<CombineProduct> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
			checklist = new int[list.size()];
			for (int i = 0; i < list.size(); i++)
				checklist[i] = TYPE_UNSELECT;
		}
		notifyDataSetChanged();
	}

	public interface OnSelectListener {
		public void OnSelectChange(int positon, boolean isSelect);
	}

	public void setMonSelectListener(OnSelectListener monSelectListener) {
		this.monSelectListener = monSelectListener;
	}

	public int[] getChecklist() {
		return checklist;
	}
	
}
