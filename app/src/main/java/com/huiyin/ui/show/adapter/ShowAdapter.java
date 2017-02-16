package com.huiyin.ui.show.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.AppShow.Info;
import com.huiyin.bean.AppShow.TouXiang;
import com.huiyin.ui.classic.HorizontialListView;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.ui.show.TheShowCommentActivity;
import com.huiyin.ui.show.TheShowlikeActivity;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.ui.show.interf.OnAttentionListener;
import com.huiyin.ui.show.view.CircularImage;
import com.huiyin.ui.show.view.ShareDialog;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.wight.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ShowAdapter extends BaseAdapter {
	private Context mContext;
	private List<Info> lists;
	private OnAddShowLikeListener mOnAddShowLikeListener;
	private OnAttentionListener mOnAttentionListener;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	// 设置监听事件
	public void setmOnAddShowLikeListener(
			OnAddShowLikeListener mOnAddShowLikeListener) {
		this.mOnAddShowLikeListener = mOnAddShowLikeListener;
	}

	public void setmOnAttentionListener(OnAttentionListener mOnAttentionListener) {
		this.mOnAttentionListener = mOnAttentionListener;
	}

	public ShowAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		this.lists = new ArrayList<Info>();

		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.drawable.default_head)
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		if (lists != null & lists.size() > 0) {
			return lists.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (lists != null & lists.size() > 0) {
			return lists.get(position);
		} else {
			return position;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int pos = position;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// 控件初始化
			convertView = View.inflate(mContext, R.layout.theshow_recommend,
					null);
			holder.headimage = (CircularImage) convertView
					.findViewById(R.id.iv_touxiang);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.guanzhu = (TextView) convertView
					.findViewById(R.id.tv_guanzhu);
			holder.share_pic = (MyGridView) convertView
					.findViewById(R.id.grv_share_pic);
			holder.theshow_fenxiang = (TextView) convertView
					.findViewById(R.id.theshow_fenxiang);
			holder.like_num = (TextView) convertView
					.findViewById(R.id.tv_like_num);
			holder.tall_num = (TextView) convertView
					.findViewById(R.id.tv_tall_num);
			holder.share_content = (TextView) convertView
					.findViewById(R.id.tv_share_content);
			holder.tv_like_info = (TextView) convertView
					.findViewById(R.id.tv_like_info);
			holder.layout_head_pic = (LinearLayout) convertView
					.findViewById(R.id.layout_head_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Info info_item = lists.get(pos);
		// 头像
		if (lists != null && lists.size() > 0) {

			imageLoader.displayImage(URLs.IMAGE_URL
					+ lists.get(position).FACE_IMAGE_PATH, holder.headimage,
					options);

			holder.name.setText(info_item.USER_NAME);
			holder.time.setText(lists.get(position).ADDTIME);

			if (AppContext.getInstance().getUserId() == null) {

				holder.guanzhu.setText("关注");

			} else {

				if (info_item.ATTENTION_ID == -1) {
					holder.guanzhu.setText("关注");
				} else {
					holder.guanzhu.setText("取消关注");
				}

			}

			holder.guanzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					// 登录判断

					if (AppContext.getInstance().getUserId() == null) {
						Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
								.show();

						Intent intent = new Intent(mContext,
								LoginActivity.class);
						intent.putExtra(LoginActivity.TAG,
								LoginActivity.hkdetail_code);
						((Activity) mContext).startActivity(intent);

						return;

					}
					if (info_item.ATTENTION_ID == -1) {

						mOnAttentionListener.addAttention(info_item.USER_ID);
					} else {

						mOnAttentionListener
								.addCancelAttention(info_item.USER_ID);
					}
				}
			});

			if (lists.get(position).LIKENUM == null) {
				holder.like_num.setText("0");
			} else {
				holder.like_num.setText(lists.get(position).LIKENUM);

			}
			// 点击喜欢
			holder.like_num.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 登录判断

					if (AppContext.getInstance().getUserId() == null) {
						Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
								.show();

						Intent intent = new Intent(mContext,
								LoginActivity.class);
						intent.putExtra(LoginActivity.TAG,
								LoginActivity.hkdetail_code);
						((Activity) mContext).startActivity(intent);

						return;

					} else {

						if (mOnAddShowLikeListener != null) {
							mOnAddShowLikeListener.addShowLike(lists.get(pos).ID);
						}

					}
				}
			});
			if (lists.get(position).APPRAISENUM == null) {
				holder.tall_num.setText("0");
			} else {

				holder.tall_num.setText(lists.get(position).APPRAISENUM);
			}
			// 点击评论
			holder.tall_num.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 登录判断
					if (AppContext.getInstance().getUserId() == null) {
						Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
								.show();

						Intent intent = new Intent(mContext,
								LoginActivity.class);
						intent.putExtra(LoginActivity.TAG,
								LoginActivity.hkdetail_code);
						((Activity) mContext).startActivityForResult(intent,
								100);

						return;
					} else {
						Intent intent = new Intent(mContext,
								TheShowCommentActivity.class);
						intent.putExtra("id", lists.get(pos).ID);
						intent.putExtra("username", lists.get(pos).USER_NAME);
						mContext.startActivity(intent);
					}
				}
			});
			if (lists.get(position).CONTENT != null) {
				holder.share_content.setText(lists.get(position).CONTENT + "");
				holder.share_content.setVisibility(View.VISIBLE);
			} else {
				holder.share_content.setVisibility(View.GONE);
			}
			// 晒图分享的图片 //图片点击方法
			final ShowSharePicGridViewAdapter spadapter = new ShowSharePicGridViewAdapter(
					mContext);
			if (lists.get(position).SPO_IMG != null) {
				spadapter.addItem(getIMGList(lists.get(position).SPO_IMG));
				if (getIMGList(lists.get(position).SPO_IMG).size() == 2) {
					holder.share_pic.setNumColumns(2);
				} else if (getIMGList(lists.get(position).SPO_IMG).size() == 4) {
					holder.share_pic.setNumColumns(2);
				} else if (getIMGList(lists.get(position).SPO_IMG).size() == 1) {
					holder.share_pic.setNumColumns(2);
				} else {
					holder.share_pic.setNumColumns(3);
				}
				holder.share_pic.setAdapter(spadapter);
				holder.share_pic
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent = new Intent();
								intent.setClass(mContext,
										PhotoViewActivity.class);
								intent.putStringArrayListExtra(
										PhotoViewActivity.INTENT_KEY_PHOTO,
										spadapter.getListDatas());
								intent.putExtra(
										PhotoViewActivity.INTENT_KEY_POSITION,
										position);
								mContext.startActivity(intent);
							}
						});
			} else {
				holder.share_pic.setAdapter(spadapter);
			}

		}

		// 喜欢头像列表
		if (lists.get(position).touxiangList != null
				&& lists.get(position).touxiangList.size() > 0) {

			ArrayList<TouXiang> headList = lists.get(position).touxiangList;
			holder.layout_head_pic.removeAllViews();
			for (int i = 0; i < headList.size() && i < 5; i++) {
				View view;
				view = LayoutInflater.from(mContext).inflate(
						R.layout.theshow_gridview_item, null);
				CircularImage headListImge = (CircularImage) view
						.findViewById(R.id.theshow_pic_item);
				imageLoader.displayImage(URLs.IMAGE_URL
						+ headList.get(i).FACE_IMAGE_PATH, headListImge,
						options);
				holder.layout_head_pic.addView(view);
			}
			if (headList.size() > 5) {
				View view;
				view = LayoutInflater.from(mContext).inflate(
						R.layout.theshow_gridview_item, null);
				CircularImage headListImge = (CircularImage) view
						.findViewById(R.id.theshow_pic_item);
				headListImge.setImageResource(R.drawable.yuandian);
				headListImge.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								TheShowlikeActivity.class);
						intent.putExtra("userid",
								String.valueOf(lists.get(pos).ID));
						mContext.startActivity(intent);
					}
				});
				holder.layout_head_pic.addView(view);
			}

			holder.tv_like_info.setText("谁喜欢过我:");
			holder.tv_like_info.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext,
							TheShowlikeActivity.class);
					intent.putExtra("userid", String.valueOf(lists.get(pos).ID));
					mContext.startActivity(intent);
				}
			});
		} else {
			holder.tv_like_info.setText("暂时无人喜欢");
			holder.layout_head_pic.removeAllViews();
		}

		// 分享
		holder.theshow_fenxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShareDialog dialog = new ShareDialog(mContext);

				dialog.show();

			}
		});
		return convertView;

	}

	class ViewHolder {
		CircularImage headimage;
		TextView name;
		TextView time;
		TextView guanzhu;
		TextView like_num;
		TextView tall_num;
		TextView share_content;
		TextView tv_like_info;
		MyGridView share_pic;
		TextView theshow_fenxiang;
		LinearLayout layout_head_pic;
	}

	public void addItem(ArrayList<Info> temp) {
		if (temp == null || temp.size() == 0) {
			return;
		}
		if (temp instanceof ArrayList) {
			lists.clear();
			lists.addAll(temp);
		}
		notifyDataSetChanged();
	}

	public void addMoreItem(ArrayList<Info> temp) {

		if (temp == null || temp.size() == 0) {
			return;
		}
		if (temp instanceof ArrayList) {
			lists.addAll(temp);
			notifyDataSetChanged();
		}
	}

	public void deleteItem() {
		lists.clear();
		notifyDataSetChanged();
	}

	public ArrayList<String> getIMGList(String str) {
		ArrayList<String> slist = new ArrayList<String>();
		slist.clear();
		String[] s = str.split(",");
		for (int i = 0; i < s.length; i++) {
			slist.add(s[i]);
		}
		return slist;
	}

}
