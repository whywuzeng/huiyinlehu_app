package com.huiyin.ui.show;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.AppShowAddAppraise;
import com.huiyin.bean.AppShowAddAttention;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.bean.AppShowAppraise;
import com.huiyin.bean.AppShowCancelAttention;
import com.huiyin.bean.AppShowAppraise.Info;
import com.huiyin.bean.AppShowAppraise.Item;
import com.huiyin.bean.AppShowAppraise.TouXiang;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.ui.show.adapter.ShowSharePicGridViewAdapter;
import com.huiyin.ui.show.view.CircularImage;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.MyMessageActivity;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.MyGridView;
import com.huiyin.wight.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TheShowCommentActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView left_ib, right_ib, name;

	private MyListView mlistview;

	public CircularImage headimage;
	public TextView time;
	public TextView guanzhu;
	public TextView like_num;
	public TextView tall_num;
	public TextView share_content;
	public TextView tv_like_info;
	public MyGridView share_pic;
	public LinearLayout layout_head_pic;
	public EditText et_send;
	public Button bt_send;

	public String spotlight_id;
	public String user_id;
	public int reply_id = -1;
	public String reply_name = "";
	public String content = "";

	AppShowAppraise data;
	AppShowAddAppraise data1;
	AppShowAddLike data2;
	AppShowAddAttention data3;
	AppShowCancelAttention data4;
	public ArrayList<Info> lists = new ArrayList<Info>();
	public ArrayList<Item> list = new ArrayList<Item>();

	public boolean isclick = false;

	public int id;
	public String UserName;

	private MyListAdapter adapter;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	String pushFlag; // 消息标识

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.theshow_comment);
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id");
		UserName = intent.getExtras().getString("username");
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0"
				: getIntent().getStringExtra("pushFlag");
		user_id = AppContext.getInstance().getUserId();
		left_ib = (TextView) findViewById(R.id.left_ib);
		right_ib = (TextView) findViewById(R.id.right_ib);
		right_ib.setBackgroundResource(R.drawable.icon_fabu_xinzeng);
		left_ib.setOnClickListener(this);
		right_ib.setOnClickListener(this);

		TextView tv_title = (TextView) findViewById(R.id.middle_title_tv);
		tv_title.setText(UserName + "的秀场");

		headimage = (CircularImage) findViewById(R.id.iv_touxiang);
		name = (TextView) findViewById(R.id.tv_name);
		time = (TextView) findViewById(R.id.tv_time);
		guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
		share_pic = (MyGridView) findViewById(R.id.grv_share_pic);
		like_num = (TextView) findViewById(R.id.tv_like_num);
		tall_num = (TextView) findViewById(R.id.tv_tall_num);
		share_content = (TextView) findViewById(R.id.tv_share_content);
		tv_like_info = (TextView) findViewById(R.id.tv_like_info);
		layout_head_pic = (LinearLayout) findViewById(R.id.layout_head_pic);

		et_send = (EditText) findViewById(R.id.et_send);
		bt_send = (Button) findViewById(R.id.bt_send);
		share_content.setOnClickListener(this);
		bt_send.setOnClickListener(this);
		like_num.setOnClickListener(this);

		if (isclick) {
			et_send.setHint("回复@" + reply_name);
		} else {
			et_send.setHint("");
		}

		mlistview = (MyListView) findViewById(R.id.mylist);
		adapter = new MyListAdapter(this);
		mlistview.setAdapter(adapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// isclick=true;
				if (list != null && list.size() > 0) {
					et_send.setHint("回复@" + list.get(position).USER_NAME);
					setREPLYID(list.get(position).USER_ID);
					setReplyName(list.get(position).USER_NAME);
				}
			}
		});
	}

	protected void setREPLYID(int reply_ID2) {
		reply_id = reply_ID2;

	}

	protected void setReplyName(String replyname) {
		reply_name = replyname;

	}

	protected void setSpotlightID(String spotlightid) {
		spotlight_id = spotlightid;

	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {

		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(R.drawable.default_head)
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data = AppShowAppraise.explainJson(content, mContext);

				if (data.type == 1) {
					if (data.spotlight != null && data.spotlight.size() > 0) {
						lists.clear();
						lists.add(data.spotlight.get(0));
						spotlight_id = data.spotlight.get(0).ID;
					}
					UpdateUI(lists);
					setSpotlightID(data.spotlight.get(0).ID);
					if (data.appraise != null && data.appraise.size() > 0) {
						adapter.deleteItem();
						list.clear();
						adapter.addItem(data.appraise);
						list.addAll(data.appraise);
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		RequstClient.appAppraise(handler, id + "");

	}

	private void sendData() {
		// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data1 = AppShowAddAppraise.explainJson(content, mContext);

				if (data1.type == 1) {
					initData();
				} else {
					Toast.makeText(mContext, "发表失败", Toast.LENGTH_SHORT).show();
				}

				et_send.setText("");
				reply_name = "";
				content = "";

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (reply_id == -1 && "".equals(reply_name) && !"".equals(spotlight_id)) {// 评论
			RequstClient.appAddAppraise(handler, spotlight_id, user_id, "",
					content);
		}
		if (reply_id != -1 && !"".equals(reply_name)
				&& !"".equals(spotlight_id)) {// 回复
			RequstClient.appAddAppraise(handler, spotlight_id, user_id,
					reply_id + "", content);
		}

	}

	private void UpdateUI(final ArrayList<Info> lists) {

		if (lists != null && lists.size() > 0) {
			imageLoader.displayImage(URLs.IMAGE_URL
					+ lists.get(0).FACE_IMAGE_PATH, headimage, options);
			if (lists.get(0).USER_NAME != null) {

				name.setText(lists.get(0).USER_NAME);
			} else {
				name.setText("");
			}
			if (lists.get(0).ADDTIME != null) {

				time.setText(lists.get(0).ADDTIME);
			} else {
				time.setText("");
			}
			
			if (lists.get(0).ATTENTION_ID.equals("-1")) {
				guanzhu.setText("关注");
			} else {
				guanzhu.setText("取消关注");
			}
			
			guanzhu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (user_id != null && !"".equals(user_id)) {
						if (lists.get(0).ATTENTION_ID.equals("-1")) {
							AttentionData(lists.get(0).USER_ID);
						} else {
							CancelAttentionData(lists.get(0).USER_ID);
						}
					} else {
						Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
						startActivity(intent);
					}
				}
			});

			if (lists.get(0).LIKENUM != null) {
				like_num.setText(lists.get(0).LIKENUM);
			} else {

				like_num.setText("0");
			}
			if (lists.get(0).APPRAISENUM == null) {
				tall_num.setText("0");
			} else {

				tall_num.setText(String.valueOf(Integer.parseInt(lists.get(0).APPRAISENUM)));
			}
			if (lists.get(0).CONTENT != null) {
				share_content.setText(lists.get(0).CONTENT);
				share_content.setVisibility(View.VISIBLE);
			} else {
				share_content.setVisibility(View.GONE);
			}
			final ShowSharePicGridViewAdapter spadapter = new ShowSharePicGridViewAdapter(
					mContext);
			if (lists.get(0).SPO_IMG != null) {
				spadapter.addItem(getIMGList(lists.get(0).SPO_IMG));
				if (getIMGList(lists.get(0).SPO_IMG).size() == 2
						|| getIMGList(lists.get(0).SPO_IMG).size() == 4) {
					share_pic.setNumColumns(2);
				} else if (getIMGList(lists.get(0).SPO_IMG).size() == 1) {
					share_pic.setNumColumns(2);
				} else {
					share_pic.setNumColumns(3);
				}

				share_pic.setAdapter(spadapter);
				share_pic.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						intent.setClass(mContext, PhotoViewActivity.class);
						intent.putStringArrayListExtra(
								PhotoViewActivity.INTENT_KEY_PHOTO,
								spadapter.getListDatas());
						intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION,
								position);
						mContext.startActivity(intent);
					}
				});
			}
		}

		// 头像列表
		if (lists.get(0).touxiangList != null
				&& lists.get(0).touxiangList.size() > 0) {
			ArrayList<TouXiang> headList = lists.get(0).touxiangList;
			layout_head_pic.removeAllViews();
			for (int i = 0; i < headList.size() && i < 5; i++) {
				View view;
				view = LayoutInflater.from(mContext).inflate(
						R.layout.theshow_gridview_item, null);
				CircularImage headListImge = (CircularImage) view
						.findViewById(R.id.theshow_pic_item);
				imageLoader.displayImage(URLs.IMAGE_URL
						+ headList.get(i).FACE_IMAGE_PATH, headListImge,
						options);
				layout_head_pic.addView(view);
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
								String.valueOf(lists.get(0).ID));
						mContext.startActivity(intent);
					}
				});
				layout_head_pic.addView(view);
			}

			tv_like_info.setText("谁喜欢过我:");
			tv_like_info.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext,
							TheShowlikeActivity.class);
					intent.putExtra("userid", String.valueOf(lists.get(0).ID));
					mContext.startActivity(intent);
				}
			});
		} else {
			tv_like_info.setText("暂时无人喜欢");
		}

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

	public class MyListAdapter extends BaseAdapter {
		private Context mContext;
		private List<Item> lists;

		public MyListAdapter(Context mContext) {
			super();
			this.mContext = mContext;
			this.lists = new ArrayList<Item>();
		}

		@Override
		public int getCount() {
			if (lists != null && lists.size() > 0) {
				return lists.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			if (lists != null && lists.size() > 0) {
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

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.theshow_comment_item, null);
				holder.headimage = (CircularImage) convertView
						.findViewById(R.id.comment_touxiang);
				holder.name = (TextView) convertView
						.findViewById(R.id.tv_list_name);
				holder.time = (TextView) convertView
						.findViewById(R.id.tv_comment_time);
				holder.content_info = (TextView) convertView
						.findViewById(R.id.tv_content_info);
				holder.content = (TextView) convertView
						.findViewById(R.id.tv_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.headimage.setLayoutParams(new RelativeLayout.LayoutParams(
					85, 85));
			holder.headimage.setPadding(4, 4, 4, 4);// 设置间距
			holder.headimage.setAdjustViewBounds(false);// 设置边界对齐
			holder.headimage
					.setScaleType(CircularImage.ScaleType.CENTER_INSIDE);
			imageLoader.displayImage(URLs.IMAGE_URL
					+ lists.get(position).FACE_IMAGE_PATH, holder.headimage,
					options);
			if (lists.get(position).USER_NAME == null) {
				holder.name.setText("");

			} else {

				holder.name.setText(lists.get(position).USER_NAME);
			}
			if (lists.get(position).DATETIME != null) {

				holder.time.setText(StringUtils.friendly_time(lists
						.get(position).DATETIME));
			} else {
				holder.time.setText("");
			}

			if (lists.get(position).REPLY_NAME != null) {
				holder.content_info.setText("回复@"
						+ lists.get(position).REPLY_NAME + ": ");
			} else {
				holder.content_info.setText("");
			}

			holder.content.setText(lists.get(position).CONTENT == null ? ""
					: lists.get(position).CONTENT);

			return convertView;
		}

		class ViewHolder {
			CircularImage headimage;
			TextView name;
			TextView time;
			TextView content_info;
			TextView content;
		}

		public void addItem(ArrayList<Item> temp) {
			if (temp == null) {
				return;
			}
			if (temp instanceof ArrayList) {
				lists.addAll(temp);
			}
			notifyDataSetChanged();
		}

		public void deleteItem() {
			lists.clear();
			notifyDataSetChanged();
		}

	}

	private void addLikeData(String spotlightId) {
		// 喜欢 网络请求
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			public void onRefreshData(String content) {

				data2 = AppShowAddLike.explainJson(content, mContext);

				if (data2.type == 1) {

					Toast.makeText(mContext, "谢谢你喜欢我", Toast.LENGTH_SHORT)
							.show();
					initData();

				}
				if (data2.type == 2) {
					Toast.makeText(mContext, data2.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {

			// Toast.makeText(mContext, "请登录！！！", 0).show();
		} else {

			RequstClient.appShowLike(handler, user_id, spotlightId);
		}
	}
	
	private void AttentionData(int spotlightUserId) {
		// 关注网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data3 = AppShowAddAttention.explainJson(content, mContext);

				if (data3.type == 1) {

					Toast.makeText(mContext, "谢谢你关注我", Toast.LENGTH_SHORT)
							.show();
					initData();
				} else {
					Toast.makeText(mContext, data3.msg, Toast.LENGTH_SHORT)
							.show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {
			// Toast.makeText(mContext, "请登录！！！", 0).show();

		} else if (user_id.equals(spotlightUserId+"")) {

			Toast.makeText(mContext, "不能关注自己！", Toast.LENGTH_SHORT).show();
		} else {
			RequstClient.appAttention(handler, user_id, spotlightUserId+"");
		}
	}
	
	private void CancelAttentionData(int spotlightUserId) {
		// 取消关注网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data4 = AppShowCancelAttention.explainJson(content, mContext);

				if (data4.type == 1) {

					Toast.makeText(mContext, "亲,希望你能够再次关注我", 0).show();
					initData();
				} else {

					Toast.makeText(mContext, data4.msg, 0).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {
			// Toast.makeText(mContext, "请登录！！！", 0).show();

		} else {
			RequstClient.appCancelAttention(handler, user_id, spotlightUserId
					+ "");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_ib:
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
			break;
		case R.id.right_ib:
			// 发布
			startActivity(new Intent(mContext, TheShowReleaseActivity.class));
			break;
		case R.id.tv_share_content:
			// 评论
			et_send.setText("");
			et_send.setHint("");
			reply_name = "";
			reply_id = -1;
			break;
		case R.id.tv_like_num:
			if (user_id != null && !"".equals(user_id)) {

				addLikeData(lists.get(0).ID);
			} else {
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
				return;
			}
			break;
		case R.id.bt_send:
			// 提交
			String str = et_send.getText().toString().trim();
			if (user_id != null && !"".equals(user_id)) {

				if (str != null && !"".equals(str)) {
					content = str;
					sendData();

				} else {
					Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT)
							.show();
				}
				isclick = false;
				et_send.setText("");
				et_send.setHint("");
				reply_name = "";
				content = "";
			} else {
				Toast.makeText(mContext, "请登陆！！！", Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
