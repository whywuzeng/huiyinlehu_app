package com.huiyin.ui.housekeeper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.WebViewUtil;
import com.huiyin.wight.rongcloud.RongCloud;

public class HkDetailActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "HkDetailActivity";

	private static final int REQUEST_CODE = 100;

	private TextView tv_title, left_ib;
	private ImageView right_ib1;
	private HKDeatilBean mHK;
	private String id,idd;
	private String content, housekeeperId, housekeeperdeatilsTitle,
			introduction,ABSTRAC11,ABSTRAC22;
	private Button first_button_yuyue;
	WebView webView;
    private RelativeLayout button_layout;
 
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hk_detail_introduce);
		 
		initView();
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		webView = (WebView) findViewById(R.id.wb_view);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webView.setBackgroundResource(android.R.color.transparent); // 设置背景色
		webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.deatils_tt_layut);
		first_button_yuyue = (Button) findViewById(R.id.button);
		first_button_yuyue.setOnClickListener(this);
//		button_layout =  (RelativeLayout) findViewById(R.id.button_layout);
//		button_layout.setOnClickListener(this);
		tv_title = (TextView) rl.findViewById(R.id.middle_title_tv);
		left_ib = (TextView) rl.findViewById(R.id.left_ib);
		left_ib.setOnClickListener(this);
		right_ib1 = (ImageView)rl.findViewById(R.id.right_ib1);
		TextView right_ib = (TextView) rl.findViewById(R.id.right_ib);
		right_ib.setText("咨询");
		// right_ib.setTextSize(28);
		right_ib.setBackgroundResource(R.color.index_red);
		// left_ib.setVisibility(View.GONE);
		right_ib.setOnClickListener(this);
		right_ib1.setOnClickListener(this);
		//first_button_yuyue.setOnClickListener(this);
		mHK = new HKDeatilBean();
		Intent i = getIntent();
		id = i.getStringExtra("id");
		deatils();
		String title = i.getStringExtra("title"); 
		tv_title.setText(title);
	}

	public void loadWebData(String dataHtml) {
		webView.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtml), "text/html", "utf-8", null);
	}

	public void deatils() {
		// 传入智慧管家数据ID= 2
		Intent i = getIntent();
		id = i.getStringExtra("id");
		RequstClient.houseKeeperDeatils( id, 
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "HkDetailActivity:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(HkDetailActivity.this, errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							mHK = new Gson().fromJson(content,
									HKDeatilBean.class);

							loadData();
                            
							ABSTRAC22 = mHK.wisdom.INTRODUCTION;
							idd =   mHK.wisdom.ID;
							 
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
 
				 
 
	}

	private void loadData() {
		loadWebData(mHK.wisdom.CONTENT);
         
	}

	public class HKDeatilBean {

		public HDKItem wisdom = new HDKItem();

		public class HDKItem {
			public String INTRODUCTION;// 智慧 管家简介
			public String ID;// 智慧 管家id
			public String CONTENT;
			public String TITLE;// 智慧 管家标题
			public String ABSTRACTING;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_ib:
			finish();
			break;
		case R.id.button:// 点击马上预约，跳到预约界面

			if(AppContext.getInstance().getUserId() ==null){
				Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
				
				 
				Intent intent=new Intent(this,LoginActivity.class);  
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivityForResult(intent, 20);  
				          
  
				 
				return;
			}else{ 
			Intent i = new Intent();
			i.setClass(this, HouseKeeperOrderActivity.class);
			i.putExtra("title", tv_title.getText().toString().trim());
			//i.putExtra("ABSTRACTING", ABSTRAC11);
			i.putExtra("id", idd);
			i.putExtra("ABSTRACTING", ABSTRAC22);
			startActivity(i);
	      }
			break;

		case R.id.right_ib:
              
			String telPhone = PreferenceUtil.getInstance(mContext).getHotLine();
			String regEx = "[^0-9]"; 
			Pattern p = Pattern.compile(regEx); 
			Matcher m = p.matcher(telPhone); 
			telPhone = m.replaceAll("").trim(); 
			Log.i("解析结果", "====" + telPhone); 

			if(StringUtils.isBlank(telPhone)) 
			return; 
			Intent d_intent = new Intent(Intent.ACTION_DIAL, 
			Uri.parse("tel:"+telPhone)); 
			d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(d_intent);
			
			
			
//			
//			Intent d_intent = new Intent(Intent.ACTION_DIAL,
//					Uri.parse("tel:400-188-2108"));
//			d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(d_intent);

			break;
			
			case R.id.right_ib1:
				RongCloud.getInstance(HkDetailActivity.this).startCustomerServiceChat();
				break;

		default:
			break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
     {  
	              
			   if(requestCode==20 && resultCode==Activity.RESULT_OK) 
			   {
				  
				   Intent in1  = new Intent(); 
			       in1.setClass(HkDetailActivity.this, HouseKeeperOrderActivity.class);
			       in1.putExtra("title", tv_title.getText().toString().trim());
			       //in1.putExtra("ABSTRACTING", ABSTRAC11);
			       in1.putExtra("id", idd);
			       in1.putExtra("ABSTRACTING", ABSTRAC22);
				   startActivity(in1);
                                          
		                          
		                   
	        super.onActivityResult(requestCode, resultCode, data);  
	    }  
     }
}
