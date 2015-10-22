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
public class ViewSettingClick extends RelativeLayout {

    public static final String NAME_SPACE = "http://schemas.android.com/apk/res-auto";
    private TextView mTvAutoUpdateDesc;
    private TextView mTvAutoUpdateTitle;
    private String mDesc_on;

    public ViewSettingClick(Context context) {
        super(context);
        initView();
    }

    public ViewSettingClick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        String vs_title = attrs.getAttributeValue(NAME_SPACE, "vs_title");
        mDesc_on = attrs.getAttributeValue(NAME_SPACE, "desc_on");
        setTitle(vs_title);
        setDesc(mDesc_on);
    }

    public ViewSettingClick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewSettingClick(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_setting_click,this);
        mTvAutoUpdateTitle = (TextView) findViewById(R.id.tvAutoUpdateTitle);
        mTvAutoUpdateDesc = (TextView) findViewById(R.id.tvAutoUpdateDesc);
    }

    public void setTitle(String title) {
        mTvAutoUpdateTitle.setText(title);
    }

    public void setDesc(String desc) {
        mTvAutoUpdateDesc.setText(desc);
    }


}
