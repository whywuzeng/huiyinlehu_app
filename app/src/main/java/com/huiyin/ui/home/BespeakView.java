package com.huiyin.ui.home;

import com.huiyin.R;
import com.huiyin.bean.ProductBespeakBean;
import com.huiyin.ui.bespeak.BespeakActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.utils.ImageManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 一个布局
 * @author admin_w
 */
public class BespeakView extends LinearLayout {
	private Context mContext;
	private ProductBespeakBean data;

	private TextView bespeakview_name;
	private TextView bespeakview_adv;
	private TextView bespeakview_more;
	private ImageView bespeakview_iv1;
	private ImageView bespeakview_iv2;
	private ImageView bespeakview_iv3;

	public BespeakView(Context context) {
		super(context);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, AttributeSet attrs) {
		super(context, attrs);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, ProductBespeakBean Data) {
		super(context);
		mContext = context;
		data = Data;
		findView();
		SetListener();
		InitView();
	}

	private void findView() {
		LayoutInflater.from(mContext)
				.inflate(R.layout.bespeak_view, this, true);

		bespeakview_name = (TextView) findViewById(R.id.bespeakview_name);
		bespeakview_adv = (TextView) findViewById(R.id.bespeakview_adv);
		bespeakview_more = (TextView) findViewById(R.id.bespeakview_more);
		bespeakview_iv1 = (ImageView) findViewById(R.id.bespeakview_iv1);
		bespeakview_iv2 = (ImageView) findViewById(R.id.bespeakview_iv2);
		bespeakview_iv3 = (ImageView) findViewById(R.id.bespeakview_iv3);

	}

	private void SetListener() {
		bespeakview_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BespeakActivity.class);
				mContext.startActivity(intent);
			}
		});
	}

	private void InitView() {
		bespeakview_name.setText(data.getANAME());
		bespeakview_adv.setText(data.getADVER());

		bespeakview_iv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						String.valueOf(data.CID1));
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_subscribe_ID,
						data.PID1);
				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG1, bespeakview_iv1);

		bespeakview_iv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						String.valueOf(data.CID2));
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_subscribe_ID,
						data.PID2);
				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG2, bespeakview_iv2);
		
		bespeakview_iv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						String.valueOf(data.CID3));
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_subscribe_ID,
						data.PID3);
				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG3, bespeakview_iv3);

	}
}
