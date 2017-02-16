package com.huiyin.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.huiyin.bean.ChannelItem;

public class LnkToolsDao extends DBDao {

	public long add(ChannelItem bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("lnkId", bean.getChannelId());
		initialValues.put("imageurl", bean.getImageUrl());
		initialValues.put("orderId", bean.getOrderId());
		return mSQLiteDatabase.insert(DBHelper.LNKTOOLS_TABLE, null,
				initialValues);
	}

	public void addAll(List<ChannelItem> listDatas) {
		for (ChannelItem bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", bean.getName());
			initialValues.put("lnkId", bean.getChannelId());
			initialValues.put("imageurl", bean.getImageUrl());
			initialValues.put("orderId", bean.getOrderId());
			mSQLiteDatabase
					.insert(DBHelper.LNKTOOLS_TABLE, null, initialValues);
		}
	}

	public void update(ChannelItem bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("lnkId", bean.getChannelId());
		initialValues.put("name", bean.getName());
		initialValues.put("imageurl", bean.getImageUrl());
		initialValues.put("orderId", bean.getOrderId());
		mSQLiteDatabase.update(DBHelper.LNKTOOLS_TABLE, initialValues, "rowid="
				+ bean.getId(), null);
	}

	public void delete(ChannelItem bean) {
		mSQLiteDatabase.delete(DBHelper.LNKTOOLS_TABLE,
				"rowid=" + bean.getId(), null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.LNKTOOLS_TABLE, null, null);
	}

	public List<ChannelItem> fetcheAll() {
		List<ChannelItem> listDatas = new ArrayList<ChannelItem>();
		String[] query = new String[] { "rowid as _id", "lnkId", "name",
				"imageurl", "orderId" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.LNKTOOLS_TABLE,
				query, null, null, null, null, " _id asc ", null);
		ChannelItem bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new ChannelItem();
				bean.setId(cursor.getInt(0));
				bean.setChannelId(cursor.getInt(1));
				bean.setName(cursor.getString(2));
				bean.setImageUrl(cursor.getString(3));
				bean.setOrderId(cursor.getInt(4));
				listDatas.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listDatas;
	}

}
