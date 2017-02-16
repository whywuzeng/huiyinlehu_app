package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.bean.GalleryAd;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.GoodsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.home.HuodongWebPageActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.ui.user.KefuCenterActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.YuYueShenQingActivity;
import com.huiyin.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GalleryViewflowAdapter extends BaseAdapter {

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Context mContext;

	private List<GalleryAd> listDatas;

	public GalleryViewflowAdapter(Context context, List<GalleryAd> listDatas) {
		mContext = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_gallery)
				.showImageForEmptyUri(R.drawable.image_default_gallery)
				.showImageOnFail(R.drawable.image_default_gallery)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(0)).build();

		imageLoader = ImageLoader.getInstance();
		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GalleryAd bean = listDatas.get(position % listDatas.size());
		imageLoader.displayImage(bean.getImageUrl(), viewHolder.imageView,
				options);

		viewHolder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GalleryAd bean = listDatas.get(position % listDatas.size());
				if (bean.getFlag() == 2) {
					Intent intent = new Intent(mContext,
							GoodsDetailActivity.class);
					intent.putExtra(GoodsDetailActivity.BUNDLE_KEY_GOODS_ID,
							bean.getRowId() + "");
					mContext.startActivity(intent);
				} else if (bean.getFlag() == 3) {
					Intent intent = new Intent(mContext, ZhuanQuActivity.class);
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID,
							bean.getRowId() + "");
					intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 3);
					mContext.startActivity(intent);
				} else if (bean.getFlag() == 4) {

					switch (bean.getRowId()) {
					case 1:
						// 今日头条
						mContext.startActivity(new Intent(mContext,
								NewsTodayActivity.class));
						break;
					case 2:
						// 乐虎彩票
						mContext.startActivity(new Intent(mContext,
								LotteryActivity.class));
						break;
					case 3:
						if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
							Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
									.show();
							mContext.startActivity(new Intent(mContext,
									LoginActivity.class));
						} else {
							// 预约服务
							mContext.startActivity(new Intent(mContext,
									YuYueShenQingActivity.class));
						}
						break;
					case 4:
						// 物流查询
						if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
							Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
									.show();
							mContext.startActivity(new Intent(mContext,
									LoginActivity.class));
						} else {
							mContext.startActivity(new Intent(mContext,
									LogisticsQueryActivity.class));
						}
						break;
					case 5:
						// 智慧管家
						AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
						Intent i = new Intent();
						i.setClass(mContext, MainActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(i);
						break;
					case 6:
						// 秀场
						mContext.startActivity(new Intent(mContext,
								TheShowActivity.class));
						break;
					case 7:
						// 积分club
						Intent intent = new Intent(mContext, ClubActivity.class);
						intent.putExtra(ClubActivity.INTENT_KEY_TITLE, "积分club");
						mContext.startActivity(intent);
						break;
					case 8:
						// 客户中心
						mContext.startActivity(new Intent(mContext,
								KefuCenterActivity.class));
						break;
					case 9:
						mContext.startActivity(new Intent(mContext,
								NearTheShopActivityNew.class));
						break;
					case 10:
						// 服务卡
						if (StringUtils.isBlank(AppContext.getInstance().getUserId())) {
							Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
									.show();
							mContext.startActivity(new Intent(mContext,
									LoginActivity.class));
						} else {
							Intent fuwu_intent = new Intent();
							if (AppContext.getInstance().getUserInfo().bdStatus == 1) {
								fuwu_intent.setClass(mContext,
										ServiceCardActivity.class);
							} else {
								fuwu_intent.setClass(mContext,
										BindServiceCard.class);
							}
							mContext.startActivity(fuwu_intent);
						}
						break;
					default:
						break;
					}

				} else if (bean.getFlag() == 1) {
					Intent intent = new Intent(mContext,
							HuodongWebPageActivity.class);
					intent.putExtra("flag", bean.getHuodongId() + "");
					mContext.startActivity(intent);
				}
				// else if (bean.getFlag() == 2) {
				// Intent intent = new Intent(mContext,
				// CategorySearchActivity.class);
				// intent.putExtra(CategorySearchActivity.BUNDLE_KEY_CATEGORY_ID,
				// bean.getRowId() + "");
				// mContext.startActivity(intent);
				// }
			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
	}

}