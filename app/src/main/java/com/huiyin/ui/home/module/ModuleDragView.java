package com.huiyin.ui.home.module;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.eims.widget.grid.drag.DragGridView;
import com.huiyin.R;

public class ModuleDragView extends LinearLayout {
    
    private ModuleDragViewCoallBack mCoallBack;
    private Context mContext;
    private DragGridView mDragGridView;
    private DragAdapter mDragAdapter;
    
    public ModuleDragView(Context context) {
        super(context);
        mContext = context;
        init();
    }
    
    public ModuleDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

	private void init() {
	    LayoutInflater.from(mContext).inflate(R.layout.module_drag_view, this, true);
	    
	    mDragGridView = (DragGridView) findViewById(R.id.dragGridView);
	    mDragAdapter = new DragAdapter(mContext);
        mDragGridView.setAdapter(mDragAdapter);
        mDragGridView.setLockLastItem(true);
        mDragGridView.setDragResponseMS(2000);
        mDragGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Module module = (Module) mDragAdapter.getItem(arg2);
                if (mCoallBack != null) {
                    mCoallBack.onItemClick(module);
                }
            }
        });
	}
	
	public void refreshView() {
	    mDragAdapter.notifyDataSetChanged();
	}
	
	public void setScrollView(ScrollView v) {
	    mDragGridView.setScrollView(v);
	}
	
	public void setViewPager(ViewPager v) {
        mDragGridView.setViewPager(v);
    }
	
	public void setDate(ArrayList<Module> arr){
	    mDragAdapter.delAllItem();
	    
	    Module module = new Module();
        module.id = -1;
        module.name = "更多";
        module.imagePath = "drawable://" + R.drawable.icon_gengduo;
        arr.add(module);
	    
	    mDragAdapter.addItems(arr);
	}
	
	public String getModeluIds(){
	    ArrayList<Module> arrayList = getData();
	    StringBuffer bf = new StringBuffer();
	    for (Module module : arrayList) {
            bf.append(module.id);
            bf.append(",");
        }
	    return bf.length()==0?"":bf.substring(0, bf.length()-1);
	}
	
	public ArrayList<Module> getData(){
	    return mDragAdapter.getAllItemData();
	}
	
	public void cancelLongPressItem(){
	    mDragAdapter.cancelLongPressItem();
	}
	
	public void setCoallBack(ModuleDragViewCoallBack coallBack){
        mCoallBack = coallBack;
    }
    
    public interface ModuleDragViewCoallBack{  
        public void onItemClick(Module module);  
    } 
    
//	private ArrayList<Module> getData() {
//	    String[] names = {"今日头条", "乐虎彩票", "智慧管家", "积分CLUB", "预约服务", "物流查询", "秀场", "更多"};
//	    int[] icons = {R.drawable.icon_toutiao, 
//	            R.drawable.icon_caipiao, 
//	            R.drawable.icon_guanjia, 
//	            R.drawable.icon_jifen, 
//	            R.drawable.icon_yuyue, 
//	            R.drawable.icon_wuliu,
//	            R.drawable.icon_xiuchang,
//	            R.drawable.icon_gengduo};
//	    
//	    ArrayList<Module> modules = new ArrayList<Module>();
//	    Module module = null;
//	    for (int i = 0; i < names.length; i++) {
//	        module = new Module();
//            module.id = i;
//            module.name = names[i];
//            module.type = i;
//            module.imagePath = icons[i];
//            modules.add(module);
//        }
//	    return modules;
//	}

}
