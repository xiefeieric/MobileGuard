package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;

import com.fei.mobileguard.R;
import com.fei.mobileguard.utils.SmsUtils;
import com.fei.mobileguard.utils.UiUtils;

public class ActivityAdvanceTool extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_tool);
    }

    public void doClickCheckNumber(View view) {
        startActivity(new Intent(this, ActivityToolNumberCheck.class));
    }

    public void doClickSmsBackup (View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        new Thread(){
            @Override
            public void run() {
                boolean success = SmsUtils.backupSms(ActivityAdvanceTool.this, new SmsUtils.BackupCallBack() {
                    @Override
                    public void beforeProgress(int count) {
                        progressDialog.setMax(count);
                    }

                    @Override
                    public void onProgress(int progress) {
                        progressDialog.setProgress(progress);
                    }
                });
                if (success) {
                    UiUtils.showToast(ActivityAdvanceTool.this,"Back Up Success");
                } else {
                    UiUtils.showToast(ActivityAdvanceTool.this,"Back Up Fail");
                }

                progressDialog.dismiss();
            }
        }.start();
    }

    public void doClickSetLock(View view) {
        startActivity(new Intent(this, ActivityPrivacyLock.class));
    }

}
