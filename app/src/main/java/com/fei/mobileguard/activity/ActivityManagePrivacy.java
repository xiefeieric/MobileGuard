package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fei.mobileguard.R;
import com.fei.mobileguard.utils.UiUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ActivityManagePrivacy extends Activity implements View.OnClickListener {

    @ViewInject(R.id.etPrivacyPass)
    private EditText etPrivacyPass;
    @ViewInject(R.id.btn0)
    private Button btn0;
    @ViewInject(R.id.btn1)
    private Button btn1;
    @ViewInject(R.id.btn2)
    private Button btn2;
    @ViewInject(R.id.btn3)
    private Button btn3;
    @ViewInject(R.id.btn4)
    private Button btn4;
    @ViewInject(R.id.btn5)
    private Button btn5;
    @ViewInject(R.id.btn6)
    private Button btn6;
    @ViewInject(R.id.btn7)
    private Button btn7;
    @ViewInject(R.id.btn8)
    private Button btn8;
    @ViewInject(R.id.btn9)
    private Button btn9;
    @ViewInject(R.id.btnCls)
    private Button btnCls;
    @ViewInject(R.id.btnDel)
    private Button btnDel;
    @ViewInject(R.id.btnPrivacyOK)
    private Button btnPrivacyOK;
    private String mPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_privacy);
        ViewUtils.inject(this);
        initViews();
        initListeners();
    }

    private void initViews() {
        Intent intent = getIntent();
        if (intent!=null) {
            mPackageName = intent.getStringExtra("packageName");
//            System.out.println("package name: "+mPackageName);
        }
        etPrivacyPass.setInputType(View.LAYER_TYPE_NONE);
    }

    private void initListeners() {
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn0.getText().toString());
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn1.getText().toString());
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn2.getText().toString());
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn3.getText().toString());
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn4.getText().toString());
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn5.getText().toString());
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn6.getText().toString());
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn7.getText().toString());
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn8.getText().toString());
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText(etPrivacyPass.getText().toString() + btn9.getText().toString());
            }
        });
        btnCls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrivacyPass.setText("");
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = etPrivacyPass.getText().toString();
                if (pass.length() > 0) {
                    int newLength = pass.length() - 1;
                    String newPass = pass.substring(0, newLength);
                    etPrivacyPass.setText(newPass);
                }
            }
        });
        btnPrivacyOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrivacyOK:
                if (etPrivacyPass.getText().toString().equals("123")) {
                    Intent intent = new Intent();
                    intent.setAction("com.fei.StopWatchDog");
                    intent.putExtra("packageName",mPackageName);
                    sendBroadcast(intent);
                    finish();
                } else {
                    UiUtils.showToast(this, "Wrong Password!");
                    etPrivacyPass.setText("");
                }
                break;
        }
    }

    //press back and go straight back to home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }
}

