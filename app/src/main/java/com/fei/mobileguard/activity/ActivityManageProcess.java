package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.adapter.ManageProcessAdapter;
import com.fei.mobileguard.business.ProcessManage;
import com.fei.mobileguard.model.ProcessInfo;
import com.fei.mobileguard.utils.UiUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ActivityManageProcess extends Activity {

    @ViewInject(R.id.tvProcessNumber)
    private TextView tvProcessNumber;
    @ViewInject(R.id.tvSpace)
    private TextView tvSpace;
    @ViewInject(R.id.tvProcessType)
    private TextView tvProcessType;
    @ViewInject(R.id.lvManageProcess)
    private ListView lvManageProcess;
    private ActivityManager mActivityManager;

    private List<ProcessInfo> mArrayListUser;
    private List<ProcessInfo> mArrayListSys;
    private ManageProcessAdapter mAdapter;
    private int mTotalProcess;
    private long mAvailMem;
    private long mTotalMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_process);
        ViewUtils.inject(this);
        mArrayListUser = new ArrayList<>();
        initViews();
        initListeners();
    }

    private void initViews() {

        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        mTotalProcess = runningAppProcesses.size();
        tvProcessNumber.setText("Running: " + mTotalProcess);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        mAvailMem = memoryInfo.availMem;
        mTotalMem = memoryInfo.totalMem;
        tvSpace.setText("Free/Total: " + Formatter.formatFileSize(this, mAvailMem) + "/" + Formatter.formatFileSize(this, mTotalMem));

        new Thread(){
            @Override
            public void run() {
                mArrayListUser = ProcessManage.getUserProcess(ActivityManageProcess.this);
                mArrayListSys = ProcessManage.getSysProcess(ActivityManageProcess.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ManageProcessAdapter(ActivityManageProcess.this, mArrayListUser, mArrayListSys);
                        lvManageProcess.setAdapter(mAdapter);
                    }
                });
            }
        }.start();

//        System.out.println(mArrayListUser.size());


    }

    private void initListeners() {

        lvManageProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox cbProcessCheck = (CheckBox) view.findViewById(R.id.cbProcessCheck);

                if (position<mArrayListUser.size()) {
                    if (mArrayListUser.get(position).isCbStatus()) {
                        mArrayListUser.get(position).setCbStatus(false);
                        cbProcessCheck.setChecked(false);
                    } else {
                        mArrayListUser.get(position).setCbStatus(true);
                        cbProcessCheck.setChecked(true);
                    }
                } else {
                    int location = position-mArrayListUser.size();
                    if (mArrayListSys.get(location).isCbStatus()) {
                        mArrayListSys.get(location).setCbStatus(false);
                        cbProcessCheck.setChecked(false);
                    } else {
                        mArrayListSys.get(location).setCbStatus(true);
                        cbProcessCheck.setChecked(true);
                    }
                }
            }
        });

        lvManageProcess.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mArrayListUser!=null && firstVisibleItem < mArrayListUser.size()) {
                    tvProcessType.setText("User Processes");
                } else if (mArrayListUser!=null && firstVisibleItem >= mArrayListUser.size()) {
                    tvProcessType.setText("System Processes");
                }

            }
        });
    }

    public void selectAll(View view) {

        for (int i=0;i<mArrayListUser.size();i++) {
            if (!mArrayListUser.get(i).isCbStatus()) {
                mArrayListUser.get(i).setCbStatus(true);
            }
        }

        for (int i=0;i<mArrayListSys.size();i++) {
            if (!mArrayListSys.get(i).isCbStatus()) {
                mArrayListSys.get(i).setCbStatus(true);
            }
        }

        if (mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new ManageProcessAdapter(this,mArrayListUser,mArrayListSys);
            lvManageProcess.setAdapter(mAdapter);
        }

    }

    public void selectNone(View view) {

        for (int i=0;i<mArrayListUser.size();i++) {
            if (mArrayListUser.get(i).isCbStatus()) {
                mArrayListUser.get(i).setCbStatus(false);
            }
        }

        for (int i=0;i<mArrayListSys.size();i++) {
            if (mArrayListSys.get(i).isCbStatus()) {
                mArrayListSys.get(i).setCbStatus(false);
            }
        }

        if (mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new ManageProcessAdapter(this,mArrayListUser,mArrayListSys);
            lvManageProcess.setAdapter(mAdapter);
        }

    }



    public void clean(View view) {

        List<ProcessInfo> cleanList = new ArrayList<>();
        int count = 0;
        long aMem = 0;

        for (ProcessInfo info:mArrayListUser) {
            if (info.isCbStatus()) {
                cleanList.add(info);
            }
        }

        for (ProcessInfo info:mArrayListSys) {
            if (info.isCbStatus()) {
                cleanList.add(info);
            }
        }

        if (cleanList.size()>0) {

            for (ProcessInfo info:cleanList) {
                if (info.isUserProcess()) {
                    mActivityManager.killBackgroundProcesses(info.getApkPackageName());
                    mArrayListUser.remove(info);
                    count++;
                    aMem += info.getApkSize();
                } else {
                    mActivityManager.killBackgroundProcesses(info.getApkPackageName());
                    mArrayListSys.remove(info);
                    count++;
                    aMem += info.getApkSize();
                }

            }

            mAdapter.notifyDataSetChanged();
            UiUtils.showToast(this, "Cleaned " + count + " processes, and freed " + Formatter.formatFileSize(this, aMem) + " space");

            int currentProcess = mTotalProcess - count;
            tvProcessNumber.setText("Running: " + currentProcess);
            mTotalProcess = currentProcess;

            mAvailMem += aMem;
            tvSpace.setText("Free/Total: " + Formatter.formatFileSize(this, mAvailMem) + "/" + Formatter.formatFileSize(this, mTotalMem));
        }

    }
}
