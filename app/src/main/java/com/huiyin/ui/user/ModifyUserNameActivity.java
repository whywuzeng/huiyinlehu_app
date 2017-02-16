package com.huiyin.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;

public class ModifyUserNameActivity extends BaseActivity {
	
	public final static String TAG = "ModifyUserNameActivity";
	
	TextView account_change_name,account_change_sure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_dialog_layout);
		
		account_change_name = (TextView)findViewById(R.id.account_change_name);
		account_change_name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(account_change_name.getText().toString().length()>20){
					Toast.makeText(mContext, "您输入的用户名已经超过最大的长度！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		account_change_sure = (TextView)findViewById(R.id.account_change_sure);
		
		account_change_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String change_name = account_change_name.getText().toString();
				if(change_name.equals("")){
					Toast.makeText(ModifyUserNameActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
					return;
					//^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{2,15}$
				}if(!change_name.matches("[-\\w\u4e00-\u9fa5]{1,20}")){
					Toast.makeText(mContext, "用户名不符合命名规则，请重新输入！",Toast.LENGTH_SHORT).show();
					account_change_name.setText("");
					return;
				}
				else{
					Intent i = new Intent(ModifyUserNameActivity.this,AccountManageActivity.class);
					i.putExtra("username", change_name);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}
	  
 
  
}




