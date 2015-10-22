package com.fei.mobileguard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.db.dao.AddressDao;

public class ActivityToolNumberCheck extends Activity {

    private EditText etNumberCheck;
    private TextView tvShowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_number_check);
        etNumberCheck = (EditText) findViewById(R.id.etNumberCheck);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);

        etNumberCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    String address = AddressDao.getAddress(s.toString());
                    tvShowLocation.setText(address);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void doClick(View view) {
        String number = etNumberCheck.getText().toString().trim();
        if (!TextUtils.isEmpty(number)) {
            String address = AddressDao.getAddress(number);
            tvShowLocation.setText(address);
        } else {
            Animation anim = AnimationUtils.loadAnimation(this,R.anim.shake);
            etNumberCheck.startAnimation(anim);
            vibrator();
        }
    }

    private void vibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}
