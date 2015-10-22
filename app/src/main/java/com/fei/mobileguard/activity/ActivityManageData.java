package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fei.mobileguard.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityManageData extends Activity {

    private ListView lvDataTraffic;
    private List<DataInfo> mDataInfoList;
    private PackageManager pm;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
                    MyDataAdapter adapter = new MyDataAdapter();
                    lvDataTraffic.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        mDataInfoList = new ArrayList<>();
        initViews();
        initData();
    }

    private void initViews() {
        lvDataTraffic = (ListView) findViewById(R.id.lvDataTraffic);

    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {

                pm = getPackageManager();
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                for (PackageInfo info:installedPackages){
                    DataInfo dataInfo = new DataInfo();
                    int appUid = info.applicationInfo.uid;

                    long uidReceive = TrafficStats.getUidRxBytes(appUid);
                    if (uidReceive<0) {uidReceive=0;}
                    dataInfo.uidReceive = uidReceive;
                    long uidSend = TrafficStats.getUidTxBytes(appUid);
                    if (uidSend<0){uidSend=0;}
                    dataInfo.uidSend = uidSend;
                    long uidTotal = uidReceive + uidSend;
                    dataInfo.uidTotal = uidTotal;
                    String appName = info.applicationInfo.loadLabel(pm).toString();
                    dataInfo.appName = appName;
                    Drawable appIcon = info.applicationInfo.loadIcon(pm);
                    dataInfo.appIcon = appIcon;
                    mDataInfoList.add(dataInfo);
//                    System.out.println("uid:" + appUid+","+"name: "+appName+","+"uidsend:"+uidSend+","+"uidreceive:"+uidReceive);
                }
                mHandler.sendEmptyMessage(123);
//                System.out.println(TrafficStats.getMobileRxBytes()+";"+TrafficStats.getMobileTxBytes());
            }
        }.start();

    }

    static class DataInfo {
        Drawable appIcon;
        long uidReceive;
        long uidSend;
        long uidTotal;
        String appName;
    }

    class MyDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null) {
                holder = new ViewHolder();
                convertView = View.inflate(ActivityManageData.this,R.layout.list_view_item_data_traffic,null);
                holder.ivDataIcon = (ImageView) convertView.findViewById(R.id.ivDataIcon);
                holder.tvDataTitle = (TextView) convertView.findViewById(R.id.tvDataTitle);
                holder.tvDataReceive = (TextView) convertView.findViewById(R.id.tvDataReceive);
                holder.tvDataSend = (TextView) convertView.findViewById(R.id.tvDataSend);
                holder.tvDataSize = (TextView) convertView.findViewById(R.id.tvDataSize);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivDataIcon.setBackground(mDataInfoList.get(position).appIcon);
            holder.tvDataTitle.setText(mDataInfoList.get(position).appName);
            holder.tvDataSize.setText("Total:"+Formatter.formatFileSize(ActivityManageData.this, mDataInfoList.get(position).uidTotal));
            holder.tvDataSend.setText("Send:"+Formatter.formatFileSize(ActivityManageData.this,mDataInfoList.get(position).uidSend));
            holder.tvDataReceive.setText("Received:"+Formatter.formatFileSize(ActivityManageData.this,mDataInfoList.get(position).uidReceive));

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivDataIcon;
        TextView tvDataTitle;
        TextView tvDataSize;
        TextView tvDataSend;
        TextView tvDataReceive;
    }
}
