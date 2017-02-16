package com.huiyin.ui.shoppingcar;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.ChooseLehjuanAdapter;
import com.huiyin.adapter.ChooseLehjuanAdapter.LehuCheckChange;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.Lehujuan;

public class HuJuanDeductionActivity extends BaseActivity {

	public final static String INTENT_KEY_LIST = "list_data";

	private final static String TAG = "HuJuanDeductionActivity";

	private TextView ab_title, ab_back;

	private ListView mListView;

	private List<Lehujuan> listDatas;

	private ChooseLehjuanAdapter adapter;

	private TextView hj_total_num;

	private TextView ab_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hujuan_deduction);

		initData();

		initView();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		listDatas = (List<Lehujuan>) getIntent().getSerializableExtra(INTENT_KEY_LIST);

		if (listDatas.size() > 0) {
			listDatas.get(0).setChecked(true);
		}

	}

	private void initView() {

		hj_total_num = (TextView) findViewById(R.id.hj_total_num);
		hj_total_num.setText("共" + listDatas.size() + "张乐虎劵可用");
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("乐虎劵抵扣");
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				back();
			}
		});

		ab_right = (TextView) findViewById(R.id.ab_right);
		ab_right.setText("确定");
		ab_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				back();
			}
		});

		adapter = new ChooseLehjuanAdapter(getApplicationContext(), listDatas);
		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setAdapter(adapter);

		adapter.setOnLehuCheckChange(new LehuCheckChange() {
			@Override
			public void onCheckChage(boolean check, int position) {
				if (check) {
					listDatas.get(position).setChecked(true);
					positionChoose = position;
					for (int i = 0; i < listDatas.size(); i++) {
						if (i != position) {
							listDatas.get(i).setChecked(false);
						}
					}
					adapter.notifyDataSetChanged();
				} else {
					listDatas.get(position).setChecked(false);
					if (positionChoose == position) {
						positionChoose = -1;
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private int positionChoose = 0;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		back();
	}

	private void back() {
		Intent i = new Intent();
		i.setClass(HuJuanDeductionActivity.this, WriteOrderActivity.class);
		Lehujuan obj = new Lehujuan();
		for (Lehujuan bean : listDatas) {
			if (bean.isChecked()) {
				bean = obj;
			}
		}

		if (positionChoose == -1) {
			i.putExtra("lhj_value_id", obj.getId() + "");
			i.putExtra("lhj_value_price", obj.getAmount() + "");
		} else {
			i.putExtra("lhj_value_id", listDatas.get(positionChoose).getId() + "");
			i.putExtra("lhj_value_price", listDatas.get(positionChoose).getAmount() + "");
		}

		Log.i("", positionChoose + ">>>>>>>>>>>" + obj);

		setResult(RESULT_OK, i);
		finish();

	}
}
