package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fei.mobileguard.R;

public class ActivityLostFind extends Activity {

    private SharedPreferences mSharedPreferences;
    private ImageView ivLock;
    private TextView tvSafeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        Boolean configed = mSharedPreferences.getBoolean("configed",false);
        if (configed) {
            setContentView(R.layout.activity_lost_find);
            initViews();
            initListeners();
        } else {
            startActivity(new Intent(ActivityLostFind.this,ActivitySetup1.class));
            finish();
        }

    }

    private void initListeners() {
        boolean setProtect = mSharedPreferences.getBoolean("setProtect", false);
        if (setProtect) {
            ivLock.setImageResource(R.drawable.lock);
        } else {
            ivLock.setImageResource(R.drawable.unlock);
        }

        String phone = mSharedPreferences.getString("phone", "");
        tvSafeNumber.setText(phone);
    }

    private void initViews() {
        ivLock = (ImageView) findViewById(R.id.ivLock);
        tvSafeNumber = (TextView) findViewById(R.id.tvSafeNumber);
    }

    public void doClick(View view) {
        startActivity(new Intent(ActivityLostFind.this,ActivitySetup1.class));
        finish();
    }

}
