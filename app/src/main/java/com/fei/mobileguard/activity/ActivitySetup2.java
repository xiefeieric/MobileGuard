package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fei.mobileguard.R;
import com.fei.mobileguard.views.ViewSettingItem;

public class ActivitySetup2 extends ActivityBaseSetup {

    private Button btnNext, btnPre;
    private ViewSettingItem viewBindSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initViews();
        initListeners();

    }

    @Override
    void showNextPage() {
        if (viewBindSim.isChecked()) {
            Intent intent = new Intent(ActivitySetup2.this, ActivitySetup3.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        } else {
            Toast.makeText(ActivitySetup2.this,"Please bind your sim", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void showPreviousPage() {
        Intent intent = new Intent(ActivitySetup2.this, ActivitySetup1.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_trans_in, R.anim.pre_trans_out);
    }


    private void initViews() {
        viewBindSim = (ViewSettingItem) findViewById(R.id.viewBindSim);
        viewBindSim.setChecked(mSharedPreferences.getBoolean("bind", false));
        if (mSharedPreferences.getBoolean("bind", false)) {
            viewBindSim.setDesc("SIM bind already");
        } else {
            viewBindSim.setDesc("SIM is not bind");
        }

        btnNext = (Button) findViewById(R.id.btnNextBind);
        btnPre = (Button) findViewById(R.id.btnPreBind);
    }

    private void initListeners() {

        viewBindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBindSim.isChecked()) {
                    viewBindSim.setChecked(false);
                    mSharedPreferences.edit().putBoolean("bind", false).commit();
                    mSharedPreferences.edit().remove("sim");
                } else {
                    viewBindSim.setChecked(true);
                    mSharedPreferences.edit().putBoolean("bind", true).commit();
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //need to change later, for test only!!
                    String simSerialNumber = tm.getSimSerialNumber();
                    mSharedPreferences.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBindSim.isChecked()) {
                    showNextPage();
                } else {
                    Toast.makeText(ActivitySetup2.this,"Please bind your sim", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousPage();
            }
        });

    }


}
