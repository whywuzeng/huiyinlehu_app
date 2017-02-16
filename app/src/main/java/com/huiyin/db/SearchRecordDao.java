package com.huiyin.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.huiyin.bean.ChannelItem;
import com.huiyin.bean.SearchHistroyBean;

public class SearchRecordDao extends DBDao {

	public long add(SearchHistroyBean bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getSh_name());
		return mSQLiteDatabase.insert(DBHelper.SEARCH_RECORD_TABLE, null,
				initialValues);
	}

	public void addAll(List<SearchHistroyBean> listDatas) {
		for (SearchHistroyBean bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", bean.getSh_name());
			mSQLiteDatabase
					.insert(DBHelper.SEARCH_RECORD_TABLE, null, initialValues);
		}
	}

	public void update(SearchHistroyBean bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getSh_name());
		mSQLiteDatabase.update(DBHelper.SEARCH_RECORD_TABLE, initialValues, "rowid="
				+ bean.getSh_id(), null);
	}

	public void delete(SearchHistroyBean bean) {
		mSQLiteDatabase.delete(DBHelper.SEARCH_RECORD_TABLE,
				"rowid=" + bean.getSh_id(), null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.SEARCH_RECORD_TABLE, null, null);
	}

	public ArrayList<SearchHistroyBean> fetcheAll() {
		
		ArrayList<SearchHistroyBean> listDatas = new ArrayList<SearchHistroyBean>();
		String[] query = new String[] { "rowid as _id","name"};
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.SEARCH_RECORD_TABLE,
				query, null, null, null, null, " _id desc ", null);
		SearchHistroyBean bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new SearchHistroyBean();
				bean.setSh_id(cursor.getInt(0)+"");
				bean.setSh_name(cursor.getString(1));
				listDatas.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listDatas;
	}

}
