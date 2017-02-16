package com.huiyin.adapter;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.flash.FlashPrefectureActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.seckill.SeckillActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.ui.show.TheShowCommentActivity;
import com.huiyin.ui.user.AfterSaleDetailActivity;
import com.huiyin.ui.user.KefuCenterActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.MessageWebActivity;
import com.huiyin.ui.user.MyOrderDetailActivity;
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.ui.user.MyMessageActivity.SysMessage;
import com.huiyin.ui.user.YuYueShenQingActivity;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UserinfoPreferenceUtil;
import com.huiyin.wight.ExpandableTextView;

public class MessageAdapter extends BaseAdapter{
	
	private ArrayList<SysMessage> messages = new ArrayList<SysMessage>();
	Context context;
	
	public MessageAdapter(Context context) {
		this.context=context;
	}
	
	public ArrayList<SysMessage> returnDatas(){
		return messages;
	}
	
	public void add(ArrayList<SysMessage> data){
		this.messages.addAll(data);
		notifyDataSetChanged();
	}

	public void refresh(ArrayList<SysMessage> arrayList) {
		this.messages.clear();
		this.messages = null;
		this.messages = arrayList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context,R.layout.message_list_item, null);	 
			holder.ll_root = (LinearLayout)convertView.findViewById(R.id.ll_root);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (ExpandableTextView) convertView.findViewById(R.id.content);
			holder.img_arraw = (ImageView) convertView.findViewById(R.id.img_arraw);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			
			holder.content_root = (LinearLayout)convertView.findViewById(R.id.content_root);
			holder.content_root2 = (LinearLayout)convertView.findViewById(R.id.content_root2);
			holder.content2 = (TextView)convertView.findViewById(R.id.content2);
			holder.examine = (TextView)convertView.findViewById(R.id.examine);
			
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(messages.get(position).SEND_TITLE);
		holder.time.setText(StringUtils.friendly_time(messages.get(position).SEND_TIME));
		holder.content.setText(messages.get(position).SEND_CONTENT);
		holder.content.setImg(holder.img_arraw,holder.img);
		
		if(messages.get(position).READ_STATUS.equals("1")){
			holder.title.setTextColor(0xffb6b6b6);
			holder.content.setTextColor(0xffb6b6b6);
			holder.content2.setTextColor(0xffb6b6b6);
			holder.examine.setTextColor(0xffb6b6b6);
	    }else{
	      	holder.title.setTextColor(0xff000000);
			holder.content.setTextColor(0xff000000);
			holder.content2.setTextColor(0xff000000);
			holder.examine.setTextColor(0xff000000);
	    }
		
		if(messages.get(position).SEND_CONTENT !=null){
			if(messages.get(position).SEND_CONTENT.length() < 40){
				holder.img_arraw.setVisibility(View.GONE);
				holder.ll_root.setVisibility(View.GONE);
			}else{
				holder.img_arraw.setVisibility(View.VISIBLE);
				holder.ll_root.setVisibility(View.GONE);
			}
		}else{
			holder.img_arraw.setVisibility(View.GONE);
			holder.ll_root.setVisibility(View.GONE);
		}
		
		
		if(!messages.get(position).VIEW_TYPE.equals("1")&&!messages.get(position).VIEW_TYPE.equals("5")){
			holder.ll_root.setVisibility(View.VISIBLE);
			holder.content_root.setVisibility(View.GONE);
			holder.content_root2.setVisibility(View.VISIBLE);
			try {
				//holder.content2.setText(messages.get(position).SEND_CONTENT.replaceAll("[^\\u4e00-\\u9fa5]", ""));
				holder.content2.setText(messages.get(position).SEND_CONTENT.replaceAll("</?[^>]+>", "").replaceAll("\\s*|\t|\r|\n","").replaceAll("&nbsp;", ""));
			} catch (Exception e) {
			}
		}else{
			holder.content_root.setVisibility(View.VISIBLE);
			holder.content_root2.setVisibility(View.GONE);
			holder.ll_root.setVisibility(View.GONE);
			holder.content.setText(messages.get(position).SEND_CONTENT);
		}
		
