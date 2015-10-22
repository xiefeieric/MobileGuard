package com.fei.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.mobileguard.R;
import com.fei.mobileguard.db.dao.AddressDao;

public class MyAddressService extends Service {

    private TelephonyManager mTm;
    private MyPhoneListener mListener;
    private WindowManager mMWM;
//    private TextView mView;
    private View mInflate;
    private MyOutCallReceiver mReceiver;
    private LinearLayout mLinearLayout;
    private WindowManager.LayoutParams mParams;
    private SharedPreferences sp;

    public MyAddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config",MODE_PRIVATE);

        mReceiver = new MyOutCallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mReceiver,intentFilter);

        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mListener = new MyPhoneListener();
        mTm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTm.listen(mListener,PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(mReceiver);
    }

    class MyPhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    System.out.println("incoming call");
//                    Toast.makeText(MyAddressService.this,address,Toast.LENGTH_LONG).show();
                    showCustomToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mMWM!=null && mInflate!=null) {
                        mMWM.removeView(mInflate);
                    }
                    break;
            }

        }
    }

    public void showCustomToast(String text) {

        mMWM = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int width = mMWM.getDefaultDisplay().getWidth();
        int height = mMWM.getDefaultDisplay().getHeight();
        mParams = new WindowManager.LayoutParams();
        int lastX = sp.getInt("lastX", width / 2);
        int lastY = sp.getInt("lastY", height / 2);
        mParams.x = lastX;
        mParams.y = lastY;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mInflate = View.inflate(this, R.layout.address_toast, null);
        mLinearLayout = (LinearLayout) mInflate.findViewById(R.id.llAddressToast);

        int style = sp.getInt("style_alert", 0);
        int[] styles = new int[] {R.drawable.function_greenbutton_normal,R.drawable.call_locate_blue};
        TextView tvAddressToast = (TextView) mInflate.findViewById(R.id.tvAddressToast);
        tvAddressToast.setText(text);
        mLinearLayout.setBackgroundResource(styles[style]);
        mMWM.addView(mInflate, mParams);
        MyOnTouchListener listener = new MyOnTouchListener();
        mInflate.setOnTouchListener(listener);
    }

    class MyOnTouchListener implements View.OnTouchListener {

        private int mStartX;
        private int mStartY;
        private int mOffsetX;
        private int mOffsetY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = (int) event.getRawX();
                    mStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getRawX();
                    int moveY = (int) event.getRawY();

                    mOffsetX = moveX - mStartX;
                    mOffsetY = moveY - mStartY;

                    mParams.x += mOffsetX;
                    mParams.y += mOffsetY;
                    mMWM.updateViewLayout(v,mParams);

                    mStartX = moveX;
                    mStartY = moveY;
                    break;
                case MotionEvent.ACTION_UP:
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("lastX",mParams.x);
                    editor.putInt("lastY",mParams.y);
                    editor.commit();

                    break;

            }

            return true;
        }
    }

    class MyOutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resultData = getResultData();
            String address = AddressDao.getAddress(resultData);
            showCustomToast(address);
        }
    }

}
