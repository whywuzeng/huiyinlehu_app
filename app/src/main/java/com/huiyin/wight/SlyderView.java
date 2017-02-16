package com.huiyin.wight;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.huiyin.R;

public class SlyderView extends View {
	
	//屏幕宽度
	private int screenW= 0;

	//屏幕的高度
	private int screenH= 0;
	
	//分割的文字
//	public String [] strs = {"积分","乐虎券","积分","乐虎券","谢谢","乐虎券","积分","乐虎券"};
	public List<String> strs =new ArrayList<String>();
	//分割的积分
	public String [] str = {"500","800","800","1000"," ","100","100","500"};
	
	//分割的颜色
//	private int [] colos = new int[]{ 0xfed9c960, 0xfe57c8c8, 0xfe9fe558, 0xfef6b000, 0xfef46212, 0xfecf2911,0xfe9d3011,0xfe9fe558 }; 
	
	//画笔
	private Paint paint;
	
	//旋转台里的分隔线
	/*private Bitmap divider;
	
	private static Context context;
	
	private int nYoffset = 0;*/
	 
    /**
     * 图标，及对应的Activity等 集合
     */
//    private List<FunctionIcon> fiList = new ArrayList<FunctionIcon>();
    /**
     * 旋转台背景图
     */
    private Bitmap turntable;
	
	/**
     * 旋转角度
     */
    private double turnDegree;
    /**
     * 45度角
     */
//    private final double Angle45Degree = Math.PI * 2 / 5; // 圆周率/4
    
    /**
     * 小图标离圆心的距离
     */
//    private float arcRadius;
    /**
     * 分隔线离圆心的距离
     */
//    private float inArcRadius;
    /**
     * 图标 离圆心的距离icon(left,top)
     */
//    private float iconRadius;
    /**
     * 触摸是否在旋转台
     */
//    private boolean touchInTurnTable; // 元件中心
    /**
     * 触摸是否在中心控件
     */
//    private boolean touchInCenterAre; // 元件中心
    /**
     * 
     * move
     */
//    private double touchDegreeFromX, _touchDegreeFromX;
    
    private int [] drgrees = {45,45,45,45,45,45,45,45};
    
    //文字的大小
    private float textSize  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
	
    //文字的颜色
    private int textcolor = Color.WHITE;
    
