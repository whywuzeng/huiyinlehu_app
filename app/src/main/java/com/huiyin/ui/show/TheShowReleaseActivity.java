package com.huiyin.ui.show;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ShowReleaseGridViewAdapter;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.MyGridView;

public class TheShowReleaseActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_CODE_PICK_IMAGE = 0;
	private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
	private static final String TAG = "TheShowReleaseActivity";
	public EditText et_mycontent;
	public MyGridView picgrid;
	public ImageView iv_add_pic;
	public TextView left_ib;
	public TextView middle_title_tv;
	public TextView right_ib;

	public String capturePath = null;
	public String userid = "";// 用户id

	ShowReleaseGridViewAdapter adapter;
	public Context context;
	public List<String> imgPathlist;
	private Bitmap curImagePic;

	private String imagePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		userid = AppContext.getInstance().getUserId();
		setContentView(R.layout.theshow_release);
		initView();
		imgPathlist = new ArrayList<String>();

	}

	private void initView() {
		et_mycontent = (EditText) findViewById(R.id.et_mycontent);
		picgrid = (MyGridView) findViewById(R.id.grv_pic_content);
		iv_add_pic = (ImageView) findViewById(R.id.iv_add_pic);

		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("发布");
		right_ib = (TextView) findViewById(R.id.right_ib);

		right_ib.setBackgroundResource(R.drawable.icon_wancheng);

		adapter = new ShowReleaseGridViewAdapter(context);
		picgrid.setAdapter(adapter);

		iv_add_pic.setOnClickListener(this);
		left_ib.setOnClickListener(this);

		right_ib.setOnClickListener(this);
	}

	private AlertDialog dialog;
	private String out_file_path;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_pic:
			AlertDialog.Builder alert = new Builder(TheShowReleaseActivity.this);
			alert.setTitle("上传图片");
			alert.setItems(R.array.update_user_icon,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								getImageFromAlbum();
								break;
							case 1:
								getImageFromCamera();
								break;
							case 2:
								dialog.dismiss();
								break;
							}
						}
					});
			dialog = alert.create();
			dialog.show();
			break;
		case R.id.left_ib:
			finish();
			break;
		case R.id.right_ib:

			if (userid != null && !"".equals(userid)) {
				initData();
			} else {
				Toast.makeText(getApplicationContext(), "请登录！！！",
						Toast.LENGTH_LONG).show();

				// 判断登录
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);

				return;
			}
			break;

		}
	}

	private void initData() {
		/*
		 * if (str==null&&"".equals(str)) {
		 * Toast.makeText(getApplicationContext(), "你输入的内容不能为空",
		 * Toast.LENGTH_LONG).show(); return; }
		 */
		if (adapter.getImagePathList().size() <= 0) {
			Toast.makeText(getApplicationContext(), "你需要添加至少1张图片",
					Toast.LENGTH_LONG).show();
			return;
		}
		new ImageUpload(mContext, adapter.getImagePathList(),
				new UpLoadImageListener() {

					@Override
					public void UpLoadSuccess(ArrayList<String> netimageurls) {

						// 加载网络
						MyCustomResponseHandler handler = new MyCustomResponseHandler(
								mContext, true) {

							@SuppressLint("NewApi")
							@Override
							public void onRefreshData(String content) {
								Toast.makeText(getApplicationContext(),
										"感谢您的提交，我们会尽快审核！", Toast.LENGTH_LONG)
										.show();
								finish();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								super.onFailure(error, content);
								return;
							}
						};

						String imagePath = "";
						for (int i = 0; i < netimageurls.size(); i++) {

							if (i == netimageurls.size() - 1) {

								imagePath += netimageurls.get(i);
								break;
							}
							imagePath += netimageurls.get(i) + ",";
						}
						String content = et_mycontent.getText().toString()
								.trim();
						String spo_img = imagePath;
						String user_id = AppContext.getInstance().getUserId();
						RequstClient.appPublish(handler, user_id, content,
								spo_img);

					}

					@Override
					public void UpLoadFail() {
						return;
					}
				}).startLoad();

	}

	protected void getImageFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
	}

	protected void getImageFromCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent getImageByCamera = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			out_file_path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/huiyin";
			File dir = new File(out_file_path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			capturePath = ImageFolder.getTempImageName().getPath();
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(capturePath)));
			// getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(getImageByCamera,
					REQUEST_CODE_CAPTURE_CAMEIA);
		} else {
			Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 相册
		if (requestCode == REQUEST_CODE_PICK_IMAGE) {
			if (data == null || "".equals(data)) {
				// 相册没有图片
				Toast.makeText(getApplicationContext(), "没有选择图片",
						Toast.LENGTH_LONG).show();
				return;
			}
			Uri selectedImage = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			Bitmap bm;
			// bm = MediaStore.Images.Media.getBitmap(resolver, selectedImage);
			// bm = BitmapUtils.zoomBitmap(bm, 160, 160);
			bm = BitmapUtils.showimageFull(picturePath, 160, 160);
			adapter.addImagePath(picturePath);
			adapter.addPhoto(bm);

		}

		// 照相机
		else if (resultCode == RESULT_OK
				&& requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
			if (data == null || "".equals(data)) {
				File f = new File(capturePath);
				if (!f.exists()) {
					return;
				} else {

					Bitmap bm;
					// bm = MediaStore.Images.Media.getBitmap(
					// mContext.getContentResolver(),
					// Uri.parse("file://" + capturePath));
					// bm = BitmapUtils.zoomBitmap(bm, 100, 100);
					bm = BitmapUtils.showimageFull(capturePath, 160, 160);
					adapter.addImagePath(capturePath);
					adapter.addPhoto(bm);
					return;
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	// Uri转图片路径
	@SuppressWarnings("deprecation")
	public String UriToPath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		return actualimagecursor.getString(actual_image_column_index);
	}

	/**
	 * RequestParams params = new RequestParams(); params.put("username",
	 * "james"); params.put("password", "123456"); params.put("email",
	 * "my@email.com"); params.put("profile_picture", new File("pic.jpg")); //
	 * Upload a File params.put("profile_picture2", someInputStream); // Upload
	 * an InputStream params.put("profile_picture3", new
	 * ByteArrayInputStream(someBytes)); // Upload // some // bytes
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 点击空白处，隐藏软键盘
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

}