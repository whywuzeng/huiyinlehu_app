package com.huiyin.ui.nearshop;

import com.huiyin.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

 
public class NearShopDetails extends Activity{
	
	
	 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	 
    	 setContentView(R.layout.near_shop_deatils);
    	 RelativeLayout rl = (RelativeLayout)findViewById(R.id.theshow_like_title);
         TextView tv_title  = (TextView)rl.findViewById(R.id.middle_title_tv); 
         tv_title.setText("门店详情");
         TextView tv_title_right  = (TextView)rl.findViewById(R.id.right_ib);
         tv_title_right.setBackgroundResource(R.drawable.icon_ditu);
      
    }
  
}
