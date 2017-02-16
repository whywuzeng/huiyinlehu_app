package com.huiyin.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;

public class BackgoodsDealStatuActivity extends BaseActivity {
	 
	TextView left_ib,middle_title_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backgoods_layout2);
		 
		initView();
		
	}
	
	private void initView(){
		
		left_ib = (TextView)findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		middle_title_tv = (TextView)findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("退款详情");
	}
 
}





