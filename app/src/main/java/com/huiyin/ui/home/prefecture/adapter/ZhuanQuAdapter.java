package com.huiyin.ui.home.prefecture.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ZhuanquGoodbean;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZhuanQuAdapter extends BaseAdapter {

	private static final String TAG = "ZhuanQu2Adapter";
	private List<ZhuanquGoodbean> listGoodbeans;
	private Context ct;
	private String layout;

	public ZhuanQuAdapter(Context ct, String layout, List<ZhuanquGoodbean> listGoodbeans) {
		this.ct = ct;
		this.listGoodbeans = listGoodbeans;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return listGoodbeans.size();
	}

	@Override
	public Object getItem(int position) {
		return listGoodbeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (layout.equals("1")) {
			ViewHodlerForLayout1 hodler1 = null;
			if (convertView == null) {
				hodler1 = new ViewHodlerForLayout1();
				convertView = View.inflate(ct, R.layout.zhuanqu_list_item1, null);
				hodler1.iv_img = (ImageView) convertView.findViewById(R.id.zhuan_qu_iv_img);
				hodler1.tv_title = (TextView) convertView.findViewById(R.id.zhuan_qu_tv_title);
				hodler1.tv_price = (TextView) convertView.findViewById(R.id.zhuan_qu_tv_price);
				hodler1.tv_del_price = (TextView) convertView.findViewById(R.id.zhuan_qu_tv_del_price);
				convertView.setTag(hodler1);

			} else {
				hodler1 = (ViewHodlerForLayout1) convertView.getTag();
			}

			ZhuanquGoodbean bean = listGoodbeans.get(position);

			// 图片
			if (bean.getImagePath() != null || !bean.getImagePath().equals("")) {
				ImageManager.Load(URLs.IMAGE_URL + bean.getImagePath(), hodler1.iv_img);
			} else {
				// Toast.makeText(ct, "图片路径有误！！！", 0).show();
				Log.i(TAG, "图片路径有误！！！");
			}
			// 介绍说明
			hodler1.tv_title.setText(bean.getTitle());
			// 价格
			hodler1.tv_price.setText(MathUtil.priceForAppWithSign(bean.getPrice()));
			// 参考价
			hodler1.tv_del_price.setText(MathUtil.priceForAppWithSign(bean.getReprice()));
			hodler1.tv_del_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
		}

		if (layout.equals("2")) {
			ViewhodlerForLayOut2 hodler2 = null;
			if (convertView == null) {
				hodler2 = new ViewhodlerForLayOut2();
				convertView = View.inflate(ct, R.layout.zhuanqu_list_item2, null);
				hodler2.iv_img = (ImageView) convertView.findViewById(R.id.zhuan_qu2_iv_img);
				hodler2.tv_title = (TextView) convertView.findViewById(R.id.zhuan_qu2_tv_title);
				hodler2.tv_price = (TextView) convertView.findViewById(R.id.zhuan_qu2_tv_price);
				hodler2.buy_num = (TextView) convertView.findViewById(R.id.zhuan_qu2_tv_buy_num);
				hodler2.tv_del_price = (TextView) convertView.findViewById(R.id.zhuan_qu2_tv_del_price);
				convertView.setTag(hodler2);

			} else {
				hodler2 = (ViewhodlerForLayOut2) convertView.getTag();
			}

			final ZhuanquGoodbean bean = listGoodbeans.get(position);

			if (bean.getImagePath() != null) {
				ImageManager.Load(URLs.IMAGE_URL + bean.getImagePath(), hodler2.iv_img);
			}

			hodler2.tv_title.setText(bean.getTitle());
			hodler2.tv_price.setText(MathUtil.priceForAppWithSign(bean.getPrice()));
			hodler2.buy_num.setText(bean.getSaleSum() + "人已经购买");

			hodler2.tv_del_price.setText(MathUtil.priceForAppWithSign(bean.getReprice()));
			hodler2.tv_del_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
			// hodler2.tv_del_price.setVisibility(View.INVISIBLE);
			hodler2.iv_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
					intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID, bean.getId() + "");
					ct.startActivity(intent);
				}
			});
		}
		if (layout.equals("3")) {
			// TODO
		}
		return convertView;
	}

	// 布局一
	class ViewHodlerForLayout1 {
		ImageView iv_img;
		TextView tv_title;
		TextView tv_price;
		TextView tv_del_price;
	}

	// 布局二
	class ViewhodlerForLayOut2 {
		ImageView iv_img;
		TextView tv_title;
		TextView tv_price;
		TextView tv_del_price;
		TextView buy_num;
	}

	// 布局三
	class ViewhodlerForLayOut3 {
		// TODO
	}

	/**
	 * 移除空格
	 * 
	 * @param str
	 * @return
	 */
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
