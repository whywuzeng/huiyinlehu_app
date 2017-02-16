package com.huiyin.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.huiyin.bean.BrowseItem;

public class ScanRecordDao extends DBDao {

	public long add(BrowseItem bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("PRICE", bean.PRICE);
		initialValues.put("COMMODITY_ID", bean.COMMODITY_ID);
		initialValues.put("COMMODITY_IMAGE_PATH", bean.COMMODITY_IMAGE_PATH);
		initialValues.put("COMMODITY_NAME", bean.COMMODITY_NAME);
		return mSQLiteDatabase.insert(DBHelper.SCAN_RECORD_TABLE, null, initialValues);
	}

	public void addAll(List<BrowseItem> listDatas) {
		for (BrowseItem bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("PRICE", bean.PRICE);
			initialValues.put("COMMODITY_ID", bean.COMMODITY_ID);
			initialValues.put("COMMODITY_IMAGE_PATH", bean.COMMODITY_IMAGE_PATH);
			initialValues.put("COMMODITY_NAME", bean.COMMODITY_NAME);
			mSQLiteDatabase.insert(DBHelper.SCAN_RECORD_TABLE, null, initialValues);
		}
	}

	public void update(BrowseItem bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("PRICE", bean.PRICE);
		initialValues.put("COMMODITY_ID", bean.COMMODITY_ID);
		initialValues.put("COMMODITY_IMAGE_PATH", bean.COMMODITY_IMAGE_PATH);
		initialValues.put("COMMODITY_NAME", bean.COMMODITY_NAME);
		mSQLiteDatabase.update(DBHelper.SCAN_RECORD_TABLE, initialValues, "COMMODITY_ID=" + bean.COMMODITY_ID, null);
	}

	public void delete(BrowseItem bean) {
		mSQLiteDatabase.delete(DBHelper.SCAN_RECORD_TABLE, "COMMODITY_ID=" + bean.COMMODITY_ID, null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.SCAN_RECORD_TABLE, null, null);
	}

	public ArrayList<BrowseItem> fetcheAll() {
		ArrayList<BrowseItem> listDatas = new ArrayList<BrowseItem>();
		String[] query = new String[] { "rowid as _id", "PRICE", "COMMODITY_ID", "COMMODITY_IMAGE_PATH", "COMMODITY_NAME" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.SCAN_RECORD_TABLE, query, null, null, null, null, " _id desc ", null);
		BrowseItem bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new BrowseItem();
				bean.PRICE = cursor.getString(1);
				bean.COMMODITY_ID = cursor.getString(2);
				bean.COMMODITY_IMAGE_PATH = cursor.getString(3);
				bean.COMMODITY_NAME = cursor.getString(4);
				listDatas.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listDatas;
	}

	public BrowseItem fetcheById(String id) {
		BrowseItem bean = null;
		String[] query = new String[] { "rowid as _id", "PRICE", "COMMODITY_ID", "COMMODITY_IMAGE_PATH", "COMMODITY_NAME" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.SCAN_RECORD_TABLE, query, "COMMODITY_ID=" + id, null, null, null, " _id asc ", null);
		if (cursor.moveToFirst()) {
			bean =  new BrowseItem();
			bean.PRICE = cursor.getString(1);
			bean.COMMODITY_ID = cursor.getString(2);
			bean.COMMODITY_IMAGE_PATH = cursor.getString(3);
			bean.COMMODITY_NAME = cursor.getString(4);
		}
		cursor.close();
		return bean;
	}

}
