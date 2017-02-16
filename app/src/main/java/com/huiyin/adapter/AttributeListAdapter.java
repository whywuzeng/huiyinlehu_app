package com.huiyin.adapter;

import java.util.ArrayList;

import com.huiyin.R;
import com.huiyin.bean.ClassListLevel3Bean;
import com.huiyin.bean.CommodityTypeName;
import com.huiyin.bean.SearchHistroyListResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AttributeListAdapter extends BaseAdapter {

	private Context context;
	private CommodityTypeName bean;

	public AttributeListAdapter(Context context, CommodityTypeName bean) {
		this.context = context;
		this.bean = bean;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bean.commodityTypeNameList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bean.commodityTypeNameList.get(position);
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
			v = LayoutInflater.from(context).inflate(R.layout.attribut_list_item, null);
			holder = new ViewHolder();
			holder.tv_attribute = (TextView) v.findViewById(R.id.tv_attribute);
			holder.layout_attribute = (RelativeLayout)v.findViewById(R.id.layout_attribute);
			v.setTag(holder);
		}
		holder.tv_attribute.setText(bean.commodityTypeNameList.get(position).PROPERTY_NAME);
		holder.layout_attribute.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(null!=myOnItemClickListener){
					myOnItemClickListener.onItemmClickListener(position);
				}
			}
		});
		
		return v;
	}

	private static class ViewHolder {
		private TextView tv_attribute;
		private RelativeLayout layout_attribute;
		
	}
	private MyOnItemClickListener myOnItemClickListener;
	public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener){
		this.myOnItemClickListener=myOnItemClickListener;
	}
	public interface MyOnItemClickListener{
		void onItemmClickListener(int position);
	}
}
