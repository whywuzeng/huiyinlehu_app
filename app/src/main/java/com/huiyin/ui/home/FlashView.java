package com.huiyin.ui.home;

import com.huiyin.R;
import com.huiyin.bean.HomeFlashBean;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.flash.FlashActivity;
import com.huiyin.utils.ImageManager;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlashView extends LinearLayout {
	private Context mContext;
	private HomeFlashBean data;
	private OnClickListener mClickListener;
	// Content View Elements

	private TextView flashview_name;
	private TextView flashview_adv;
	private TextView flashview_more;
	private ImageView flashview_iv1;
	private ImageView flashview_iv2;
	private ImageView flashview_iv3;
	private ImageView flashview_iv4;

	// End Of Content View Elements

	public FlashView(Context context) {
		super(context);
		findView();
		SetListener();
		InitView();

	}

	public FlashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		findView();
		SetListener();
		InitView();

	}

	public FlashView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		findView();
		SetListener();
		InitView();
	}

	public FlashView(Context context, HomeFlashBean Data) {
		super(context);
		this.mContext = context;
		this.data = Data;
		findView();
		SetListener();
		InitView();

	}

	private void findView() {
		LayoutInflater.from(mContext).inflate(R.layout.flash_view, this, true);

		flashview_name = (TextView) findViewById(R.id.flashview_name);
		flashview_adv = (TextView) findViewById(R.id.flashview_adv);
		flashview_more = (TextView) findViewById(R.id.flashview_more);
		flashview_iv1 = (ImageView) findViewById(R.id.flashview_iv1);
		flashview_iv2 = (ImageView) findViewById(R.id.flashview_iv2);
		flashview_iv3 = (ImageView) findViewById(R.id.flashview_iv3);
		flashview_iv4 = (ImageView) findViewById(R.id.flashview_iv4);

	}

	private void SetListener() {
		flashview_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, FlashActivity.class);
				intent.putExtra("maintitle", data.getACTIVE_NAME());
				mContext.startActivity(intent);
			}
		});
		mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int ID = (Integer) v.getTag();
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
						String.valueOf(ID));
				mContext.startActivity(intent);
			}
		};
	}

	private void InitView() {
		flashview_name.setText(data.getACTIVE_NAME());
		flashview_adv.setText(data.getADVER());
		
		flashview_iv1.setTag(data.PID1);
		flashview_iv1.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG1, flashview_iv1);
		
		flashview_iv2.setTag(data.PID2);
		flashview_iv2.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG2, flashview_iv2);
		
		flashview_iv3.setTag(data.PID3);
		flashview_iv3.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG3, flashview_iv3);
		
		flashview_iv4.setTag(data.PID4);
		flashview_iv4.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG4, flashview_iv4);
	}
}
