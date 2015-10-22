package com.fei.mobileguard.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fei.mobileguard.R;

/**
 * Created by Fei on 17/09/15.
 */
public class ViewSettingItem extends RelativeLayout {

    public static final String NAME_SPACE = "http://schemas.android.com/apk/res-auto";
    private TextView mTvAutoUpdateDesc;
    private CheckBox mCbStatus;
    private TextView mTvAutoUpdateTitle;
    private String mDesc_on;
    private String mDesc_off;

    public ViewSettingItem(Context context) {
        super(context);
        initView();
    }

    public ViewSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        String vs_title = attrs.getAttributeValue(NAME_SPACE, "vs_title");
        mDesc_on = attrs.getAttributeValue(NAME_SPACE, "desc_on");
        mDesc_off = attrs.getAttributeValue(NAME_SPACE, "desc_off");
        setTitle(vs_title);

    }

    public ViewSettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewSettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_setting_item,this);
        mTvAutoUpdateTitle = (TextView) findViewById(R.id.tvAutoUpdateTitle);
        mTvAutoUpdateDesc = (TextView) findViewById(R.id.tvAutoUpdateDesc);
        mCbStatus = (CheckBox) findViewById(R.id.cbStatus);
    }

    public void setTitle(String title) {
        mTvAutoUpdateTitle.setText(title);
    }

    public void setDesc(String desc) {
        mTvAutoUpdateDesc.setText(desc);
    }

    public boolean isChecked() {
        return mCbStatus.isChecked();
    }

    public void setChecked(Boolean choice){
        mCbStatus.setChecked(choice);

        if (choice) {
            setDesc(mDesc_on);
        } else {
            setDesc(mDesc_off);
        }
    }
}
