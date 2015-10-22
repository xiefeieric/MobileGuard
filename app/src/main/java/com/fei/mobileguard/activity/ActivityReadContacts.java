package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fei.mobileguard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityReadContacts extends Activity {

    private ListView lvContacts;
    private List<HashMap<String,String>> mMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        mMaps = new ArrayList();
//        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2);

        Cursor cursorRawContact = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
        if (cursorRawContact!=null) {
            while (cursorRawContact.moveToNext()) {
                String rawId = cursorRawContact.getString(0);
//            System.out.println("Raw Id: "+rawId);
                Cursor cursorData = getContentResolver().query(Uri.parse("content://com.android.contacts/data"),
                        new String[]{"data1","mimetype"},"contact_id=?",new String[]{rawId},null);
                if (cursorData!=null) {
                    HashMap<String,String> map = new HashMap<>();
                    while (cursorData.moveToNext()) {
                        String data = cursorData.getString(0);
                        String mime = cursorData.getString(1);
                        System.out.println(data + " : " + mime);


                        if (mime.equals("vnd.android.cursor.item/phone_v2")) {
                            map.put("phone",data);
                        } else if (mime.equals("vnd.android.cursor.item/name")) {
                            map.put("name",data);
                        }
                    }
                    mMaps.add(map);

                    cursorData.close();
                }

            }
            cursorRawContact.close();

//        lvContacts.setAdapter(adapter);
        }

        lvContacts.setAdapter(new SimpleAdapter(this,mMaps,R.layout.contact_list_item,
                new String[]{"name","phone"},new int[] {R.id.tvContactName,R.id.tvContactNumber}));

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = mMaps.get(position);
                String phone = hashMap.get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }



}
