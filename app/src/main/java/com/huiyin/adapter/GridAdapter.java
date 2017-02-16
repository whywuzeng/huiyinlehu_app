package com.huiyin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;

public class GridAdapter extends BaseAdapter { 
	
    private LayoutInflater inflater; 
    private ArrayList<Picture> pictures; 
 
    public GridAdapter(String[] names, String[] images,String [] price, Context context) 
    { 
        super(); 
        pictures = new ArrayList<Picture>(); 
        inflater = LayoutInflater.from(context); 
        for (int i = 0; i < pictures.size(); i++) 
        { 
            Picture picture = new Picture(names[i], images[i],price [i]); 
            Log.i("names[i]", "names[i]--"+names[i]);
            pictures.add(picture); 
        } 
    } 
 
    @Override
    public int getCount() 
    { 
        if (null != pictures) 
        { 
            return pictures.size(); 
        } else
        { 
            return 0; 
        } 
    } 
 
    @Override
    public Object getItem(int position) 
    { 
        return pictures.get(position); 
    } 
 
    @Override
    public long getItemId(int position) 
    { 
        return position; 
    } 
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    { 
        ViewHolder viewHolder; 
        if (convertView == null) 
        { 
            convertView = inflater.inflate(R.layout.browse_item, null); 
            viewHolder = new ViewHolder(); 
            viewHolder.name = (TextView) convertView.findViewById(R.id.browse_item_name); 
            viewHolder.image = (ImageView) convertView.findViewById(R.id.browse_item_iv); 
            viewHolder.price = (TextView) convertView.findViewById(R.id.browse_item_price); 
            convertView.setTag(viewHolder); 
            
        } else
        { 
            viewHolder = (ViewHolder) convertView.getTag(); 
        } 
        
        viewHolder.name.setText(pictures.get(position).getname()); 
        Log.i("pictures.get(position).getname()", "pictures.get(position).getname()----"+pictures.get(position).getname());
//        ImageLoader.getInstance().displayImage(URLs.IMAGE_URL+pictures.get(position).getimage_path(),viewHolder.image);
        viewHolder.price.setText(pictures.get(position).getPrice()); 
        
        return convertView; 
        
    } 
 
}

 
 
class ViewHolder 
{ 
    public TextView name; 
    public ImageView image; 
    public TextView  price;
} 
 
class Picture 
{ 
    private String name; 
    private String image_path ; 
    private String price;
 
    public Picture() 
    { 
        super(); 
    } 
 
    public Picture(String name, String image_path,String price) 
    { 
        super(); 
        this.name = name; 
        this.image_path = image_path; 
        this.price = price; 
        
    } 
 
    public String getname() 
    { 
        return name; 
    } 
 
    public void setname(String name) 
    { 
        this.name = name; 
    } 
 
    public String getimage_path() 
    { 
        return image_path; 
    } 
 
    public void setimage_path(String image_path) 
    { 
        this.image_path = image_path; 
    } 
    
    public void setPrice(String price) {
		this.price = price;
	}
    
    public String getPrice() {
		return price;
	}
    
} 





