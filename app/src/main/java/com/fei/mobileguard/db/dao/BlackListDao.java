package com.fei.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fei on 27/09/15.
 */
public class BlackListDao extends SQLiteOpenHelper {

    public BlackListDao(Context context) {
        super(context, "black_list.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table black_list (_id integer primary key autoincrement, number text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(String number) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        long addSuccess = database.insert("black_list", null, values);
        if (addSuccess != -1) {
            database.close();
            return true;
        }
        return false;
    }

    public boolean findNumber(String number) {

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query("black_list", new String[]{"number"}, "number =?",new String[]{number},null,null,null);
        if (cursor!=null) {
            if (cursor.moveToNext()) {
                database.close();
                cursor.close();
                return true;
            }
        }
        return false;
    }

    public boolean deleteData(String number) {
        SQLiteDatabase database = getWritableDatabase();
        int deleteSuccess = database.delete("black_list", "number =?", new String[]{number});
        database.close();
        if (deleteSuccess!=0) {
            System.out.println("delete success");
            return true;
        } else {
            System.out.println("delete fail");
            return false;
        }
    }

    public List<String> queryAll() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query("black_list", new String[]{"number"}, null, null, null, null, null);
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                list.add(number);
            }
            database.close();
            cursor.close();
            return list;

        }
        return null;
    }

    public List<String> queryPerPage(int page, int offset) {

        List<String> list = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select number from black_list limit ? offset ?",
                new String[]{String.valueOf(page), String.valueOf(offset)});
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                list.add(number);
            }
            database.close();
            cursor.close();
            return list;
        }
        return null;
    }

    public int getCount() {

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select count(*) from black_list", null);
        if (cursor!=null) {
            cursor.moveToNext();
            int count = cursor.getInt(0);
            cursor.close();
            database.close();
            return count;
        }

        return 0;
    }
}
