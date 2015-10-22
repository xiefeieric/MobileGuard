package com.fei.mobileguard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fei on 02/10/15.
 */
public class VirusDao {

    public static final String PATH = "data/data/com.fei.mobileguard/files/antivirus.db";

    public static String getVirusDesc(String md5) {

        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);
        String sql = "select desc from datable where md5=?";
        Cursor cursor = database.rawQuery(sql, new String[]{md5});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }

        return null;
    }

}
