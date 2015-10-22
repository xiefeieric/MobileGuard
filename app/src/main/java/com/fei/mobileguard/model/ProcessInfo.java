package com.fei.mobileguard.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Fei on 29/09/15.
 */
public class ProcessInfo implements Serializable {

    private Drawable icon;
    private String apkName;
    private long apkSize;
    private boolean userProcess;
    private String apkPackageName;
    private boolean cbStatus;

    public boolean isCbStatus() {
        return cbStatus;
    }

    public void setCbStatus(boolean cbStatus) {
        this.cbStatus = cbStatus;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserProcess() {
        return userProcess;
    }

    public void setUserProcess(boolean userProcess) {
        this.userProcess = userProcess;
    }


    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", userProcess=" + userProcess +
                ", apkPackageName='" + apkPackageName + '\'' +
                '}';
    }
}
