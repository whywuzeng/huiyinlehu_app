package com.huiyin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.huiyin.AppContext;

public class DWSqliteUtils extends SQLiteOpenHelper {

	public static final String TAG = "DWSqliteUtils";
	public static SQLiteDatabase mDatabase;
	private static DWSqliteUtils instance;
	private static final String dbName = "huiyin_db.db";
	private static final String tableName = "area_table";
	private static final int VERSION = 1;
	private Cursor mCursor;
	 
	public static String parentId="parentId";
	public static String areaName="areaName";
	public static String level="level";
	 
	private static final String VALUES = "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ parentId
			+ " INTEGER, "
			+ areaName
			+ " TEXT, "
			+ level
			+ " INTEGER) ";
			 
	
	private static final String CREATE_DB_ORDER = "CREATE TABLE IF NOT EXISTS "
			+ tableName + VALUES;

	private DWSqliteUtils(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static DWSqliteUtils getInstance() {
//		if (instance == null || mDatabase == null) {
			instance = new DWSqliteUtils(AppContext.getInstance(), dbName,
					null, VERSION);
			mDatabase = instance.getReadableDatabase();
//		}
		return instance;
	}
 
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DB_ORDER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		db.update(tableName, values, null, null);
	}

}



