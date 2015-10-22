package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.db.dao.VirusDao;
import com.fei.mobileguard.utils.MD5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class ActivityVirusAnti extends Activity {

    public static final int VIRUS_SCAN_BEGIN = 1;
    public static final int VIRUS_SCAN_PROGRESS = 2;
    public static final int VIRUS_SCAN_FINISH = 3;

    @ViewInject(R.id.ivVirusRadar)
    private ImageView ivVirusRadar;
    @ViewInject(R.id.pbVirus)
    private ProgressBar pbVirus;
    @ViewInject(R.id.llContent)
    private LinearLayout llContent;
    @ViewInject(R.id.tvVirusTitle)
    private TextView tvVirusTitle;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIRUS_SCAN_BEGIN:
                    break;

                case VIRUS_SCAN_PROGRESS:
                    tvVirusTitle.setText("Quick Scanning ......");
                    ScanInfo scanInfo = (ScanInfo) msg.obj;
                    TextView view = new TextView(ActivityVirusAnti.this);
                    if (!scanInfo.isVirus) {
                        view.setText(scanInfo.appName+" is OK");
                    } else {
                        view.setTextColor(Color.RED);
                        view.setText(scanInfo.appName+" is VIRUS");
                    }
                    llContent.addView(view,0);
                    break;

                case VIRUS_SCAN_FINISH:
                    tvVirusTitle.setText("Scan Finished");
                    ivVirusRadar.clearAnimation();
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_anti);
        initView();
        initData();
    }

    private void initView() {

        ViewUtils.inject(this);
        RotateAnimation anim = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(2000);
        anim.setRepeatCount(Animation.INFINITE);
        ivVirusRadar.startAnimation(anim);
    }

    private void initData() {

        new Thread(){
            @Override
            public void run() {

                Message msg = mHandler.obtainMessage();
                msg.what = VIRUS_SCAN_BEGIN;
                mHandler.sendMessage(msg);

                PackageManager packageManager = getPackageManager();
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                pbVirus.setMax(installedPackages.size());

                SystemClock.sleep(200);

                int count = 0;
                for (PackageInfo info:installedPackages) {
                    SystemClock.sleep(200);
                    ScanInfo scanInfo = new ScanInfo();
                    String appName = info.applicationInfo.loadLabel(packageManager).toString();
                    scanInfo.appName = appName;
                    String packageName = info.applicationInfo.packageName;
                    scanInfo.appPackName = packageName;
                    String sourceDir = info.applicationInfo.sourceDir;
                    String fileMd5 = MD5Utils.getFileMd5(sourceDir);
                    String virusDesc = VirusDao.getVirusDesc(fileMd5);
                    if (virusDesc==null) {
                        scanInfo.isVirus = false;
                    } else {
                        scanInfo.isVirus = true;
                    }
                    msg = mHandler.obtainMessage();
                    msg.what = VIRUS_SCAN_PROGRESS;
                    msg.obj = scanInfo;
                    mHandler.sendMessage(msg);
                    pbVirus.setProgress(count);
                    count++;
                    pbVirus.setProgress(count);
                }

                msg = mHandler.obtainMessage();
                msg.what = VIRUS_SCAN_FINISH;
                mHandler.sendMessage(msg);
            }
        }.start();

    }

    static class ScanInfo {
        boolean isVirus;
        String appName;
        String appPackName;
    }

}

