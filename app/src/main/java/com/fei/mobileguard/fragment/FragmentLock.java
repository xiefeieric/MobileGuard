package com.fei.mobileguard.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.business.AppManage;
import com.fei.mobileguard.db.dao.AppLockDao;
import com.fei.mobileguard.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLock extends Fragment {


    private List<AppInfo> mApps;
    private List<AppInfo> mAppsLock;
    private AppLockDao mAppLockDao;
    private MyFragmentAdapter mAdapter;
    private ListView mLvLock;
    private TextView mTvLockCount;

    public FragmentLock() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppsLock = new ArrayList<>();
        mAppLockDao = new AppLockDao(getActivity());
        new Thread(){
            @Override
            public void run() {
                mApps = AppManage.getApps(getActivity());
                for (AppInfo info:mApps) {
                    String apkPackageName = info.getApkPackageName();
                    boolean isLock = mAppLockDao.findLock(apkPackageName);
                    if (isLock) {
                        mAppsLock.add(info);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new MyFragmentAdapter();
                        mLvLock.setAdapter(mAdapter);
                    }
                });

            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lock, null);

        mLvLock = (ListView) view.findViewById(R.id.lvLock);
        mTvLockCount = (TextView)view.findViewById(R.id.tvLockCount);

//        if (mAdapter!=null) {
//            mLvLock.setAdapter(mAdapter);
//        }

        return view;
    }

    class MyFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            mTvLockCount.setText("Locked Apps (" + mAppsLock.size() + ")");
            return mAppsLock.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppsLock.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null) {
                holder = new ViewHolder();
                convertView = View.inflate(getActivity(),R.layout.list_view_item_fragment_unlock,null);
                holder.ivLockAppIcon = (ImageView) convertView.findViewById(R.id.ivUnlockAppIcon);
                holder.tvLockAppName = (TextView) convertView.findViewById(R.id.tvUnlockAppName);
                holder.ivLockStatus = (ImageView) convertView.findViewById(R.id.ivUnlockStatus);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final View view = convertView;

            holder.ivLockAppIcon.setBackground(mAppsLock.get(position).getIcon());
            holder.tvLockAppName.setText(mAppsLock.get(position).getApkName());
            holder.ivLockStatus.setBackgroundResource(R.drawable.fragment_unlock_selector);

            holder.ivLockStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println("fragment unlock on click");
                    new Thread() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
                                            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                                    anim.setDuration(500);
                                    view.startAnimation(anim);
                                }
                            });

                            String appPackageName = mAppsLock.get(position).getApkPackageName();
                            mAppLockDao.deleteLock(appPackageName);
                            SystemClock.sleep(500);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAppsLock.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });


                        }
                    }.start();


                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivLockAppIcon;
        TextView tvLockAppName;
        ImageView ivLockStatus;
    }


}
