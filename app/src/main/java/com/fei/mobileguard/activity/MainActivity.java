package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.service.MyAddressService;
import com.fei.mobileguard.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {


    private TextView tvVersion;
    private String versionName;
    private RelativeLayout rl;
    private int versionCode;
    private String versionDescription;
    private long startTime;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_UPDATE_OK:
                    showDialog();
                    break;
                case ENTER_HOME:
                    enterHomePage();
                    break;
            }
        }
    };
    public static final int APP_UPDATE_OK = 0;
    public static final int NETWORK_FAIL = 1;
    public static final int IO_FAIL = 2;
    public static final int JSON_FAIL = 3;
    public static final int ENTER_HOME = 4;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        initViews();
        copyDB("address.db");
        copyDB("antivirus.db");
        startService(new Intent(this, MyAddressService.class));

        createShortcut();

//        startService(new Intent(this, MyRocketService.class));

        if (mSharedPreferences.getBoolean("auto_update",true)) {
            checkUpdate();
        } else {
            Message message = mHandler.obtainMessage();
            message.what = ENTER_HOME;
            mHandler.sendMessageDelayed(message,2000);
        }
        startTime = System.currentTimeMillis();
//        HttpUtils httpUtils = new HttpUtils();
    }

    //method for create shortcut on android
    //need launcher permission
    private void createShortcut() {

        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //only one shortcut allowed
        intent.putExtra("duplicate",false);


        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"Mobile Guard");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), android.R.mipmap.sym_def_app_icon));

        Intent intentShortcut = new Intent();
        intentShortcut.setAction("com.fei.shortcut");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intentShortcut);

        sendBroadcast(intent);

    }

    //check update version from server
    private void checkUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime-startTime;
                //flash page for 2 seconds
                if (timeDifference<2000) {
                    try {
                        Thread.sleep(2000-timeDifference);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = mHandler.obtainMessage();
                try {

                    //server address
                    URL url = new URL("http://10.0.2.2:8888/update.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.connect();

                    if (urlConnection.getResponseCode()==200) {
                        //get input stream from server
                        InputStream is = urlConnection.getInputStream();
                        String resultJSON = StreamUtil.streamToString(is);
//                        System.out.println("JSON: "+resultJSON);
                        JSONObject jsonObject = new JSONObject(resultJSON);
                        int newVersionCode = jsonObject.getInt("versionCode");
                        versionDescription = jsonObject.getString("versionDescription");
                        if (newVersionCode>versionCode) {
                            msg.what = APP_UPDATE_OK;
                            mHandler.sendMessage(msg);
                        } else {
                            enterHomePage();
                        }
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = NETWORK_FAIL;
                    mHandler.sendMessage(msg);
                    enterHomePage();
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_FAIL;
                    mHandler.sendMessage(msg);
                    enterHomePage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_FAIL;
                    mHandler.sendMessage(msg);
                    enterHomePage();
                }

            }
        }).start();
    }

    private void enterHomePage() {
        Intent intent = new Intent(MainActivity.this,ActivityHome.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {

        tvVersion = (TextView) findViewById(R.id.tvVersion);
        rl = (RelativeLayout) findViewById(R.id.rl);
        AlphaAnimation animation = new AlphaAnimation(0.2f,1f);
        animation.setDuration(2000);
        rl.startAnimation(animation);
        versionName = getVersionName();
        versionCode = getVersionCode();
        tvVersion.setText("Version: " + versionName);
    }

    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Update Found");
        builder.setMessage(versionDescription);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("download start");
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHomePage();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHomePage();
            }
        });
        builder.show();
    }

    private void copyDB(String db) {

        InputStream is = null;
        FileOutputStream fos = null;

        try {
            File destFile = new File(getFilesDir(),db);
            if (destFile.exists()) {
                System.out.println(db+" is exist!!");
                return;
            }
            is = getAssets().open(db);
            fos = new FileOutputStream(destFile);

            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer))!=-1) {
                fos.write(buffer,0,length);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
