package com.huiyin.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.huiyin.R;

/**
 * Created with IntelliJ IDEA.
 * User: linyanjun
 * Date: 14-3-24
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class CustomListView extends ListView {
    private int leftlist_img_width;
    public CustomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        leftlist_img_width=(int) context.getResources().getDimension(R.dimen.leftlist_img_width);
    }

    public void computeScroll() {
        super.computeScroll();
        //this.scrollTo();
    }
}
