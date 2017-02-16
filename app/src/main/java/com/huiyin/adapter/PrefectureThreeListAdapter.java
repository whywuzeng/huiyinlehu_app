package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.PrefectureThree.Suibian;
import com.huiyin.utils.ImageManager;



public class PrefectureThreeListAdapter extends BaseAdapter {
	private List<Suibian> lists;
	private Context ct;
	
	public PrefectureThreeListAdapter() {
		
	}
	
	public PrefectureThreeListAdapter(Context ct) {
		this.ct = ct;
		this.lists = new ArrayList<Suibian>();
	}
	
	/*public PrefectureThreeListAdapter(Context ct, List<Suibian> newlists) {
		this.ct = ct;
		this.lists = newlists;
	}*/
	
	@Override
	public int getCount() {
		if (lists!=null&lists.size()>0) {
			return lists.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (lists!=null&lists.size()>0) {
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
		
		if (position%2==0) {//偶数
			if (convertView == null) {
				hodler = new ViewHodler();
				convertView = View.inflate(ct, R.layout.prefecture_list_item3_1,null);
				hodler.datu=(ImageView) convertView.findViewById(R.id.iv_big_img);
				hodler.IMG1 = (ImageView) convertView
						.findViewById(R.id.iv_snall_img1);
				hodler.IMG2 = (ImageView) convertView
						.findViewById(R.id.iv_small_img2);

				convertView.setTag(hodler);

			} else {
				hodler = (ViewHodler) convertView.getTag();
			}
			
			if (lists!=null&lists.size()>0) {

				if (lists.get(position).datu != null) {
					ImageManager.LoadWithServer(lists.get(position).datu, hodler.datu);
				}
				
				hodler.datu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).datuId+"");
						ct.startActivity(intent);
						
					}
				});
				
				if (lists.get(position).IMG1 != null) {
					ImageManager.LoadWithServer(lists.get(position).IMG1, hodler.IMG1);
				}
				
				hodler.IMG1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).ID1+"");
						ct.startActivity(intent);
					}
				});
				
				if (lists.get(position).IMG2 == null) {
					hodler.IMG2.setImageResource(R.drawable.ic_launcher);
				} else {
					ImageManager.LoadWithServer(lists.get(position).IMG2, hodler.IMG2);
				}
				
				hodler.IMG2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).ID2+"");
						ct.startActivity(intent);
					}
				});
			}
			
			return convertView;
		}else{//奇数
			if (convertView == null) {
				hodler = new ViewHodler();
				convertView = View.inflate(ct, R.layout.prefecture_list_item3_2,null);
				hodler.datu=(ImageView) convertView.findViewById(R.id.iv_big_img);
				hodler.IMG1 = (ImageView) convertView
						.findViewById(R.id.iv_snall_img1);
				hodler.IMG2 = (ImageView) convertView
						.findViewById(R.id.iv_small_img2);

				convertView.setTag(hodler);

			} else {
				hodler = (ViewHodler) convertView.getTag();
			}
			if (lists!=null&lists.size()>0) {
				
			
				if (lists.get(position).datu != null) {
					ImageManager.LoadWithServer(lists.get(position).datu, hodler.datu);
				}
				
				hodler.datu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).datuId+"");
						ct.startActivity(intent);
					}
				});
				
				if (lists.get(position).IMG1 != null) {
					ImageManager.LoadWithServer(lists.get(position).IMG1, hodler.IMG1);
				}
				
				hodler.IMG1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).ID1+"");
						ct.startActivity(intent);
					}
				});
				
				if (lists.get(position).IMG2 != null) {
					ImageManager.LoadWithServer(lists.get(position).IMG2, hodler.IMG2);
				}
				
				hodler.IMG2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ct, com.huiyin.ui.classic.GoodsDetailActivity.class);
						intent.putExtra("goods_detail_id", lists.get(position).ID2+"");
						ct.startActivity(intent);
					}
				});
				
			}
			
			return convertView;
		}
	}
	
	class ViewHodler {
		ImageView datu;
		ImageView IMG1;
		ImageView IMG2;
	}
	
	/*public int getId(int arg0) {
		return ((Suibian) lists.get(arg0)).ID;
	}*/

	public void deleteItem() {
		lists.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<Suibian> temp) {
		if (temp == null || temp.size() == 0) {
			return;
		}
		if (temp instanceof ArrayList) {
			lists.addAll(temp);
		}
		notifyDataSetChanged();
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


