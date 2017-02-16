package com.huiyin.ui.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.AppContext.LocationCallBack;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AddressItem;
import com.huiyin.ui.shoppingcar.WriteOrderActivity;
import com.huiyin.utils.DWSqliteUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

public class AddressModifyActivity extends BaseActivity {

	public static String TAG = "AddressModifyActivity";

	public static String ADD = "1", MODIFY = "2", UNLOGIN = "3", INIT = "4";
	TextView left_rb, middle_title_tv;
	private Spinner province, city, country;
	private static String databasepath = "/data/data/%s/databases";
	private Cursor cursorProvince = null;
	private Cursor cursorCity = null;
	private Cursor cursorArea = null;
	// 省、市、县适配器
	private SimpleCursorAdapter adapterProvince = null;
	private SimpleCursorAdapter adapterCity = null;
	private SimpleCursorAdapter adapterArea = null;

	public SQLiteDatabase mDatabase;
	// 省市县id
	private String provinceId, cityId, areaId;

	private String provinceName, cityName, areaName;
	// 收货人、手机号码、邮编地址、详细地址
	EditText address_receiver, address_modify_phone, address_modify_code,
			address_modify_addr;
	TextView address_modify_sure, address_modify_cancel;
	// 收货人、手机号码、邮编地址、详细地址、地址id
	String receiver, phone, code, detail_addr, addressId;
	private Toast mToast;
	private String which;
	// 修改地址时从上页传过来的地址信息
	private AddressItem addressItem;

	private TextView address_detail_tv;

	private ImageView province_img, city_img, area_img;