		convertView.setOnClickListener(new MyViewOnclick(holder.title, holder.time, holder.content,holder.content2,holder.examine, position));
		
		return convertView;
	}
	
	
   class ViewHolder{
		TextView title;
		TextView time;
		ExpandableTextView content;
		ImageView img_arraw;
		ImageView img;
		LinearLayout ll_root;
		LinearLayout content_root;
		LinearLayout content_root2;
		TextView content2;
		TextView examine;
	}
   
   
   
   private void appLetterReader(String letterId,final int position,final Context context){
		if (AppContext.getInstance().getUserId()==null) {
			return;
		}
		RequstClient.appLetterReader(letterId, new CustomResponseHandler((Activity)context,false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(context, errorMsg,Toast.LENGTH_SHORT).show();
						return;
					}
					messages.get(position).READ_STATUS = "1";
					AppContext.getInstance().getUserInfo().systemMessage = Integer.parseInt(AppContext.getInstance().getUserInfo().systemMessage) - 1 +"";
					
					UserInfo mUserInfo = AppContext.getInstance().getUserInfo();
					UserinfoPreferenceUtil.saveUserInfo(context, mUserInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
   
   
   class MyViewOnclick implements View.OnClickListener{
       TextView title;
       TextView time;
       TextView content;
       TextView content2;
       TextView examine;
       int item;
	   
	   public MyViewOnclick(TextView title,TextView time,TextView content,TextView content2,TextView examine,int item) {
		   this.title = title;
		   this.time = time;
		   this.content = content;
		   this.content2 = content2;
		   this.examine = examine;
		   this.item = item;
	   }
	   
		@Override
		public void onClick(View v) {
			try {
				SysMessage message = messages.get(item);
				if(message.VIEW_TYPE.equals("1")){             //通知栏跳转
					if(message.JUMP_DATA != null){
						JSONObject object = new JSONObject(message.JUMP_DATA);
						Jump(object.getInt("sendType"),object);
					}
				}else if(message.VIEW_TYPE.equals("2")){
					Intent intent = new Intent(context,MessageWebActivity.class);
					intent.putExtra("htmlContent", message.SEND_CONTENT);
					intent.putExtra("url_type", message.URL_TYPE);
					intent.putExtra("id", message.URL_ID);
					intent.putExtra("commodityId", message.CID);
					context.startActivity(intent);
				}else if(message.VIEW_TYPE.equals("3")){
					switch (message.URL_TYPE) {
					//1.秒杀 2.闪购 3.新品预约/商品详情 4.分类聚合 5.快捷服务 6.促销活动
					case 1:
						judgeSeckill(message.URL_ID);
						break;
					case 2:
						Intent intent4 = new Intent(context,FlashPrefectureActivity.class);
						intent4.putExtra("id", message.URL_ID);
						context.startActivity(intent4);
						break;
					case 3:
						Intent intent3 = new Intent(context,GoodsDetailActivity.class);
						intent3.putExtra("goods_detail_id", message.CID + "");
						intent3.putExtra("subscribeId", message.URL_ID);
						context.startActivity(intent3);
						break;
					case 4:
						Intent intent2 = new Intent(context, ZhuanQuActivity.class);
						intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID,message.URL_ID + "");
						intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
						context.startActivity(intent2);
						break;
					case 5:
						connt(message.URL_ID);
						break;
					case 6:
						Intent intent6 = new Intent(context,ZhuanQuActivity.class);
						intent6.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
						intent6.putExtra(ZhuanQuActivity.INTENT_KEY_ID,message.URL_ID + "");
						intent6.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
						context.startActivity(intent6);
						break;
					case 7:
						Intent intent5 = new Intent(context,GoodsDetailActivity.class);
						intent5.putExtra("goods_detail_id", message.URL_ID + "");
						context.startActivity(intent5);
						break;
					}
					
				}else if(message.VIEW_TYPE.equals("4")){
					Intent intent = new Intent(context,MessageWebActivity.class);
					intent.putExtra("htmlContent", message.SEND_CONTENT);
					intent.putExtra("url_type", message.URL_TYPE);
					intent.putExtra("id", message.URL_ID);
					intent.putExtra("commodityId", message.CID);
					context.startActivity(intent);
				}
				if(!messages.get(item).READ_STATUS.equals("1")){
				   appLetterReader(messages.get(item).ID,item,context);
				}
					title.setTextColor(0xffb6b6b6);
					time.setTextColor(0xffb6b6b6);
					content.setTextColor(0xffb6b6b6);
					content2.setTextColor(0xffb6b6b6);
					examine.setTextColor(0xffb6b6b6);
			} catch (Exception e) {
			}
		}
	   
   }
   
   
   
   public void 	connt(int id){
		switch (id) {
		case 1:
			// 今日头条
			Intent intent = new Intent();
			intent.setClass(context.getApplicationContext(),NewsTodayActivity.class);
			context.startActivity(intent);
			break;
		case 2:
			// 乐虎彩票
			Intent intent1 = new Intent();
			intent1.setClass(context.getApplicationContext(),LotteryActivity.class);
			context.startActivity(intent1);
			break;
		case 3:
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent2 = new Intent();
				intent2.setClass(context.getApplicationContext(),LoginActivity.class);
				context.startActivity(intent2);
			} else {
				// 预约服务
				Intent intent2 = new Intent();
				intent2.setClass(context.getApplicationContext(),YuYueShenQingActivity.class);
				context.startActivity(intent2);
			}
			break;
		case 4:
			// 物流查询
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent2 = new Intent();
				intent2.setClass(context.getApplicationContext(),LoginActivity.class);
				context.startActivity(intent2);
			} else {
				Intent intent2 = new Intent();
				intent2.setClass(context.getApplicationContext(),LogisticsQueryActivity.class);
				context.startActivity(intent2);
			}
			break;
		case 5:
			// 智慧管家
			AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
			Intent i = new Intent();
			i.setClass(context, MainActivity.class);
			context.startActivity(i);
			break;
		case 6:
			// 秀场
			Intent intent2 = new Intent();
			intent2.setClass(context.getApplicationContext(),TheShowActivity.class);
			context.startActivity(intent2);
			break;
		case 7:
			// 积分club
			Intent intent3 = new Intent();
			intent3.setClass(context.getApplicationContext(),ClubActivity.class);
			context.startActivity(intent3);
			break;
		case 8:
			// 客户中心
			Intent intent4 = new Intent();
			intent4.setClass(context.getApplicationContext(),KefuCenterActivity.class);
			context.startActivity(intent4);
			break;
		case 9:
			Intent intent5 = new Intent();
			intent5.setClass(context.getApplicationContext(),NearTheShopActivityNew.class);
			context.startActivity(intent5);
			break;
		case 10:
			// 服务卡
			if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
						.show();
				Intent intent6 = new Intent();
				intent6.setClass(context.getApplicationContext(),LoginActivity.class);
				context.startActivity(intent6);
			} else {
				Intent fuwu_intent = new Intent();
				if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
					fuwu_intent.setClass(context,
							ServiceCardActivity.class);
				} else {
					fuwu_intent.setClass(context,
							BindServiceCard.class);
				}
				context.startActivity(fuwu_intent);
			}
			break;
		default:
			break;
		}
		
	}
   
   
   private void judgeSeckill(int letterId){
		RequstClient.judgeSeckill(letterId, new CustomResponseHandler((Activity)context,false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						Toast.makeText(context, "秒杀已过期!",Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent7 = new Intent(context,SeckillActivity.class);
					context.startActivity(intent7);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
   
   
   private void Jump(int type , JSONObject jumpDate){
	   try {
		
	   switch (type) {
		case 7:
		case 8:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17: // 跳转到订单详情页
			Intent intent = new Intent();
			intent.putExtra("order_id", jumpDate.getString("id"));
			intent.setClass(context.getApplicationContext(), MyOrderDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intent);
			break;
		case 19:
		case 20:
		case 21: // 跳转到预约详情页
			Intent intentYuYue = new Intent();
			intentYuYue.putExtra("orderId", jumpDate.getString("id"));
			intentYuYue.setClass(context.getApplicationContext(), YuYueDetailActivity.class);
			intentYuYue.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intentYuYue);
			break;
		case 28:
//			Intent intentWeb = new Intent();
//			intentWeb.putExtra("htmlContent", description);
//			if (jumpDate.has("url_type") && jumpDate.has("id")) {
//				intentWeb.putExtra("url_type", jumpDate.getInt("url_type"));
//				intentWeb.putExtra("id", jumpDate.getInt("id"));
//				intentWeb.putExtra("commodityId", jumpDate.getInt("commodityId"));
//			}
//			intentWeb.putExtra("pushFlag", "1");
//			intentWeb.setClass(context.getApplicationContext(), MessageWebActivity.class);
//			intentWeb.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.getApplicationContext().startActivity(intentWeb);
			break;
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42: // 跳转到换货详情页
			Intent intentHuanhuo = new Intent();
			if(jumpDate.has("id")){
				intentHuanhuo.putExtra("returnId", jumpDate.getString("id"));
			}
			if(jumpDate.has("commodityId")){
				intentHuanhuo.putExtra("commodityId", jumpDate.getString("commodityId"));
			}
			if(jumpDate.has("status")){
				intentHuanhuo.putExtra("statu", Integer.parseInt(jumpDate.getString("status")));
			}
			if(jumpDate.has("commodityName")){
				intentHuanhuo.putExtra("goodName", jumpDate.getString("commodityName"));
			}
			intentHuanhuo.setClass(context.getApplicationContext(), AfterSaleDetailActivity.class);
			intentHuanhuo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intentHuanhuo);
			break;
		case 43:
		case 44:
			Intent intent1 = new Intent();
			intent1.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
			intent1.putExtra("username", jumpDate.getString("spotlightName"));
			intent1.setClass(context.getApplicationContext(), TheShowCommentActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intent1);
			break;
		case 51:
			switch (jumpDate.getInt("url_type")) {
			// 1.秒杀 2.闪购 3.新品预约/商品详情 4.分类聚合 5.快捷服务 6.促销活动
			case 1:
				Intent intent7 = new Intent(context, SeckillActivity.class);
				intent7.putExtra("id", jumpDate.getInt("id"));
				intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent7);
				break;
			case 2:
				Intent intent4 = new Intent(context, FlashPrefectureActivity.class);
				intent4.putExtra("id", jumpDate.getInt("id"));
				intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent4);
				break;
			case 3:
				Intent intent3 = new Intent(context, GoodsDetailActivity.class);
				intent3.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
				intent3.putExtra("subscribeId", jumpDate.getInt("id"));
				intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent3);
				break;
			case 4:
				Intent intent2 = new Intent(context, ZhuanQuActivity.class);
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
				intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent2);
				break;
			case 5:
				connt(jumpDate.getInt("id"));
				break;
			case 6:
				Intent intent6 = new Intent(context, ZhuanQuActivity.class);
				intent6.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
				intent6.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
				intent6.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
				intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent6);
				break;
			case 7:
				Intent intent5 = new Intent(context, GoodsDetailActivity.class);
				intent5.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
				intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent5);
				break;
			}

			break;
	}
	   } catch (Exception e) {
		}
	   
   }

   
	
}