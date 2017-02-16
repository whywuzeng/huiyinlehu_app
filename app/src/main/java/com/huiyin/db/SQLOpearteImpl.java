package com.huiyin.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.huiyin.utils.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLOpearteImpl {
	private static String databasepath = "/data/data/%s/databases";
	private DBOpenHelper mDBOpenHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private Context mContext;

	public SQLOpearteImpl(Context context) {
		this.mContext = context;
		copyData();
		mDBOpenHelper = new DBOpenHelper(context, null);
		mSQLiteDatabase = mDBOpenHelper.getWritableDatabase();
	}

	public void CloseDB() {
		mSQLiteDatabase.close();
	}

	public int checkIdByName(String cityName) {
		int id = -1;
		if (StringUtils.isBlank(cityName))
			return id;
		cityName = cityName.replaceAll("市", "");
		Cursor c = mSQLiteDatabase.query("area_table", new String[] { "_id", "parentId", "areaName", "level" }, "areaName = ?",
				new String[] { cityName }, null, null, null);
		if (c.moveToFirst()) {
			id = c.getInt(c.getColumnIndex("_id"));
		}
		c.close();
		return id;
	}

	/**
	 * 查询所有的省 包括直辖市和自治区特区
	 * 
	 * @return
	 */
	public ArrayList<Area> checkAllProvince() {
		ArrayList<Area> provinces = new ArrayList<SQLOpearteImpl.Area>();
		Cursor c = mSQLiteDatabase.query("area_table", new String[] { "_id", "parentId", "areaName", "level" }, "level = ?",
				new String[] { "1" }, null, null, null);
		while (c.moveToNext()) {
			Area temp = new Area();
			temp.rowId = c.getInt(c.getColumnIndex("_id"));
			temp.areaName = c.getString(c.getColumnIndex("areaName"));
			temp.parentId = c.getInt(c.getColumnIndex("parentId"));
			temp.level = c.getInt(c.getColumnIndex("level"));
			provinces.add(temp);
		}
		c.close();
		return provinces;
	}

	/**
	 * 通过省Id查询下属市
	 * 
	 * @param provinceId
	 * @return
	 */
	public ArrayList<Area> checkAllCityById(int provinceId) {
		ArrayList<Area> cities = new ArrayList<SQLOpearteImpl.Area>();
		Cursor c = mSQLiteDatabase.query("area_table", new String[] { "_id", "parentId", "areaName", "level" },
				"level = ? and parentId = ?", new String[] { "2", String.valueOf(provinceId) }, null, null, null);
		while (c.moveToNext()) {
			Area temp = new Area();
			temp.rowId = c.getInt(c.getColumnIndex("_id"));
			temp.areaName = c.getString(c.getColumnIndex("areaName"));
			temp.parentId = c.getInt(c.getColumnIndex("parentId"));
			temp.level = c.getInt(c.getColumnIndex("level"));
			cities.add(temp);
		}
		c.close();
		return cities;
	}

	/**
	 * 通过城市ID查询下属区
	 * 
	 * @param cityId
	 * @return
	 */
	public ArrayList<Area> checkAllDistriceById(int cityId) {
		ArrayList<Area> districes = new ArrayList<SQLOpearteImpl.Area>();
		Cursor c = mSQLiteDatabase.query("area_table", new String[] { "_id", "parentId", "areaName", "level" },
				"level = ? and parentId = ?", new String[] { "3", String.valueOf(cityId) }, null, null, null);
		while (c.moveToNext()) {
			Area temp = new Area();
			temp.rowId = c.getInt(c.getColumnIndex("_id"));
			temp.areaName = c.getString(c.getColumnIndex("areaName"));
			temp.parentId = c.getInt(c.getColumnIndex("parentId"));
			temp.level = c.getInt(c.getColumnIndex("level"));
			districes.add(temp);
		}
		c.close();
		return districes;
	}

	public class Area {
		public int rowId;
		public String areaName;
		public int parentId;
		public int level;
	}

	public void copyData() {
		String dpath = String.format(databasepath, mContext.getApplicationInfo().packageName);
		String path = dpath + "/huiyin_db.db";
		Log.e("path", path);
		File file = new File(dpath);
		File fileData = new File(path);
		if (!fileData.exists()) {
			InputStream in = null;
			FileOutputStream out = null;
			try {
				if (!file.exists()) {
					file.mkdir();
				}
				in = mContext.getAssets().open("huiyin_db.db");
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
}
