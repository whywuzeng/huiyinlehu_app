package com.huiyin.ui.shoppingcar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.WriteGoodsListAdapter;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.OrderBean;

/**
 * 填写订单 - 商品清单页面
 * 
 * @author lixiaobin
 **/
public class GoodsListActivity extends BaseActivity {

	private final static String TAG = "GoodListActivity";

	public final static String INTENT_KEY_GOODS_LIST = "goods_list";

	private TextView left_rb, middle_title_tv;
	private ListView list_view;
	private OrderBean mOrderBean;

	private WriteGoodsListAdapter adatper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_goods_list);

		initData();

		initView();
	}

	private void initData() {
		if (getIntent().hasExtra(INTENT_KEY_GOODS_LIST)) {
			mOrderBean = (OrderBean) getIntent().getSerializableExtra(
					INTENT_KEY_GOODS_LIST);
		}

		if (mOrderBean == null) {
			mOrderBean = new OrderBean();
		}
	}

	private void initView() {

		left_rb = (TextView) findViewById(R.id.ab_back);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("商品清单");

		list_view = (ListView) findViewById(R.id.mListView);

		if (mOrderBean != null) {
			adatper = new WriteGoodsListAdapter(getApplicationContext(),
					mOrderBean);
			list_view.setAdapter(adatper);
		}
	}

}