    //画文字的距离
    private float textdis = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 80, getResources().getDisplayMetrics());
    private float textdis1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100, getResources().getDisplayMetrics());
    
    //圆的半径
    private float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100, getResources().getDisplayMetrics());
    
    //圆心
    private float centerX;
    private float centerY;
    
	public SlyderView(Context context) {
		super(context);
	}

	public SlyderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SlyderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 /**
     * 释放内存
     */
    private void releaseRes() {
       /* if (turntable != null) {
            turntable.recycle();// 释放资源
            turntable = null;
        }
        if (divider != null) {
            divider.recycle();
            divider = null;
        }
        for (FunctionIcon fi : fiList) {
            if (fi.icon != null) {
                fi.icon.recycle();
                fi.icon = null;
            }
            if (fi._icon != null) {
                fi._icon.recycle();
                fi._icon = null;
            }
        }*/
    }
    
    // 当view的大小发生变化时触发，相当于初始化界面
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint = new Paint();
		paint.setAntiAlias(true);//抗锯齿
        if (screenW == 0) {
            releaseRes();
            /*screenW = w;
            screenH=h;*/
            // 背景大圆图
            turntable = BitmapFactory.decodeResource(getResources(),R.drawable.lockey55);
            List<String> slist=new ArrayList<String>();
            slist.add("积分");
            slist.add("乐虎券");
            slist.add("积分");
            slist.add("乐虎券");
            slist.add("谢谢");
            slist.add("乐虎券");
            slist.add("积分");
            slist.add("乐虎券");
            
            strs=slist;
            
           
            // 圆图中的分隔线
//            divider = BitmapFactory.decodeResource(getResources(),R.drawable.tt_divider);
//            arcRadius = screenW / 2.0f + 70;// 半径 相当于圆的半径(小图标离圆心的距离)
            // inArcRadius = mWidth / 8.0f;// 分隔线离圆心的距离
            // 向fiList加入值
            /*for (int i = 0; i < strs.length; i++) { // 对应的Activity
                FunctionIcon fi = new FunctionIcon();
//                fi.icon = BitmapFactory.decodeResource(getResources(),resIcons[i]);
//                fi._icon = BitmapFactory.decodeResource(getResources(),res_Icons[i]);
//                fi.click = false;
                fi.mindegree = Angle45Degree * i;//每一个扇形的角度
 
                *//**
                 * Math.atan2(x,y) x 指定点的 x 坐标的数字。 y 指定点的 y 坐标的数字。
                 * 计算出来的结果translateDegree是一个弧度值, 也可以表示相对直角三角形对角的角，其中 x 是临边边长，而 y
                 * 是对边边长。 Math.cos 余弦函数 Math.acos 反余弦函数
                 *//*
                fi.translateDegree = Math.atan2(
                        fi.icon.getWidth() / 2,
                        arcRadius * Math.cos(Angle45Degree / 2)
                                - fi.icon.getHeight())
                        + Angle45Degree / 2 + Angle45Degree * i - 180;
                fi.rotateDegree = (float) ((Angle45Degree * i - Angle45Degree * 5 / 3) -180);//ccy up
                // fi.activity = activities[i];
                fiList.add(fi);
            }
            *//**
             * Math.pow(x,y)：x的y次方 Math.sqrt(x)：平方根
             *//*
            iconRadius = (float) Math.sqrt(Math.pow(
                    (arcRadius * Math.cos(Math.PI / 5) - fiList.get(0).icon
                            .getHeight()), 2)
                    + Math.pow(fiList.get(0).icon.getWidth() / 2, 2)) - 10;*/
        }
    }
 
    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
		// 画一个位图为大（大圆图，src为你要画的bitmap的区域，dst为你要显示在屏幕上的区域，）
		canvas.drawBitmap(turntable, new Rect(0, 0, turntable.getWidth(),
				turntable.getHeight()), new Rect(0, 0, turntable.getWidth(),
						turntable.getWidth()), paint);
		screenW=turntable.getWidth();
        screenH=turntable.getHeight();
        
        centerX = screenW/2;
        centerY = screenH/2;
		// 移动原点
        canvas.translate(0,0);
