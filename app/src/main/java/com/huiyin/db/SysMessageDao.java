package com.huiyin.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;


public class SysMessageDao extends DBDao{
	
	public long add(String userid,String letterId) {
		mSQLiteDatabase.delete(DBHelper.SYSMESSAGE_TABLE,
				"userid=" + userid +" and letterId=" + letterId, null);
		ContentValues initialValues = new ContentValues();
		initialValues.put("userid",userid);
		initialValues.put("letterId",letterId);
		return mSQLiteDatabase.insert(DBHelper.SYSMESSAGE_TABLE, null, initialValues);
	}
	
	public ArrayList<String> fetcheAll(String userid) {
		ArrayList<String> listDatas = new ArrayList<String>();
		Cursor c = mSQLiteDatabase.query(DBHelper.SYSMESSAGE_TABLE, new String[] { "letterId" }, "userid = ?",
				new String[] {userid}, null, null, null);
		while (c.moveToNext()) {
				listDatas.add(c.getString(c.getColumnIndex("letterId")));
		}
		c.close();
		return listDatas;
	}

}
