package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.MessageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class MyMessageActivity extends BaseActivity {
	
	public final static String TAG = "MyMessageActivity";
	TextView left_ib,middle_title_tv ;
	
	private XListView mListView;
	private MessageAdapter messageAdapter;
	
	private ArrayList<SysMessage> messages = new ArrayList<SysMessage>();
	private int page = 1;  
	private Boolean initType = true;
	
	String pushFlag;
	String letterId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymessage_layout);
		initView();
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		getCollectList();
	}
	
	private void initView(){
		middle_title_tv = (TextView)findViewById(R.id.ab_title);
		middle_title_tv.setText("系统消息");
		
		left_ib = (TextView)findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(pushFlag.equals("1")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
				}else{
					finish();
				}
			}
		});
		
		mListView = (XListView)findViewById(R.id.xlistview);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener(){
			@Override
			public void onRefresh() {
				initType = true;
				page = 1;
				getCollectList();
			}
			@Override
			public void onLoadMore() {
				if(initType){
					page++;
					getCollectList();
				}
			}
		});
		
	}
	/***
	 * 获取系统消息
	 * @param initB
	 */
	private void getCollectList(){
		if (AppContext.getInstance().getUserId()==null) {
			return;
		}
		RequstClient.getSystemMsg(AppContext.getInstance().getUserId(), page +"", new CustomResponseHandler(this) {
			@Override
			public void onFinish() {
				super.onFinish();
				mListView.stopLoadMore();
				mListView.stopRefresh();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_SHORT).show();
						return;
					}
					
					messages = new Gson().fromJson(
							obj.getString("letterList"),new TypeToken<List<SysMessage>>(){}.getType());
					if(messages != null && messages.size() > 0){
						if(page == 1){
							if(messageAdapter == null){
								messageAdapter = new MessageAdapter(MyMessageActivity.this);
								mListView.setAdapter(messageAdapter);
							}
							messageAdapter.refresh(messages);
						}else{
							messageAdapter.add(messages);
						}
						if(messages.size() < 10){
							mListView.hideFooter();
						}else{
							mListView.showFooter();
						}
					}else{
						initType = false;
						mListView.hideFooter();
						Toast.makeText(MyMessageActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
//	private void appLetterReader(String letterId,final int position){
//		if (AppContext.userId==null) {
//			return;
//		}
//		RequstClient.appLetterReader(letterId, new CustomResponseHandler(this,false) {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					String content) {
//				super.onSuccess(statusCode, headers, content);
//				try {
//					JSONObject obj = new JSONObject(content);
//					if (!obj.getString("type").equals("1")) {
//						String errorMsg = obj.getString("msg");
//						Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_SHORT).show();
//						return;
//					}
//					messageAdapter.returnDatas().get(position).READ_STATUS = "1";
//					ArrayList<SysMessage> arrayList = messageAdapter.returnDatas();
//					arrayList.get(position).READ_STATUS = "1";
//					messageAdapter.refresh(arrayList);
//					messageAdapter.notifyDataSetChanged();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(pushFlag.equals("1")){
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public class SysMessage{
		public String ID;  //id
		public String SEND_TIME;    //发送时间
		public String SEND_TITLE;   //标题
		public String SEND_CONTENT;  //内容
		public String READ_STATUS;   //阅读状态
		public String VIEW_TYPE;   //类型
		public String JUMP_DATA;   //类型
		public int CID;
		public int URL_TYPE;
		public int URL_ID;
		
		
		@Override
		public String toString() {
			return "SysMessage [ID=" + ID + ", SEND_TIME=" + SEND_TIME
					+ ", SEND_TITLE=" + SEND_TITLE + ", SEND_CONTENT="
					+ SEND_CONTENT + ", READ_STATUS=" + READ_STATUS
					+ ", VIEW_TYPE=" + VIEW_TYPE + "]";
		}
	}
}
