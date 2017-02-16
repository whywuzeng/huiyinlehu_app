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
import com.huiyin.api.URLs;
import com.huiyin.utils.ImageManager;

public class Drag2Adapter extends BaseAdapter implements DragGridBaseAdapter{
    
	private ArrayList<Module> list;
	private LayoutInflater mInflater;
	private int mHidePosition = -1;
	private boolean isLongItem = false;
	private Context mContext;
	private ModeluPop modeluPop;
	private boolean isDrag = false;
	
	public Drag2Adapter(Context context, ModeluPop modeluPop, boolean isDrag){
	    mContext = context;
	    this.modeluPop = modeluPop;
	    this.isDrag = isDrag;
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
	
	public ArrayList<Module> getAllItemData(){
	    return list;
	}
	
	public void addItem(Module module){
	    list.add(module);
        notifyDataSetChanged();
	}
	
	public void delItem(Module module){
	    list.remove(module);
        notifyDataSetChanged();
	}
	
	public void delAllItem(){
	    list.clear();
	    notifyDataSetChanged();
	}
	
	public void addItems(ArrayList<Module> arr){
	    list.addAll(arr);
	    notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Module module = list.get(position);
		convertView = mInflater.inflate(R.layout.modelu_pop_grid_item, null);
		RoundImageView iconIv = (RoundImageView) convertView.findViewById(R.id.home_drag_grid_item_icon_iv);
		ImageView delIv = (ImageView) convertView.findViewById(R.id.home_drag_grid_item_del_iv);
		TextView nameTv = (TextView) convertView.findViewById(R.id.home_drag_grid_item_name_tv);
		if (isDrag) {
		    delIv.setOnClickListener(new DelOrAddListener(module, true));
		    if (isLongItem && module.id != -1) {
	            delIv.setVisibility(View.VISIBLE);
	        } else {
	            delIv.setVisibility(View.GONE);
	        }
        } else {
            delIv.setOnClickListener(new DelOrAddListener(module, false));
            delIv.setImageResource(R.drawable.icon_tianjia_tian);
            delIv.setVisibility(View.VISIBLE);
        }
		
		ImageManager.Load(module.imagePath, iconIv);
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
    
    class DelOrAddListener implements OnClickListener{
        private boolean isDel;
        private Module module;
        
        public DelOrAddListener(Module module, boolean isDel){
            this.isDel = isDel;
            this.module = module;
        }        
        
        @Override
        public void onClick(View arg0) {
            if (isDel) {
                modeluPop.delModelu(module);
            } else {
                modeluPop.addModelu(module);                
            }
        }
    }

}
