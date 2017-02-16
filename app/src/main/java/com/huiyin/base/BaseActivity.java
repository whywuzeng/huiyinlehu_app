package com.huiyin.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.huiyin.UIHelper;
import com.huiyin.utils.AppManager;
import com.huiyin.utils.LogUtil;

public class BaseActivity extends Activity {
    private final static String TAG = "BaseActivity";
	public Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		mContext = this;
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		onFindViews();
        onSetListener();
        onLoadData();
	}
	
	/**
	 * 控件初始化 
	 */
	protected void onFindViews(){
	    LogUtil.i(TAG, "onFindViews()---->");
	}
	
	/**
	 * 设置控件事件
	 */
	protected void onSetListener(){
	    LogUtil.i(TAG, "onSetListener()---->");
	}
	
	protected void onLoadData(){
		LogUtil.i(TAG, "onLoadData()---->");
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        // 点击空白处，隐藏软键盘
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		
		 try{
			 UIHelper.cloesLoadDialog();
	        }catch (Exception e) {
	            System.out.println("myDialog取消，失败！");
	            
	        }
		super.onDestroy();
	}
	
	
	
}
