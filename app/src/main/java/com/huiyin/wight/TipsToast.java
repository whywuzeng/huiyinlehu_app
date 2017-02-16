package com.huiyin.wight;


import com.huiyin.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




/**    
 *     
 * ��Ŀ��ƣ�Skyworth_allhere    
 * ����ƣ�TipsToast    
 * �������� �Զ�����ʾToast   
 * �����ˣ�zengweijie
 * ����ʱ�䣺2014��5��15�� ����2:33:34    
 * �޸��ˣ� 
 * �޸�ʱ�䣺2014��5��15�� ����2:33:34    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class TipsToast extends Toast {

	public TipsToast(Context context) {
		super(context);
	}

	public static TipsToast makeText(Context context, CharSequence text,
			int duration) {
		TipsToast result = new TipsToast(context);

		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.tips_toast_view, null);
		TextView tv = (TextView) v.findViewById(R.id.tips_msg);
		tv.setText(text);

		result.setView(v);
		// setGravity������������λ�ã��˴�Ϊ��ֱ����
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);

		return result;
	}

	public static TipsToast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId),
				duration);
	}

	public void setIcon(int iconResId) {
		if (getView() == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		ImageView iv = (ImageView) getView().findViewById(R.id.tips_icon);
		if (iv == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		iv.setImageResource(iconResId);
	}

	@Override
	public void setText(CharSequence s) {
		if (getView() == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		TextView tv = (TextView) getView().findViewById(R.id.tips_msg);
		if (tv == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		tv.setText(s);
	}
}
