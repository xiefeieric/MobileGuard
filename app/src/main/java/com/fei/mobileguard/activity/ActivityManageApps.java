package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.mobileguard.R;
import com.fei.mobileguard.adapter.ManageAppsAdapter;
import com.fei.mobileguard.business.AppManage;
import com.fei.mobileguard.model.AppInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


public class ActivityManageApps extends Activity implements View.OnClickListener {

    @ViewInject(R.id.tvRam)
    private TextView tvRam;
    @ViewInject(R.id.tvSD)
    private TextView tvSD;
    @ViewInject(R.id.lvManageApps)
    private ListView lvManageApps;
    @ViewInject(R.id.tvAppType)
    private TextView tvAppType;

//    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mUserAppInfoList;
    private List<AppInfo> mSysAppInfoList;
    private PopupWindow mPopupWindow;
    private AppInfo mClickItem;
    private ManageAppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_apps);
//        mAppInfoList = new ArrayList<>();
        initViews();
        initListeners();
    }

    private void initViews() {
        ViewUtils.inject(this);
        long freeSpaceRam = Environment.getDataDirectory().getFreeSpace();
        long freeSpaceSD = Environment.getExternalStorageDirectory().getFreeSpace();
        tvRam.setText("Free RAM: "+ (Formatter.formatFileSize(this,freeSpaceRam)));
        tvSD.setText("Free SD: "+(Formatter.formatFileSize(this,freeSpaceSD)));

//        mAppInfoList = AppManage.getApps(this);
        new Thread(){
            @Override
            public void run() {
                mUserAppInfoList = AppManage.getUserApps(ActivityManageApps.this);
                mSysAppInfoList = AppManage.getSysApps(ActivityManageApps.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ManageAppsAdapter(ActivityManageApps.this, mUserAppInfoList, mSysAppInfoList);
                        lvManageApps.setAdapter(mAdapter);
                    }
                });
            }
        }.start();

    }

    private void initListeners() {
        lvManageApps.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mPopupWindow!=null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
//                System.out.println(firstVisibleItem);
                if (mUserAppInfoList!=null && firstVisibleItem < mUserAppInfoList.size()) {
                    tvAppType.setText("User Apps");
                } else if (mUserAppInfoList!=null && firstVisibleItem >= mUserAppInfoList.size()) {
                    tvAppType.setText("System Apps");
                }
            }
        });

        lvManageApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mPopupWindow!=null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }

                mClickItem = (AppInfo) lvManageApps.getItemAtPosition(position);

                View popView = View.inflate(ActivityManageApps.this,R.layout.popup_activity_manage,null);
                LinearLayout llDeleteApp = (LinearLayout) popView.findViewById(R.id.llDeleteApp);
                llDeleteApp.setOnClickListener(ActivityManageApps.this);
                LinearLayout llRunApp = (LinearLayout) popView.findViewById(R.id.llRunApp);
                llRunApp.setOnClickListener(ActivityManageApps.this);
                LinearLayout llShareApp = (LinearLayout) popView.findViewById(R.id.llShareApp);
                llShareApp.setOnClickListener(ActivityManageApps.this);
                mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] location = new int[2];
                view.getLocationInWindow(location);
                mPopupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 100, location[1]);
                ScaleAnimation anim = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                AlphaAnimation anim2 = new AlphaAnimation(0.2f,1.0f);
                anim2.setDuration(500);
                anim.setDuration(500);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(anim);
                animationSet.addAnimation(anim2);
                popView.startAnimation(animationSet);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow!=null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llDeleteApp:
//                System.out.println("delete");
                if (mClickItem.isUserApp()) {
                    Intent intentDelete = new Intent();
                    intentDelete.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
                    intentDelete.addCategory("android.intent.category.DEFAULT");
                    intentDelete.setData(Uri.parse("package:" + mClickItem.getApkPackageName()));
//                    startActivity(intentDelete);
                    startActivityForResult(intentDelete, 123);
                    mPopupWindow.dismiss();
                } else {
                    Toast.makeText(this,"To delete system app, ROOT permission is needed!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.llRunApp:
//                System.out.println("run");
                if (mClickItem!=null) {
                    Intent intentRun = this.getPackageManager().getLaunchIntentForPackage(mClickItem.getApkPackageName());
                    startActivity(intentRun);
                    mPopupWindow.dismiss();
                }
                break;

            case R.id.llShareApp:
//                System.out.println("share");
                Intent intentShare = new Intent();
                intentShare.setAction("android.intent.action.SEND");
                intentShare.addCategory("android.intent.category.DEFAULT");
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, "Share an app："+mClickItem.getApkName()+", download：https://play.google.com/store/apps/details?id="+mClickItem.getApkPackageName());
                startActivity(intentShare);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==123) {
//            System.out.println("app deleted");
            mUserAppInfoList.remove(mClickItem);

            if (mAdapter!=null) {
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter = new ManageAppsAdapter(this,mUserAppInfoList,mSysAppInfoList);
                lvManageApps.setAdapter(mAdapter);
            }
        }
    }


}
