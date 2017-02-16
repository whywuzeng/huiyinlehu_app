package com.huiyin.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.SalesPromotion;
import com.huiyin.utils.ImageManager;

public class CuXiaoHuodongView extends LinearLayout {

	private CuXiaoHuodongViewCoallBack mCoallBack;
	private Context mContext;

	private SalesPromotion mPrommotion;

	private View mView;

	public CuXiaoHuodongView(Context context) {
		super(context);
		mContext = context;
		init();
		setData(mPrommotion);
	}

	public CuXiaoHuodongView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LinearLayout.inflate(mContext, R.layout.cuxiaohuodong_view, this);
	}

	public void setData(SalesPromotion prommotion) {
		mPrommotion = prommotion;
		upDataView();
	}

	public void upDataView() {
		int layoutid = mPrommotion.getLayoutType();
		if (layoutid != 4) {
			if (layoutid == 1) {
				mView = LinearLayout.inflate(mContext, R.layout.cuxiaohuodong1, this);
			} else if (layoutid == 2) {
				mView = LayoutInflater.from(mContext).inflate(R.layout.cuxiaohuodong2, this, true);
			} else if (layoutid == 3) {
				mView = LayoutInflater.from(mContext).inflate(R.layout.cuxiaohuodong3, this, true);
			} else if (layoutid == 5) {
				mView = LayoutInflater.from(mContext).inflate(R.layout.cuxiaohuodong5, this, true);
			}

			CuxiaoViewHolder cuxiaoViewHolder1 = new CuxiaoViewHolder();
			cuxiaoViewHolder1.cuxiaohuodong_iv1 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv1);
			cuxiaoViewHolder1.cuxiaohuodong_iv2 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv2);

			if (layoutid != 2) {
				cuxiaoViewHolder1.cuxiaohuodong_iv3 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv3);
				cuxiaoViewHolder1.cuxiaohuodong_iv3.setOnClickListener(new CuxiaoListener(3));
				ImageManager.LoadWithServer(mPrommotion.getImageUrl3(), cuxiaoViewHolder1.cuxiaohuodong_iv3);
			}
			if (layoutid == 5) {
				cuxiaoViewHolder1.cuxiaohuodong_iv4 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv4);
				cuxiaoViewHolder1.cuxiaohuodong_iv4.setOnClickListener(new CuxiaoListener(4));
				ImageManager.LoadWithServer(mPrommotion.getImageUrl4(), cuxiaoViewHolder1.cuxiaohuodong_iv4);
			}

			cuxiaoViewHolder1.cuxiaohuodong_iv1.setOnClickListener(new CuxiaoListener(1));
			cuxiaoViewHolder1.cuxiaohuodong_iv2.setOnClickListener(new CuxiaoListener(2));

			ImageManager.LoadWithServer(mPrommotion.getImageUrl1(), cuxiaoViewHolder1.cuxiaohuodong_iv1);
			ImageManager.LoadWithServer(mPrommotion.getImageUrl2(), cuxiaoViewHolder1.cuxiaohuodong_iv2);

		} else if (layoutid == 4) {

			mView = LayoutInflater.from(mContext).inflate(R.layout.cuxiaohuodong4, this, true);

			CuxiaoViewHolder4 viewHolder = new CuxiaoViewHolder4();
			viewHolder.cuxiaohuodong_iv1 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv1);
			viewHolder.cuxiaohuodong_iv2 = (ImageView) mView.findViewById(R.id.cuxiaohuodong_iv2);

			viewHolder.cuxiaohodong_keyword1_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword1_tv);
			viewHolder.cuxiaohodong_keyword2_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword2_tv);
			viewHolder.cuxiaohodong_keyword3_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword3_tv);
			viewHolder.cuxiaohodong_keyword4_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword4_tv);
			viewHolder.cuxiaohodong_keyword5_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword5_tv);
			viewHolder.cuxiaohodong_keyword6_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword6_tv);
			viewHolder.cuxiaohodong_keyword7_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword7_tv);
			viewHolder.cuxiaohodong_keyword8_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword8_tv);
			viewHolder.cuxiaohodong_keyword9_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword9_tv);
			viewHolder.cuxiaohodong_keyword10_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword10_tv);
			viewHolder.cuxiaohodong_keyword11_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword11_tv);
			viewHolder.cuxiaohodong_keyword12_tv = (TextView) mView.findViewById(R.id.cuxiaohodong_keyword12_tv);

			viewHolder.cuxiaohuodong_iv1.setOnClickListener(new CuxiaoListener(1));
			viewHolder.cuxiaohuodong_iv2.setOnClickListener(new CuxiaoListener(2));

			ImageManager.LoadWithServer(mPrommotion.getImageUrl1(), viewHolder.cuxiaohuodong_iv1);
			ImageManager.LoadWithServer(mPrommotion.getImageUrl2(), viewHolder.cuxiaohuodong_iv2);

			viewHolder.cuxiaohodong_keyword1_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY1(), mPrommotion.getKEY1URL(), mPrommotion.getDETAIL1ID()));
			viewHolder.cuxiaohodong_keyword2_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY2(), mPrommotion.getKEY2URL(), mPrommotion.getDETAIL2ID()));
			viewHolder.cuxiaohodong_keyword3_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY3(), mPrommotion.getKEY3URL(), mPrommotion.getDETAIL3ID()));
			viewHolder.cuxiaohodong_keyword4_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY4(), mPrommotion.getKEY4URL(), mPrommotion.getDETAIL4ID()));
			viewHolder.cuxiaohodong_keyword5_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY5(), mPrommotion.getKEY5URL(), mPrommotion.getDETAIL5ID()));
			viewHolder.cuxiaohodong_keyword6_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY6(), mPrommotion.getKEY6URL(), mPrommotion.getDETAIL6ID()));
			viewHolder.cuxiaohodong_keyword7_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY7(), mPrommotion.getKEY7URL(), mPrommotion.getDETAIL7ID()));
			viewHolder.cuxiaohodong_keyword8_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY8(), mPrommotion.getKEY8URL(), mPrommotion.getDETAIL8ID()));
			viewHolder.cuxiaohodong_keyword9_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY9(), mPrommotion.getKEY9URL(), mPrommotion.getDETAIL9ID()));
			viewHolder.cuxiaohodong_keyword10_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY10(), mPrommotion.getKEY10URL(), mPrommotion.getDETAIL10ID()));
			viewHolder.cuxiaohodong_keyword11_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY11(), mPrommotion.getKEY11URL(), mPrommotion.getDETAIL11ID()));
			viewHolder.cuxiaohodong_keyword12_tv.setOnClickListener(new KeywordListener(mPrommotion.getKEY12(), mPrommotion.getKEY12URL(), mPrommotion.getDETAIL12ID()));

			viewHolder.cuxiaohodong_keyword1_tv.setText(mPrommotion.getKEY1());
			viewHolder.cuxiaohodong_keyword2_tv.setText(mPrommotion.getKEY2());
			viewHolder.cuxiaohodong_keyword3_tv.setText(mPrommotion.getKEY3());
			viewHolder.cuxiaohodong_keyword4_tv.setText(mPrommotion.getKEY4());
			viewHolder.cuxiaohodong_keyword5_tv.setText(mPrommotion.getKEY5());
			viewHolder.cuxiaohodong_keyword6_tv.setText(mPrommotion.getKEY6());
			viewHolder.cuxiaohodong_keyword7_tv.setText(mPrommotion.getKEY7());
			viewHolder.cuxiaohodong_keyword8_tv.setText(mPrommotion.getKEY8());
			viewHolder.cuxiaohodong_keyword9_tv.setText(mPrommotion.getKEY9());
			viewHolder.cuxiaohodong_keyword10_tv.setText(mPrommotion.getKEY10());
			viewHolder.cuxiaohodong_keyword11_tv.setText(mPrommotion.getKEY11());
			viewHolder.cuxiaohodong_keyword12_tv.setText(mPrommotion.getKEY12());

		}
		try {
			addView(mView);
		} catch (Exception e) {
		}

	}

	public void setCoallBack(CuXiaoHuodongViewCoallBack coallBack) {
		mCoallBack = coallBack;
	}

	public interface CuXiaoHuodongViewCoallBack {

		/** 点击促销图片事件回调 */
		public void onCuxiaoClick(int id);

		/** 点击促销关键字的事件回调 */
		public void onkeywordClick(String keyword, String keywordUrl, String id);
	}

	/**
	 * 促销图片事件监听
	 */
	class CuxiaoListener implements OnClickListener {
		private int type;

		public CuxiaoListener(int type) {
			this.type = type;
		}

		@Override
		public void onClick(View arg0) {
			if (mCoallBack != null) {
				mCoallBack.onCuxiaoClick(type);
			}
		}
	}

	/**
	 * 促销关键字事件监听
	 */
	class KeywordListener implements OnClickListener {
		private String keyword;
		private String keywordUrl;
		private String detailId;

		public KeywordListener(String keyword, String keywordUrl, String detailId) {
			this.keyword = keyword;
			this.keywordUrl = keywordUrl;
			this.detailId = detailId;
		}

		@Override
		public void onClick(View arg0) {
			if (mCoallBack != null) {
				mCoallBack.onkeywordClick(keyword, keywordUrl, detailId);
			}
		}
	}

	class CuxiaoViewHolder {
		ImageView cuxiaohuodong_iv1;
		ImageView cuxiaohuodong_iv2;
		ImageView cuxiaohuodong_iv3;
		ImageView cuxiaohuodong_iv4;

		LinearLayout cuxiaohoudong_dv_linLayout;
	}

	class CuxiaoViewHolder4 {
		ImageView cuxiaohuodong_iv1;
		ImageView cuxiaohuodong_iv2;

		TextView cuxiaohodong_keyword1_tv;
		TextView cuxiaohodong_keyword2_tv;
		TextView cuxiaohodong_keyword3_tv;
		TextView cuxiaohodong_keyword4_tv;
		TextView cuxiaohodong_keyword5_tv;
		TextView cuxiaohodong_keyword6_tv;
		TextView cuxiaohodong_keyword7_tv;
		TextView cuxiaohodong_keyword8_tv;
		TextView cuxiaohodong_keyword9_tv;
		TextView cuxiaohodong_keyword10_tv;
		TextView cuxiaohodong_keyword11_tv;
		TextView cuxiaohodong_keyword12_tv;

		LinearLayout cuxiaohoudong_dv_linLayout;
	}
}
