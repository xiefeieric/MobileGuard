package com.fei.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fei on 04/10/15.
 */
public class AppLockDao extends SQLiteOpenHelper {

    private Context mContext;

    public AppLockDao(Context context) {
        super(context, "app_lock.db", null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table app_lock (_id integer primary key autoincrement, package_name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addLock(String package_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", package_name);
        db.insert("app_lock", null, values);
        db.close();

        mContext.getContentResolver().notifyChange(Uri.parse("com.fei.dataChange"),null);

    }

    public void deleteLock(String package_name) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete("app_lock", "package_name=?", new String[]{package_name});
        db.close();
        mContext.getContentResolver().notifyChange(Uri.parse("com.fei.dataChange"), null);
    }

    public boolean findLock(String package_name) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("app_lock", new String[]{"package_name"}, "package_name=?", new String[]{package_name}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public List<String> findAll() {

        List<String> packageList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("app_lock", new String[]{"package_name"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String pack = cursor.getString(0);
            packageList.add(pack);
        }
        cursor.close();
        db.close();
        return packageList;
    }
}
