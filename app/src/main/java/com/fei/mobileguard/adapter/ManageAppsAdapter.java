package com.fei.mobileguard.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.model.AppInfo;

import java.util.List;

/**
 * Created by Fei on 29/09/15.
 */
public class ManageAppsAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppInfo> mUserAppInfoList;
    private List<AppInfo> mSysAppInfoList;

    public ManageAppsAdapter() {
    }

    public ManageAppsAdapter(Context context, List<AppInfo> appUserInfoList, List<AppInfo> appSysInfoList) {
        mContext = context;
        mUserAppInfoList = appUserInfoList;
        mSysAppInfoList = appSysInfoList;
    }

    @Override
    public int getCount() {
        return (mUserAppInfoList.size()+mSysAppInfoList.size());
    }

    @Override
    public Object getItem(int position) {

        if (position<mUserAppInfoList.size()) {
            return mUserAppInfoList.get(position);
        } else {
            return mSysAppInfoList.get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if (convertView==null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.list_view_item_manage_apps,null);
            holder.icon = (ImageView) view.findViewById(R.id.ivAppIcon);
            holder.apkName = (TextView) view.findViewById(R.id.tvApkName);
            holder.apkLocation = (TextView) view.findViewById(R.id.tvApkLocation);
            holder.apkSize = (TextView) view.findViewById(R.id.tvApkSize);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (position<mUserAppInfoList.size()) {
            holder.icon.setImageDrawable(mUserAppInfoList.get(position).getIcon());
            holder.apkName.setText(mUserAppInfoList.get(position).getApkName());
            holder.apkSize.setText(Formatter.formatFileSize(mContext,mUserAppInfoList.get(position).getApkSize()));
            if (mUserAppInfoList.get(position).isRam()) {
                holder.apkLocation.setText("RAM");
            } else {
                holder.apkLocation.setText("SD");
            }
            return view;
        } else {
            int location = position - mUserAppInfoList.size();
            holder.icon.setImageDrawable(mSysAppInfoList.get(location).getIcon());
            holder.apkName.setText(mSysAppInfoList.get(location).getApkName());
            holder.apkSize.setText(Formatter.formatFileSize(mContext,mSysAppInfoList.get(location).getApkSize()));
            if (mSysAppInfoList.get(location).isRam()) {
                holder.apkLocation.setText("RAM");
            } else {
                holder.apkLocation.setText("SD");
            }
            return view;
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView apkName;
        TextView apkSize;
        TextView apkLocation;
    }
}
