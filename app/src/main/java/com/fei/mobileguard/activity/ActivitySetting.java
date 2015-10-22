package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.fei.mobileguard.R;
import com.fei.mobileguard.service.MyAddressService;
import com.fei.mobileguard.service.MyBlackListService;
import com.fei.mobileguard.service.MyDogService;
import com.fei.mobileguard.service.MyRocketService;
import com.fei.mobileguard.views.ViewSettingClick;
import com.fei.mobileguard.views.ViewSettingItem;

import java.util.List;

public class ActivitySetting extends Activity {

    private ViewSettingItem mViewSettingItem,mViewSettingAddress,mViewSettingRocket,mViewSettingBlackList,mViewSettingDog;
    private ViewSettingClick mViewSettingClick;
//    private RelativeLayout mRl;
    private SharedPreferences mSharedPreferences;
    private String[] mNames;
    private int mStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mNames = new String[]{"Green","Blue"};
        initViews();
    }

    private void initViews() {

        initAutoUpdate();
        initAddressCheck();
        initStyleChooser();
        initRocket();
        initBlackListService();
        initPrivacyService();

    }

    private void initPrivacyService() {
        mViewSettingDog = (ViewSettingItem) findViewById(R.id.viewSettingDog);
        mViewSettingDog.setChecked(mSharedPreferences.getBoolean("dog_service", false));
//        mViewSettingItem.setTitle("Auto Update");
        if (mSharedPreferences.getBoolean("dog_service",true)) {
            mViewSettingDog.setDesc("Service is ON");
            mSharedPreferences.edit().putBoolean("dog_service", true).commit();
        } else {
            mViewSettingDog.setDesc("Service is OFF");
            mSharedPreferences.edit().putBoolean("dog_service", false).commit();
        }

        if (validDogService()) {
            mViewSettingDog.setChecked(true);
            mViewSettingDog.setDesc("Service is ON");
        } else {
            mViewSettingDog.setChecked(false);
            mViewSettingDog.setDesc("Service is OFF");
        }
//        mRl = (RelativeLayout) findViewById(R.id.rlViewSetting);
        mViewSettingDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSettingDog.isChecked()) {
                    mViewSettingDog.setChecked(false);
//                    mViewSettingItem.setDesc("Auto Update is OFF");
                    mSharedPreferences.edit().putBoolean("dog_service", false).commit();
                    stopService(new Intent(ActivitySetting.this, MyDogService.class));
                } else {
                    mViewSettingDog.setChecked(true);
//                    mViewSettingItem.setDesc("Auto Update is ON");
                    mSharedPreferences.edit().putBoolean("dog_service", true).commit();
                    startService(new Intent(ActivitySetting.this, MyDogService.class));
                }
            }
        });
    }

    private void initBlackListService() {

        mViewSettingBlackList = (ViewSettingItem) findViewById(R.id.viewSettingBlackList);
        mViewSettingBlackList.setChecked(mSharedPreferences.getBoolean("black_list_service", false));
//        mViewSettingItem.setTitle("Auto Update");
        if (mSharedPreferences.getBoolean("black_list_service",true)) {
            mViewSettingBlackList.setDesc("Service is ON");
            mSharedPreferences.edit().putBoolean("black_list_service", true).commit();
        } else {
            mViewSettingBlackList.setDesc("Service is OFF");
            mSharedPreferences.edit().putBoolean("black_list_service", false).commit();
        }

        if (validBlackListService()) {
            mViewSettingBlackList.setChecked(true);
            mViewSettingBlackList.setDesc("Service is ON");
        } else {
            mViewSettingBlackList.setChecked(false);
            mViewSettingBlackList.setDesc("Service is OFF");
        }
//        mRl = (RelativeLayout) findViewById(R.id.rlViewSetting);
        mViewSettingBlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSettingBlackList.isChecked()) {
                    mViewSettingBlackList.setChecked(false);
//                    mViewSettingItem.setDesc("Auto Update is OFF");
                    mSharedPreferences.edit().putBoolean("black_list_service", false).commit();
                    stopService(new Intent(ActivitySetting.this, MyBlackListService.class));
                } else {
                    mViewSettingBlackList.setChecked(true);
//                    mViewSettingItem.setDesc("Auto Update is ON");
                    mSharedPreferences.edit().putBoolean("black_list_service", true).commit();
                    startService(new Intent(ActivitySetting.this, MyBlackListService.class));
                }
            }
        });

    }

    private void initRocket() {
        mViewSettingRocket = (ViewSettingItem) findViewById(R.id.viewSettingRocket);
        mViewSettingRocket.setChecked(mSharedPreferences.getBoolean("rocket", false));
//        mViewSettingItem.setTitle("Auto Update");
        if (mSharedPreferences.getBoolean("rocket",true)) {
            mViewSettingRocket.setDesc("Rocket is ON");
            mSharedPreferences.edit().putBoolean("rocket", true).commit();
        } else {
            mViewSettingRocket.setDesc("Rocket is OFF");
            mSharedPreferences.edit().putBoolean("rocket", false).commit();
        }

        if (validRocketService()) {
            mViewSettingRocket.setChecked(true);
            mViewSettingRocket.setDesc("Rocket is ON");
        } else {
            mViewSettingRocket.setChecked(false);
            mViewSettingRocket.setDesc("Rocket is OFF");
        }
//        mRl = (RelativeLayout) findViewById(R.id.rlViewSetting);
        mViewSettingRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSettingRocket.isChecked()) {
                    mViewSettingRocket.setChecked(false);
//                    mViewSettingItem.setDesc("Auto Update is OFF");
                    mSharedPreferences.edit().putBoolean("rocket", false).commit();
                    stopService(new Intent(ActivitySetting.this, MyRocketService.class));
                } else {
                    mViewSettingRocket.setChecked(true);
//                    mViewSettingItem.setDesc("Auto Update is ON");
                    mSharedPreferences.edit().putBoolean("rocket", true).commit();
                    startService(new Intent(ActivitySetting.this, MyRocketService.class));
                }
            }
        });
    }

    private void initStyleChooser() {
        mStyle = mSharedPreferences.getInt("style_alert",0);
        mViewSettingClick = (ViewSettingClick) findViewById(R.id.viewSettingClick);
        mViewSettingClick.setDesc(mNames[mStyle]);

        mViewSettingClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStyle = mSharedPreferences.getInt("style_alert",0);
                showStyleChooser();
            }
        });
    }

    private void showStyleChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Style Chooser");

        builder.setSingleChoiceItems(mNames,mStyle , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSharedPreferences.edit().putInt("style_alert", which).commit();
                mViewSettingClick.setDesc(mNames[which]);
                mStyle = mSharedPreferences.getInt("style_alert",0);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void initAutoUpdate() {
        mViewSettingItem = (ViewSettingItem) findViewById(R.id.viewSettingItem);
        mViewSettingItem.setChecked(mSharedPreferences.getBoolean("auto_update", true));
//        mViewSettingItem.setTitle("Auto Update");
        if (mSharedPreferences.getBoolean("auto_update",true)) {
            mViewSettingItem.setDesc("Auto Update is ON");
        } else {
            mViewSettingItem.setDesc("Auto Update is OFF");
        }
//        mRl = (RelativeLayout) findViewById(R.id.rlViewSetting);
        mViewSettingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSettingItem.isChecked()) {
                    mViewSettingItem.setChecked(false);
//                    mViewSettingItem.setDesc("Auto Update is OFF");
                    mSharedPreferences.edit().putBoolean("auto_update", false).commit();
                } else {
                    mViewSettingItem.setChecked(true);
//                    mViewSettingItem.setDesc("Auto Update is ON");
                    mSharedPreferences.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    private void initAddressCheck(){
        mViewSettingAddress = (ViewSettingItem) findViewById(R.id.viewSettingAddress);
        mViewSettingAddress.setChecked(mSharedPreferences.getBoolean("address_check", false));
        if (mSharedPreferences.getBoolean("address_check",true)) {
            mViewSettingAddress.setDesc("Call Check is ON");
        } else {
            mViewSettingAddress.setDesc("Call Check is OFF");
        }

        if (validService()) {
            mViewSettingAddress.setChecked(true);
            mViewSettingAddress.setDesc("Call Check is ON");
        } else {
            mViewSettingAddress.setChecked(false);
            mViewSettingAddress.setDesc("Call Check is OFF");
        }

        mViewSettingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSettingAddress.isChecked()) {
                    mViewSettingAddress.setChecked(false);
//                    mViewSettingItem.setDesc("Auto Update is OFF");
                    mSharedPreferences.edit().putBoolean("address_check", false).commit();
                    stopService(new Intent(ActivitySetting.this, MyAddressService.class));
                } else {
                    mViewSettingAddress.setChecked(true);
//                    mViewSettingItem.setDesc("Auto Update is ON");
                    mSharedPreferences.edit().putBoolean("address_check", true).commit();
                    startService(new Intent(ActivitySetting.this, MyAddressService.class));
                }
            }
        });
    }

    private boolean validService(){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo:runningServices) {
            String className = serviceInfo.service.getClassName();
            if (className.equals("com.fei.mobileguard.service.MyAddressService")) {
                return true;
            }
        }

        return false;
    }

    private boolean validRocketService(){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo:runningServices) {
            String className = serviceInfo.service.getClassName();
            if (className.equals("com.fei.mobileguard.service.MyRocketService")) {
                return true;
            }
        }

        return false;
    }

    private boolean validBlackListService(){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo:runningServices) {
            String className = serviceInfo.service.getClassName();
            if (className.equals("com.fei.mobileguard.service.MyBlackListService")) {
                return true;
            }
        }

        return false;
    }

    private boolean validDogService(){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo:runningServices) {
            String className = serviceInfo.service.getClassName();
            if (className.equals("com.fei.mobileguard.service.MyDogService")) {
                return true;
            }
        }

        return false;
    }

}
