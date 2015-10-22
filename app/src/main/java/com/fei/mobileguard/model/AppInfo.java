package com.fei.mobileguard.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Fei on 29/09/15.
 */
public class AppInfo implements Serializable {

    private Drawable icon;
    private String apkName;
    private long apkSize;
    private boolean userApp;
    private boolean isRam;
    private String apkPackageName;

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

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRam() {
        return isRam;
    }

    public void setIsRam(boolean isRam) {
        this.isRam = isRam;
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
                ", userApp=" + userApp +
                ", isRam=" + isRam +
                ", apkPackageName='" + apkPackageName + '\'' +
                '}';
    }
}
