package com.huiyin.db;

import com.huiyin.AppContext;

import android.database.sqlite.SQLiteDatabase;

public class DBDao {

	protected final SQLiteDatabase mSQLiteDatabase;

	public DBDao() {
		mSQLiteDatabase = AppContext.getDB();
	}

}
