package com.fei.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.fei.mobileguard.db.dao.BlackListDao;

public class MyBlackListService extends Service {

    private TelephonyManager mTm;
    private BlackListDao mBlackListDao;
    private InnerReceiver mReceiver;

    public MyBlackListService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mReceiver, filter);
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mBlackListDao = new BlackListDao(this);
        MyPhoneStateListener listener = new MyPhoneStateListener();
        mTm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
//                    System.out.println("try to block");
                    boolean blackNumber = mBlackListDao.findNumber(incomingNumber);
                    if (blackNumber) {
                        Toast.makeText(MyBlackListService.this,"This number will be blocked...",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object object:pdus) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = msg.getOriginatingAddress();
                if (mBlackListDao.findNumber(originatingAddress)) {
                    abortBroadcast();
                    Toast.makeText(MyBlackListService.this,"sms was blocked",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
