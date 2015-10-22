package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fei.mobileguard.R;

public class ActivitySetup3 extends ActivityBaseSetup {

    private Button btnPre,btnNext,btnSelectContact;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initViews();
        initListeners();
    }

    @Override
    void showNextPage() {
        if (!TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
            mSharedPreferences.edit().putString("phone",etPhoneNumber.getText().toString()).commit();
            Intent intent = new Intent(ActivitySetup3.this,ActivitySetup4.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        } else {
            Toast.makeText(this,"Please enter phone number",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void showPreviousPage() {
        Intent intent = new Intent(ActivitySetup3.this,ActivitySetup2.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_trans_in, R.anim.pre_trans_out);
    }

    private void initViews() {
        btnPre = (Button) findViewById(R.id.btnPreNumber);
        btnNext = (Button) findViewById(R.id.btnNextNumber);
        btnSelectContact = (Button) findViewById(R.id.btnSelectContacts);
        etPhoneNumber = (EditText) findViewById(R.id.etNumberWarn);
        String phone = mSharedPreferences.getString("phone", "");
        etPhoneNumber.setText(phone);
    }

    private void initListeners() {

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showPreviousPage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
                    showNextPage();
                } else {
                    Toast.makeText(ActivitySetup3.this,"Please enter phone number",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetup3.this,ActivityReadContacts.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            etPhoneNumber.setText(phone);
        }
    }
}
