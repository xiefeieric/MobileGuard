package com.fei.mobileguard.business;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;

import com.fei.mobileguard.model.AppInfo;
import com.fei.mobileguard.model.ProcessInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fei on 29/09/15.
 */
public class ProcessManage {

    public static List<ProcessInfo> getUserProcess(Context context) {
//        ArrayList<AppInfo> appList = new ArrayList<>();
        ArrayList<ProcessInfo> userProcessList = new ArrayList<>();
//        ArrayList<ProcessInfo> sysProcessList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process:runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            String processName = process.processName;
            processInfo.setApkPackageName(processName);

            try {
                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{process.pid});
                int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty()*1024;
//                System.out.println(totalPrivateDirty);
                processInfo.setApkSize(totalPrivateDirty);
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                Drawable icon = applicationInfo.loadIcon(packageManager);
                if (icon!=null) {
                    processInfo.setIcon(icon);
                } else {
                    processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                }

                String apkName = applicationInfo.loadLabel(packageManager).toString();
                if (apkName!=null) {
                    processInfo.setApkName(apkName);
                } else {
                    processInfo.setApkName("");
                }

                int flags = applicationInfo.flags;
                if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
//                //for system
                    processInfo.setUserProcess(false);
////                sysAppList.add(appInfo);
                } else {
//                //for user
                    processInfo.setUserProcess(true);
                    userProcessList.add(processInfo);
                }
//                System.out.println(processInfo.toString());

            } catch (Exception e) {
                e.printStackTrace();
                processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                processInfo.setApkName("");
            }

        }
        return userProcessList;
    }

    public static List<ProcessInfo> getSysProcess(Context context) {
//        ArrayList<AppInfo> appList = new ArrayList<>();
//        ArrayList<ProcessInfo> userProcessList = new ArrayList<>();
        ArrayList<ProcessInfo> sysProcessList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process:runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            String processName = process.processName;
            try {
                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{process.pid});
                int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty()*1024;
//                System.out.println(totalPrivateDirty);
                processInfo.setApkSize(totalPrivateDirty);
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                Drawable icon = applicationInfo.loadIcon(packageManager);
                if (icon!=null) {
                    processInfo.setIcon(icon);
                } else {
                    processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                }
                String apkName = applicationInfo.loadLabel(packageManager).toString();
                if (apkName!=null) {
                    processInfo.setApkName(apkName);
                } else {
                    processInfo.setApkName("");
                }

                int flags = applicationInfo.flags;
                if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
//                //for system
                    processInfo.setUserProcess(false);
                    sysProcessList.add(processInfo);
                } else {
//                //for user
                    processInfo.setUserProcess(true);
//                    userProcessList.add(processInfo);
                }
//                System.out.println(processInfo.toString());

            } catch (Exception e) {
                e.printStackTrace();
                processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                processInfo.setApkName("");
            }

        }
        return sysProcessList;
    }

    public static List<ProcessInfo> getProcess(Context context) {
        List<ProcessInfo> processList = new ArrayList<>();
//        ArrayList<ProcessInfo> userProcessList = new ArrayList<>();
//        ArrayList<ProcessInfo> sysProcessList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process:runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            String processName = process.processName;
            try {
                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{process.pid});
                int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty()*1024;
//                System.out.println(totalPrivateDirty);
                processInfo.setApkSize(totalPrivateDirty);
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                Drawable icon = applicationInfo.loadIcon(packageManager);
                if (icon!=null) {
                    processInfo.setIcon(icon);
                } else {
                    processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                }
                String apkName = applicationInfo.loadLabel(packageManager).toString();
                if (apkName!=null) {
                    processInfo.setApkName(apkName);
                } else {
                    processInfo.setApkName("");
                }

                int flags = applicationInfo.flags;
                if ((flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
//                //for system
                    processInfo.setUserProcess(false);
                    processList.add(processInfo);
                } else {
//                //for user
                    processInfo.setUserProcess(true);
//                    userProcessList.add(processInfo);
                    processList.add(processInfo);
                }
//                System.out.println(processInfo.toString());

            } catch (Exception e) {
                e.printStackTrace();
                processInfo.setIcon(context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon));
                processInfo.setApkName("");
            }

        }
        return processList;
    }

}