//        System.out.println(centerX+"移动原点"+ centerY+"移动原点");
        
        
        float start = 0;
        //画文字
        paint.setColor(textcolor);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        for(int i=0;i<drgrees.length;i++){
        	canvas.save();
        	canvas.rotate(start+drgrees[i]/2, centerX, centerY);
        	canvas.translate(centerX+textdis, centerY);
        	canvas.rotate(90.0f);
//        	canvas.drawText(strs[i],0,0, paint);
        	canvas.drawText(strs.get(i),0,0, paint);
        	canvas.rotate(-90.0f);
        	canvas.translate(0,0);
        	canvas.restore();
        	start += drgrees[i];
        }
        
        start = 0;
        for(int i=0;i<drgrees.length;i++){
        	canvas.save();
        	canvas.rotate(start+drgrees[i]/2, centerX, centerY);
        	canvas.translate(centerX+textdis1, centerY);
        	canvas.rotate(90.0f);
        	canvas.drawText(str[i],0, 0, paint);
        	canvas.rotate(-90.0f);
        	canvas.translate(0,0);
        	canvas.restore();
        	start += drgrees[i];
        }
        canvas.save();
        
        // 平移（x,y）个像素单位
        /*nYoffset = canvas.getWidth() / 2;
        //nYoffset=canvas.getHeight()/2;
        canvas.translate(0, nYoffset);
        // canvas.translate(0, canvas.getWidth() / 2 +300);//ccy
        // canvas.drawBitmap(bitmap, src, dst, paint)
        // 画一个位图为大（大圆图，src为你要画的bitmap的区域，dst为你要显示在屏幕上的区域，）
        canvas.drawBitmap(turntable, new Rect(0, 0, turntable.getWidth(),
                turntable.getHeight()), new Rect(0, 0, canvas.getWidth(),
                canvas.getWidth()), paint);
        // 移动原点
        canvas.translate(mWidth / 2, canvas.getWidth() / 2);
        // Matrix类用于对图像进行移动缩放旋转等等操作
        Matrix matrix = new Matrix();
        for (FunctionIcon fi : fiList) {
            matrix.setValues(new float[] { 
                    (float) Math.cos(fi.rotateDegree),
                    (float) -Math.sin(fi.rotateDegree),
                    (float) (iconRadius * Math.cos(fi.translateDegree)),
                    (float) Math.sin(fi.rotateDegree),
                    (float) Math.cos(fi.rotateDegree),
                    (float) (iconRadius * Math.sin(fi.translateDegree)),
                    0, 0,1 });
             
            if (fi.click) {
                // 把点击的图片画上去
                canvas.drawBitmap(fi._icon, matrix, paint);
                fi.click = false;
            } else {
                // 把没点击的图片画上去
                canvas.drawBitmap(fi.icon, matrix, paint);
            }
            // 线的旋转角度
            double dividerRotateDegree = fi.rotateDegree + Angle45Degree * 5
                    / 2;
            if (dividerRotateDegree > 2 * Math.PI) {
                dividerRotateDegree -= 2 * Math.PI;
            } else if (dividerRotateDegree < 0) {
                dividerRotateDegree += 2 * Math.PI;
            }
            double dividerTranslateDegree = fi.mindegree;
            matrix.setValues(new float[] {
                    (float) Math.cos(dividerRotateDegree),
                    (float) -Math.sin(dividerRotateDegree),
                    (float) (inArcRadius * Math.cos(dividerTranslateDegree)),
                    (float) Math.sin(dividerRotateDegree),
                    (float) Math.cos(dividerRotateDegree),
                    (float) (inArcRadius * Math.sin(dividerTranslateDegree)),
                    0, 0, 1 });
            // 把线画上去
            canvas.drawBitmap(divider, matrix, paint);
        }*/
    }
 
   /* public List<String> s=new ArrayList<String>();
    public  void showHelp(Context context) {
        List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < s.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("icon", res_Icons[i]);
            // map.put("title",
            // context.getResources().getString(res_Titles[i]));
            itemlist.add(map);
        }
        // final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        // LayoutInflater li = LayoutInflater.from(context);
        // RelativeLayout rl = (RelativeLayout) li.inflate(R.layout.help, null);
        // ImageView iv = (ImageView) rl.findViewById(R.id.close);
        // iv.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // dialog.cancel();
        // dialog.dismiss();
        // }
        // });
        // ListView lv = (ListView) rl.findViewById(R.id.help);
        // SimpleAdapter adapter = new SimpleAdapter(context,
        // (List<Map<String, Object>>) itemlist, R.layout.help_item,
        // new String[] { "icon", "title" }, new int[] { R.id.icon,
        // R.id.title });
        // lv.setAdapter(adapter);
        // dialog.setContentView(rl);
        // dialog.show();
    }
 
    *//**
     * 是否在旋转台区域
     * 
     * @param x
     * @param y
     * @return
     *//*
    private boolean inTurnTable(float x, float y) {
        // 出圆的面积
        double outCircle = Math.PI * arcRadius * arcRadius;
        double inCircle = Math.PI * inArcRadius * inArcRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if (fingerCircle < outCircle && fingerCircle > inCircle) {
            return true;
        } else {
            return false;
        }
    }
 
    *//**
     * 是否是圆中心的区域
     * 
     * @param x
     * @param y
     * @return
     *//*
    private boolean inCenterArc(float x, float y) {
        // 圆的面积
        double inCircle = Math.PI * inArcRadius * inArcRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if (fingerCircle < inCircle) {
            return true;
        } else {
            return false;
        }
    }
 
    *//**
     * 
     *//*
    class FunctionIcon {
        *//**
         * 默认状态图标
         *//*
        public Bitmap icon;
        *//**
         * 按下时状态图标
         *//*
        public Bitmap _icon;
        *//**
         * 是否点击
         *//*
        public boolean click;
        *//**
         * 最小度数
         *//*
        public double mindegree;
        *//**
         * icon 转变度数
         *//*
        public double translateDegree;
        *//**
         * icon 旋转角度
         *//*
        public float rotateDegree;
        *//**
         * 对应的 activity
         *//*
        @SuppressWarnings({ "rawtypes" })
        public Class activity;
    }
	*/
}
