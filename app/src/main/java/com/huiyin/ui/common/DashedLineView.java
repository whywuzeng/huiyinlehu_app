package com.huiyin.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashedLineView extends View {
	/**
	 * 虚线
	 * 
	 * @param context
	 * @param attrs
	 */
	public DashedLineView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		Path path = new Path();
		path.moveTo(0, 5);
		path.lineTo(2000, 5);
		PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}
}





