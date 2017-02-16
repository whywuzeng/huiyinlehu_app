package com.huiyin.ui.club;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.ClubGridViewAdapter;
import com.huiyin.utils.AppManager;
import com.huiyin.wight.VerticalScrollTextView;

public class ClubAct extends Activity implements OnClickListener {
	public Context mContext;
	public ImageButton lottery_button;
	public ImageView lottery_point;
	
	public float todegree=0.0f;
	public float startdegree=0.0f;
	
	public ClubGridViewAdapter gridadapter;
	private GridView gridView;
	
	public String s="12602";
	public String text="";
	
	//当前积分
	public TextView current_integral;
	//扣除积分
	public TextView deduct_integral;
	//抽奖次数
	public TextView lottery_count;
	//查看活动说明
	public TextView lottery_detail;
	
	public LinearLayout normal_show;
	public LinearLayout deduct_show;
	
	VerticalScrollTextView mSampleView;
	public List<String> lst=new ArrayList<String>();
	public List<String> list=new ArrayList<String>();
	private StringBuilder builder=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		mContext = this;
		setContentView(R.layout.club_main);
		initView();
//		initData();
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		lottery_point=(ImageView) findViewById(R.id.iv_lottery_point);
		
		lottery_button=(ImageButton) findViewById(R.id.ib_lottery);
		
		//扣除布局
		deduct_show=(LinearLayout) findViewById(R.id.ll_deduct_show);
		//正常布局
		normal_show=(LinearLayout) findViewById(R.id.ll_normal_show);
		
		//设置布局隐藏显示
		/*deduct_show.setVisibility(View.VISIBLE);
		normal_show.setVisibility(View.GONE);*/
		
		current_integral=(TextView) findViewById(R.id.tv_lottery_current_integral);
		deduct_integral=(TextView) findViewById(R.id.tv_lottery_deduct_integral);
		lottery_count=(TextView) findViewById(R.id.tv_lottery_count);
		
		lottery_detail=(TextView) findViewById(R.id.tv_lottery_detail);
		
		current_integral.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		deduct_integral.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		lottery_count.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		//转盘按钮
		lottery_button.setOnClickListener(this);
		//查看活动说明
		lottery_detail.setOnClickListener(this);
		
		gridView = (GridView) findViewById(R.id.gv_lottery_num);
		mSampleView = (VerticalScrollTextView) findViewById(R.id.tv_my_scroll_textview);
		
		//参与人数
		if ((s == null) || (s.length() == 0)) {  
            return;  
        } else {  
            LayoutParams params = new LayoutParams(s.length() * (24 + 6), LayoutParams.WRAP_CONTENT);  
            gridView.setLayoutParams(params);  
            gridView.setColumnWidth(26);
            gridView.setHorizontalSpacing(4); 
            gridView.setStretchMode(GridView.NO_STRETCH);  
            gridView.setNumColumns(s.length());  
            gridadapter = new ClubGridViewAdapter(this, s);  
            gridView.setAdapter(gridadapter);
        }
		
