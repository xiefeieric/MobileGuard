package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fei.mobileguard.R;
import com.fei.mobileguard.fragment.FragmentLock;
import com.fei.mobileguard.fragment.FragmentUnlock;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ActivityPrivacyLock extends Activity implements RadioGroup.OnCheckedChangeListener {

    @ViewInject(R.id.rbUnclock)
    private RadioButton rbUnlock;
    @ViewInject(R.id.rbLock)
    private RadioButton rbLock;
    @ViewInject(R.id.flContent)
    private FrameLayout flContent;
    @ViewInject(R.id.rrLock)
    private RadioGroup rrLock;
    private FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_lock);
        initViews();
        initListeners();
    }


    private void initViews() {
        ViewUtils.inject(this);
        mFm = getFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        ft.replace(R.id.flContent,new FragmentUnlock()).commit();

    }

    private void initListeners() {
        rrLock.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (rbUnlock.isChecked()) {
            rbUnlock.setTextColor(Color.WHITE);
            rbUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
            rbLock.setTextColor(Color.GRAY);
            rbLock.setBackgroundResource(R.drawable.tab_right_default);
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.flContent,new FragmentUnlock()).commit();

        } else if (rbLock.isChecked()){
            rbUnlock.setTextColor(Color.GRAY);
            rbUnlock.setBackgroundResource(R.drawable.tab_left_default);
            rbLock.setTextColor(Color.WHITE);
            rbLock.setBackgroundResource(R.drawable.tab_right_pressed);
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.flContent,new FragmentLock()).commit();
        }
    }
}
