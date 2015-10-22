package com.fei.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class MyBootCompleteReceiver extends BroadcastReceiver {

    public MyBootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean setProtect = sp.getBoolean("setProtect", false);
        if (setProtect) {
            String simSaved = sp.getString("sim", null);
            if (!TextUtils.isEmpty(simSaved)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String simSerialNumber = tm.getSimSerialNumber();
                if (simSerialNumber.equals(simSaved)) {
                    System.out.println("SIM OK");
                } else {
//                    System.out.println("SIM CHANGED!!!!!");
                    String phone = sp.getString("phone", "");
//                    System.out.println(phone);

                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(phone,null,"SIM CHANGED",null,null);

                }
            }

        }
    }
}