		lst.add("hehehhe");
		//模拟数据
		for(int i=0;i<30;i++){
			if(i%2==0){
				lst.add(i, i+"金球奖三甲揭晓 C罗梅西哈维入围甲揭晓 ...换魔兽换魔兽兽换魔兽换魔？？？？=====%%%换魔兽换魔兽？？？？=====### ");
			}else{
				lst.add(i, i+"公牛欲用三大主力换魔兽换魔兽？？？？=====");
			}
		}
		
		
		//底部消息字符串过长分段截取
		for(int k=0;k<lst.size();k++){
			text=lst.get(k).trim();
			if(text.length()<26){
				list.add(text);
			}else{
				
				for(int j=0;j<text.length();j++){  
					if(j%33==0){  
						builder = new StringBuilder();   
					}  
		            if(j%33<=32){ 
		            	 builder.append(text.charAt(j)); 
		             }  
					if(j%33==32){   
						list.add(builder.toString());   
					}
					if(j%33<32&j==text.length()-1){
						list.add(builder.toString().trim());   
					}
				}
			}
		}
		//给View传递数据
		mSampleView.setList(list);
		//更新View
		mSampleView.updateUI();		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_lottery://转盘
			AnimationUtile();
			break;
		case R.id.tv_lottery_detail://活动详情
			showTheDialog();
			break;
		case R.id.bt_ok://活动详情
			finish();
			break;
		case R.id.bt_refresh://活动详情
			finish();
			break;

		}
	}
	private AlertDialog dialog;
	//未开始
	private void showTheDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View contentView = View.inflate(this, R.layout.dialog_layout, null);
		TextView time = (TextView) contentView.findViewById(R.id.tv_dialog_content_time);
		Button ok = (Button) contentView.findViewById(R.id.bt_ok);
		time.setText("2014-10-18 17:09:27");
		
		ok.setOnClickListener(this);
		/* 
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        
        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
//        lp.width = 300; // 宽度
//        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
//        dialogWindow.setAttributes(lp);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
	}
	
	//刷新
	private void showTheDialog1() {
		AlertDialog.Builder builder = new Builder(this);
		View contentView = View.inflate(this, R.layout.dialog_layout1, null);
		TextView time = (TextView) contentView.findViewById(R.id.tv_dialog_refresh_time);
		Button refresh = (Button) contentView.findViewById(R.id.bt_refresh);
		time.setText("还有"+"23:59:59");
		
		refresh.setOnClickListener(this);
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		
	}

	/**
	 * 模拟服务器端抽奖
	 * @return 
	 */
	public float RandomDegree(){
		int num=(int)(Math.random()*1000)+1;
		if(num<=40){
			//角度随机数 90-135		区间1
			todegree=(float)(Math.random()*((133.0f-92.0f)+1.0f))+92.0f;
		}else if(40<num&num<=60){
			//135-180 	区间2		int result = Min + (int)(Math.random() * ((Max - Min) + 1));
			todegree=(float)(Math.random()*((178.0f-138.0f)+1.0f))+138.0f;
		}else if(60<num&num<=90){
			//180-225		区间3
			todegree=(float)(Math.random()*((223.0f-183.0f)+1.0f))+183.0f;
		}else if(90<num&num<=100){
			//225-270		区间4
			todegree=(float)(Math.random()*((268.0f-228.0f)+1.0f))+228.0f;
		}else if(820<num&num<=890){
			//315-360		区间6
			todegree=(float)(Math.random()*((358.0f-318.0f)+1.0f))+318.0f;
		}else if(890<num&num<=970){
			//0-45   		 区间7
			todegree=(float)(Math.random()*((43.0f-2.0f)+1.0f))+2.0f;
		}else if(970<num&num<=1000){
			//46-90 		区间8
			todegree=(float)(Math.random()*((88.0f-48.0f)+1.0f))+48.0f;
		}else{//谢谢 270-315 	区间5
			todegree=(float)(Math.random()*((313.0f-273.0f)+1.0f))+273.0f;
		}
		System.out.println("随机数："+num+"目标角度"+todegree);
		return todegree;
	}
	
	//转盘动画
	public void AnimationUtile(){
		todegree=RandomDegree();
		RotateAnimation  myAnimation_Rotate = new RotateAnimation(startdegree, todegree+720.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		myAnimation_Rotate.setDuration(5000);
        LinearInterpolator lins = new LinearInterpolator();
        myAnimation_Rotate.setInterpolator(lins);
        myAnimation_Rotate.setFillAfter(true);
        lottery_point.startAnimation(myAnimation_Rotate);
        startdegree=todegree;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 try{
			 UIHelper.cloesLoadDialog();
	        }catch (Exception e) {
	            System.out.println("myDialog取消，失败！");
	            // TODO: handle exception
	        }
		super.onDestroy();
		
	}
}
