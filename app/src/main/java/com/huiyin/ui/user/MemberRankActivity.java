package com.huiyin.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AppUserRankBean;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;

public class MemberRankActivity extends BaseActivity {
	private Context mContext;
	private TextView left_ib, middle_title_tv;
	private TextView member_rank_info;
	private TextView the_rank_textview;
	private LinearLayout division_addto, scaleplate_addto;
	private TableLayout menber_rank_introduce;

	private AppUserRankBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_rank_layout);
		mContext = this;
		findView();
		setListener();
		InitData();
	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.ab_back);
		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("会员等级");
		member_rank_info = (TextView) findViewById(R.id.member_rank_info);
		the_rank_textview = (TextView) findViewById(R.id.the_rank_textview);
		division_addto = (LinearLayout) findViewById(R.id.division_addto);
		scaleplate_addto = (LinearLayout) findViewById(R.id.scaleplate_addto);
		menber_rank_introduce = (TableLayout) findViewById(R.id.menber_rank_introduce);
	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void InitData() {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				bean = AppUserRankBean.explainJson(content, mContext);
				if (bean.type > 0) {
					reBulidView();
				}
			}

		};
		if (!StringUtils.isBlank(AppContext.getInstance().getUserId()))
			RequstClient.appUserRank(handler, AppContext.getInstance().getUserId());
	}

	public void reBulidView() {
		String info = "我的当前等级：" + "普通会员" + "<br/>当前金额：<font color=\"#dd434d\">" + MathUtil.priceForAppWithSign(bean.getUserIntegral())
				+ "</font><br/>再累积<font color=\"#dd434d\">" + MathUtil.priceForAppWithSign(bean.getApartPrice()) + "</font>即可升级到<font color=\"#dd434d\">"
				+ bean.getNextLevelName() + "</font>";
		member_rank_info.setText(Html.fromHtml(info));

		float max = 1.0f;
//		int a = 0;
		if (bean.getRankInfo() != null && bean.getRankInfo().size() > 0) {
			for (int i = 0; i < bean.getRankInfo().size(); i++) {
				View v1 = new View(mContext);
				v1.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
				division_addto.addView(v1);
				View v2 = new View(mContext);
				v2.setBackgroundColor(0xFFC3C3C3);
				v2.setLayoutParams(new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT));
				division_addto.addView(v2);

				TextView textView = new TextView(mContext);
				textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
				textView.setTextSize(12);
				textView.setTextColor(0xffdd434d);
				textView.setText(String.valueOf(bean.getRankInfo().get(i).getLEVELINTEGRAL()));
				scaleplate_addto.addView(textView);
				max = Math.max(max, bean.getRankInfo().get(i).getLEVELINTEGRAL());

				View v = new View(mContext);
				v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 1)));
				v.setBackgroundColor(0xffC3C3C3);
				menber_rank_introduce.addView(v);
				
				TableRow row = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.member_rank_introduce_item, null, true);

				TextView rank_name = (TextView) row.findViewById(R.id.rank_name);
				TextView rank_info = (TextView) row.findViewById(R.id.rank_info);
				rank_name.setText(bean.getRankInfo().get(i).getLEVELNAME());
				rank_info.setText(bean.getRankInfo().get(i).getDISCOUNT() + "折");

				menber_rank_introduce.addView(row);
				
//				if (bean.getMemberRank() == bean.getRankInfo().get(i).getID()) {
//					a = i;
//				}
			}
			TheAnimation(the_rank_textview, MathUtil.stringToFloat(bean.getUserIntegral()) / max);

//			TableRow row = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.member_rank_introduce_item, null, true);
//
//			TextView rank_name = (TextView) row.findViewById(R.id.rank_name);
//			TextView rank_info = (TextView) row.findViewById(R.id.rank_info);
//			rank_name.setText(bean.getRankInfo().get(a).getLEVELNAME());
//			rank_info.setText(bean.getRankInfo().get(a).getDISCOUNT() + "折");
//
//			menber_rank_introduce.addView(row);
		}
	}

	public void TheAnimation(TextView mTextView, float toX) {
		ScaleAnimation animation = new ScaleAnimation(1.0f, toX, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(2000);
		animation.setFillAfter(true);
		mTextView.startAnimation(animation);
	}
}
