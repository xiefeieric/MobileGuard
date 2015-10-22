package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fei.mobileguard.R;

public class ActivitySetup4 extends ActivityBaseSetup {

    private Button btnPre,btnDone;
    private CheckBox cbSetDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initViews();
        initListeners();
    }

    @Override
    void showNextPage() {
        boolean setProtect = mSharedPreferences.getBoolean("setProtect", false);
        if (setProtect) {
            Intent intent = new Intent(ActivitySetup4.this,ActivityLostFind.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        } else {
            Toast.makeText(this,"Plese enable the protection",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void showPreviousPage() {
        Intent intent = new Intent(ActivitySetup4.this,ActivitySetup3.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_trans_in, R.anim.pre_trans_out);
    }

    private void initViews() {
        btnPre = (Button) findViewById(R.id.btnPreDone);
        btnDone = (Button) findViewById(R.id.btnDone);
        cbSetDone = (CheckBox) findViewById(R.id.cbSetDone);
        boolean setProtect = mSharedPreferences.getBoolean("setProtect", false);
        if (setProtect) {
            cbSetDone.setChecked(true);
            cbSetDone.setText("Lost protection is Enabled");
         } else {
            cbSetDone.setChecked(false);
            cbSetDone.setText("Lost protection is Disabled");
        }
    }

    private void initListeners() {

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousPage();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences.edit().putBoolean("configed",true).commit();
                showNextPage();
            }
        });

        cbSetDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSharedPreferences.edit().putBoolean("setProtect",true).commit();
                    cbSetDone.setText("Lost protection is Enabled");
                } else {
                    mSharedPreferences.edit().putBoolean("setProtect",false).commit();
                    cbSetDone.setText("Lost protection is Disabled");
                }
            }
        });
    }

}
