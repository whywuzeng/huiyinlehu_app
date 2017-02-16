package com.huiyin.ui.home.module;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.eims.widget.grid.drag.DragGridView;
import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.BaseBean;

public class ModeluPop {

    private ModeluPopCoallBack mCoallBack;
    private Context mContext;
    private PopupWindow popupWindow;
    private View mView;
    private ArrayList<Module> mModuleList;
    
    private ImageButton module_pop_complete_ibtn;
    private ImageButton module_pop_canl_ibtn;
    
    private DragGridView mDragGridView;
    private Drag2Adapter mDragAdapter;

    private GridView mGridView;
    private Drag2Adapter mAdapter;
    
    public ModeluPop(Context context, ArrayList<Module> list){
        mContext = context;
        mModuleList = new ArrayList<Module>();
        mModuleList.addAll(list);
        
        mView = LayoutInflater.from(context).inflate(R.layout.module_pop_view, null);
        initView();
        popupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //这个是为了点击“返回Back”也能使其消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setHeight(LayoutParams.MATCH_PARENT);  
        popupWindow.setWidth(LayoutParams.MATCH_PARENT);  
    }
    
    private void initView(){
        module_pop_complete_ibtn = (ImageButton) mView.findViewById(R.id.module_pop_complete_ibtn);
        module_pop_canl_ibtn = (ImageButton) mView.findViewById(R.id.module_pop_canl_ibtn);
        module_pop_complete_ibtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                if (mCoallBack != null) {
                    mCoallBack.onComplete(mDragAdapter.getAllItemData());
                }
                dismiss();
            }
        });
        module_pop_canl_ibtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        
        initDragGridView();
        initGridView();
    }
    
    private void initDragGridView(){
        mDragGridView = (DragGridView) mView.findViewById(R.id.dragGridView);
        mDragAdapter = new Drag2Adapter(mContext, ModeluPop.this, true);
        mDragGridView.setAdapter(mDragAdapter);
        mDragGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
        });
        mModuleList.remove(mModuleList.size()-1);
        mDragAdapter.addItems(mModuleList);
    }
    
    private void initGridView(){
        mGridView = (GridView) mView.findViewById(R.id.gridView);
        mAdapter = new Drag2Adapter(mContext, ModeluPop.this, false);
        mGridView.setAdapter(mAdapter);
    }
    
    private void addAllModelu(ArrayList<Fast> fastList){
        ArrayList<Module> modules = new ArrayList<Module>();
        Module module = null;
        for (Fast fast : fastList) {
            boolean isNot = true;
            for (Module item : mModuleList) {
                if (fast.ID == item.id) {
                    isNot = false;
                }
            } 
            if (isNot) {
                module = new Module();
                module.id = fast.ID;
                module.name = fast.FAST_NAME;
                module.type = fast.ID;
                module.imagePath = URLs.IMAGE_URL + fast.FAST_IMG;

                modules.add(module);
            }
        }
        mAdapter.addItems(modules);
    }
    
    public void addModelu(Module module){
        mDragAdapter.addItem(module);
        mAdapter.delItem(module);
    }
    
    public void delModelu(Module module){
        mAdapter.addItem(module);
        mDragAdapter.delItem(module);
    }
    
    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     * @param parent
     */
    public void showAsDropDown(View parent) {
        loadData();
//        popupWindow.showAtLocation(parent,Gravity.RIGHT,0,0);  area
        popupWindow.showAsDropDown(parent);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
    }
    
    /**
     * 隐藏菜单
     */
    public void dismiss() {
        popupWindow.dismiss();
    }
    
    private void loadData() {
        CustomResponseHandler handler = new CustomResponseHandler((Activity)mContext, true) {
            @Override
            public void onRefreshData(String content) {
                Gson gson = new Gson();
                AppAllFast dataBean = gson.fromJson(content, AppAllFast.class);
                if (dataBean != null) {
                    if (dataBean.type == 1) {
                        addAllModelu(dataBean.fastList);
                    } else {
                        Toast.makeText(mContext, "数据请求失败！", 0).show();
                        return;
                    }
                } else {
                    Toast.makeText(mContext, "数据请求失败！", 0).show();
                    return;
                }
            }
        };
        RequstClient.appAllFast(handler);
    }
    
    class AppAllFast extends BaseBean{
        public ArrayList<Fast> fastList;
    }
    
    public void setCoallBack(ModeluPopCoallBack coallBack){
        mCoallBack = coallBack;
    }
    
    public interface ModeluPopCoallBack{  
        public void onComplete(ArrayList<Module> arrayList);  
    } 
    
}
