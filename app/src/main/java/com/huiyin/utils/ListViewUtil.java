package com.huiyin.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * 项目名称：Skyworth_allhere    
 * 类名称：ListViewUtil    
 * 类描述：ListView 工具类    
 * 创建人：zengweijie
 * 创建时间：2014年6月23日 下午7:52:15    
 * 修改人： 
 * 修改时间：2014年6月23日 下午7:52:15    
 * 修改备注：    
 * @version     
 *
 */
public class ListViewUtil {
    
    /**
     * 根据计算所有ListView item的高度设置ListView的高度
     * @param listView  
     */
    public static void setListViewHight(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }  
    
    public static void setListViewHight(ListView listView,View footer) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
        totalHeight+=footer.getMeasuredHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }  
    
}
