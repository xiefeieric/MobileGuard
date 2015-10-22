package com.fei.mobileguard.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Fei on 30/09/15.
 */
public class SmsUtils {

    public interface BackupCallBack {

        void beforeProgress(int count);
        void onProgress(int progress);

    }

    public static boolean backupSms(Context context, BackupCallBack callBack) {



        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {


//            System.out.println("sd mounted");

            try {

                File file = new File(Environment.getExternalStorageDirectory(),"sms_backup.xml");
//                System.out.println(Environment.getExternalStorageDirectory());
                FileOutputStream fos = new FileOutputStream(file);

                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = Uri.parse("content://sms/");
                //type: 1 = send sms, 2 = received sms
                String[] projection = new String[]{"address","date","type","body"};
                Cursor cursor = contentResolver.query(uri, projection, null, null, null);
                int count = cursor.getCount();
                callBack.beforeProgress(count);
                int progress = 0;

                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fos,"utf-8");
                serializer.startDocument("utf-8", true);
                serializer.startTag(null, "smss");
                serializer.attribute(null, "size", String.valueOf(count));


                if (cursor!=null) {
                    while (cursor.moveToNext()) {
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        String type = cursor.getString(cursor.getColumnIndex("type"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
//                        System.out.println("--------------------");
//                        System.out.println(address);
//                        System.out.println(date);
//                        System.out.println(type);
//                        System.out.println(body);

                        serializer.startTag(null, "sms");
                        serializer.startTag(null, "address");
                        serializer.text(address);
                        serializer.endTag(null, "address");
                        serializer.startTag(null,"date");
                        serializer.text(date);
                        serializer.endTag(null,"date");
                        serializer.startTag(null,"type");
                        serializer.text(type);
                        serializer.endTag(null,"type");
                        serializer.startTag(null,"body");
                        //need to encrypt the body
                        String encryptBody = Crypto.encrypt("123", body);
                        serializer.text(encryptBody);
                        serializer.endTag(null, "body");
                        serializer.endTag(null, "sms");

                        SystemClock.sleep(100);

                        progress++;
                        callBack.onProgress(progress);
                    }
                }
//                System.out.println("cursor is null");

                serializer.endTag(null,"smss");
                serializer.endDocument();

                cursor.close();
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

}


