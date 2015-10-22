package com.fei.mobileguard.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.fei.mobileguard.activity.ActivityManagePrivacy;
import com.fei.mobileguard.db.dao.AppLockDao;

import java.util.List;

public class MyDogService extends Service {

    private boolean isOn = false;
    private String mPackageName;

    private AppLockDao mAppLockDao;
    private WatchDogReceiver mReceiver;
    private List<String> mAllPackages;

    public MyDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyContentObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mAllPackages = mAppLockDao.findAll();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //register content observer for database change
        getContentResolver().registerContentObserver(Uri.parse("content://com.fei.dataChange"),true,new MyContentObserver(null));

        mReceiver = new WatchDogReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.fei.StopWatchDog");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        mAppLockDao = new AppLockDao(this);
        mAllPackages = mAppLockDao.findAll();
        isOn = true;
        startDog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isOn = false;
        unregisterReceiver(mReceiver);
    }

    private void startDog() {
        new Thread() {
            @Override
            public void run() {

                while (isOn) {
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    String processName = am.getRunningAppProcesses().get(0).processName;
                    SystemClock.sleep(50);
//                System.out.println("process name: " +processName);
//                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(10);
//                    String packageName = runningTasks.get(0).topActivity.getPackageName();
//                    if (mAppLockDao.findLock(processName)) {
                    if (mAllPackages.contains(processName)) {

                        if (processName.equals(mPackageName)) {

                        } else {
                            Intent intent = new Intent(MyDogService.this, ActivityManagePrivacy.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packageName",processName);
                            startActivity(intent);
                        }
//                        System.out.println("locked: " + processName);

                    } else {
//                        System.out.println("unlocked: " + processName);
                    }
                }

            }
        }.start();
    }

    class WatchDogReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.fei.StopWatchDog")) {
                mPackageName = intent.getStringExtra("packageName");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                isOn = false;
                System.out.println("screen off");

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (!isOn) {
                    isOn = true;
                    System.out.println("screen on");
                    startDog();
                }
            }
        }
    }
}
