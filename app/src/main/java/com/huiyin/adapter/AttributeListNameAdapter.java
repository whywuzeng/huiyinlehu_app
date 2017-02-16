package com.huiyin.adapter;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.bean.CommodityTypeName.TypeName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AttributeListNameAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<TypeName> propertValueList;
	
	public AttributeListNameAdapter(Context context, ArrayList<TypeName> propertValueList) {
		this.context = context;
		this.propertValueList =propertValueList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return propertValueList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return propertValueList.get(position);
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
			v = LayoutInflater.from(context).inflate(R.layout.attribut_name_list_item, null);
			holder = new ViewHolder();
			holder.tv_attribute = (TextView) v.findViewById(R.id.tv_attribute);
			holder.iv_selecte = (CheckBox)v.findViewById(R.id.iv_selecte);
			holder.layout_attribute = (RelativeLayout)v.findViewById(R.id.layout_attribute);
			v.setTag(holder);
		}
		
		TypeName bean = propertValueList.get(position);
		holder.layout_attribute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickListener.onItemclickListener(position);
			}
		});
		
		if(bean.isSelected()){
			holder.iv_selecte.setChecked(true);
		}else {
			holder.iv_selecte.setChecked(false);
		}
		holder.tv_attribute.setText(propertValueList.get(position).NAME);
		return v;
	}

	private static class ViewHolder {
		private TextView tv_attribute;
		private CheckBox iv_selecte;
		private RelativeLayout layout_attribute;
	}

	private OnMyItemClickListener clickListener;
	public void setOnMyItemClickListener(OnMyItemClickListener clickListener){
		this.clickListener=clickListener;
	}
	public interface OnMyItemClickListener{
		public void onItemclickListener(int position);
	}
	
}
