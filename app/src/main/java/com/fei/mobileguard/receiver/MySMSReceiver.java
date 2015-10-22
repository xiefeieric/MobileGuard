package com.fei.mobileguard.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.fei.mobileguard.R;
import com.fei.mobileguard.service.MyLocationService;

public class MySMSReceiver extends BroadcastReceiver {

    private DevicePolicyManager mPolicyManager;

    public MySMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mPolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object object:pdus) {
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = msg.getOriginatingAddress();
            String messageBody = msg.getMessageBody();
            String savedPhone = sp.getString("phone", "");
//            System.out.println(originatingAddress+" : "+messageBody+" : "+savedPhone);
            if (savedPhone.equals(originatingAddress) && messageBody.equals("#*music*#")){
                abortBroadcast();
                System.out.println("Play music");
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(1f,1f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } else  if (savedPhone.equals(originatingAddress) && messageBody.equals("#*location*#")){
                abortBroadcast();
                System.out.println("Location");
                context.startService(new Intent(context, MyLocationService.class));
                String location = sp.getString("location","");
                System.out.println(location);
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(savedPhone,null,location,null,null);
            } else if (savedPhone.equals(originatingAddress) && messageBody.equals("#*lockscreen*#")) {
                abortBroadcast();
                System.out.println("Lock Screen");
                mPolicyManager.lockNow();

            } else if (savedPhone.equals(originatingAddress) && messageBody.equals("#*wipedata*#")) {
                abortBroadcast();
                System.out.println("Wipe Data");
                mPolicyManager.wipeData(0);
            }
        }
    }
}
