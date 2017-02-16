package com.huiyin.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;

public class BindActivity extends BaseActivity {
	 
	TextView  left_ib,middle_title_tv,phone_bind_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bind_layout);
		 
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
		middle_title_tv.setText("手机绑定");
		
		phone_bind_tv = (TextView)findViewById(R.id.phone_bind_tv);
		String phoneStr = String.format(getString(R.string.phone_title_2), AppContext.getInstance().getUserInfo().phone);
		phone_bind_tv.setText(phoneStr);
		
	}
 
}





