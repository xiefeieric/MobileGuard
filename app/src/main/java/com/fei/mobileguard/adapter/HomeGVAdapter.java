package com.fei.mobileguard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fei.mobileguard.R;

/**
 * Created by Fei on 16/09/15.
 */
public class HomeGVAdapter extends BaseAdapter {

    private Context mContext;

    private String[] names = new String[]{"Safe","Call","Apps","Process","Data","Virus","Cache","Advanced","Settings"};
    private int[] pics = new int[] {R.drawable.home_safe, R.drawable.home_callmsgsafe,R.drawable.home_apps,
                                    R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                                    R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};

    public HomeGVAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView==null) {
            view = View.inflate(mContext,R.layout.gv_list_item,null);
        } else {
            view = convertView;
        }

        ImageView iv = (ImageView) view.findViewById(R.id.ivGvItem);
        iv.setImageResource(pics[position]);
        TextView tv = (TextView) view.findViewById(R.id.tvGvName);
        tv.setText(names[position]);

        return view;
    }
}
