package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fei.mobileguard.R;
import com.fei.mobileguard.utils.UiUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActivityManageCache extends Activity implements View.OnClickListener {

    private PackageManager packageManager;
    private List<CacheInfo> mCacheList;
    private ListView lvCache;
    private Button btnCacheClean;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mAdapter = new MyCacheAdapter();
                        lvCache.setAdapter(mAdapter);
                    break;
            }
        }
    };
    private MyCacheAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cache);
        initViews();
    }

    private void initViews() {
        lvCache = (ListView) findViewById(R.id.lvCache);
        btnCacheClean = (Button) findViewById(R.id.btnCacheClean);
        btnCacheClean.setOnClickListener(this);
        mCacheList = new ArrayList<>();
        packageManager = getPackageManager();

        new Thread(){
            @Override
            public void run() {
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                for (PackageInfo info:installedPackages) {

                    getCacheSize(info);
//                    System.out.println("getcacheinfo");
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MyCacheAdapter adapter = new MyCacheAdapter();
//                        lvCache.setAdapter(adapter);
//                    }
//                });
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        Method[] methods = PackageManager.class.getMethods();
        for (Method method:methods) {
            if (method.getName().equals("freeStorageAndNotify")) {
                try {
                    method.invoke(packageManager,Integer.MAX_VALUE,new MyIPackageDataObserver());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyIPackageDataObserver extends IPackageDataObserver.Stub {

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            UiUtils.showToast(ActivityManageCache.this, "All Cleared!");
        }
    }

    class MyCacheAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mCacheList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCacheList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ImageView ivCacheDel = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ActivityManageCache.this,R.layout.list_view_item_cache,null);
                ivCacheDel = (ImageView) convertView.findViewById(R.id.ivCacheDel);

                holder.icon = (ImageView) convertView.findViewById(R.id.ivCacheIcon);
                holder.tvCacheName = (TextView) convertView.findViewById(R.id.tvCacheName);
                holder.tvCacheSize = (TextView) convertView.findViewById(R.id.tvCacheSize);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setBackground(mCacheList.get(position).icon);
            holder.tvCacheName.setText(mCacheList.get(position).appName);
            holder.tvCacheSize.setText(Formatter.formatFileSize(ActivityManageCache.this, mCacheList.get(position).cacheSize));
            if (ivCacheDel!=null) {
                ivCacheDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //intent which go to the app detail 
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:"+mCacheList.get(position).pkgName));
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView tvCacheName;
        TextView tvCacheSize;
    }

    private void getCacheSize(PackageInfo info) {

        //use reflection to invoke hide method
        try {
//            Class<?> clazz = getClassLoader().loadClass("PackageManager");
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, info.applicationInfo.packageName, new MyIPackageStatsObserver(info));
//            System.out.println("method invoke");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {

        private PackageInfo mInfo;

        public MyIPackageStatsObserver(PackageInfo info) {
            this.mInfo = info;
//            System.out.println("created");
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
//            System.out.println("ongetstatscompleted");
            long cacheSize = pStats.cacheSize;
            if (cacheSize>0) {
//                System.out.println("cache: "+ cacheSize);
                CacheInfo cacheInfo = new CacheInfo();
                Drawable icon = mInfo.applicationInfo.loadIcon(packageManager);
                cacheInfo.icon = icon;
                String appName = mInfo.applicationInfo.loadLabel(packageManager).toString();
                cacheInfo.appName = appName;
                cacheInfo.cacheSize = cacheSize;
                String packageName = mInfo.applicationInfo.packageName;
                cacheInfo.pkgName = packageName;
                mCacheList.add(cacheInfo);
                if (mAdapter!=null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
//            System.out.println("no cache");
        }
    }

    static class CacheInfo {
        Drawable icon;
        String appName;
        long cacheSize;
        String pkgName;
    }
}
