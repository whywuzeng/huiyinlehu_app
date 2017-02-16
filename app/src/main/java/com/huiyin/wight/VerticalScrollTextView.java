package com.huiyin.wight;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalScrollTextView extends TextView {

	private Paint mPaint;
	private List<String> list;
	
	private float mX;
	private Paint mPathPaint;	
	public int index = 0;
	public float mTouchHistoryY;
	private int mY;	
	private float middleY;// y轴中间
	private static final int DY = 30; // 每一行的间隔
	
	/*private float step =0f;     
    private Paint mPaint=new Paint(); 
    private String text;   
    private float width;   
    private List<String> list = new ArrayList<String>();    //分行保存textview的显示信息。   
*/
	public VerticalScrollTextView(Context context) {
		super(context);
		init();
	}

	public VerticalScrollTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VerticalScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setFocusable(true);
		//这里主要处理如果没有传入内容显示的默认值
		if(list==null){
			list=new ArrayList<String>();
//			list.add(0, sen);
		}		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(14);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.SERIF);		
		
		// 高亮部分 当前歌词
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(Color.WHITE);
		mPathPaint.setTextSize(14);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(0xEFeffff);
		Paint p = mPaint;
		Paint p2 = mPathPaint;
		p.setTextAlign(Paint.Align.LEFT);
		if (index == -1)
			return;
		p2.setTextAlign(Paint.Align.LEFT);
		// 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置
		if(index<list.size()-1){
			canvas.drawText(list.get(index), mX, middleY, p2);
		}
		float tempY = middleY;
		// 画出本句之前的句子
		for (int i = index - 1; i >= 0; i--) {			
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			canvas.drawText(list.get(i), mX, tempY, p);			
		}
		tempY = middleY;
		// 画出本句之后的句子
		for (int i = index + 1; i < list.size(); i++) {
			// 往下推移
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(list.get(i), mX, tempY, p);			
		}
	}
		
	protected void onSizeChanged(int w, int h, int ow, int oh) {
			super.onSizeChanged(w, h, ow, oh);
//			mX = w * 0.5f; 
			mX = 26f; 
			mY = h;
			middleY = h * 0.4f;
		}

	private long updateIndex(int index) {	
			if (index == -1)
				return -1;
			this.index=index;		
			return index;
		}
		
	
	public void updateUI(){
			new Thread(new updateThread()).start();
		}
		
	class updateThread implements Runnable {
			long time = 1600;
			int i=0;
			public void run() {
				while (true) {
					long sleeptime = updateIndex(i);
					time += sleeptime;
					mHandler.post(mUpdateResults);
					if (sleeptime == -1)
						return;
					try {
						Thread.sleep(time);
						i++;
						if(i==getList().size())
							i=0;
					} catch (InterruptedException e) {					
						e.printStackTrace();
					}
				}
			}
		}
	
	Handler mHandler = new Handler();
		Runnable mUpdateResults = new Runnable() {
			public void run() {
				invalidate(); 
			}
		};
		
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	/*@Override   
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {           
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
        width = MeasureSpec.getSize(widthMeasureSpec);      
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);     
        if (widthMode != MeasureSpec.EXACTLY) {      
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");   
        }         
         
        text=getText().toString();  
        if(text==null|text.length()==0){   
                 
            return ;   
        }         
           
       //下面的代码是根据宽度和字体大小，来计算textview显示的行数。   
   
        list.clear();   
        StringBuilder builder =null;  
        for(int i=0;i<text.length();i++){  
            if(i%20==0){  
                builder = new StringBuilder();   
            }  
             if(i%20<=19){   
                 builder.append(text.charAt(i));   
             }  
             if(i%20==19){   
                 list.add(builder.toString());   
             }  
               
        } 
        System.out.println("textviewscroll"+list.size());   
         

    }   
   
   
    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。   
     @Override   
    public void onDraw(Canvas canvas) {   
       if(list.size()==0)  return;   
         
       mPaint.setTextSize(20f);//设置字体大小  
       mPaint.setAntiAlias(true);
       mPaint.setColor(Color.WHITE);
        for(int i = 0; i < list.size(); i++) {   
            canvas.drawText(list.get(i), 20, this.getHeight()+(i+1)*mPaint.getTextSize()-step+30, mPaint);   
        }   
        invalidate();      
          
        step = step+0.3f;   
        if (step >= this.getHeight()+list.size()*mPaint.getTextSize()) {   
            step = 0;   
        }           
    }  */
	
}
