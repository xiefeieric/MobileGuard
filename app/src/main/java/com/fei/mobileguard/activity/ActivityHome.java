package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.fei.mobileguard.R;
import com.fei.mobileguard.adapter.HomeGVAdapter;
import com.fei.mobileguard.utils.MD5Utils;

public class ActivityHome extends Activity {

    private GridView gvHome;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSp = getSharedPreferences("config",MODE_PRIVATE);
        initViews();
        initListeners();
    }

    private void initListeners() {
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("position: "+position);
                switch (position) {
                    case 0:
                        //to check password set or not

                        //password not set
                        String password = mSp.getString("password", null);
                        if (password != null) {
                            showDialogSafeLogin();
                        } else {
                            showDialogPass();
                        }

                        break;

                    case 1:
                        startActivity(new Intent(ActivityHome.this,ActivityManageBlackList.class));
                        break;

                    case 2:
                        startActivity(new Intent(ActivityHome.this,ActivityManageApps.class));
                        break;
                    case 3:
                        startActivity(new Intent(ActivityHome.this,ActivityManageProcess.class));
                        break;
                    case 4:
                        startActivity(new Intent(ActivityHome.this,ActivityManageData.class));
                        break;
                    case 5:
                        startActivity(new Intent(ActivityHome.this,ActivityVirusAnti.class));
                        break;
                    case 6:
                        startActivity(new Intent(ActivityHome.this,ActivityManageCache.class));
                        break;
                    case 7:
                        startActivity(new Intent(ActivityHome.this,ActivityAdvanceTool.class));
                        break;

                    case 8:
                        Intent intent = new Intent(ActivityHome.this, ActivitySetting.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    private void showDialogSafeLogin() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_safe_login, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        dialog.setView(view);
        dialog.show();

        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    String savedPassword = mSp.getString("password",null);
                    if (MD5Utils.encode(password).equals(savedPassword)) {
                        //login success
                        Intent intent = new Intent(ActivityHome.this,ActivityLostFind.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ActivityHome.this,"Passwords is Wrong!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityHome.this,"Password cannot be empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_password, null);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.etPasswordConfirm);
        dialog.setView(view);
        dialog.show();

        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String password = etPassword.getText().toString();
                String confirmPassword = etPasswordConfirm.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
                    if (password.equals(confirmPassword)) {
                        //login success
                        mSp.edit().putString("password", MD5Utils.encode(password)).commit();
                        dialog.dismiss();
                        Intent intent = new Intent(ActivityHome.this,ActivityLostFind.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ActivityHome.this,"Passwords need to be same!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityHome.this,"Password cannot be empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initViews() {
        gvHome = (GridView) findViewById(R.id.gvHome);
        HomeGVAdapter adapter = new HomeGVAdapter(this);
        gvHome.setAdapter(adapter);

    }


}
