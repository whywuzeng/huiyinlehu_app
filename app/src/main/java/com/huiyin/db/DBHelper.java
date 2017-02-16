package com.huiyin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 5;
	public static final String DB_NAME = "huiyin.db";
	private static DBHelper mDBHelper;

	public static final String LNKTOOLS_TABLE = "lnktools";// 快捷方式
	
	public static final String SCAN_RECORD_TABLE = "scan_record";// 浏览记录
	
	public static final String SEARCH_RECORD_TABLE = "search_record";// 搜索记录
	
	public static final String SYSMESSAGE_TABLE = "sysmessage";// 系统消息

	private static final String CREATE_LNKTOOLS_TABLE = "CREATE TABLE IF NOT EXISTS " + LNKTOOLS_TABLE
			+ "( name text, imageurl text, orderId integer, lnkId intger )";
	
	private static final String CREATE_SCAN_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + SCAN_RECORD_TABLE
			+ "( PRICE text, COMMODITY_ID text, COMMODITY_IMAGE_PATH text, COMMODITY_NAME text )";
	
	private static final String CREATE_SYSMESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + SYSMESSAGE_TABLE
			+ "(userid text,letterId text)";

	
	private static final String CREATE_SEARCH_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + SEARCH_RECORD_TABLE
					+ "( name text )";
	
	public static synchronized DBHelper getInstance(Context context) {
		if (mDBHelper == null) {
			mDBHelper = new DBHelper(context);
		}
		return mDBHelper;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LNKTOOLS_TABLE);
		db.execSQL(CREATE_SCAN_RECORD_TABLE);
		db.execSQL(CREATE_SEARCH_RECORD_TABLE);
		db.execSQL(CREATE_SYSMESSAGE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + LNKTOOLS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SCAN_RECORD_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SEARCH_RECORD_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SYSMESSAGE_TABLE);
		onCreate(db);
	}
}