	// 定位相关
	private LocationClient mLocationClient;
	private String cityNameByLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_modify_add);

		initView();
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

		receiver = address_receiver.getText().toString();
		phone = address_modify_phone.getText().toString();
		code = address_modify_code.getText().toString();
		detail_addr = address_modify_addr.getText().toString();

		if (StringUtils.isBlank(receiver)) {
			showToast(R.string.receiver_is_null);
			return false;
		} else if (StringUtils.isBlank(phone)) {
			showToast(R.string.phone_is_null);
			return false;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			showToast(R.string.yuyue_phone_number_regx);
			return false;
		} else if (StringUtils.isBlank(detail_addr)) {
			showToast(R.string.addr_is_null);
			return false;
		}

		return true;
	}

	/**
	 * 修改地址
	 * 
	 */

	private void doModifyAddr() {

		String levelAddr = provinceName + cityName + areaName;
		if (AppContext.getInstance().getUserId() == null) {
			return;
		}

		RequstClient.postModifyAddress(AppContext.getInstance().getUserId(), cityId, phone,
				detail_addr, receiver, code, areaId, provinceId, addressId, "",
				levelAddr, new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postModifyAddress:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							Intent i;
							if (which.equals(INIT)) {

								i = new Intent(AddressModifyActivity.this,
										WriteOrderActivity.class);
								AddressItem item = new AddressItem();
								item.CITY_ID = cityId;
								item.ADDRESSID = obj.getString("returnId");
								item.CONSIGNEE_PHONE = phone;
								item.ADDRESS = detail_addr;
								item.CONSIGNEE_NAME = receiver;
								item.POSTAL_CODE = code;
								item.AREA_ID = areaId;
								item.PROVINCE_ID = provinceId;
								item.LEVELADDR = provinceName + cityName
										+ areaName;
								i.putExtra("addr", item);
							} else {

								i = new Intent(AddressModifyActivity.this,
										AddressManagementActivity.class);
							}
							setResult(RESULT_OK, i);
							if (which.equals(MODIFY)) {
								Toast.makeText(getBaseContext(), "修改成功！",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getBaseContext(), "增加成功！",
										Toast.LENGTH_SHORT).show();
							}

							AddressModifyActivity.this.finish();

						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
	}

	private void initView() {

		which = getIntent().getStringExtra(TAG);

		province = (Spinner) findViewById(R.id.address_modify_province);
		city = (Spinner) findViewById(R.id.address_modify_city);
		country = (Spinner) findViewById(R.id.address_modify_country);

		province_img = (ImageView) findViewById(R.id.province_img);
		city_img = (ImageView) findViewById(R.id.city_img);
		area_img = (ImageView) findViewById(R.id.area_img);

		province_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				province.performClick();
			}
		});

		city_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				city.performClick();
			}
		});

		area_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				country.performClick();
			}
		});

		left_rb = (TextView) findViewById(R.id.ab_back);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.ab_title);

		if (which.equals(MODIFY)) {
			addressId = getIntent().getStringExtra("addressId");
			middle_title_tv.setText("地址修改");
			addressItem = (AddressItem) getIntent().getSerializableExtra(
					"addressItem");

		} else if (which.equals(UNLOGIN)) {
			middle_title_tv.setText("地址管理");
			addressId = "";
		} else {
			middle_title_tv.setText("添加地址");
			addressId = "";
		}

		if (AppContext.isFirstCopy) {
			copyData();// 拷贝assets目录下的数据库到databases目录下
		}

		DWSqliteUtils instance = DWSqliteUtils.getInstance();
		mDatabase = instance.mDatabase;

		cursorProvince = mDatabase.rawQuery(
				"SELECT _id,parentId,areaName,level from area_table"
						+ " where level = ?", new String[] { "1" });

		adapterProvince = new SimpleCursorAdapter(this, R.layout.spinner_item,
				cursorProvince, new String[] { "areaName" },
				new int[] { R.id.text_name });

		province.setAdapter(adapterProvince);
		province.setOnItemSelectedListener(new ItemProvinceListenerImpl());

		address_receiver = (EditText) findViewById(R.id.address_receiver);
		address_modify_phone = (EditText) findViewById(R.id.address_modify_phone);
		address_modify_code = (EditText) findViewById(R.id.address_modify_code);
		address_modify_addr = (EditText) findViewById(R.id.address_modify_addr);

		address_detail_tv = (TextView) findViewById(R.id.address_detail_tv);

		address_modify_sure = (TextView) findViewById(R.id.address_modify_sure);
		address_modify_cancel = (TextView) findViewById(R.id.address_modify_cancel);
		address_modify_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkInfo()) {
					if (AppContext.getInstance().getUserId() == null) {

						Intent i = new Intent();
						i.setClass(AddressModifyActivity.this,
								WriteOrderActivity.class);
						i.putExtra("receiver", receiver);
						i.putExtra("phone", phone);
						i.putExtra("code", code);
						i.putExtra("detail_addr", detail_addr);
						i.putExtra("address", provinceName + cityName
								+ areaName + detail_addr);
						i.putExtra("provinceId", provinceId);
						i.putExtra("cityId", cityId);
						i.putExtra("areaId", areaId);
						setResult(RESULT_OK, i);
						finish();

					} else {
						doModifyAddr();
					}

				}
			}
		});

		address_modify_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		if (which.equals(MODIFY)) {

			address_receiver.setText(addressItem.CONSIGNEE_NAME);
			CharSequence text = address_receiver.getText();
			if (text instanceof Spannable) {
				Spannable spanText = (Spannable) text;
				Selection.setSelection(spanText, text.length());
			}
			address_modify_phone.setText(addressItem.CONSIGNEE_PHONE);
			address_modify_code.setText(addressItem.POSTAL_CODE);
			address_modify_addr.setText(addressItem.ADDRESS);

			provinceId = addressItem.PROVINCE_ID;
			cityId = addressItem.CITY_ID;
			areaId = addressItem.AREA_ID;
			addressId = addressItem.ADDRESSID;

			cursorProvince.moveToFirst();

			for (int i = 0; i < cursorProvince.getCount(); i++) {
				int nameColumnIndex = cursorProvince.getColumnIndex("_id");
				int id = cursorProvince.getInt(nameColumnIndex);

				if ((id + "").equals(provinceId)) {
					province.setSelection(cursorProvince.getPosition());
					break;
				} else {
					cursorProvince.moveToNext();
				}
			}

		} else {
			// province.setSelection(0);
			cityNameByLocation = AppContext.getInstance().cityName;
			if (StringUtils.isBlank(cityNameByLocation)) {
				StartLoacation();
			} else {
				setTheProvince();
			}
		}
	}

	private class ItemProvinceListenerImpl implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			cursorProvince.moveToPosition(arg2);
			int nameColumnIndex = cursorProvince.getColumnIndex("_id");
			int id = cursorProvince.getInt(nameColumnIndex);
			provinceId = id + "";
			// 省份名字
			int index = cursorProvince.getColumnIndex("areaName");
			provinceName = cursorProvince.getString(index);

			cursorCity = mDatabase.rawQuery(
					"SELECT _id,parentId,areaName,level from area_table"
							+ " where parentId = ?", new String[] { id + "" });

			adapterCity = new SimpleCursorAdapter(AddressModifyActivity.this,
					R.layout.spinner_item, cursorCity,
					new String[] { "areaName" }, new int[] { R.id.text_name });

			city.setAdapter(adapterCity);
			city.setOnItemSelectedListener(new ItemCityListenerImpl());

			if (which.equals(MODIFY)) {

				cursorCity.moveToFirst();
				for (int i = 0; i < cursorCity.getCount(); i++) {
					int ColumnIndex = cursorCity.getColumnIndex("_id");
					int mId = cursorCity.getInt(ColumnIndex);

					if ((mId + "").equals(cityId)) {
						city.setSelection(cursorCity.getPosition());
						break;
					} else {
						cursorCity.moveToNext();
					}
				}

			} else {
				cursorCity.moveToFirst();
				for (int i = 0; i < cursorCity.getCount(); i++) {
					int ColumnIndex = cursorCity.getColumnIndex("areaName");
					String temp = cursorCity.getString(ColumnIndex);
					if (temp.equals(cityNameByLocation)) {
						city.setSelection(cursorCity.getPosition());
						break;
					} else {
						cursorCity.moveToNext();
					}
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private class ItemCityListenerImpl implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			cursorCity.moveToPosition(arg2);

			int nameColumnIndex = cursorCity.getColumnIndex("_id");
			int id = cursorCity.getInt(nameColumnIndex);
			cityId = id + "";

			// 城市名字
			int index = cursorCity.getColumnIndex("areaName");
			cityName = cursorCity.getString(index);

			cursorArea = mDatabase.rawQuery(
					"SELECT _id,parentId,areaName,level from area_table "
							+ "where parentId = ?", new String[] { id + "" });

			adapterArea = new SimpleCursorAdapter(AddressModifyActivity.this,
					R.layout.spinner_item, cursorArea,
					new String[] { "areaName" }, new int[] { R.id.text_name });

			country.setAdapter(adapterArea);
			country.setOnItemSelectedListener(new ItemAreaListenerImpl());

			if (which.equals(MODIFY)) {

				cursorArea.moveToFirst();

				for (int i = 0; i < cursorArea.getCount(); i++) {
					int ColumnIndex = cursorArea.getColumnIndex("_id");
					int mId = cursorArea.getInt(ColumnIndex);

					if ((mId + "").equals(areaId)) {
						country.setSelection(cursorArea.getPosition());
						break;
					} else {
						cursorArea.moveToNext();
					}
				}

			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private class ItemAreaListenerImpl implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			cursorArea.moveToPosition(arg2);

			int nameColumnIndex = cursorArea.getColumnIndex("_id");
			int id = cursorArea.getInt(nameColumnIndex);
			// 地区名字
			int index = cursorArea.getColumnIndex("areaName");
			areaName = cursorArea.getString(index);

			areaId = id + "";

			address_detail_tv.setText(String.format(
					getString(R.string.addr_detail_info), provinceName
							+ cityName + areaName));

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cursorProvince != null) {
			cursorProvince.close();
		} else if (cursorCity != null) {
			cursorCity.close();
		} else if (cursorArea != null) {
			cursorArea.close();
		} else if (mDatabase != null) {
			mDatabase.close();
		}

	}

	public void copyData() {

		InputStream in = null;
		FileOutputStream out = null;
		String dpath = String.format(databasepath,
				this.getApplicationInfo().packageName);

		String path = dpath + "/huiyin_db.db";
		Log.e("path", path);
		File file = new File(dpath);
		File fileData = new File(path);
		if (!fileData.exists()) {
			try {
				if (!file.exists()) {
					file.mkdir();
				}
				in = this.getAssets().open("huiyin_db.db");
				out = new FileOutputStream(fileData);
				int length = -1;
				byte[] buf = new byte[1024];
				while ((length = in.read(buf)) != -1) {
					out.write(buf, 0, length);
				}
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				AppContext.isFirstCopy = false;
				if (in != null) {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	private void StartLoacation() {
		AppContext.getInstance().initLocationClient();
		if (mLocationClient == null)
			mLocationClient = AppContext.getInstance().mLocationClient;
		if (mLocationClient.isStarted()) {
			return;
		}
		mLocationClient.start();
		AppContext.getInstance().setLocationCallBack(new LocationCallBack() {

			@Override
			public void getPoistion(BDLocation location) {
				Toast.makeText(mContext, "定位成功", Toast.LENGTH_SHORT).show();
				cityNameByLocation = location.getCity();
				if (!StringUtils.isBlank(cityNameByLocation)) {
					mLocationClient.stop();
					setTheProvince();
				}
			}
		});
	}

	private void setTheProvince() {
		cityNameByLocation = cityNameByLocation.replaceAll("市", "");
		Cursor checkTheCity = mDatabase.query("area_table", null,
				"areaName like ?", new String[] { cityNameByLocation }, null,
				null, null);
		while (checkTheCity.moveToNext()) {
			cursorProvince.moveToFirst();
			for (int i = 0; i < cursorProvince.getCount(); i++) {
				int nameColumnIndex = cursorProvince.getColumnIndex("_id");
				int id = cursorProvince.getInt(nameColumnIndex);
				if ((id + "").equals(checkTheCity.getString(checkTheCity
						.getColumnIndex("parentId")))) {
					province.setSelection(cursorProvince.getPosition());
					break;
				} else {
					cursorProvince.moveToNext();
				}
			}
		}
		checkTheCity.close();
	}
}
