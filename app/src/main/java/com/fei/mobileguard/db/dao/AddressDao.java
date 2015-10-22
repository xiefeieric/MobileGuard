package com.fei.mobileguard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fei on 22/09/15.
 */
public class AddressDao {

    public static final String PATH = "data/data/com.fei.mobileguard/files/address.db";


    public static String getAddress (String number) {
        String location = "Unknown";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);

        if (number.length()>=7 && number.startsWith("1")) {

            Cursor cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id=?)",
                    new String[]{number.substring(0, 7)});
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
                cursor.close();
            }
        } else if (number.length()==3){
            location = "Emergency Number";
        } else if (number.length()==4){
            location = "Emulator";
        } else if (number.length()==5){
            location = "Customer Service";
        } else if (number.length()>=10 && number.startsWith("0")) {

            Cursor cursor = database.rawQuery("select location from data2 where area = ?",
                    new String[]{number.substring(1,4)});
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
                cursor.close();
            } else {
                cursor = database.rawQuery("select location from data2 where area = ?",
                        new String[]{number.substring(1,3)});
                if (cursor.moveToNext()) {
                    location = cursor.getString(0);
                    cursor.close();
                }

            }

        }
        database.close();
        return location;
    }
}
