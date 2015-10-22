package com.fei.mobileguard.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.model.AppInfo;
import com.fei.mobileguard.model.ProcessInfo;

import java.util.List;

/**
 * Created by Fei on 29/09/15.
 */
public class ManageProcessAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProcessInfo> mUserProcessInfoList;
    private List<ProcessInfo> mSysProcessInfoList;

    public ManageProcessAdapter(Context context, List<ProcessInfo> processUserInfoList, List<ProcessInfo> processSysInfoList) {
        mContext = context;
        mUserProcessInfoList = processUserInfoList;
        mSysProcessInfoList = processSysInfoList;
    }

    @Override
    public int getCount() {
        return (mUserProcessInfoList.size()+mSysProcessInfoList.size());
    }

    @Override
    public Object getItem(int position) {

        if (position<mUserProcessInfoList.size()) {
            return mUserProcessInfoList.get(position);
        } else {
            return mSysProcessInfoList.get(position);
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
            view = View.inflate(mContext, R.layout.list_view_item_manage_process,null);
            holder.icon = (ImageView) view.findViewById(R.id.ivProcessIcon);
            holder.apkName = (TextView) view.findViewById(R.id.tvProcessName);
            holder.apkSize = (TextView) view.findViewById(R.id.tvProcessSpace);
            holder.cbProcessState = (CheckBox) view.findViewById(R.id.cbProcessCheck);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (position<mUserProcessInfoList.size()) {
            holder.icon.setImageDrawable(mUserProcessInfoList.get(position).getIcon());
            holder.apkName.setText(mUserProcessInfoList.get(position).getApkName());
            holder.apkSize.setText("Used: "+Formatter.formatFileSize(mContext,mUserProcessInfoList.get(position).getApkSize()));
            holder.cbProcessState.setChecked(mUserProcessInfoList.get(position).isCbStatus());
            return view;
        } else {
            int location = position - mUserProcessInfoList.size();
            holder.icon.setImageDrawable(mSysProcessInfoList.get(location).getIcon());
            holder.apkName.setText(mSysProcessInfoList.get(location).getApkName());
            holder.apkSize.setText("Used: "+Formatter.formatFileSize(mContext,mSysProcessInfoList.get(location).getApkSize()));
            holder.cbProcessState.setChecked(mSysProcessInfoList.get(location).isCbStatus());
            return view;
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView apkName;
        TextView apkSize;
        CheckBox cbProcessState;
    }
}
