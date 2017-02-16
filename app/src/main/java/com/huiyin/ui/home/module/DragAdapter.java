package com.huiyin.ui.home.module;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.widget.grid.drag.DragGridBaseAdapter;
import com.huiyin.R;
import com.huiyin.utils.ImageManager;

public class DragAdapter extends BaseAdapter implements DragGridBaseAdapter{
	private ArrayList<Module> list;
	private LayoutInflater mInflater;
	private int mHidePosition = -1;
	private boolean isLongItem = false;
	private Context mContext;
	
	public DragAdapter(Context context) {
	    this.mContext = context;
		this.list = new ArrayList<Module>();
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void delAllItem(){
	    list.clear();
	    notifyDataSetChanged();
	}
	
	public void addItems(ArrayList<Module> arr){
	    list.addAll(arr);
	    notifyDataSetChanged();
	}
	
	public ArrayList<Module> getAllItemData(){
	    return list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Module module = list.get(position);
		convertView = mInflater.inflate(R.layout.home_drag_grid_item, null);
		ImageView iconIv = (ImageView) convertView.findViewById(R.id.home_drag_grid_item_icon_iv);
		ImageView delIv = (ImageView) convertView.findViewById(R.id.home_drag_grid_item_del_iv);
		TextView nameTv = (TextView) convertView.findViewById(R.id.home_drag_grid_item_name_tv);
		
		ImageManager.Load(module.imagePath, iconIv);
		if (module.id == -1) {
		    delIv.setVisibility(View.GONE);
        } else if(isLongItem) {
            delIv.setVisibility(View.VISIBLE);
            delIv.setOnClickListener(new DelListener(module));
        } else {
            delIv.setVisibility(View.GONE);
        }
		nameTv.setText(module.name);
		
		
		if(position == mHidePosition){
			convertView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	

	@Override
	public void reorderItems(int oldPosition, int newPosition) {
		Module temp = list.get(oldPosition);
		if(oldPosition < newPosition){
			for(int i=oldPosition; i<newPosition; i++){
				Collections.swap(list, i, i+1);
			}
		}else if(oldPosition > newPosition){
			for(int i=oldPosition; i>newPosition; i--){
				Collections.swap(list, i, i-1);
			}
		}
		
		list.set(newPosition, temp);
	}
	
	@Override
	public void setHideItem(int hidePosition) {
		this.mHidePosition = hidePosition; 
		notifyDataSetChanged();
	}

    @Override
    public void longpressItem() {
        isLongItem = true;
        notifyDataSetChanged();
    }

    @Override
    public void cancelLongPressItem() {
        isLongItem = false;
        notifyDataSetChanged();
    }

    class DelListener implements OnClickListener {
        Module module;

        public DelListener(Module module) {
            this.module = module;
        }

        @Override
        public void onClick(View arg0) {
            list.remove(module);
            notifyDataSetChanged();
        }
    }
    
}
