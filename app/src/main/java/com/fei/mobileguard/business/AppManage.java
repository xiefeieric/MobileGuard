package com.fei.mobileguard.business;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.fei.mobileguard.model.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fei on 29/09/15.
 */
public class AppManage {

    public static List<AppInfo> getApps(Context context) {
        ArrayList<AppInfo> appList = new ArrayList<>();
//        ArrayList<AppInfo> userAppList = new ArrayList<>();
//        ArrayList<AppInfo> sysAppList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo pkg:installedPackages) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo applicationInfo = pkg.applicationInfo;
            //get apk icon
            Drawable icon = applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(icon);
            //get apk name
            String apkName = applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apkName);
            //get apk package name
            String apkPackageName = pkg.packageName;
            appInfo.setApkPackageName(apkPackageName);
            //get apk size
            String sourceDir = applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();
            appInfo.setApkSize(apkSize);
            //get apk for user or system
            int flags = applicationInfo.flags;

            //get apk dir in ram or sd
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0) {
                //external storage
                appInfo.setIsRam(false);
            } else {
                //internal storage
                appInfo.setIsRam(true);
            }

            if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
                //for system
                appInfo.setUserApp(false);
//                sysAppList.add(appInfo);
            } else {
                //for user
                appInfo.setUserApp(true);
//                userAppList.add(appInfo);
            }
            appList.add(appInfo);
//            appList.addAll(userAppList);
//            appList.addAll(sysAppList);
        }

        return appList;
    }

    public static List<AppInfo> getUserApps(Context context) {
//        ArrayList<AppInfo> appList = new ArrayList<>();
        ArrayList<AppInfo> userAppList = new ArrayList<>();
//        ArrayList<AppInfo> sysAppList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo pkg:installedPackages) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo applicationInfo = pkg.applicationInfo;
            //get apk icon
            Drawable icon = applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(icon);
            //get apk name
            String apkName = applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apkName);
            //get apk package name
            String apkPackageName = pkg.packageName;
            appInfo.setApkPackageName(apkPackageName);
            //get apk size
            String sourceDir = applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();
            appInfo.setApkSize(apkSize);
            //get apk for user or system
            int flags = applicationInfo.flags;

            //get apk dir in ram or sd
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0) {
                //external storage
                appInfo.setIsRam(false);
            } else {
                //internal storage
                appInfo.setIsRam(true);
            }

            if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
                //for system
                appInfo.setUserApp(false);
//                sysAppList.add(appInfo);
            } else {
                //for user
                appInfo.setUserApp(true);
                userAppList.add(appInfo);
            }
//            appList.add(appInfo);
//            appList.addAll(userAppList);
//            appList.addAll(sysAppList);
        }

        return userAppList;
    }

    public static List<AppInfo> getSysApps(Context context) {
//        ArrayList<AppInfo> appList = new ArrayList<>();
//        ArrayList<AppInfo> userAppList = new ArrayList<>();
        ArrayList<AppInfo> sysAppList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo pkg:installedPackages) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo applicationInfo = pkg.applicationInfo;
            //get apk icon
            Drawable icon = applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(icon);
            //get apk name
            String apkName = applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apkName);
            //get apk package name
            String apkPackageName = pkg.packageName;
            appInfo.setApkPackageName(apkPackageName);
            //get apk size
            String sourceDir = applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();
            appInfo.setApkSize(apkSize);
            //get apk for user or system
            int flags = applicationInfo.flags;

            //get apk dir in ram or sd
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0) {
                //external storage
                appInfo.setIsRam(false);
            } else {
                //internal storage
                appInfo.setIsRam(true);
            }

            if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
                //for system
                appInfo.setUserApp(false);
                sysAppList.add(appInfo);
            } else {
                //for user
                appInfo.setUserApp(true);
//                userAppList.add(appInfo);
            }
//            appList.add(appInfo);
//            appList.addAll(userAppList);
//            appList.addAll(sysAppList);
        }

        return sysAppList;
    }

}
