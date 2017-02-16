package com.huiyin.wight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.StringUtils;


public class MyText extends TextView {

	PopupWindow popupWindow;
	private Activity context;
	private LayoutInflater  mLayoutInflater;
	
	private int[] images = {R.drawable.heart_normal,R.drawable.record_normal,R.drawable.paradise_normal,
			R.drawable.discover_normal,R.drawable.setting_normal};
	private String[] names = {"首页", "分类", "购物车", "智慧管家", "我的乐虎"};
	
	public MyText(Context context) {
		super(context);
		this.context = (Activity)context;
		initData();
	}

	public MyText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = (Activity)context;
		initData();
	}

	public MyText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity)context;
		initData();
	}
	 
	
	private void initData(){
		
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = mLayoutInflater.inflate(R.layout.popwindow, null);
	    ListView listView = (ListView) contentView.findViewById(R.id.top_listview);
	    listView.setAdapter(getAdapter());
	    listView.setOnItemClickListener(new ItemClickListener());
        
    	popupWindow = new PopupWindow(contentView, StringUtils.DpToPx(context, 160),ViewGroup.LayoutParams.WRAP_CONTENT);
    	popupWindow.setFocusable(true);//取得焦点
    	popupWindow.setBackgroundDrawable(new BitmapDrawable());
//    	popupWindow.setAnimationStyle(R.style.popwin_anim_style);
    	
    	this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(popupWindow.isShowing()) {
					popupWindow.dismiss();
				}else{
					popupWindow.showAsDropDown(MyText.this,StringUtils.DpToPx(context,-50),StringUtils.DpToPx(context,14)); 
				}
			}
		});
    	
	}
	
	   private final class ItemClickListener implements OnItemClickListener{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if(popupWindow.isShowing()) popupWindow.dismiss(); 
				if(position==0){
					AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
				}else if(position==1){
					AppContext.MAIN_TASK = AppContext.CLASSIC;
				}else if(position==2){
					AppContext.MAIN_TASK = AppContext.SHOPCAR;
				}else if(position==3){
					AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
				}else if(position==4){
					AppContext.MAIN_TASK = AppContext.LEHU;
				}
				Intent i = new Intent();
				i.setClass(context, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				context.finish();
			}    	
	    }
	   
	private ListAdapter getAdapter() {
		
    	List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
    	for(int i = 0 ; i < images.length ; i++ ){
    		HashMap<String, Object> item = new HashMap<String, Object>();
    		item.put("image", images[i]);
    		item.put("name", names[i]);
    		data.add(item);
    	}
    	
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, R.layout.list_item,
				new String[]{"image", "name"}, new int[]{R.id.imageView, R.id.textView});
		
		return simpleAdapter;
	}
	
}






