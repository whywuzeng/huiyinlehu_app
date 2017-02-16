package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.OnLineMsgActivity.MsgTypeBean.TypeItem;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

public class OnLineMsgActivity extends BaseActivity {

	private final static String TAG = "OnLineMsgActivity";
	TextView left_ib, middle_title_tv, online_msg_commit_btn;
	MsgTypeBean mMsgTypeBean;
	Spinner spinner_type;
	EditText mail_msg_ed, phone_msg_ed, contact_msg_ed, content_msg_ed;
	private Toast mToast;
	List<String> data;
	// 反馈内容字数
	TextView msg_text_num;
	private ImageView spinner_jiantou;
	String mail, phone, contact, content, type_id, type_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_msg_layout);

		initView();
		initData();
	}

	private void initView() {

		mail_msg_ed = (EditText) findViewById(R.id.mail_msg_ed);
		phone_msg_ed = (EditText) findViewById(R.id.phone_msg_ed);
		contact_msg_ed = (EditText) findViewById(R.id.contact_msg_ed);
		content_msg_ed = (EditText) findViewById(R.id.content_msg_ed);
		msg_text_num = (TextView) findViewById(R.id.msg_text_num);

		spinner_jiantou = (ImageView) findViewById(R.id.spinner_jiantou);
		spinner_jiantou.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				spinner_type.performClick();
			}
		});

		content_msg_ed.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				String content = content_msg_ed.getText().toString();
				msg_text_num.setText(content.length() + "/300字");
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		left_ib = (TextView) findViewById(R.id.left_ib);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("在线留言");
		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type_id = mMsgTypeBean.guestbookTypeList.get(arg2).ID;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		online_msg_commit_btn = (TextView) findViewById(R.id.online_msg_commit_btn);
		online_msg_commit_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkInfo()) {
					makeMsg();
				}

			}
		});

	}

	private List<String> getData() {

		if (mMsgTypeBean == null) {
			return null;
		}
		data = new ArrayList<String>();
		for (TypeItem item : mMsgTypeBean.guestbookTypeList) {
			data.add(item.NAME);
			Log.i("dff", data.toString());
		}

		return data;
	}

	private void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast
					.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	private boolean checkInfo() {

		mail = mail_msg_ed.getText().toString();
		phone = phone_msg_ed.getText().toString();
		contact = contact_msg_ed.getText().toString();
		content = content_msg_ed.getText().toString();

		if (TextUtils.isEmpty(content.trim())) {
			showToast(R.string.content_is_null);
			return false;
		} else if (TextUtils.isEmpty(contact.trim())) {
			showToast(R.string.contact_is_null);
			return false;
		} else if (TextUtils.isEmpty(phone)) {
			showToast(R.string.phone_is_null);
			return false;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			showToast(R.string.phone_disenable);
			return false;
		} else if (!TextUtils.isEmpty(mail) && !StringUtils.isEmail(mail)) {
			showToast(R.string.email_disenable);
			return false;
		}
		return true;
	}

	private void makeMsg() {

		if (AppContext.getInstance().getUserId() == null) {
			return;
		}
		RequstClient.postMsg(AppContext.getInstance().getUserId(), content, contact, type_id,
				phone, mail, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postMsg:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(getBaseContext(),
									R.string.msg_success, Toast.LENGTH_SHORT)
									.show();
							finish();

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	private void initData() {

		RequstClient.getMsgTypeList(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "getMsgTypeList:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					mMsgTypeBean = new Gson().fromJson(content,
							MsgTypeBean.class);
					spinner_type.setAdapter(new ArrayAdapter<String>(
							OnLineMsgActivity.this, R.layout.spin_item,
							getData()));

				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public class MsgTypeBean {

		public ArrayList<TypeItem> guestbookTypeList = new ArrayList<TypeItem>();

		public class TypeItem {

			public String NAME;// 类型名称
			public String ID;// 类型id

		}
	}

}
