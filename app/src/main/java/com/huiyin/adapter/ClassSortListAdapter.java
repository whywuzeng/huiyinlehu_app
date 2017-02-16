package com.huiyin.adapter;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ClassListTuPianBean;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassSortListAdapter extends BaseAdapter {

	private ClassListTuPianBean bean;
	private Context mContext;

	public ClassSortListAdapter(ClassListTuPianBean bean, Context mContext) {
		this.bean = bean;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return bean.commodityList == null ? 0 : bean.commodityList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return bean.commodityList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View v, ViewGroup arg2) {
		ViewHolder holder = null;
		if (v != null) {
			holder = (ViewHolder) v.getTag();
		} else {
			holder = new ViewHolder();
			v = LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_normal_mode, null);
			holder.class_sort_list1_ll_iv = (ImageView) v.findViewById(R.id.class_sort_list1_ll_iv);
			holder.class_sort_list1_ll_title = (TextView) v.findViewById(R.id.class_sort_list1_ll_title);
			holder.class_sort_list1_ll_jiage1 = (TextView) v.findViewById(R.id.class_sort_list1_ll_jiage1);
			holder.class_sort_list1_ll_jiage2 = (TextView) v.findViewById(R.id.class_sort_list1_ll_jiage2);
			holder.class_sort_list1_ll_haopingdu = (TextView) v.findViewById(R.id.class_sort_list1_ll_haopingdu);
			v.setTag(holder);
		}

		ImageManager.LoadWithServer(bean.commodityList.get(arg0).COMMODITY_IMAGE_PATH, holder.class_sort_list1_ll_iv);
//		ImageManager.Load(URLs.SERVER_URL1 + bean.commodityList.get(arg0).COMMODITY_IMAGE_PATH, holder.class_sort_list1_ll_iv);
		holder.class_sort_list1_ll_title.setText(bean.commodityList.get(arg0).COMMODITY_NAME);
		if(bean.commodityList.get(arg0).MARK!=-1) {
			holder.class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).PROMOTIONS_PRICE));
		} else {
			holder.class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).PRICE));
		}
		
		if (bean.commodityList.get(arg0).COUNTREVIEW != null
				&& MathUtil.stringToFloat(bean.commodityList.get(arg0).COUNTREVIEW) != 0) {
			holder.class_sort_list1_ll_haopingdu.setText(MathUtil.keepMost2Decimal(MathUtil.stringToFloat(bean.commodityList
					.get(arg0).COUNTREVIEW) * 100.0f) + "%");
		} else {
			holder.class_sort_list1_ll_haopingdu.setText("100%");
		}
		if (StringUtils.isBlank(bean.commodityList.get(arg0).REVIEW_NUMBER)) {
			String hh = holder.class_sort_list1_ll_haopingdu.getText().toString();
			holder.class_sort_list1_ll_haopingdu.setText(hh + "(0人)");
		} else {
			String hh = holder.class_sort_list1_ll_haopingdu.getText().toString();
			holder.class_sort_list1_ll_haopingdu.setText(hh + "(" + bean.commodityList.get(arg0).REVIEW_NUMBER + "人)");
		}
		// 给textview中间加横线
		holder.class_sort_list1_ll_jiage2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		// 参考价
		holder.class_sort_list1_ll_jiage2.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).REFERENCE_PRICE));
		return v;
	}

	private class ViewHolder {
		ImageView class_sort_list1_ll_iv;
		TextView class_sort_list1_ll_title;
		TextView class_sort_list1_ll_jiage1;
		TextView class_sort_list1_ll_jiage2;
		TextView class_sort_list1_ll_haopingdu;
	}
}
