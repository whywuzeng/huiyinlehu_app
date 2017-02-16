package com.huiyin.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.TodayHeadLine.Commodity;
import com.huiyin.utils.ImageManager;

public class NewTodayListAdapter extends BaseAdapter {
	private List<Commodity> lists;
	private Context ct;
	
	public NewTodayListAdapter() {
		
	}
	
	public NewTodayListAdapter(Context ct) {
		this.ct = ct;
	}
	
	public NewTodayListAdapter(Context ct, List<Commodity> newlists) {
		this.ct = ct;
		this.lists = newlists;
	}
	
	@Override
	public int getCount() {
		if(lists!=null&lists.size()>0){
			
			return lists.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if(lists!=null&lists.size()>0){
			return lists.get(position);
		}else {
			return position;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler hodler = null;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = View.inflate(ct, R.layout.news_list_item,null);
			hodler.image = (ImageView) convertView
					.findViewById(R.id.iv_img);
			hodler.name = (TextView) convertView
					.findViewById(R.id.tv_name);

			convertView.setTag(hodler);

		} else {
			hodler = (ViewHodler) convertView.getTag();
		}

		if (lists.get(position).COMMODITY_IMAGE_PATH != null) {
			ImageManager.Load(URLs.IMAGE_URL+lists.get(position).COMMODITY_IMAGE_PATH, hodler.image);
		}
		if (lists!= null&lists.size()>0) {
			hodler.name.setText(lists.get(position).COMMODITY_NAME);
		}
		
		return convertView;
		/*View view=View.inflate(ct, R.layout.news_list_item,null);
		return view;*/
	}
	
	class ViewHodler {
		ImageView image;
		TextView name;
	}
	
	public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll(" ");
        }
        return dest;
    }
}


